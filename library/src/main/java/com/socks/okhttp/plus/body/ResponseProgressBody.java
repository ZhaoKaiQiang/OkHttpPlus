package com.socks.okhttp.plus.body;

import com.socks.okhttp.plus.listener.ProgressListener;
import com.socks.okhttp.plus.model.Progress;
import okhttp3.MediaType;
import okhttp3.ResponseBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by zhaokaiqiang on 15/11/23.
 */
public class ResponseProgressBody extends ResponseBody {

    private final ResponseBody mResponseBody;
    private final ProgressListener mProgressListener;
    private BufferedSource bufferedSource;

    public ResponseProgressBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.mResponseBody = responseBody;
        this.mProgressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {

            long totalBytesRead;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += ((bytesRead != -1) ? bytesRead : 0);
                if (mProgressListener != null) {
                    mProgressListener.onProgress(new Progress(totalBytesRead, mResponseBody.contentLength(), bytesRead == -1));
                }
                return bytesRead;
            }
        };
    }
}