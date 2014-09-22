package com.nowopen.encrypt;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * Created by admin@OYJ on 2014/9/15.
 */
public class PropertiesUtil {

    public static final String sys = "system.properties";
    private static final PropertiesUtil instance = new PropertiesUtil();

    //Configurations backed by Apache Commons
    private PropertiesConfiguration system_config;

    private PropertiesUtil() {
        try {
            system_config = new PropertiesConfiguration(sys);
            system_config.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException e1) {
            e1.printStackTrace();
        }
    }

    public static PropertiesUtil getInstance() {
        return instance;
    }
    public Configuration getSysConfig() {
        return system_config;
    }

}