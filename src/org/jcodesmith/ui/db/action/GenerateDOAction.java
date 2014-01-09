/**
 * 
 */
package org.jcodesmith.ui.db.action;

import org.jcodesmith.db.dal.MetaManager;
import org.jcodesmith.db.meta.TableMeta;
import org.jcodesmith.engine.SupportType;
import org.jcodesmith.engine.TemplateObject;
import org.jcodesmith.engine.TemplateProperty;
import org.jcodesmith.plugin.helper.PluginLogger;
import org.jcodesmith.plugin.resources.DefalutTemplateFile;
import org.jcodesmith.plugin.resources.PluginMessages;
import org.jcodesmith.ui.action.ExcuteAction;
import org.jcodesmith.ui.db.views.DataBaseView;

/**
 * 生成默认的DO对象
 * 
 * @author greki.shen
 */
public class GenerateDOAction extends ExcuteAction {
    public  GenerateDOAction() {
        super();
        super.tplObject = new TemplateObject(DefalutTemplateFile.DO_FTL);
    }

    /**
     * 打开创建连接向导页面
     */
    @Override
    public void run() {

        TableMeta tableMeta = DataBaseView.getSelectTableNode().getTableMeta();
            if (tableMeta == null) {
                PluginLogger.openInformation(PluginMessages.PLEASE_SELECT_TABLE);
                return;
            }

            if (tableMeta.getColumns() == null) {
                tableMeta.setColumns(MetaManager.getColumns(DataBaseView.getSelectConnection(), tableMeta.getName()));
            }

            for (TemplateProperty p : getTemplateObject().getPropertyList()) {
                if (p.getType().equals(SupportType.TABLE.getType())) {
                    p.setValue(tableMeta);
                    p.setDefaultValue(tableMeta.getName());
                    break;
                }
            }
            super.run();
    }
}
