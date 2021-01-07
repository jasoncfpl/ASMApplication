package com.asm.plugin.autotrack.click;

import com.android.tools.r8.org.objectweb.asm.ClassVisitor;
import com.android.tools.r8.org.objectweb.asm.MethodVisitor;
import com.android.tools.r8.org.objectweb.asm.Opcodes;

public class ASMClassVisitor extends ClassVisitor implements Opcodes {

    private String mClassName;

    public ASMClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        System.out.println("ASMClassVisitor : visit -----> started ：" + name);
        this.mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        System.out.println("ASMClassVisitor : visitMethod : " + name + "==:" + mClassName);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if ("onClick".equals(name)) {
//             && "(Landroid/view/View;)V".equals(desc)
            System.out.println("ASMClassVisitor desc:" + desc);
            //onClick
            return new OnClickMethodVisitor(Opcodes.ASM4,mv,access,name,desc);
        }
        return mv;
    }
    @Override
    public void visitEnd() {
        System.out.println("ASMClassVisitor : visit -----> end");
        super.visitEnd();
    }
}