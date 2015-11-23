package com.socks.sample.okhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.socks.okhttp.plus.OkHttpClientManager;
import com.socks.okhttp.plus.request.OkHttpRequest;
import com.socks.sample.okhttp.callback.OkHttpResultCallback;
import com.socks.sample.okhttp.model.User;
import com.socks.sample.okhttp.plus.OkHttpCallback;
import com.socks.sample.okhttp.plus.OkHttpJsonParser;
import com.socks.sample.okhttp.plus.OkHttpProxy;
import com.socks.sample.okhttp.util.TestUrls;
import com.socks.sample.okhttp.util.Utils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okio.Buffer;

public class MainActivity extends AppCompatActivity implements TestUrls {

    private TextView tv_response;
    private TextView tv_header;
    private ActionBar actionBar;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_response = (TextView) findViewById(R.id.tv_response);
        tv_header = (TextView) findViewById(R.id.tv_header);
        pb = (ProgressBar) findViewById(R.id.pb);

        actionBar = getSupportActionBar();

        initHttpClient();
    }

    private void initHttpClient() {
        OkHttpClientManager.getInstance().setCertificates(new InputStream[]{
                new Buffer().writeUtf8(CER_12306).inputStream()});
        OkHttpClient httpClient = OkHttpClientManager.getInstance().getOkHttpClient();
        httpClient.setConnectTimeout(10 * 1000, TimeUnit.MILLISECONDS);
        httpClient.setWriteTimeout(30 * 1000, TimeUnit.MILLISECONDS);
        httpClient.setWriteTimeout(30 * 1000, TimeUnit.MILLISECONDS);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Test Code
    ///////////////////////////////////////////////////////////////////////////

    public void getUser(View view) {
        OkHttpProxy.get(URL_USER, new OkHttpCallback<User>(new OkHttpJsonParser<User>()) {
            @Override
            public void onSuccess(int code, User user) {
                tv_response.setText(user.toString());
            }

            @Override
            public void onFailure(int code, Throwable e) {
                tv_response.setText(e.getMessage());
            }
        });
    }

    public void getUsers(View view) {
        new OkHttpRequest.Builder().url(URL_USERS).get(new OkHttpResultCallback<List<User>>(actionBar) {
            @Override
            public void onError(Request request, Exception e) {
                tv_response.setText(e.getMessage());
            }

            @Override
            public void onResponse(Response response, List<User> data) {
                tv_response.setText(data.toString());
                tv_header.setText(Utils.getResponse(response));
            }
        });
    }

    public void getSimpleString(View view) {
        new OkHttpRequest.Builder().url(URL_USER)
                .get(new OkHttpResultCallback<String>(actionBar) {
                    @Override
                    public void onError(Request request, Exception e) {
                        tv_response.setText(e.getMessage());
                    }

                    @Override
                    public void onResponse(Response response, String data) {
                        tv_response.setText(data.toString());
                        tv_header.setText(Utils.getResponse(response));
                    }
                });
    }

    public void getHtml(View view) {
        new OkHttpRequest.Builder().url(URL_BAIDU).get(new OkHttpResultCallback<String>(actionBar) {
            @Override
            public void onError(Request request, Exception e) {
                tv_response.setText(e.getMessage());
            }

            @Override
            public void onResponse(Response response, String data) {
                tv_response.setText(data.toString());
                tv_header.setText(Utils.getResponse(response));
            }
        });
    }

    public void getHttpsHtml(View view) {
        new OkHttpRequest.Builder().url(URL_12306).get(new OkHttpResultCallback<String>(actionBar) {
            @Override
            public void onError(Request request, Exception e) {
                tv_response.setText(e.getMessage());
            }

            @Override
            public void onResponse(Response response, String data) {
                tv_response.setText(data.toString());
                tv_header.setText(Utils.getResponse(response));
            }
        });
    }

    public void uploadFile(View view) {

        File file = new File(Environment.getExternalStorageDirectory(), "jiandan02.jpg");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }

        //Token生成网址 http://jsfiddle.net/gh/get/extjs/4.2/icattlecoder/jsfiddle/tree/master/uptoken
        new OkHttpRequest.Builder()
                .url(URL_UPLOAD)
                .addParams("token", TOKEN)
                .files(new Pair<>("file", file))
                .upload(new OkHttpResultCallback<String>(actionBar) {
                    @Override
                    public void onError(Request request, Exception e) {
                        tv_response.setText(e.toString());
                    }

                    @Override
                    public void onResponse(Response response, String data) {
                        tv_response.setText(data);
                        if (response.isSuccessful()) {
                            tv_header.setText("上传成功！" + " code = " + response.code());
                        }
                    }

                    @Override
                    public void onProgress(float progress) {
                        KLog.d("progress = " + progress);
                        pb.setProgress((int) (progress * 100));
                        if (progress == 1) {
                            pb.setProgress(0);
                        }
                    }
                });
    }

    /**
     * 多文件上传需要服务器支持
     *
     * @param view
     */
    public void multiFileUpload(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        File file2 = new File(Environment.getExternalStorageDirectory(), "test1.txt");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }

        new OkHttpRequest.Builder()
                .url(URL_UPLOAD)
                .files(new Pair<>("File1", file), new Pair<>("File2", file2))
                .upload(new OkHttpResultCallback(actionBar) {
                    @Override
                    public void onError(Request request, Exception e) {
                        tv_response.setText(e.getMessage());
                    }

                    @Override
                    public void onResponse(Response response, Object data) {
                        tv_response.setText(data.toString());
                        tv_header.setText(Utils.getResponse(response));
                    }
                });
    }


    public void downloadFile(View view) {

        new OkHttpRequest.Builder()
                .url(URL_DOWMLOAD)
                .destFileDir(Environment.getExternalStorageDirectory().getAbsolutePath())
                .destFileName("gson-2.2.1.jar")
                .download(new OkHttpResultCallback<String>(actionBar) {
                    @Override
                    public void onError(Request request, Exception e) {
                        tv_response.setText(e.getMessage());
                    }

                    @Override
                    public void onResponse(Response response, String data) {
                        tv_response.setText(data.toString());
                        tv_header.setText("code = " + response.code());
                    }

                    @Override
                    public void onProgress(float progress) {
                        pb.setProgress((int) (progress * 100));
                        if (progress == 1) {
                            pb.setProgress(0);
                        }
                    }
                });
    }

}