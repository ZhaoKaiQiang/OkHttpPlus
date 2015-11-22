package com.socks.okhttp.plus;

import java.net.FileNameMap;
import java.net.URLConnection;

/**
 * Created by zhaokaiqiang on 15/11/17.
 */
public class Utils {

    public static String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}
