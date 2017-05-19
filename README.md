# ksoap2retrofit
you use retrofit2 access webservice ,replace ksoap jar.

## usage
#### 1,set ConfigManager,include of baseurl,timeout field.
    
            ConfigManager configManager = new ConfigManager.Builder().setBASE_URL(ConstantManager.baseurl).build();
#### 2,init retrofit.
    
            RetrofitManager manager = RetrofitManager.getInstance().init(configManager);
#### 3,convert ksoap string to the retrofit header and body.
    
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("loginName", username);
        properties.put("password", pwd);
        properties.put("code", "");
        
        List<Object> getParamters = SoapHelper.getInstance().getParams(ConstantManager.loginMethodName, ConstantManager.nameSpace, properties);
        Map<String, String> soapHeaderMap = null;
        String mBody = null;
        if (getParamters != null) {
            soapHeaderMap = (Map<String, String>) getParamters.get(0);
            mBody = new String((byte[]) getParamters.get(1));
        }
        return getPams4Api().downloadbasedata(soapHeaderMap, mBody);
#### 4,make your modle.it will be parsed by simplexml.


