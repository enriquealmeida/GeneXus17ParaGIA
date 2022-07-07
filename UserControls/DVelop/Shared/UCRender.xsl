<?xml version='1.0'?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:msxml="urn:schemas-microsoft-com:xslt"
	xmlns:gx="urn:shemas-artech-com:gx"
	exclude-result-prefixes="msxml gx"
	xmlns:gxca="urn:GXControlAdap">
  <xsl:output method="html"/>
  <xsl:template match="/" >
    <xsl:apply-templates select="/GxControl"/>
  </xsl:template>
  <xsl:template match="GxControl">
    <img>
      <xsl:attribute name="src">
        <xsl:value-of select="gxca:GetMyPath()"/>
        <xsl:choose>
          <xsl:when test="@type = 'DVelop.Bootstrap.DropDownOptions'">
            <xsl:call-template name="RenderDVelopBootstrapDropDownOptions"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.Bootstrap.SidebarMenu'">
            <xsl:call-template name="RenderDVelopBootstrapSidebarMenu"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.Bootstrap.TagsInput'">
            <xsl:call-template name="RenderDVelopBootstrapTagsInput"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.Bootstrap.Tooltip'">
            <xsl:call-template name="RenderDVelopBootstrapTooltip"/>
          </xsl:when>
          <xsl:when test="@type = 'WWP.Chronometer'">
            <xsl:call-template name="RenderWWPChronometer"/>
          </xsl:when>
          <xsl:when test="@type = 'WWP.DatePicker'">
            <xsl:call-template name="RenderWWPDatePicker"/>
          </xsl:when>
          <xsl:when test="@type = 'WWP.DateRangePicker'">
            <xsl:call-template name="RenderWWPDateRangePicker"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.DVGroupBy'">
            <xsl:call-template name="RenderDVelopDVGroupBy"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.DVHorizontalMenu'">
            <xsl:call-template name="RenderDVelopDVHorizontalMenu"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.DVImageZoom'">
            <xsl:call-template name="RenderDVelopDVImageZoom"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.DVMessage'">
            <xsl:call-template name="RenderDVelopDVMessage"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.DVPaginationBar'">
            <xsl:call-template name="RenderDVelopDVPaginationBar"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.DVTabsTransform'">
            <xsl:call-template name="RenderDVelopDVTabsTransform"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.DynamicWebCanvas'">
            <xsl:call-template name="RenderDVelopDynamicWebCanvas"/>
          </xsl:when>
          <xsl:when test="@type = 'WWP.GridEmpowerer'">
            <xsl:call-template name="RenderWWPGridEmpowerer"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.GridTitlesCategories'">
            <xsl:call-template name="RenderDVelopGridTitlesCategories"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.GXBootstrap.DDComponent'">
            <xsl:call-template name="RenderDVelopBootstrapDropDownOptions"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.GXBootstrap.DDOExtendedCombo'">
            <xsl:call-template name="RenderDVelopBootstrapDropDownOptions"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.GXBootstrap.DDOGridColumnsSelector'">
            <xsl:call-template name="RenderDVelopBootstrapDropDownOptions"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.GXBootstrap.DDOGridTitleSettings'">
            <xsl:call-template name="RenderDVelopBootstrapDropDownOptions"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.GXBootstrap.DDOGridTitleSettingsM'">
            <xsl:call-template name="RenderDVelopBootstrapDropDownOptions"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.GXBootstrap.DDORegular'">
            <xsl:call-template name="RenderDVelopBootstrapDropDownOptions"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.GXBootstrap.DropDownOptions'">
            <xsl:call-template name="RenderDVelopBootstrapDropDownOptions"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.GXBootstrap.SidebarMenu'">
            <xsl:call-template name="RenderDVelopBootstrapSidebarMenu"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.GXBootstrap.TagsInput'">
            <xsl:call-template name="RenderDVelopBootstrapTagsInput"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.GXBootstrap.Tooltip'">
            <xsl:call-template name="RenderDVelopBootstrapTooltip"/>
          </xsl:when>
          <xsl:when test="@type = 'WWP.Suggest'">
            <xsl:call-template name="RenderWWPSuggest"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.WorkWithPlusUtilities'">
            <xsl:call-template name="RenderDVelopWorkWithPlusUtilities"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.WorkWithPlusUtilities_F5'">
            <xsl:call-template name="RenderDVelopWorkWithPlusUtilities"/>
          </xsl:when>
          <xsl:when test="@type = 'DVelop.WWPPopover'">
            <xsl:call-template name="RenderDVelopWWPPopover"/>
          </xsl:when>
        </xsl:choose>
      </xsl:attribute>
    </img>
  </xsl:template>

  <xsl:template name="RenderDVelopBootstrapDropDownOptions">
    <xsl:text>\Bootstrap\DropDownOptions\DVelopBootstrapDropDownOptions.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopBootstrapSidebarMenu">
    <xsl:text>\Bootstrap\SidebarMenu\SideBarMenuExample.gif</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopBootstrapTagsInput">
    <xsl:text>\Bootstrap\TagsInput\DVelopBootstrapTagsInput.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopBootstrapTooltip">
    <xsl:text>\Bootstrap\Tooltip\TooltipExample.gif</xsl:text>
  </xsl:template>

  <xsl:template name="RenderWWPChronometer">
    <xsl:text>\Chronometer\Chronometer.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderWWPDatePicker">
    <xsl:text>\DatePicker\DatePickerIcon.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderWWPDateRangePicker">
    <xsl:text>\DateRangePicker\DateRangeIcon.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopDVGroupBy">
    <xsl:text>\DVGroupBy\DVGroupBy.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopDVHorizontalMenu">
    <xsl:text>\DVHorizontalMenu\DVHorizontalMenu.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopDVImageZoom">
    <xsl:text>\DVImageZoom\DVImageZoom.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopDVMessage">
    <xsl:text>\DVMessage\DVMessage.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopDVPaginationBar">
    <xsl:text>\DVPaginationBar\DVPaginationBar.gif</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopDVTabsTransform">
    <xsl:text>\DVTabsTransform\DVTabsTransform.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopDynamicWebCanvas">
    <xsl:text>\DynamicWebCanvas\DynamicWebCanvas.gif</xsl:text>
  </xsl:template>

  <xsl:template name="RenderWWPGridEmpowerer">
    <xsl:text>\GridEmpowerer\GridEmpowerer.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopGridTitlesCategories">
    <xsl:text>\GridTitlesCategories\GridTitlesCategories.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderWWPSuggest">
    <xsl:text>\Suggest\SuggestIcon.png</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopWorkWithPlusUtilities">
    <xsl:text>\WorkWithPlusUtilities\WWPUtilities.gif</xsl:text>
  </xsl:template>

  <xsl:template name="RenderDVelopWWPPopover">
    <xsl:text>\Popover\WWPPopover.gif</xsl:text>
  </xsl:template>

</xsl:stylesheet>
