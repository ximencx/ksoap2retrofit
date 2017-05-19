package com.expert.net_framework.api;

import com.expert.net_framework.bean.DownloadBaseDataBean;
import com.expert.net_framework.bean.DownloadPlanDataBean;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

/**
 * author：wangkezhi
 * date: 2017/5/19 10:42
 * email：454366460@qq.com
 * summary：pams4api
 */

public interface Pams4Api {

    @POST("/pcm/PatrolService")
    rx.Observable<DownloadBaseDataBean> downloadbasedata(@HeaderMap Map<String,String> headerMap, @Body String body);


    @POST("/pcm/PatrolService")
    rx.Observable<DownloadPlanDataBean> downloadplandata(@HeaderMap Map<String,String> headerMap, @Body String body);

}
