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
      <xsl:when test="@type = 'GXProgressIndicator'">
        <xsl:call-template name="RenderGXProgressIndicator"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- GXProgressIndicator design render -->
  <!-- ///////////////////  Implement your render here  ///////////////////-->
  <xsl:template name="RenderGXProgressIndicator">
    <span atomicselection="true">

      <xsl:call-template name="AddStyleAttribute"/>
      <table style="width:408px;height:27px;border:0px;">
		<TR>
			<TD style="height:27px;border:0px;">
				<xsl:attribute name="background">
					<xsl:value-of select='gxca:GetMyPath()'/>
					<xsl:text>\GXProgressIndicator.png</xsl:text>
				</xsl:attribute>
			</TD>
		</TR>
	  </table>
      
   
      
      
          </span>


  </xsl:template>


  <!-- Helpers Templates -->

  <xsl:template name="AddStyleAttribute" >
    <xsl:variable name="Style">
      <xsl:text>width: </xsl:text>
      <xsl:value-of select="gxca:GetPropertyValueInt('Width')"/>
      <xsl:text>; </xsl:text>
      <xsl:text>height:27px; </xsl:text>            
      <xsl:text>border:1px silver solid;padding:0px;</xsl:text>
    </xsl:variable>
  </xsl:template>
</xsl:stylesheet>
