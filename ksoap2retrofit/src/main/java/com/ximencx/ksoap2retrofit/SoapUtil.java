package com.ximencx.ksoap2retrofit;

import com.ximencx.ksoap2retrofit.ksoap2.SoapEnvelope;
import com.ximencx.ksoap2retrofit.kxml2.io.KXmlParser;
import com.ximencx.ksoap2retrofit.kxml2.io.KXmlSerializer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;

/**
 * author：wangkezhi
 * date: 2017/5/22 14:37
 * email：454366460@qq.com
 * summary：工具类
 */

public class SoapUtil {
    protected static final String CONTENT_TYPE_XML_CHARSET_UTF_8 = "text/xml;charset=utf-8";
    protected static final String CONTENT_TYPE_SOAP_XML_CHARSET_UTF_8 = "application/soap+xml;charset=utf-8";
    protected static final String USER_AGENT = "ksoap2-android/2.6.0+";
    private static String xmlVersionTag = "";
    private static HashMap prefixes = new HashMap();
    public static final int DEFAULT_BUFFER_SIZE = 256 * 1024; // 256 Kb
    private static int bufferLength = DEFAULT_BUFFER_SIZE;

    /**
     * Serializes the request.
     */
    protected static byte[] createRequestData(SoapEnvelope envelope, String encoding)
            throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(bufferLength);
        byte result[] = null;
        bos.write(xmlVersionTag.getBytes());
        XmlSerializer xw = new KXmlSerializer();

        final Iterator keysIter = prefixes.keySet().iterator();

        xw.setOutput(bos, encoding);
        while (keysIter.hasNext()) {
            String key = (String) keysIter.next();
            xw.setPrefix(key, (String) prefixes.get(key));
        }
        envelope.write(xw);
        xw.flush();
        bos.write('\r');
        bos.write('\n');
        bos.flush();
        result = bos.toByteArray();
        xw = null;
        bos = null;
        return result;
    }


    /**
     * Sets up the parsing to hand over to the envelope to deserialize.
     */
    public static void parseResponse(SoapEnvelope envelope, InputStream is) { //throws XmlPullParserException, IOException
        try {
            XmlPullParser xp = new KXmlParser();
            xp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            xp.setInput(is, null);
            envelope.parse(xp);
        /*
         * Fix memory leak when running on android in strict mode. Issue 133
         */
            is.close();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException m) {
            m.printStackTrace();
        }

    }
}
