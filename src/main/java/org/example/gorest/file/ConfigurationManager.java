package org.example.gorest.file;

import org.aeonbits.owner.ConfigCache;

public class ConfigurationManager {

    public static AppConfig getBaseConfig(){
        return ConfigCache.getOrCreate(AppConfig.class);
    }
}
