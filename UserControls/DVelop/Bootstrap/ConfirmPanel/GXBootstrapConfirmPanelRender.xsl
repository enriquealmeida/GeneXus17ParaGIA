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
      <xsl:when test="@type = 'DVelop.GXBootstrap.ConfirmPanel'">
        <xsl:call-template name="RenderBootstrapConfirmPanel"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- BootstrapConfirmPanel design render -->
  <!-- ///////////////////  Implement your render here  ///////////////////-->
  <xsl:template name="RenderBootstrapConfirmPanel">
    <table cellSpacing="0" cellPadding="0" background="">
      <xsl:attribute name="style">
        <xsl:text>width: </xsl:text>
        <xsl:value-of select="gxca:GetPropertyValueInt('Width')"/>
        <xsl:text>; </xsl:text>
        <xsl:text>height: </xsl:text>
        <xsl:value-of select="gxca:GetPropertyValueInt('Height')"/>
        <xsl:text>; </xsl:text>
        <xsl:text>border-style: 1px solid;</xsl:text>
      </xsl:attribute>
      <tbody>
        <tr>
          <td>
            <xsl:attribute name="style">
              <xsl:text>padding:1 2 1 2;width:100%;height:1.3em;font-family:Tahoma;font-size:0.9em</xsl:text>
            </xsl:attribute>
            <xsl:value-of select="gxca:GetStringPropertyValue('Title')"/>
          </td>
        </tr>
        <tr>
          <td>
		    <xsl:attribute name="style">
              <xsl:text>padding:2 3 2 3;vertical-align:top;</xsl:text>
            </xsl:attribute>
		  	<table>
				<xsl:attribute name="style">
					<xsl:text>font-family:Tahoma;font-size:0.8em;</xsl:text>
				</xsl:attribute>
				<tr>
				  <td>
				    <xsl:value-of select="gxca:GetStringPropertyValue('ConfirmationText')"/>
				  </td>
				</tr>
				<tr>
				  <td>
				  	<xsl:attribute name="containerId">
					  <xsl:text>Body</xsl:text>
					</xsl:attribute>
				  </td>
				</tr>
			</table>
          </td>
        </tr>
		<tr>
		  <td>
		    <xsl:attribute name="style">
			  <xsl:text>vertical-align: bottom;</xsl:text>
		    </xsl:attribute>
		    <table>
			  <xsl:attribute name="style">
			    <xsl:text>width: 100%;</xsl:text>
			  </xsl:attribute>
			  <tbody>
			    <tr>
				  <td>
				    <xsl:attribute name="style">
						<xsl:text>width: 100%;</xsl:text>
					</xsl:attribute>
				  </td>
				  <td>
				    <xsl:attribute name="style">
						<xsl:text>padding:6px 12px; align:right; color: #333333; background-color: #ebebeb; border-color: #adadad;</xsl:text>
					</xsl:attribute>
					<xsl:value-of select="gxca:GetStringPropertyValue('NoButtonCaption')"/>
				  </td>
				  <td>
				    <xsl:attribute name="style">
						<xsl:text>padding:6px 12px; align:right; color: #ffffff; background-color: #428bca; border-color: #357ebd;</xsl:text>
					</xsl:attribute>
					<xsl:value-of select="gxca:GetStringPropertyValue('YesButtonCaption')"/>
				  </td>
				</tr>
			  </tbody>
			</table>
		  </td>
		</tr>
      </tbody>
    </table>
  </xsl:template>

</xsl:stylesheet>
