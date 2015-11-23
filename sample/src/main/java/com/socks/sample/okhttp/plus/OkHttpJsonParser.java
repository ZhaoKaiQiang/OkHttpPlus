package com.socks.sample.okhttp.plus;

import android.os.Build;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by zhaokaiqiang on 15/11/22.
 */
public class OkHttpJsonParser<T> extends OkHttpParser<T> {

    protected Gson mGson;
    public Type mType;

    public OkHttpJsonParser() {
        mType = getSuperclassTypeParameter(getClass());
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

    @Nullable
    @Override
    public T parse(Response response) {
        try {
            String body = response.body().string();
            return mGson.fromJson(body, mType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }

}