package com.socks.sample.okhttp;

import android.app.Application;

import com.socks.okhttp.plus.OkHttpProxy;
import com.socks.sample.okhttp.util.MyHostnameVerifier;
import com.socks.sample.okhttp.util.MyTrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by zhaokaiqiang on 15/11/23.
 */
public class OkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient.Builder builder = OkHttpProxy.getInstance().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS);

        //ignore HTTPS Authentication
        builder.hostnameVerifier(new MyHostnameVerifier());
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
            builder.sslSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        OkHttpProxy.setInstance(builder.build());
    }

}
