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
      <xsl:when test="@type = 'FCKEditor'">
        <xsl:call-template name="RenderCKEditor"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- CKEditor design render -->
  <!-- ///////////////////  Implement your render here  ///////////////////-->
  <xsl:template name="RenderCKEditor">
    <span atomicselection="true">
      <xsl:call-template name="AddStyleAttribute"/>
      <table width="100%" height="100%">
        <TBody>
          <TR>
            <img style="background-repeat: no-repeat;" padding="0">
                <xsl:variable name="toolbar">
                  <xsl:value-of select="gxca:GetPropValue('Toolbar')"/>
                </xsl:variable>
              <xsl:attribute name ="height">
                <xsl:choose>
                  <xsl:when test ="$toolbar = 'Default'">
                    <xsl:text>128</xsl:text>
                  </xsl:when>
                  <xsl:when test="$toolbar = 'Basic'">
                    <xsl:text>40</xsl:text>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:text>35</xsl:text>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:attribute>
                <xsl:attribute name ="src">
                  <xsl:value-of select='gxca:GetMyPath()'/>
                  <xsl:text>\</xsl:text>
                  <xsl:value-of select="gxca:GetPropValue('Skin')"/>
                  <xsl:value-of select="gxca:GetPropValue('Toolbar')"/>
                  <xsl:text>.bmp</xsl:text>
                </xsl:attribute>
            </img>
          </TR>
          <TR>
            <TD>
              <textarea Style="width:100%;height:100%">
                <xsl:text>CKEditor.</xsl:text>
              </textarea>
            </TD>
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
