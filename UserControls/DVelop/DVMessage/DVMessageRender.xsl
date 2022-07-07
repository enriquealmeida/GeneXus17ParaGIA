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
      <xsl:when test="@type = 'DVelop.DVMessage'">
        <xsl:call-template name="RenderDVMessage"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
  
  <!-- dvMessage design render -->
  <xsl:template name="RenderDVMessage">
   <span atomicselection="true">
      <img alt="DVMessage">
		<xsl:attribute name="src">
			<xsl:value-of select='gxca:GetMyPath()'/>
			<xsl:text>/DVMessage/DVMessage.gif</xsl:text>
		</xsl:attribute>
	</img>      
   </span>	
  </xsl:template>

</xsl:stylesheet>