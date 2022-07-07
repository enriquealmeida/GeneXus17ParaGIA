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
      <xsl:when test="@type = 'DVelop.Bootstrap.SidebarMenu'">
        <xsl:call-template name="RenderDVelop.Bootstrap.SidebarMenu"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- Dvelop.ExtUC.SidebarMenu design render -->
  <!-- ///////////////////  Implement your render here  ///////////////////-->
  <xsl:template name="RenderDVelop.Bootstrap.SidebarMenu">
   <span atomicselection="true">   
		<xsl:call-template name="AddStyleAttribute"/>
		<img alt="DVelop Bootstrap SidebarMenu control">
			<xsl:attribute name="src">
			  <xsl:value-of select='gxca:GetMyPath()'/>
			 <xsl:text>/Bootstrap/SidebarMenu/SideBarMenuExample.gif</xsl:text>
			</xsl:attribute>
		</img> 
      </span>   
  </xsl:template>


  <!-- Helpers Templates -->

  <xsl:template name="AddStyleAttribute" >
    <xsl:variable name="Style">
      <xsl:text>border-style: solid;	border-width: 2px;</xsl:text>
    </xsl:variable>
    <xsl:attribute name="style">
      <xsl:value-of select="$Style"/>
    </xsl:attribute>
  </xsl:template>
</xsl:stylesheet>
