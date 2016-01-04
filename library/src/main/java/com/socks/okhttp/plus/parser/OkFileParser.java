package com.socks.okhttp.plus.parser;

import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhaokaiqiang on 15/11/23.
 */
public class OkFileParser extends OkBaseParser<File> {

    private String mDestFileDir;
    private String mDestFileName;

    public OkFileParser(String destFileDir, String destFileName) {
        mDestFileDir = destFileDir;
        mDestFileName = destFileName;
    }

    @Override
    protected File parse(Response response) throws IOException {
        return saveFile(response);
    }


    public File saveFile(Response response) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File dir = new File(mDestFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, mDestFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return file;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }
}
