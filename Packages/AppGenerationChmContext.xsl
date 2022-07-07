<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:msxsl="urn:schemas-microsoft-com:xslt"
	xmlns:gx="urn:shemas-artech-com:gx"
	exclude-result-prefixes="msxsl gx">
<xsl:param name="pMainPageTitle">Application Help</xsl:param>
<xsl:param name="pMainPageUrl">AppHlp.htm</xsl:param>

<xsl:output method="html" encoding="UTF-8"/>

<xsl:template match="/">
<HTML><xsl:text>
</xsl:text><HEAD></HEAD><xsl:text>
</xsl:text><BODY><xsl:text>
</xsl:text>
	<UL>
		<xsl:call-template name="ContextItem">
			<xsl:with-param name="pPageName" select="$pMainPageTitle"/>
			<xsl:with-param name="pPageUrl" select="$pMainPageUrl"/>
			<xsl:with-param name="pOmitExt" select="true"/>
		</xsl:call-template>
		<UL>
			<xsl:apply-templates select="Objects/Object"/>
		</UL><xsl:text>
</xsl:text>
	</UL><xsl:text>
</xsl:text></BODY><xsl:text>
</xsl:text></HTML>
</xsl:template>

<!--
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	match = Object
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 -->
<xsl:template match="Object">
<xsl:variable name="vPrintList" select="TopicId and count(Variables/Variable) > 0"/>

	<xsl:if test="TopicId">
		<xsl:call-template name="ContextItem">
			<xsl:with-param name="pPageName" select="ObjTitle"/>
			<xsl:with-param name="pPageUrl" select="TopicId"/>
		</xsl:call-template>
	</xsl:if>

	<xsl:if test="$vPrintList">
<xsl:text disable-output-escaping="yes">&lt;UL&gt;</xsl:text>
	</xsl:if>

	<xsl:apply-templates select="Variables/Variable"/>

	<xsl:if test="$vPrintList">
<xsl:text disable-output-escaping="yes">&lt;/UL&gt;</xsl:text>
	</xsl:if>
</xsl:template>

<!--
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	match = Variable
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 -->
<xsl:template match="Variable">
	<xsl:call-template name="ContextItem">
		<xsl:with-param name="pPageName" select="Description"/>
		<xsl:with-param name="pPageUrl" select="TopicId"/>
	</xsl:call-template>
</xsl:template>

<!--
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	ContextItem
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 -->
<xsl:template name="ContextItem">
<xsl:param name="pPageName"/>
<xsl:param name="pPageUrl"/>
<xsl:param name="pOmitExt">false</xsl:param>

	<xsl:text disable-output-escaping="yes">	&lt;LI&gt; &lt;OBJECT type="text/sitemap"&gt;
			&lt;param name="Name" value="</xsl:text><xsl:value-of select="$pPageName"/><xsl:text disable-output-escaping="yes">"/&gt;
			&lt;param name="Local" value="</xsl:text><xsl:value-of select="$pPageUrl"/><xsl:if test="$pOmitExt">.htm</xsl:if><xsl:text disable-output-escaping="yes">"/&gt;
			&lt;/OBJECT&gt;
	</xsl:text>
</xsl:template>

</xsl:stylesheet>
