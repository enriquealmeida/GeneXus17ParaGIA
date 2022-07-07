<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	<xsl:output method="html" encoding="UTF-8"/>
	<xsl:template match="/">
		<html>
			<xsl:call-template name="doHeader">
				<xsl:with-param name="dvgPath">
					<xsl:value-of select="GXObject/DVGPath"/>
				</xsl:with-param>
			</xsl:call-template>
			<body  bgcolor="#FFFFFF" text="#000000" link="#000000" alink="#000000" vlink="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
				<xsl:call-template name="doScripts">
					<xsl:with-param name="dvgPath">
						<xsl:value-of select="GXObject/DVGPath"/>
					</xsl:with-param>
				</xsl:call-template>
				<form action="" id="DVGFORM" method="post" name="DVGFORM">
					<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
						<tr>
							<td>
								<div>
									<xsl:call-template name="doObjectTitle">
										<xsl:with-param name="objPreffix">
											<xsl:value-of select="GXObject/ObjPreffix"/>
										</xsl:with-param>
										<xsl:with-param name="dvgPath">
											<xsl:value-of select="GXObject/DVGPath"/>
										</xsl:with-param>
									</xsl:call-template>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div>
									<xsl:call-template name="doWarnings">
										<xsl:with-param name="hasWarnings">
											<xsl:value-of select="GXObject/HasWarnings"/>
										</xsl:with-param>
										<xsl:with-param name="dvgPath">
											<xsl:value-of select="GXObject/DVGPath"/>
										</xsl:with-param>
									</xsl:call-template>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div>
									<xsl:call-template name="doAttributes">
										<xsl:with-param name="objPreffix">
											<xsl:value-of select="GXObject/ObjPreffix"/>
										</xsl:with-param>
										<xsl:with-param name="dvgPath">
											<xsl:value-of select="GXObject/DVGPath"/>
										</xsl:with-param>
										<xsl:with-param name="product">
											<xsl:value-of select="GXObject/Product"/>
										</xsl:with-param>
									</xsl:call-template>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div>
									<xsl:call-template name="doIndexes">
										<xsl:with-param name="objPreffix">
											<xsl:value-of select="GXObject/ObjPreffix"/>
										</xsl:with-param>
										<xsl:with-param name="dvgPath">
											<xsl:value-of select="GXObject/DVGPath"/>
										</xsl:with-param>
										<xsl:with-param name="hasIndexes">
											<xsl:value-of select="GXObject/HasIndexes"/>
										</xsl:with-param>
									</xsl:call-template>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div>
									<xsl:call-template name="doSubordinations">
										<xsl:with-param name="objPreffix">
											<xsl:value-of select="GXObject/ObjPreffix"/>
										</xsl:with-param>
										<xsl:with-param name="dvgPath">
											<xsl:value-of select="GXObject/DVGPath"/>
										</xsl:with-param>
										<xsl:with-param name="hasSuperords">
											<xsl:value-of select="GXObject/HasSuperords"/>
										</xsl:with-param>
										<xsl:with-param name="hasSubords">
											<xsl:value-of select="GXObject/HasSubords"/>
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
	
	<xsl:template name="doObjectTitle">
	<xsl:param name="dvgPath"/>
		<div id="objectInfo">
			<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
				<tr>
					<td class="titulosRosaBgc">
						<b>
							<xsl:call-template name="doObjectType">
								<xsl:with-param name="objPreffix">
									<xsl:value-of select="GXObject/ObjPreffix"/>
								</xsl:with-param>
								<xsl:with-param name="product">
									<xsl:value-of select="GXObject/Product"/>
								</xsl:with-param>
							</xsl:call-template>
							<xsl:value-of select="GXObject/Name"/> Report
						</b>
					</td>
				</tr>
			</table>
			<xsl:call-template name="doObjectInfo">
				<xsl:with-param name="objPreffix">
					<xsl:value-of select="GXObject/ObjPreffix"/>
				</xsl:with-param>
				<xsl:with-param name="dvgPath">
					<xsl:value-of select="$dvgPath"/>
				</xsl:with-param>
				<xsl:with-param name="product">
					<xsl:value-of select="GXObject/Product"/>
				</xsl:with-param>
			</xsl:call-template>
		</div>
	</xsl:template>
	
	<xsl:template name="doObjectType">
	<xsl:param name="objPreffix"/>
	<xsl:param name="product"/>
		<xsl:if test="$product='1'">
			<xsl:if test="$objPreffix='TRN'"> Transaction </xsl:if>
			<xsl:if test="$objPreffix='DV'"> Dataview </xsl:if>
			<xsl:if test="$objPreffix='TBL'"> Table </xsl:if>
		</xsl:if>
		<xsl:if test="$product='2'">
			<xsl:if test="$objPreffix='TRN'"> Business Component </xsl:if>
			<xsl:if test="$objPreffix='DV'"> External View </xsl:if>
			<xsl:if test="$objPreffix='TBL'"> External Table </xsl:if>
		</xsl:if>
		<xsl:if test="$product='3'">
			<xsl:if test="$objPreffix='DV'"> Table </xsl:if>
			<xsl:if test="$objPreffix='TBL'"> Table </xsl:if>
		</xsl:if>
		<xsl:if test="$objPreffix='SG'">Subtype Group </xsl:if>
	</xsl:template>
	
	<xsl:template name="doObjectInfo">
	<xsl:param name="objPreffix"/>
	<xsl:param name="dvgPath"/>
	<xsl:param name="product"/>
		<table class="gxcontent" cellspacing="0" cellpadding="0" width="100%" style="BORDER-BOTTOM: thin inset; BORDER-RIGHT: thin inset">
			<tr>
				<td>
					<b>Type: </b>
					<xsl:if test="$objPreffix='TRN'">
						<img width="13" height="13" border="0">
							<xsl:if test="$product='1'">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\transaction.ico</xsl:attribute>
							</xsl:if>
							<xsl:if test="$product='2'">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\BusinessComponent.ico</xsl:attribute>
							</xsl:if>
						</img>
					</xsl:if>
					<xsl:if test="$objPreffix='DV'">
						<img width="13" height="13" border="0">
							<xsl:if test="$product='1'">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\dataview.ico</xsl:attribute>
							</xsl:if>
							<xsl:if test="$product='2'">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\dkview.ico</xsl:attribute>
							</xsl:if>
							<xsl:if test="$product='3'">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\table.ico</xsl:attribute>
							</xsl:if>
						</img>
					</xsl:if>
					<xsl:if test="$objPreffix='SG'">
						<img width="13" height="13" border="0">
							<xsl:if test="$product='1'">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\group.ico</xsl:attribute>
							</xsl:if>
							<xsl:if test="$product='2'">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\dkgroup.ico</xsl:attribute>
							</xsl:if>
						</img>
					</xsl:if>
					<xsl:if test="$objPreffix='TBL'">
						<img width="13" height="13" border="0">
							<xsl:if test="$product='1'">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\table.ico</xsl:attribute>
							</xsl:if>
							<xsl:if test="$product='2'">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\tblext.gif</xsl:attribute>
							</xsl:if>
							<xsl:if test="$product='3'">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\table.ico</xsl:attribute>
							</xsl:if>
						</img>
					</xsl:if>
					<xsl:call-template name="doObjectType">
						<xsl:with-param name="objPreffix">
							<xsl:value-of select="GXObject/ObjPreffix"/>
						</xsl:with-param>
						<xsl:with-param name="product">
							<xsl:value-of select="GXObject/Product"/>
						</xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
			<tr>
				<td>
					<b>Name: </b>
					<xsl:value-of select="GXObject/Name"/>
				</td>
			</tr>
		</table>
	</xsl:template>

	<xsl:template name="doWarnings">
	<xsl:param name="hasWarnings"/>
	<xsl:param name="dvgPath"/>
		<xsl:if test="$hasWarnings='true'">
			<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
				<tr>
					<td class="titulosTabla">
						<xsl:attribute name="title">Warnings</xsl:attribute>
						<b>Warnings</b>
					</td>
				</tr>
			</table>
			<table class="gxcontent" cellspacing="0" cellpadding="0" width="100%">
				<tr class="subTitulosTabla">
					<td nowrap="yes"><b></b></td>
					<td nowrap="yes"><b>Warning</b></td>
					<td nowrap="yes"><b>Options</b></td>
				</tr>
				<xsl:for-each select="GXObject/Warnings/Warning">
					<tr>
						<td></td>
						<td>
							<img width="13" height="13" border="0">
								<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\warningIcon.gif</xsl:attribute>
							</img>
							<xsl:value-of select="Message"/>
						</td>
						<xsl:call-template name="doWarningFixes"/>
					</tr>
					<xsl:call-template name="doRowSeparator">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</xsl:for-each>
			</table>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="doWarningFixes">
		<td>
			<table cellspacing="0" cellpadding="0" border="0">
				<tr>
					<xsl:for-each select="Fixes/WarningFix">
						<td>
							<span style="cursor: hand" class="gxobj">
								<xsl:attribute name="onclick">
									<xsl:text>ApplyWarningFix(</xsl:text>
									<xsl:value-of select="Id"/>
									<xsl:text>);</xsl:text>
								</xsl:attribute>
								<u><xsl:value-of select="FixName"/></u>
							</span>
						</td>
					</xsl:for-each>
				</tr>
			</table>
		</td>
	</xsl:template>
	
	<xsl:template name="doAttributes">
	<xsl:param name="objPreffix"/>
	<xsl:param name="dvgPath"/>
	<xsl:param name="product"/>
		<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
			<tr>
				<td class="titulosTabla">
					<xsl:attribute name="title">Attributes</xsl:attribute>
					<b>Attributes</b>
				</td>
			</tr>
		</table>
		<table class="gxcontent" cellspacing="0" cellpadding="0" width="100%" style="BORDER-BOTTOM: thin inset; BORDER-RIGHT: thin inset">
			<xsl:if test="$objPreffix='TRN'">
				<xsl:call-template name="doTrnTblAttributes">
					<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					<xsl:with-param name="product"><xsl:value-of select="$product"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="doTrnSublevels">
					<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					<xsl:with-param name="product"><xsl:value-of select="$product"/></xsl:with-param>
					<xsl:with-param name="hasSublevels"><xsl:value-of select="GXObject/HasSubLevels"/></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$objPreffix='TBL'">
				<xsl:call-template name="doTrnTblAttributes">
					<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					<xsl:with-param name="product"><xsl:value-of select="$product"/></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$objPreffix='DV'">
				<xsl:call-template name="doDvAttributes">
					<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					<xsl:with-param name="product"><xsl:value-of select="$product"/></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
			<xsl:if test="$objPreffix='SG'">
				<xsl:call-template name="doSgAttributes">
					<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
				</xsl:call-template>
			</xsl:if>
		</table>
	</xsl:template>
	
	<xsl:template name="doTrnTblAttributes">
	<xsl:param name="dvgPath"/>
	<xsl:param name="product"/>
		<tr class="subTitulosTabla">
			<td nowrap="yes"><b></b></td>
			<td nowrap="yes"><b>Name</b></td>
			<td nowrap="yes"><b>External Name</b></td>
			<td nowrap="yes"><b>Description</b></td>
			<td nowrap="yes"><b>Type</b></td>
			<td nowrap="yes"><b>Length</b></td>
			<!--<td nowrap="yes"><b>Decimals</b></td>
			<td nowrap="yes"><b>Is Primary Key</b></td>
			<td nowrap="yes"><b>Is Foreign Key</b></td>-->
		</tr>
		<xsl:for-each select="GXObject/Attributes/GXAttribute">
			<tr class="textoGrisTabla">
				<td valign="top" align="right">
					<xsl:if test="IsPrimaryKey='true'">
						<img width="16" height="16" border="0">
							<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\key.ico</xsl:attribute>
						</img>
					</xsl:if>
				</td>
				<xsl:call-template name="doTrnTblAttInfo">
					<xsl:with-param name="attType"><xsl:value-of select="DataType/Type"/></xsl:with-param>
					<xsl:with-param name="product"><xsl:value-of select="$product"/></xsl:with-param>
				</xsl:call-template>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="doTrnSublevels">
	<xsl:param name="dvgPath"/>
	<xsl:param name="product"/>
	<xsl:param name="hasSublevels"/>

    <xsl:for-each select="GXObject/Levels/GXTransactionLevel">

      <xsl:call-template name="doTrnSublevel">
        <xsl:with-param name="dvgPath">
          <xsl:value-of select="$dvgPath"/>
        </xsl:with-param>
        <xsl:with-param name="product">
          <xsl:value-of select="$product"/>
        </xsl:with-param>
        <xsl:with-param name="hasSublevels">
          <xsl:value-of select="$hasSublevels"/>
        </xsl:with-param>
      </xsl:call-template>

      
    </xsl:for-each>



  </xsl:template>
  
  
  <xsl:template name="doTrnSublevel">
    <xsl:param name="dvgPath"/>
    <xsl:param name="product"/>
    <xsl:param name="hasSublevels"/>

    <xsl:if test="$hasSublevels='true'">
      <tr>
        <td nowrap="yes">
          <b></b>
        </td>
        <td colspan="5" class="titulosTabla">
          <b> Level: </b>
          <xsl:value-of select="BaseTable/Name"/>
        </td>
      </tr>
      <tr class="subTitulosTabla">
        <td nowrap="yes">
          <b></b>
        </td>
        <td nowrap="yes">
          <b>Name</b>
        </td>
        <td nowrap="yes">
          <b>External Name</b>
        </td>
        <td nowrap="yes">
          <b>Description</b>
        </td>
        <td nowrap="yes">
          <b>Type</b>
        </td>
        <td nowrap="yes">
          <b>Length</b>
        </td>
        <!--<td nowrap="yes"><b>Decimals</b></td>
			<td nowrap="yes"><b>Is Primary Key</b></td>
			<td nowrap="yes"><b>Is Foreign Key</b></td>-->
      </tr>
    </xsl:if>
    <xsl:for-each select="Attributes/GXAttribute">
      <tr class="textoGrisTabla">
        <td valign="top" align="right">
          <xsl:if test="IsPrimaryKey='true'">
            <img width="16" height="16" border="0">
              <xsl:attribute name="src">
                <xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\key.ico
              </xsl:attribute>
            </img>
          </xsl:if>
        </td>
        <xsl:call-template name="doTrnTblAttInfo">
          <xsl:with-param name="attType">
            <xsl:value-of select="DataType/Type"/>
          </xsl:with-param>
          <xsl:with-param name="product">
            <xsl:value-of select="$product"/>
          </xsl:with-param>
        </xsl:call-template>
      </tr>
      <xsl:call-template name="doRowSeparator">
        <xsl:with-param name="dvgPath">
          <xsl:value-of select="$dvgPath"/>
        </xsl:with-param>
      </xsl:call-template>
     </xsl:for-each>

    <xsl:for-each select="Levels/GXTransactionLevel">

      <xsl:call-template name="doTrnSublevel">
        <xsl:with-param name="dvgPath">
          <xsl:value-of select="$dvgPath"/>
        </xsl:with-param>
        <xsl:with-param name="product">
          <xsl:value-of select="$product"/>
        </xsl:with-param>
        <xsl:with-param name="hasSublevels">
          <xsl:value-of select="$hasSublevels"/>
        </xsl:with-param>
      </xsl:call-template>


    </xsl:for-each>

  </xsl:template>




  <xsl:template name="doTrnTblAttInfo">
	<xsl:param name="attType"/>
	<xsl:param name="product"/>
		<td valign="top">
			<span style="cursor: hand" class="gxobj">
				<xsl:attribute name="onclick">
					<xsl:text>AttCrossReference('</xsl:text>
					<xsl:value-of select="Name"/>
					<xsl:text>');</xsl:text>
				</xsl:attribute>
				<u><xsl:value-of select="Name"/></u>
			</span>
		</td>
		<td valign="top"><xsl:value-of select="ExternalName"/></td>
		<td valign="top"><xsl:value-of select="Description"/></td>
		<xsl:if test="$attType='Numeric'">
			<td valign="top"><xsl:value-of select="$attType"/></td>
			<td valign="top"><xsl:value-of select="DataType/Length"/>.<xsl:value-of select="DataType/Decimals"/><xsl:if test="IsSigned='true'">-</xsl:if></td>
		</xsl:if>
		<xsl:if test="$attType='Boolean'">
			<td valign="top"><xsl:value-of select="$attType"/></td>
			<td valign="top"><xsl:value-of select="DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='Date'">
			<td valign="top">Date</td>
			<td valign="top"><xsl:value-of select="DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='TimeDate'">
			<td valign="top">DateTime</td>
			<td valign="top"><xsl:value-of select="DataType/Length"/>.<xsl:value-of select="DataType/Decimals"/></td>
		</xsl:if>
		<xsl:if test="$attType='Character'">
			<td valign="top"><xsl:value-of select="$attType"/></td>
			<td valign="top"><xsl:value-of select="DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='GUID'">
			<xsl:if test="$product='1'">
				<td valign="top">Unique Identifier</td>
			</xsl:if>
			<xsl:if test="$product='2'">
				<td valign="top">GUID</td>
			</xsl:if>
			<td valign="top"><xsl:value-of select="DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='BLOb'">
			<td valign="top">Blob</td>
			<td valign="top"><xsl:value-of select="DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='Bitmap'">
			<td valign="top">Image</td>
			<td valign="top"><xsl:value-of select="DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='VarChar'">
			<xsl:if test="$product='1'">
				<td valign="top">LongVarChar</td>
			</xsl:if>
			<xsl:if test="$product='2'">
				<td valign="top">Text</td>
			</xsl:if>
			<td valign="top"><xsl:value-of select="DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='ShortVarChar'">
			<td valign="top">VarChar</td>
			<td valign="top"><xsl:value-of select="DataType/Length"/></td>
		</xsl:if>
		<!--<td valign="top" class="textoGrisTabla"><xsl:value-of select="DataType/Decimals"/></td>
		<td valign="top" class="textoGrisTabla"><xsl:value-of select="IsPrimaryKey"/></td>
		<td valign="top" class="textoGrisTabla"><xsl:value-of select="IsForeignKey"/></td>-->
	</xsl:template>

	
	<xsl:template name="doDvAttributes">
	<xsl:param name="dvgPath"/>
	<xsl:param name="product"/>
		<tr class="subTitulosTabla">
			<td nowrap="yes"><b></b></td>
			<td nowrap="yes"><b>Name</b></td>
			<td nowrap="yes"><b>External Name</b></td>
			<td nowrap="yes"><b>Description</b></td>
			<td nowrap="yes"><b>Type</b></td>
			<td nowrap="yes"><b>Length</b></td>
		</tr>
		<xsl:for-each select="GXObject/Attributes/GXDVAttribute">
			<tr class="textoGrisTabla">
				<td valign="top"></td>
				<td valign="top">
					<span style="cursor: hand" class="gxobj">
						<xsl:attribute name="onclick">
							<xsl:text>AttCrossReference('</xsl:text>
							<xsl:value-of select="Name"/>
							<xsl:text>');</xsl:text>
						</xsl:attribute>
						<u><xsl:value-of select="Name"/></u>
					</span>
				</td>
				<td valign="top"><xsl:value-of select="ExtName"/></td>
				<xsl:call-template name="doDVAttributeInfo">
					<xsl:with-param name="attType"><xsl:value-of select="Attribute/DataType/Type"/></xsl:with-param>
					<xsl:with-param name="product"><xsl:value-of select="$product"/></xsl:with-param>
				</xsl:call-template>
				<xsl:call-template name="doRowSeparator">
					<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
				</xsl:call-template>
			</tr>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="doDVAttributeInfo">
	<xsl:param name="attType"/>
	<xsl:param name="product"/>
		<td valign="top"><xsl:value-of select="Attribute/Description"/></td>
		<xsl:if test="$attType='Numeric'">
			<td valign="top"><xsl:value-of select="$attType"/></td>
			<td valign="top"><xsl:value-of select="Attribute/DataType/Length"/>.<xsl:value-of select="Attribute/DataType/Decimals"/><xsl:if test="Attribute/IsSigned='true'">-</xsl:if></td>
		</xsl:if>
		<xsl:if test="$attType='Boolean'">
			<td valign="top"><xsl:value-of select="$attType"/></td>
			<td valign="top"><xsl:value-of select="Attribute/DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='Date'">
			<td valign="top"><xsl:value-of select="$attType"/></td>
			<td valign="top"><xsl:value-of select="Attribute/DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='TimeDate'">
			<td valign="top">DateTime</td>
			<td valign="top"><xsl:value-of select="Attribute/DataType/Length"/>.<xsl:value-of select="Attribute/DataType/Decimals"/></td>
		</xsl:if>
		<xsl:if test="$attType='Character'">
			<td valign="top"><xsl:value-of select="$attType"/></td>
			<td valign="top"><xsl:value-of select="Attribute/DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='GUID'">
			<xsl:if test="$product='1'">
				<td valign="top">Unique Identifier</td>
			</xsl:if>
			<xsl:if test="$product='2'">
				<td valign="top">GUID</td>
			</xsl:if>
			<td valign="top"><xsl:value-of select="Attribute/DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='BLOb'">
			<td valign="top">Blob</td>
			<td valign="top"><xsl:value-of select="Attribute/DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='Bitmap'">
			<td valign="top">Image</td>
			<td valign="top"><xsl:value-of select="Attribute/DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='VarChar'">
			<xsl:if test="$product='1'">
				<td valign="top">LongVarChar</td>
			</xsl:if>
			<xsl:if test="$product='2'">
				<td valign="top">Text</td>
			</xsl:if>
			<td valign="top"><xsl:value-of select="Attribute/DataType/Length"/></td>
		</xsl:if>
		<xsl:if test="$attType='ShortVarChar'">
			<td valign="top">VarChar</td>
			<td valign="top"><xsl:value-of select="Attribute/DataType/Length"/></td>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="doSgAttributes">
	<xsl:param name="dvgPath"/>
		<tr class="subTitulosTabla">
			<td nowrap="yes"><b></b></td>
			<td nowrap="yes"><b>Subtype Name</b></td>
			<td nowrap="yes"><b>Supertype Name</b></td>
		</tr>
		<xsl:for-each select="GXObject/Attributes/GXSubTypeAttribute">
			<tr class="textoGrisTabla">
				<td valign="top"></td>
				<td valign="top">
					<span style="cursor: hand" class="gxobj">
						<xsl:attribute name="onclick">
							<xsl:text>AttCrossReference('</xsl:text>
							<xsl:value-of select="Name"/>
							<xsl:text>');</xsl:text>
						</xsl:attribute>
						<u><xsl:value-of select="Name"/></u>
					</span>
				</td>
				<td valign="top">
					<span style="cursor: hand" class="gxobj">
						<xsl:attribute name="onclick">
							<xsl:text>AttCrossReference('</xsl:text>
							<xsl:value-of select="SuperType"/>
							<xsl:text>');</xsl:text>
						</xsl:attribute>
						<u><xsl:value-of select="SuperType"/></u>
					</span>
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="doIndexes">
	<xsl:param name="objPreffix"/>
	<xsl:param name="dvgPath"/>
	<xsl:param name="hasIndexes"/>
		<xsl:if test="$hasIndexes='true'">
			<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
				<tr>
					<td class="titulosTabla">
						<xsl:attribute name="title">Indexes</xsl:attribute>
						<b>Indexes</b>
					</td>
				</tr>
			</table>
			<table class="gxcontent" cellspacing="0" cellpadding="0" width="100%">
				<xsl:if test="$objPreffix='TBL'">
					<xsl:call-template name="doTblIndexes">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
				<xsl:if test="$objPreffix='DV'">
					<xsl:call-template name="doDvIndexes">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</xsl:if>
			</table>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="doSubordinations">
	<xsl:param name="objPreffix"/>
	<xsl:param name="dvgPath"/>
	<xsl:param name="hasSuperords"/>
	<xsl:param name="hasSubords"/>
		<xsl:if test="$objPreffix='TBL'">
			<xsl:if test="$hasSuperords='true'">
				<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
					<tr>
						<td class="titulosTabla">
							<xsl:attribute name="title">Subordinated To</xsl:attribute>
							<b>Subordinated To</b>
						</td>
					</tr>
				</table>
				<table class="gxcontent" cellspacing="0" cellpadding="0" width="100%">
					<xsl:call-template name="doSuperords">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</table>
			</xsl:if>
			<xsl:if test="$hasSubords='true'">
				<table width="100%" cellpadding="0" cellspacing="0" border="1" bordercolor="#CDAAAD" style="border-collapse: collapse">
					<tr>
						<td class="titulosTabla">
							<xsl:attribute name="title">Superordinated To</xsl:attribute>
							<b>Superordinated To</b>
						</td>
					</tr>
				</table>
				<table class="gxcontent" cellspacing="0" cellpadding="0" width="100%">
					<xsl:call-template name="doSubords">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</table>
			</xsl:if>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="doSuperords">
	<xsl:param name="dvgPath"/>
		<tr class="subTitulosTabla">
			<td nowrap="yes"><b></b></td>
			<td nowrap="yes"><b>Table</b></td>
			<td nowrap="yes"><b>By</b></td>
		</tr>
		<xsl:for-each select="GXObject/SuperordTables/GXForeignKey">
			<tr class="textoGrisTabla">
				<td valign="top"></td>
				<td valign="top">
					<span style="cursor: hand" class="gxobj">
						<xsl:attribute name="onclick">
							<xsl:text>OpenObjectReport('TBL','</xsl:text>
							<xsl:value-of select="FKTableName"/>
							<xsl:text>');</xsl:text>
						</xsl:attribute>
						<u><xsl:value-of select="FKTableName"/></u>
					</span>
				</td>
				<td valign="top">
					<xsl:call-template name="doSubordComposition">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="doSubords">
	<xsl:param name="dvgPath"/>
		<tr class="subTitulosTabla">
			<td nowrap="yes"><b></b></td>
			<td nowrap="yes"><b>Table</b></td>
			<td nowrap="yes"><b>By</b></td>
		</tr>
		<xsl:for-each select="GXObject/SubordTables/GXForeignKey">
			<tr class="textoGrisTabla">
				<td valign="top"></td>
				<td valign="top">
					<span style="cursor: hand" class="gxobj">
						<xsl:attribute name="onclick">
							<xsl:text>OpenObjectReport('TBL','</xsl:text>
							<xsl:value-of select="FKTableName"/>
							<xsl:text>');</xsl:text>
						</xsl:attribute>
						<u><xsl:value-of select="FKTableName"/></u>
					</span>
				</td>
				<td valign="top">
					<xsl:call-template name="doSubordComposition">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="doSubordComposition">
	<xsl:param name="dvgPath"/>
		<table cellspacing="0" cellpadding="0" border="0">
			<xsl:for-each select="FKAttributes/anyType">
				<tr>
					<td>
						<span style="cursor: hand" class="gxobj">
							<xsl:attribute name="onclick">
								<xsl:text>AttCrossReference('</xsl:text>
								<xsl:value-of select="Name"/>
								<xsl:text>');</xsl:text>
							</xsl:attribute>
							<u><xsl:value-of select="Name"/></u>
						</span>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
	
	<xsl:template name="doTblIndexes">
	<xsl:param name="dvgPath"/>
		<tr class="subTitulosTabla">
			<td nowrap="yes"><b></b></td>
			<td nowrap="yes"><b>Name</b></td>
			<td nowrap="yes"><b>Unique - Duplicate</b></td>
			<td nowrap="yes"><b>Composition</b></td>
		</tr>
		<xsl:for-each select="GXObject/Indexes/GXIndex">
			<tr class="textoGrisTabla">
				<td valign="top"></td>
				<td valign="top"><xsl:value-of select="Name"/></td>
				<td valign="top"><xsl:value-of select="UniqueDuplicate"/></td>
				<td valign="top">
					<xsl:call-template name="doIndexComposition">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="doDvIndexes">
	<xsl:param name="dvgPath"/>
		<tr class="subTitulosTabla">
			<td nowrap="yes"><b></b></td>
			<td nowrap="yes"><b>Name</b></td>
			<td nowrap="yes"><b>External Name</b></td>
			<td nowrap="yes"><b>Unique - Duplicate</b></td>
			<td nowrap="yes"><b>Composition</b></td>
		</tr>
		<xsl:for-each select="GXObject/Indexes/GXIndex">
			<tr class="textoGrisTabla">
				<td valign="top"></td>
				<td valign="top"><xsl:value-of select="Name"/></td>
				<td valign="top"><xsl:value-of select="ExternalName"/></td>
				<td valign="top"><xsl:value-of select="UniqueDuplicate"/></td>
				<td valign="top">
					<xsl:call-template name="doIndexComposition">
						<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
					</xsl:call-template>
				</td>
			</tr>
			<xsl:call-template name="doRowSeparator">
				<xsl:with-param name="dvgPath"><xsl:value-of select="$dvgPath"/></xsl:with-param>
			</xsl:call-template>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template name="doIndexComposition">
	<xsl:param name="dvgPath"/>
		<xsl:variable name="i" select="count(Attributes/anyType)"/>
		<table cellspacing="0" cellpadding="0" border="0">
			<xsl:for-each select="Attributes/anyType">
				<tr>
					<td>
						<xsl:call-template name="doIndexAttribute">
							<xsl:with-param name="dvgPath">
								<xsl:value-of select="$dvgPath"/>
							</xsl:with-param>
							<xsl:with-param name="attOrder">
								<xsl:value-of select="order"/>
							</xsl:with-param>
						</xsl:call-template>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
	
	<xsl:template name="doIndexAttribute">
	<xsl:param name="dvgPath"/>
	<xsl:param name="attOrder"/>
		<xsl:if test="$attOrder='Ascending'">
			<img border="0">
				<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\arrow.gif</xsl:attribute>
			</img>
		</xsl:if>
		<xsl:if test="$attOrder='Descending'">
			<img border="0">
				<xsl:attribute name="src"><xsl:value-of select="$dvgPath"/>\packages\reverseengineering\images\arrowDown.gif</xsl:attribute>
			</img>
		</xsl:if>
		<span style="cursor: hand" class="gxobj">
			<xsl:attribute name="onclick">
				<xsl:text>AttCrossReference('</xsl:text>
				<xsl:value-of select="attributeName"/> 
				<xsl:text>');</xsl:text>
			</xsl:attribute>
			<u><xsl:value-of select="attributeName"/></u>
		</span>
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