package com.genexus.android.core.common.handlers.images

import android.graphics.drawable.Drawable
import com.genexus.android.core.common.ForPostOnUiThread

abstract class ImagesForPostOnUiThread : ForPostOnUiThread<Drawable?>(), OnReceiveImageHandler
