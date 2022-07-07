<?xml version='1.0' encoding='iso-8859-1'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- DYNAMIC COMBO NAVIGATION -->
<xsl:template match="DynamicCombo" mode="TableRow">
	  <td><xsl:value-of select="DynamicComboName"/></td>
	  <td><xsl:apply-templates select="Table" mode="icon"/></td>
	  <td><xsl:value-of select="DynamicComboDesc"/></td>
	  <td><xsl:value-of select="DynamicComboSorted"/></td>
	  <td><xsl:value-of select="DynamicComboValue"/></td>
</xsl:template>

<xsl:template match="DynamicCombos">
	<xsl:call-template name="GenerateTable">
		<xsl:with-param name="ID">DynamicCombos</xsl:with-param>
		<xsl:with-param name="Title">Dynamic Combos</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template match="Suggests">
	<xsl:call-template name="GenerateTable">
		<xsl:with-param name="ID">Suggests</xsl:with-param>
		<xsl:with-param name="Title">Suggestions</xsl:with-param>
	</xsl:call-template>
</xsl:template>
<xsl:template match="Suggest" mode="TableRow">
		<td style="vertical-align:top"><xsl:apply-templates select="ControlName"/></td>
		<td style="vertical-align:top"><xsl:value-of select="SuggestType"/></td>
		<td style="vertical-align:top"><xsl:apply-templates select="Navigation"/></td>
</xsl:template>

<!-- MENU BAR -->
<xsl:template match="MenuBar">
	<xsl:apply-templates select="MenuBarOption"/>
</xsl:template>

<xsl:template match="MenuBarOption">
	<xsl:value-of select="Name"/> <xsl:text>   </xsl:text> <xsl:value-of select="Event"/>
	<ol>
		<xsl:apply-templates select="MenuBarOption"/>
	</ol>
</xsl:template>
<!-- END MENUBAR -->

<xsl:template match="MenuOption" mode="TableRow">
  <td><xsl:value-of select="OptionNumber"/></td>
  <td><xsl:apply-templates select="OptionCls"/></td>
  <td><xsl:value-of select="OptionName"/></td>
  <td><xsl:value-of select="OptionDesc"/></td>
  <td><xsl:value-of select="OptionCall"/></td>
</xsl:template>

<xsl:template match="MenuOptions">
	<xsl:call-template name="GenerateTable">
		<xsl:with-param name="ID">MenuOptions</xsl:with-param>
		<xsl:with-param name="Title">Menu Options</xsl:with-param>
	</xsl:call-template>
</xsl:template>

<xsl:template match="Prompts">
	<xsl:call-template name="GenerateTable">
		<xsl:with-param name="ID">Prompts</xsl:with-param>
		<xsl:with-param name="Title">Prompts</xsl:with-param>
	</xsl:call-template>
</xsl:template>

<xsl:template match="Prompt" mode="TableRow">
	<td><xsl:apply-templates select="Table" mode="icon"/></td>
	<td></td>
	<td>
		<xsl:if test="Object">
			<xsl:apply-templates select="Object" mode="icon"/>
			<xsl:if test="PromptType">
				<xsl:apply-templates select="PromptType"/>
			</xsl:if>
		</xsl:if>
		<xsl:if test="ProgramName">
			<xsl:value-of select="ProgramName"/>
			<xsl:if test="PromptType">
				<xsl:apply-templates select="PromptType"/>
			</xsl:if>
		</xsl:if>
	</td>
	<td><xsl:apply-templates select="InputParameters"/></td>
	<td><xsl:apply-templates select="OutputParameters"/></td>
</xsl:template>



<!-- PROMPT TYPE TEMPLATE -->
<xsl:template match="PromptType">
	<xsl:if test="text()[.='User']">
		<xsl:call-template name="RenderImage">
			<xsl:with-param name="Id">User</xsl:with-param>
		</xsl:call-template>
	</xsl:if>
</xsl:template>

<xsl:template match="Prompt"><xsl:apply-templates/></xsl:template>

</xsl:stylesheet>
