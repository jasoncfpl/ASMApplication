//package com.asm.plugin.autotrack
//
//import com.android.build.api.transform.DirectoryInput
//import com.android.build.api.transform.QualifiedContent
//import com.android.build.api.transform.Transform
//import com.android.build.api.transform.TransformException
//import com.android.build.api.transform.TransformInput
//import com.android.build.api.transform.TransformInvocation
//import com.android.build.api.transform.TransformOutputProvider
//import com.android.tools.r8.org.objectweb.asm.ClassReader
//import com.android.tools.r8.org.objectweb.asm.ClassVisitor
//import com.android.tools.r8.org.objectweb.asm.ClassWriter
//import com.asm.plugin.autotrack.lifecycle.LifecycleClassVisitor
//import org.gradle.api.Plugin
//import org.gradle.api.Project
//
//import java.util.function.Consumer
//
//public class TrackPlugin extends Transform implements Plugin<Project>{
//
//    @Override
//    void apply(Project project) {
//        def appExtension = project.getExtensions().getByType(AppExtension.class)
//        appExtension.registerTransform(this)
//    }
//
//    @Override
//    String getName() {
//        return "ASMTransform"
//    }
//
//    @Override
//    Set<QualifiedContent.ContentType> getInputTypes() {
//        return TransformManager.CONTENT_CLASS
//    }
//
//    @Override
//    Set<? super QualifiedContent.Scope> getScopes() {
//        return TransformManager.SCOPE_FULL_PROJECT
//    }
//
//    @Override
//    boolean isIncremental() {
//        return false
//    }
//
//    @Override
//    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
//        println "ASMTransform transform"
//        def startTime = System.currentTimeMillis()
//
//        Collection<TransformInput> inputs = transformInvocation.inputs
//        TransformOutputProvider outputProvider = transformInvocation.outputProvider
//
//        inputs.forEach(new Consumer<TransformInput>() {
//            @Override
//            void accept(TransformInput transformInput) {
//                //遍历文件夹
//                transformInput.directoryInputs.each { DirectoryInput directoryInput ->
//                    handleDirectoryInput(directoryInput,outputProvider)
//
//                }
//                //遍历jar
//                transformInput.jarInputs.each { DirectoryInput directoryInput ->
////                    handleDirectoryInput(directoryInput,outputProvider)
//
//                }
//            }
//        })
//    }
//
//    private void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
//        if (directoryInput.file.isDirectory()) {
//            directoryInput.file.eachFileRecurse { File file ->
//                def name = file.name
//                if (name.endsWith(".class") && !name.startsWith("R\$")
//                        && !"R.class".equals(name) && !"BuildConfig.class".equals(name)
//                        && "android/support/v4/app/FragmentActivity.class".equals(name)) {
//                    println '----------- deal with "class" file <' + name + '> -----------'
//                    ClassReader classReader = new ClassReader(file.bytes)
//                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
//                    ClassVisitor cv = new LifecycleClassVisitor(classWriter)
//                    classReader.accept(cv, EXPAND_FRAMES)
//                    byte[] code = classWriter.toByteArray()
//                    FileOutputStream fos = new FileOutputStream(
//                            file.parentFile.absolutePath + File.separator + name)
//                    fos.write(code)
//                    fos.close()
//                }
//            }
//        }
//        //处理完输入文件之后，要把输出给下一个任务
//        def dest = outputProvider.getContentLocation(directoryInput.name,
//                directoryInput.contentTypes, directoryInput.scopes,
//                Format.DIRECTORY)
//        FileUtils.copyDirectory(directoryInput.file, dest)
//    }
//}