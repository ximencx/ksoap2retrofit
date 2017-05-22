# ksoap2retrofit
It is can help you use okhttp3 or retrofit accsse webservice.
KSoap2RetrofitHelper can convert httprequest to ksoaprequest and convert ksoap result to normal data,don't include netframwork denpence. 
KSoap2JsonConverterFactory can help you that convert ksoap result to json when you  use retrofit.
KSoap2XmlConverterFactory can help you that convert ksoap result to simplexml when you  use retrofit.


## Base usage
#### first,you need convert ksoap header ,nameSpace,method，key-value to normal header and request body.
     /**
     * 转换ksoap请求头和请求体
     *
     * @param method     方法名
     * @param nameSpacre 命名空间
     * @param properties 键值对
     * @return 转换后的请求头和请求体
     */
     Requestksoap requestksoap = KSoap2RetrofitHelper.getInstance().convertRequest(method, nameSpacre, key-value);
	 
#### secend，you need conver ksoap response to normal data.
## when you don't use retrofit,the method can conver ksoap response to the String data response.
	 /**
     * 转换ksoap响应体
     *
     * @param value 响应体
     * @return 转换后的响应体
     * @throws IOException
     */
     String normalresponse=KSoap2RetrofitHelper.getInstance().convertRequest(ksoapresponse);
	 
#### 
## when you use retrofit,you can use convertfactor tool.
     KSoap2XmlConverterFactory is xml tool.
	 KSoap2JsonConverterFactory is json tool.



