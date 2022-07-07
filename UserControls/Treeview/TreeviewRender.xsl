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
      <xsl:when test="@type = 'Treeview'">
        <xsl:call-template name="RenderTreeview" />
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- Treeview design render -->
  <!-- ///////////////////  Implement your render here  ///////////////////-->
  <xsl:template name="RenderTreeview">
    <span atomicselection="true">
      <xsl:call-template name="AddStyleAttribute"/>
      <table width="100%" height="100%">
        <TBody>
          <TR>
            <img style="background-repeat: no-repeat;" padding="0">
              <xsl:attribute name ="src">
                <xsl:value-of select='gxca:GetMyPath()'/>
                <xsl:text>\assets\img\RenderImages\</xsl:text>
                <xsl:value-of select="gxca:GetPropValue('Style')"/>
                <xsl:text>.JPG</xsl:text>
              </xsl:attribute>
            </img>
          </TR>
        </TBody>
      </table>        
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
      <xsl:text>border-style: solid;	border-width: 2px;</xsl:text>
    </xsl:variable>
    <xsl:attribute name="style">
      <xsl:value-of select="$Style"/>
    </xsl:attribute>
  </xsl:template>
</xsl:stylesheet>
