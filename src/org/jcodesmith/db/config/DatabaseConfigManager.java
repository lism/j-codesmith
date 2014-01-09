package org.jcodesmith.db.config;

import org.jcodesmith.plugin.helper.PluginConfigFile;
import org.jcodesmith.plugin.helper.PluginSetting;

public class DatabaseConfigManager {
    private static final PluginConfigFile<DatabaseConfig> dbconfig = new PluginConfigFile<DatabaseConfig>(
            PluginSetting.CONFIG_FILE_DATABASE,DatabaseConfig.class);

    public static PluginConfigFile<DatabaseConfig> getConfigMgr() {
        return dbconfig;
    }
    
}
