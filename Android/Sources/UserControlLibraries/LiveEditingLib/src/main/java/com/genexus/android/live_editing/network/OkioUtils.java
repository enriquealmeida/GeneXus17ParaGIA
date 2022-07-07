package com.genexus.android.live_editing.network;

import java.io.IOException;

import okio.Buffer;
import okio.GzipSink;
import okio.GzipSource;

class OkioUtils {
    private OkioUtils() { }

    public static Buffer gzip(Buffer content) throws IOException {
        Buffer result = new Buffer();

        GzipSink sink = new GzipSink(result);
        sink.write(content, content.size());
        sink.close();

        return result;
    }

    public static Buffer gunzip(Buffer gzipped) throws IOException {
        Buffer result = new Buffer();

        GzipSource source = new GzipSource(gzipped);
        while (source.read(result, Integer.MAX_VALUE) != -1) { }
        source.close();

        return result;
    }
}
