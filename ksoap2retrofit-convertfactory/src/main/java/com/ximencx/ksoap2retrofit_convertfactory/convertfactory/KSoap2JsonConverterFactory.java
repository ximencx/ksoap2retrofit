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

public class KSoap2JsonConverterFactory<T> extends Converter.Factory {
    private String TAG = getClass().getSimpleName();
    public static KSoap2JsonConverterFactory create() {
        return new KSoap2JsonConverterFactory();
    }


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {
        return new Soap2JsonResponseBodyConverter<Type>(type);
    }


    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new SoapRequestBodyConverter<Type>();
    }
}
