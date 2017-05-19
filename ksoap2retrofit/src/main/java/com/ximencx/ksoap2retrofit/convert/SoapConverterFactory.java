package com.ximencx.ksoap2retrofit.convert;

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

public class SoapConverterFactory<T> extends Converter.Factory {


    public static SoapConverterFactory create() {
        return new SoapConverterFactory();
    }


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new SoapResponseBodyConverter<Type>(type);
    }


    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new SoapRequestBodyConverter<Type>();
    }
}
