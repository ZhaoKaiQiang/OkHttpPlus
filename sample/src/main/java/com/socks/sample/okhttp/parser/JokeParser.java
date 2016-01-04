package com.socks.sample.okhttp.parser;

import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.socks.okhttp.plus.parser.OkJsonParser;
import com.socks.sample.okhttp.model.Joke;
import okhttp3.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zhaokaiqiang on 15/11/23.
 */
public class JokeParser<T> extends OkJsonParser<T> {

    @Nullable
    @Override
    public T parse(Response response) throws IOException {
        String jsonStr = response.body().string();
        try {
            jsonStr = new JSONObject(jsonStr).getJSONArray("comments").toString();
            return mGson.fromJson(jsonStr, new TypeToken<ArrayList<Joke>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
