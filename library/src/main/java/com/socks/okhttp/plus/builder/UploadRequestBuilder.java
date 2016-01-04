package com.socks.okhttp.plus.builder;

import android.util.Pair;

import com.socks.okhttp.plus.OkHttpProxy;
import com.socks.okhttp.plus.body.BodyWrapper;
import com.socks.okhttp.plus.listener.UploadListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhaokaiqiang on 15/11/24.
 */
public class UploadRequestBuilder extends RequestBuilder {

    private static final int DEFAULT_TIME_OUT = 30;

    private int connectTimeOut;
    private int writeTimeOut;
    private int readTimeOut;
    private Pair<String, File> file;
    private Map<String, String> headers;

    public UploadRequestBuilder file(Pair<String, File> file) {
        this.file = file;
        return this;
    }

    public UploadRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public UploadRequestBuilder setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public UploadRequestBuilder addParams(String key, String value) {
        if (params == null) {
            params = new IdentityHashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public UploadRequestBuilder headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public UploadRequestBuilder addHeader(String key, String values) {
        if (headers == null) {
            headers = new IdentityHashMap<>();
        }
        headers.put(key, values);
        return this;
    }

    public UploadRequestBuilder tag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * @param connectTimeOut unit is minute
     * @return
     */
    public UploadRequestBuilder setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
        return this;
    }

    /**
     * @param writeTimeOut unit is minute
     * @return
     */
    public UploadRequestBuilder setWriteTimeOut(int writeTimeOut) {
        this.writeTimeOut = writeTimeOut;
        return this;
    }

    /**
     * @param readTimeOut unit is minute
     * @return
     */
    public UploadRequestBuilder setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public Call start(UploadListener uploadListener) {

        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        addParams(multipartBuilder, params);
        addFiles(multipartBuilder, file);

        Request.Builder builder = new Request.Builder();
        appendHeaders(builder, headers);
        Request request = builder.url(url).post(BodyWrapper.addProgressRequestListener(multipartBuilder.build(), uploadListener)).build();

        OkHttpClient.Builder clientBuilder = OkHttpProxy.getInstance().newBuilder();

        if (connectTimeOut > 0) {
            clientBuilder.connectTimeout(connectTimeOut, TimeUnit.MINUTES);
        } else {
            clientBuilder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.MINUTES);
        }

        if (writeTimeOut > 0) {
            clientBuilder.writeTimeout(writeTimeOut, TimeUnit.MINUTES);
        } else {
            clientBuilder.writeTimeout(DEFAULT_TIME_OUT, TimeUnit.MINUTES);
        }

        if (readTimeOut > 0) {
            clientBuilder.readTimeout(readTimeOut, TimeUnit.MINUTES);
        } else {
            clientBuilder.readTimeout(DEFAULT_TIME_OUT, TimeUnit.MINUTES);
        }

        OkHttpClient clone = clientBuilder.build();
        Call call = clone.newCall(request);
        call.enqueue(uploadListener);
        return call;
    }


    private static void addParams(MultipartBody.Builder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private static void addFiles(MultipartBody.Builder builder, Pair<String, File>... files) {
        if (files != null) {
            RequestBody fileBody;
            for (int i = 0; i < files.length; i++) {
                Pair<String, File> filePair = files[i];
                String fileKeyName = filePair.first;
                File file = filePair.second;
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeyName + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        } else {
            throw new IllegalArgumentException("File can not be null");
        }
    }

    private static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    @Override
    Call enqueue(Callback callback) {
        return null;
    }

    @Override
    Response execute() throws IOException {
        return null;
    }

}
