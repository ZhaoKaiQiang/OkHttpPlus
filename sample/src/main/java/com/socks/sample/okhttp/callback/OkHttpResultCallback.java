package com.socks.sample.okhttp.callback;

import android.support.v7.app.ActionBar;

import com.socks.okhttp.plus.callback.OkCallback;
import com.squareup.okhttp.Request;

/**
 * Created by zhaokaiqiang on 15/11/16.
 */
public abstract class OkHttpResultCallback<T> extends OkCallback<T> {

    private ActionBar toolbar;

    public OkHttpResultCallback(ActionBar toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public void onBefore(Request request) {
        super.onBefore(request);
        toolbar.setTitle("loading...");
    }

    @Override
    public void onAfter() {
        super.onAfter();
        toolbar.setTitle("OkHttpPlus");
    }
}