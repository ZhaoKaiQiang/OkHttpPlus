package com.socks.sample.okhttp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.okhttp.plus.OkHttpClientManager;
import com.socks.okhttp.plus.request.OkHttpRequest;
import com.socks.sample.okhttp.callback.OkHttpResultCallback;
import com.socks.sample.okhttp.model.User;
import com.socks.sample.okhttp.util.TestUrls;
import com.socks.sample.okhttp.util.Utils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okio.Buffer;

public class MainActivity extends AppCompatActivity implements TestUrls {

    private TextView tv_response;
    private TextView tv_header;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_response = (TextView) findViewById(R.id.tv_response);
        tv_header = (TextView) findViewById(R.id.tv_header);

        actionBar = getSupportActionBar();

        initHttpClient();
    }

    private void initHttpClient() {
        OkHttpClientManager.getInstance().setCertificates(new InputStream[]{
                new Buffer().writeUtf8(CER_12306).inputStream()});
        OkHttpClientManager.getInstance().getOkHttpClient().setConnectTimeout(10 * 1000, TimeUnit.MILLISECONDS);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Test Code
    ///////////////////////////////////////////////////////////////////////////

    public void getUser(View view) {
        new OkHttpRequest.Builder().url(URL_USER).get(new OkHttpResultCallback<User>(actionBar) {
            @Override
            public void onError(Request request, Exception e) {
                tv_response.setText(e.getMessage());
            }

            @Override
            public void onResponse(Response response, User data) {
                tv_response.setText(data.toString());
                tv_header.setText(Utils.getResponse(response));
            }
        });
    }

    public void getUsers(View view) {
        new OkHttpRequest.Builder().url(URL_USERS).post(new OkHttpResultCallback<List<User>>(actionBar) {
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

        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "name");
        params.put("password", "123");

        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");

        String url = "http://192.168.56.1:8080/okHttpServer/fileUpload";
        new OkHttpRequest.Builder()
                .url(url)
                .params(params)
                .headers(headers)
                .files(new Pair<>("mFile", file))
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


    public void multiFileUpload(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        File file2 = new File(Environment.getExternalStorageDirectory(), "test1.txt");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "name");
        params.put("password", "123");

        String url = "http://192.168.1.103:8080/okHttpServer/mulFileUpload";
        new OkHttpRequest.Builder()
                .url(url)
                .params(params)
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
        String url = "https://github.com/hongyangAndroid/okhttp-utils/blob/master/gson-2.2.1.jar?raw=true";
        new OkHttpRequest.Builder()
                .url(url)
                .destFileDir(Environment.getExternalStorageDirectory().getAbsolutePath())
                .destFileName("gson-2.2.1.jar")
                .download(new OkHttpResultCallback(actionBar) {
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

}