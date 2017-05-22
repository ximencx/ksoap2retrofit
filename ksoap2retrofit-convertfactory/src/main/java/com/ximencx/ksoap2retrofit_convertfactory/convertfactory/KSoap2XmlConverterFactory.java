package com.ximencx.ksoap2retrofit_convertfactory.convertfactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * author：wangkezhi
 * date: 2017/5/19 11:17
 * email：454366460@qq.com
 * summary：自定义转换器
 */

public class KSoap2XmlConverterFactory<T> extends Converter.Factory {


    public static KSoap2XmlConverterFactory create() {
        return new KSoap2XmlConverterFactory();
    }


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new Soap2XmlResponseBodyConverter<Type>(type);
    }


    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new SoapRequestBodyConverter<Type>();
    }
}
