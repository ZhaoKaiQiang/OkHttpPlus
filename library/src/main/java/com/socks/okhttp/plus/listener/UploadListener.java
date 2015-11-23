package com.socks.okhttp.plus.listener;

import android.os.Handler;

import com.socks.okhttp.plus.callback.ProgressHandler;
import com.socks.okhttp.plus.callback.UIHandler;
import com.socks.okhttp.plus.model.Progress;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public abstract class UploadListener implements ProgressListener, Callback, UIProgressListener {

    private final Handler mHandler = new UIHandler(this);
    private boolean isFirst = true;

    @Override
    public void onResponse(final Response response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(response);
            }
        });
    }

    @Override
    public void onFailure(Request request, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(e);
            }
        });
    }

    public abstract void onSuccess(Response response);

    public abstract void onFailure(Exception e);

    @Override
    public void onProgress(Progress progress) {

        if (!isFirst) {
            isFirst = true;
            mHandler.obtainMessage(ProgressHandler.START, progress)
                    .sendToTarget();
        }

        mHandler.obtainMessage(ProgressHandler.UPDATE,
                progress)
                .sendToTarget();

        if (progress.isDone()) {
            mHandler.obtainMessage(ProgressHandler.FINISH,
                    progress)
                    .sendToTarget();
        }
    }

    public abstract void onUIProgress(Progress progress);

    public void onUIStart() {
    }

    public void onUIFinish() {
    }
}
