package com.socks.okhttp.plus.listener;

import android.os.Handler;

import com.socks.okhttp.plus.callback.HandlerFactory;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhaokaiqiang on 15/11/23.
 */
public abstract class OkDownloadListener implements ProgressListener, Callback {

    private String mDestFileDir;
    private String mDestFileName;
    private Handler mHandler;

    public OkDownloadListener(String destFileDir, String destFileName) {
        mDestFileDir = destFileDir;
        mDestFileName = destFileName;
        mHandler = HandlerFactory.getInstance();
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
}
