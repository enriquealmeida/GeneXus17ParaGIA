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
      <xsl:when test="@type = 'DVelop.WorkWithPlusUtilities' or @type = 'DVelop.WorkWithPlusUtilities_F5'">
        <xsl:call-template name="RenderWorkWithPlusUtilities"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- DVelop.WorkWithPlusUtilities design render -->
  <xsl:template name="RenderWorkWithPlusUtilities">
    <xsl:variable name="UCPath" select="gxca:GetMyPath()" />
    <xsl:value-of select="user:Render($UCPath)" disable-output-escaping="yes" />
  </xsl:template>
</xsl:stylesheet>
