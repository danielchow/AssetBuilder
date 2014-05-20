package com.gametravel.assetsbuilder;

import com.badlogic.gdx.files.FileHandle;
import com.gametravel.assetsbuilder.export.AssetCodeGenerator;
import com.gametravel.assetsbuilder.scan.AssetBundle;
import com.gametravel.assetsbuilder.scan.AssetScanner;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.*;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.awt.RelativePoint;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel_2
 * Date: 11/10/13
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionBuildAssets extends AnAction {

    private Module coreModule;
    private Module androidModule;
    private Project project;

    public void actionPerformed(final AnActionEvent event) {
        project = getEventProject(event);
        final Options assetBuilderOptions;
        VirtualFile baseDir = project.getBaseDir();
        VirtualFile assetBuilderOptionsFile = baseDir.findChild("AssetBuilderOptions.json");
        if (assetBuilderOptionsFile == null) {
            assetBuilderOptions = Options.DEFAULT;
            final FileHandle optionFile = new FileHandle(baseDir.getPath() + "/AssetBuilderOptions.json");
            final Options opts = assetBuilderOptions;
            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                @Override
                public void run() {
                    opts.save(optionFile);
                }
            });
        } else {
            assetBuilderOptions = Options.load(new FileHandle(assetBuilderOptionsFile.getPath()));
        }
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        androidModule = moduleManager.findModuleByName("android");
        coreModule = moduleManager.findModuleByName("core");

        ModuleRootManager androidModuleRootManager = ModuleRootManager.getInstance(androidModule);
        VirtualFile[] androidModuleContentRoots = androidModuleRootManager.getContentRoots();
        if (androidModuleContentRoots == null || androidModuleContentRoots.length <= 0) {
            showError("Can't find root of android module", event);
            return;
        }
        VirtualFile androidModuleRoot = androidModuleContentRoots[0];
        final VirtualFile assetRoot = androidModuleRoot.findChild("assets");
        if (assetRoot == null) {
            showError("Can't find \"assets\" folder in android module", event);
            return;
        }

        ModuleRootManager coreModuleRootManager = ModuleRootManager.getInstance(coreModule);
        VirtualFile[] coreModuleSourceRoots = coreModuleRootManager.getSourceRoots();
        if (coreModuleSourceRoots == null || coreModuleSourceRoots.length <= 0) {
            showError("Can't find any source roots of core module", event);
            return;
        }
        final VirtualFile coreModuleRoot = coreModuleSourceRoots[0];

        AssetScanner assetScanner = new AssetScanner(assetBuilderOptions);
        final AssetBundle bundle;
        try {
            bundle = assetScanner.scanPath(new FileHandle(assetRoot.getPath()));
        } catch (BuildException e) {
            e.printStackTrace();
            showError("Build failed: " + e.getMessage(), event);
            return;
        }
        final AssetCodeGenerator generator = new AssetCodeGenerator();
        final Options opts = assetBuilderOptions;

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                VirtualFile assetsJavaFile;
                String packageDir = "/" + assetBuilderOptions.Output.PackageName.replace('.', '/');
                try {
                    VirtualFile folder = VfsUtil.createDirectoryIfMissing(coreModuleRoot, packageDir);
                    assetsJavaFile = folder.findOrCreateChildData(null, assetBuilderOptions.Output.ClassName + ".java");
                } catch (IOException e) {
                    showError("Create directory or java file failed!", event);
                    return;
                }

                try {
                    String code = generator.generate(bundle, opts);
                    assetsJavaFile.setBinaryContent(code.getBytes("UTF-8"));
                    showInfo("Assets built successfully", event);
                } catch (Exception e) {
                    e.printStackTrace();
                    showError("Write file content failed", event);
                }
            }
        });

    }

    private void showBalloon(String message, MessageType type, long duration, AnActionEvent event) {
        StatusBar statusBar = WindowManager.getInstance()
                .getStatusBar(DataKeys.PROJECT.getData(event.getDataContext()));
        JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(message, type, null)
                .setFadeoutTime(duration)
                .createBalloon()
                .show(RelativePoint.getCenterOf(statusBar.getComponent()),
                        Balloon.Position.atRight);
    }

    private void showWarning(String message, AnActionEvent event) {

        showBalloon(message, MessageType.WARNING, 5000, event);
    }

    private void showInfo(String message, AnActionEvent event) {
        showBalloon(message, MessageType.INFO, 5000, event);
    }

    private void showError(String message, AnActionEvent event) {
        showBalloon(message, MessageType.ERROR, 5000, event);
    }

    public void test() {

    }

    public static void main(String [] args) {
        ActionBuildAssets action = new ActionBuildAssets();
        action.test();
    }

}
