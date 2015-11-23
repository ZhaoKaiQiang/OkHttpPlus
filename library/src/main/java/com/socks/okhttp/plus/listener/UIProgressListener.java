package com.socks.okhttp.plus.listener;

import com.socks.okhttp.plus.model.Progress;

/**
 * Created by zhaokaiqiang on 15/11/23.
 */
public interface UIProgressListener {

    void onUIProgress(Progress progress);

    void onUIStart();

    void onUIFinish();
}
