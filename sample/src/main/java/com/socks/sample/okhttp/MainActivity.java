package com.socks.sample.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.socks.library.KLog;
import com.socks.okhttp.plus.OkHttpProxy;
import com.socks.okhttp.plus.callback.OkCallback;
import com.socks.okhttp.plus.parser.OkJsonParser;
import com.socks.okhttp.plus.parser.OkTextParser;
import com.socks.sample.okhttp.model.Joke;
import com.socks.sample.okhttp.model.User;
import com.socks.sample.okhttp.parser.JokeParser;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TestUrls {

    private TextView tv_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_response = (TextView) findViewById(R.id.tv_response);
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
                KLog.e(e);
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
                KLog.e(e);
            }
        });
    }

    public void uploadFile(View view) {

//        File file = new File(Environment.getExternalStorageDirectory(), "jiandan02.jpg");
//        if (!file.exists()) {
//            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        //Token生成网址 http://jsfiddle.net/gh/get/extjs/4.2/icattlecoder/jsfiddle/tree/master/uptoken
//        new OkHttpRequest.Builder()
//                .url(URL_UPLOAD)
//                .addParams("token", TOKEN)
//                .files(new Pair<>("file", file))
//                .upload(new OkHttpResultCallback<String>(actionBar) {
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        tv_response.setText(e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(Response response, String data) {
//                        tv_response.setText(data);
//                        if (response.isSuccessful()) {
//                            tv_header.setText("上传成功！" + " code = " + response.code());
//                        }
//                    }
//
//                    @Override
//                    public void onProgress(float progress) {
//                        KLog.d("progress = " + progress);
//                        pb.setProgress((int) (progress * 100));
//                        if (progress == 1) {
//                            pb.setProgress(0);
//                        }
//                    }
//                });
    }

    public void downloadFile(View view) {

//        new OkHttpRequest.Builder()
//                .url(URL_DOWMLOAD)
//                .destFileDir(Environment.getExternalStorageDirectory().getAbsolutePath())
//                .destFileName("gson-2.2.1.jar")
//                .download(new OkHttpResultCallback<String>(actionBar) {
//                    @Override
//                    public void onError(Request request, Exception e) {
//                        tv_response.setText(e.getMessage());
//                    }
//
//                    @Override
//                    public void onResponse(Response response, String data) {
//                        tv_response.setText(data.toString());
//                        tv_header.setText("code = " + response.code());
//                    }
//
//                    @Override
//                    public void onProgress(float progress) {
//                        pb.setProgress((int) (progress * 100));
//                        if (progress == 1) {
//                            pb.setProgress(0);
//                        }
//                    }
//                });
    }

}