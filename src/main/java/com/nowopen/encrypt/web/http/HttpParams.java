package com.nowopen.encrypt.web.http;

import com.nowopen.encrypt.web.bussiness.BIParams;

import java.util.List;
import java.util.Map;

/**
 * Created by pxie on 9/19/2014.
 */
public class HttpParams implements BIParams {

    final Map<String, List<String>> valueList;

    public HttpParams(final Map<String, List<String>> valueList){
       this.valueList = valueList;

    }

    @Override
    public String[] getArray(String key) {
        List<String> valueListForKey = getInternal(key);
        if(null == valueListForKey){
            return null;
        }
        return valueListForKey.toArray(new String[valueListForKey.size()]);
    }

    @Override
    public String getString(String key) {
        List<String> valueListForKey = getInternal(key);
        if(null == valueListForKey){
            return null;
        }
        return valueListForKey.get(0);
    }

    @Override
    public Boolean getBoolean(String key) {
        List<String> valueListForKey = getInternal(key);
        if(null == valueListForKey){
            return null;
        }

        return Boolean.parseBoolean(valueListForKey.get(0));
    }

    @Override
    public List<String> get(String key) {
        return getInternal(key);
    }

    private List<String> getInternal(String key) {
        if(null == valueList){
            return null;
        }
        List<String> valuesForkey = valueList.get(key);
        if(null == valuesForkey || valuesForkey.isEmpty()){
            return null;

        }
        return valuesForkey;
    }


}
