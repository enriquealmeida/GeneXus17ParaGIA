<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:gxsql="urn:SQLFunc">
	<xsl:template match="ReorgSpec">
		<div id="ReorgSpec">
			<xsl:call-template name="TableHeaderMain">
				<xsl:with-param name="id">tblreorg</xsl:with-param>
				<xsl:with-param name="title">
					<xsl:text>Table </xsl:text>
					<xsl:apply-templates select="Table" />
					<xsl:text> </xsl:text>
					<xsl:apply-templates select="ReorgCls"/>
				</xsl:with-param>
			</xsl:call-template>
			<div>
				<table class="MainContentTable">
					<tbody>
						<xsl:choose>
							<xsl:when test="ReorgCls[text()=&#39;c&#39;]">
								<tr>
									<td colspan="2" style="border:1px solid #6C6C6C;padding:10px">
										<span class="LabelText">Table name: </span>
										<xsl:apply-templates select="Table" />
									</td>
								</tr>
								<xsl:if test="LocalTable">
									<tr>
										<td style="padding:5px 10px">Local:</td>
										<td>
											<xsl:value-of select="LocalTable" />
										</td>
									</tr>
								</xsl:if>
								<tr>
									<td colspan="2" style="padding:5px 10px" class="MessageText">
										<xsl:value-of select="ReorgMsg" />
									</td>
								</tr>
								<xsl:if test="ReorgMsgEx">
									<tr>
										<td colspan="2" style="padding:5px 10px" class="MessageText">
											<xsl:value-of select="ReorgMsgEx" />
										</td>
									</tr>
								</xsl:if>
							</xsl:when>
							<xsl:when test="ReorgCls[text()=&#39;l&#39;]">
								<tr>
									<td style="padding:5px 10px">Table name:</td>
									<td>
										<xsl:apply-templates select="Table" />
									</td>
								</tr>
							</xsl:when>
							<xsl:when test="ReorgCls[text()=&#39;s&#39;]">
								<tr>
									<td colspan="2" style="padding:5px 10px">Synchronization by row reorganization procedure</td>
								</tr>
							</xsl:when>
							<xsl:otherwise></xsl:otherwise>
						</xsl:choose>
						<xsl:if test="RedundantAttrisToUpdate">
							<tr>
								<td style="padding:5px 10px">Redundant attributes to update:</td>
								<td>
									<xsl:apply-templates select="RedundantAttrisToUpdate" />
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="FromAttrisToUpdate">
							<tr>
								<td style="padding:5px 10px">From attributes to update:</td>
								<td>
									<xsl:apply-templates select="FromAttrisToUpdate" />
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="RedundantAttris">
							<tr>
								<td style="padding:5px 10px">Redundant attributes:</td>
								<td>
									<xsl:apply-templates select="RedundantAttris" />
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="ProcedureName">
							<tr>
								<td style="padding:5px 10px">Procedure Name:</td>
								<td>
									<xsl:value-of select="ProcedureName" />
								</td>
							</tr>
						</xsl:if>
						<tr>
							<td colspan="2">
								<xsl:apply-templates select="RenameTable" />
							</td>
						</tr>
						<xsl:if test="Errors/Error">
							<tr>
								<td colspan="2">
									<xsl:apply-templates select="Errors" />
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="Warnings/Warning">
							<tr>
								<td colspan="2">
									<xsl:apply-templates select="Warnings" />
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="Information/InfoMsg">
							<xsl:comment>Printing Info</xsl:comment>
							<tr>
								<td colspan="2" style="padding:0">
									<xsl:apply-templates select="Information"/>
								</td>
							</tr>
						</xsl:if>
						<tr>
							<td colspan="2">
								<xsl:apply-templates select="TableAttributes" />
							</td>
						</tr>
						<xsl:if test="TableIndices">
							<tr>
								<td colspan="2">
									<xsl:apply-templates select="TableIndices" />
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="FKConstraints">
							<tr>
								<td colspan="2">
									<xsl:apply-templates select="FKConstraints" />
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="Statements">
							<tr>
								<td colspan="2">
									<xsl:apply-templates select="Statements" />
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="ObjectSpec/Levels">
							<tr>
								<td colspan="2">
									<xsl:apply-templates select="ObjectSpec/Levels" />
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="AttrisInManyTables/Attribute">
							<xsl:apply-templates select="AttrisInManyTables" />
						</xsl:if>
					</tbody>
				</table>
			</div>
		</div>
	</xsl:template>
	<!-- ReorgCls TEMPLATE-->
	<xsl:template match="ReorgCls">
		<xsl:choose>
			<xsl:when test="text()[.=&#39;c&#39;]">specification</xsl:when>
			<xsl:when test="text()[.=&#39;r&#39;]">load redundancy procedure</xsl:when>
			<xsl:when test="text()[.=&#39;u&#39;]">update redundancy procedure</xsl:when>
			<xsl:when test="text()[.=&#39;l&#39;]">append data to file procedure</xsl:when>
			<xsl:when test="text()[.=&#39;s&#39;]">procedure</xsl:when>
			<xsl:otherwise>specification</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="TableAttributes">
		<xsl:call-template name="GenerateTable">
			<xsl:with-param name="ID">TableAttributes</xsl:with-param>
			<xsl:with-param name="Title">Table Structure</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="AttrisInTable|AttrisNoLongerInTable" mode="TableRow">
		<xsl:for-each select="Attribute">
			<tr>
				<xsl:apply-templates select="." mode="TableRow" />
			</tr>
		</xsl:for-each>
	</xsl:template>
	<xsl:template match="Indices|NewIndices|IndicesToDelete" mode="TableRow">
		<xsl:apply-templates select="Index" mode="TableRow" />
	</xsl:template>
	<xsl:template match="RenameTable">
		<xsl:text>Rename </xsl:text>
		<xsl:value-of select="OldName" />
		<xsl:text> to </xsl:text>
		<xsl:value-of select="NewName" />
	</xsl:template>
	<xsl:template match="AttriOrder">
		<xsl:choose>
			<xsl:when test="text()[.=&#39;Descending&#39;]">
				<xsl:call-template name="RenderImage">
					<xsl:with-param name="Id">IdxDescending</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="RenderImage">
					<xsl:with-param name="Id">IdxAscending</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template name="CompareProperties">
		<xsl:for-each select="AttriType/Properties/Property">
			<xsl:variable name="Name" select="Name" />
			<xsl:variable name="Value" select="Value" />
			<xsl:for-each select="../../../AttriOldType/Properties/Property">
				<xsl:if test="$Name = Name and $Value != Value">
					<xsl:call-template name="ProperyValue">
						<xsl:with-param name="Name">
							<xsl:value-of select="Name" />
						</xsl:with-param>
						<xsl:with-param name="Value">
							<xsl:value-of select="Value" />
						</xsl:with-param>
					</xsl:call-template>
				</xsl:if>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>
	<xsl:template name="CompareType">
		<xsl:variable name="typ1" select="AttriType/DataType" />
		<xsl:variable name="len1" select="AttriType/Presicion" />
		<xsl:variable name="dec1" select="AttriType/Scale" />
		<xsl:variable name="typ2" select="AttriOldType/DataType" />
		<xsl:variable name="len2" select="AttriOldType/Presicion" />
		<xsl:variable name="dec2" select="AttriOldType/Scale" />
		<xsl:choose>
			<xsl:when test="$typ1 = $typ2 and $len1 = $len2 and $dec1 = $dec2"></xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="ProperyValue">
					<xsl:with-param name="Name">Type</xsl:with-param>
					<xsl:with-param name="Value">
						<xsl:apply-templates select="AttriOldType" />
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="AttriChangeType">
		<xsl:variable name="Var" select="../.." />
		<xsl:choose>
			<xsl:when test="text()[.=&#39;nv&#39;] or text()[.=&#39;nn&#39;] or text()[.=&#39;ni&#39;]">
				<xsl:call-template name="ChangeObject">
					<xsl:with-param name="ChgType">N</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$Var[name()=&#39;AttrisNoLongerInTable&#39;]">
				<xsl:call-template name="ChangeObject">
					<xsl:with-param name="ChgType">D</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise />
			<!-- del,upd -->
		</xsl:choose>
	</xsl:template>
	<!-- table FROM TEMPLATE -->
	<xsl:template match="TakesValueFrom">
		<xsl:apply-templates select="*" />
	</xsl:template>
	<xsl:template match="OnPrimaryKey">
		<xsl:if test="text()[.=&#39;Yes&#39;]">
			<xsl:call-template name="RenderImage">
				<xsl:with-param name="Id">Key</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template match="Attribute" mode="TableRow">
		<!-- Type of change -->
		<td style="vertical-align:top">
			<xsl:apply-templates select="AttriChangeType" />
		</td>
		<!-- Attribute is part of primary key -->
		<td style="vertical-align:top">
			<xsl:apply-templates select="OnPrimaryKey" />
		</td>
		<!--  Attribute name -->
		<td style="vertical-align:top;white-space: nowrap">
			<xsl:apply-templates select="." />
		</td>
		<!--  Definition -->
		<td style="vertical-align:top">
			<xsl:apply-templates select="AttriType" />
			<xsl:for-each select="AttriType/Properties/Property">
				<xsl:if test="Name[.=&#39;AllowNulls&#39;] and Value[.=&#39;No&#39;]">
					<xsl:text>, Not null</xsl:text>
				</xsl:if>
			</xsl:for-each>
			<xsl:for-each select="AttriType/Properties/Property">
				<xsl:if test="Name[.=&#39;Autonumber&#39;] and Value[.=&#39;Yes&#39;]">
					<xsl:text>, Autonumber</xsl:text>
				</xsl:if>
			</xsl:for-each>
			<xsl:for-each select="AttriType/Properties/Property">
				<xsl:if test="Name[.=&#39;Autogenerate&#39;] and Value[.=&#39;Yes&#39;]">
					<xsl:text>, Autogenerate</xsl:text>
				</xsl:if>
			</xsl:for-each>
			<xsl:for-each select="AttriType/Properties/Property">
				<xsl:if test="Name[.=&#39;NLS&#39;] and Value[.=&#39;Yes&#39;]">
					<xsl:text>, NLS</xsl:text>
				</xsl:if>
			</xsl:for-each>
		</td>
		<!-- Previous values -->
		<td style="vertical-align:top">
			<table style="width:100%;border-collapse:collapse">
				<tbody>
					<xsl:if test="AttriOldName">
						<xsl:call-template name="ProperyValue">
							<xsl:with-param name="Name">Name</xsl:with-param>
							<xsl:with-param name="Value">
								<xsl:value-of select="AttriOldName" />
							</xsl:with-param>
						</xsl:call-template>
					</xsl:if>
					<xsl:if test="AttriOldType">
						<xsl:call-template name="CompareType" />
						<xsl:call-template name="CompareProperties" />
					</xsl:if>
				</tbody>
			</table>
		</td>
		<!-- Takes values from -->
		<td style="vertical-align:top">
			<xsl:apply-templates select="TakesValueFrom" />
		</td>
	</xsl:template>
	<!-- ATTRIS IN MANY table TEMPLATE -->
	<xsl:template match="AttrisInManyTables">
		<div>
			<xsl:attribute name="id">inmany</xsl:attribute>
			<xsl:call-template name="TableHeader">
				<xsl:with-param name="id">inmany</xsl:with-param>
				<xsl:with-param name="title">Secondary attributes in many tables</xsl:with-param>
			</xsl:call-template>
			<div style="padding:10px">
				<table style="width:100%;border-collapse:collapse">
					<tbody>
						<tr class="subTitulosTabla">
							<td>Attribute</td>
							<td>Type</td>
							<td />
						</tr>
						<xsl:for-each select="Attribute">
							<tr>
								<td style="width:15%">
									<xsl:apply-templates select="." />
								</td>
								<td style="width:7%">
									<xsl:apply-templates select="AttriType" />
								</td>
								<td style="width:30%"/>
							</tr>
						</xsl:for-each>
					</tbody>
				</table>
			</div>
		</div>
	</xsl:template>
	<xsl:template match="TableIndices">
		<xsl:call-template name="GenerateTable">
			<xsl:with-param name="ID">Indices</xsl:with-param>
			<xsl:with-param name="Title">Indexes</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="Index" mode="TableRow">
		<xsl:param name="ChgType">none</xsl:param>
		<tr>
			<td style="vertical-align:top">
				<xsl:choose>
					<xsl:when test="name(..) =&#39;NewIndices&#39;">
						<xsl:call-template name="ChangeObject">
							<xsl:with-param name="ChgType">N</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
					<xsl:when test="name(..) = &#39;IndicesToDelete&#39;">
						<xsl:call-template name="ChangeObject">
							<xsl:with-param name="ChgType">D</xsl:with-param>
						</xsl:call-template>
					</xsl:when>
				</xsl:choose>
			</td>
			<td />
			<td style="vertical-align:top">
				<xsl:value-of select="IndexName" />
			</td>
			<td style="vertical-align:top">
				<xsl:apply-templates select="IndexType" />
				<xsl:apply-templates select="Clustered" />
			</td>
			<td style="vertical-align:top">
				<xsl:apply-templates select="IndexAttris" />
			</td>
			<td />
		</tr>
	</xsl:template>
	<xsl:template match="IndexType">
		<xsl:choose>
			<xsl:when test="text()[.=&#39;u&#39;]">primary key</xsl:when>
			<xsl:when test="text()[.=&#39;k&#39;]">unique</xsl:when>
			<xsl:otherwise>duplicate</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<xsl:template match="Clustered">
		<xsl:if test="text()[.=&#39;Yes&#39;]">
			<xsl:text> Clustered</xsl:text>
		</xsl:if>
	</xsl:template>
	<xsl:template match="IndexAttris">
		<table style="border-collapse:collapse;border-spacing:0">
			<tbody>
				<xsl:for-each select="Attribute">
					<tr class="text">
						<td>
							<xsl:choose>
								<xsl:when test="AttriOrder">
									<xsl:apply-templates select="AttriOrder" />
								</xsl:when>
								<xsl:otherwise>
									<xsl:call-template name="RenderImage">
										<xsl:with-param name="Id">IdxAscending</xsl:with-param>
									</xsl:call-template>
								</xsl:otherwise>
							</xsl:choose>
						</td>
						<td>
							<xsl:apply-templates select="." />
							<xsl:text>
 
</xsl:text>
						</td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>
	<xsl:template match="IndexAttribute">
		<td>
			<xsl:apply-templates select="IndexOrder" />
		</td>
		<td>
			<xsl:value-of select="Attribute" />,
		</td>
	</xsl:template>
	<xsl:template match="FKConstraint" mode="TableRow">
		<td style="vertical-align:top">
			<xsl:choose>
				<xsl:when test="ChangeType = &#39;new&#39;">
					<xsl:call-template name="ChangeObject">
						<xsl:with-param name="ChgType">N</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:when test="ChangeType = &#39;rmv&#39;">
					<xsl:call-template name="ChangeObject">
						<xsl:with-param name="ChgType">D</xsl:with-param>
					</xsl:call-template>
				</xsl:when>
				<xsl:otherwise></xsl:otherwise>
			</xsl:choose>
		</td>
		<td style="vertical-align:top">
			<xsl:apply-templates select="Table" />
		</td>
		<td style="vertical-align:top">
			<xsl:call-template name="VAttributes" />
		</td>
	</xsl:template>
	<xsl:template match="FKConstraints">
		<xsl:if test="FKConstraint">
			<xsl:call-template name="GenerateTable">
				<xsl:with-param name="ID">FKConstraint</xsl:with-param>
				<xsl:with-param name="Title">Foreign key constraints</xsl:with-param>
				<xsl:with-param name="RowTemplate">FKConstraint</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>
	<xsl:template match="Statements">
		<xsl:call-template name="GenerateTable">
			<xsl:with-param name="ID">Statement</xsl:with-param>
			<xsl:with-param name="Title">Statements</xsl:with-param>
			<xsl:with-param name="RowTemplate">Statement</xsl:with-param>
			<xsl:with-param name="Subtitulo">false</xsl:with-param>
		</xsl:call-template>
	</xsl:template>
	<xsl:template match="Statement" mode="TableRow">
		<td style="vertical-align:top">
			<pre style="white-space: normal">
				<xsl:value-of disable-output-escaping="yes" select="gxsql:GetSql(.)" />
			</pre>
		</td>
	</xsl:template>
</xsl:stylesheet>
