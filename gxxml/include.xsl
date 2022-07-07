<?xml version='1.0' encoding='iso-8859-1'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="iso-8859-1"/>

	<xsl:variable name="Product" select="document('version.xml')/Product/Name"/>
	<xsl:variable name="Images"  select="document('images.xml')"/>
	<xsl:variable name="Classes" select="document('classes.xml')"/>

	<xsl:template name="TableHeaderMain">
		<xsl:param name="title">Table Title</xsl:param>
		<div>
			<table class="MainHeaderTable">
				<tbody>
					<tr>
						<td class="MainHeaderTitle">
							<xsl:attribute name="title">
								<xsl:value-of select="$title"/>
							</xsl:attribute>
							<xsl:value-of select="$title"/>
						</td>
						<xsl:call-template name="closeNugget"/>
					</tr>
				</tbody>
			</table>
		</div>
	</xsl:template>

	<xsl:template name="RelPathImage">
		<xsl:param name="Id">img</xsl:param>
		<xsl:text>images/</xsl:text>
		<xsl:value-of select="$Images/Images/Image[@Id=normalize-space($Id)]/RelPath"/>
	</xsl:template>

	<xsl:template name="PathImage">
		<xsl:param name="Id">img</xsl:param>

		<xsl:call-template name="GetImageSrc">
			<xsl:with-param name="Name" select="$Images/Images/Image[@Id=normalize-space($Id)]/RelPath"/>
			<xsl:with-param name="Path" select="/ObjSpecs/gxpath"/>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="TooltipImage">
		<xsl:param name="Id">img</xsl:param>
		<xsl:value-of select="$Images/Images/Image[@Id=normalize-space($Id)]/Tooltip"/>
	</xsl:template>

	<xsl:template name="RenderImage">
		<xsl:param name="Id">img</xsl:param>
		<img style="vertical-align:bottom;margin: 0px auto">
			<xsl:attribute name="alt">
				<xsl:value-of select="normalize-space($Id)"/>
			</xsl:attribute>
			<xsl:attribute name="src">
				<xsl:call-template name="PathImage">
					<xsl:with-param name="Id">
						<xsl:value-of select="$Id"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:attribute>
		</img>
	</xsl:template>

	<xsl:template name="RenderImageTooltip">
		<xsl:param name="Id">img</xsl:param>
		<xsl:param name="Tooltip">
			<xsl:value-of select="normalize-space($Id)"/>
		</xsl:param>
		<img style="margin:0px auto;vertical-align:bottom">
			<xsl:attribute name="alt">
				<xsl:value-of select="$Tooltip"/>
			</xsl:attribute>
			<xsl:attribute name="title">
				<xsl:value-of select="$Tooltip"/>
			</xsl:attribute>
			<xsl:attribute name="src">
				<xsl:call-template name="PathImage">
					<xsl:with-param name="Id">
						<xsl:value-of select="$Id"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:attribute>
		</img>
	</xsl:template>

	<xsl:template name="GenerateTable">
		<xsl:param name="ID"></xsl:param>
		<xsl:param name="Title"/>
		<xsl:param name="RowTemplate"></xsl:param>
		<xsl:param name="HeaderMain">false</xsl:param>
		<xsl:param name="Subtitulo">true</xsl:param>
		<xsl:param name="image"/>

		<xsl:variable name="Tables" select="document('tables.xml')/Tables"/>
		<xsl:choose>
			<xsl:when test="$HeaderMain = 'true'">
				<xsl:call-template name="TableHeaderMain">
					<xsl:with-param name="title" select="$Title"/>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="TableHeader">
					<xsl:with-param name="title" select="$Title"/>
					<xsl:with-param name="image" select="$image"/>
					<xsl:with-param name="width">
						<xsl:choose>
							<xsl:when test="$Tables/Table[@id = $ID]/Width">
								<xsl:value-of select="$Tables/Table[@id = $ID]/Width"/>
							</xsl:when>
							<xsl:otherwise>100%</xsl:otherwise>
						</xsl:choose>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>

		<div class="gxcontent">
			<table class="gxcontent">
				<xsl:attribute name="style">
					<xsl:text>width:</xsl:text>
					<xsl:choose>
						<xsl:when test="$Tables/Table[@id = $ID]/Width">
							<xsl:value-of select="$Tables/Table[@id = $ID]/Width"/>
						</xsl:when>
						<xsl:otherwise>100%</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
				<xsl:if test="$Subtitulo='true'">
					<thead>
						<tr class="subTitulosTabla">
							<xsl:for-each select="$Tables/Table[@id = $ID]/Column">
								<th>
									<xsl:attribute name="style">
										<xsl:text>white-space: nowrap</xsl:text>
										<xsl:if test="@style">
											<xsl:text>;</xsl:text>
											<xsl:value-of select="@style"/>
										</xsl:if>
									</xsl:attribute>
									<xsl:if test="Name">
										<xsl:value-of select="Name"/>
									</xsl:if>
								</th>
							</xsl:for-each>
						</tr>
					</thead>
				</xsl:if>
				<tbody>
					<xsl:variable name="CurrentNode" select="."/>
					<xsl:for-each select="$Tables/Table[@id = $ID]/Content/Template">
						<xsl:variable name="Template" select="."/>
						<xsl:for-each select="$CurrentNode/*[name() = $Template]">
							<tr>
								<xsl:apply-templates select="." mode="TableRow" />
							</tr>
						</xsl:for-each>
					</xsl:for-each>
					<xsl:if test="$RowTemplate != ''">
						<xsl:variable name="CurrentNodeEx" select=".."/>
						<xsl:for-each select="$Tables/Table[@id = $ID]/Content/Template">
							<xsl:for-each select="$CurrentNodeEx/*[name() = $RowTemplate]">
								<xsl:for-each select="./*">
									<tr>
										<xsl:apply-templates select="." mode="TableRow" />
									</tr>
								</xsl:for-each>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:if>
				</tbody>
			</table>
		</div>
	</xsl:template>

	<xsl:template name="TableHeader">
		<xsl:param name="title">Table Title</xsl:param>
		<xsl:param name="image"/>
		<xsl:param name="width">100%</xsl:param>
		<xsl:param name="tableClass">HeaderTable</xsl:param>
		<xsl:param name="tableAdditionalStyle"></xsl:param>
		<div>
			<table class="HeaderTable">
				<xsl:attribute name="class">
					<xsl:value-of select="$tableClass"/>
				</xsl:attribute>
				<xsl:attribute name="style">
					<xsl:text>width:</xsl:text>
					<xsl:choose>
						<xsl:when test="/ObjSpecs/PrintVersion">
							<xsl:attribute name="style">
								<xsl:value-of select="$width"/>
								<xsl:text>;border: 1px #D6D6D6 solid</xsl:text>
							</xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$width"/>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="$tableAdditionalStyle!=''">
						<xsl:text>;</xsl:text>
						<xsl:value-of select="$tableAdditionalStyle"/>
					</xsl:if>
				</xsl:attribute>
				<tbody>
					<tr>
						<xsl:if test="$image!=''">
							<td>
								<xsl:attribute name="class">
									<xsl:value-of select="$tableClass"/>
								</xsl:attribute>
								<img alt="Image">
									<xsl:attribute name="src">
										<xsl:call-template name="GetImageSrc">
											<xsl:with-param name="Name" select="$image"/>
											<xsl:with-param name="Path" select="/ObjSpecs/gxpath"/>
										</xsl:call-template>
									</xsl:attribute>
								</img>
							</td>
						</xsl:if>
						<td style="width:100%">
							<xsl:attribute name="title">
								<xsl:value-of select="$title"/>
							</xsl:attribute>
							<xsl:attribute name="class">
								<xsl:value-of select="$tableClass"/>
							</xsl:attribute>
							<xsl:value-of select="$title"/>
						</td>
						<xsl:call-template name="closeNugget"/>
					</tr>
				</tbody>
			</table>
		</div>
	</xsl:template>

	<xsl:template name="closeNugget">
		<xsl:if test="not(/ObjSpecs/PrintVersion)">
			<td title="Hide" onclick="displayNugget()" style="text-align:right">
				<img style="cursor:pointer;width:13px;height:13px" alt="Collapse or expand section">
					<xsl:attribute name="src">
						<xsl:call-template name="GetImageSrc">
							<xsl:with-param name="Name">ReportView_CollapseSection.gif</xsl:with-param>
							<xsl:with-param name="Path" select="/ObjSpecs/gxpath"/>
						</xsl:call-template>
					</xsl:attribute>
				</img>
			</td>
		</xsl:if>
	</xsl:template>

	<xsl:template name="GxOpen">
		<xsl:param name="Class"/>
		<xsl:param name="Id"/>
		<xsl:choose>
			<xsl:when test="$Class='' or $Id='' or ($Class!=8 and $Id=0)">
				<!-- 8: Folder, [8:0] Objects Folder -->
				<xsl:attribute name="class">gxobjnotexist</xsl:attribute>
				<xsl:attribute name="title">
					<xsl:text>Invalid </xsl:text>
					<xsl:choose>
						<xsl:when test="$Class!=''">
							<xsl:call-template name="GetClsNameById">
								<xsl:with-param name="Class" select="$Class"/>
							</xsl:call-template>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>Object</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:attribute>
			</xsl:when>
			<xsl:when test="$Class!=14 and not(/ObjSpecs/PrintVersion)">
				<!-- 14: External Program -->
				<xsl:attribute name="class">gxobj</xsl:attribute>
				<xsl:attribute name="onclick">
					<xsl:text>gxopen(</xsl:text>
					<xsl:value-of select="$Class"/>
					<xsl:text>,</xsl:text>
					<xsl:value-of select="$Id"/>
					<xsl:text>)</xsl:text>
				</xsl:attribute>
				<xsl:attribute name="oncontextmenu">
					<xsl:text>gxcontextmenu(</xsl:text>
					<xsl:value-of select="$Class"/>
					<xsl:text>,</xsl:text>
					<xsl:value-of select="$Id"/>
					<xsl:text>)</xsl:text>
				</xsl:attribute>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!-- Group TEMPLATE -->
	<xsl:template match="Group">
		<SPAN>
			<xsl:call-template name="GxOpen">
				<xsl:with-param name="Class">10</xsl:with-param>
				<xsl:with-param name="Id" select="GroupId"/>
			</xsl:call-template>
			<xsl:value-of select="GroupName"/>
		</SPAN>
	</xsl:template>

	<xsl:template name="GetClsPrefix">
		<xsl:param name="Class"/>
		<xsl:value-of select="$Classes/Classes/Class[@Id=$Class]/Prefix"/>
	</xsl:template>

	<xsl:template name="GetClsNameById">
		<xsl:param name="Class"/>
		<xsl:choose>
			<xsl:when test="$Classes/Classes/Class[@Id=$Class]/MappedName">
				<xsl:value-of select="$Classes/Classes/Class[@Id=$Class]/MappedName"/>
			</xsl:when>
			<xsl:when test="$Class=9">Model</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$Classes/Classes/Class[@Id=$Class]/Name"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="GetClsMappedName">
		<xsl:param name="Class"/>
		<xsl:choose>
			<xsl:when test="$Classes/Classes/Class[Name=$Class]/MappedName">
				<xsl:value-of select="$Classes/Classes/Class[Name=$Class]/MappedName"/>
			</xsl:when>
			<xsl:when test="$Classes/Classes/Class[Name=$Class]">
				<xsl:value-of select="$Classes/Classes/Class[Name=$Class]/Name"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$Class"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="GetClsNameImage">
		<xsl:param name="Class"/>
		<xsl:param name="ImgPath"/>
		<xsl:call-template name="GetImageSrc">
			<xsl:with-param name="Name">
				<xsl:call-template name="GetClsImage">
					<xsl:with-param name="Class" select="$Class"/>
				</xsl:call-template>
			</xsl:with-param>
			<xsl:with-param name="Path" select="$ImgPath"/>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="GetClsImage">
		<xsl:param name="Class"/>
		<xsl:variable name="ClassName" select="$Classes/Classes/Class[@Id=$Class]/Name"/>
		<xsl:choose>
			<xsl:when test="$Images/Images/Image[@Id=$ClassName]/RelPath">
				<xsl:value-of select="$Images/Images/Image[@Id=$ClassName]/RelPath"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>MissingClassIcon.ico</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="GetImageSrc">
		<xsl:param name="Name"/>
		<xsl:param name="Extension"/>
		<xsl:param name="Path"/>
		<xsl:value-of select="$Path"/>
		<xsl:text>gxxml/images/</xsl:text>
		<xsl:value-of select="$Name"/>
		<xsl:value-of select="$Extension"/>
	</xsl:template>

	<xsl:template match="GenId">
		<img style="vertical-align:middle" alt="Generator">
			<xsl:attribute name="src">
				<xsl:value-of select="/ObjSpecs/gxpath"/>
				<xsl:text>gxxml/</xsl:text>
				<xsl:call-template name="GetImgGenerator">
					<xsl:with-param name="GenId">
						<xsl:value-of select="."/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:attribute>
		</img>
	</xsl:template>

	<xsl:template name="GetImgGenerator">
		<xsl:param name="GenId"/>
		<xsl:choose>
			<!-- DOS platform deprecated -->
			<!-- xsl:when test="$GenId=4" -->
			<xsl:when test="$GenId=5">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">rpg</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=6">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">gcbl</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<!-- VisualBasic platform deprecated -->
			<!--xsl:when test="$GenId=3"-->
			<!--xsl:when test="$GenId=7"-->
			<!--xsl:when test="$GenId=14"-->
			<!-- C platform deprecated -->
			<!--xsl:when test="$GenId=8"-->
			<xsl:when test="$GenId=9">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">gvfp</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=10">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">gcsvfp</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=11">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">gj</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=12">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">wgj</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=13">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">gvb</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=15">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">net</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=18">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">net</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=22">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">gr</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=27">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">gios</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=28">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">gand</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=29">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">gjs</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=31">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">gswift</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:when test="$GenId=41">
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">netcore</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="RelPathImage">
					<xsl:with-param name="Id">design</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- LinkObjectStd TEMPLATE -->
	<xsl:template name="LinkObjectStd">
		<xsl:param name="ShowClassAs">icon</xsl:param>
		<xsl:call-template name="LinkObject">
			<xsl:with-param name="ObjCls" select="ObjCls"/>
			<xsl:with-param name="ObjId" select="ObjId"/>
			<xsl:with-param name="ObjName" select="ObjName"/>
			<xsl:with-param name="ObjDesc" select="ObjTitle"/>
			<xsl:with-param name="ShowClassAs" select="$ShowClassAs"/>
		</xsl:call-template>
	</xsl:template>

	<!-- LinkObject TEMPLATE -->
	<xsl:template name="LinkObject">
		<xsl:param name="ObjCls"/>
		<xsl:param name="ObjId"/>
		<xsl:param name="ObjName"/>
		<xsl:param name="ObjDesc"/>
		<xsl:param name="ObjShortName"/>
		<xsl:param name="ShowClassAs">icon</xsl:param>
		<xsl:if test="$ObjCls!='' and $ObjId!='' and $ShowClassAs='icon'">
			<img style="vertical-align:middle" alt="Object Icon">
				<xsl:attribute name="src">
					<xsl:call-template name="GetClsNameImage">
						<xsl:with-param name="Class" select="$ObjCls"/>
						<xsl:with-param name="ImgPath" select="/ObjSpecs/gxpath"/>
					</xsl:call-template>
				</xsl:attribute>
			</img>
			<xsl:text> </xsl:text>
		</xsl:if>
		<span>
			<xsl:call-template name="GxOpen">
				<xsl:with-param name="Class" select="$ObjCls"/>
				<xsl:with-param name="Id" select="$ObjId"/>
			</xsl:call-template>

			<xsl:if test="$ShowClassAs='prefix' and $ObjCls!='' and $ObjCls!=14">
				<xsl:call-template name="GetClsPrefix">
					<xsl:with-param name="Class">
						<xsl:value-of select="$ObjCls"/>
					</xsl:with-param>
				</xsl:call-template>
				<xsl:text>:</xsl:text>
			</xsl:if>

			<xsl:choose>
				<xsl:when test="$ObjShortName!=''">
					<xsl:value-of select="$ObjShortName"/>
				</xsl:when>
				<xsl:when test="$ObjName!=''">
					<xsl:value-of select="$ObjName"/>
				</xsl:when>
				<xsl:when test="$ObjDesc!=''">
					<xsl:value-of select="$ObjDesc"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:text>???</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</span>
	</xsl:template>

	<!-- AttriSuper TEMPLATE -->
	<xsl:template match="AttriSuper" mode="Simple">
		<xsl:apply-templates select="Attribute"/>
	</xsl:template>

	<xsl:template match="AttriSuper">
		<!-- Subtype may be in more than one group -->
		<xsl:apply-templates select="Attribute"/>
		<xsl:for-each select="Group">
			<xsl:if test="position() = 1">
				<xsl:text> In </xsl:text>
			</xsl:if>
			<xsl:if test="position() != 1">
				<xsl:choose>
					<xsl:when test="position() != last()">
						<xsl:text>, </xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text> AND </xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
			<xsl:apply-templates select="."/>
		</xsl:for-each>
	</xsl:template>

	<!-- Attribute TEMPLATE -->
	<xsl:template match="Attribute">
		<xsl:if test="AttriOrder[text()='Descending']">(</xsl:if>
		<xsl:choose>
			<xsl:when test="$Product='Deklarit'">
				<a class="gxobj">
					<xsl:attribute name="href">
						Deklarit://attributes/
						<xsl:value-of select="AttriId"/>
					</xsl:attribute>
					<xsl:value-of select="AttriName"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<SPAN>
					<xsl:choose>
						<xsl:when test="Description">
							<xsl:call-template name="AttTooltip">
								<xsl:with-param name="Description">
									<xsl:value-of select="Description"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="AttriDesc">
							<xsl:call-template name="AttTooltip">
								<xsl:with-param name="Description">
									<xsl:value-of select="AttriDesc"/>
								</xsl:with-param>
							</xsl:call-template>
						</xsl:when>
						<xsl:when test="AttriName">
							<xsl:call-template name="AttTooltip"/>
						</xsl:when>
					</xsl:choose>

					<xsl:call-template name="GxOpen">
						<xsl:with-param name="Class">5</xsl:with-param>
						<xsl:with-param name="Id" select="AttriId"/>
					</xsl:call-template>

					<xsl:choose>
						<xsl:when test="AttriShortName">
							<xsl:value-of select="AttriShortName"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="AttriName"/>
						</xsl:otherwise>
					</xsl:choose>
				</SPAN>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:if test="AttriOrder[text()='Descending']">)</xsl:if>
	</xsl:template>

	<xsl:template name="AttTooltip">
		<xsl:param name="Description"/>
		<xsl:attribute name="title">
			<xsl:value-of select="AttriName"/>
			<xsl:if test="$Description != ''">
				<xsl:text> : </xsl:text>
				<xsl:value-of select="$Description"/>
			</xsl:if>
			<xsl:if test="Type">
				<xsl:text> - </xsl:text>
				<xsl:call-template name="PrintType">
					<xsl:with-param name="Type">
						<xsl:value-of select="Type"/>
					</xsl:with-param>
					<xsl:with-param name="Length">
						<xsl:value-of select="Length"/>
					</xsl:with-param>
					<xsl:with-param name="Decimals">
						<xsl:value-of select="Decimals"/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:if>
		</xsl:attribute>
	</xsl:template>

	<xsl:template name="PrintType">
		<xsl:param name="Type"/>
		<xsl:param name="Length">0</xsl:param>
		<xsl:param name="Decimals">0</xsl:param>
		<xsl:call-template name="TypeName">
			<xsl:with-param name="Type">
				<xsl:value-of select="$Type"/>
			</xsl:with-param>
		</xsl:call-template>
		<xsl:if test="$Length >= 0">
			<xsl:if test="$Type='N' or $Type='Numeric' or $Type='C' or $Type='Character' or $Type='V' or $Type='VarChar'">
				<xsl:text> (</xsl:text>
				<xsl:value-of select="$Length"/>
				<xsl:if test="$Decimals > 0 and ($Type='N' or $Type='Numeric')">
					<xsl:text>.</xsl:text>
					<xsl:value-of select="$Decimals"/>
				</xsl:if>
				<xsl:text>)</xsl:text>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<xsl:template name="TypeName">
		<xsl:param name="Type"/>
		<xsl:choose>
			<xsl:when test="$Type='C'">
				<xsl:text>Character</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='N'">
				<xsl:text>Numeric</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='T'">
				<xsl:text>DateTime</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='V'">
				<xsl:text>VarChar</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='D'">
				<xsl:text>Date</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='L'">
				<xsl:text>LongVarChar</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='O'">
				<xsl:text>Blob</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='1'">
				<xsl:text>Boolean</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='G'">
				<xsl:text>GUID</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='B'">
				<xsl:text>Image</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='A'">
				<xsl:text>Audio</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='W'">
				<xsl:text>Video</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='BF'">
				<xsl:text>BlobFile</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='GE'">
				<xsl:text>Geography</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='GP'">
				<xsl:text>GeoPoint</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='GL'">
				<xsl:text>GeoLine</xsl:text>
			</xsl:when>
			<xsl:when test="$Type='GG'">
				<xsl:text>GeoPolygon</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$Type"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- TOKEN TEMPLATE -->
	<xsl:template match="Token">
		<xsl:value-of select="."/>
	</xsl:template>

	<!-- VARIABLE TEMPLATE -->
	<xsl:template match="Variable">
		<xsl:value-of select="VarName"/>
	</xsl:template>

	<!-- table TEMPLATE -->
	<xsl:template match="Table">
		<xsl:call-template name="GralTable">
			<xsl:with-param name="ShowClassAs">none</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="Table" mode="icon">
		<xsl:call-template name="GralTable">
			<xsl:with-param name="ShowClassAs">icon</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="GralTable">
		<xsl:param name="ShowClassAs">icon</xsl:param>
		<xsl:choose>
			<xsl:when test="$Product='Deklarit'">
				<a class="gxobj">
					<xsl:attribute name="href">
						<xsl:text>Deklarit://tables/</xsl:text>
						<xsl:value-of select="TableId"/>
					</xsl:attribute>
					<xsl:value-of select="TableName"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="TableId">
					<xsl:call-template name="LinkObject">
						<xsl:with-param name="ObjCls">7</xsl:with-param>
						<xsl:with-param name="ObjId" select="TableId"/>
						<xsl:with-param name="ObjName" select="TableName"/>
						<xsl:with-param name="ObjDesc">
							<xsl:value-of select="Description"/>
							<xsl:value-of select="TableDesc"/>
						</xsl:with-param>
						<xsl:with-param name="ObjShortName" select="TableShortName"/>
						<xsl:with-param name="ShowClassAs" select="$ShowClassAs"/>
					</xsl:call-template>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Subtype Group TEMPLATE -->
	<xsl:template match="SubtypeGroup">
		<xsl:if test="SubtypeGroupId">
			<SPAN>
				<xsl:call-template name="GxOpen">
					<xsl:with-param name="Class">10</xsl:with-param>
					<xsl:with-param name="Id" select="SubtypeGroupId"/>
				</xsl:call-template>
				<xsl:value-of select="SubtypeGroupName"/>
			</SPAN>
		</xsl:if>
	</xsl:template>


	<!-- Object TEMPLATE -->
	<xsl:template match="Object">
		<xsl:call-template name="GralObject">
			<xsl:with-param name="ShowClassAs">prefix</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="Object" mode="xRef">
		<xsl:call-template name="GralObject">
			<xsl:with-param name="ShowClassAs">none</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="Object" mode="icon">
		<xsl:call-template name="GralObject">
			<xsl:with-param name="ShowClassAs">icon</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="GralObject">
		<xsl:param name="ShowClassAs">icon</xsl:param>
		<xsl:choose>
			<xsl:when test="$Product='Deklarit'">
				<xsl:if test="$ShowClassAs='icon'">
					<img style="vertical-align:middle" alt="Object Icon">
						<xsl:attribute name="src">
							<xsl:value-of select="/ObjSpecs/gxpath"/>
							<xsl:text>gxxml/images/</xsl:text>
							<xsl:apply-templates select="ObjClsName"/>
							<xsl:text>.ico</xsl:text>
						</xsl:attribute>
					</img>
					<xsl:text> </xsl:text>
				</xsl:if>

				<a class="gxobj">
					<xsl:attribute name="href">
						Deklarit://objects/
						<xsl:value-of select="ObjCls"/>/<xsl:value-of select="ObjId"/>
					</xsl:attribute>
					<xsl:value-of select="ObjName"/>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="LinkObjectStd">
					<xsl:with-param name="ShowClassAs" select="$ShowClassAs"/>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="HtmlHeader">
		<xsl:param name="ReportTitle">GeneXus Report</xsl:param>
		<xsl:text disable-output-escaping="yes">&lt;head&gt;</xsl:text>
		<meta>
			<xsl:attribute name="charset">
				<xsl:choose>
					<xsl:when test="/ObjSpecs/charset">
						<xsl:value-of select="/ObjSpecs/charset"/>
					</xsl:when>
					<xsl:otherwise>windows-1252</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
		</meta>
		<link rel="stylesheet" type="text/css">
			<xsl:attribute name="href">
				<xsl:value-of select="/ObjSpecs/gxpath"/>
				<xsl:text>gxxml/genexus.css</xsl:text>
			</xsl:attribute>
		</link>
		<title>
			<xsl:value-of select="$ReportTitle"/>
		</title>
		<xsl:text disable-output-escaping="yes">&lt;/head&gt;</xsl:text>
	</xsl:template>

	<xsl:template name="HtmlScript">
		<script>
			<xsl:attribute name="type">
				<xsl:text>text/javascript</xsl:text>
			</xsl:attribute>
			<![CDATA[var sGXPath = "]]><xsl:value-of select="/ObjSpecs/gxpathesc"/><![CDATA[";
	var sGXImagesPath = "]]><xsl:value-of select="/ObjSpecs/gxpathesc"/><xsl:text>gxxml/images/</xsl:text><![CDATA[";

	function displayNugget()
	{
		try
		{
			var currel = window.event.srcElement;
			oNug = currel.parentElement.parentElement.parentElement.parentElement.parentElement.parentElement;
			var e = currel.parentElement;
			var f = oNug.children[1];
			
			ContentRegKey = "DD_CONTENT_" + document.title + "_" + oNug.id;

			if (f.length != null)
			{
				if (f[0].style.display == "none")
				{
					e[0].title = "Hide";
					e[0].children[0].src = sGXImagesPath + "ReportView_CollapseSection.gif";
					f[0].style.display = "block";
				}
				else
				{
					e[0].title = "Show";
					e[0].children[0].src = sGXImagesPath + "ReportView_ExpandSection.gif";
					f[0].style.display = "none";
				}
			}
			else
			{
				//  If contents are hidden, show them
				if (f.style.display == "none")
				{
					e.title = "Hide";
					e.children[0].src = sGXImagesPath + "ReportView_CollapseSection.gif";
					f.style.display = "block";
					display = "0";
					// if the page is not runin the correct security context then
					try { window.external.SetPref(ContentRegKey,"display");}
					catch (exception) {}

				}
				//  If contents are showing, hide them
				else
				{
					e.title = "Show";
					e.children[0].src = sGXImagesPath + "ReportView_ExpandSection.gif";
					f.style.display = "none";
					display = "1";
					try { window.external.SetPref(ContentRegKey,"hide");}
					catch (exception) {}
				}
			}
		}
		catch (exception)
		{
		}
	}

	function gxopen(nCls,nId)
	{
		try
		{
			if (nCls == 12) // Prompt
				nCls = 4;     // Work Panel
			else if (nCls == -5) // Formula
				nCls = 5;          // Attribute
			window.external.gxopen(nCls,nId);
		}
		catch (exception)
		{
		}
	}

	function gxopenpart(nCls,nId,sPart,sLocation)
	{
		try
		{
			window.external.gxopenpart(nCls,nId,sPart,sLocation);
		}
		catch (exception)
		{
		}
	}

	function gxcontextmenu(nCls,nId)
	{
		try
		{
			var el=window.event;
			if (nCls == 12) // Prompt
				nCls = 4;     // Work Panel
			else if (nCls == 11) // Domain
				nCls = 5;          // Attribute
			else if (nCls == -5) // Formula
				nCls = 5;          // Attribute
			window.external.gxcontextmenu(nCls,nId,el.screenX,el.screenY);
		}
		catch (exception)
		{
		}
		window.event.returnValue=false;
	}

	function gxopenhelp(sHelpStr)
	{
		try
		{
			window.external.gxopenhelp(sHelpStr);
		}
		catch (exception)
		{
		}
		window.event.returnValue=false;
		}]]>
		</script>
	</xsl:template>

	<xsl:template name="ProperyValue">
		<xsl:param name="Name"></xsl:param>
		<xsl:param name="Value"></xsl:param>
		<tr>
			<td>
				<xsl:value-of select="$Name"/>
			</td>
			<td>=</td>
			<td>
				<xsl:value-of select="$Value"/>
			</td>
		</tr>
	</xsl:template>

	<xsl:template name="ChangeObject">
		<xsl:param name="ChgType">none</xsl:param>
		<xsl:choose>
			<xsl:when test="$ChgType = 'N'">
				<span class="gxnew">New</span>
			</xsl:when>
			<xsl:when test="$ChgType = 'U'">
				<span class="gxnew">Upd</span>
			</xsl:when>
			<xsl:when test="$ChgType = 'D'">
				<span class="gxremoved">Del</span>
			</xsl:when>
			<xsl:otherwise/>
		</xsl:choose>
	</xsl:template>

	<!-- WARNINGS -->
	<xsl:template match="Warnings">
		<xsl:call-template name="GenerateTable">
			<xsl:with-param name="ID">Warnings</xsl:with-param>
			<xsl:with-param name="Title">Warnings</xsl:with-param>
			<xsl:with-param name="RowTemplate">AdditionalWarnings</xsl:with-param>
			<xsl:with-param name="Subtitulo">false</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="Warning" mode="TableRow">
		<td style="vertical-align:top; margin: 0px 10px" class="WarningText">
			<img alt="Warning Icon">
				<xsl:attribute name="src">
					<xsl:call-template name="GetImageSrc">
						<xsl:with-param name="Name" select="$Images/Images/Image[@Id='Warning']/RelPath"/>
						<xsl:with-param name="Path" select="/ObjSpecs/gxpath"/>
					</xsl:call-template>
				</xsl:attribute>
			</img>
		</td>
		<td style="vertical-align:top; margin: 0px 10px" class="WarningText">
			<span class="gxlink">
				<xsl:attribute name="onclick">
					<xsl:text>gxopenhelp('</xsl:text>
					<xsl:value-of select="MsgCode"/>
					<xsl:text>')</xsl:text>
				</xsl:attribute>
				<xsl:value-of select="MsgCode"/>
			</span>
		</td>
		<td style="vertical-align:top; width:100%" class="WarningText">
			<xsl:apply-templates select="Message"/>
			<xsl:apply-templates select="Location">
				<xsl:with-param name="ObjId" select="../../Object/ObjId"/>
				<xsl:with-param name="ObjCls" select="../../Object/ObjCls"/>
			</xsl:apply-templates>
		</td>
	</xsl:template>

	<!-- End warnings -->

	<!-- ERRORS -->
	<xsl:template match="Errors">
		<xsl:call-template name="GenerateTable">
			<xsl:with-param name="ID">Errors</xsl:with-param>
			<xsl:with-param name="Title">Errors</xsl:with-param>
			<xsl:with-param name="RowTemplate">AdditionalErrors</xsl:with-param>
			<xsl:with-param name="Subtitulo">false</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="Error" mode="TableRow">
		<td style="vertical-align:top; margin: 0px 10px" class="ErrorText">
			<img alt="Error Icon">
				<xsl:attribute name="src">
					<xsl:call-template name="GetImageSrc">
						<xsl:with-param name="Name" select="$Images/Images/Image[@Id='Error']/RelPath"/>
						<xsl:with-param name="Path" select="/ObjSpecs/gxpath"/>
					</xsl:call-template>
				</xsl:attribute>
			</img>
		</td>
		<td style="vertical-align:top; margin: 0px 10px" class="ErrorText">
			<span class="gxlink">
				<xsl:attribute name="onclick">
					<xsl:text>gxopenhelp('</xsl:text>
					<xsl:value-of select="MsgCode"/>
					<xsl:text>')</xsl:text>
				</xsl:attribute>
				<xsl:value-of select="MsgCode"/>
			</span>
		</td>
		<td style="vertical-align:top; width:100%" class="ErrorText">
			<xsl:apply-templates select="Message"/>
			<xsl:apply-templates select="Location">
				<xsl:with-param name="ObjId" select="../../Object/ObjId"/>
				<xsl:with-param name="ObjCls" select="../../Object/ObjCls"/>
			</xsl:apply-templates>
		</td>
	</xsl:template>
	<!-- END ERRORS -->

	<!-- INFORMATION -->
	<xsl:template match="Information">
		<xsl:call-template name="GenerateTable">
			<xsl:with-param name="ID">Information</xsl:with-param>
			<xsl:with-param name="Title">Information</xsl:with-param>
			<xsl:with-param name="RowTemplate">AdditionalInformation</xsl:with-param>
			<xsl:with-param name="Subtitulo">false</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template match="InfoMsg" mode="TableRow">
		<td style="vertical-align:top; margin: 0px 10px" class="MessageText">
			<img alt="Warning Icon">
				<xsl:attribute name="src">
					<xsl:call-template name="GetImageSrc">
						<xsl:with-param name="Name" select="$Images/Images/Image[@Id='Message']/RelPath"/>
						<xsl:with-param name="Path" select="/ObjSpecs/gxpath"/>
					</xsl:call-template>
				</xsl:attribute>
			</img>
		</td>
		<td style="vertical-align:top; margin: 0px 10px" class="MessageText">
			<span class="gxlink">
				<xsl:attribute name="onclick">
					<xsl:text>gxopenhelp('</xsl:text>
					<xsl:value-of select="MsgCode"/>
					<xsl:text>')</xsl:text>
				</xsl:attribute>
				<xsl:value-of select="MsgCode"/>
			</span>
		</td>
		<td style="vertical-align:top; width:100%" class="MessageText">
			<xsl:apply-templates select="Message"/>
			<xsl:apply-templates select="Location">
				<xsl:with-param name="ObjId" select="../../Object/ObjId"/>
				<xsl:with-param name="ObjCls" select="../../Object/ObjCls"/>
			</xsl:apply-templates>
		</td>
		<xsl:call-template name="AdditionalInfoRows"/>
	</xsl:template>

	<xsl:template name="AdditionalInfoRows">
				<xsl:if test="AdditionalInfo">
					<xsl:for-each select="AdditionalInfo">
						<tr class="NoSpacing">
							<td/>
							<td style="vertical-align:top" colspan="4">
								<span style="font-weight: bold;">
									<xsl:value-of select="Title"/> 
								</span>
							</td>
						</tr>
						<xsl:for-each select="Info">
							<tr class="NoSpacing">
								<td/>
								<td/>
								<td style="vertical-align:top" colspan="4">
									<span>
										<xsl:apply-templates select="."/>
									</span>
								</td>
							</tr>
						</xsl:for-each>
					</xsl:for-each>
				</xsl:if>
	</xsl:template>

	<!-- END INFORMATION -->

	<xsl:template match="Location">
		<xsl:param name="ObjId"/>
		<xsl:param name="ObjCls"/>
		<xsl:if test="Type">
			<xsl:text> (</xsl:text>
			<span>
				<xsl:variable name="xObjCls">
					<xsl:choose>
						<xsl:when test="ObjCls">
							<xsl:value-of select="ObjCls"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$ObjCls"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="xObjId">
					<xsl:choose>
						<xsl:when test="ObjId">
							<xsl:value-of select="ObjId"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$ObjId"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>

				<xsl:if test="$xObjId != '' and $xObjCls != ''">
					<xsl:attribute name="class">gxlink</xsl:attribute>
					<xsl:attribute name="onclick">
						<xsl:text>gxopenpart(</xsl:text>
						<xsl:value-of select="$xObjCls"/>
						<xsl:text>,</xsl:text>
						<xsl:value-of select="$xObjId"/>
						<xsl:text>,'</xsl:text>
						<xsl:value-of select="Type"/>
						<xsl:text>','</xsl:text>
						<xsl:choose>
							<xsl:when test="Data">
								<xsl:value-of select="Data"/>
								<xsl:if test="Line">
									<xsl:text>;</xsl:text>
									<xsl:value-of select="Line"/>
								</xsl:if>
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="Line"/>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:text>')</xsl:text>
					</xsl:attribute>
				</xsl:if>

				<xsl:value-of select="Type"/>
				<xsl:choose>
					<xsl:when test="Text">
						<xsl:text>, </xsl:text>
						<xsl:value-of select="Text"/>
					</xsl:when>
					<xsl:when test="Line">
						<xsl:text>, Line: </xsl:text>
						<xsl:value-of select="Line"/>
					</xsl:when>
				</xsl:choose>
			</span>
			<xsl:text>)</xsl:text>
		</xsl:if>
	</xsl:template>

	<xsl:template match="Message">
		<!-- Copied from msg.xsl -->
		<xsl:for-each select="Attribute|Subtype|Token|Variable|Table|Index|SubtypeGroup|Sp">
			<xsl:apply-templates select="."/>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="ProcessList">
		<xsl:param name="Sep"/>
		<xsl:for-each select="Token|Attribute|Variable|Object|Table|Parameter|SubtypeGroup|Icon">
			<xsl:choose>
				<xsl:when test="text()[.=$Sep]"></xsl:when>
				<xsl:when test="starts-with(text(),$Sep) and string-length($Sep) > 0">
					<xsl:value-of select="$Sep"/>
					<xsl:value-of select="substring-after(text(),$Sep)"/>
				</xsl:when>
				<xsl:when test="starts-with(text(),';')">
					<xsl:apply-templates select="." />
				</xsl:when>
				<xsl:when test="starts-with(text(),'.')">
					<xsl:apply-templates select="." />
				</xsl:when>
				<xsl:when test="starts-with(text(),')')">
					<xsl:apply-templates select="." />
				</xsl:when>
				<xsl:when test="starts-with(text(),' ')">
					<xsl:apply-templates select="." />
				</xsl:when>
				<xsl:when test="position() != 1">
					<xsl:value-of select="$Sep"/>&#160;<xsl:apply-templates select="." />
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="." />
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="TitledRules">
		<xsl:param name="Title">Title</xsl:param>
		<xsl:param name="Node">AfterConfirmRules</xsl:param>
		<tr style="font-weight: bold;">
			<td>
				<xsl:value-of select="$Title"/>
			</td>
		</tr>
		<tr>
			<td style="padding-left:20px;">
				<xsl:variable name="CurrentNode" select="."/>
				<xsl:for-each select="$CurrentNode/*[name() = $Node]">
					<xsl:apply-templates select="."/>
					<xsl:if test="NonTriggeredActions/Action">
						<table>
							<tbody>
								<xsl:call-template name="TitledRules">
									<xsl:with-param name="Title">No Triggered Actions</xsl:with-param>
									<xsl:with-param name="Node">NonTriggeredActions</xsl:with-param>
								</xsl:call-template>
							</tbody>
						</table>
					</xsl:if>
				</xsl:for-each>
			</td>
		</tr>
	</xsl:template>

	<xsl:template name="TitledRulesParen">
		<xsl:param name="Title">Title</xsl:param>
		<xsl:param name="Node">AfterConfirmRules</xsl:param>
		<tr style="font-weight: bold;">
			<td>
				<xsl:value-of select="$Title"/>
			</td>
		</tr>
		<tr>
			<td style="padding-left:20px;">
				<xsl:variable name="CurrentNode" select="../.."/>
				<xsl:for-each select="$CurrentNode/*[name() = $Node]">
					<xsl:apply-templates select="."/>
				</xsl:for-each>
			</td>
		</tr>
	</xsl:template>

	<!-- Token TEMPLATE -->
	<xsl:template match="Token">
		<xsl:value-of select="."/>
	</xsl:template>

	<!-- Sp TEMPLATE -->
	<xsl:template match="Sp">
		<xsl:text> </xsl:text>
	</xsl:template>

	<!-- FormulaDef TEMPLATE -->
	<xsl:template match="FormulaDef">
		<xsl:for-each select="Token|Attribute|Object">
			<xsl:apply-templates select="." />
		</xsl:for-each>
	</xsl:template>

	<!-- Icon TEMPLATE -->
	<xsl:template match="Icon">
		<xsl:text> </xsl:text>
		<xsl:call-template name="RenderImageTooltip">
			<xsl:with-param name="Id">
				<xsl:value-of select="."/>
			</xsl:with-param>
			<xsl:with-param name="Tooltip">
				<xsl:call-template name="TooltipImage">
					<xsl:with-param name="Id">
						<xsl:value-of select="."/>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

</xsl:stylesheet>