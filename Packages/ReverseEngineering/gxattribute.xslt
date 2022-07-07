<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output method="html" encoding="UTF-8"/>
	<xsl:template match="/">
		<html>
			<xsl:call-template name="doHeader">
				<xsl:with-param name="dvgPath">
					<xsl:value-of select="AttCrossReference/DVGPath"/>
				</xsl:with-param>
			</xsl:call-template>
			<body  bgcolor="#FFFFFF" text="#000000" link="#000000" alink="#000000" vlink="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
				<xsl:call-template name="doScripts">
					<xsl:with-param name="dvgPath">
						<xsl:value-of select="AttCrossReference/DVGPath"/>
					</xsl:with-param>
				</xsl:call-template>
				<form action="" id="DVGFORM" method="post" name="DVGFORM">
					<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
						<tr>
							<td>
								<div>
									<xsl:call-template name="doAttributeTitle">
										<xsl:with-param name="dvgPath">
											<xsl:value-of select="AttCrossReference/DVGPath"/>
										</xsl:with-param>
									</xsl:call-template>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div>
									<xsl:call-template name="doAttributeObjects">
										<xsl:with-param name="dvgPath">
											<xsl:value-of select="AttCrossReference/DVGPath"/>
										</xsl:with-param>
										<xsl:with-param name="product">
											<xsl:value-of select="AttCrossReference/Product"/>
										</xsl:with-param>
									</xsl:call-template>
								</div>
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
			<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\gxobjects.js</xsl:attribute>
      <xsl:text> </xsl:text>
		</script>
	</xsl:template>
	
	<xsl:template name="doAttributeTitle">
	<xsl:param name="dvgPath"/>
		<div id="objectInfo">
			<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
				<tr>
					<td class="titulosRosaBgc">
						<b>Attribute <xsl:value-of select="AttCrossReference/AttInfo/Name"/> Cross Reference</b>
					</td>
				</tr>
				<xsl:call-template name="doAttributeInfo">
					<xsl:with-param name="dvgPath">
						<xsl:value-of select="$dvgPath"/>
					</xsl:with-param>
					<xsl:with-param name="product">
						<xsl:value-of select="AttCrossReference/Product"/>
					</xsl:with-param>
				</xsl:call-template>
			</table>
		</div>
	</xsl:template>
	
	<xsl:template name="doAttributeInfo">
	<xsl:param name="dvgPath"/>
	<xsl:param name="product"/>
		<table class="gxcontent" cellspacing="0" cellpadding="0" width="100%" style="BORDER-BOTTOM: thin inset; BORDER-RIGHT: thin inset">
			<tr>
				<td><b>Name: </b><xsl:value-of select="AttCrossReference/AttInfo/Name"/></td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<xsl:call-template name="doAttributeType">
				<xsl:with-param name="attType">
					<xsl:value-of select="AttCrossReference/AttInfo/Type"/>
				</xsl:with-param>
				<xsl:with-param name="dvgPath">
					<xsl:value-of select="$dvgPath"/>
				</xsl:with-param>
				<xsl:with-param name="product">
					<xsl:value-of select="$product"/>
				</xsl:with-param>
			</xsl:call-template>
		</table>
	</xsl:template>
	
	<xsl:template name="doAttributeType">
	<xsl:param name="attType"/>
	<xsl:param name="dvgPath"/>
	<xsl:param name="product"/>
		<xsl:if test="$attType='Numeric'">
			<tr>
				<td>
					<b>Type: </b><xsl:value-of select="$attType"/> 
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<tr>
				<td>
					<b>Length: </b><xsl:value-of select="AttCrossReference/AttInfo/Length"/>.<xsl:value-of select="AttCrossReference/AttInfo/Decimals"/>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="$attType='Boolean'">
			<tr>
				<td>
					<b>Type: </b><xsl:value-of select="$attType"/> 
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<tr>
				<td>
					<b>Length: </b><xsl:value-of select="AttCrossReference/AttInfo/Length"/>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="$attType='Date'">
			<tr>
				<td>
					<b>Type: </b><xsl:value-of select="$attType"/> 
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<tr>
				<td>
					<b>Length: </b><xsl:value-of select="AttCrossReference/AttInfo/Length"/>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="$attType='TimeDate'">
			<tr>
				<td>
					<b>Type: </b>DateTime	
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<tr>
				<td>
					<b>Length: </b><xsl:value-of select="AttCrossReference/AttInfo/Length"/>.<xsl:value-of select="AttCrossReference/AttInfo/Decimals"/>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="$attType='Character'">
			<tr>
				<td>
					<b>Type: </b><xsl:value-of select="$attType"/> 
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<tr>
				<td>
					<b>Length: </b><xsl:value-of select="AttCrossReference/AttInfo/Length"/>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="$attType='GUID'">
			<tr>
				<td>
					<b>Type: </b>Unique Identifier
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<tr>
				<td>
					<b>Length: </b><xsl:value-of select="AttCrossReference/AttInfo/Length"/>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="$attType='BLOb'">
			<tr>
				<td>
					<b>Type: </b>Blob
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<tr>
				<td>
					<b>Length: </b><xsl:value-of select="AttCrossReference/AttInfo/Length"/>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="$attType='Bitmap'">
			<tr>
				<td>
					<b>Type: </b>Image
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<tr>
				<td>
					<b>Length: </b><xsl:value-of select="AttCrossReference/AttInfo/Length"/>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="$attType='VarChar'">
			<tr>
				<td>
					<b>Type: </b><xsl:if test="$product='1'">LongVarChar</xsl:if><xsl:if test="$product='2'">Text</xsl:if>
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<tr>
				<td>
					<b>Length: </b><xsl:value-of select="AttCrossReference/AttInfo/Length"/>
				</td>
			</tr>
		</xsl:if>
		<xsl:if test="$attType='ShortVarChar'">
			<tr>
				<td>
					<b>Type: </b>VarChar
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<tr>
				<td>
					<b>Length: </b><xsl:value-of select="AttCrossReference/AttInfo/Length"/>
				</td>
			</tr>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="doAttributeObjects">
	<xsl:param name="dvgPath"/>
	<xsl:param name="product"/>
		<xsl:if test="AttCrossReference/IsInTables='True'">
			<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
				<tr>
					<td class="titulosTabla">
						<xsl:attribute name="title"><xsl:if test="$product='1'">Tables</xsl:if><xsl:if test="$product='2'">External Tables</xsl:if></xsl:attribute>
						<b><xsl:if test="$product='1'">Tables</xsl:if><xsl:if test="$product='2'">External Tables</xsl:if><xsl:if test="$product='3'">Tables</xsl:if></b>
					</td>
				</tr>
			</table>
			<table class="gxcontent" cellspacing="0" cellpadding="0" width="100%" style="BORDER-BOTTOM: thin inset; BORDER-RIGHT: thin inset">
				<tr class="subTitulosTabla">
					<td nowrap="yes"><b></b></td>
					<td nowrap="yes"><b>Name</b></td>
				</tr>
				<xsl:for-each select="AttCrossReference/ObjectsWithAtt/Tables/Table">
					<tr>
						<td valign="top"></td>
						<td valign="top">
							<span style="cursor: hand" class="gxobj">
								<xsl:attribute name="onclick">
									<xsl:text>OpenObjectReport('TBL','</xsl:text>
									<xsl:value-of select="Name"/>
									<xsl:text>');</xsl:text>
								</xsl:attribute>
								<u><xsl:value-of select="Name"/></u>
							</span>
						</td>
					</tr>
					<xsl:call-template name="doRowSeparator">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</xsl:for-each>
			</table>
		</xsl:if>
		<xsl:if test="AttCrossReference/IsInTransactions='True'">
			<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
				<tr>
					<td class="titulosTabla">
						<xsl:attribute name="title"><xsl:if test="$product='1'">Transactions</xsl:if><xsl:if test="$product='2'">Business Components</xsl:if></xsl:attribute>
						<b><xsl:if test="$product='1'">Transactions</xsl:if><xsl:if test="$product='2'">Business Components</xsl:if></b>
					</td>
				</tr>
			</table>
			<table class="gxcontent" cellspacing="0" cellpadding="0" width="100%" style="BORDER-BOTTOM: thin inset; BORDER-RIGHT: thin inset">
				<tr class="subTitulosTabla">
					<td nowrap="yes"><b></b></td>
					<td nowrap="yes"><b>Name</b></td>
				</tr>
				<xsl:for-each select="AttCrossReference/ObjectsWithAtt/Transactions/Transaction">
					<tr>
						<td valign="top"></td>
						<td valign="top">
							<span style="cursor: hand" class="gxobj">
								<xsl:attribute name="onclick">
									<xsl:text>OpenObjectReport('TRN','</xsl:text>
									<xsl:value-of select="Name"/>
									<xsl:text>');</xsl:text>
								</xsl:attribute>
								<u><xsl:value-of select="Name"/></u>
							</span>
						</td>
					</tr>
					<xsl:call-template name="doRowSeparator">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</xsl:for-each>
			</table>
		</xsl:if>
		<xsl:if test="AttCrossReference/IsInDataviews='True'">
			<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
				<tr>
					<td class="titulosTabla">
						<xsl:attribute name="title">Dataviews</xsl:attribute>
						<b>Dataviews</b>
					</td>
				</tr>
			</table>
			<table class="gxcontent" cellspacing="0" cellpadding="0" width="100%" style="BORDER-BOTTOM: thin inset; BORDER-RIGHT: thin inset">
				<tr class="subTitulosTabla">
					<td nowrap="yes"><b></b></td>
					<td nowrap="yes"><b>Name</b></td>
				</tr>
				<xsl:for-each select="AttCrossReference/ObjectsWithAtt/Dataviews/Dataview">
					<tr>
						<td valign="top"></td>
						<td valign="top">
							<span style="cursor: hand" class="gxobj">
								<xsl:attribute name="onclick">
									<xsl:text>OpenObjectReport('DV','</xsl:text>
									<xsl:value-of select="Name"/>
									<xsl:text>');</xsl:text>
								</xsl:attribute>
								<u><xsl:value-of select="Name"/></u>
							</span>
						</td>
					</tr>
					<xsl:call-template name="doRowSeparator">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</xsl:for-each>
			</table>
		</xsl:if>
		<xsl:if test="AttCrossReference/IsInGroups='True'">
			<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
				<tr>
					<td class="titulosTabla">
						<xsl:attribute name="title">Subtype Groups</xsl:attribute>
						<b>Subtype Groups</b>
					</td>
				</tr>
			</table>
			<table class="gxcontent" cellspacing="0" cellpadding="0" width="100%" style="BORDER-BOTTOM: thin inset; BORDER-RIGHT: thin inset">
				<tr class="subTitulosTabla">
					<td nowrap="yes"><b></b></td>
					<td nowrap="yes"><b>Name</b></td>
				</tr>
				<xsl:for-each select="AttCrossReference/ObjectsWithAtt/Groups/Group">
					<tr>
						<td valign="top"></td>
						<td valign="top">
							<span style="cursor: hand" class="gxobj">
								<xsl:attribute name="onclick">
									<xsl:text>OpenObjectReport('SG','</xsl:text>
									<xsl:value-of select="Name"/>
									<xsl:text>');</xsl:text>
								</xsl:attribute>
								<u><xsl:value-of select="Name"/></u>
							</span>
						</td>
					</tr>
					<xsl:call-template name="doRowSeparator">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</xsl:for-each>
			</table>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="doObjectType">
	<xsl:param name="objPreffix"/>
	<xsl:param name="product"/>
		<xsl:if test="$product='1'">
			<xsl:if test="$objPreffix='TRN'"> Transactions </xsl:if>
			<xsl:if test="$objPreffix='DV'"> Dataviews </xsl:if>
			<xsl:if test="$objPreffix='TBL'"> Tables </xsl:if>
		</xsl:if>
		<xsl:if test="$product='2'">
			<xsl:if test="$objPreffix='TRN'"> Business Components </xsl:if>
			<xsl:if test="$objPreffix='DV'"> External Tables </xsl:if>
			<xsl:if test="$objPreffix='TBL'"> External Tables </xsl:if>
		</xsl:if>
		<xsl:if test="$product='3'">
			<xsl:if test="$objPreffix='DV'"> Tables </xsl:if>
			<xsl:if test="$objPreffix='TBL'"> Tables </xsl:if>
		</xsl:if>
		<xsl:if test="$objPreffix='SG'">Subtype Groups </xsl:if>
	</xsl:template>
	
	<xsl:template name="doRowSeparator">
	<xsl:param name="dvgPath"/>
		<tr>
			<td colspan="6" bgcolor="#F0F0F0" height="1">
				<img width="1" height="1">
					<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\pixel.gif</xsl:attribute>
				</img>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>