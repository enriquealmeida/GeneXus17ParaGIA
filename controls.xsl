<?xml version='1.0'?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:msxml="urn:schemas-microsoft-com:xslt"
	xmlns:gx="urn:shemas-artech-com:gx"
	exclude-result-prefixes="msxml gx"
	xmlns:gxca="urn:GXControlAdap">
  <xsl:output  method ="html"/>
	<xsl:template match="/" >
		<xsl:apply-templates select="/GxControl"/>
	</xsl:template>

	<xsl:template match="GxControl">
		<xsl:choose>
			<xsl:when test="@type = 'Attribute'">
				<xsl:variable name="ControlType" select="gxca:GetPropValue('ControlType')"/>
				<xsl:choose>
					<xsl:when test="$ControlType = 'Combo Box'">
						<xsl:call-template name="RenderControlCombo"/>
					</xsl:when>
					<xsl:when test="$ControlType = 'Dynamic Combo Box'">
						<xsl:call-template name="RenderControlDynamicCombo"/>
					</xsl:when>
					<xsl:when test="$ControlType = 'List Box'">
						<xsl:call-template name="RenderControlList"/>
					</xsl:when>
					<xsl:when test="$ControlType = 'Dynamic List Box'">
						<xsl:call-template name="RenderControlDynamicList"/>
					</xsl:when>
					<xsl:when test="$ControlType = 'Radio Button'">
						<xsl:call-template name="RenderControlRadio"/>
					</xsl:when>
					<xsl:when test="$ControlType = 'Check Box'">
						<xsl:call-template name="RenderControlCheck"/>
					</xsl:when>
					<xsl:when test="$ControlType = 'Edit'">
						<xsl:call-template name="RenderControlEdit"/>
					</xsl:when>
					<xsl:otherwise>
						<SPAN><xsl:value-of select="@type"/></SPAN>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="@type = 'Button'">
				<xsl:call-template name="RenderButton"/>
			</xsl:when>
			<xsl:when test="@type = 'Grid'">
				<xsl:apply-templates mode="RenderGrid" select="gxca:GetHtmlColumnsProperties('ColAttId:ColTitle:ColVisible:ControlType:AttType:ColTitleFont:ColTitleColor:ColLinesFont:ColLinesColor:title:ControlValues:ControlName:ColAutoResize:ColWidth:Height:ColCount:RowCount:ControlTitle:FieldSpecifier:Alignment:ColumnClass')"/>
			</xsl:when>
			<xsl:when test="@type = 'TextBlock'">
				<xsl:call-template name="RenderTextBlock"/>
			</xsl:when>
			<xsl:when test="@type = 'ErrorViewer'">
				<xsl:call-template name="RenderErrorViewer"/>
			</xsl:when>
			<xsl:when test="@type = 'EmbeddedPage'">
				<xsl:call-template name="RenderEmbeddedPage"/>
			</xsl:when>
			<xsl:when test="@type = 'Layout'">
				<xsl:call-template name="RenderLayout"/>
			</xsl:when>
			<xsl:otherwise>
			<span><xsl:value-of select="@type"/></span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- EmbeddedPage -->
	<xsl:template name="RenderEmbeddedPage">
		<span>
			<xsl:call-template name="AddStyleAttribute"/>
			<xsl:text>EmbeddedPage: </xsl:text>
			<xsl:value-of select="gxca:GetPropValue('ControlName')"/>
		</span>
	</xsl:template>

	<!-- Layout -->
	<xsl:template name="RenderLayout">
		<span>
			<xsl:call-template name="AddStyleAttribute"/>
			<xsl:text>Layout: </xsl:text>
			<xsl:value-of select="gxca:GetPropValue('ControlName')"/>
		</span>
	</xsl:template>

	<!-- ErrorViewer -->
	<xsl:template name="RenderErrorViewer">
		<xsl:variable name="style">
			<xsl:if test="gxca:IsPropDefault('DisplayMode') = False">
				<xsl:text>list-style-type: </xsl:text>
				<xsl:choose>
					<xsl:when test="gxca:GetPropValue('DisplayMode') = 'List'">
						<xsl:text>none</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>disc</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:text>; </xsl:text>
			</xsl:if>
			<xsl:if test="gxca:IsPropDefault('ForeColor') = False">
				<xsl:text>color: </xsl:text>
				<xsl:variable name="foreColor" select="gxca:GetPropValue('ForeColor')"/>
				<xsl:value-of select="gxca:GetHtmlColor($foreColor)"/>
				<xsl:text>; </xsl:text>
			</xsl:if>
			<xsl:if test="gxca:IsPropDefault('Font') = False">
				<xsl:variable name="font" select="gxca:GetPropValue('Font')"/>
				<xsl:value-of select="gxca:GetHtmlFontStyle($font)"/>
				<xsl:text>; </xsl:text>
			</xsl:if>
			<xsl:text>background: </xsl:text>
			<xsl:choose>
				<xsl:when test="gxca:IsPropDefault('Fill') = False and gxca:GetPropValue('Fill') = 'True'">
					<xsl:variable name="backColor" select="gxca:GetPropValue('BackColor')"/>
					<xsl:value-of select="gxca:GetHtmlColor($backColor)"/>
				</xsl:when>
				<xsl:otherwise>transparent</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<span>
			<ul>
				<xsl:call-template name="AddClassAttribute"/>
				<xsl:if test="$style != ''">
					<xsl:attribute name="style">
						<xsl:value-of select="$style"/>
					</xsl:attribute>
				</xsl:if>
				<li>
					<xsl:text>Errorviewer: </xsl:text>
					<xsl:value-of select="gxca:GetPropValue('ControlName')"/>
				</li>
			</ul>
		</span>
	</xsl:template>

	<!-- Attribute -->
	<xsl:template name="RenderControlEdit">
		<xsl:variable name="AttID" select="gxca:GetPropValue('AttID')"/>
		<xsl:variable name="Size">
			<xsl:value-of select="gxca:GetPropMeasure('GxWidth',0)"/>
		</xsl:variable>
		<xsl:variable name="AttLinesNo">
			<xsl:value-of select="gxca:GetPropMeasure('GxHeight',0)"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$AttLinesNo = '1' or $AttLinesNo = ''">
				<input type="text">
					<xsl:attribute name="value">
						<xsl:value-of select="gxca:GetDisplayName($AttID)"/>
					</xsl:attribute>
					<xsl:call-template name="AddClassAttribute"/>
					<xsl:if test="$Size != ''">
						<xsl:attribute name="size">
							<xsl:value-of select="$Size"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:call-template name="AddTitleAttribute"/>
					<xsl:call-template name="AddStyleAttribute"/>
				</input>
			</xsl:when>
			<xsl:otherwise>
				<textarea>
					<xsl:attribute name="rows">
						<xsl:value-of select="$AttLinesNo"/>
					</xsl:attribute>
					<xsl:if test="$Size != ''">
						<xsl:attribute name="cols">
							<xsl:value-of select="$Size"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:call-template name="AddClassAttribute"/>
					<xsl:call-template name="AddTitleAttribute"/>
					<xsl:call-template name="AddStyleAttribute"/>
					<xsl:value-of select="gxca:GetDisplayName($AttID)"/>
				</textarea>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="RenderControlCombo" >
		<select>
			<xsl:attribute name="name">
				<xsl:value-of select="gxca:GetPropValue('ControlName')"/>
			</xsl:attribute>
			<xsl:call-template name="AddClassAttribute"/>
			<xsl:call-template name="AddStyleAttribute"/>
			<xsl:variable name="ControlValues" select="gxca:GetPropValue('ControlValues')"/>
			<xsl:apply-templates mode="OptionsValues"  select="gxca:GetHtmlControlValues($ControlValues)"/>
		</select>
	</xsl:template>

	<xsl:template match="controlvalues" mode="OptionsValues">
		<xsl:for-each select="controlvalue">
			<option>
				<xsl:attribute name="value">
					<xsl:value-of select="@value"/>
				</xsl:attribute>
				<xsl:value-of select="@name"/>
			</option>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="RenderControlDynamicCombo" >
		<xsl:variable name="AttID" select="gxca:GetPropValue('AttID')"/>
		<xsl:variable name="Size" select="gxca:GetPropValue('ColCount')"/>
		<xsl:variable name="Font" select="gxca:GetPropValue('Font')"/>
		<xsl:variable name="TextForWidth" select="gxca:GetTextForDynamicControlsWidth($Size, $Font)"/>
		<select>
			<xsl:attribute name="name">
				<xsl:value-of select="gxca:GetPropValue('ControlName')"/>
			</xsl:attribute>
			<xsl:call-template name="AddClassAttribute"/>
			<xsl:call-template name="AddStyleAttribute"/>
			<option>        
				<xsl:value-of select="gxca:GetDisplayName($AttID)"/>
			</option>
			<xsl:if test="$TextForWidth != ''">
				<option>
					<xsl:value-of select="$TextForWidth"/>
				</option>
			</xsl:if>
		</select>
	</xsl:template>

	<xsl:template name="RenderControlList">
		<select>
			<xsl:attribute name="name">
				<xsl:value-of select="gxca:GetPropValue('ControlName')"/>
			</xsl:attribute>
			<xsl:call-template name="AddClassAttribute"/>
			<xsl:call-template name="AddStyleAttribute"/>
			<xsl:variable name="ControlValues" select="gxca:GetPropValue('ControlValues')"/>
			<xsl:choose>
				<xsl:when test="gxca:GetPropValue('AutoResize') = 'True'">
					<xsl:apply-templates mode="OptionsValuesWithSize"  select="gxca:GetHtmlControlValues($ControlValues)"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="multiple"/>
					<xsl:apply-templates mode="OptionsValues"  select="gxca:GetHtmlControlValues($ControlValues)"/>
				</xsl:otherwise>
			</xsl:choose>
		</select>
	</xsl:template>

	<xsl:template match="controlvalues" mode="OptionsValuesWithSize">
		<xsl:choose>
			<xsl:when test="@count = 0 or @count = 1">
				<xsl:attribute name="multiple"/>
				<xsl:attribute name="size">1</xsl:attribute>
			</xsl:when>
			<xsl:when test="@count > 8">
				<xsl:attribute name="size">5</xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="size">
					<xsl:value-of select="@count"/>
				</xsl:attribute>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates mode="OptionsValues" select="."/>
	</xsl:template>

	<xsl:template name="RenderControlDynamicList">
		<xsl:variable name="AttID" select="gxca:GetPropValue('AttID')"/>
		<xsl:variable name="Size" select="gxca:GetPropValue('ColCount')"/>
		<xsl:variable name="Font" select="gxca:GetPropValue('Font')"/>
		<xsl:variable name="TextForWidth" select="gxca:GetTextForDynamicControlsWidth($Size, $Font)"/>
		<select>
			<xsl:attribute name="name">
				<xsl:value-of select="gxca:GetPropValue('ControlName')"/>
			</xsl:attribute>
			<xsl:call-template name="AddClassAttribute"/>
			<xsl:call-template name="AddStyleAttribute"/>
			<xsl:choose>
				<xsl:when test="gxca:GetPropValue('AutoResize') = 'True'">
					<xsl:attribute name="size">5</xsl:attribute>
					<option/>
					<option/>
					<option>
						<xsl:value-of select="gxca:GetDisplayName($AttID)"/>
					</option>
					<option/>
					<option/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="size">3</xsl:attribute>
					<option/>
					<option>
						<xsl:value-of select="gxca:GetDisplayName($AttID)"/>
					</option>
					<option/>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="$TextForWidth != ''">
				<option>
					<xsl:value-of select="$TextForWidth"/>
				</option>
			</xsl:if>
		</select>
	</xsl:template>

	<xsl:template name="RenderControlRadio">
		<table>
			<xsl:call-template name="AddTitleAttribute"/>
			<xsl:call-template name="AddStyleAttribute"/>
			<xsl:call-template name="AddClassAttribute"/>
			<xsl:variable name="ControlValues" select="gxca:GetPropValue('ControlValues')"/>
			<xsl:apply-templates mode="RadioValues"  select="gxca:GetHtmlControlValues($ControlValues)"/>
		</table>
	</xsl:template>

	<xsl:template match="controlvalues" mode="RadioValues">
		<xsl:variable name="AttID" select="gxca:GetPropValue('AttID')"/>
		<xsl:variable name="AttName" select="gxca:GetDisplayName($AttID)"/>
		<xsl:variable name="Count" select="@count"/>
		<xsl:choose>
			<xsl:when test="$Count = '0'">
				<tr>
					<td>
						<input type="radio">
							<xsl:attribute name="name">
								<xsl:value-of select="$AttName"/>
							</xsl:attribute>
						</input>
					</td>
				</tr>
			</xsl:when>
			<xsl:otherwise>
				<xsl:choose>
					<xsl:when test="gxca:GetPropValue('ControlDirection') = 'Vertical'">
						<xsl:for-each select="controlvalue">
							<tr>
								<td>
									<input type="radio">
										<xsl:attribute name="name">
											<xsl:value-of select="$AttName"/>
										</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="@value"/>
										</xsl:attribute>
										<xsl:value-of select="@name"/>
									</input>
								</td>
							</tr>
						</xsl:for-each>
					</xsl:when>
					<xsl:otherwise>
						<tr>
							<xsl:for-each select="controlvalue">
								<td>
									<input type="radio">
										<xsl:attribute name="name">
											<xsl:value-of select="$AttName"/>
										</xsl:attribute>
										<xsl:attribute name="value">
											<xsl:value-of select="@value"/>
										</xsl:attribute>
										<xsl:value-of select="@name"/>
									</input>
								</td>
							</xsl:for-each>
						</tr>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="RenderControlCheck">
		<span>
			<xsl:call-template name="AddClassAttribute"/>
			<xsl:call-template name="AddTitleAttribute"/>
			<xsl:call-template name="AddStyleAttribute"/>
			<input type="checkbox">
				<xsl:value-of select="gxca:GetPropValue('ControlTitle')"/>
			</input>
		</span>
	</xsl:template>

	<!-- Button -->
	<xsl:template name="RenderButton">
		<xsl:variable name="borderStyle">
			<xsl:value-of select="gxca:GetPropValue('ButtonBorderStyle')"/>
		</xsl:variable>
		<xsl:variable name="caption">
			<xsl:value-of select="gxca:GetPropValue('Caption')"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$borderStyle = 'standard'">
				<input type="button">
					<xsl:attribute name="value">
						<xsl:value-of select="gxca:GetTranslationFor($caption)"/>
					</xsl:attribute>
					<xsl:call-template name="AddClassAttribute"/>
					<xsl:call-template name="AddStyleAttribute"/>
				</input>
			</xsl:when>
			<xsl:otherwise>
				<xsl:variable name="Class">
					<xsl:value-of select="gxca:GetPropValue('Class')"/>
				</xsl:variable>
				<span>
					<xsl:attribute name="class">BaseRBtn R<xsl:value-of select="$Class"/></xsl:attribute>
					<span class="BtnLeft"><span class="BtnRight"><span class="BtnBackground">
						<input type="button">
							<xsl:attribute name="value">
								<xsl:value-of select="gxca:GetTranslationFor($caption)"/>
							</xsl:attribute>
							<xsl:attribute name="class">BtnText</xsl:attribute>
						</input>
					</span></span></span>
				</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Grid -->
	<xsl:template match="Columns" mode="RenderGrid">
		<xsl:choose>
			<xsl:when test="Column">
				<xsl:variable name="count" select="@count"/>
				<xsl:variable name="class" select="gxca:GetPropValue('Class')"/>
				<xsl:variable name="autoResize" select="gxca:GetPropValue('AutoResize')"/>
				<xsl:variable name="BackColorStyle" select="gxca:GetPropValue('BackColorStyle')"/>
				<xsl:variable name="BackImage" select="gxca:GetPropValueImage('backgroundGX')"/>
				<xsl:variable name="AllHTMLBackColor">
					<xsl:if test="$BackColorStyle = 'Uniform' and gxca:IsPropDefault('AllBackColor') = False">
						<xsl:variable name="AllBackColor" select="gxca:GetPropValue('AllBackColor')"/>
						<xsl:value-of select="gxca:GetHtmlColor($AllBackColor)"/>
					</xsl:if>
				</xsl:variable>
				<xsl:variable name="applyTransparent">
					<xsl:choose>
						<xsl:when test="$BackColorStyle = 'None' or $AllHTMLBackColor != ''">
							<xsl:text>True</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>False</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
				<xsl:variable name="tableStyle">
					<xsl:if test="$autoResize = 'False'">
						<xsl:text>width: </xsl:text>
						<xsl:value-of select="gxca:GetPropValue('GxWidth')"/>
						<xsl:text>;</xsl:text>
						<xsl:text>height: </xsl:text>
						<xsl:value-of select="gxca:GetPropValue('GxHeight')"/>
						<xsl:text>;</xsl:text>
					</xsl:if>
					<xsl:if test="$AllHTMLBackColor != ''">
						<xsl:text>background: </xsl:text>
						<xsl:value-of select="$AllHTMLBackColor"/>
						<xsl:text>;</xsl:text>
					</xsl:if>
					<xsl:if test="$BackImage != ''">
						<xsl:text>background-image: url('</xsl:text>
						<xsl:value-of select="$BackImage"/>
						<xsl:text>')</xsl:text>
					</xsl:if>
				</xsl:variable>
				
				<table>
					<xsl:if test="$class != '(none)'">
						<xsl:attribute name="class">
							<xsl:value-of select="$class"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:if test="$tableStyle != ''">
						<xsl:attribute name="style">
							<xsl:value-of select="$tableStyle"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:if test="gxca:IsPropDefault('align') = False">
						<xsl:attribute name ="align">
							<xsl:value-of select="gxca:GetPropValue('align')"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:attribute name="border">
						<xsl:value-of select="gxca:GetPropValue('Border')"/>
					</xsl:attribute>
					<xsl:if test="gxca:IsPropDefault('borderColor') = False">
						<xsl:attribute name="borderColor">
							<xsl:variable name="BorderColor" select="gxca:GetPropValue('borderColor')"/>
							<xsl:value-of select="gxca:GetHtmlColor($BorderColor)"/>
						</xsl:attribute>
					</xsl:if>
					<xsl:attribute name="cellPadding">
						<xsl:value-of select="gxca:GetPropValue('cellPadding')"/>
					</xsl:attribute>
					<xsl:attribute name="cellSpacing">
						<xsl:value-of select="gxca:GetPropValue('cellSpacing')"/>
					</xsl:attribute>
					<xsl:if test="gxca:IsPropDefault('rules') = False">
						<xsl:attribute name="rules">
							<xsl:value-of select="gxca:GetPropValue('rules')"/>
						</xsl:attribute>
					</xsl:if>
					
					<tbody>
						<xsl:variable name="TitleFont">
							<xsl:if test="gxca:IsPropDefault('TitleFont') = False">
								<xsl:value-of select="gxca:GetPropValue('TitleFont')"/>
							</xsl:if>
						</xsl:variable>
						<xsl:variable name="TitleFontColor">
							<xsl:if test="gxca:IsPropDefault('TitleColor') = False">
								<xsl:value-of select="gxca:GetPropValue('TitleColor')"/>
							</xsl:if>
						</xsl:variable>
						<xsl:variable name="TitleStyle">
							<xsl:choose>
								<xsl:when test="($BackColorStyle = 'Report' or $BackColorStyle = 'Header') and gxca:IsPropDefault('TitleBackColor') = False">
									<xsl:text>background: </xsl:text>
									<xsl:variable name="TitleBackColor" select="gxca:GetPropValue('TitleBackColor')"/>
									<xsl:value-of select="gxca:GetHtmlColor($TitleBackColor)"/>
								</xsl:when>
								<xsl:when test="$applyTransparent = 'True'">
									<xsl:text>background: transparent</xsl:text>
								</xsl:when>
							</xsl:choose>
						</xsl:variable>
						
						<tr>
							<xsl:choose>
								<xsl:when test="$class = '(none)'"></xsl:when>
								<xsl:when test="$BackColorStyle = 'Uniform'">
									<xsl:attribute name="class"><xsl:value-of select="$class"/>UniformTitle</xsl:attribute>
								</xsl:when>
								<xsl:otherwise>
									<xsl:attribute name="class"><xsl:value-of select="$class"/>Title</xsl:attribute>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:if test="$TitleStyle != ''">
								<xsl:attribute name="style">
									<xsl:value-of select="$TitleStyle"/>
								</xsl:attribute>
							</xsl:if>
							
							<xsl:for-each select="Column">
								<xsl:if test="@ColVisible = 'True'">
									<xsl:variable name="ColTitleStyle">
										<xsl:choose>
											<xsl:when test="@ColTitleFont != '' and $TitleFont != @ColTitleFont">
												<xsl:value-of select="gxca:GetHtmlFontStyle(@ColTitleFont)"/>
												<xsl:text>; </xsl:text>
											</xsl:when>
											<xsl:when test="$TitleFont != ''">
												<xsl:value-of select="gxca:GetHtmlFontStyle($TitleFont)"/>
												<xsl:text>; </xsl:text>
											</xsl:when>
										</xsl:choose>
										<xsl:choose>
											<xsl:when test="@ColTitleColor != '' and $TitleFontColor != @ColTitleColor">
												<xsl:text>color: </xsl:text>
												<xsl:value-of select="gxca:GetHtmlColor(@ColTitleColor)"/>
												<xsl:text>; </xsl:text>
											</xsl:when>
											<xsl:when test="$TitleFontColor != ''">
												<xsl:text>color: </xsl:text>
												<xsl:value-of select="gxca:GetHtmlColor($TitleFontColor)"/>
												<xsl:text>; </xsl:text>
											</xsl:when>
										</xsl:choose>
										<xsl:if test="@Alignment = 'Center'">
											<xsl:text>text-align: center;</xsl:text>
										</xsl:if>
										<xsl:if test="@Alignment = 'Right'">
											<xsl:text>text-align: right;</xsl:text>
										</xsl:if>
										<xsl:if test="@ColAutoResize = 'False'">
											<xsl:text>width: </xsl:text>
											<xsl:value-of select="@ColWidth"/>
											<xsl:text>;</xsl:text>
											<xsl:text>height: </xsl:text>
											<xsl:value-of select="@ColHeight"/>
										</xsl:if>
									</xsl:variable>

									<th nowrap="nowrap">
										<xsl:if test="$ColTitleStyle != ''">
											<xsl:attribute name="style">
												<xsl:value-of select="$ColTitleStyle"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="@ColumnClass != ''">
											<xsl:attribute name="class">
												<xsl:value-of select="@ColumnClass"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:value-of select="@ColTitle"/>
									</th>
								</xsl:if>
							</xsl:for-each>
						</tr>
						
						<xsl:variable name="LinesHTMLBackColor">
							<xsl:if test="($BackColorStyle = 'Report' or $BackColorStyle = 'Header') and gxca:IsPropDefault('LinesBackColor') = False">
								<xsl:variable name="LinesBackColor" select="gxca:GetPropValue('LinesBackColor')"/>
								<xsl:value-of select="gxca:GetHtmlColor($LinesBackColor)"/>
							</xsl:if>
						</xsl:variable>
						<xsl:variable name="LinesHTMLBackColor2">
							<xsl:if test="$BackColorStyle = 'Report' and gxca:IsPropDefault('LinesBackColor2') = False">
								<xsl:variable name="LinesBackColor2" select="gxca:GetPropValue('LinesBackColor2')"/>
								<xsl:value-of select="gxca:GetHtmlColor($LinesBackColor2)"/>
							</xsl:if>
						</xsl:variable>
						<xsl:variable name="LinesFont">
							<xsl:if test="gxca:IsPropDefault('LinesFont') = False">
								<xsl:value-of select="gxca:GetPropValue('LinesFont')"/>
							</xsl:if>
						</xsl:variable>
						<xsl:variable name="LinesFontColor">
							<xsl:if test="gxca:IsPropDefault('LinesColor') = False">
								<xsl:value-of select="gxca:GetPropValue('LinesColor')"/>
							</xsl:if>
						</xsl:variable>
						<xsl:variable name="oddLinesClass">
							<xsl:choose>
								<xsl:when test="$class = '(none)'"></xsl:when>
								<xsl:when test="$BackColorStyle = 'Uniform'"><xsl:value-of select="$class"/>Uniform</xsl:when>
								<xsl:otherwise><xsl:value-of select="$class"/>Odd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:variable name="evenLinesClass">
							<xsl:choose>
								<xsl:when test="$class = '(none)'"></xsl:when>
								<xsl:when test="$BackColorStyle = 'Uniform'"><xsl:value-of select="$class"/>Uniform</xsl:when>
								<xsl:when test="$BackColorStyle = 'Report'"><xsl:value-of select="$class"/>Even</xsl:when>
								<xsl:otherwise><xsl:value-of select="$class"/>Odd</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
						<xsl:variable name="commonLinesStyle">
							<xsl:if test="$applyTransparent = 'True'">
								<xsl:text>background: transparent</xsl:text>
							</xsl:if>
						</xsl:variable>
						<xsl:variable name="oddLinesStyle">
							<xsl:choose>
								<xsl:when test="$commonLinesStyle != ''">
									<xsl:value-of select="$commonLinesStyle"/>
								</xsl:when>
								<xsl:when test="$LinesHTMLBackColor != ''">
									<xsl:text>background: </xsl:text>
									<xsl:value-of select="$LinesHTMLBackColor"/>
								</xsl:when>
							</xsl:choose>
						</xsl:variable>
						<xsl:variable name="evenLinesStyle">
							<xsl:choose>
								<xsl:when test="$commonLinesStyle != ''">
									<xsl:value-of select="$commonLinesStyle"/>
								</xsl:when>
								<xsl:when test="$BackColorStyle = 'Header'">
									<xsl:if test="$LinesHTMLBackColor != ''">
										<xsl:text>background: </xsl:text>
										<xsl:value-of select="$LinesHTMLBackColor"/>
									</xsl:if>
								</xsl:when>
								<xsl:when test="$LinesHTMLBackColor2 != ''">
									<xsl:text>background: </xsl:text>
									<xsl:value-of select="$LinesHTMLBackColor2"/>
								</xsl:when>
							</xsl:choose>
						</xsl:variable>
						
						<tr>
							<xsl:if test="$evenLinesClass != ''">
								<xsl:attribute name="class">
									<xsl:value-of select="$evenLinesClass"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:if test="$evenLinesStyle != ''">
								<xsl:attribute name="style">
									<xsl:value-of select="$evenLinesStyle"/>
								</xsl:attribute>
							</xsl:if>
							
							<xsl:for-each select="Column">
								<xsl:if test="@ColVisible = 'True'">
									<xsl:variable name="colStyle">
										<xsl:if test="@ColAutoResize = 'False'">
											<xsl:text>width: </xsl:text>
											<xsl:value-of select="@ColWidth"/>
										</xsl:if>
									</xsl:variable>
									
									<td>
										<xsl:if test="$colStyle != ''">
											<xsl:attribute name="style">
												<xsl:value-of select="$colStyle"/>
											</xsl:attribute>
										</xsl:if>
										<xsl:if test="@ColumnClass != ''">
											<xsl:attribute name="class">
												<xsl:value-of select="@ColumnClass"/>
											</xsl:attribute>
										</xsl:if>

										<xsl:variable name="style">
											<xsl:choose>
												<xsl:when test="@ColLinesFont != '' and @ColLinesFont != $LinesFont">
													<xsl:value-of select="gxca:GetHtmlFontStyle(@ColLinesFont)"/>
													<xsl:text>; </xsl:text>
												</xsl:when>
												<xsl:when test="$LinesFont != ''">
													<xsl:value-of select="gxca:GetHtmlFontStyle($LinesFont)"/>
													<xsl:text>; </xsl:text>
												</xsl:when>
											</xsl:choose>
											<xsl:choose>
												<xsl:when test="@ColLinesColor != '' and @ColLinesColor != $LinesFontColor">
													<xsl:text>color: </xsl:text>
													<xsl:value-of select="gxca:GetHtmlColor(@ColLinesColor)"/>
													<xsl:text>; </xsl:text>
												</xsl:when>
												<xsl:when test="$LinesFontColor != ''">
													<xsl:text>color: </xsl:text>
													<xsl:value-of select="gxca:GetHtmlColor($LinesFontColor)"/>
													<xsl:text>; </xsl:text>
												</xsl:when>
											</xsl:choose>
											<xsl:if test="@Alignment = 'Center'">
												<xsl:text>text-align: center;</xsl:text>
											</xsl:if>
											<xsl:if test="@Alignment = 'Right'">
												<xsl:text>text-align: right;</xsl:text>
											</xsl:if>
											<xsl:text>background: transparent</xsl:text>
										</xsl:variable>
								
										<xsl:choose>
											
											<xsl:when test="@ControlType = 'Combo Box'">
												<select>
													<xsl:if test="$evenLinesClass != ''">
														<xsl:attribute name="class">
															<xsl:value-of select="$evenLinesClass"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:attribute name="style">
														<xsl:value-of select="$style"/>
													</xsl:attribute>
													<xsl:attribute name="name">
														<xsl:value-of select="@ControlName"/>
													</xsl:attribute>
													<xsl:if test="@ControlValues != ''">
														<xsl:apply-templates mode="OptionsValues" select="gxca:GetHtmlControlValues(@ControlValues)"/>
													</xsl:if>
												</select>
											</xsl:when>
											
											<xsl:when test="@ControlType = 'Dynamic Combo Box'">
												<xsl:variable name="Size" select="@ColCount"/>
												<xsl:variable name="Font">
													<xsl:choose>
														<xsl:when test="@ColFont != ''">
															<xsl:value-of select="@ColFont"/>
														</xsl:when>
														<xsl:when test="$LinesFont != ''">
															<xsl:value-of select="$LinesFont"/>
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="gxca:GetPropValue('LinesFont')"/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:variable>
												<xsl:variable name="TextForWidth" select="gxca:GetTextForDynamicControlsWidth($Size, $Font)"/>

												<select>
													<xsl:if test="$evenLinesClass != ''">
														<xsl:attribute name="class">
															<xsl:value-of select="$evenLinesClass"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:attribute name="style">
														<xsl:value-of select="$style"/>
													</xsl:attribute>
													<xsl:attribute name="name">
														<xsl:value-of select="@ControlName"/>
													</xsl:attribute>
													<option>
														<xsl:value-of select="gxca:GetDisplayName(@ColAttId, @FieldSpecifier)"/>
													</option>
													<xsl:if test="$TextForWidth != ''">
														<option>
															<xsl:value-of select="$TextForWidth"/>
														</option>
													</xsl:if>
												</select>
											</xsl:when>
											
											<xsl:when test="@ControlType = 'Edit'">
												<xsl:variable name="Size">
													<xsl:choose>
														<xsl:when test="@ColAutoResize = 'True'">
															<xsl:value-of select="@ColCount"/>
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="gxca:GetPropMeasureValue(@ColWidth, 0)"/>
														</xsl:otherwise>															
													</xsl:choose>
												</xsl:variable>
												<xsl:variable name="LinesNo">
													<xsl:choose>
														<xsl:when test="@ColAutoResize = 'True'">
															<xsl:value-of select="@RowCount"/>
														</xsl:when>
														<xsl:otherwise>
															<xsl:value-of select="gxca:GetPropMeasureValue(@Height, 0)"/>
														</xsl:otherwise>
													</xsl:choose>
												</xsl:variable>
												<xsl:choose>
													<xsl:when test="$LinesNo = '1' or $LinesNo = ''">
														<input type="text">
															<xsl:if test="$evenLinesClass != ''">
																<xsl:attribute name="class">
																	<xsl:value-of select="$evenLinesClass"/>
																</xsl:attribute>
															</xsl:if>
															<xsl:attribute name="style">
																<xsl:value-of select="$style"/>
															</xsl:attribute>
															<xsl:attribute name="value">
																<xsl:value-of select="gxca:GetDisplayName(@ColAttId, @FieldSpecifier)"/>
															</xsl:attribute>
															<xsl:choose>
																<xsl:when test="@ColAutoResize = 'True'">
																	<xsl:attribute name="size">
																		<xsl:value-of select="$Size"/>
																	</xsl:attribute>
																</xsl:when>
																<xsl:otherwise>
																	<xsl:variable name="Font">
																		<xsl:choose>
																			<xsl:when test="@ColFont != ''">
																				<xsl:value-of select="@ColFont"/>
																			</xsl:when>
																			<xsl:when test="$LinesFont != ''">
																				<xsl:value-of select="$LinesFont"/>
																			</xsl:when>
																			<xsl:otherwise>
																				<xsl:value-of select="gxca:GetPropValue('LinesFont')"/>
																			</xsl:otherwise>
																		</xsl:choose>
																	</xsl:variable>
																	<xsl:variable name="ColSize">
																		<xsl:value-of select="gxca:GetColAttSize(@ColWidth, $Font)"/>
																	</xsl:variable>
																	<xsl:if test="$ColSize != ''">
																		<xsl:attribute name="size">
																			<xsl:value-of select="$ColSize"/>
																		</xsl:attribute>
																	</xsl:if>
																</xsl:otherwise>
															</xsl:choose>
														</input>
													</xsl:when>													
													<xsl:otherwise>
														<textarea>
															<xsl:if test="$evenLinesClass != ''">
																<xsl:attribute name="class">
																	<xsl:value-of select="$evenLinesClass"/>
																</xsl:attribute>
															</xsl:if>
															<xsl:attribute name="style">
																<xsl:value-of select="$style"/>
															</xsl:attribute>
															<xsl:attribute name="rows">
																<xsl:value-of select="$LinesNo"/>
															</xsl:attribute>
															<xsl:attribute name="cols">
																<xsl:value-of select="$Size"/>
															</xsl:attribute>
															<xsl:value-of select="gxca:GetDisplayName(@ColAttId, @FieldSpecifier)"/>
														</textarea>                            
													</xsl:otherwise>													
												</xsl:choose>
											</xsl:when>
											
											<xsl:when test="@ControlType = 'Check Box'">
												<input type="checkbox">
													<xsl:if test="$evenLinesClass != ''">
														<xsl:attribute name="class">
															<xsl:value-of select="$evenLinesClass"/>
														</xsl:attribute>
													</xsl:if>
													<xsl:attribute name="style">
														<xsl:value-of select="$style"/>
													</xsl:attribute>
													<xsl:value-of select="@ControlTitle"/>
												</input>
											</xsl:when>
											
											<xsl:otherwise>
												<xsl:value-of disable-output-escaping="yes" select="gxca:GetHtmlUserControl(@ControlType, @ncol)"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
								</xsl:if>
							</xsl:for-each>
						</tr>
						
						<xsl:variable name="Rows" select="gxca:GetPropValue('MaxRows')"/>
						<xsl:call-template name="AddRows">
							<xsl:with-param name="Rows" select="$Rows"/>
							<xsl:with-param name="CurrentRow">
								<xsl:number value="0"/>
							</xsl:with-param>
							<xsl:with-param name="oddLinesClass" select="$oddLinesClass"/>
							<xsl:with-param name="evenLinesClass" select="$evenLinesClass"/>
							<xsl:with-param name="oddLinesStyle" select="$oddLinesStyle"/>
							<xsl:with-param name="evenLinesStyle" select="$evenLinesStyle"/>
						</xsl:call-template>
					</tbody>
				</table>
			</xsl:when>
			<xsl:otherwise>
				<span>Empty Grid</span>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="AddRows">
		<xsl:param name="Rows"/>
		<xsl:param name="CurrentRow"/>
		<xsl:param name="oddLinesClass"/>
		<xsl:param name="evenLinesClass"/>
		<xsl:param name="oddLinesStyle"/>
		<xsl:param name="evenLinesStyle"/>
		<xsl:if test="$Rows != $CurrentRow">
			
			<tr>
				<xsl:choose>
					<xsl:when test="gxca:IsEven($CurrentRow) = True">
						<xsl:if test="$evenLinesClass != ''">
							<xsl:attribute name="class">
								<xsl:value-of select="$evenLinesClass"/>
							</xsl:attribute>
						</xsl:if>
						<xsl:if test="$evenLinesStyle != ''">
							<xsl:attribute name="style">
								<xsl:value-of select="$evenLinesStyle"/>
							</xsl:attribute>
						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="$oddLinesClass != ''">
							<xsl:attribute name="class">
								<xsl:value-of select="$oddLinesClass"/>
							</xsl:attribute>
						</xsl:if>
						<xsl:if test="$oddLinesStyle != ''">
							<xsl:attribute name="style">
								<xsl:value-of select="$oddLinesStyle"/>
							</xsl:attribute>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
				
				<xsl:for-each select="Column">
					<xsl:if test="@ColVisible = 'True'">
						<xsl:variable name="colStyle">
							<xsl:if test="@ColAutoResize = 'False'">
								<xsl:text>width: </xsl:text>
								<xsl:value-of select="@ColWidth"/>
							</xsl:if>
						</xsl:variable>
						<td>
							<xsl:if test="$colStyle != ''">
								<xsl:attribute name="style">
									<xsl:value-of select="$colStyle"/>
								</xsl:attribute>
							</xsl:if>
							<xsl:if test="@ColumnClass != ''">
								<xsl:attribute name="class">
									<xsl:value-of select="@ColumnClass"/>
								</xsl:attribute>
							</xsl:if>
						</td>
					</xsl:if>
				</xsl:for-each>
			</tr>
			
			<xsl:variable name="NextRow" select="ceiling(concat($CurrentRow,'.9'))"/>
			<xsl:call-template name="AddRows">
				<xsl:with-param name="Rows" select="$Rows"/>
				<xsl:with-param name="CurrentRow" select="$NextRow"/>
				<xsl:with-param name="oddLinesClass" select="$oddLinesClass"/>
				<xsl:with-param name="evenLinesClass" select="$evenLinesClass"/>
				<xsl:with-param name="oddLinesStyle" select="$oddLinesStyle"/>
				<xsl:with-param name="evenLinesStyle" select="$evenLinesStyle"/>
			</xsl:call-template>
		</xsl:if>
	</xsl:template>


	<!-- TextBlock -->
	<xsl:template name="RenderTextBlock">
    <xsl:variable name="caption">
      <xsl:value-of select="gxca:GetPropValue('Caption')"/>
    </xsl:variable>
    <span>
      <xsl:call-template name="AddClassAttribute"/>
      <xsl:call-template name="AddTitleAttribute"/>
      <xsl:call-template name="AddStyleAttribute"/>
			<xsl:value-of select="gxca:GetTranslationFor($caption)"/>
		</span>
	</xsl:template>

	<!-- Helpers Templates -->
	<xsl:template name="AddStyleAttribute" >
		<xsl:variable name="Style">
			<xsl:variable name="Width">
				<xsl:value-of select="gxca:GetPropMeasure('GxWidth',1)"/>
			</xsl:variable>
			<xsl:if test="$Width != ''">
				<xsl:text>width: </xsl:text>
				<xsl:value-of select="$Width"/>
				<xsl:text>; </xsl:text>
			</xsl:if>
			<xsl:variable name="Height">
				<xsl:value-of select="gxca:GetPropMeasure('GxHeight',1)"/>
			</xsl:variable>
			<xsl:if test="$Height != ''">
				<xsl:text>height: </xsl:text>
				<xsl:value-of select="$Height"/>
				<xsl:text>; </xsl:text>
			</xsl:if>
			<xsl:if test="gxca:IsPropDefault('Font') = False">
				<xsl:variable name="Font" select="gxca:GetPropValue('Font')"/>
				<xsl:value-of select="gxca:GetHtmlFontStyle($Font)"/>
				<xsl:text>; </xsl:text>
			</xsl:if>
			<xsl:if test="gxca:IsPropDefault('ForeColor') = False">
				<xsl:variable name="ForeColor" select="gxca:GetPropValue('ForeColor')"/>
				<xsl:text>color: </xsl:text>
				<xsl:value-of select="gxca:GetHtmlColor($ForeColor)"/>
				<xsl:text>; </xsl:text>
			</xsl:if>
			<xsl:if test="gxca:IsPropDefault('BackColor') = False">
				<xsl:text>background-color: </xsl:text>
				<xsl:choose>
					<xsl:when test="gxca:GetPropValue('Fill') != 'False'">
						<xsl:variable name="BackColor" select="gxca:GetPropValue('BackColor')"/>
						<xsl:value-of select="gxca:GetHtmlColor($BackColor)"/>
					</xsl:when>
					<xsl:otherwise>transparent</xsl:otherwise>
				</xsl:choose>
				<xsl:text>; </xsl:text>
			</xsl:if>
			<xsl:if test="gxca:IsPropDefault('Border') = False">
				<xsl:text>border-width: </xsl:text>
				<xsl:value-of select="gxca:GetPropValue('Border')"/>
				<xsl:text>; </xsl:text>
			</xsl:if>
			<xsl:if test="gxca:IsPropDefault('BorderColor') = False">
				<xsl:variable name="BorderColor" select="gxca:GetPropValue('BorderColor')"/>
				<xsl:text>border-color: </xsl:text>
				<xsl:value-of select="gxca:GetHtmlColor($BorderColor)"/>
			</xsl:if>
			<xsl:if test="gxca:GetPropValue('Alignment') = 'Center'">
				<xsl:text>text-align: center;</xsl:text>
			</xsl:if>
			<xsl:if test="gxca:GetPropValue('Alignment') = 'Right'">
				<xsl:text>text-align: right;</xsl:text>
			</xsl:if>
		</xsl:variable>
		<xsl:if test="$Style != ''">
			<xsl:attribute name="style">
				<xsl:value-of select="$Style"/>
			</xsl:attribute>
		</xsl:if>
	</xsl:template>

	<xsl:template name="AddTitleAttribute" >
		<xsl:if test="gxca:IsPropDefault('title') = False">
			<xsl:attribute name="Title">
				<xsl:value-of select="gxca:GetPropValue('title')"/>
			</xsl:attribute>
		</xsl:if>
	</xsl:template>

	<xsl:template name="AddClassAttribute" >
		<xsl:attribute name="class"><xsl:value-of select="gxca:GetPropValue('Class')"/></xsl:attribute>
	</xsl:template>

</xsl:stylesheet>
