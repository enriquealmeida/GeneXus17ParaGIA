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
    <xsl:choose>
      <xsl:when test="@type = 'FileUpload'">
        <xsl:call-template name="RenderFileUpload"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- FileUpload design render -->
  <xsl:template name="RenderFileUpload">
    <xsl:variable name="ConvertedPath" select="translate(gxca:GetMyPath(),'\','/')"/>
    <div atomicselection="true">
      <div style="display:none;">&amp;nbsp;</div>
      <style>
        <xsl:attribute name="type">text/css</xsl:attribute>
        <xsl:text>@import url("file:///</xsl:text>
        <xsl:value-of select='$ConvertedPath'/>
        <xsl:text>/fileupload.min.css</xsl:text>
        <xsl:text>");</xsl:text>
      </style>
      <div style="display:none;">&#160;</div>
      <div class="gx-fileupload">
        <div class="row fileupload-buttonbar">
          <div class="col-lg-7">
            <span class="btn btn-success fileinput-button">
              <i class="glyphicon glyphicon-plus"></i>
              <span>Add files...</span>
            </span>
            <xsl:text>&#160;</xsl:text>
            <button type="submit" class="btn btn-primary start">
              <i class="glyphicon glyphicon-upload"></i>
              <span>Start upload</span>
            </button>
            <xsl:text>&#160;</xsl:text>
            <button type="reset" class="btn btn-warning cancel">
              <i class="glyphicon glyphicon-ban-circle"></i>
              <span>Cancel upload</span>
            </button>
            <xsl:text>&#160;</xsl:text>
            <span class="fileupload-process"></span>
          </div>
          <div class="col-lg-5 fileupload-progress fade">
            <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100">
              <div class="progress-bar progress-bar-success" style="width:0%;"></div>
            </div>
            <div class="progress-extended">&#160;</div>
          </div>
        </div>
      </div>
    </div>
  </xsl:template>
  <xsl:template name="string-replace-all">
    <xsl:param name="text" />
    <xsl:param name="replace" />
    <xsl:param name="by" />
    <xsl:choose>
      <xsl:when test="contains($text, $replace)">
        <xsl:value-of select="substring-before($text,$replace)" />
        <xsl:value-of select="$by" />
        <xsl:call-template name="string-replace-all">
          <xsl:with-param name="text"
          select="substring-after($text,$replace)" />
          <xsl:with-param name="replace" select="$replace" />
          <xsl:with-param name="by" select="$by" />
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$text" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>