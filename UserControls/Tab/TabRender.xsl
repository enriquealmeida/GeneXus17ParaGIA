<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:msxml="urn:schemas-microsoft-com:xslt"
	xmlns:gx="urn:shemas-artech-com:gx"
	exclude-result-prefixes="msxml gx"
	xmlns:gxca="urn:GXControlAdap">
  <xsl:output method="html"/>
  <xsl:template match="/" >
    <xsl:apply-templates select="/GxControl"/>
  </xsl:template>
  <xsl:template match="GxControl">
    <xsl:choose>
      <xsl:when test="@type = 'Tab' or @type = 'BasicTab'">
        <xsl:call-template name="RenderTab"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- Tab design render -->
  <xsl:template name="RenderTab">
    <div atomicselection="true" style="border:1px solid black">
      <xsl:call-template name="RenderOneTab">
        <xsl:with-param name="count" select="gxca:GetPropValue('PageCount')"/>
      </xsl:call-template>
    </div>
  </xsl:template>

  <xsl:template name="RenderOneTab">
    <xsl:param name="count" select="1"/>
    <xsl:if test="$count > 0">
      <xsl:call-template name="RenderOneTab">
        <xsl:with-param name="count" select="$count - 1"/>
      </xsl:call-template>

      <h2>
        <xsl:attribute name="containerId">title<xsl:value-of select="$count"/></xsl:attribute>
        <xsl:text>Title</xsl:text><xsl:value-of select="$count"/>
      </h2>
      <div>
        <xsl:attribute name="containerId">panel<xsl:value-of select="$count"/></xsl:attribute>
        <xsl:text>Panel</xsl:text><xsl:value-of select="$count"/>
      </div>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>