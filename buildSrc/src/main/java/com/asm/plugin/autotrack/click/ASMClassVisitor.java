package com.asm.plugin.autotrack.click;

import com.android.tools.r8.org.objectweb.asm.ClassVisitor;
import com.android.tools.r8.org.objectweb.asm.MethodVisitor;
import com.android.tools.r8.org.objectweb.asm.Opcodes;

public class ASMClassVisitor extends ClassVisitor implements Opcodes {

    private String mClassName;
    //是否是实现了onClickListener接口
    private boolean isClickListenerChild;
    private boolean isLongClickListenerChild = true;
    private boolean isItemClickListenerChild = true;

    public ASMClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces) {
        this.mClassName = name;
        for (String anInterface : interfaces) {
            if ("android/view/View$OnClickListener".equals(anInterface)) {
                isClickListenerChild = true;
            }
            if ("android/view/View$OnLongClickListener".equals(anInterface)) {
                isLongClickListenerChild = true;
            }
            if ("android/widget/AdapterView$OnItemClickListener".equals(anInterface)) {
                isItemClickListenerChild = true;
            }
        }

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (isClickListenerChild && "onClick".equals(name)) {
            System.out.println("onClick method:" + mClassName);
            return new OnClickMethodVisitor(mv,desc);
        } else if (isLongClickListenerChild && "onLongClick".equals(name)) {
            System.out.println("onLongClick method:" + mClassName);
            return new OnLongClickMethodVisitor(mv,desc);
        } else if (isItemClickListenerChild && "onItemClick".equals(name)) {
            System.out.println("onItemClick method:" + mClassName);
            return new OnItemClickMethodVisitor(mv,desc);
        }
        return mv;
    }
    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}