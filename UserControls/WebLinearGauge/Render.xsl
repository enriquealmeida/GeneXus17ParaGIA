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
      <xsl:when test="@type = 'WebLinearGauge'">
        <xsl:call-template name="Render"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="Render">
	 <img border="0">
		<xsl:attribute name ="src">
		  <xsl:value-of select='gxca:GetMyPath()'/>
		  <xsl:text>\WebLinearGauge-</xsl:text>
		  <xsl:value-of select="gxca:GetStringPropertyValue('Style')"/>
		  <xsl:text>.PNG</xsl:text>
		</xsl:attribute>
	 </img>
  </xsl:template>
</xsl:stylesheet>
