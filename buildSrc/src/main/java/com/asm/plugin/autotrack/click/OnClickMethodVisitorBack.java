package com.asm.plugin.autotrack.click;

import com.android.tools.r8.org.objectweb.asm.AnnotationVisitor;
import com.android.tools.r8.org.objectweb.asm.MethodVisitor;
import com.android.tools.r8.org.objectweb.asm.Opcodes;
import com.android.tools.r8.org.objectweb.asm.commons.AdviceAdapter;


public class OnClickMethodVisitorBack extends AdviceAdapter {

//     if ("onClick".equals(name) && access == Opcodes.ACC_PUBLIC) {
////             && "(Landroid/view/View;)V".equals(desc)
//        System.out.println("ASMClassVisitor desc:" + desc);
//        //onClick
//        return new OnClickMethodVisitor(Opcodes.ASM4,mv,access,name,desc);
//    }

    private String mMethodName;
    private String mAnnotationDesc;

    public OnClickMethodVisitorBack(final int api, final MethodVisitor mv, final int access, final String name, final String desc) {
        super(api,mv,access,name,desc);
        mMethodName = name;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if ("onClick".equals(mMethodName)) {
            System.out.println("OnClickMethodVisitor visitAnnotation:" + desc + "==visible:" + visible);
        }

        return super.visitAnnotation(desc, visible);
    }

    @Override
    protected void onMethodEnter() {
        if ("onClick".equals(mMethodName)) {
            System.out.println("onMethodEnter:" + mMethodName);
            mv.visitVarInsn(ALOAD,1);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,"com/asm/plugin/autotrack/track/ASMTracker",
                    "track","(Landroid/view/View;)V",false);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
    }
}
