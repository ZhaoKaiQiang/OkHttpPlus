package com.socks.okhttp.plus.request;

import android.util.Pair;

import com.socks.okhttp.plus.Utils;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.Map;

/**
 * Created by zhy on 15/11/6.
 *
 * @author zhaokaiqiang
 *         15/11/17 修改
 */
public class OkHttpUploadRequest extends OkHttpPostRequest {

    private Pair<String, File>[] files;

    protected OkHttpUploadRequest(String url, String tag, Map<String, String> params, Map<String, String> headers, Pair<String, File>[] files) {
        super(url, tag, params, headers, null, null, null);
        this.files = files;
    }

    @Override
    protected void validParams() {
        if (params == null && files == null) {
            throw new IllegalArgumentException("params and files can't both null in upload request .");
        }
    }

    @Override
    public RequestBody buildRequestBody() {
        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);
        addParams(builder, params);

        if (files != null) {
            RequestBody fileBody;
            for (int i = 0; i < files.length; i++) {
                Pair<String, File> filePair = files[i];
                String fileKeyName = filePair.first;
                File file = filePair.second;
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(Utils.guessMimeType(fileName)), file);
                builder.addPart(Headers.of("Content-Disposition",
                                "form-data; name=\"" + fileKeyName + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        return builder.build();
    }

    private void addParams(MultipartBuilder builder, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

}
