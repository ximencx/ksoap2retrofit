package com.expert.net_framework;

import com.ximencx.ksoap2retrofit_convertfactory.convertfactory.KSoap2XmlConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * author：wangkezhi
 * date: 2017/5/19 10:28
 * email：454366460@qq.com
 * retrofit管理类
 */

public class RetrofitManager {

    private static RetrofitManager mRetrofitManager;

    private Retrofit mRetrofit;

    private RetrofitManager() {
        //initRetrofit();
    }

    protected static RetrofitManager getInstance() {
        if (mRetrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (mRetrofitManager == null) {
                    mRetrofitManager = new RetrofitManager();
                }
            }

        }
        return mRetrofitManager;
    }

    /**
     * 初始化配置
     *
     * @param manager
     * @return
     */
    protected RetrofitManager init(ConfigManager manager) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
      /*  if (manager.DEBUG) {
            HttpLoggingInterceptor LoginInterceptor = new HttpLoggingInterceptor();
            LoginInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(LoginInterceptor);
        }
*/
        builder.connectTimeout(manager.Time_Out, TimeUnit.SECONDS);
        builder.readTimeout(manager.Time_Out, TimeUnit.SECONDS);
        builder.writeTimeout(manager.Time_Out, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        OkHttpClient client = builder.build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(manager.BASE_URL)
                .addConverterFactory(KSoap2XmlConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        return this;
    }

    /**
     * 获得retrofit接口实例
     *
     * @param reqServer
     * @param <T>
     * @return
     */
    protected <T> T createReq(Class<T> reqServer) {
        return mRetrofit.create(reqServer);
    }
}
