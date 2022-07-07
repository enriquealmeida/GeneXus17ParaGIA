package com.genexus.android.core.common.handlers.fonts

import android.graphics.Typeface
import com.genexus.android.core.common.ForPostOnUiThread

abstract class FontsForPostOnUiThread : ForPostOnUiThread<Typeface?>(), OnReceiveFontHandler
