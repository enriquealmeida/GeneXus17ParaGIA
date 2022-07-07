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
      <xsl:when test="@type = 'FBHoverPanel'">
        <xsl:call-template name="RenderFBHoverPanel"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- FBHoverPanel design render -->
  <!-- ///////////////////  Implement your render here  ///////////////////-->
  <xsl:template name="RenderFBHoverPanel">
    <table style="width: 253px; height: 109px">
	<xsl:call-template name="AddStyleAttribute"/>
        <tr>
            <td containerId="DataContainer" rowspan="3" style="height: 105px; width: 75px;">Static Content Area
            </td>
            <td >
                <table style="width: 100%; height: 100%;border:1 solid">
                    <tr>
                        <td containerId="ContextTitleContainer" colspan="" style="height: 3px">Action Area
                        </td>
                    </tr>
                    <tr>
                        <td containerId="ContextDataContainer" colspan="3" rowspan="2" style="height: 90%">Drop Down Area
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        
    </table>

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
      <xsl:text>border-style: solid;	border-width: 2px;</xsl:text>
    </xsl:variable>
    <xsl:attribute name="style">
      <xsl:value-of select="$Style"/>
    </xsl:attribute>
  </xsl:template>
</xsl:stylesheet>
