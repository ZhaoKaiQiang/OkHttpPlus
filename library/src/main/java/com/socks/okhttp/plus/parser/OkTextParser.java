package com.socks.okhttp.plus.parser;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by zhaokaiqiang on 15/11/22.
 */
public class OkTextParser extends OkBaseParser<String> {

    @Override
    public String parse(Response response) throws IOException {

        if (response.isSuccessful()) {
            return response.body().string();
        }

        return null;
    }
}
