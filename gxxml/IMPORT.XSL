<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output indent="no" />
	<xsl:include href="include.xsl"/>

	<xsl:template match="/">
		<xsl:text disable-output-escaping="yes">&lt;!DOCTYPE html&gt;</xsl:text>
		<xsl:call-template name="HtmlHeader">
			<xsl:with-param name="ReportTitle">GeneXus Import Report</xsl:with-param>
		</xsl:call-template>
		<body bgcolor="#FFFFFF" text="#000000" link="#000000" alink="#000000" vlink="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
			<xsl:call-template name="HtmlScript"/>

			<form action="" id="FORM1" method="post" name="FORM1">
				<div>
					<table style="width:100%">
						<tbody>
							<xsl:for-each select="ObjSpecs/RowImport">
								<tr>
									<td>
										<xsl:apply-templates select="."/>
									</td>
								</tr>
							</xsl:for-each>
						</tbody>
					</table>
				</div>
			</form>
		</body>
		<xsl:text disable-output-escaping="yes">&lt;/html&gt;</xsl:text>
	</xsl:template>

	<xsl:template match="Name">
		<span>
			<xsl:call-template name="GxOpen">
				<xsl:with-param name="Class" select="../Class"/>
				<xsl:with-param name="Id" select="../Id"/>
			</xsl:call-template>
			<xsl:value-of select="."/>
		</span>
	</xsl:template>

	<xsl:template match="Status">
		<span class="gxstatus">
			<xsl:call-template name="GetStatusDesc">
				<xsl:with-param name="Status">
					<xsl:value-of select="."/>
				</xsl:with-param>
			</xsl:call-template>
		</span>
	</xsl:template>

	<xsl:template match="RowImport">
		<div>
			<xsl:call-template name="TableHeaderMain">
				<xsl:with-param name="title">
					<xsl:call-template name="GetClsMappedName">
						<xsl:with-param name="Class">
							<xsl:value-of select="TypeName"/>
						</xsl:with-param>
					</xsl:call-template>
					<xsl:if test="TypeName">
						<xsl:text> </xsl:text>
					</xsl:if>
					<xsl:apply-templates select="Name"/>
					<xsl:text> </xsl:text>
					<xsl:apply-templates select="Status"/>
				</xsl:with-param>
			</xsl:call-template>
			<div>
				<table class="MainContentTable">
					<tbody>
						<tr>
							<td>
								<table class="TableContainer">
									<tbody>
										<tr>
											<td class="LabelText">Name:</td>
											<td style="width:100%">
												<xsl:apply-templates select="Name"/>
												<xsl:if test="Infos/OrigTblName">
													<span style="font-weight:bold">
														<xsl:text>Original Name: </xsl:text>
														<xsl:value-of select="Infos/OrigTblName"/>
													</span>
												</xsl:if>
											</td>
										</tr>
										<tr>
											<td class="LabelText">Description:</td>
											<td style="width:100%">
												<xsl:value-of select="Description"/>
												<xsl:if test="Infos/OldObjName">
													(Old Description : <xsl:value-of select="Infos/OldObjName"/>)
												</xsl:if>
											</td>
										</tr>
										<xsl:if test="Infos/Folder">
											<tr>
												<td class="LabelText">Folder:</td>
												<td style="width:100%">
													<xsl:apply-templates select="Infos/Folder"/>
												</td>
											</tr>
										</xsl:if>
									</tbody>
								</table>
							</td>
						</tr>
						<xsl:if test="Warnings/Warning|Infos/Message">
							<tr>
								<td>
									<div>
										<xsl:call-template name="TableHeader">
											<xsl:with-param name="title">Info</xsl:with-param>
										</xsl:call-template>
										<div class="gxcontent">
											<table class="gxcontent">
												<tbody>
													<xsl:for-each select=".//Warning|.//Message">
														<xsl:apply-templates select="."/>
													</xsl:for-each>
												</tbody>
											</table>
										</div>
									</div>
								</td>
							</tr>
						</xsl:if>
						<xsl:if test="Errors/Error">
							<tr>
								<td>
									<div>
										<xsl:call-template name="TableHeader">
											<xsl:with-param name="title">Errors</xsl:with-param>
										</xsl:call-template>
										<div class="gxcontent">
											<table class="gxcontent">
												<tbody>
													<xsl:for-each select=".//Error">
														<xsl:apply-templates select="."/>
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

	<xsl:template match="Folder">
		<xsl:call-template name="RenderImage">
			<xsl:with-param name="Id">Folder</xsl:with-param>
		</xsl:call-template>
		<em>
			<xsl:text> </xsl:text>
			<xsl:choose>
				<xsl:when test="text()[.!='']">
					<xsl:value-of select="."/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>Main</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</em>
		<xsl:if test="../OldFolder">
			(Old Folder : <xsl:value-of select="../OldFolder"/>)
		</xsl:if>
	</xsl:template>

	<xsl:template match="Warning">
		<tr class="WarningRow">
			<td style="vertical-align:top">
				<xsl:call-template name="RenderImage">
					<xsl:with-param name="Id">Warning</xsl:with-param>
				</xsl:call-template>
			</td>
			<td class="WarningText">
				<span canHaveHTML="true">
					<xsl:value-of select="."/>
				</span>
			</td>
		</tr>
	</xsl:template>

	<xsl:template match="Message">
		<tr class="MessageRow">
			<td style="vertical-align:top;width:1%">
				<xsl:call-template name="RenderImage">
					<xsl:with-param name="Id">Message</xsl:with-param>
				</xsl:call-template>
			</td>
			<td>
				<span class="MessageText" canHaveHTML="true">
					<xsl:value-of select="."/>
				</span>
			</td>
		</tr>
	</xsl:template>

	<xsl:template match="Error">
		<tr class="ErrorRow">
			<td style="vertical-align:top; margin: 0px 10px" class="ErrorText">
				<xsl:call-template name="RenderImage">
					<xsl:with-param name="Id">Error</xsl:with-param>
				</xsl:call-template>
			</td>
			<td>
				<span class="ErrorText" canHaveHTML="true">
					<xsl:value-of select="."/>
				</span>
			</td>
		</tr>
	</xsl:template>

	<xsl:template match="Class">
		<img alt="Class Image">
			<xsl:attribute name="src">
				<xsl:call-template name="GetClsNameImage">
					<xsl:with-param name="Class" select="."/>
					<xsl:with-param name="ImgPath" select="/ObjSpecs/gxpath"/>
				</xsl:call-template>
			</xsl:attribute>
		</img>
	</xsl:template>

	<xsl:template name="GetStatusDesc">
		<xsl:param name="Status"/>
		<xsl:choose>
			<xsl:when test="$Status='Error'">not imported</xsl:when>
			<xsl:when test="$Status='Warning' or $Status='Ok'">imported</xsl:when>
			<xsl:otherwise>Unknow Status</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
