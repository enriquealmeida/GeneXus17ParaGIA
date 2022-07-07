<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"	xmlns:gx="urn:schemas-artech-com:gx">
	<xsl:include href="include.xsl"/>

	<xsl:output method="html" encoding="iso-8859-1"/>

	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
		<xsl:call-template name="HtmlHeader">
			<xsl:with-param name="ReportTitle">GeneXus Tables Report</xsl:with-param>
		</xsl:call-template>
		<body>
			<xsl:call-template name="HtmlScript"/>
			<table class="LayoutNoBorder">
				<tbody>
					<xsl:for-each select="ObjSpecs/TableList">
						<tr>
							<td>
								<xsl:apply-templates select="."/>
							</td>
						</tr>
					</xsl:for-each>
				</tbody>
			</table>
		</body>
		<xsl:text disable-output-escaping="yes">&lt;/html&gt;</xsl:text>
	</xsl:template>

	<xsl:template match="TableList">
		<div>
			<xsl:attribute name="id">
				<xsl:text>gx</xsl:text>
				<xsl:value-of select="Table/TableName"/>
			</xsl:attribute>
			<xsl:call-template name="TableHeaderMain">
				<xsl:with-param name="title">
					<xsl:text>Table </xsl:text>
					<xsl:apply-templates select="Table"/>
				</xsl:with-param>
			</xsl:call-template>
			<div>
				<table class="MainContentTable">
					<tbody>
						<tr>
							<td>
								<table class="LayoutNoBorder">
									<tbody>
										<tr>
											<td class="LabelText" style="padding:5px 10px">Name:</td>
											<td style="width:100%">
												<xsl:apply-templates select="Table" mode="icon"/>
											</td>
										</tr>
										<tr>
											<td class="LabelText" style="padding:5px 10px">Description:</td>
											<td style="width:100%">
												<xsl:value-of select="Table/TableDesc"/>
											</td>
										</tr>
										<xsl:if test="TblAssocDView">
											<tr>
												<td class="LabelText" style="padding:5px 10px">Associated to Data View:</td>
												<td style="width:100%">
													<xsl:apply-templates select="TblAssocDView"/>
												</td>
											</tr>
										</xsl:if>
									</tbody>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<xsl:apply-templates select="TblAtts"/>
							</td>
						</tr>
						<!-- Only in detailed listing -->
						<xsl:if test="TblIdxs">
							<tr>
								<td>
									<xsl:apply-templates select="TblIdxs"/>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="SubToTbls/TblSubor">
							<tr>
								<td>
									<xsl:apply-templates select="SubToTbls"/>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="SupOfTbls/TblSuper">
							<tr>
								<td>
									<xsl:apply-templates select="SupOfTbls"/>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="TblCKeys[CKey[2]]">
							<tr>
								<td>
									<xsl:apply-templates select="TblCKeys"/>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="TblPrompts/TblPrompt">
							<tr>
								<td>
									<xsl:apply-templates select="TblPrompts"/>
								</td>
							</tr>
						</xsl:if>
						<!-- end of: Only in detailed listing -->
						<xsl:if test="PropPart/OneProp">
							<tr>
								<td>
									<xsl:apply-templates select="PropPart"/>
								</td>
							</tr>
						</xsl:if>
					</tbody>
				</table>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="TblAtt" mode="TableRow">

	</xsl:template>

	<!-- TableAtts TEMPLATE -->
	<xsl:template match="TblAtts">
		<div>
			<xsl:attribute name="id">tblAtts</xsl:attribute>
			<xsl:call-template name="TableHeader">
				<xsl:with-param name="title">Table Structure</xsl:with-param>
			</xsl:call-template>
			<div class="LayoutNoBorder">
				<table class="LayoutNoBorder">
					<thead>
						<tr class="text">
							<th class="subTitulosTabla"/>
							<th class="subTitulosTabla">Name</th>
							<th class="subTitulosTabla">Description</th>
							<th class="subTitulosTabla">Type</th>
							<th class="subTitulosTabla">Formula</th>
							<th class="subTitulosTabla" style="white-space: nowrap">Subtype of</th>
						</tr>
					</thead>
					<tbody>
						<xsl:for-each select="TblAtt[not(TblAttInf) and (not(Attribute/FormulaDef) or TblAttInf)]">
							<tr>
								<xsl:apply-templates select="."/>
							</tr>
						</xsl:for-each>

						<xsl:if test="TblAtt[ TblAttInf or (Attribute/FormulaDef and not(TblAttInf))]">
							<tr>
								<td colspan="6" style="padding:0">
									<div>
										<xsl:call-template name="TableHeader">
											<xsl:with-param name="title">Formula and Inferred Attributes</xsl:with-param>
											<xsl:with-param name="tableAdditionalStyle">border-left: 1px solid #6C6C6C;padding:5px</xsl:with-param>
										</xsl:call-template>
										<div class="LayoutNoBorder">
											<table class="LayoutNoBorder">
												<tbody>
													<xsl:for-each select="TblAtt[ TblAttInf or (Attribute/FormulaDef and not(TblAttInf))]">
														<tr >
															<xsl:apply-templates select="."/>
														</tr>
													</xsl:for-each>
												</tbody>
											</table>
										</div>
									</div>
								</td>
							</tr>
						</xsl:if>
					</tbody>
				</table>
			</div>
		</div>
	</xsl:template>

	<!-- TblAttri TEMPLATE -->
	<xsl:template match="TblAtt">
		<xsl:variable name="ImageId">
			<xsl:call-template name="GetBmpAtt"/>
		</xsl:variable>
		<td style="vertical-align:top;width:2%">
			<xsl:if test="$ImageId != ''">
				<xsl:call-template name="RenderImage">
					<xsl:with-param name="Id">
						<xsl:value-of select="$ImageId"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:if>
		</td>
		<td style="vertical-align:top;width:15%">
			<xsl:apply-templates select="Attribute"/>
		</td>
		<td style="vertical-align:top;width:25%">
			<xsl:value-of select="Attribute/AttriDesc"/>
		</td>
		<td style="vertical-align:top;width:15%;white-space: nowrap">
			<xsl:call-template name="PrintType">
				<xsl:with-param name="Type">
					<xsl:value-of select="Attribute/Type"/>
				</xsl:with-param>
				<xsl:with-param name="Length">
					<xsl:choose>
						<xsl:when test="Attribute/MaxLen">
							<xsl:value-of select="Attribute/MaxLen"/>
						</xsl:when>
						<xsl:when test="Attribute/DateLen">
							<xsl:value-of select="Attribute/DateLen"/>
						</xsl:when>
						<xsl:when test="Attribute/Length">
							<xsl:value-of select="Attribute/Length"/>
						</xsl:when>
					</xsl:choose>
				</xsl:with-param>
				<xsl:with-param name="Decimals">
					<xsl:choose>
						<xsl:when test="Attribute/TimeLen">
							<xsl:value-of select="Attribute/TimeLen"/>
						</xsl:when>
						<xsl:when test="Attribute/Decimals">
							<xsl:value-of select="Attribute/Decimals"/>
						</xsl:when>
					</xsl:choose>
				</xsl:with-param>
			</xsl:call-template>

			<xsl:if test="Attribute/Sign">
				<xsl:text>-</xsl:text>
			</xsl:if>
		</td>
		<td style="vertical-align:top;text-align:left">
			<xsl:if test="Attribute/FormulaDef">
				<xsl:apply-templates select="Attribute/FormulaDef"/>
			</xsl:if>
		</td>
		<td style="vertical-align:top">
			<xsl:if test="Attribute/AttriSuper">
				<xsl:apply-templates select="Attribute/AttriSuper"/>
			</xsl:if>
		</td>
	</xsl:template>

	<!-- SubtypeOf TEMPLATE -->
	<xsl:template match="SubtypeOf">
		<xsl:apply-templates/>
	</xsl:template>

	<!-- TblAssocDView -->
	<xsl:template match="TblAssocDView">
		<xsl:apply-templates select="Object" mode="icon"/>
	</xsl:template>

	<!-- SubToTbls TEMPLATE -->
	<xsl:template match="SubToTbls">
		<xsl:call-template name="GenerateTable">
			<xsl:with-param name="ID">SubOfTbls</xsl:with-param>
			<xsl:with-param name="Title">Subordinated To</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- SupOfTbls TEMPLATE -->
	<xsl:template match="SupOfTbls">
		<xsl:call-template name="GenerateTable">
			<xsl:with-param name="ID">SupOfTbls</xsl:with-param>
			<xsl:with-param name="Title">Superordinated To</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- TABLES TEMPLATE -->
	<xsl:template match="TblSubor" mode="TableRow">
		<td>
			<xsl:apply-templates select="Table"/>
		</td>
		<td>
			<xsl:apply-templates select="TblSubBy"/>
		</td>
	</xsl:template>

	<xsl:template match="TblSuper" mode="TableRow">
		<td>
			<xsl:apply-templates select="Table"/>
		</td>
		<td>
			<xsl:apply-templates select="TblSupBy"/>
		</td>
	</xsl:template>

	<!-- Prompts TEMPLATE -->
	<xsl:template match="TblPrompts">
		<div>
			<xsl:attribute name="id">prompts</xsl:attribute>
			<xsl:call-template name="TableHeader">
				<xsl:with-param name="title">Associated Prompts</xsl:with-param>
			</xsl:call-template>
			<div style="padding:10px">
				<table style="width:100%;border-collapse:collapse" class="gxcontent">
					<tbody>
						<xsl:for-each select="TblPrompt">
							<tr>
								<td>
									<xsl:apply-templates select="Object" mode="icon"/>
								</td>
							</tr>
						</xsl:for-each>
					</tbody>
				</table>
			</div>
		</div>
	</xsl:template>


	<!-- CKEYS TEMPLATE -->
	<xsl:template match="TblCKeys">
		<div>
			<xsl:attribute name="id">ckey</xsl:attribute>
			<xsl:call-template name="TableHeader">
				<xsl:with-param name="title">Candidate Keys</xsl:with-param>
			</xsl:call-template>
			<div style="padding:10px">
				<table style="width:100%;border-collapse:collapse">
					<thead>
						<tr class="subTitulosTabla">
							<th style="text-align:left">Composition</th>
						</tr>
					</thead>
					<tbody>
						<xsl:apply-templates select="CKey"/>
					</tbody>
				</table>
			</div>
		</div>
	</xsl:template>

	<!-- CKEY TEMPLATE -->
	<xsl:template match="CKey">
		<xsl:if test="not(KeyIsPrimary)">
			<tr>
				<td>
					<xsl:for-each select="Attribute">
						<xsl:if test="position() != 1">, </xsl:if>
						<xsl:apply-templates select="."/>
					</xsl:for-each>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>

	<!-- SATTS TEMPLATE -->
	<xsl:template match="TblSubBy|TblSupBy">
		<xsl:for-each select="Attribute">
			<xsl:if test="position() != 1">, </xsl:if>
			<xsl:apply-templates select="."/>
		</xsl:for-each>
	</xsl:template>

	<!-- IndexAtts TEMPLATE -->
	<xsl:template match="IdxAtts">
		<xsl:for-each select="IdxAtt">
			<xsl:if test="position() != 1">, </xsl:if>
			<xsl:apply-templates select="Attribute"/>
			<xsl:apply-templates select="IdxOrder"/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template match="IdxOrder">
		<xsl:if test="text()[.='D']">
			<xsl:text> </xsl:text>
			<xsl:call-template name="RenderImage">
				<xsl:with-param name="Id">IdxDescending</xsl:with-param>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>

	<!-- TABLEIDXS TEMPLATE -->
	<xsl:template match="TblIdxs">
		<xsl:call-template name="GenerateTable">
			<xsl:with-param name="ID">ListTableIndices</xsl:with-param>
			<xsl:with-param name="Title">Indices</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<!-- INDEX TEMPLATE -->
	<xsl:template match="Index" mode="TableRow">
		<td>
			<xsl:value-of select="IdxName"/>
		</td>
		<td>
			<xsl:value-of select="IdxType"/>
		</td>
		<td style="white-space: nowrap">
			<xsl:apply-templates select="IdxAtts"/>
		</td>
	</xsl:template>

	<xsl:template match="text()|@*">
		<xsl:value-of select="."/>
	</xsl:template>

	<xsl:template name="GetBmpAtt">
		<xsl:choose>
			<xsl:when test="TblAttIsKey">
				Key
			</xsl:when>
			<xsl:when test="TblAttInf">
				Inferred
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="TblAttRed">
					Redundant
				</xsl:if>
				<xsl:if test="Attribute/FormulaDef">
					Formula
				</xsl:if>
				<xsl:choose>
					<xsl:when test="AllowNulls[text()='Yes']">
						Null
					</xsl:when>
					<xsl:when test="AllowNulls[text()='No']">
						Not Null
					</xsl:when>
					<xsl:otherwise>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- PropPart TEMPLATE -->
	<xsl:template match="PropPart">
		<xsl:call-template name="GenerateTable">
			<xsl:with-param name="ID">Properties</xsl:with-param>
			<xsl:with-param name="Title">Properties</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="OneProp" mode="TableRow">
		<td>
			<xsl:value-of select="Name"/>
		</td>
		<td>
			<xsl:value-of select="Value"/>
		</td>
	</xsl:template>
</xsl:stylesheet>
