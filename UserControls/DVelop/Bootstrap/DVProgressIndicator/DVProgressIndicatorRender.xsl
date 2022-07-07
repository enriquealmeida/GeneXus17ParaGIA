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
      <xsl:when test="@type = 'DVelop.Bootstrap.DVProgressIndicator'">
        <xsl:call-template name="RenderDVelop.Bootstrap.DVProgressIndicator"/>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <!-- DVelop.Bootstrap.DVProgressIndicator design render -->
  <!-- ///////////////////  Implement your render here  ///////////////////-->
  <xsl:template name="RenderDVelop.Bootstrap.DVProgressIndicator">
    <span atomicselection="true">
      <div>
        <xsl:attribute name="class">
          <xsl:value-of select="gxca:GetStringPropertyValue('Cls')"/>
        </xsl:attribute>
        <xsl:choose>
          <xsl:when test="gxca:GetStringPropertyValue('Type') = 'Bar'">
            <div class="progress">
              <xsl:attribute name="style">
                <xsl:text>width: </xsl:text>
                <xsl:value-of select="gxca:GetStringPropertyValue('BarWidth')"/>
              </xsl:attribute>
              <div class="progress-bar" >
                <xsl:attribute name="style">
                  <xsl:text>width: </xsl:text>
                  <xsl:value-of select="gxca:GetStringPropertyValue('Percentage')"/>
                  <xsl:text>%</xsl:text>
                </xsl:attribute>
                <xsl:value-of select="gxca:GetStringPropertyValue('Caption')"/>
              </div>
            </div>
          </xsl:when>
          <xsl:otherwise>
            <xsl:attribute name="style">
              <xsl:text>position: relative;width:</xsl:text>
              <xsl:value-of select="gxca:GetStringPropertyValue('CircleWidth')"/>
              <xsl:text>px; height:</xsl:text>
              <xsl:value-of select="gxca:GetStringPropertyValue('CircleWidth')"/>
              <xsl:text>px;</xsl:text>
            </xsl:attribute>
            <svg class="ProgressIndicatorCircle">
              <xsl:attribute name="height">
                <xsl:value-of select="gxca:GetStringPropertyValue('CircleWidth')"/>
              </xsl:attribute>
              <xsl:attribute name="width">
                <xsl:value-of select="gxca:GetStringPropertyValue('CircleWidth')"/>
              </xsl:attribute>
              <xsl:variable name="CircleWidth" select="gxca:GetStringPropertyValue('CircleWidth')"/>
              <xsl:variable name="CircleProgressWidth" select="gxca:GetStringPropertyValue('CircleProgressWidth')"/>
              <xsl:variable name="cirlceRadius" select="($CircleWidth - $CircleProgressWidth) div 2"/>
              <xsl:variable name="center" select="($CircleProgressWidth div 2) + $cirlceRadius"/>
              <circle class="BackCircle">
                <xsl:attribute name="cx">
                  <xsl:value-of select="$center"/>
                </xsl:attribute>
                <xsl:attribute name="cy">
                  <xsl:value-of select="$center"/>
                </xsl:attribute>
                <xsl:attribute name="r">
                  <xsl:value-of select="$cirlceRadius"/>
                </xsl:attribute>
                <xsl:attribute name="style">
                  <xsl:text>stroke-width: </xsl:text>
                  <xsl:value-of select="gxca:GetStringPropertyValue('CircleProgressWidth')"/>
                  <xsl:text>px</xsl:text>
                </xsl:attribute>
              </circle>
              <circle class="ProgressCircle">
                <xsl:attribute name="cx">
                  <xsl:value-of select="$center"/>
                </xsl:attribute>
                <xsl:attribute name="cy">
                  <xsl:value-of select="$center"/>
                </xsl:attribute>
                <xsl:attribute name="r">
                  <xsl:value-of select="$cirlceRadius"/>
                </xsl:attribute>
                <xsl:attribute name="style">
                  <xsl:text>stroke-dasharray: </xsl:text>
                  <xsl:variable name="Percentage" select="gxca:GetStringPropertyValue('Percentage')"/>
                  <xsl:variable name="lineLength" select="2 * $cirlceRadius * 3.14159265359"/>
                  <xsl:choose>
                    <xsl:when test="$Percentage >= 25">
                      <xsl:value-of select="($Percentage - 25) * $lineLength div 100"/>
                      <xsl:text> </xsl:text>
                      <xsl:value-of select="(100 - $Percentage) * $lineLength div 100"/>
                      <xsl:text> </xsl:text>
                      <xsl:value-of select="25 * $lineLength div 100"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:text>0 </xsl:text>
                      <xsl:value-of select="75 * $lineLength div 100"/>
                      <xsl:text> </xsl:text>
                      <xsl:value-of select="$Percentage * $lineLength div 100"/>
                      <xsl:text> </xsl:text>
                      <xsl:value-of select="(25 - $Percentage) * $lineLength div 100"/>
                    </xsl:otherwise>
                  </xsl:choose>
                  <xsl:text>;stroke-width: </xsl:text>
                  <xsl:value-of select="gxca:GetStringPropertyValue('CircleProgressWidth')"/>
                  <xsl:text>px;</xsl:text>
                </xsl:attribute>
              </circle>
            </svg>
            <div class="ProgressIndicatorCircle">
              <xsl:attribute name="style">
                <xsl:text>position: absolute;top: 0;left: 0;width:</xsl:text>
                <xsl:value-of select="gxca:GetStringPropertyValue('CircleWidth')"/>
                <xsl:text>px; height:</xsl:text>
                <xsl:value-of select="gxca:GetStringPropertyValue('CircleWidth')"/>
                <xsl:text>px;</xsl:text>
              </xsl:attribute>
              <xsl:choose>
                <xsl:when test="gxca:GetStringPropertyValue('CircleCaptionType') = 'Caption'">
                  <div style="left: 50%;position: absolute;">
                    <span class="CircleCaption">
                      <xsl:attribute name="style">
                        <xsl:text>position: relative; left: -50%;line-height:</xsl:text>
                        <xsl:value-of select="gxca:GetStringPropertyValue('CircleWidth')"/>
                        <xsl:text>px;</xsl:text>
                      </xsl:attribute>
                      <xsl:value-of select="gxca:GetStringPropertyValue('Caption')"/>
                    </span>
                  </div>
                </xsl:when>
                <xsl:when test="gxca:GetStringPropertyValue('CircleCaptionType') = 'CaptionAndSubtitle'">
                  <div style="left: 50%;position: absolute;" class="CircleCaptionContainer">
                    <span class="CircleCaption" style="left: -50%;position: relative;">
                      <xsl:value-of select="gxca:GetStringPropertyValue('Caption')"/>
                    </span>
                  </div>
                  <div style="left: 50%;position: absolute;" class="CircleSubtitleContainer">
                    <span class="CircleSubtitle" style="left: -50%;position: relative;">
                      <xsl:value-of select="gxca:GetStringPropertyValue('Subtitle')"/>
                    </span>
                  </div>
                </xsl:when>
                <xsl:otherwise>
                  <xsl:value-of select="gxca:GetStringPropertyValue('RawHTML')"/>
                </xsl:otherwise>
              </xsl:choose>
            </div>
          </xsl:otherwise>
        </xsl:choose>
      </div>
    </span>
  </xsl:template>
</xsl:stylesheet>
