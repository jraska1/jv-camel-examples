package cz.dsw.camel_examples;

import cz.dsw.camel_examples.entity.HealthCareProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> logger.info("*** Hello World, greetings from Dwarf ***");
    }

    @Bean
    public Map<URI, HealthCareProvider> configStorage() {
        return new HashMap<>();
    }
}
