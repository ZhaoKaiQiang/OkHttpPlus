package com.socks.okhttp.plus.parser;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * Created by zhaokaiqiang on 15/11/22.
 */
public abstract class OkJsonParser<T> extends OkBaseJsonParser<T> {

    protected Gson mGson;

    public OkJsonParser() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithModifiers(
                            Modifier.FINAL,
                            Modifier.TRANSIENT,
                            Modifier.STATIC);
            mGson = gsonBuilder.create();
        } else {
            mGson = new Gson();
        }
    }

    @Override
    public T parse(Response response) throws IOException {
        String body = response.body().string();
        return mGson.fromJson(body, mType);
    }
}