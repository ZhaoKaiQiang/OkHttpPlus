#OkHttpPlus

OkHttpPlus is a tool for OkHttp

---

OkHttpPlus是OkHttp的一个工具类，主要对Get和Post方法进行了简单封装，所有方法的回调都在UI线程完成，内置了String、JsonObject、JsonArray数据类型的解析器，封装了对小文件下载和文件上传功能，可以实现进度监听，使之满足常见的Http需求。

如果满足不了你的需求，请发issuse。

中文文档请戳[这里](http://blog.csdn.net/zhaokaiqiang1992/article/details/50016815)

##Features

- More simple use for GET and POST
- CallBack  run on the UI Thread
- Support small size file download and upload without other library
- Build in support for JSON Object and JSON Array parse

##Update

- Add sync method,enqueue is Async Method，execute is Sync Method

##Sample Usage

###Init OkHttpClient
First , you can init the OkHttpClient as usual

```
public class OkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = OkHttpProxy.getInstance();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        okHttpClient.setWriteTimeout(15, TimeUnit.SECONDS);    }
}
```

###Get JSON Object And JSON Array

Use OkJsonParser<User> ，you could get a User Object form json format string.

```
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
```

Use OkJsonParser<List<User>> ，you could get a list of User Object form json format string.

```
  OkHttpProxy.get()
                .url(URL_USER)
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
```

###Custom  Parser 

By extends for OkJsonParser<T>, you could custom your parser as following .

```
public class JokeParser<T> extends OkJsonParser<T> {

    @Nullable
    @Override
    public T parse(Response response) throws IOException {
        String jsonStr = response.body().string();
        try {
            jsonStr = new JSONObject(jsonStr).getJSONArray("comments").toString();
            return mGson.fromJson(jsonStr, new TypeToken<ArrayList<Joke>>() {
            }.getType());
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

```

Then you can use as above.

```
OkHttpProxy.get()
                .url(Joke.getRequestUrl(1))
                .tag(this).enqueue(new OkCallback<List<Joke>>(new JokeParser()) {
            @Override
            public void onSuccess(int code, List<Joke> jokes) {
                tv_response.setText(jokes.toString());
            }

            @Override
            public void onFailure(Throwable e) {
                tv_response.setText(e.getMessage());
            }
        });
```

###GET String

Get String by useing OkTextParser.

```
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
```

###Post

Post is also very easy.

```
OkHttpProxy.post()
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
```

###Upload

```
/**
     *  Validity period if Token is12 hours， if the Token Invalid，please create it by follow website
     * Token website = http://jsfiddle.net/gh/get/extjs/4.2/icattlecoder/jsfiddle/tree/master/uptoken
     * AK = IUy4JnOZHP6o-rx9QsGLf9jMTAKfRkL07gNssIDA
     * CK = DkfA7gPTNy1k4HWnQynra3qAZhrzp-wmSs15vub6
     * BUCKE_NAME = zhaokaiqiang
     */
    public void uploadFile(View view) {

        File file = new File(Environment.getExternalStorageDirectory(), "jiandan02.jpg");
        if (!file.exists()) {
            Toast.makeText(MainActivity.this, "File not exits！", Toast.LENGTH_SHORT).show();
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
```

###Download

```
 String desFileDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        OkHttpProxy.download(URL_DOWMLOAD, new DownloadListener(desFileDir, "json.jar") {

            @Override
            public void onUIProgress(Progress progress) {
                // if the content length is unknow, the progress.getTotalBytes() = -1
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
```

##JCenter

```
compile 'com.github.zhaokaiqiang.okhttpplus:library:1.0.0'
```


##Inspiration

- [okhttp-utils](https://github.com/hongyangAndroid/okhttp-utils)
- [CoreProgres](https://github.com/lizhangqu/CoreProgress)

##About me

- [Android Developer](http://weibo.com/zhaokaiqiang1992)
- [CSDN Blog](http://blog.csdn.net/zhaokaiqiang1992)

##License

```
Copyright 2015, 2016 ZhaoKaiQiang

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
