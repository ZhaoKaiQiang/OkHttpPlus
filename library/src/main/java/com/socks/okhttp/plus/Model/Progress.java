package com.socks.okhttp.plus.model;

import java.io.Serializable;

public class Progress implements Serializable {

    private long currentBytes;
    private long contentLength;
    private boolean done;

    public Progress(long currentBytes, long contentLength, boolean done) {
        this.currentBytes = currentBytes;
        this.contentLength = contentLength;
        this.done = done;
    }

    public long getCurrentBytes() {
        return currentBytes;
    }

    public long getContentLength() {
        return contentLength;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        return "ProgressModel{" +
                "currentBytes=" + currentBytes +
                ", contentLength=" + contentLength +
                ", done=" + done +
                '}';
    }
}
