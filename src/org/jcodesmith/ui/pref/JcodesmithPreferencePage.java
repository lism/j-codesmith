package org.jcodesmith.ui.pref;

import static org.eclipse.swt.layout.GridData.GRAB_HORIZONTAL;
import static org.eclipse.swt.layout.GridData.GRAB_VERTICAL;
import static org.eclipse.swt.layout.GridData.HORIZONTAL_ALIGN_FILL;
import static org.eclipse.swt.layout.GridData.VERTICAL_ALIGN_FILL;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jcodesmith.JCodeSmithActivator;
import org.jcodesmith.plugin.helper.PluginSetting;

public class JcodesmithPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public JcodesmithPreferencePage() {
        super(GRID);
        setTitle("JCodeSmith Preference");
        setPreferenceStore(JCodeSmithActivator.getDefault().getPreferenceStore());
        
        
 }
    
    @Override
    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void createFieldEditors() {
        Composite composite = new Composite(getFieldEditorParent(), SWT.NORMAL);
        
        GridLayout basicLayout = new GridLayout();
        basicLayout.numColumns = 1;
        composite.setLayout(basicLayout);
        
        GridData gdc = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gdc);
        
        
        FileFieldEditor fileEditor=      new FileFieldEditor(PluginSetting.PREFERENCE_JAR, "Extends Util Jars", composite);
        fileEditor.setFileExtensions(new String[]{"*.jar"});
        //jar
        addField(fileEditor);
        
        
        PathEditor pathEditor=new PathEditor("", "", "", composite);
        addField(pathEditor);
        
    }

    protected void addField(FieldEditor editor) {
        super.addField(editor);
    }
}
