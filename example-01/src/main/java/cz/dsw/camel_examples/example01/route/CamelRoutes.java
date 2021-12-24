package cz.dsw.camel_examples.example01.route;

import cz.dsw.camel_examples.example01.entity.HealthCareProvider;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.dsl.HttpEndpointBuilderFactory;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.processor.aggregate.AbstractListAggregationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CamelRoutes extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(CamelRoutes.class);

    @Value("classpath:templates/catalog_request.xml")
    private Resource template;

    @Autowired
    private Map<URI, HealthCareProvider> configStorage;

    @Override
    public void configure() {

        from("scheduler://main?delay={{scheduler.delay}}&repeatCount={{scheduler.repeatCount}}").routeId("main-route")
                .process(exchange -> {
                    exchange.getMessage().setBody(template.getInputStream());
                    exchange.getIn().setHeader(Exchange.HTTP_METHOD, HttpEndpointBuilderFactory.HttpMethods.POST);
                    exchange.getIn().setHeader("Content-Type", "application/xml;charset=UTF-8");
                    exchange.getIn().setHeader("Accept", "application/xml");
                    exchange.getIn().setHeader("Accept-Charset", "UTF-8");
                })
                .to("{{catalog.url}}")
                .setBody(xpath("/catalogInfo/catalogs/catalog/items/item/url", String.class))
                .process(exchange ->
                        exchange.getIn().setHeader(Exchange.HTTP_METHOD, HttpEndpointBuilderFactory.HttpMethods.GET))
                .toD("${body}")
                .unmarshal().zipFile()
                .split(xpath("/Participants/Participant/Clickbox"), new AbstractListAggregationStrategy<URI>() {
                    @Override
                    public URI getValue(Exchange exchange) {
                        return exchange.getIn().getBody(URI.class);
                    }
                }).streaming()
                    .choice()
                        .when(xpath("/Clickbox/cgmnumber"))
                            .to("xslt:templates/xml_to_json.xslt")
                            .unmarshal(new JacksonDataFormat(HealthCareProvider.class))
                            .process(exchange -> {
                                HealthCareProvider info = exchange.getMessage().getBody(HealthCareProvider.class);
                                configStorage.put(info.getOid(), info);
                                exchange.getMessage().setBody(info.getOid());
                            })
                        .endChoice()
                        .otherwise()
                            .setBody().simple("${null}")
                        .endChoice()
                    .end()
                .end()
                .process(exchange -> {
                    Set<URI> removeKeys = new HashSet<>(configStorage.keySet());
                    exchange.getMessage().getBody(List.class).forEach(removeKeys::remove);
                    removeKeys.forEach(oid -> configStorage.remove(oid));
                });

        restConfiguration().port("9091").bindingMode(RestBindingMode.json);
        rest("/storage").produces("application/json")
                .get()
                    .route()
                        .process(exchange -> exchange.getMessage().setBody(configStorage));
    }
}