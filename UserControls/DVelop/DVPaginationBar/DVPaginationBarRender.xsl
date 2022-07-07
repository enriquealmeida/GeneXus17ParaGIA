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
      <xsl:when test="@type = 'DVelop.DVPaginationBar'">
        <xsl:call-template name="RenderDVPaginationBar"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- DVelop.DVPaginationBar design render -->
  <xsl:template name="RenderDVPaginationBar">
   <span atomicselection="true">
      <img alt="Tooltip control">
		<xsl:attribute name="src">
			<xsl:value-of select='gxca:GetMyPath()'/>
			<xsl:text>/DVPaginationBar/DVPaginationBar.gif</xsl:text>
		</xsl:attribute>
	</img>      
   </span>	
  </xsl:template>  
</xsl:stylesheet>
