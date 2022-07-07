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
      <xsl:when test="@type = 'QueryViewer'">
        <xsl:call-template name="RenderQueryViewer"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>
 <!-- QueryViewer design render -->
  <!-- ///////////////////  Implement your render here  ///////////////////-->
  <xsl:template name="RenderQueryViewer">
    <span atomicselection="true">
      <xsl:call-template name="AddStyleAttribute"/>
      <img>
        <xsl:attribute name ="src">
          <xsl:value-of select='gxca:GetMyPath()'/>
          <xsl:choose>
            <xsl:when test="gxca:GetStringPropertyValue('DesignRenderOutputType') = ''">
              <xsl:text>\images\PivotTable.png</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                  <xsl:text>\images\</xsl:text>
                  <xsl:value-of select="gxca:GetStringPropertyValue('DesignRenderOutputType')"/>
              <xsl:text>.png</xsl:text>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:attribute>
        <xsl:attribute name ="width">
          <xsl:value-of select="gxca:GetPropertyValueInt('Width')"/>
        </xsl:attribute>
        <xsl:attribute name ="height">
          <xsl:value-of select="gxca:GetPropertyValueInt('Height')"/>
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
      <xsl:text>border-style: solid;	border-width: 0px;</xsl:text>
    </xsl:variable>
    <xsl:attribute name="style">
      <xsl:value-of select="$Style"/>
    </xsl:attribute>
  </xsl:template>
</xsl:stylesheet>
