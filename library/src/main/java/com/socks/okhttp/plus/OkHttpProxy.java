package com.socks.okhttp.plus;

import com.socks.okhttp.plus.body.BodyWrapper;
import com.socks.okhttp.plus.builder.GetRequestBuilder;
import com.socks.okhttp.plus.builder.PostRequestBuilder;
import com.socks.okhttp.plus.builder.UploadRequestBuilder;
import com.socks.okhttp.plus.listener.DownloadListener;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

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
        getInstance().cancel(tag);
    }


}
