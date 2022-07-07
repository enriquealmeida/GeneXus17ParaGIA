<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" media-type = "string"/>
	<xsl:template match="/">
		<xsl:apply-templates select="//Warning|//Error|//Information|//InfoMsg"/>
	</xsl:template>

	<xsl:template match="Warning">
		<xsl:apply-templates select="MsgCode"/>
		<xsl:apply-templates select="Message"/>
		<!--xsl:apply-templates select="Location"/-->
	</xsl:template>

	<xsl:template match="Error">
		<xsl:apply-templates select="MsgCode"/>
		<xsl:apply-templates select="Message"/>
		<!--xsl:apply-templates select="Location"/-->
	</xsl:template>

	<xsl:template match="Information">
		<xsl:text>Information: </xsl:text>
		<xsl:apply-templates select="MsgCode"/>
		<xsl:apply-templates select="Message"/>
		<!--xsl:apply-templates select="Location"/-->
	</xsl:template>

	<xsl:template match="InfoMsg">
		<xsl:text>information </xsl:text>
		<xsl:apply-templates select="MsgCode"/>
		<xsl:apply-templates select="Message"/>
		<!--xsl:apply-templates select="Location"/-->
	</xsl:template>

	<xsl:template match="MsgCode">
		<xsl:value-of select="."/>
		<xsl:text>: </xsl:text>
	</xsl:template>

	<xsl:template match="Token">
		<xsl:value-of select="."/>
	</xsl:template>

	<xsl:template match="Variable">
		<xsl:value-of select="VarName"/>
	</xsl:template>

	<xsl:template match="Attribute">
		<xsl:if test="AttriOrder='Descending'">
			<xsl:text>(</xsl:text>
		</xsl:if>
		<xsl:choose>
			<xsl:when test="AttriName">
				<xsl:value-of select="AttriName"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="AttriOrder='Descending'">
			<xsl:text>)</xsl:text>
		</xsl:if>
	</xsl:template>

	<xsl:template match="Table">
		<xsl:value-of select="TableName"/>
	</xsl:template>

	<xsl:template match="Object">
		<xsl:value-of select="ObjName"/>
	</xsl:template>

	<xsl:template match="Index">
		<xsl:value-of select="IndexName"/>
	</xsl:template>

	<xsl:template match="SubtypeGroup">
		<xsl:value-of select="SubtypeGroupName"/>
	</xsl:template>

	<xsl:template match="Subtype">
		<xsl:value-of select="SubtypeName"/>
	</xsl:template>

	<xsl:template match="Sp">
		<xsl:text> </xsl:text>
	</xsl:template>

	<xsl:template match="Message">
		<xsl:for-each select="Attribute|Subtype|Token|Variable|Table|Index|SubtypeGroup|Sp|Object">
			<xsl:apply-templates select="."/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="Location">
		<xsl:if test="Type">
			<xsl:text> (</xsl:text>
			<xsl:value-of select="Type"/>
			<xsl:if test="Line">
				<xsl:text>, Line: </xsl:text>
				<xsl:value-of select="Line"/>
			</xsl:if>
			<xsl:text>)</xsl:text>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>