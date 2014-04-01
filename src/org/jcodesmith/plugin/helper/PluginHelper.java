package org.jcodesmith.plugin.helper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jcodesmith.JCodeSmithActivator;

public class PluginHelper {
    public static String NEWLINE = System.getProperty("line.separator");

    /**
     * 返回图标的url
     * 
     * @param name
     * @return
     */
    public static URL iconURL(String name) {
        return JCodeSmithActivator.getDefault().getBundle().getEntry("icons/" + name);
    }

    /**
     * 获取配置路径
     * 
     * @param configfileName
     * @return
     */
    public static String getConfigFilePath(String configfileName) {
        String path = JCodeSmithActivator.getDefault().getStateLocation().makeAbsolute().toFile().getAbsolutePath();
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            if (path.indexOf("\\") != -1) {
                path += "\\" + configfileName;
            } else {
                path += "/" + configfileName;
            }
        }
        return path;
    }

    /**
     * 返回颜色值
     * 
     * @param color
     * @return
     */
    public static Color getColor(int color) {
        return Display.getCurrent().getSystemColor(color);
    }

    /**
     * 列出所有扩展点(仅用于编程时咨询)
     */
    public static void printAllExtendsionPoint() {
        IExtensionPoint[] points = Platform.getExtensionRegistry().getExtensionPoints();
        for (IExtensionPoint point : points) {
            System.out.println(point.getUniqueIdentifier());
        }
    }

    /**
     * 包文本message存到黏贴板中
     * 
     * @param message
     */
    public static void copyToClipBoard(String message) {
        Display display = JCodeSmithActivator.getActiveWorkbenchShell().getDisplay();
        Clipboard cb = new Clipboard(display);
        TextTransfer textTransfer = TextTransfer.getInstance();
        cb.setContents(new Object[] { message }, new Transfer[] { textTransfer });
    }

    /**
     * 为视图创建菜单和工具条
     * 
     * @param site
     * @param toolbarAction
     * @param menuAction
     */
    public static void createMenuAndToolBar(IViewSite site, Action[] toolbarAction, Action[] menuAction) {
        IActionBars bars = site.getActionBars();
        IToolBarManager toolbarManager = bars.getToolBarManager();
        IMenuManager menuManager = bars.getMenuManager();
        boolean first = true;
        for (Action action : toolbarAction) {
            toolbarManager.add(action);
            if (first) {
                first = false;
            } else {
                menuManager.add(new Separator());
            }
            menuManager.add(action);
        }
        for (Action action : menuAction) {
            if (first) {
                first = false;
            } else {
                menuManager.add(new Separator());
            }
            menuManager.add(action);
        }
    }

    /**
     * 打开视图或者设置焦点到视图
     * 
     * @param viewId
     */
    public static void showView(String viewId) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page == null) {
            return;
        }
        try {
            page.showView(viewId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final static String JAVA_NATURE = "org.eclipse.jdt.core.javanature";

    /**
     * 是否是java project
     * 
     * @param project
     * @return
     * @throws CoreException
     */
    public static boolean isJavaProject(IProject project) throws CoreException {
        return project.hasNature(JAVA_NATURE);
    }

    /**
     * 获取当前的编辑器
     * 
     * @return
     */
    public static IEditorPart getActiveEditor() {
        IEditorPart editor = JCodeSmithActivator.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        return editor;
    }

    /**
     * 初始化资源文件
     * 
     * @param path
     */
    public static void initResourceFile(String path) {
        String outpath = getConfigFilePath(path);

        String s = PluginHelper.class.getProtectionDomain().getCodeSource().getLocation().toString();
        if (s.endsWith(".jar")) {
            s = "jar:" + s + "!";
        }
        s += path;

        FileOutputStream out = null;
        InputStream in = null;
        File outFile = new File(outpath);
        try {
            if (outFile.exists()) {
                return;
            } else {
                String p = outFile.getParent();
                File pfile = new File(p);
                if (!pfile.exists()) {
                    pfile.mkdirs();
                }
            }

            URL a = new URL(s);

            out = new FileOutputStream(outFile);
            in = a.openStream();
            int temp = 0;
            while ((temp = in.read()) != -1) {
                out.write(temp);
            }

            // JarURLConnection conn = (JarURLConnection) a.openConnection();
            // JarFile jarfile = conn.getJarFile();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    // 完整的判断中文汉字和符号
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取默认的工程
     * 
     * @return
     */
    public static IProject getDefaultProject() {
        try {
            IProject project = PluginHelper.getSelectProject();
            if (project == null) {
                project = PluginHelper.getActiveProject();
            }
            if (project == null) {
                project = ResourcesPlugin.getWorkspace().getRoot().getProject(JCodeSmithActivator.PLUGIN_ID);
            }
            if (!project.isOpen()) {
                IProgressMonitor monitor = new NullProgressMonitor();
                project.open(monitor);
            }
            return project;
        } catch (CoreException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前选中的工程
     * 
     * @return
     */
    @SuppressWarnings("restriction")
    public static IProject getSelectProject() {
        ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();

        ISelection selection = selectionService.getSelection();

        IProject project = null;
        if (selection instanceof IStructuredSelection) {
            Object element = ((IStructuredSelection) selection).getFirstElement();

            if (element instanceof IResource) {
                project = ((IResource) element).getProject();
            } else if (element instanceof PackageFragmentRootContainer) {
                IJavaProject jProject = ((PackageFragmentRootContainer) element).getJavaProject();
                project = jProject.getProject();
            } else if (element instanceof IJavaElement) {
                IJavaProject jProject = ((IJavaElement) element).getJavaProject();
                project = jProject.getProject();
            }
        }
        return project;
    }

    /**
     * 获取当前激活的工程
     * 
     * @return
     */
    @SuppressWarnings("restriction")
    public static IProject getActiveProject() {
        IProject project = null;

        // 1.根据当前编辑器获取工程
        IEditorPart part = getActiveEditor();
        if (part != null) {
            Object object = part.getEditorInput().getAdapter(IFile.class);
            if (object != null) {
                project = ((IFile) object).getProject();
            }
        }

        if (project == null) {
            ISelectionService selectionService = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getSelectionService();
            ISelection selection = selectionService.getSelection();
            if (selection instanceof IStructuredSelection) {
                Object element = ((IStructuredSelection) selection).getFirstElement();

                if (element instanceof IResource) {
                    project = ((IResource) element).getProject();
                } else if (element instanceof PackageFragmentRootContainer) {
                    IJavaProject jProject = ((PackageFragmentRootContainer) element).getJavaProject();
                    project = jProject.getProject();
                } else if (element instanceof IJavaElement) {
                    IJavaProject jProject = ((IJavaElement) element).getJavaProject();
                    project = jProject.getProject();
                } else if (element instanceof IEditorPart) {
                    project = ((IFile) ((IEditorPart) element).getEditorInput().getAdapter(IFile.class)).getProject();
                }
            }
        }

        return project;
    }

    /**
     * 在java editor里打开内容
     * 
     * @param content
     */
    public static void openWithJavaEditor(String content, String filePath) {
        try {
            IProject project = PluginHelper.getDefaultProject();
            if (project == null ) {
                return;
            }else{
                if (!project.exists()) {
                    project.create(null);
                }
            }
            IFile java_file = project.getFile(new Path(filePath));
            if (!java_file.exists())
                java_file.create(new ByteArrayInputStream(content.getBytes()), false, null);
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            IDE.openEditor(page, java_file);
        } catch (Exception e) {
            PluginLogger.openInformation(e.getMessage());
        }
    }
}
