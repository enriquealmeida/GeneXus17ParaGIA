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
      <xsl:when test="@type = 'SDSwitch'">
        <xsl:call-template name="RenderSwitch"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

 
  <xsl:template name="RenderSwitch">
   <span atomicselection="true">
      <xsl:call-template name="AddStyleAttribute"/>
      <img style='width:105px;height:38px;'>
        <xsl:attribute name="src">
          <xsl:value-of select='gxca:GetMyPath()'/>
							<xsl:text>\switchPlaceHolder.png</xsl:text>
        </xsl:attribute>
      </img>
    </span>


  </xsl:template>


  <!-- Helpers Templates -->

  <xsl:template name="AddStyleAttribute" >
    <xsl:variable name="Style">
      <xsl:text>width: </xsl:text>
      <xsl:value-of select="gxca:GetPropertyValueInt('Width')"/>
      <xsl:text>; </xsl:text>
      <xsl:text>height: </xsl:text>
      <xsl:value-of select="gxca:GetPropertyValueInt('Height')"/>
      <xsl:text>; </xsl:text>

    </xsl:variable>
    <xsl:attribute name="style">
      <xsl:value-of select="$Style"/>
    </xsl:attribute>
  </xsl:template>
</xsl:stylesheet>
