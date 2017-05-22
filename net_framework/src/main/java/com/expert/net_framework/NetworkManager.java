package com.expert.net_framework;

import com.expert.net_framework.api.Pams4Api;
import com.expert.net_framework.bean.DownloadBaseDataBean;
import com.expert.net_framework.bean.DownloadPlanDataBean;
import com.ximencx.ksoap2retrofit.Requestksoap;
import com.ximencx.ksoap2retrofit.KSoap2RetrofitHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * author：wangkezhi
 * date: 2017/5/19 14:32
 * email：454366460@qq.com
 * summary：
 */

public class NetworkManager {

    private static NetworkManager networkManager;
    private final RetrofitManager manager;
    private String TAG = getClass().getSimpleName();

    private NetworkManager() {
        ConfigManager configManager = new ConfigManager.Builder().setBASE_URL(ConstantManager.baseurl).build();
        manager = RetrofitManager.getInstance().init(configManager);
    }

    public static NetworkManager getInstance() {
        if (networkManager == null) {
            synchronized (NetworkManager.class) {
                if (networkManager == null) {
                    networkManager = new NetworkManager();
                }
            }

        }
        return networkManager;
    }

    /**
     * 获取pams4api
     *
     * @return
     */
    private Pams4Api getPams4Api() {
        return manager.createReq(Pams4Api.class);
    }

    /**
     * 下载基础数据
     *
     * @param username
     * @param pwd
     * @return
     */
    public rx.Observable<DownloadBaseDataBean> Downloadbasicdata(String username, String pwd) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("loginName", username);
        properties.put("password", pwd);
        properties.put("code", "");
        Requestksoap requestksoap = KSoap2RetrofitHelper.getInstance().convertRequest(ConstantManager.downloadBasicData, ConstantManager.nameSpace, properties);
        Map<String, String> soapHeaderMap = null;
        String mBody = null;
        if (requestksoap != null) {
            soapHeaderMap = requestksoap.getMheaderMap();
            mBody = new String(requestksoap.getRequestData());
        }
        return getPams4Api().downloadbasedata(soapHeaderMap, mBody);
    }

    /**
     * 下载路线数据
     *
     * @param username
     * @param pwd
     * @return
     */
    public rx.Observable<DownloadPlanDataBean> Downloadplandata(String username, String pwd) {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("userName", username);
        properties.put("password", pwd);
        //properties.put("code", "");
        Requestksoap requestksoap = KSoap2RetrofitHelper.getInstance().convertRequest(ConstantManager.downloadPatrolPlan, ConstantManager.nameSpace, properties);
        Map<String, String> soapHeaderMap = null;
        String mBody = null;
        if (requestksoap != null) {
            soapHeaderMap = requestksoap.getMheaderMap();
            mBody = new String(requestksoap.getRequestData());
        }
        return getPams4Api().downloadplandata(soapHeaderMap, mBody);
    }

}
