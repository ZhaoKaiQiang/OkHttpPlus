package com.socks.sample.okhttp;

import android.app.Application;

import com.socks.okhttp.plus.OkHttpProxy;
import com.socks.sample.okhttp.util.MyHostnameVerifier;
import com.socks.sample.okhttp.util.MyTrustManager;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Created by zhaokaiqiang on 15/11/23.
 */
public class OkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpProxy.init(10, 10, 10);

        //设置忽略HTTPS认证
        OkHttpProxy.getInstance().setHostnameVerifier(new MyHostnameVerifier());
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
            OkHttpProxy.getInstance().setSslSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

    }


}
