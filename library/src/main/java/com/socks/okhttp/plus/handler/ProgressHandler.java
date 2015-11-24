package com.socks.okhttp.plus.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.socks.okhttp.plus.listener.UIProgressListener;
import com.socks.okhttp.plus.model.Progress;

import java.lang.ref.WeakReference;

public abstract class ProgressHandler extends Handler {

    public static final int UPDATE = 0x01;
    public static final int START = 0x02;
    public static final int FINISH = 0x03;

    private final WeakReference<UIProgressListener> mListenerWeakRef;

    public ProgressHandler(UIProgressListener uiProgressListener) {
        super(Looper.getMainLooper());
        mListenerWeakRef = new WeakReference<>(uiProgressListener);
    }

    @Override
    public void handleMessage(Message msg) {
        UIProgressListener listener = mListenerWeakRef.get();
        switch (msg.what) {
            case UPDATE:
                if (listener != null)
                    progress(listener, (Progress) msg.obj);
                break;
            case START:
                if (mListenerWeakRef.get() != null)
                    start(listener);
                break;
            case FINISH:
                if (mListenerWeakRef.get() != null)
                    finish(listener);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    public abstract void start(UIProgressListener listener);

    public abstract void progress(UIProgressListener listener, Progress progress);

    public abstract void finish(UIProgressListener listener);
}
