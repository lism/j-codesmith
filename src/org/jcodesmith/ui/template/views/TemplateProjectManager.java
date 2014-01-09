package org.jcodesmith.ui.template.views;

import org.jcodesmith.plugin.helper.PluginConfigFile;
import org.jcodesmith.plugin.helper.PluginSetting;

public class TemplateProjectManager {
    private static final PluginConfigFile<TemplateProject> config = new PluginConfigFile<TemplateProject>(
            PluginSetting.CONFIG_FILE_TEMPLATE_FOLDER,TemplateProject.class);

    public static PluginConfigFile<TemplateProject> getConfigMgr() {
        return config;
    }

}
