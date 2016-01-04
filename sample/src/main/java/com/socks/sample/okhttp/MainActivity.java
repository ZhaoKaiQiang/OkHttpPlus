package com.socks.sample.okhttp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

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
        OkHttpProxy.get()
                .url(URL_USER)
                .tag(this)
                .enqueue(new OkCallback<User>(new OkJsonParser<User>() {
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

    public void getUserSync(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = OkHttpProxy.get()
                            .url(URL_USER)
                            .tag(this)
                            .execute();

                    final User user = new OkJsonParser<User>() {
                    }.parse(response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_response.setText(user.toString());
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getUsers(View view) {
        OkHttpProxy.get()
                .url(URL_USERS)
                .tag(this)
                .enqueue(new OkCallback<List<User>>(new OkJsonParser<List<User>>() {
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
        OkHttpProxy.get()
                .url(Joke.getRequestUrl(1))
                .tag(this).enqueue(new OkCallback<List<Joke>>(new JokeParser<List<Joke>>()) {
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
        OkHttpProxy.get()
                .url(URL_BAIDU)
                .tag(this)
                .enqueue(new OkCallback<String>(new OkTextParser()) {
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
        OkHttpProxy.get()
                .url(URL_12306)
                .tag(this)
                .enqueue(new OkCallback<String>(new OkTextParser()) {
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

    public void postUsers(View view) {

        OkHttpProxy
                .post()
                .url(URL_USERS)
                .tag(this)
                .addParams("name", "zhaokaiqiang")
                .addHeader("header", "okhttp")
                .enqueue(new OkCallback<ArrayList<User>>(new OkJsonParser<ArrayList<User>>() {
                }) {
                    @Override
                    public void onSuccess(int code, ArrayList<User> users) {
                        tv_response.setText(users.toString());
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

        OkHttpProxy
                .upload()
                .url(URL_UPLOAD)
                .file(pair)
                .setParams(param)
                .setWriteTimeOut(20)
                .start(new UploadListener() {
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
                        int pro = (int) ((progress.getCurrentBytes() + 0.0) / progress.getTotalBytes() * 100);
                        if (pro > 0) {
                            pb.setProgress(pro);
                        }
                        KLog.d("pro = " + pro + " getCurrentBytes = " + progress.getCurrentBytes() + " getTotalBytes = " + progress.getTotalBytes());
                    }
                });
    }

    public void downloadFile(View view) {

        String desFileDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        OkHttpProxy.download(URL_DOWMLOAD, new DownloadListener(desFileDir, "json.jar") {

            @Override
            public void onUIProgress(Progress progress) {
                //当下载资源长度不可知时，progress.getTotalBytes()为-1，此时不能显示下载进度
                int pro = (int) (progress.getCurrentBytes() / progress.getTotalBytes() * 100);
                if (pro > 0) {
                    pb.setProgress(pro);
                }
                KLog.d("pro = " + pro + " getCurrentBytes = " + progress.getCurrentBytes() + " getTotalBytes = " + progress.getTotalBytes());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpProxy.cancel(this);
    }

    ///////////////////////////////////////////////////////////////////////////
    // MENU
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_github:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ZhaoKaiQiang/OkHttpPlus")));
                break;
            case R.id.action_csdn:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.csdn.net/zhaokaiqiang1992")));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}