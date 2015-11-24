package com.socks.okhttp.plus;

import android.util.Pair;

import com.socks.okhttp.plus.body.BodyWrapper;
import com.socks.okhttp.plus.callback.OkCallback;
import com.socks.okhttp.plus.listener.DownloadListener;
import com.socks.okhttp.plus.listener.UploadListener;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
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
        return post(url, params, responseCallback, null);
    }

    public static Call post(String url, Map<String, String> params, Callback responseCallback,Object tag) {

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

        builder.post(encodingBuilder.build());

        if (responseCallback instanceof OkCallback) {
            ((OkCallback) responseCallback).onStart();
        }

        Request request = builder.build();
        Call call = getInstance().newCall(request);
        call.enqueue(responseCallback);
        return call;
    }

    public static Call download(String url, DownloadListener downloadListener) {
        Request request = new Request.Builder().url(url).build();
        Call call = BodyWrapper.addProgressResponseListener(getInstance(), downloadListener).newCall(request);
        call.enqueue(downloadListener);
        return call;
    }

    /**
     * 上传文件，默认超时时间为30min
     *
     * @param url
     * @param file
     * @param params
     * @param uploadListener
     * @return
     */
    public static Call upload(String url, Pair<String, File> file, Map<String, String> params, UploadListener uploadListener) {

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        addParams(builder, params);
        addFiles(builder, file);

        Request request = new Request.Builder()
                .url(url)
                .post(BodyWrapper.addProgressRequestListener(builder.build(), uploadListener))
                .build();

        OkHttpClient clone = getInstance().clone();
        clone.setWriteTimeout(30, TimeUnit.MINUTES);
        clone.setConnectTimeout(30, TimeUnit.MINUTES);
        clone.setReadTimeout(30, TimeUnit.MINUTES);
        Call call = clone.newCall(request);
        call.enqueue(uploadListener);
        return call;
    }

    public static void cancel(Object tag) {
        getInstance().cancel(tag);
    }

    private static void addParams(MultipartBuilder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private static void addFiles(MultipartBuilder builder, Pair<String, File>... files) {
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

}
