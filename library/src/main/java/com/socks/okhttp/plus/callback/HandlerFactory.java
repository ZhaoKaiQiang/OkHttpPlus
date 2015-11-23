package com.socks.okhttp.plus.callback;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by zhaokaiqiang on 15/11/23.
 */
public class HandlerFactory {
    private static Handler mHandler;

    public static Handler getInstance() {

        if (mHandler == null) {
            synchronized (HandlerFactory.class) {
                if (mHandler == null) {
                    mHandler = new Handler(Looper.getMainLooper());
                }
            }
        }
        return mHandler;
    }

}
