package com.socks.okhttp.plus;

import com.socks.okhttp.plus.body.BodyWrapper;
import com.socks.okhttp.plus.builder.GetRequestBuilder;
import com.socks.okhttp.plus.builder.PostRequestBuilder;
import com.socks.okhttp.plus.builder.UploadRequestBuilder;
import com.socks.okhttp.plus.listener.DownloadListener;

import okhttp3.Call;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by zhaokaiqiang on 15/11/22.
 */
public class OkHttpProxy {

    private static OkHttpClient mHttpClient;

    private static OkHttpClient init() {
        synchronized (OkHttpProxy.class) {
            if (mHttpClient == null) {
                mHttpClient = new OkHttpClient();
            }
        }
        return mHttpClient;
    }

    public static OkHttpClient getInstance() {
        return mHttpClient == null ? init() : mHttpClient;
    }

    public static void setInstance(OkHttpClient okHttpClient) {
        OkHttpProxy.mHttpClient = okHttpClient;
    }

    public static GetRequestBuilder get() {
        return new GetRequestBuilder();
    }

    public static PostRequestBuilder post() {
        return new PostRequestBuilder();
    }

    public static Call download(String url, DownloadListener downloadListener) {
        Request request = new Request.Builder().url(url).build();
        Call call = BodyWrapper.addProgressResponseListener(getInstance(), downloadListener).newCall(request);
        call.enqueue(downloadListener);
        return call;
    }

    /**
     * default time out is 30 min
     */
    public static UploadRequestBuilder upload() {
        return new UploadRequestBuilder();
    }

    public static void cancel(Object tag) {
        Dispatcher dispatcher = getInstance().dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


}
