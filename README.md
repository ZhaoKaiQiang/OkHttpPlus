#OkHttpPlus

OkHttpPlus is a tool for OkHttp

---

OkHttpPlus是OkHttp的一个工具类，主要对Get和Post方法进行了简单封装，所有方法的回调都在UI线程完成，内置了String、JsonObject、JsonArray数据类型的解析器，封装了对小文件下载和文件上传功能，可以实现进度监听，使之满足常见的Http需求。

该项目暂时处于测试阶段，功能比较单一，__暂时不要应用到生产环境中__，请等待之后的正式版发布。

如果满足不了你的需求，请发issuse。

中文文档请戳[这里](http://blog.csdn.net/zhaokaiqiang1992)

##Features

- More simple use for GET and POST
- CallBack  run on the UI Thread
- Support small size file download and upload without other library
- Build in support for JSON Object and JSON Array parse

##Sample Usage

###Init OkHttpClient
First , you need init OkHttpClient at Application.onCreate() to set ConnectTimeout、ReadTimeout and  WriteTimeout.

```
public class OkApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
  	
        OkHttpProxy.init(10, 10, 10);
    }
}
```

###Get JSON Object And JSON Array

Use OkJsonParser<User> ，you could get a User Object form json format string.

```
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
```

Use OkJsonParser<List<User>> ，you could get a list of User Object form json format string.

```
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
```

###Custom  Parser 

By extends for OkJsonParser<T>, you could custom your parser as following 

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

###GET String

Get String by useing OkTextParser.

```
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
```

##JCenter

```
Auditing...
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