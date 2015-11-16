package com.socks.sample.okhttp.util;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by zhaokaiqiang on 15/11/16.
 */
public class Utils {

    public static String getResponse(Response response) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append("code = ").append(response.code()).append("\n").append("body = ").append(response.body().string()).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        stringBuilder.append("headers = ").append(response.headers());
        return stringBuilder.toString();
    }
}
