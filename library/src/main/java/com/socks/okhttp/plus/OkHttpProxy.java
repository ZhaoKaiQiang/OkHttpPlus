package com.socks.okhttp.plus;

import com.socks.okhttp.plus.callback.OkCallback;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaokaiqiang on 15/11/22.
 */
public class OkHttpProxy {

    private static final int TIME_CONNECTION_DEFAULT = 10;
    private static final int TIME_READ_DEFAULT = 15;
    private static final int TIME_WRITE_DEFAULT = 15;

    private static OkHttpClient mHttpClient;

    public static void init(int connectionTime, int readTime, int writeTime) {
        synchronized (OkHttpProxy.class) {
            if (mHttpClient == null) {
                mHttpClient = new OkHttpClient();
                mHttpClient.setConnectTimeout(connectionTime, TimeUnit.SECONDS);
                mHttpClient.setReadTimeout(readTime, TimeUnit.SECONDS);
                mHttpClient.setWriteTimeout(writeTime, TimeUnit.SECONDS);
            }
        }
    }

    public static OkHttpClient getInstance() {
        if (mHttpClient == null)
            init(TIME_CONNECTION_DEFAULT, TIME_READ_DEFAULT, TIME_WRITE_DEFAULT);
        return mHttpClient;
    }

    public static Call get(String url, Callback responseCallback) {
        return get(url, null, responseCallback);
    }

    public static Call get(String url, Object tag, Callback responseCallback) {
        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null) {
            builder.tag(tag);
        }
        Request request = builder.build();

        if (responseCallback instanceof OkCallback) {
            ((OkCallback) responseCallback).onStart();
        }

        Call call = getInstance().newCall(request);
        call.enqueue(responseCallback);
        return call;
    }


    public static Call post(String url, Map<String, String> params, Callback responseCallback) {
        return post(url, params, null, responseCallback);
    }

    public static Call post(String url, Map<String, String> params, Object tag, Callback responseCallback) {

        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null) {
            builder.tag(tag);
        }

        FormEncodingBuilder encodingBuilder = new FormEncodingBuilder();

        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                encodingBuilder.add(key, params.get(key));
            }
        }

        RequestBody formBody = encodingBuilder.build();
        builder.post(formBody);

        if (responseCallback instanceof OkCallback) {
            ((OkCallback) responseCallback).onStart();
        }

        Request request = builder.build();
        Call call = getInstance().newCall(request);
        call.enqueue(responseCallback);
        return call;
    }

    public static void cancel(Object tag) {
        getInstance().cancel(tag);
    }

}
