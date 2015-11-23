package com.socks.sample.okhttp.plus;

import android.support.annotation.Nullable;

import com.squareup.okhttp.Response;

/**
 * 负责对返回值进行解析，使用的是策略设计模式
 * Created by zhaokaiqiang on 15/11/22.
 */
public abstract class OkHttpParser<T> {

    protected int code;

    @Nullable
    public abstract T parse(Response response);

    protected T parseResponse(Response response) {
        code = wrapperCode(response.code());
        return parse(response);
    }

    /**
     * 对返回码进行包装，可以自定义返回值
     *
     * @param code
     * @return
     */
    protected static int wrapperCode(int code) {
        if (code >= 500) {
            return OkHttpCallback.ERROR_SERVER;
        } else if (code >= 400) {
            return OkHttpCallback.ERROR_CLIENT;
        } else {
            return OkHttpCallback.SUCCESS;
        }
    }

    public int getCode() {
        return code;
    }

}
