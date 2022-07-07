package com.genexus.android.printer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;

import com.genexus.android.core.base.services.Services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

// Most of this class was extracted from Bluetooth Print app from Play Store
public class PrintImage {
    private int mBmHeight;
    private Bitmap mBmPrint;
    private Bitmap mBmSource;
    private int mBmWidth;
    private int[] mPixels;
    private boolean mResized;

    private static final int BLACK = 1;
    private static final int WHITE = 0;

    private static final byte FEED_LINE = (byte) 10;
    private static final byte ESCAPE = (byte) 27;
    private static final byte GROUP_SEPARATOR = (byte) 29;
    public static final byte[] PAPER_CUT = {GROUP_SEPARATOR, (byte)'V', 0};
    private static final byte[] SET_LINE_SPACING_24 = {ESCAPE, (byte)'3', 24};
    private static final byte[] SET_LINE_SPACING_DEFAULT = {ESCAPE, (byte)'2'};

    public PrintImage(Bitmap source) {
        this.mBmSource = source;
        this.mResized = false;
        int width = this.mBmSource.getWidth();
        int height = this.mBmSource.getHeight();
        if (width > 576) {
            this.mBmSource = getResizedBitmap(this.mBmSource, 576, (int) Math.floor((double) (((float) height) * (((float) 576) / ((float) width)))));
        } else {
            this.mResized = true;
            if (Math.floor((double) (width / 8)) != ((double) width) / 8.0d) {
                int newWidth = (int) Math.floor((double) (width / 8));
                this.mBmSource = getResizedBitmap(this.mBmSource, newWidth, (int) Math.floor((double) (((float) height) * (((float) newWidth) / ((float) width)))));
            }
        }
        this.mBmWidth = this.mBmSource.getWidth();
        this.mBmHeight = this.mBmSource.getHeight();
        this.mPixels = new int[(width * height)];
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / ((float) width);
        float scaleHeight = ((float) newHeight) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public byte[] getPrintImageData() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte wh = (byte) (this.mBmWidth / 256);
            byte wl = (byte) (this.mBmWidth % 256);

            // ESC/POS Command
            // http://www.starmicronics.com/support/mannualfolder/escpos_cm_en.pdf
            out.write(SET_LINE_SPACING_24);
            byte[] selectBitImageMode = new byte[5];
            selectBitImageMode[0] = ESCAPE;
            selectBitImageMode[1] = (byte) '*';
            selectBitImageMode[2] = (byte) 33; // 24-dot double density
            selectBitImageMode[3] = wl;
            selectBitImageMode[4] = wh;

            for (int offset = 0; offset < this.mBmHeight; offset += 24) {
                out.write(selectBitImageMode);
                for (int x = 0; x < this.mBmWidth; ++x) {
                    for (int k = 0; k < 3; ++k) {
                        int slice = 0;
                        for (int b = 0; b < 8; ++b) {
                            int y = offset + k * 8 + b;
                            int i = y * this.mBmWidth + x;
                            slice <<= 1;
                            if (i < this.mPixels.length && this.mPixels[i] == BLACK)
                                slice |= 1;
                        }
                        out.write((byte) slice);
                    }
                }
                out.write(FEED_LINE);
            }
            out.write(SET_LINE_SPACING_DEFAULT);

            return out.toByteArray();
        } catch (IOException e) {
            Services.Log.error(e);
            return new byte[0];
        }
    }

    public void prepareImage() {
        ditherFloydSteinberg(128);
    }

    private void ditherFloydSteinberg(int brightValue) {
        int w = this.mBmWidth;
        int h = this.mBmHeight;
        brightValue -= 128;
        if (this.mBmPrint != null) {
            this.mBmPrint.recycle();
        }
        this.mBmPrint = this.mBmSource.copy(this.mBmSource.getConfig(), true);
        this.mBmPrint.getPixels(this.mPixels, 0, this.mBmWidth, 0, 0, this.mBmWidth, this.mBmHeight);
        w++;
        h++;
        int[] tab = new int[w * h];
        for (int y = 0; y < h - 1; y++) {
            int wy = w * y;
            int wyBm = this.mBmWidth * y;
            for (int x = 0; x < w - 1; x++) {
                if (x == w - 1 || y == h - 1) {
                    tab[wy + x] = 0;
                } else {
                    int pixel = this.mPixels[wyBm + x];
                    int alpha = Color.alpha(pixel);
                    int noAlpha = 255 - alpha;
                    int red = noAlpha + (alpha * Color.red(pixel)) / 255;
                    int green = noAlpha + (alpha * Color.green(pixel)) / 255;
                    int blue = noAlpha + (alpha * Color.blue(pixel)) / 255;
                    tab[wy + x] = (red * 76 + blue * 151 + green * 29) / 256 + brightValue;
                }
            }
        }
        for (int y = 0; y < h - 2; y++) {
            for (int x = 0; x < w - 2; x++) {
                int offset = x + (y * w);
                int gc = tab[offset];
                int g = gc < 128 ? 0 : 255;
                gc -= g;
                tab[offset] = g;
                tab[offset + 1] = tab[offset + 1] + ((gc * 7) / 16);
                tab[(offset - 1) + w] = tab[(offset - 1) + w] + ((gc * 3) / 16);
                tab[offset + w] = tab[offset + w] + ((gc * 5) / 16);
                tab[(offset + 1) + w] = tab[(offset + 1) + w] + (gc / 16);
            }
        }
        for (int y = 0; y < h - 1; y++) {
            int wyx = w * y;
            int wyxBm = this.mBmWidth * y;
            for (int x = 0; x < w - 1; x++) {
                this.mPixels[wyxBm++] = tab[wyx++] == 0 ? BLACK : WHITE;
            }
        }
        this.mBmPrint.setPixels(this.mPixels, 0, this.mBmWidth, 0, 0, this.mBmWidth, this.mBmHeight);
    }
}
