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
      <xsl:when test="@type = 'Sparkline'">
        <xsl:call-template name="Sparkline"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="Sparkline">
    <div atomicselection="true">
		<img alt="Sparkline">
			<xsl:attribute name="src">
				<xsl:value-of select='gxca:GetMyPath()'/>
				<xsl:text>\Sparkline.png</xsl:text>
			</xsl:attribute>
			<xsl:attribute name="style">
				<xsl:text>margin-right:4px;float:left;</xsl:text>
			</xsl:attribute>
		</img>
	</div>
  </xsl:template>
</xsl:stylesheet>
