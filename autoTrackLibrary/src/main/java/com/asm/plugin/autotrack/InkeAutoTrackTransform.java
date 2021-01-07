package com.asm.plugin.autotrack;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.tools.r8.org.objectweb.asm.ClassReader;
import com.android.tools.r8.org.objectweb.asm.ClassVisitor;
import com.android.tools.r8.org.objectweb.asm.ClassWriter;
import com.android.utils.FileUtils;
import com.asm.plugin.autotrack.click.ASMClassVisitor;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.jar.JarFile;

public class InkeAutoTrackTransform extends Transform {
    @Override
    public String getName() {
        return "com.asm.plugin.autotrack.AutoTrackPlugin";
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
        System.out.println("transform start-----------:" );
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();

        if (outputProvider != null) {
            outputProvider.deleteAll();
        }
        System.out.println("transform inputs:" + inputs.size());

        inputs.forEach(new Consumer<TransformInput>() {
            @Override
            public void accept(TransformInput transformInput) {
                Collection<DirectoryInput> directoryInputs = transformInput.getDirectoryInputs();
                directoryInputs.forEach(new Consumer<DirectoryInput>() {
                    @Override
                    public void accept(DirectoryInput directoryInput) {
                        handleDirectoryInput(directoryInput,outputProvider);
                    }
                });
                Collection<JarInput> jarInputs = transformInput.getJarInputs();
                jarInputs.forEach(new Consumer<JarInput>() {
                    @Override
                    public void accept(JarInput jarInput) {
                        handleJarInput(jarInput,outputProvider);
                    }
                });
            }
        });

    }

    private void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        System.out.println("accept directoryInput:" + directoryInput.getFile().getAbsolutePath());
        FileUtils.getAllFiles(directoryInput.getFile()).forEach(new Consumer<File>() {
            @Override
            public void accept(File file) {
                String name = file.getName();
                if (name.endsWith(".class") && !name.startsWith("R$") && !"R.class".equals(name) && !"BuildConfig.class".equals(name)) {
//                    System.out.println("handleDirectoryInput classfile:" + name);
                    FileOutputStream fileOutputStream = null;
                    try {
                        ClassReader classReader = new ClassReader(org.apache.commons.io.FileUtils.readFileToByteArray(file));
                        ClassWriter classWriter = new ClassWriter(classReader,ClassWriter.COMPUTE_MAXS);
                        ClassVisitor classVisitor = new ASMClassVisitor(classWriter);
                        classReader.accept(classVisitor,ClassReader.EXPAND_FRAMES);
                        byte[] bytes = classWriter.toByteArray();
                        File destFile = new File(Objects.requireNonNull(file.getParentFile()).getAbsoluteFile(),name);
                        System.out.println("directoryInput =====重新写入位置 --> " + destFile.getAbsolutePath());
                        fileOutputStream = new FileOutputStream(destFile);
                        fileOutputStream.write(bytes);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fileOutputStream != null) {
                                fileOutputStream.close();
                                fileOutputStream = null;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        File dest = outputProvider.getContentLocation(directoryInput.getName(),
                directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
        System.out.println("handleDirectoryInput name:" + directoryInput.getName());
        System.out.println("handleDirectoryInput getContentTypes:" + directoryInput.getContentTypes());
        System.out.println("handleDirectoryInput getScopes:" + directoryInput.getScopes());
        System.out.println("handleDirectoryInput dest:" + dest.getAbsolutePath());
        System.out.println("handleDirectoryInput directoryInput.getFile():" + directoryInput.getFile());

        try {
            FileUtils.copyDirectory(directoryInput.getFile(), dest);
        } catch (IOException e) {
            System.out.println("copy error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {
        if (jarInput.getFile().getAbsolutePath().endsWith(".jar")) {
            String jarName = jarInput.getName();
            String md5Name = DigestUtils.md5Hex(jarInput.getFile().getAbsolutePath());
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4);
            }

            JarFile jarFile = null;
            try {
//                jarFile = new JarFile(jarInput.getFile());
//                Enumeration enumeration = jarFile.entries();
//                File tmpFile = new File(jarInput.getFile().getParent() + File.separator +
//                        "classes_temp.jar");
//                //避免上次的缓存被重复插入
//                if (tmpFile.exists()) {
//                    tmpFile.delete();
//                }
////                JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile));
//                //用于保存
//                while (enumeration.hasMoreElements()) {
//                    JarEntry jarEntry = (JarEntry) enumeration.nextElement();
//                    String entryName = jarEntry.getName();
//                    ZipEntry zipEntry = new ZipEntry(entryName);
//                    InputStream inputStream = jarFile.getInputStream(jarEntry);
//                    //插桩class
//                    if (entryName.endsWith(".class") && !entryName.startsWith("R$")
//                            && !"R.class".equals(entryName) && !"BuildConfig.class".equals(entryName)) {
//                        //class文件处理
//                        System.out.println("'----------- deal with jar class file <' + "+ entryName + " + '> " + "-----------'");
//                        System.out.println("handleJarInput classfile:" + entryName);
//                        jarOutputStream.putNextEntry(zipEntry);
//                        ClassReader classReader = new ClassReader(IOUtils.toByteArray(inputStream));
//                        ClassWriter classWriter = new ClassWriter(classReader,
//                                ClassWriter.COMPUTE_MAXS);
//                        ClassVisitor cv = new LifecycleClassVisitor(classWriter);
//                        classReader.accept(cv, ClassReader.EXPAND_FRAMES);
//                        byte[] code = classWriter.toByteArray();
//                        jarOutputStream.write(code);
//                    } else {
//                        jarOutputStream.putNextEntry(zipEntry);
//                        jarOutputStream.write(IOUtils.toByteArray(inputStream));
//                    }
//                    jarOutputStream.closeEntry();
//                }
//                //结束
//                jarOutputStream.close();
//                jarFile.close();

                File dest = outputProvider.getContentLocation(jarName+md5Name,
                        jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);
//
                FileUtils.copyFile(jarInput.getFile(), dest);
//                tmpFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
