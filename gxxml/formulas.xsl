<?xml version='1.0' encoding='iso-8859-1'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- FORMULAS TEMPLATES -->

	<!-- AGGREGATE SELECT TEMPLATE -->
	<xsl:template match="AggSelFormulas">
		<xsl:if test="Formula">
			<div>
				<xsl:call-template name="TableHeader">
					<xsl:with-param name="title">Formulas</xsl:with-param>
					<xsl:with-param name="width">100%</xsl:with-param>
				</xsl:call-template>
				<table class="NestedTable">
					<tbody>
						<xsl:for-each select="Formula">
							<tr>
								<td class="LabelText" style="white-space: nowrap">Navigation to evaluate:</td>
								<xsl:choose>
									<xsl:when test="FormulaType='aggsel'">
										<td style="width:100%">
											<xsl:apply-templates select="FormulaAttri"/>
										</td>
									</xsl:when>
									<xsl:otherwise>
										<td style="width:100%">
											<xsl:value-of select="FormulaName"/>
											<xsl:value-of select="FormulaType"/>
											<xsl:apply-templates select="FormulaOutputAttri"/>
											<xsl:text> )</xsl:text>
										</td>
									</xsl:otherwise>
								</xsl:choose>
							</tr>
							<xsl:if test="FormulaType='aggsel' and FormulaExpression">
								<tr>
									<td class="LabelText" style="white-space: nowrap">Formula:</td>
									<td style="width:100%">
										<xsl:apply-templates select="FormulaExpression"/>
									</td>
								</tr>
							</xsl:if>
							<xsl:choose>
								<xsl:when test="FormulaType='aggsel'">
									<tr>
										<td colspan="2">
											<table class="NestedTable">
												<tbody>
													<xsl:call-template name="FormulaWhereMultiple"/>
													<xsl:if test="FormulaGivenAttris">
														<tr>
															<td>Given:</td>
															<td>
																<xsl:apply-templates select="FormulaGivenAttris"/>
															</td>
														</tr>
													</xsl:if>
													<xsl:if test="FormulaIndex">
														<tr>
															<td>Index:</td>
															<td>
																<xsl:value-of select="FormulaIndex"/>
															</td>
														</tr>
													</xsl:if>
													<xsl:if test="FormulaGroupByAttris">
														<tr>
															<td>Group by:</td>
															<td>
																<xsl:apply-templates select="FormulaGroupByAttris"/>
															</td>
														</tr>
													</xsl:if>
												</tbody>
											</table>
										</td>
									</tr>
								</xsl:when>
								<xsl:otherwise>
									<tr>
										<td colspan="2">
											<table class="NestedTable">
												<tr>
													<td>Where:</td>
													<td>
														<xsl:apply-templates select="FormulaWhere"/>
													</td>
												</tr>
												<tr>
													<td>Given:</td>
													<td>
														<xsl:apply-templates select="FormulaGivenAttris"/>
													</td>
												</tr>
												<tr>
													<td>Index:</td>
													<td>
														<xsl:value-of select="FormulaIndex"/>
													</td>
												</tr>
												<tr>
													<td>Start From:</td>
													<td>
														<xsl:apply-templates select="StartFrom"/>
													</td>
												</tr>
												<tr>
													<td>Loop While:</td>
													<td>
														<xsl:apply-templates select="LoopWhile"/>
													</td>
												</tr>
												<tr>
													<td>Returning:</td>
													<td>
														<xsl:apply-templates select="FormulaReturnAttri"/>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</xsl:otherwise>
							</xsl:choose>
							<tr>
								<td colspan="2">
									<xsl:apply-templates select="NavigationTree"/>
								</td>
							</tr>
						</xsl:for-each>
					</tbody>
				</table>
			</div>
		</xsl:if>
	</xsl:template>
	<!-- END AGGREGATE SELECT TEMPLATE -->

	<!-- VERTICAL FORMULAS-->

	<xsl:template name="FormulaWhereMultiple">
		<xsl:for-each select="FormulaWhere">
			<xsl:choose>
				<xsl:when test="position() = 1">
					<tr>
						<td>Where:</td>
						<td>
							<xsl:call-template name="ProcessList"/>
						</td>
					</tr>
				</xsl:when>
				<xsl:otherwise>
					<tr>
						<td/>
						<td>
							<xsl:call-template name="ProcessList"/>
						</td>
					</tr>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="FormulaWhere">
		<xsl:call-template name="ProcessList"/>
	</xsl:template>

	<xsl:template match="VerticalFormulas">
		<xsl:if test="VerticalFormulaGroup">
			<table>
				<tbody>
					<tr>
						<td>
							<span class="gxsource">Formulas:</span>
						</td>
					</tr>
					<tr>
						<td>
							<xsl:for-each select="VerticalFormulaGroup">
								<span style="font-weight:bold">Navigation s to evaluate: </span>
								<xsl:apply-templates select="FormulasInGroup"/>
								<xsl:apply-templates select="NavigationTree"/>
							</xsl:for-each>
						</td>
					</tr>
				</tbody>
			</table>
		</xsl:if>
	</xsl:template>

	<xsl:template match="FormulaOutputAttri">
		<xsl:call-template name="ProcessList">
			<xsl:with-param name="Sep">,</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="FormulasInGroup">
		<xsl:call-template name="ProcessList"/>
	</xsl:template>

	<!-- REDUNDANTFORMULAS -->
	<xsl:template match="RedundantFormulas">
		<span>Redundant Formulas:</span>
		<xsl:apply-templates select="FormulaToUpdate"/>
	</xsl:template>

	<xsl:template match="FormulaToUpdate">
		<xsl:apply-templates/>
	</xsl:template>


</xsl:stylesheet>
