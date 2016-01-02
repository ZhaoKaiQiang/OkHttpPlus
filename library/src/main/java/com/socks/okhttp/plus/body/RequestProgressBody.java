package com.socks.okhttp.plus.body;

import com.socks.okhttp.plus.listener.ProgressListener;
import com.socks.okhttp.plus.model.Progress;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by zhaokaiqiang on 15/11/23.
 */
public class RequestProgressBody extends RequestBody {

    private final RequestBody requestBody;
    private final ProgressListener progressListener;
    private BufferedSink bufferedSink;

    public RequestProgressBody(RequestBody requestBody, ProgressListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long bytesWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                if (progressListener != null) {
                    progressListener.onProgress(new Progress(bytesWritten, contentLength, bytesWritten == contentLength));
                }
            }
        };
    }
}