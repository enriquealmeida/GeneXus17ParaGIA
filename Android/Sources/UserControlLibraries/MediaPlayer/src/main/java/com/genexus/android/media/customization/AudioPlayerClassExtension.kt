package com.genexus.android.media.customization

import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition

const val AUDIO_PLAYER_CLASS_NAME = "AudioController"

val ThemeClassDefinition.miniPlayerTitleClass: ThemeClassDefinition?
    get() = getRelatedClass("MiniPlayerTitleLabelClass")

val ThemeClassDefinition.miniPlayerSubtitleClass: ThemeClassDefinition?
    get() = getRelatedClass("MiniPlayerSubtitleLabelClass")

val ThemeClassDefinition.miniPlayerImageClass: ThemeClassDefinition?
    get() = getRelatedClass("MiniPlayerImageClass")

val ThemeClassDefinition.miniPlayerButtonClass: ThemeClassDefinition?
    get() = getRelatedClass("MiniPlayerPlayPauseButtonClass")

val ThemeClassDefinition.fullScreenPlayerTitleClass: ThemeClassDefinition?
    get() = getRelatedClass("FullScreenPlayerTitleLabelClass")

val ThemeClassDefinition.fullScreenPlayerSubtitleClass: ThemeClassDefinition?
    get() = getRelatedClass("FullScreenPlayerSubtitleLabelClass")

val ThemeClassDefinition.fullScreenPlayerImageClass: ThemeClassDefinition?
    get() = getRelatedClass("FullScreenPlayerImageClass")

val ThemeClassDefinition.fullScreenPlayerPlayPauseButtonClass: ThemeClassDefinition?
    get() = getRelatedClass("FullScreenPlayerPlayPauseButtonClass")

val ThemeClassDefinition.fullScreenPlayerButtonsClass: ThemeClassDefinition?
    get() = getRelatedClass("FullScreenPlayerButtonsClass")
