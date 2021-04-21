package com.xue.plugins.transform;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.xue.plugins.log.LogUtil;
import com.xue.plugins.manager.asm.ASMUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class MyTransform extends Transform {
    private static String TAG = "MyTransform";
    private boolean isASM = true;

    @Override
    public String getName() {
        return "MyTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        LogUtil.i(TAG, "transform() transformInvocation: " + transformInvocation);
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        for (TransformInput input : inputs) {
            //处理 jar 包
            transJarClass(input.getJarInputs(), transformInvocation);
            //处理手写的源码
            transDirClass(input.getDirectoryInputs(), transformInvocation);
        }
    }

    private void transJarClass(Collection<JarInput> jarInputs, TransformInvocation transformInvocation) throws IOException {
        LogUtil.i(TAG, "handlerJarClass()------------------------------------------------------ size: " + jarInputs.size());
        for (JarInput jarInput : jarInputs) {
            String destName = jarInput.getFile().getName();
            String absolutePath = jarInput.getFile().getAbsolutePath();
            LogUtil.i(TAG, "jar包名称: " + destName);
            LogUtil.i(TAG, "jar包路径: " + absolutePath);
            JarFile jarFile = new JarFile(jarInput.getFile());
            Enumeration<JarEntry> classes = jarFile.entries();
            while (classes.hasMoreElements()) {
                JarEntry libClass = classes.nextElement();
                LogUtil.i(TAG, " libClass.getName(): " + libClass.getName());
            }
            byte[] md5Name = DigestUtils.md5(absolutePath);
            if (destName.endsWith(".jar")) {
                destName.substring(0, destName.length() - 4);
            }
            File modifyJar = jarInput.getFile();
            File dest = transformInvocation.getOutputProvider().getContentLocation(destName + "_" + md5Name, jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);
            LogUtil.i(TAG, "jar 目标文件夹: " + dest.getAbsolutePath());
            FileUtils.copyFile(modifyJar, dest);
        }
    }

    private void transDirClass(Collection<DirectoryInput> directoryInputs, TransformInvocation transformInvocation) throws IOException {
        LogUtil.i(TAG, "transDirectoryInput()------------------------------------------------------ size: " + directoryInputs.size());
        for (DirectoryInput directoryInput : directoryInputs) {
            File dir = directoryInput.getFile();
            File dest = transformInvocation.getOutputProvider().getContentLocation(directoryInput.getName(), directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
            LogUtil.i(TAG, "当前文件夹 dir: " + dir.getAbsolutePath());
            LogUtil.i(TAG, "目标文件夹 dest: " + dest.getAbsolutePath());
            Map<String, File> modifyMap = new HashMap<>();
            Collection<File> files = FileUtils.listFiles(dir, new String[]{"class"}, true);
            for (File classFile : files) {
                if (!classFile.getName().endsWith("R.class") && !classFile.getName().endsWith("BuildConfig.class") && !classFile.getName().contains("R$")) {
                    File modified = modifyClassFile(dir, classFile, transformInvocation.getContext().getTemporaryDir());
                    String classRealName = classFile.getAbsolutePath().replace(dir.getAbsolutePath(), "");
                    modifyMap.put(classRealName, modified);
                }
            }
            FileUtils.copyDirectory(dir, dest);
            for (Map.Entry<String, File> entry : modifyMap.entrySet()) {
                File target = new File(dest.getAbsolutePath() + entry.getKey());
                if (target.exists()) {
                    target.delete();
                }
                FileUtils.copyFile(entry.getValue(), target);
                entry.getValue().delete();
            }
        }
    }

    private File modifyClassFile(File dir, File classFile, File tempDir) {
        LogUtil.i(TAG, "modifyClassFile() dir:" + dir.getAbsolutePath());
        LogUtil.i(TAG, "modifyClassFile() classFile:" + classFile.getAbsolutePath());
        LogUtil.i(TAG, "modifyClassFile() tempDir:" + tempDir.getAbsolutePath());
        File modified = null;
        try {
            String dirPath = dir.getAbsolutePath() + File.separator;
            String className = classFile.getAbsolutePath()
                    .replace(dirPath, "")
                    .replace(File.separator, ".")
                    .replace(".class", "");
            byte[] sourceClassBytes = IOUtils.toByteArray(new FileInputStream(classFile));
            byte[] modifyClassBytes = ASMUtils.modifyClass(sourceClassBytes);
            if (modifyClassBytes != null) {
                modified = new File(tempDir, className.replace(".", "") + ".class");
                if (modified.exists()) {
                    modified.delete();
                }
                modified.createNewFile();
                new FileOutputStream(modified).write(modifyClassBytes);
            }
        } catch (IOException e) {
            LogUtil.i(TAG, "e = " + e.getMessage());
            e.printStackTrace();
        }
        return modified;
    }
}
