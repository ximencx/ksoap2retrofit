# ksoap2retrofit简介
这是一个ksoap协议转换库。通过转换ksop协议请求头和请求体，从而直接使用okhttp或者retrofit访问Webservice，得到的响应头经过转换即可得到响应体。优点：避免使用ksoap框架及更好的生命周期处理，内存泄漏处理。
<br/> **KSoap2RetrofitHelper** 包含请求头和请求体转换类，响应体转换类。不涉及网络部分，使用字符串转换。
<br/> **KSoap2JsonConverterFactory** 如果使用retrofit，且响应体数据是json格式，用法和gson类似。
<br/> **KSoap2XmlConverterFactory** 如果使用retrofit，且响应体数据是xml格式，用法和simplexm类似。

## 基本使用方法
### 第一步，转换ksoap方法名，命名空间，键值对 为请求头和请求体。
```
/**
* 转换ksoap请求头和请求体
*
* @param method     方法名
* @param nameSpacre 命名空间
* @param properties 键值对
* @return 转换后的请求头和请求体
*/
Requestksoap requestksoap = KSoap2RetrofitHelper.getInstance().convertRequest(method, nameSpacre, key-value);
```
	 
### 第二步，转换ksoap响应体 为包含的响应体。
* 如果没有使用retrofit，该方法可以将ksoap响应体转换为包含的响应体。
```java
/**
* 转换ksoap响应体
*
* @param value 响应体
* @return 转换后的响应体
* @throws IOException
*/
String normalresponse=KSoap2RetrofitHelper.getInstance().convertResponse(ksoapresponse);
```	 
#### 
* 如果使用retrofit，下面2个工具类可以将ksoap响应体解析为xml和json模型。
```java
<br/> **KSoap2XmlConverterFactory** 为ksoap转xml转换类。
<br/> **KSoap2JsonConverterFactory** 为ksoap转json转换类。
```



