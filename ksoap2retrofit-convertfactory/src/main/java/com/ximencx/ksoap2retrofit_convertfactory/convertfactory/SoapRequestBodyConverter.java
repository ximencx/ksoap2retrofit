package com.ximencx.ksoap2retrofit_convertfactory.convertfactory;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Copyright (c) 2016. 东华博育云有限公司 Inc. All rights reserved.
 * com.mr_sun.logindemo.convert
 * 作者：Created by sfq on 2016/12/2.
 * http://www.jianshu.com/p/6fc8c9081c64
 * 联系方式：
 * 功能描述：
 * 修改：无
 */
final class SoapRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private String TAG = getClass().getSimpleName();

    private static final MediaType MEDIA_TYPE = MediaType.parse("text/plain; charset=UTF-8");

    @Override public RequestBody convert(T value) throws IOException {
        Log.v(TAG,"RequestBody:"+value);
        return RequestBody.create(MEDIA_TYPE, (String)value);
    }
}
