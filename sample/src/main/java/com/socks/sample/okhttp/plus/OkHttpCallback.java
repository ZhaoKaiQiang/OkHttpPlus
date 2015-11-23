package com.socks.sample.okhttp.plus;

import android.accounts.NetworkErrorException;
import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by zhaokaiqiang on 15/11/22.
 */
public abstract class OkHttpCallback<T> implements Callback {

    public static final int SUCCESS = 200;
    public static final int ERROR_SERVER = 500;
    public static final int ERROR_CLIENT = 400;

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    private OkHttpParser<T> mParser;

    public OkHttpCallback(OkHttpParser<T> mParser) {
        if (mParser == null) {
            throw new IllegalArgumentException("Parser can't be null");
        }
        this.mParser = mParser;
    }

    @Override
    public void onFailure(Request request, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                onFailure(ERROR_SERVER, e);
            }
        });
    }

    @Override
    public void onResponse(final Response response) {
        final T t = mParser.parseResponse(response);
        final int code = mParser.getCode();

        if (code == SUCCESS && t != null) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onSuccess(code, t);
                }
            });
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onFailure(code, new NetworkErrorException(Integer.toString(code)));
                }
            });
        }
    }

    public abstract void onSuccess(int code, T t);

    public abstract void onFailure(int code, Throwable e);

}