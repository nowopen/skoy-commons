package com.nowopen.encrypt.web.bussiness;

import java.util.List;

/**
 * Created by pxie on 9/19/2014.
 */
public interface BIParams {

    public String[] getArray(final String key);
    public String getString(final String key);

    public Boolean getBoolean(final String key);

    public List<String> get(final String key);

}
