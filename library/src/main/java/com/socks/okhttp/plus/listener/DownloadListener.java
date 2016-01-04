package com.socks.okhttp.plus.listener;

import android.os.Handler;

import com.socks.okhttp.plus.handler.ProgressHandler;
import com.socks.okhttp.plus.handler.UIHandler;
import com.socks.okhttp.plus.model.Progress;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class DownloadListener implements ProgressListener, Callback, UIProgressListener {

    private final Handler mHandler = new UIHandler(this);
    private boolean isFirst = true;
    private String mDestFileDir;
    private String mDestFileName;

    public DownloadListener(String mDestFileDir, String mDestFileName) {
        this.mDestFileDir = mDestFileDir;
        this.mDestFileName = mDestFileName;
    }

    @Override
    public void onResponse(final Response response) {

        File file = null;
        try {
            file = saveFile(response);
        } catch (final IOException e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(e);
                }
            });
        }

        final File newFile = file;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onSuccess(newFile);
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

    public abstract void onSuccess(File file);

    public abstract void onFailure(Exception e);

    public File saveFile(Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File dir = new File(mDestFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, mDestFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }

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

        if (progress.isFinish()) {
            mHandler.obtainMessage(ProgressHandler.FINISH,
                    progress)
                    .sendToTarget();
        }
    }

    public abstract void onUIProgress(Progress progress);

    @Override
    public void onUIStart() {
    }

    @Override
    public void onUIFinish() {
    }
}
