<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output method="html" encoding="UTF-8"/>
	<xsl:template match="/">
		<html>
			<xsl:call-template name="doHeader">
				<xsl:with-param name="dvgPath">
					<xsl:value-of select="Recent/DVGPath"/>
				</xsl:with-param>
			</xsl:call-template>
			<body  bgcolor="#FFFFFF" text="#000000" link="#000000" alink="#000000" vlink="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onload="initializeControls()">
				<xsl:call-template name="doScripts">
					<xsl:with-param name="dvgPath">
						<xsl:value-of select="Recent/DVGPath"/>
					</xsl:with-param>
				</xsl:call-template>
				<form id="DVGFORM" method="post" name="DVGFORM">
					<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
						<tr>
							<td>
								<input type="radio" name="activity" value="Enter Connection Information" checked="checked" onclick=""/><b>Enter Connection Information</b><br/>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="activity" value="Use Saved Connection Information" onclick=""/><b>Use Saved Connection Information</b><br/>
								<table>
									<tr>
										<td>	</td><td>	</td><td>	</td><td>	</td><td>	</td><td>	</td>
										<td>
											<xsl:call-template name="doConnections">
												<xsl:with-param name="dvgPath">
													<xsl:value-of select="Recent/DVGPath"/>
												</xsl:with-param>
											</xsl:call-template>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<input type="radio" name="activity" value="Open Saved Metadata" onclick=""/><b>Open Saved Metadata</b><br/>
								<table>
									<tr>
										<td>	</td><td>	</td><td>	</td><td>	</td><td>	</td><td>	</td>
										<td>
											<xsl:call-template name="doMetadatas">
												<xsl:with-param name="dvgPath">
													<xsl:value-of select="Recent/DVGPath"/>
												</xsl:with-param>
											</xsl:call-template>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</form>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template name="doHeader">
	<xsl:param name="dvgPath"/>
		<meta http-equiv="Content-Type">
			<xsl:attribute name="content">
				<xsl:text>text/html; charset=UTF-8</xsl:text>
			</xsl:attribute>
		</meta>
		<link rel="stylesheet" type="text/css">
			<xsl:attribute name="href"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\styles.css</xsl:attribute>
		</link>
	</xsl:template>
	
	<xsl:template name="doScripts">
	<xsl:param name="dvgPath"/>
		<script language="javascript">
			<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\initial.js</xsl:attribute>
		</script>
	</xsl:template>
	
	<xsl:template name="doConnections">
	<xsl:param name="dvgPath"/>
		<pre>
			<xsl:for-each select="Recent/Connections/File">
				<input type="radio" id="connection" name="connection" checked="checked" onclick="">
					<xsl:attribute name="value"><xsl:value-of select="Name"/></xsl:attribute>
				</input><xsl:value-of select="Name"/><br/>
			</xsl:for-each>
		</pre>
	</xsl:template>
	
	<xsl:template name="doMetadatas">
	<xsl:param name="dvgPath"/>
		<pre>
			<xsl:for-each select="Recent/Metadatas/File">
				<input type="radio" id="metadata" name="metadata" checked="checked" onclick="mdataRadioClick(this)">
					<xsl:attribute name="value"><xsl:value-of select="Name"/></xsl:attribute>
				</input><xsl:value-of select="Name"/><br/>
			</xsl:for-each>
			<input type="radio" id="metadata" name="metadata" value="other_mdata" onclick="mdataRadioClick(this)"/>
			<input type="file" id="mdataSelFile" name="mdataSelFile" size="50" disabled="disabled" onmouseout="metadataSelected('onchange')"/>
		</pre>
	</xsl:template>
	<!--
	<xsl:template name="doObjectType">
	<xsl:param name="objPreffix"/>
		<xsl:if test="$objPreffix='TRN'">Transaction </xsl:if>
		<xsl:if test="$objPreffix='DV'">Dataview</xsl:if>
		<xsl:if test="$objPreffix='SG'">Subtype Group</xsl:if>
		<xsl:if test="$objPreffix='TBL'">Table</xsl:if>
	</xsl:template>

	<xsl:template name="doObjectInfo">
	<xsl:param name="objPreffix"/>
	<xsl:param name="dvgPath"/>
		<xsl:if test="$objPreffix='TRN'">
			<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
				<tr>
					<td class="titulosTabla">
						<xsl:attribute name="title">Warnings</xsl:attribute>
						<b>Warnings</b>
					</td>
				</tr>
				<xsl:for-each select="DBObject/Warnings/Warning">
					<tr>
						<td>
							<img width="13" height="13" border="0">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\warningIcon.gif</xsl:attribute>
							</img>
							<xsl:value-of select="Message"/>
						</td>
					</tr>
				</xsl:for-each>
			</table>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="doActions">
	<xsl:param name="dvgPath"/>
	<xsl:param name="hasSubtypes"/>
		<table width="100%" cellpadding="0" cellspacing="0" border="0" bordercolor="#CDAAAD" style="border-collapse: collapse">
			<tr>
			   <td class="titulosTabla">
					<xsl:attribute name="title">Objects</xsl:attribute>
					<b>Objects</b>
				</td>
			</tr>
			<xsl:for-each select="DBObject/Actions/Action">
				<xsl:call-template name="doCreateAction">
					<xsl:with-param name="actionType" select="@xsi:type"/>
					<xsl:with-param name="dvgPath" select="$dvgPath"/>
				</xsl:call-template>
			</xsl:for-each>
		</table>
		<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
			<tr>
			   <td class="titulosTabla">
					<xsl:attribute name="title">Attributes</xsl:attribute>
					<b>Attributes</b>
				</td>
			</tr>
			<xsl:for-each select="DBObject/Actions/Action">
				<xsl:call-template name="doAttributeAction">
					<xsl:with-param name="actionType" select="@xsi:type"/>
					<xsl:with-param name="dvgPath" select="$dvgPath"/>
				</xsl:call-template>
			</xsl:for-each>
		</table>
		<xsl:if test="$hasSubtypes='true'">
			<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
				<tr>
				   <td class="titulosTabla">
						<xsl:attribute name="title">Subtypes</xsl:attribute>
						<b>Subtypes</b>
					</td>
				</tr>
				<xsl:for-each select="DBObject/Actions/Action">
					<xsl:call-template name="doSubtypeAttributeAction">
						<xsl:with-param name="actionType" select="@xsi:type"/>
						<xsl:with-param name="dvgPath" select="$dvgPath"/>
					</xsl:call-template>
				</xsl:for-each>
			</table>
		</xsl:if>
		<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
			<tr>
			   <td class="titulosTabla">
					<xsl:attribute name="title">Indexes</xsl:attribute>
					<b>Indexes</b>
				</td>
			</tr>
			<xsl:for-each select="DBObject/Actions/Action">
				<xsl:call-template name="doIndexAction">
					<xsl:with-param name="actionType" select="@xsi:type"/>
					<xsl:with-param name="dvgPath" select="$dvgPath"/>
				</xsl:call-template>
			</xsl:for-each>
		</table>
	</xsl:template>
	
	<xsl:template name="doCreateAction">
	<xsl:param name="actionType"/>
	<xsl:param name="dvgPath"/>
		<xsl:if test="$actionType='CreateAction'">
			<xsl:call-template name="CreateAction">
				<xsl:with-param name="dvgPath" select="$dvgPath"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="doAttributeAction">
	<xsl:param name="actionType"/>
	<xsl:param name="dvgPath"/>
		<xsl:if test="$actionType='AddAttributeAction'">
			<xsl:call-template name="AddAttributeAction">
				<xsl:with-param name="dvgPath" select="$dvgPath"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="doSubtypeAttributeAction">
	<xsl:param name="actionType"/>
	<xsl:param name="dvgPath"/>
		<xsl:if test="$actionType='AddSubtypeAttributeAction'">
			<xsl:call-template name="AddSubtypeAttributeAction">
				<xsl:with-param name="dvgPath" select="$dvgPath"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="doIndexAction">
	<xsl:param name="actionType"/>
	<xsl:param name="dvgPath"/>
		<xsl:if test="$actionType='AddIndexAction'">
			<xsl:call-template name="AddIndexAction">
				<xsl:with-param name="dvgPath" select="$dvgPath"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="CreateAction">
	<xsl:param name="dvgPath"/>
		<tr>
			<td>
				<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
				<tr>
					<td width="150">
						<xsl:if test="ObjectType=1">
							<img width="16" height="16" border="0">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\dataview.ico</xsl:attribute>
							</img>
							<xsl:text>Dataview: </xsl:text>
						</xsl:if>
						<xsl:if test="ObjectType=2">
							<img width="16" height="16" border="0">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\transaction.ico</xsl:attribute>
							</img>
							<xsl:text>Transaction: </xsl:text>
						</xsl:if>
						<xsl:if test="ObjectType=3">
							<img width="16" height="16" border="0">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\table.ico</xsl:attribute>
							</img>
							<xsl:text>Table: </xsl:text>
						</xsl:if>
						<xsl:if test="ObjectType=4">
							<img width="16" height="16" border="0">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\group.ico</xsl:attribute>
							</img>
							<xsl:text>Subtype Group: </xsl:text>
						</xsl:if>
					</td>
					<td>
						<xsl:value-of select="ObjectName"/>
					</td>
					</tr>
				</table>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template name="AddAttributeAction">
	<xsl:param name="dvgPath"/>
		<tr>
			<td>
				<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
					<xsl:call-template name="doAttributeCommon">
						<xsl:with-param name="dvgPath" select="$dvgPath"/>
					</xsl:call-template>
				</table>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template name="AddSubtypeAttributeAction">
	<xsl:param name="dvgPath"/>
		<tr>
			<td>
				<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
					<xsl:call-template name="doAttributeCommon">
						<xsl:with-param name="dvgPath" select="$dvgPath"/>
					</xsl:call-template>
					<tr>
						<td>Group Name: </td><td><xsl:value-of select="SubtypeGroupName"/></td>
					</tr>
					<tr>
						<td>Supertype: </td>
						<td>
							<span style="cursor: hand">
								<xsl:attribute name="onclick">
									<xsl:text>AttCrossReference('</xsl:text>
									<xsl:value-of select="SupertypeName"/>
									<xsl:text>');</xsl:text>
								</xsl:attribute>
								<u><xsl:value-of select="SupertypeName"/></u>
							</span>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template name="doAttributeCommon">
	<xsl:param name="dvgPath"/>
		<tr>
			<td width="150">Name: </td>
			<td>
				<span style="cursor: hand">
					<xsl:attribute name="onclick">
						<xsl:text>AttCrossReference('</xsl:text>
						<xsl:value-of select="AttributeName"/>
						<xsl:text>');</xsl:text>
					</xsl:attribute>
					<u><xsl:value-of select="AttributeName"/></u>
				</span>
				<xsl:if test="IsPrimaryKey='true'">
					<img width="16" height="16" border="0">
						<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\key.ico</xsl:attribute>
					</img>
				</xsl:if>
				<xsl:if test="IsForeignKey='true'">
					<img width="16" height="16" border="0">
						<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\key subtipo.ico</xsl:attribute>
					</img>
				</xsl:if>
			</td>
		</tr>
		<tr>
			<td>External Name: </td><td><xsl:value-of select="ExternalName"/></td>
		</tr>
		<tr>
			<td>Type: </td><td><xsl:value-of select="AttributeType"/></td>
		</tr>
		<tr>
			<td>Length: </td><td><xsl:value-of select="AttributeLength"/></td>
		</tr>
		<tr>
			<td>Decimals: </td><td><xsl:value-of select="AttributeDecimals"/></td>
		</tr>
	</xsl:template>
	
	<xsl:template name="AddIndexAction">
	<xsl:param name="dvgPath"/>
		<tr>
			<td>
				<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
					<tr>
						<td width="150">Name: </td><td><xsl:value-of select="IndexName"/></td>
					</tr>
					<tr>
						<td>ExternalName: </td><td><xsl:value-of select="ExternalName"/></td>
					</tr>
					<tr>
						<td>Type: </td><td><xsl:value-of select="UniqueDuplicate"/></td>
					</tr>
					<tr>
						<td>Attributes: </td>
						<td>
							<table>
								<xsl:for-each select="Attributes/anyType">
									<tr>
										<td>
											<span style="cursor: hand">
												<xsl:attribute name="onclick">
													<xsl:text>AttCrossReference('</xsl:text>
													<xsl:value-of select="attributeName"/>
													<xsl:text>');</xsl:text>
												</xsl:attribute>
												<u><xsl:value-of select="attributeName"/></u>
											</span>
										</td>
										<td>, <xsl:value-of select="order"/></td>
									</tr>
								</xsl:for-each>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</xsl:template>-->
</xsl:stylesheet>