#OkHttpPlus

OkHttpPlus is a tool for OkHttp

---

OkHttpPlus是OkHttp的一个工具类，主要对Get和Post方法进行了简单封装，使之满足常见的Http需求。

OkHttpPlus采用泛型和策略设计模式，内置了String、JsonObject、JsonArray数据类型的解析器，默认使用Gson作为解析库。

OkHttpPlus具有良好的扩展性，你可以自定义自己的解析器，对网络数据进行解析后返回，这样可以更好的遵守“单一职责原则”，减少网络请求的代码体积。

OkHttpPlus还封装了对小文件下载和文件上传功能，可以实现进度监听，不需要第三方类库，即可满足项目中的小文件下载和单文件上传需求。

##Features

##Sample Usage

##JCenter

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

##About me
- [Android Developer](http://weibo.com/zhaokaiqiang1992)
- [CSDN Blog](http://blog.csdn.net/zhaokaiqiang1992)