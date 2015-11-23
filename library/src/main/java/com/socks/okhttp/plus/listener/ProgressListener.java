package com.socks.okhttp.plus.listener;

import com.socks.okhttp.plus.model.Progress;

public interface ProgressListener {
    void onProgress(Progress progress);
}