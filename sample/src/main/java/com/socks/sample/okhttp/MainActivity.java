package com.socks.sample.okhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.socks.okhttp.plus.OkHttpProxy;
import com.socks.okhttp.plus.callback.OkCallback;
import com.socks.okhttp.plus.listener.DownloadListener;
import com.socks.okhttp.plus.listener.UploadListener;
import com.socks.okhttp.plus.model.Progress;
import com.socks.okhttp.plus.parser.OkJsonParser;
import com.socks.okhttp.plus.parser.OkTextParser;
import com.socks.sample.okhttp.model.Joke;
import com.socks.sample.okhttp.model.User;
import com.socks.sample.okhttp.parser.JokeParser;
import com.socks.sample.okhttp.util.TestUrls;
import com.squareup.okhttp.Response;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements TestUrls {

    private TextView tv_response;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_response = (TextView) findViewById(R.id.tv_response);
        pb = (ProgressBar) findViewById(R.id.pb);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Test Code
    ///////////////////////////////////////////////////////////////////////////

    public void getUser(View view) {
        OkHttpProxy.get(URL_USER, new OkCallback<User>(new OkJsonParser<User>() {
        }) {
            @Override
            public void onSuccess(int code, User user) {
                tv_response.setText(user.toString());
            }

            @Override
            public void onFailure(Throwable e) {
                tv_response.setText(e.getMessage());
            }
        });
    }

    public void getUsers(View view) {
        OkHttpProxy.get(URL_USERS, new OkCallback<List<User>>(new OkJsonParser<List<User>>() {
        }) {
            @Override
            public void onSuccess(int code, List<User> users) {
                tv_response.setText(users.toString());
            }

            @Override
            public void onFailure(Throwable e) {
                tv_response.setText(e.getMessage());
            }
        });
    }

    public void getJokes(View view) {
        OkHttpProxy.get(Joke.getRequestUrl(1), new OkCallback<List<Joke>>(new JokeParser()) {
            @Override
            public void onSuccess(int code, List<Joke> jokes) {
                tv_response.setText(jokes.toString());
            }

            @Override
            public void onFailure(Throwable e) {
                tv_response.setText(e.getMessage());
            }
        });
    }

    public void getHtml(View view) {
        OkHttpProxy.get(URL_BAIDU, new OkCallback<String>(new OkTextParser()) {
            @Override
            public void onSuccess(int code, String s) {
                tv_response.setText(s);
            }

            @Override
            public void onFailure(Throwable e) {
                tv_response.setText(e.getMessage());
            }
        });
    }

    public void getHttpsHtml(View view) {
        OkHttpProxy.get(URL_12306, new OkCallback<String>(new OkTextParser()) {
            @Override
            public void onSuccess(int code, String s) {
                tv_response.setText(s);
            }

            @Override
            public void onFailure(Throwable e) {
                tv_response.setText(e.getMessage());
            }
        });
    }

    /**
     * 采用七牛上传接口，Token有效期为12小时，若Token无效，请在下面自行获取
     * Token生成网址 http://jsfiddle.net/gh/get/extjs/4.2/icattlecoder/jsfiddle/tree/master/uptoken
     * <p/>
     * AK = IUy4JnOZHP6o-rx9QsGLf9jMTAKfRkL07gNssIDA
     * CK = DkfA7gPTNy1k4HWnQynra3qAZhrzp-wmSs15vub6
     * BUCKE_NAME = zhaokaiqiang
     */
    public void uploadFile(View view) {

        File file = new File(Environment.getExternalStorageDirectory(), "jiandan02.jpg");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put("token", TOKEN);
        Pair<String, File> pair = new Pair("file", file);

        OkHttpProxy.upload(URL_UPLOAD, pair, param, new UploadListener() {

            @Override
            public void onSuccess(Response response) {
                tv_response.setText("isSuccessful = " + response.isSuccessful() + "\n" + "code = " + response.code());
            }

            @Override
            public void onFailure(Exception e) {
                tv_response.setText(e.getMessage());
            }

            @Override
            public void onUIProgress(Progress progress) {

                int pro = (int) ((progress.getCurrentBytes() + 0.0) / progress.getContentLength() * 100);
                if (pro > 0) {
                    pb.setProgress(pro);
                }
                KLog.d("pro = " + pro + " getCurrentBytes = " + progress.getCurrentBytes() + " getContentLength = " + progress.getContentLength());
            }
        });

    }

    public void downloadFile(View view) {

        String desFileDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        OkHttpProxy.download(URL_DOWMLOAD, new DownloadListener(desFileDir, "json.jar") {

            @Override
            public void onUIProgress(Progress progress) {
                //当下载资源长度不可知时，progress.getContentLength()为-1，此时不能显示下载进度
                KLog.d("getCurrentBytes = " + progress.getCurrentBytes() + " getContentLength = " + progress.getContentLength());

                int pro = (int) (progress.getCurrentBytes() / progress.getContentLength() * 100);

                if (pro > 0) {
                    pb.setProgress(pro);
                }
            }

            @Override
            public void onSuccess(File file) {
                tv_response.setText(file.getAbsolutePath());
            }

            @Override
            public void onFailure(Exception e) {
                tv_response.setText(e.getMessage());
            }
        });
    }

}