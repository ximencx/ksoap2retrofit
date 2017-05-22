package com.ximencx.ksoap2retrofit;

import java.util.HashMap;
import java.util.Map;

/**
 * author：wangkezhi
 * date: 2017/5/22 11:51
 * email：454366460@qq.com
 * summary：ksoap请求模型
 */

public class Requestksoap {

    /**
     * 请求头
     */
    private Map<String, String> mheaderMap;

    /**
     * 请求数据
     */
    private byte[] requestData;

    public Map<String, String> getMheaderMap() {
        return mheaderMap;
    }

    public void setMheaderMap(Map<String, String> mheaderMap) {
        this.mheaderMap = mheaderMap;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public void setRequestData(byte[] requestData) {
        this.requestData = requestData;
    }

    public Requestksoap() {
    }

    public Requestksoap(Map<String, String> mheaderMap, byte[] requestData) {
        this.mheaderMap = mheaderMap;
        this.requestData = requestData;
    }
}
