package com.ximencx.ksoap2retrofit.ksoap2.transport;


import  com.ximencx.ksoap2retrofit.ksoap2.HeaderProperty;
import  com.ximencx.ksoap2retrofit.ksoap2.SoapEnvelope;
import  com.ximencx.ksoap2retrofit.ksoap2.serialization.MarshalBase64;
import  com.ximencx.ksoap2retrofit.ksoap2.serialization.SoapObject;
import  com.ximencx.ksoap2retrofit.ksoap2.serialization.SoapSerializationEnvelope;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by gx on 2016/11/29.
 * A J2SE based HttpTransport layer.
 */
public class SoapHelper extends Transport {


    private static SoapHelper mhttpTransportBY;

    private SoapSerializationEnvelope envelope;

    public static SoapHelper getInstance() {
        if (mhttpTransportBY == null) {
            mhttpTransportBY = new SoapHelper(null);
        }
        return mhttpTransportBY;
    }

    /**
     * Creates instance of HttpTransportSE with set url
     *
     * @param url the destination to POST SOAP data
     */
    public SoapHelper(String url) {
        super(null, url);
    }

    /**
     * set the desired soapAction header field
     *
     * @param soapAction the desired soapAction
     * @param envelope   the envelope containing the information for the soap call.
     * @throws HttpResponseException
     * @throws IOException
     * @throws XmlPullParserException
     */
    public void call(String soapAction, SoapEnvelope envelope)
            throws HttpResponseException, IOException, XmlPullParserException {

        call(soapAction, envelope, null);
    }

    @Override
    public ServiceConnection getServiceConnection() throws IOException {
        return null;
    }

    public List call(String soapAction, SoapEnvelope envelope, List headers)
            throws HttpResponseException, IOException, XmlPullParserException {
        return call(soapAction, envelope, headers, null);
    }

    /**
     * Perform a soap call with a given namespace and the given envelope providing
     * any extra headers that the user requires such as cookies. Headers that are
     * returned by the web service will be returned to the caller in the form of a
     * <code>List</code> of <code>HeaderProperty</code> instances.
     *
     * @param soapAction the namespace with which to perform the call in.
     * @param envelope   the envelope the contains the information for the call.
     * @param headers    <code>List</code> of <code>HeaderProperty</code> headers to send with the SOAP request.
     * @param outputFile a file to stream the response into rather than parsing it, streaming happens when file is not null
     * @return Headers returned by the web service as a <code>List</code> of
     * <code>HeaderProperty</code> instances.
     * @throws HttpResponseException an IOException when Http response code is different from 200
     */
    public List call(String soapAction, SoapEnvelope envelope, List headers, File outputFile) throws HttpResponseException, IOException, XmlPullParserException {

        if (soapAction == null) {
            soapAction = "\"\"";
        }

        ServiceConnection connection = getServiceConnection();

        connection.setRequestProperty("User-Agent", USER_AGENT);
        // SOAPAction is not a valid header for VER12 so do not add
        // it
        // @see "http://code.google.com/p/ksoap2-android/issues/detail?id=67
        if (envelope.version != SoapSerializationEnvelope.VER12) {
            connection.setRequestProperty("SOAPAction", soapAction);
        }

        if (envelope.version == SoapSerializationEnvelope.VER12) {
            connection.setRequestProperty("Content-Type", CONTENT_TYPE_SOAP_XML_CHARSET_UTF_8);
        } else {
            connection.setRequestProperty("Content-Type", CONTENT_TYPE_XML_CHARSET_UTF_8);
        }
        // this seems to cause issues so we are removing it
        //connection.setRequestProperty("Connection", "close");
        connection.setRequestProperty("Accept-Encoding", "gzip");
        // Pass the headers provided by the user along with the call
        if (headers != null) {
            for (int i = 0; i < headers.size(); i++) {
                HeaderProperty hp = (HeaderProperty) headers.get(i);
                connection.setRequestProperty(hp.getKey(), hp.getValue());
            }
        }

        connection.setRequestMethod("POST");

        byte[] requestData = createRequestData(envelope, "UTF-8");

        requestDump = debug ? new String(requestData) : null;
        responseDump = null;

        connection.setRequestProperty("Content-Length", "" + requestData.length);
        //send
        sendData(requestData, connection, envelope);

        //get
        requestData = null;
        InputStream is = null;
        List retHeaders = null;
        byte[] buf = null; // To allow releasing the resource after used
        int contentLength = 8192; // To determine the size of the response and adjust buffer size
        boolean gZippedContent = false;
        boolean xmlContent = false;
        int status = connection.getResponseCode();

        try {
            retHeaders = connection.getResponseProperties();

            for (int i = 0; i < retHeaders.size(); i++) {
                HeaderProperty hp = (HeaderProperty) retHeaders.get(i);
                // HTTP response code has null key
                if (null == hp.getKey()) {
                    continue;
                }

                // If we know the size of the response, we should use the size to initiate vars
                if (hp.getKey().equalsIgnoreCase("content-length")) {
                    if (hp.getValue() != null) {
                        try {
                            contentLength = Integer.parseInt(hp.getValue());
                        } catch (NumberFormatException nfe) {
                            contentLength = 8192;
                        }
                    }
                }


                // Check the content-type header to see if we're getting back XML, in case of a
                // SOAP fault on 500 codes
                if (hp.getKey().equalsIgnoreCase("Content-Type")
                        && hp.getValue().contains("xml")) {
                    xmlContent = true;
                }


                // ignoring case since users found that all smaller case is used on some server
                // and even if it is wrong according to spec, we rather have it work..
                if (hp.getKey().equalsIgnoreCase("Content-Encoding")
                        && hp.getValue().equalsIgnoreCase("gzip")) {
                    gZippedContent = true;
                }
            }

            //first check the response code....
            if (status != 200 && status != 202) {
                //202 is a correct status returned by WCF OneWay operation
                throw new HttpResponseException("HTTP request failed, HTTP status: " + status, status, retHeaders);
            }

            if (contentLength > 0) {
                if (gZippedContent) {
                    is = getUnZippedInputStream(
                            new BufferedInputStream(connection.openInputStream(), contentLength));
                } else {
                    is = new BufferedInputStream(connection.openInputStream(), contentLength);
                }
            }
        } catch (IOException e) {
            if (contentLength > 0) {
                if (gZippedContent) {
                    is = getUnZippedInputStream(
                            new BufferedInputStream(connection.getErrorStream(), contentLength));
                } else {
                    is = new BufferedInputStream(connection.getErrorStream(), contentLength);
                }
            }

            if (e instanceof HttpResponseException) {
                if (!xmlContent) {
                    if (debug && is != null) {
                        //go ahead and read the error stream into the debug buffers/file if needed.
                        readDebug(is, contentLength, outputFile);
                    }

                    //we never want to drop through to attempting to parse the HTTP error stream as a SOAP response.
                    connection.disconnect();
                    throw e;
                }
            }
        }

        if (debug) {
            is = readDebug(is, contentLength, outputFile);
        }

        if (is != null) {
            parseResponse(envelope, is, retHeaders);
        }

        // release all resources
        // input stream is will be released inside parseResponse
        is = null;
        buf = null;
        //This fixes Issue 173 read my explanation here: https://code.google.com/p/ksoap2-android/issues/detail?id=173
        connection.disconnect();
        connection = null;
        return retHeaders;
    }

