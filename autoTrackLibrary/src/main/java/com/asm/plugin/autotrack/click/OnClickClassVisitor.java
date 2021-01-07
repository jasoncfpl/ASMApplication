package com.asm.plugin.autotrack.click;

import com.android.tools.r8.org.objectweb.asm.ClassVisitor;
import com.android.tools.r8.org.objectweb.asm.MethodVisitor;
import com.android.tools.r8.org.objectweb.asm.Opcodes;
import com.asm.plugin.autotrack.AutoTrackUtils;
import com.asm.plugin.autotrack.lifecycle.LifecycleOnCreateMethodVisitor;
import com.asm.plugin.autotrack.lifecycle.LifecycleOnDestroyMethodVisitor;

public class OnClickClassVisitor extends ClassVisitor implements Opcodes {

    private String mClassName;

    public OnClickClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        System.out.println("LifecycleClassVisitor : visit -----> started ：" + name);
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        System.out.println("LifecycleClassVisitor : visitMethod : " + name + "==:" + mClassName);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        //匹配FragmentActivity
        System.out.println("isFragmentActivity:" + isFragmentActivity(mClassName));
        if (isFragmentActivity(mClassName)) {
            if ("onCreate".equals(name) ) {
                //处理onCreate
                return new LifecycleOnCreateMethodVisitor(mv);
            } else if ("onDestroy".equals(name)) {
                System.out.println("visitMethod:onClick");
                //处理onDestroy
                return new LifecycleOnDestroyMethodVisitor(mv);
            }
        }
        return mv;
    }

    public boolean isFragmentActivity(String className) {
        //Android
        if ("android/support/v4/app/FragmentActivity".equals(className)) {
            return true;
        }
        //AndroidX
        if ("androidx/fragment/app/FragmentActivity".equals(className)) {
            return true;
        }
        return false;
    }

    @Override
    public void visitEnd() {
        System.out.println("LifecycleClassVisitor : visit -----> end");
        super.visitEnd();
    }
}