package org.jcodesmith;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jcodesmith.db.config.DatabaseConfigManager;
import org.jcodesmith.db.dal.ConnectionManager;
import org.jcodesmith.engine.helper.ExcuteDialogDataCacheHelper;
import org.jcodesmith.plugin.InitialPluginResource;
import org.jcodesmith.ui.pref.JcodesmithPreferencePage;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class JCodeSmithActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "jcodesmith"; //$NON-NLS-1$

	// The shared instance
	private static JCodeSmithActivator plugin;

	public static Shell getActiveWorkbenchShell() {
		IWorkbenchWindow workBenchWindow = getActiveWorkbenchWindow();
		if (workBenchWindow == null)
			return null;
		return workBenchWindow.getShell();
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		if (plugin == null)
			return null;
		IWorkbench workBench = plugin.getWorkbench();
		if (workBench == null)
			return null;
		return workBench.getActiveWorkbenchWindow();
	}
	/**
	 * The constructor
	 */
	public JCodeSmithActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		//加载db配置
		DatabaseConfigManager.getConfigMgr().load();
		//模板
		//TemplateProjectManager.getConfigMgr().load();
		//初始化资源
		InitialPluginResource.resouceInitial();
		//初始化执行过的数据
		ExcuteDialogDataCacheHelper.load();
		
		//加载自定义变量
		JcodesmithPreferencePage.loadPerference();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	    DatabaseConfigManager.getConfigMgr().saveToFile();
	    ExcuteDialogDataCacheHelper.saveCache();
        //TemplateProjectManager.getConfigMgr().saveToFile();
	    ConnectionManager.freeAllConnection();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static JCodeSmithActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	   public static IPreferenceStore preference() {
	        return plugin.getPreferenceStore();
	    }
}
