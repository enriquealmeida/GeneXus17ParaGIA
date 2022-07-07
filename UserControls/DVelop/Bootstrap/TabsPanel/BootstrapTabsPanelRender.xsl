<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:msxml="urn:schemas-microsoft-com:xslt"
	xmlns:gx="urn:shemas-artech-com:gx"
	exclude-result-prefixes="msxml gx"
	xmlns:gxca="urn:GXControlAdap"
	xmlns:user="urn:UserRenderHelper">
  <xsl:output method="html"/>
  <xsl:template match="/" >
    <xsl:apply-templates select="/GxControl"/>
  </xsl:template>
  <xsl:template match="GxControl">
    <xsl:choose>
      <xsl:when test="@type = 'DVelop.Bootstrap.TabsPanel'">
        <xsl:call-template name="RenderBootstrapTabsPanel"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- BootstrapTabsPanel design render -->
  <!-- ///////////////////  Implement your render here  ///////////////////-->
  <xsl:template name="RenderBootstrapTabsPanel">
  	<xsl:variable name="Selected">
      <xsl:value-of select="user:HandleClick()"/>
    </xsl:variable>
    <table cellSpacing="0" cellPadding="0" background="">
      <xsl:call-template name="AddTableStyleAttribute"/>
      <tbody>
		<xsl:apply-templates mode="RenderPanels" select="user:GetPanels()">
          <xsl:with-param name="Selected" select="$Selected" />
        </xsl:apply-templates>
      </tbody>
    </table>
  </xsl:template>


  <!-- Helpers Templates -->

  <xsl:template name="AddTableStyleAttribute" >
    <xsl:variable name="Style">
      <xsl:text>width: </xsl:text>
      <xsl:value-of select="gxca:GetPropertyValueInt('Width')"/>
      <xsl:text>; </xsl:text>
      <xsl:text>height: </xsl:text>
      <xsl:text>auto</xsl:text>
      <xsl:text>; </xsl:text>
      <xsl:text>border-style: 1px solid;</xsl:text>
    </xsl:variable>
	<xsl:attribute name="style">
      <xsl:value-of select="$Style"/>
    </xsl:attribute>
  </xsl:template>


  <xsl:template match="Panels" mode="RenderPanels">
	<xsl:param name="Selected"/>
	<xsl:for-each select="Panel">
		<tr>
			<td>
				<xsl:attribute name="style">
					<xsl:text>width:100%;</xsl:text>
				</xsl:attribute>
				<xsl:attribute name="containerId">
					<xsl:text>Title</xsl:text>
					<xsl:value-of select="Id"/>
				</xsl:attribute>
			</td>
			<td>
				<xsl:attribute name="style">
					<xsl:text>width:2px;align:right;</xsl:text>
				</xsl:attribute>
				<img style="margin-left:2px;" alt="Click here to remove tab panel">
					<xsl:attribute name="id">
						<xsl:text>x</xsl:text>
						<xsl:value-of select="Id"/>
					</xsl:attribute>
					<xsl:attribute name="src">
						<xsl:value-of select='gxca:GetMyPath()'/>
						<xsl:text>/Bootstrap/Shared/images/tabCloseOn.gif</xsl:text>
					</xsl:attribute>
				</img>
			</td>
		</tr>
		<tr>
			<td style="height:50px;" colspan="2">
				<xsl:attribute name="containerId">
					<xsl:value-of select="Id"/>
				</xsl:attribute>
				<xsl:attribute name="style">
					<xsl:text>background-color:#EFF2FB;height:50px;</xsl:text>
				</xsl:attribute>
			</td>
		</tr>
	</xsl:for-each>
	<tr>
		<td style="align:right;" colspan="2">
			<img id="+" alt="Click here to add a panel">
				<xsl:attribute name="src">
					<xsl:value-of select='gxca:GetMyPath()'/>
					<xsl:text>/Bootstrap/Shared/images/add.gif</xsl:text>
				</xsl:attribute>
			</img>
		</td>
	</tr>
 </xsl:template>

</xsl:stylesheet>

