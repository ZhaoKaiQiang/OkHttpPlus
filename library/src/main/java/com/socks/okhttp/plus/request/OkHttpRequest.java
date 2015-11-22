package com.socks.okhttp.plus.request;

import android.text.TextUtils;
import android.util.Pair;

import com.socks.okhttp.plus.OkHttpClientManager;
import com.socks.okhttp.plus.callback.OkHttpCallback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by zhy on 15/11/6.
 *
 * @author zhaokaiqiang
 *         15/11/16 修改
 */
public abstract class OkHttpRequest {

    protected OkHttpClientManager mOkHttpClientManager = OkHttpClientManager.getInstance();
    protected OkHttpClient mOkHttpClient;
    protected RequestBody requestBody;
    protected Request request;

    protected String url;
    protected String tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;

    protected OkHttpRequest(String url, String tag,
                            Map<String, String> params, Map<String, String> headers) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        mOkHttpClient = mOkHttpClientManager.getOkHttpClient();
    }

    protected abstract Request buildRequest();

    protected abstract RequestBody buildRequestBody();

    public void invokeAsync(OkHttpCallback callback) {
        prepareInvoked(callback);
        mOkHttpClientManager.execute(request, callback);
    }

    public <T> T invoke(Class<T> clazz) throws IOException {
        requestBody = buildRequestBody();
        Request request = buildRequest();
        return mOkHttpClientManager.execute(request, clazz);
    }

    protected void prepareInvoked(OkHttpCallback callback) {
        requestBody = buildRequestBody();
        requestBody = wrapRequestBody(requestBody, callback);
        request = buildRequest();
    }

    protected RequestBody wrapRequestBody(RequestBody requestBody, final OkHttpCallback callback) {
        return requestBody;
    }


    protected void appendHeaders(Request.Builder builder, Map<String, String> headers) {

        if (builder == null) {
            throw new IllegalArgumentException("builder can not be empty!");
        }
        if (headers == null || headers.isEmpty()) {
            return;
        }
        Headers.Builder headerBuilder = new Headers.Builder();
        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    public void cancel() {
        if (!TextUtils.isEmpty(tag))
            mOkHttpClientManager.cancelTag(tag);
    }


    public static class Builder {
        private String url;
        private String tag;
        private Map<String, String> headers;
        private Map<String, String> params;
        private Pair<String, File>[] files;

        private String destFileDir;
        private String destFileName;

        //for post
        private String content;
        private byte[] bytes;
        private File file;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder addParams(String key, String val) {
            if (this.params == null) {
                params = new IdentityHashMap<>();
            }
            params.put(key, val);
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder addHeader(String key, String val) {
            if (this.headers == null) {
                headers = new IdentityHashMap<>();
            }
            headers.put(key, val);
            return this;
        }


        public Builder files(Pair<String, File>... files) {
            this.files = files;
            return this;
        }

        public Builder destFileName(String destFileName) {
            this.destFileName = destFileName;
            return this;
        }

        public Builder destFileDir(String destFileDir) {
            this.destFileDir = destFileDir;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public <T> T get(Class<T> clazz) throws IOException {
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            return request.invoke(clazz);
        }

        public OkHttpRequest get(OkHttpCallback callback) {
            OkHttpRequest request = new OkHttpGetRequest(url, tag, params, headers);
            request.invokeAsync(callback);
            return request;
        }

        public <T> T post(Class<T> clazz) throws IOException {
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, content, bytes, file);
            return request.invoke(clazz);
        }

        public OkHttpRequest post(OkHttpCallback callback) {
            OkHttpRequest request = new OkHttpPostRequest(url, tag, params, headers, content, bytes, file);
            request.invokeAsync(callback);
            return request;
        }

        public OkHttpRequest upload(OkHttpCallback callback) {
            OkHttpRequest request = new OkHttpUploadRequest(url, tag, params, headers, files);
            request.invokeAsync(callback);
            return request;
        }

        public <T> T upload(Class<T> clazz) throws IOException {
            OkHttpRequest request = new OkHttpUploadRequest(url, tag, params, headers, files);
            return request.invoke(clazz);
        }


        public OkHttpRequest download(OkHttpCallback callback) {
            OkHttpRequest request = new OkHttpDownloadRequest(url, tag, params, headers, destFileName, destFileDir);
            request.invokeAsync(callback);
            return request;
        }

        public String download() throws IOException {
            OkHttpRequest request = new OkHttpDownloadRequest(url, tag, params, headers, destFileName, destFileDir);
            return request.invoke(String.class);
        }

    }

}
