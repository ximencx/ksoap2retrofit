package com.ximencx.ksoap2retrofit;


import android.util.Log;

import com.ximencx.ksoap2retrofit.ksoap2.SoapEnvelope;
import com.ximencx.ksoap2retrofit.ksoap2.serialization.MarshalBase64;
import com.ximencx.ksoap2retrofit.ksoap2.serialization.SoapObject;
import com.ximencx.ksoap2retrofit.ksoap2.serialization.SoapSerializationEnvelope;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 帮助类：转换请求头，请求体。
 */
public class KSoap2RetrofitHelper {

    private String TAG = getClass().getSimpleName();

    private static KSoap2RetrofitHelper kSoap2RetrofitHelper;

    /**
     * 协议版本，默认110
     */
    private int envelopeversion = SoapEnvelope.VER11;

    /**
     * 设置版本号
     *
     * @param envelopeversion
     * @return
     */
    public KSoap2RetrofitHelper setEnvelopeversion(int envelopeversion) {
        this.envelopeversion = envelopeversion;
        return this;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public int getEnvelopeversion() {
        return envelopeversion;
    }

    /**
     * 获取对象
     *
     * @return 单例对象
     */
    public static KSoap2RetrofitHelper getInstance() {
        if (kSoap2RetrofitHelper == null) {
            synchronized (KSoap2RetrofitHelper.class) {
                if (kSoap2RetrofitHelper == null) {
                    kSoap2RetrofitHelper = new KSoap2RetrofitHelper();
                }
            }

        }
        return kSoap2RetrofitHelper;
    }


    /**
     * 转换ksoap请求头和请求体
     *
     * @param method     方法名
     * @param nameSpacre 命名空间
     * @param properties 键值对
     * @return 转换后的请求头和请求体
     */
    public Requestksoap convertRequest(String method, String nameSpacre, HashMap<String, Object> properties) {
        //throws HttpResponseException, IOException, XmlPullParserException
        SoapObject rpc = new SoapObject(nameSpacre, method);
        if (properties != null) {
            for (Iterator<Map.Entry<String, Object>> it = properties.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> entry = it.next();
                rpc.addProperty(entry.getKey(), entry.getValue());
            }
        }
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(envelopeversion);
        new MarshalBase64().register(envelope);
        envelope.bodyOut = rpc;
        envelope.dotNet = false;
        envelope.setOutputSoapObject(rpc);

        /***************** driver *******************/
        Map<String, String> mheaderMap = new HashMap<>();
        mheaderMap.put("User-Agent", SoapUtil.USER_AGENT);

        if (envelope.version != SoapSerializationEnvelope.VER12) {
            mheaderMap.put("SOAPAction", "");
        }

        if (envelope.version == SoapSerializationEnvelope.VER12) {
            mheaderMap.put("Content-Type", SoapUtil.CONTENT_TYPE_SOAP_XML_CHARSET_UTF_8);
        } else {
            mheaderMap.put("Content-Type", SoapUtil.CONTENT_TYPE_XML_CHARSET_UTF_8);
        }

        mheaderMap.put("Accept-Encoding", "gzip");
        byte[] requestData;
        try {
            requestData = SoapUtil.createRequestData(envelope, "UTF-8");
        } catch (IOException e) {
            requestData = new byte[0];
        }
        mheaderMap.put("Content-Length", "" + requestData.length);
        Log.d(TAG, "mheaderMap===" + mheaderMap.toString() + "\nrequestData===" + new String((byte[]) requestData));
        return new Requestksoap(mheaderMap, requestData);

    }


    /**
     * 转换ksoap响应体
     *
     * @param value 响应体
     * @return 转换后的响应体
     * @throws IOException
     */
    public String convertResponse(String value) throws IOException {
        //value 参考soap的返回，截取其中的字符串json
        SoapEnvelope soapEnvelope = new SoapSerializationEnvelope(KSoap2RetrofitHelper.getInstance().getEnvelopeversion());
        InputStream inputStream= new ByteArrayInputStream(value.getBytes("UTF-8"));
        SoapUtil.parseResponse(soapEnvelope,inputStream);
        SoapObject resultsRequestSOAP = (SoapObject) soapEnvelope.bodyIn;
        Object obj = resultsRequestSOAP.getProperty(0);
        Log.e(TAG, "ResponseBodyToString : " + obj.toString());
        try {
            return obj.toString();
            //return adapter.fromJson(obj.toString());
            //return (T) new Persister().read(clazz, obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            inputStream.close();
        }
    }
}
