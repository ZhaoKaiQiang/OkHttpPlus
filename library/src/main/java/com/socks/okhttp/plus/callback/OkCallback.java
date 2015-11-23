package com.socks.okhttp.plus.callback;

import com.google.gson.internal.$Gson$Types;
import com.socks.library.KLog;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class OkCallback<T> {

    public Type mType;

    public OkCallback() {
        mType = getSuperclassTypeParameter(getClass());
        KLog.d("library = "+mType.getClass().getSimpleName());
    }

    public void onBefore(Request request) {
    }

    public void onAfter() {
    }

    public void onProgress(float progress) {
    }

    public abstract void onError(Request request, Exception e);

    public abstract void onResponse(Response response, T data);

    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameter = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameter.getActualTypeArguments()[0]);
    }

}