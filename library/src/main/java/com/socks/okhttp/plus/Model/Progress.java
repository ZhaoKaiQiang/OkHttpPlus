package com.socks.okhttp.plus.model;

import java.io.Serializable;

public class Progress implements Serializable {

    private long currentBytes;
    private long totalBytes;
    private boolean isFinish;

    public Progress(long currentBytes, long totalBytes, boolean isFinish) {
        this.currentBytes = currentBytes;
        this.totalBytes = totalBytes;
        this.isFinish = isFinish;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public boolean isFinish() {
        return isFinish;
    }

}
