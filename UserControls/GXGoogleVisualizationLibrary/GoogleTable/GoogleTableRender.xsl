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
      <xsl:when test="@type = 'GoogleTable'">
        <xsl:call-template name="RenderGoogleTable"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- GoogleTable design render -->
  <!-- ///////////////////  Implement your render here  ///////////////////-->
  <xsl:template name="RenderGoogleTable">
    <img atomicselection="true">
      <xsl:attribute name="src">
					<xsl:value-of select='gxca:GetMyPath()'/>
					<xsl:text>\GoogleTable\GoogleTable.PNG</xsl:text>
			</xsl:attribute>
		</img>
  </xsl:template>
</xsl:stylesheet>
