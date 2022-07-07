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
      <xsl:when test="@type = 'GxChartControl'">
        <xsl:call-template name="RenderGxChartControl"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- GxChart design render -->
  <xsl:template name="RenderGxChartControl">


    <span atomicselection="true">
      <xsl:call-template name="AddStyleAttribute"/>
      <img unselectable='on'>
        <xsl:attribute name="src">
          <xsl:value-of select="gxca:GetPropValue('ServiceUrl')"/>
          <xsl:text>drawchart.aspx?width=</xsl:text>
          <xsl:value-of select="gxca:GetPropertyValueInt('Width')"/>
          <xsl:text>&amp;type=</xsl:text>
          <xsl:value-of select="gxca:GetPropValue('ChartType')"/>
          <xsl:text>&amp;height=</xsl:text>
          <xsl:value-of select="gxca:GetPropertyValueInt('Height')"/>
          <xsl:text>&amp;title=</xsl:text>
          <xsl:value-of select="gxca:GetPropValue('Title')"/>
          <xsl:text>&amp;domtitle=</xsl:text>
          <xsl:value-of select="gxca:GetPropValue('X_AxisTitle')"/>
          <xsl:text>&amp;rantitle=</xsl:text>
          <xsl:value-of select="gxca:GetPropValue('Y_AxisTitle')"/>
          <xsl:text>&amp;bgc1=</xsl:text>
          <xsl:value-of select="gxca:GetPropertyValueColorRGB('BackgroundColor1')"/>
          <xsl:text>&amp;bgc2=</xsl:text>
          <xsl:value-of select="gxca:GetPropertyValueColorRGB('BackgroundColor2')"/>
          <xsl:text>&amp;gbgc1=</xsl:text>
          <xsl:value-of select="gxca:GetPropertyValueColorRGB('GraphColor1')"/>
          <xsl:text>&amp;gbgc2=</xsl:text>
          <xsl:value-of select="gxca:GetPropertyValueColorRGB('GraphColor2')"/>

          <xsl:text>&amp;shadow=</xsl:text>
          <xsl:value-of select="gxca:GetPropValue('DrawShadows')"/>
          <xsl:text>&amp;border=</xsl:text>
          <xsl:value-of select="gxca:GetPropValue('DrawBorder')"/>
          <xsl:text>&amp;values=</xsl:text>
          <xsl:value-of select="gxca:GetPropValue('ShowValues')"/>

          <xsl:text>&amp;alpha=</xsl:text>
          <xsl:value-of select="gxca:GetPropValue('Opacity')"/>
          <xsl:text>&amp;legend=</xsl:text>
          <xsl:value-of select="gxca:GetPropValue('LegendPosition')"/>
          <xsl:text>&amp;scale=</xsl:text>
          <xsl:value-of select="gxca:GetPropValue('Scale')"/>
          <xsl:text>&amp;Categories=Values:Category 1,Category 2, Category 3</xsl:text>
          <xsl:text>&amp;Series1=Values:Serie 1:96,35,23</xsl:text>
          <xsl:text>&amp;Series2=Values:Serie 2:50,60,75</xsl:text>
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
      <xsl:text>border-style: solid;	border-width: 2px;	color: gray;</xsl:text>
    </xsl:variable>
    <xsl:attribute name="style">
      <xsl:value-of select="$Style"/>
    </xsl:attribute>
    
  </xsl:template>
  
  
</xsl:stylesheet>