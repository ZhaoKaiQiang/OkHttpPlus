package com.socks.sample.okhttp.plus;

import android.support.annotation.Nullable;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by zhaokaiqiang on 15/11/22.
 */
public class OkHttpTextParser extends OkHttpParser<String> {

    @Nullable
    @Override
    public String parse(Response response) {

        if (response.isSuccessful()) {
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }
}
