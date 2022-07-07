<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:msxsl="urn:schemas-microsoft-com:xslt"
	xmlns:gx="urn:shemas-artech-com:gx"
	exclude-result-prefixes="msxsl gx">

<xsl:output method="html" encoding="UTF-8"/>

<xsl:template match="/">
<HTML>
<HEAD/>
<BODY>
	<UL>
		<xsl:apply-templates select="Objects/Object"/>
	</UL>
</BODY>
</HTML>
</xsl:template>

<!--
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	match = Object
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 -->
<xsl:template match="Object">

	<xsl:if test="TopicId">
		<xsl:call-template name="IndexItem">
			<xsl:with-param name="pPageName" select="ObjTitle"/>
			<xsl:with-param name="pPageUrl" select="TopicId"/>
		</xsl:call-template>
	</xsl:if>

	<xsl:apply-templates select="Variables/Variable"/>

</xsl:template>

<!--
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	match = Variable
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 -->
<xsl:template match="Variable">
	<xsl:call-template name="IndexItem">
		<xsl:with-param name="pPageName" select="Description"/>
		<xsl:with-param name="pPageUrl" select="TopicId"/>
	</xsl:call-template>
</xsl:template>

<!--
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	IndexItem
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 -->
<xsl:template name="IndexItem">
<xsl:param name="pPageName"/>
<xsl:param name="pPageUrl"/>

	<xsl:text disable-output-escaping="yes">	&lt;LI&gt; &lt;OBJECT type="text/sitemap"&gt;
			&lt;param name="Name" value="</xsl:text><xsl:value-of select="$pPageName"/><xsl:text disable-output-escaping="yes">"/&gt;
			&lt;param name="Name" value="</xsl:text><xsl:value-of select="$pPageName"/><xsl:text disable-output-escaping="yes">"/&gt;
			&lt;param name="Local" value="</xsl:text><xsl:value-of select="$pPageUrl"/><xsl:text disable-output-escaping="yes">.htm"/&gt;
			&lt;/OBJECT&gt;
	</xsl:text>

</xsl:template>

</xsl:stylesheet>
