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
      <xsl:when test="@type = 'DigitalSign'">
        <xsl:call-template name="RenderDigitalSign"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- DigitalSign design render -->

  <xsl:template name="RenderDigitalSign">
    <span atomicselection="true">
      <xsl:call-template name="AddStyleAttribute"/>    

     	<table width="100%" height="100%" border="0">
			<TR>
				<TD>            
					<xsl:attribute name="background">
						<xsl:value-of select='gxca:GetMyPath()'/>
						<xsl:text>\DigitalSign.jpg</xsl:text>
					</xsl:attribute>
					<p ALIGN="CENTER"><font color="#ffffff"><xsl:value-of select="gxca:GetPropValue('ButtonText')"/></font></p> 
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
