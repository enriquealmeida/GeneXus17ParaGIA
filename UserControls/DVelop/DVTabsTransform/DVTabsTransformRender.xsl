<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:msxml="urn:schemas-microsoft-com:xslt"
	xmlns:gx="urn:shemas-artech-com:gx"
	exclude-result-prefixes="msxml gx"
	xmlns:gxca="urn:GXControlAdap"
	xmlns:user="urn:UserRenderHelper">
  <xsl:output method="html"/>
  <xsl:template match="/" >
    <xsl:apply-templates select="/GxControl"/>
  </xsl:template>
  <xsl:template match="GxControl">
    <xsl:choose>
      <xsl:when test="@type = 'DVelop.DVTabsTransform'">
        <xsl:call-template name="RenderDVTabsTransform"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- DVelop.DVTabsTransform design render -->
  <xsl:template name="RenderDVTabsTransform">
    <xsl:variable name="UCPath" select="gxca:GetMyPath()" />
    <xsl:value-of select="user:Render($UCPath)" disable-output-escaping="yes" />
  </xsl:template>
</xsl:stylesheet>
