package com.socks.okhttp.plus.builder;

import android.text.TextUtils;

import com.socks.okhttp.plus.OkHttpProxy;
import com.socks.okhttp.plus.callback.OkCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by zhaokaiqiang on 15/11/24.
 */
public class PostRequestBuilder<T> extends RequestBuilder<T> {

    private Map<String, String> headers;

    public PostRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public PostRequestBuilder setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public PostRequestBuilder addParams(String key, String value) {
        if (params == null) {
            params = new IdentityHashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public PostRequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public PostRequestBuilder addHeader(String key, String values) {
        if (headers == null) {
            headers = new IdentityHashMap<>();
        }
        headers.put(key, values);
        return this;
    }

    public PostRequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }


    @Override
    public Call enqueue(Callback callback) {

        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be null !");
        }

        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null) {
            builder.tag(tag);
        }
        FormBody.Builder encodingBuilder = new FormBody.Builder();
        appendParams(encodingBuilder, params);
        appendHeaders(builder, headers);
        builder.post(encodingBuilder.build());
        Request request = builder.build();

        if (callback instanceof OkCallback) {
            ((OkCallback) callback).onStart();
        }
        Call call = OkHttpProxy.getInstance().newCall(request);
        call.enqueue(callback);
        return call;
    }

    @Override
    public Response execute() throws IOException {
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url can not be null !");
        }

        Request.Builder builder = new Request.Builder().url(url);
        if (tag != null) {
            builder.tag(tag);
        }
        FormBody.Builder encodingBuilder = new FormBody.Builder();
        appendParams(encodingBuilder, params);
        appendHeaders(builder, headers);
        builder.post(encodingBuilder.build());
        Request request = builder.build();

        Call call = OkHttpProxy.getInstance().newCall(request);
        return call.execute();
    }
}