    protected void sendData(byte[] requestData, ServiceConnection connection, SoapEnvelope envelope)
            throws IOException {
        connection.setFixedLengthStreamingMode(requestData.length);
        OutputStream os = connection.openOutputStream();
        os.write(requestData, 0, requestData.length);
        os.flush();
        os.close();
    }

    protected void parseResponse(SoapEnvelope envelope, InputStream is, List returnedHeaders)
            throws XmlPullParserException, IOException {
        parseResponse(envelope, is);
    }


    private InputStream readDebug(InputStream is, int contentLength, File outputFile) throws IOException {
        OutputStream bos;
        if (outputFile != null) {
            bos = new FileOutputStream(outputFile);
        } else {
            // If known use the size if not use default value
            bos = new ByteArrayOutputStream((contentLength > 0) ? contentLength : 256 * 1024);
        }

        byte[] buf = new byte[256];

        while (true) {
            int rd = is.read(buf, 0, 256);
            if (rd == -1) {
                break;
            }
            bos.write(buf, 0, rd);
        }

        bos.flush();
        if (bos instanceof ByteArrayOutputStream) {
            buf = ((ByteArrayOutputStream) bos).toByteArray();
        }
        bos = null;
        responseDump = new String(buf);
        is.close();

        if (outputFile != null) {
            return new FileInputStream(outputFile);
        } else {
            return new ByteArrayInputStream(buf);
        }
    }

    private InputStream getUnZippedInputStream(InputStream inputStream) throws IOException {
        /* workaround for Android 2.3
           (see http://stackoverflow.com/questions/5131016/)
        */
        try {
            return (GZIPInputStream) inputStream;
        } catch (ClassCastException e) {
            return new GZIPInputStream(inputStream);
        }
    }


    public List<Object> getParams(String method, String nameSpacre, HashMap<String, Object> properties) {

        List<Object> resultList = new ArrayList<>();
        //throws HttpResponseException, IOException, XmlPullParserException
        SoapObject rpc = new SoapObject(nameSpacre, method);
        if (properties != null) {
            for (Iterator<Map.Entry<String, Object>> it = properties.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, Object> entry = it.next();
                rpc.addProperty(entry.getKey(), entry.getValue());
            }
        }
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        new MarshalBase64().register(envelope);
        envelope.bodyOut = rpc;
        envelope.dotNet = false;
        envelope.setOutputSoapObject(rpc);

        /***************** driver *******************/
        Map<String, String> mheaderMap = new HashMap<>();
        mheaderMap.put("User-Agent", USER_AGENT);

        if (envelope.version != SoapSerializationEnvelope.VER12) {
            mheaderMap.put("SOAPAction", "");
        }

        if (envelope.version == SoapSerializationEnvelope.VER12) {
            mheaderMap.put("Content-Type", CONTENT_TYPE_SOAP_XML_CHARSET_UTF_8);
        } else {
            mheaderMap.put("Content-Type", CONTENT_TYPE_XML_CHARSET_UTF_8);
        }

        mheaderMap.put("Accept-Encoding", "gzip");
        byte[] requestData;
        try {
            requestData = createRequestData(envelope, "UTF-8");
        } catch (IOException e) {
            requestData = new byte[0];
        }
        mheaderMap.put("Content-Length", "" + requestData.length);

        resultList.add(0, mheaderMap);
        resultList.add(1, requestData);
        return resultList;

    }

    public SoapEnvelope getSoapEnvelope() {
        return envelope;
    }

}
