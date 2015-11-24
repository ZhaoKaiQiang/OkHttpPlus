package com.socks.sample.okhttp;

import android.app.Application;

import com.socks.okhttp.plus.OkHttpProxy;
import com.socks.sample.okhttp.util.MyHostnameVerifier;
import com.socks.sample.okhttp.util.MyTrustManager;
import com.squareup.okhttp.OkHttpClient;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Created by zhaokaiqiang on 15/11/23.
 */
public class OkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = OkHttpProxy.getInstance();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(15, TimeUnit.SECONDS);

        //ignore HTTPS Authentication
        okHttpClient.setHostnameVerifier(new MyHostnameVerifier());
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
            okHttpClient.setSslSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

}
