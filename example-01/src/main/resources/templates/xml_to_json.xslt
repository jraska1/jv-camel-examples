<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="text"/>
    <xsl:template match="/Clickbox">
{
  "oid": "clickbox:<xsl:value-of select="cgmnumber"/>",
  "name": "<xsl:value-of select="translate(organisationname | zarizeni, '&quot;', '')"/>",
  "dn": null,
  "ico": "<xsl:value-of select="ico"/>",
  <xsl:variable name="addr"><xsl:value-of select="pracoviste"/>, <xsl:value-of select="street"/><xsl:text xml:space="preserve"> </xsl:text><xsl:value-of select="housenumber"/>, <xsl:value-of select="postalcode"/><xsl:text xml:space="preserve"> </xsl:text><xsl:value-of select="city"/></xsl:variable>
  "addr": "<xsl:value-of select="translate($addr, '&quot;', '')"/>",
  "icz": "<xsl:value-of select="icz"/>",
  "phone": "<xsl:value-of select="phoneoffice | phonemobile"/>",
  "email": "<xsl:value-of select="email"/>",
  "icp": [
<xsl:for-each select="icp">
"<xsl:value-of select="."/>"<xsl:choose><xsl:when test="position() != last()">,</xsl:when></xsl:choose>
</xsl:for-each>
],
  "contacts": [
<xsl:for-each select="doctor">{
"name": "<xsl:value-of select="firstname"/><xsl:text xml:space="preserve"> </xsl:text><xsl:value-of select="lastname"/>",
"phone": "<xsl:value-of select="phone"/>"
}<xsl:choose><xsl:when test="position() != last()">,</xsl:when></xsl:choose>
</xsl:for-each>
  ]
}
    </xsl:template>
</xsl:stylesheet>