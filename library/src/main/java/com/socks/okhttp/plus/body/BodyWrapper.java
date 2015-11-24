package com.socks.okhttp.plus.body;

import com.socks.okhttp.plus.listener.ProgressListener;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class BodyWrapper {

    public static OkHttpClient addProgressResponseListener(OkHttpClient client, final ProgressListener progressListener) {
        OkHttpClient clone = client.clone();
        clone.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ResponseProgressBody(originalResponse.body(), progressListener))
                        .build();
            }
        });
        return clone;
    }

    public static RequestProgressBody addProgressRequestListener(RequestBody requestBody, ProgressListener progressRequestListener) {
        return new RequestProgressBody(requestBody, progressRequestListener);
    }
}