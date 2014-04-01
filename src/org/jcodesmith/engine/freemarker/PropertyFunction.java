package org.jcodesmith.engine.freemarker;

import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

import org.jcodesmith.engine.SupportType;
import org.jcodesmith.engine.TemplateProperty;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * <@property name="package" desc="包名" type="String" defaultValue="12122"/>
 * 
 * @Type PropertyFunction
 * @Desc 自定义属性指令，不错处理，只做兼容
 * @author DELL
 * @date 2013年11月20日
 * @Version V1.0
 */
public class PropertyFunction implements TemplateDirectiveModel {

    public static final String PROPERTY_TAG = "@property";

    public static final String PARAM_NAME = "name";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_DESC = "desc";
    public static final String PARAM_DEFAULTVALUE = "defaultValue";

    public static final String[] PARAMS = new String[] { PARAM_NAME, PARAM_TYPE, PARAM_DESC, PARAM_DEFAULTVALUE };

    public static TemplateProperty getProperty(String propertyTag) {
        TemplateProperty p = new TemplateProperty();
        StringTokenizer st = new StringTokenizer(propertyTag, " =\"", true);
        String find = "";
        boolean nextisVal = false;
        while (st.hasMoreTokens()) {
            String tk = st.nextToken();
            if (tk.trim().isEmpty() || tk.equals("\"")) {
                continue;
            }
            if (nextisVal) {
                if (PARAM_NAME.equals(find)) {
                    p.setName(tk);
                } else if (PARAM_TYPE.equals(find)) {
                    p.setType(SupportType.getEnum(tk));
                } else if (PARAM_DESC.equals(find)) {
                    p.setDescription(tk);
                } else if (PARAM_DEFAULTVALUE.equals(find)) {
                    p.setDefaultValue(tk);
                }
                nextisVal = false;
                find = "";
                continue;
            }

            if (tk.equals("=")) {
                if (!find.isEmpty()) {
                    nextisVal = true;
                } else {
                    find = "";
                }
                continue;
            }

            for (String para : PARAMS) {
                if (para.equals(tk)) {
                    find = para;
                    break;
                }
            }

        }

        return p;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {
        // 检验参数
        checkParam(params);
        // 获取参数
        // String path = (String) DeepUnwrap.unwrap((TemplateModel)
        // params.get(PARAM_NAME));

    }

    @SuppressWarnings("rawtypes")
    protected void checkParam(Map params) throws TemplateModelException {
        if (params.isEmpty() || !params.containsKey(PARAM_NAME)) {
            throw new TemplateModelException(
                    "指令参数错误，请指定命令例如：<@property name='package' desc='包名' type='String' defaultValue='12122'");
        }
    }
}
