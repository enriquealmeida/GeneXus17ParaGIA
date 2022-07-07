<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:param name="EmptyMessage">Navigation View is empty, you can drag objects to the left pane to see their navigation.</xsl:param>

	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
		<xsl:call-template name="HtmlHeader">
			<xsl:with-param name="ReportTitle">GeneXus Navigation Report</xsl:with-param>
		</xsl:call-template>
		<body>
			<xsl:call-template name="HtmlScript"/>
			<table class="LayoutNoBorder">
				<tbody>
					<xsl:choose>
						<xsl:when test="ObjSpecs/ReorgSpec|ObjSpecs/ObjectSpec">
							<xsl:for-each select="ObjSpecs">
								<xsl:if test="position() != 1">
									<tr>
										<td>
											<xsl:text> </xsl:text>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="ReorgSpec">
									<tr>
										<td>
											<xsl:apply-templates select="ReorgSpec"/>
										</td>
									</tr>
								</xsl:if>
								<xsl:if test="ObjectSpec">
									<tr>
										<td>
											<xsl:apply-templates select="ObjectSpec"/>
										</td>
									</tr>
								</xsl:if>
							</xsl:for-each>
						</xsl:when>
						<xsl:otherwise>
							<tr>
								<td>
									<xsl:value-of select="$EmptyMessage"/>
								</td>
							</tr>
						</xsl:otherwise>
					</xsl:choose>
				</tbody>
			</table>
		</body>
		<xsl:text disable-output-escaping="yes">&lt;/html&gt;</xsl:text>
	</xsl:template>

	<xsl:include href="include.xsl"/>
	<xsl:include href="reorg.xsl"/>
	<xsl:include href="formulas.xsl"/>
	<xsl:include href="genexusSpecific.xsl"/>

	<!-- FROM FORMULA TEMPLATE -->
	<xsl:template match="FromFormula|FromValue">
		<xsl:apply-templates select="*"/>
	</xsl:template>


	<!-- RULES TEMPLATES -->

	<!-- RULES-->
	<xsl:template match="StandAloneRules|StandAloneWithModeRules|BaseTableRule|AfterConfirmRules|AfterInsertRules|AfterUpdateRules|BeforeConfirmRules|BeforeInsertRules|BeforeUpdateRules|BeforeDeleteRules|BeforeTrnRules">
		<xsl:apply-templates select="Action"/>
	</xsl:template>

	<xsl:template match="AfterDeleteRules|AfterTrnRules|AfterLevelRules|NotIncludedRules|AfterDisplayRules">
		<xsl:apply-templates select="Action"/>
	</xsl:template>

	<xsl:template match="Rules">
		<tr>
			<td>
				<xsl:apply-templates select="Action"/>
			</td>
		</tr>
		<xsl:if test="NonTriggeredActions/Action">
			<xsl:call-template name="TitledRules">
				<xsl:with-param name="Title">No Triggered Actions</xsl:with-param>
				<xsl:with-param name="Node">NonTriggeredActions</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template name="RptRules">
		<!-- BEGIN RPT RULES -->
		<xsl:if test="RptRules/Rules">
			<tr>
				<td colspan="2">
					<div>
						<xsl:call-template name="TableHeader">
							<xsl:with-param name="title">Rules</xsl:with-param>
						</xsl:call-template>
						<div class="LayoutNoBorder">							
							<xsl:for-each select="RptRules">
									<xsl:call-template name="LevelInfo"/>
							</xsl:for-each>
						</div>
					</div>
				</td>
			</tr>
		</xsl:if>
		<!-- END RPT RULES -->
	</xsl:template>

	<xsl:template match="NonTriggeredActions">
		<xsl:apply-templates/>
	</xsl:template>
	<!-- END RULES -->


	<!-- ACTION TEMPLATE -->
	<xsl:template match="Action">
		<table class="ActionTable">
			<tbody>
				<xsl:choose>
					<xsl:when test="ActionType[.='Formula']">
						<tr>
							<td>
								<xsl:apply-templates select="FormulaName"/>
								<xsl:text> </xsl:text>
								<xsl:apply-templates select="FormulaExpression"/>
							</td>
						</tr>
					</xsl:when>
					<xsl:when test="ActionType[.='ReadTable' or .='ReadBaseTable' or .='ReadCKey']">
						<tr>
							<td class="CommandText">READ</td>
							<td colspan="2">
								<xsl:apply-templates select="Table"/>
								<xsl:if test="ActionType[.='ReadCKey']">
									<span class="CommandText"> UNIQUE</span>
								</xsl:if>
								<xsl:if test="JoinType[.='Outer']">
									<span class="CommandText"> allowing nulls</span>
								</xsl:if>
							</td>
						</tr>
						<xsl:if test="JoinConditions">
							<tr>
								<td/>
								<td colspan="2" class="CommandText">WHERE</td>
							</tr>
							<xsl:for-each select="JoinConditions/JoinCondition">
								<tr>
									<td colspan="2"/>
									<td>
										<xsl:apply-templates select="../Table"/>.<xsl:apply-templates select="AttributeTo"/>
										<xsl:text> = </xsl:text>
										<xsl:if test="Table/TableName">
											<xsl:apply-templates select="Table"/>.
										</xsl:if>
										<xsl:apply-templates select="AttributeFrom"/>
									</td>
								</tr>
							</xsl:for-each>
						</xsl:if>
						<xsl:if test="Into/Attribute">
							<tr>
								<td/>
								<td style="vertical-align:top" class="CommandText">INTO</td>
								<td>
									<xsl:apply-templates select="Into"/>
								</td>
							</tr>
						</xsl:if>
					</xsl:when>
					<xsl:when test="ActionType[.='BusinessRule']">
						<tr>
							<td>
								<xsl:choose>
									<xsl:when test="CALL">
										<xsl:apply-templates select="CALL"/>
									</xsl:when>
									<xsl:when test="SUBMIT">
										<xsl:apply-templates select="SUBMIT"/>
									</xsl:when>
									<xsl:when test="LINK">
										<xsl:apply-templates select="LINK"/>
									</xsl:when>
									<xsl:when test="POPUP">
										<xsl:apply-templates select="POPUP"/>
									</xsl:when>
									<xsl:otherwise>
										<xsl:choose>
											<xsl:when test="RuleType[.='ErrorRule']">
												Error( <xsl:apply-templates select="Expression"/> )
											</xsl:when>
											<xsl:when test="RuleType[.='MsgRule']">
												Msg( <xsl:apply-templates select="Expression"/> )
											</xsl:when>
											<xsl:when test="RuleType[.='NoacceptRule']">
												NoAccept( <xsl:apply-templates select="Parameters"/> )
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="."/> ( <xsl:apply-templates select="Parameters"/> )
											</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
								<xsl:if test="Condition/*">
									<span class="CommandText">IF </span>
									<xsl:apply-templates select="Condition"/>
								</xsl:if>
							</td>
						</tr>
					</xsl:when>
					<xsl:when test="ActionType[.='SubType']">
						<tr>
							<td>
								<table style="border-collapse:collapse">
									<tbody>
										<xsl:apply-templates select="SubtypeAction"/>
									</tbody>
								</table>
							</td>
						</tr>
					</xsl:when>
					<xsl:when test="ActionType[.='VerticalFormulas']">
						<tr>
							<td style="font-weight:bold">
								<xsl:if test="not($Product!='Deklarit')">VERTICAL </xsl:if>
								<xsl:text>FORMULAS:</xsl:text>
								<xsl:apply-templates select="VerticalFormulasToCalc"/>
							</td>
						</tr>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<td>
								<xsl:apply-templates/>
							</td>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:if test="MissingInput">
					<tr>
						<td style="vertical-align:top" colspan="4">
							<span style="font-weight: bold;">
								<xsl:text>Missing input: </xsl:text>
							</span>
							<span>
								<xsl:apply-templates select="MissingInput"/>
							</span>
						</td>
					</tr>
				</xsl:if>
				<xsl:call-template name="AdditionalInfo"/>
			</tbody>
		</table>
	</xsl:template>

	<!-- SUBTYPEACTION TEMPLATE -->
	<xsl:template match="ActionType"/>

	<xsl:template match="DynamicLoad">
		<span class="CommandText">FILL </span>
		<xsl:apply-templates select="ControlName"/>
		<span class="CommandText"> with </span>
		<xsl:apply-templates select="CodeAttributes"/>
		<xsl:text>, </xsl:text>
		<xsl:apply-templates select="DescriptionAttributes"/>
		<span class="CommandText"> in </span>
		<xsl:apply-templates select="Navigation"/>
	</xsl:template>

	<xsl:template match="HideCode">
		<span class="CommandText">FIND </span>
		<xsl:apply-templates select="CodeAttributes"/>
		<xsl:text> with </xsl:text>
		<xsl:apply-templates select="DescriptionAttributes"/>
		<xsl:text> in </xsl:text>
		<xsl:apply-templates select="Navigation"/>
	</xsl:template>

	<xsl:template match="SubtypeAction">
		<xsl:value-of select="Supertype"/> = <xsl:value-of select="Subtype"/>
	</xsl:template>

	<xsl:template match="JoinLocation">
		<table style="width:100%;border-collapse:collapse">
			<tbody>
				<tr style="vertical-align:top;text-align:left">
					<td style="width:15%">
						<span class="NavigationFilter">Join location:</span>
					</td>
					<td style="width:85%">
						<xsl:choose>
							<xsl:when test="text()[.='1']">Server</xsl:when>
							<xsl:otherwise>Client</xsl:otherwise>
						</xsl:choose>
					</td>
				</tr>
			</tbody>
		</table>
	</xsl:template>

	<!-- RULE TYPE TEMPLATE -->
	<xsl:template match="RuleType">
		<xsl:choose>
			<xsl:when test="text()[.='ErrorRule']">
				<xsl:text>Error( </xsl:text>
				<xsl:apply-templates select="../Parameters"/>
				<xsl:text> )</xsl:text>
			</xsl:when>
			<xsl:when test="text()[.='NoacceptRule']">
				<xsl:text>NoAccept( </xsl:text>
				<xsl:apply-templates select="../Parameters"/>
				<xsl:text> )</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
				<xsl:text>( </xsl:text>
				<xsl:apply-templates select="../Parameters"/>
				<xsl:text> )</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- END RULE TEMPLATES -->

	<xsl:template match="BaseTable">
		<xsl:apply-templates select="Table"/>
	</xsl:template>

	<xsl:template match="Generator">
		<table>
			<tbody>
				<tr>
					<td>
						<xsl:apply-templates select="GenId"/>
					</td>
					<td>
						<xsl:value-of select="GenName"/>
					</td>
				</tr>
			</tbody>
		</table>
	</xsl:template>

	<!--  Standard Templates  -->

	<xsl:template match="ObjClsName">
		<xsl:call-template name="GetClsMappedName">
			<xsl:with-param name="Class" select="."/>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="MainInfo">
		<table class="TableContainer">
			<tbody>
				<tr>
					<td class="LabelText">Name:</td>
					<td>
						<xsl:apply-templates select="Object" mode="icon"/>
					</td>
				</tr>
				<xsl:if test="Object/ObjDesc">
					<tr style="border-top:1px solid #6C6C6C">
						<td class="LabelText">Description:</td>
						<td>
							<xsl:value-of select="Object/ObjDesc"/>
						</td>
					</tr>
				</xsl:if>
				<xsl:if test="Result!='genreq'">
					<tr style="border-top:1px solid #6C6C6C">
						<td class="LabelText">Status:</td>
						<td>
							<xsl:apply-templates select="Result">
								<xsl:with-param name="Class" select="Object/ObjCls"/>
								<xsl:with-param name="Id" select="Object/ObjId"/>
							</xsl:apply-templates>
						</td>
					</tr>
				</xsl:if>
				<xsl:if test="$Product='Deklarit'">
					<xsl:if test="Parameters[count(*)&gt;0]">
						<tr style="border-top:1px solid #6C6C6C">
							<td class="LabelText">Parameters:</td>
							<td>
								<xsl:apply-templates select="Parameters"/>
							</td>
						</tr>
					</xsl:if>
				</xsl:if>
				<xsl:if test="not($Product='Deklarit')">
					<xsl:if test="OutputDevices">
						<tr style="border-top:1px solid #6C6C6C">
							<td class="LabelText">Output Devices:</td>
							<td>
								<xsl:apply-templates select="OutputDevices"/>
							</td>
						</tr>
					</xsl:if>
					<xsl:if test="Main">
						<tr style="border-top:1px solid #6C6C6C">
							<td class="LabelText">Main:</td>
							<td>Yes</td>
						</tr>
					</xsl:if>
				</xsl:if>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template name="Environment">
		<table class="TableContainer">
			<tbody>
				<tr>
					<td class="LabelText">Environment:</td>
					<td>
						<xsl:apply-templates select="Generator"/>
					</td>
				</tr>
				<tr>
					<td class="LabelText">Spec. Version:</td>
					<td>
						<xsl:apply-templates select="SpecVersion"/>
					</td>
				</tr>
				<xsl:if test="FormClass">
					<tr>
						<td class="LabelText">Form Class:</td>
						<td>
							<xsl:value-of select="FormClass"/>
						</td>
					</tr>
				</xsl:if>
				<xsl:if test="Object/ObjPgmName">
					<tr>
						<td class="LabelText">Program Name:</td>
						<td>
							<xsl:value-of select="Object/ObjPgmName"/>
						</td>
					</tr>
				</xsl:if>
				<xsl:if test="CallProtocol">
					<tr>
						<td class="LabelText">Call Protocol:</td>
						<td>
							<xsl:value-of select="CallProtocol"/>
						</td>
					</tr>
				</xsl:if>
				<xsl:if test="Parameters[count(*)&gt;0]">
					<tr>
						<td class="LabelText">Parameters:</td>
						<td>
							<xsl:apply-templates select="Parameters"/>
						</td>
					</tr>
				</xsl:if>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template name="Levels">
		<!-- BEGIN LEVELS -->
		<xsl:if test="Levels/Level">
			<tr>
				<td colspan="2">
					<div>
						<xsl:call-template name="TableHeader">
							<xsl:with-param name="title">LEVELS</xsl:with-param>
						</xsl:call-template>
						<div class="LayoutNoBorder">
							<xsl:apply-templates select="Levels"/>
						</div>
					</div>
				</td>
			</tr>
		</xsl:if>
		<!-- END LEVELS -->
	</xsl:template>

	<xsl:template match="ObjectSpec">
		<div>
			<xsl:call-template name="TableHeaderMain">
				<xsl:with-param name="id">Obj</xsl:with-param>
				<xsl:with-param name="title">
					<xsl:apply-templates select="Object/ObjClsName"/>
					<xsl:text> </xsl:text>
					<xsl:apply-templates select="Object" mode="xRef"/>
					<xsl:text> Navigation Report</xsl:text>
				</xsl:with-param>
			</xsl:call-template>
			<div>
				<table class="MainContentTable">
					<tbody>
						<tr>
							<td style="vertical-align:top" class="MainInfoCell">
								<xsl:call-template name="MainInfo"/>
							</td>
							<td style="vertical-align:top" class="AdvancedInfoCell">
								<xsl:if test="not($Product='Deklarit') and not(Result='specreq')">
									<xsl:call-template name="Environment"/>
								</xsl:if>
							</td>
						</tr>
						<xsl:if test="Errors/Error or AdditionalErrors/Error">
							<xsl:comment>Printing Errors</xsl:comment>
							<tr>
								<td colspan="2" style="padding:0">
									<xsl:apply-templates select="Errors"/>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="Warnings/Warning or AdditionalWarnings/Warning">
							<xsl:comment>Printing Warnings</xsl:comment>
							<tr>
								<td colspan="2" style="padding:0">
									<xsl:apply-templates select="Warnings"/>
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
						<xsl:if test="NotIncludedRules/Action">
							<xsl:comment>Printing NotIncludeRules</xsl:comment>
							<tr>
								<td colspan="2" style="padding:5px 10px">
									<span class="ResultText">Rules not included</span>
								</td>
							</tr>
							<tr>
								<td colspan="2" style="padding:0">
									<xsl:apply-templates select="NotIncludedRules"/>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="Service/*">
							<xsl:apply-templates select="Service"/>
						</xsl:if>
						<xsl:if test="StandAloneRules[count(*)&gt;0]">
							<tr>
								<td colspan="2" style="padding:0">
									<xsl:apply-templates select="StandAloneRules"/>
								</td>
							</tr>
						</xsl:if>

						<xsl:call-template name="Levels"/>
						<xsl:call-template name="RptRules"/>

						<xsl:if test="not($Product='Deklarit')">
							<xsl:if test="MenuBar">
								<tr>
									<td colspan="2" style="padding:0">
										<xsl:apply-templates select="MenuBar"/>
									</td>
								</tr>
							</xsl:if>
							<xsl:if test="MenuOptions">
								<tr>
									<td colspan="2" style="padding:0">
										<xsl:apply-templates select="MenuOptions"/>
									</td>
								</tr>
							</xsl:if>
							<xsl:if test="Event/*">
								<xsl:apply-templates select="Event"/>
							</xsl:if>
						</xsl:if>

						<xsl:if test="ImplicitForEach">
							<xsl:for-each select="ImplicitForEach">
								<tr>
									<td colspan="2" style="padding:0">
										<xsl:apply-templates select="."/>
									</td>
								</tr>
							</xsl:for-each>
						</xsl:if>

						<xsl:if test="not($Product='Deklarit')">
							<xsl:if test="Prompts">
								<tr>
									<td colspan="2" style="padding:0">
										<xsl:apply-templates select="Prompts"/>
									</td>
								</tr>
							</xsl:if>
							<xsl:if test="DynamicCombos/DynamicCombo">
								<tr>
									<td colspan="2" style="padding:0">
										<xsl:apply-templates select="DynamicCombos"/>
									</td>
								</tr>
							</xsl:if>
							<xsl:if test="Suggests/Suggest">
								<tr>
									<td colspan="2" style="padding:0">
										<xsl:apply-templates select="Suggests"/>
									</td>
								</tr>
							</xsl:if>
						</xsl:if>
					</tbody>
				</table>
			</div>
		</div>
	</xsl:template>


	<xsl:template match="SpecVersion">
		<table style="border-collapse:collapse">
			<tbody>
				<tr>
					<td>
						<xsl:call-template name="RenderImage">
							<xsl:with-param name="Id">Spec</xsl:with-param>
						</xsl:call-template>
					</td>
					<td>
						<xsl:value-of select="."/>
					</td>
				</tr>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="AttriName">
		<xsl:if test="position() != 1">, </xsl:if>
		<xsl:apply-templates/>
	</xsl:template>

	<!-- Generic Navigation -->
	<xsl:template match="Navigation">
		<xsl:choose>
			<xsl:when test="DataSource">
				<xsl:for-each select="DataSource">
					<xsl:call-template name="ProcessList"/>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<div>
					<ul>
						<xsl:apply-templates select="NavigationTree/Table" mode="Tree"/>
						<li style="list-style: none"></li>
						<xsl:if test="NavigationConditions/*">
							<li style="list-style: none">
								<span class="CommandText">Where </span>
								<xsl:apply-templates select="NavigationConditions"/>
							</li>
						</xsl:if>
						<li style="list-style: none">
							<span class="CommandText">Order </span>
							<xsl:apply-templates select="NavigationOrder"/>
						</li>
					</ul>
				</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="NavigationConditions">
		<xsl:for-each select="Condition">
			<xsl:if test="position() != 1">
				<span class="CommandText"> And </span>
			</xsl:if>
			<xsl:call-template name="ProcessList"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="NavigationOrder">
		<xsl:apply-templates select="Order"/>
		<xsl:apply-templates select="IndexName"/>
	</xsl:template>

	<xsl:template match="Order">
		<xsl:choose>
			<xsl:when test="*">
				<xsl:call-template name="ProcessList"/>
			</xsl:when>
			<xsl:otherwise>
				<span class="CommandText">None</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="IndexName">
		<xsl:text>Index: </xsl:text>
		<xsl:value-of select="."/>
	</xsl:template>

	<xsl:template match="NavigationTree">
		<div>
			<ul>
				<xsl:apply-templates select="Table" mode="Tree"/>
			</ul>
		</div>
	</xsl:template>

	<xsl:template match="KeyAttributes">
		<xsl:text> ( </xsl:text>
		<span style="font-style:italic">
			<xsl:call-template name="ProcessList">
				<xsl:with-param name="Sep">, </xsl:with-param>
			</xsl:call-template>
		</span>
		<xsl:text> )</xsl:text>
	</xsl:template>

	<!-- LEVEL TEMPLATE -->

	<xsl:template match="Levels">
		<table class="LayoutNoBorder">
			<tbody>
				<xsl:for-each select="Level">
					<tr>
						<td style="padding:0">
							<xsl:apply-templates select="."/>
						</td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="Optimizations">
		<xsl:if test="Optimization">
			<table style="width:100%;border-collapse:collapse">
				<tbody>
					<tr style="vertical-align:top;text-align:left">
						<td style="width:15%">
							<span class="NavigationFilter">Optimizations:</span>
						</td>
						<td style="width:85%">
							<table style="width:100%;border-collapse:collapse">
								<tbody>
									<xsl:for-each select="Optimization">
										<tr style="vertical-align:top;text-align:left">
											<td>
												<xsl:apply-templates select="."/>
											</td>
										</tr>
									</xsl:for-each>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>

	<xsl:template match="Optimization">
		<xsl:choose>
			<xsl:when test="Type[.='Delete']">Delete</xsl:when>
			<xsl:when test="Type[.='Aggregate']">
				<xsl:apply-templates select="Expression"/>
			</xsl:when>
			<xsl:when test="Type[.='InsertWSubSelect']">Insert with subselect</xsl:when>
			<xsl:when test="Type[.='Update']">Update</xsl:when>
			<xsl:when test="Type[.='FirstRows']">
				First <xsl:value-of select="MaxRows"/> record(s)
			</xsl:when>
			<xsl:when test="Type[.='ServerPaging']">Server Paging</xsl:when>
			<xsl:otherwise>Unknown optimization</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="Unique">
		<table style="width:100%;border-collapse:collapse">
			<tbody>
				<tr style="vertical-align:top;text-align:left">
					<td style="width:15%">
						<span class="NavigationFilter">Unique:</span>
					</td>
					<td style="width:85%">
						<xsl:call-template name="ProcessList"/>
					</td>
				</tr>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="LevelOptions">
		<table style="width:100%;border-collapse:collapse">
			<tbody>
				<tr style="vertical-align:top;text-align:left">
					<td style="width:15%">
						<span class="NavigationFilter">Options:</span>
					</td>
					<td style="width:85%">
						<xsl:for-each select="LevelOption">
							<xsl:if test="position() != 1">, </xsl:if>
							<xsl:apply-templates select="."/>
						</xsl:for-each>
					</td>
				</tr>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="ConditionalOrders">
		<xsl:if test="ConditionalOrder">
			<table style="width:100%;border-collapse:collapse">
				<tbody>
					<tr style="vertical-align:top;text-align:left">
						<td style="width:15%">
							<span class="NavigationFilter">Order:</span>
						</td>
						<td style="width:85%">
							<table style="width:100%;border-collapse:collapse">
								<tbody>
									<xsl:for-each select="ConditionalOrder">
										<tr>
											<td colspan="2">
												<xsl:apply-templates select="Order"/>
												<xsl:choose>
													<xsl:when test="Condition/*">
														<span class="NavigationWarning"> WHEN </span>
														<xsl:apply-templates select="Condition"/>
													</xsl:when>
													<xsl:otherwise>
														<xsl:text> OTHERWISE</xsl:text>
													</xsl:otherwise>
												</xsl:choose>
											</td>
										</tr>
										<tr>
											<td/>
											<td>
												<xsl:call-template name="UsedIndex"/>
											</td>
										</tr>
									</xsl:for-each>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>

	<xsl:template match="LevelType">
		<xsl:choose>
			<xsl:when test="text()[.='For First']">
				<xsl:text>For First</xsl:text>
			</xsl:when>
			<xsl:when test="text()[.='New']">
				<xsl:text>New</xsl:text>
			</xsl:when>
			<xsl:when test="text()[.='XNew']">
				<xsl:text>XNew</xsl:text>
			</xsl:when>
			<xsl:when test="text()[.='For Each']">
				<xsl:text>For Each</xsl:text>
			</xsl:when>
			<xsl:when test="text()[.='Break']">
				<xsl:text>Break</xsl:text>
			</xsl:when>
			<xsl:when test="text()[.='XFor Each']">
				<xsl:text>XFor Each</xsl:text>
			</xsl:when>
			<xsl:when test="text()[.='Aggregate Formulas']">
				<xsl:text>Aggregate Formulas</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>Level</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="LevelBeginRow">
		<xsl:if test="$Product!='Deklarit'">
			<xsl:text>  (Line: </xsl:text>
			<xsl:value-of select="."/>
			<xsl:text>)</xsl:text>
		</xsl:if>
	</xsl:template>

	<!-- LEVEL OR FOREACH -->
	<xsl:template match="Level|ImplicitForEach">
		<xsl:if test="BaseTable">
			<xsl:choose>
				<xsl:when test="not(../ImplicitForEach)">
					<div>
						<xsl:call-template name="TableHeader">
							<xsl:with-param name="title">
								<xsl:apply-templates select="LevelType"/>
								<xsl:text> </xsl:text>
								<xsl:apply-templates select="BaseTable"/>
								<xsl:apply-templates select="LevelBeginRow"/>
							</xsl:with-param>
							<xsl:with-param name="tableClass">HeaderTableRight</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="LevelInfo"/>
					</div>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="LevelInfo"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
		<xsl:if test="LevelType[text()='Undefined']">
			<xsl:apply-templates select="Event"/>
		</xsl:if>
	</xsl:template>

	<xsl:template name="LevelInfo">
		<div class="LayoutNoBorder">
			<table class="LayoutNoBorder">
				<tbody>
					<tr>
						<td>
							<table class="TableLevelInfo">
								<tbody>
									<xsl:if test="SubordinateTo">
										<tr>
											<td>
												<xsl:apply-templates select="SubordinateTo"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="LevelType[text()!='New']">
										<xsl:if test="Order">
											<tr>
												<td>
													<table class="LayoutNoBorder">
														<tbody>
															<tr>
																<td style="vertical-align:top;text-align:left;width:15%">
																	<span class="NavigationFilter">Order:</span>
																</td>
																<td style="width:85%">
																	<table style="border-collapse:collapse">
																		<tbody>
																			<tr>
																				<td colspan="2">
																					<xsl:apply-templates select="Order"/>
																					<xsl:text> </xsl:text>
																				</td>
																			</tr>
																			<tr>
																				<td/>
																				<td>
																					<xsl:call-template name="UsedIndex"/>
																				</td>
																			</tr>
																		</tbody>
																	</table>
																</td>
															</tr>
														</tbody>
													</table>
												</td>
											</tr>
										</xsl:if>
									</xsl:if>
									<xsl:if test="Unique">
										<tr>
											<td>
												<xsl:apply-templates select="Unique"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="LevelOptions/*">
										<tr>
											<td>
												<xsl:apply-templates select="LevelOptions"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="ConditionalOrders">
										<tr>
											<td>
												<xsl:apply-templates select="ConditionalOrders"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="OptimizedWhere">
										<tr>
											<td>
												<xsl:apply-templates select="OptimizedWhere"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="NonOptimizedWhere">
										<tr>
											<td>
												<xsl:apply-templates select="NonOptimizedWhere"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="JoinLocation">
										<tr>
											<td>
												<xsl:apply-templates select="JoinLocation"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="Optimizations">
										<tr>
											<td>
												<xsl:apply-templates select="Optimizations"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="StandAloneRules[count(*)&gt;0]">
										<tr>
											<td>
												<xsl:apply-templates select="StandAloneRules"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="BaseTableRule">
										<tr>
											<td>
												<xsl:apply-templates select="BaseTableRule"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="StandAloneWithModeRules[count(*)&gt;0]">
										<tr>
											<td>
												<xsl:apply-templates select="StandAloneWithModeRules"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="//RedundantFormulas[count(*)&gt;0]">
										<tr>
											<td>
												<xsl:apply-templates select="//RedundantFormulas"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="AfterAttributeRules/Action|AfterAttributeRules/NonTriggeredActions">
										<xsl:call-template name="TitledRules">
											<xsl:with-param name="Title">After Attribute Rules</xsl:with-param>
											<xsl:with-param name="Node">AfterAttributeRules</xsl:with-param>
										</xsl:call-template>
									</xsl:if>
									<xsl:if test="Rules[count(*)&gt;0]">
										<tr>
											<td>
												<xsl:apply-templates select="Rules"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="BeforeConfirmRules/Action|BeforeConfirmRules/NonTriggeredActions">
										<xsl:call-template name="TitledRules">
											<xsl:with-param name="Title">Before Validate Rules</xsl:with-param>
											<xsl:with-param name="Node">BeforeConfirmRules</xsl:with-param>
										</xsl:call-template>
									</xsl:if>
									<xsl:if test="AfterConfirmRules/Action|AfterConfirmRules/NonTriggeredActions">
										<xsl:call-template name="TitledRules">
											<xsl:with-param name="Title">After Validate Rules</xsl:with-param>
											<xsl:with-param name="Node">AfterConfirmRules</xsl:with-param>
										</xsl:call-template>
									</xsl:if>
									<xsl:if test="AfterDisplayRules/Action|AfterDisplayRules/NonTriggeredActions">
										<xsl:call-template name="TitledRules">
											<xsl:with-param name="Title">After Display Rules</xsl:with-param>
											<xsl:with-param name="Node">AfterDisplayRules</xsl:with-param>
										</xsl:call-template>
									</xsl:if>
									<tr>
										<td>
											<xsl:apply-templates select="NavigationTree"/>
										</td>
									</tr>
									<xsl:if test="TablesToUpdate">
										<tr>
											<td>
												<xsl:apply-templates select="TablesToUpdate"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="Synchronize">
										<tr>
											<td>
												Load into <xsl:value-of select="Synchronize"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="DynamicLoads">
										<tr>
											<td>
												<xsl:apply-templates select="DynamicLoads"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="Formulas">
										<tr>
											<td>
												<table class="LayoutNoBorder">
													<tbody>
														<tr>
															<td>
																<xsl:apply-templates select="Formulas"/>
															</td>
														</tr>
													</tbody>
												</table>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="TablesToControlOnDelete[count(*)&gt;0]">
										<tr>
											<td>
												<xsl:apply-templates select="TablesToControlOnDelete"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="Levels[count(*)&gt;0]">
										<tr>
											<td>
												<xsl:apply-templates select="Levels"/>
											</td>
										</tr>
									</xsl:if>
									<tr>
										<td>
											<table class="LayoutNoBorder">
												<tbody>
													<xsl:for-each select="Level">
														<tr>
															<td>
																<xsl:apply-templates select="."/>
															</td>
														</tr>
													</xsl:for-each>
												</tbody>
											</table>
										</td>
									</tr>
									<xsl:if test="BeforeTrnRules/Action|BeforeTrnRules/NonTriggeredActions">
										<xsl:call-template name="TitledRules">
											<xsl:with-param name="Title">Before Complete Rules</xsl:with-param>
											<xsl:with-param name="Node">BeforeTrnRules</xsl:with-param>
										</xsl:call-template>
									</xsl:if>
									<xsl:if test="AfterTrnRules/Action|AfterTrnRules/NonTriggeredActions">
										<xsl:call-template name="TitledRules">
											<xsl:with-param name="Title">After Complete Rules</xsl:with-param>
											<xsl:with-param name="Node">AfterTrnRules</xsl:with-param>
										</xsl:call-template>
									</xsl:if>
									<xsl:if test="AfterLevelRules/Action|AfterLevelRules/NonTriggeredActions">
										<xsl:call-template name="TitledRules">
											<xsl:with-param name="Title">After Level Rules</xsl:with-param>
											<xsl:with-param name="Node">AfterLevelRules</xsl:with-param>
										</xsl:call-template>
									</xsl:if>
									<xsl:if test="Event">
										<tr>
											<td>
												<xsl:apply-templates select="Event"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="SUBMIT">
										<tr>
											<td>
												<xsl:apply-templates select="SUBMIT"/>
											</td>
										</tr>
									</xsl:if>
									<xsl:if test="Binding">
										<tr>
											<td>
												<xsl:apply-templates select="Binding"/>
											</td>
										</tr>
									</xsl:if>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</xsl:template>
	<!-- END LEVEL TEMPLATE -->


	<!-- TABLES TO UPDATE ON DELETE -->

	<xsl:template match="TablesToControlOnDelete">
		<xsl:if test="Table/TableName">
			<P>Referential integrity controls on delete:</P>
			<ul>
				<xsl:for-each select="Table">
					<li>
						<xsl:apply-templates select="."/>
						<xsl:apply-templates select="KeyAttributes"/>
					</li>
				</xsl:for-each>
			</ul>
		</xsl:if>
	</xsl:template>

	<!-- END TABLES TO UPDATE ON DELETE -->

	<xsl:template name="UsedIndex">
		<!-- Display index information only when an order has been specified -->
		<xsl:if test="Order/*">
			<xsl:choose>
				<xsl:when test="IndexName">
					<xsl:apply-templates select="IndexName"/>
				</xsl:when>
				<xsl:otherwise>
					<span class="NavigationWarning">No index!</span>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:if>
	</xsl:template>

	<xsl:template match="InputParameters|OutputParameters">
		<xsl:for-each select="Attribute|Variable">
			<xsl:if test="position() != 1">, </xsl:if>
			<xsl:apply-templates select="."/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="AttriType|AttriOldType" mode="Properties">
		<xsl:for-each select="Properties/Property">
			<xsl:apply-templates select="." mode="Attribute"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="Property" mode="Attribute">
		<xsl:if test="Value[.='Yes']">
			<xsl:value-of select="Name"/> = <xsl:value-of select="Value"/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="AttriType|AttriOldType">
		<xsl:call-template name="PrintType">
			<xsl:with-param name="Type">
				<xsl:value-of select="DataType"/>
			</xsl:with-param>
			<xsl:with-param name="Length">
				<xsl:value-of select="Presicion"/>
			</xsl:with-param>
			<xsl:with-param name="Decimals">
				<xsl:value-of select="Scale"/>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="FormulaName">
		<xsl:call-template name="ProcessList"/>
		<xsl:if test="Attribute|Variable"> = </xsl:if>
	</xsl:template>

	<xsl:template match="IF|Condition|Constraint|Expression|FormulaExpression|CodeAttributes|DescriptionAttributes|ControlName|Into|VerticalFormulasToCalc">
		<xsl:call-template name="ProcessList"/>
	</xsl:template>

	<xsl:template match="ConditionalConstraint">
		<xsl:apply-templates select="Constraint"/>
		<span class="NavigationWarning"> WHEN </span>
		<xsl:apply-templates select="Condition"/>
	</xsl:template>

	<xsl:template match="Parameters|RedundantAttris|RedundantAttrisToUpdate|FromAttrisToUpdate|AttrisToUpdate">
		<xsl:call-template name="ProcessList">
			<xsl:with-param name="Sep">, </xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="Parameter">
		<xsl:if test="not($Product='Deklarit')">
			<xsl:apply-templates select="IO"/>
		</xsl:if>
		<xsl:apply-templates select="Token"/>
		<xsl:apply-templates select="Attribute"/>
		<xsl:apply-templates select="Variable"/>
	</xsl:template>

	<xsl:template match="IO">
		<xsl:if test="text()[.!='inout']">
			<xsl:value-of select="."/>:
		</xsl:if>
	</xsl:template>

	<!-- END PROMPTS -->

	<!-- FOR EACH -->



	<!-- STATUS TEMPLATE -->
	<xsl:template match="Result">
		<xsl:param name="Class"/>
		<xsl:param name="Id"/>
		<xsl:choose>
			<xsl:when test="text()[.='genreq']">
				<span class="ResultText">Generation is required</span>
			</xsl:when>
			<xsl:when test="text()[.='specfailed']">
				<span class="ErrorText">Specification Failed</span>
			</xsl:when>
			<xsl:when test="text()[.='nogenreq']">
				<span class="ResultText">No Generation Required</span>
			</xsl:when>
			<xsl:when test="text()[.='nogenspc']">
				<span class="ResultText">No Specification Required</span>
			</xsl:when>
			<xsl:when test="text()[.='nospcreq']">
				<span class="ResultText">No Specification Required</span>
			</xsl:when>
			<xsl:when test="text()[.='specreq']">
				<span class="ResultText">
					<xsl:text>The navigation for this object was never calculated. Click </xsl:text>
					<span class="gxlink">
						<xsl:attribute name="onclick">
							<xsl:text>try{window.external.gxnavig(</xsl:text>
							<xsl:value-of select="$Class"/>,<xsl:value-of select="$Id"/>
							<xsl:text>)} catch(Exception){}</xsl:text>
						</xsl:attribute>
						<xsl:text>here</xsl:text>
					</span>
					<xsl:text> to calculate it.</xsl:text>
				</span>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="."/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- END STATUS TEMPLATE -->
	<xsl:template match="OptimizedWhere">
		<table style="width:100%;border-collapse:collapse">
			<tbody>
				<tr style="vertical-align:top;text-align:left">
					<td style="width:15%">
						<span class="NavigationFilter">Navigation filters:</span>
					</td>
					<td style="width:85%">
						<table style="width:100%;border-collapse:collapse">
							<tbody>
								<xsl:if test="../LevelType != 'Break'">
									<tr style="vertical-align:top;text-align:left">
										<td style="width:15%">Start from:</td>
										<td style="width:85%">
											<xsl:apply-templates select="StartFrom"/>
										</td>
									</tr>
								</xsl:if>
								<tr style="vertical-align:top;text-align:left">
									<td style="width:15%">Loop while:</td>
									<td style="width:85%">
										<xsl:apply-templates select="LoopWhile"/>
									</td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
	</xsl:template>

	<!-- ENDFOREACH -->

	<xsl:template match="NonOptimizedWhere">
		<xsl:if test="Condition or ConditionalConstraint">
			<table style="width:100%;border-collapse:collapse">
				<tbody>
					<tr style="vertical-align:top;text-align:left">
						<td style="width:15%">
							<span class="NavigationFilter">Constraints:</span>
						</td>
						<td style="width:85%">
							<table style="width:100%;border-collapse:collapse">
								<tbody>
									<xsl:for-each select="Condition|ConditionalConstraint">
										<tr style="vertical-align:top;text-align:left">
											<td>
												<xsl:apply-templates select="."/>
											</td>
										</tr>
									</xsl:for-each>
								</tbody>
							</table>
						</td>
					</tr>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>

	<xsl:template match="StartFrom|LoopWhile">
		<table style="width:100%;border-collapse:collapse">
			<tbody>
				<xsl:for-each select="Condition">
					<tr style="vertical-align:top;text-align:left">
						<td>
							<xsl:call-template name="ProcessList"/>
						</td>
					</tr>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="FormulaGivenAttris">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="FormulaGroupByAttris">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="Formulas">
		<xsl:apply-templates/>
	</xsl:template>

	<xsl:template match="DynamicLoads">
		<xsl:apply-templates/>
	</xsl:template>
	<!-- tableSTOUPDATE TEMPLATE -->
	<xsl:template match="TablesToUpdate">
		<table style="width:100%;border-collapse:collapse">
			<tbody>
				<xsl:for-each select="TableToUpdate">
					<xsl:apply-templates select="."/>
				</xsl:for-each>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="TableToUpdate">
		<xsl:if test="TableAction/text()[.='insert'] and ../../BeforeInsertRules/Action">
			<xsl:call-template name="TitledRulesParen">
				<xsl:with-param name="Title">Before Insert Rules</xsl:with-param>
				<xsl:with-param name="Node">BeforeInsertRules</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="TableAction/text()[.='update'] and ../../BeforeUpdateRules/Action">
			<xsl:call-template name="TitledRulesParen">
				<xsl:with-param name="Title">Before Update Rules</xsl:with-param>
				<xsl:with-param name="Node">BeforeUpdateRules</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="TableAction/text()[.='delete'] and ../../BeforeDeleteRules/Action">
			<xsl:call-template name="TitledRulesParen">
				<xsl:with-param name="Title">Before Delete Rules</xsl:with-param>
				<xsl:with-param name="Node">BeforeDeleteRules</xsl:with-param>
			</xsl:call-template>
		</xsl:if>

		<tr>
			<td>
				<table class="ActionTable">
					<tbody>
						<tr>
							<td>
								<xsl:apply-templates select="TableAction"/>
								<xsl:apply-templates select="Table"/>
								<xsl:if test="AttrisToUpdate/Attribute">
									<xsl:text> (</xsl:text>
									<xsl:apply-templates select="AttrisToUpdate"/>
									<xsl:text>)</xsl:text>
								</xsl:if>
							</td>
						</tr>
						<xsl:if test="UpdateRedundancyCall">
							<tr>
								<td>
									<xsl:text>Update Redundancy: </xsl:text>
									<xsl:apply-templates select="UpdateRedundancyCall"/>
								</td>
							</tr>
						</xsl:if>
					</tbody>
				</table>
			</td>
		</tr>

		<xsl:if test="TableAction/text()[.='insert'] and ../../AfterInsertRules/Action">
			<xsl:call-template name="TitledRulesParen">
				<xsl:with-param name="Title">After Insert Rules</xsl:with-param>
				<xsl:with-param name="Node">AfterInsertRules</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="TableAction/text()[.='update'] and ../../AfterUpdateRules/Action">
			<xsl:call-template name="TitledRulesParen">
				<xsl:with-param name="Title">After Update Rules</xsl:with-param>
				<xsl:with-param name="Node">AfterUpdateRules</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="TableAction/text()[.='delete'] and ../../AfterDeleteRules/Action">
			<xsl:call-template name="TitledRulesParen">
				<xsl:with-param name="Title">After Delete Rules</xsl:with-param>
				<xsl:with-param name="Node">AfterDeleteRules</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<xsl:template match="UpdateRedundancyCall">
		<span class="gxsource">CALL</span>
		<xsl:text>( </xsl:text>
		<xsl:value-of select="ProgramName"/>
		<xsl:if test="Token">
			<xsl:value-of select="Token"/>
		</xsl:if>
		<xsl:if test="Parameters/Parameter">
			<xsl:text>, </xsl:text>
			<xsl:value-of select="Parameters"/>
		</xsl:if>
		<xsl:text> )</xsl:text>
	</xsl:template>

	<xsl:template match="TableAction">
		<xsl:choose>
			<xsl:when test="text()[.='insert']">
				<span class="CommandText">Insert into </span>
			</xsl:when>
			<xsl:when test="text()[.='update']">
				<span class="CommandText">Update </span>
			</xsl:when>
			<xsl:when test="text()[.='delete']">
				<span class="CommandText">Delete from </span>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="TableName">
		<xsl:apply-templates/>
	</xsl:template>

	<!-- SYNCHRONIZE TEMPLATE -->
	<xsl:template match="Synchronize">
		<tr>
			<td>
				<span class="CommandText">Load into </span>
				<xsl:value-of select="text()"/>
			</td>
		</tr>
	</xsl:template>

	<!-- EVENT TEMPLATE -->
	<xsl:template match="Event|Service">
		<xsl:if test="ImplicitForEach|Level|SUBMIT|Binding|Synchronize|current()[name()='Service']">
			<tr>
				<td colspan="2">
					<div>
						<xsl:call-template name="TableHeader">
							<xsl:with-param name="title">
								<xsl:choose>
									<xsl:when test="EventType[text()='Subrutine']">Subroutine</xsl:when>
									<xsl:when test="current()[name()='Service']">Service</xsl:when>
									<xsl:otherwise>Event</xsl:otherwise>
								</xsl:choose>
								<xsl:text> </xsl:text>
								<xsl:value-of select="EventName|ServiceName"/>
								<xsl:if test="EventType[text()='Synchronizer']">
									<xsl:apply-templates select="LevelBeginRow"/>
								</xsl:if>
							</xsl:with-param>
						</xsl:call-template>
						<xsl:call-template name="AdditionalInfoWithTable"/>
						<div class="LayoutNoBorder">
							<table class="LayoutNoBorder">
								<tbody>
									<xsl:for-each select="ImplicitForEach|Level|SUBMIT|Binding|Synchronize|CALL[current()[name()='Service']]">
										<tr>
											<td>
												<xsl:apply-templates select="."/>
											</td>
										</tr>
									</xsl:for-each>
								</tbody>
							</table>
						</div>
					</div>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	<!-- END EVENT TEMPLATE -->

	<xsl:template match="EventName">
	</xsl:template>

	<xsl:template match="Binding">
		<xsl:text>Collection: </xsl:text>
		<xsl:call-template name="ProcessList"/>
	</xsl:template>

	<xsl:template match="LoadMethod">
		<xsl:if test="text()[.='Auto']">
			<xsl:text>Load command or method automatically added.</xsl:text>
		</xsl:if>
	</xsl:template>

	<!-- CALL/SUBMIT TEMPLATE -->
	<xsl:template match="CALL|LINK|POPUP">
		<xsl:choose>
			<xsl:when test="ProgramName">
				<xsl:value-of select="ProgramName"/>
			</xsl:when>
			<xsl:when test="Object">
				<xsl:apply-templates select="Object"/>
			</xsl:when>
		</xsl:choose>
		<xsl:choose>
			<xsl:when test="../LINK">
				<xsl:text>.Link(</xsl:text>
			</xsl:when>
			<xsl:when test="../POPUP">
				<xsl:text>.Popup(</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>.Call(</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="Expression"/>)
		<xsl:if test="IF/*">
			<span class="CommandText">if </span>
			<xsl:apply-templates select="IF"/>
		</xsl:if>
	</xsl:template>
	<xsl:template match="SUBMIT">
		<xsl:choose>
			<xsl:when test="ProgramName">
				<xsl:value-of select="ProgramName"/>
			</xsl:when>
			<xsl:when test="Object">
				<xsl:apply-templates select="Object"/>
			</xsl:when>
		</xsl:choose>
		<xsl:text>.Submit( </xsl:text><xsl:apply-templates select="Parameters"/>)
		<xsl:if test="IF/*">
			<span class="CommandText">if </span>
			<xsl:apply-templates select="IF"/>
		</xsl:if>
	</xsl:template>
	<!-- END CALL TEMPLATE -->

	<xsl:template name="VAttributes">
		<table style="border-collapse:collapse">
			<tbody>
				<tr>
					<td>
						<table style="border-collapse:collapse">
							<tbody>
								<xsl:for-each select="Attributes/Attribute">
									<tr>
										<td>
											<xsl:apply-templates select="."/>
											<xsl:text> </xsl:text>
										</td>
									</tr>
								</xsl:for-each>
							</tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
	</xsl:template>

	<xsl:template match="Tables">
		<xsl:if test="Table">
			<ul>
				<xsl:apply-templates select="Table" mode="Tree"/>
			</ul>
		</xsl:if>
	</xsl:template>

	<xsl:template match="Table" mode="Tree">
		<li style="list-style: none">
			<!-- Join type -->
			<xsl:comment>
				<xsl:text>Image source:</xsl:text>
				<xsl:call-template name="GetImageSrc">
					<xsl:with-param name="Path" select="/ObjSpecs/gxpath"/>
					<xsl:with-param name="Name">
						<!-- Look for the "Table" object icon -->
						<xsl:call-template name="GetClsImage">
							<xsl:with-param name="Class">7</xsl:with-param>
						</xsl:call-template>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:comment>
			<img alt="Table Icon">
				<xsl:attribute name="src">
					<xsl:call-template name="GetImageSrc">
						<xsl:with-param name="Path" select="/ObjSpecs/gxpath"/>
						<xsl:with-param name="Name">
							<!-- Look for the "Table" object icon -->
							<xsl:call-template name="GetClsImage">
								<xsl:with-param name="Class">7</xsl:with-param>
							</xsl:call-template>
						</xsl:with-param>
					</xsl:call-template>
				</xsl:attribute>
			</img>
			<xsl:choose>
				<xsl:when test="JoinType[.='Outer']">~</xsl:when>
				<xsl:otherwise>=</xsl:otherwise>
			</xsl:choose>
			<span>
				<xsl:call-template name="GxOpen">
					<xsl:with-param name="Class">7</xsl:with-param>
					<xsl:with-param name="Id" select="TableId"/>
				</xsl:call-template>
				<xsl:apply-templates select="TableName"/>
			</span>
			<xsl:apply-templates select="KeyAttributes"/>
			<xsl:if test="Into/Attribute">
				<span class="CommandText"> INTO </span>
				<xsl:apply-templates select="Into"/>
			</xsl:if>
			<xsl:apply-templates select="Tables"/>
		</li>
	</xsl:template>

	<xsl:template name="AdditionalInfo">
				<xsl:if test="AdditionalInfo">
					<xsl:for-each select="AdditionalInfo">
						<tr>
							<td style="vertical-align:top" colspan="4">
								<span style="font-weight: bold;">
									<xsl:value-of select="Title"/> 
								</span>
								<span>
									<xsl:apply-templates select="Info"/>
								</span>
							</td>
						</tr>
					</xsl:for-each>
				</xsl:if>
	</xsl:template>
	
	<xsl:template name="AdditionalInfoWithTable">
		<xsl:if test="AdditionalInfo">
			<table style="width:100%;border-collapse:collapse">
				<tbody>
						<xsl:for-each select="AdditionalInfo">
							<tr>
								<td style="vertical-align:top; padding-left:3px;">
									<span style="font-weight: bold;">
										<xsl:value-of select="Title"/> 
									</span>
									<span>
										<xsl:apply-templates select="Info"/>
									</span>
								</td>
							</tr>
						</xsl:for-each>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
