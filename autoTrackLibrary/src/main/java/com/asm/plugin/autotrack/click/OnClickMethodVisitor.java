package com.asm.plugin.autotrack.click;

import com.android.tools.r8.org.objectweb.asm.Label;
import com.android.tools.r8.org.objectweb.asm.MethodVisitor;
import com.android.tools.r8.org.objectweb.asm.Opcodes;
import com.android.tools.r8.org.objectweb.asm.commons.AdviceAdapter;


public class OnClickMethodVisitor extends AdviceAdapter {

    public OnClickMethodVisitor(final int api, final MethodVisitor mv, final int access, final String name, final String desc) {
        super(api,mv,access,name,desc);
        System.out.println("OnClickMethodVisitor");
    }


    @Override
    protected void onMethodEnter() {
        System.out.println("onMethodEnter");

        mv.visitVarInsn(ALOAD,1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,"com/asm/plugin/autotrack/track/ASMTracker",
                "track","(Landroid/view/View;)V",false);
//        Label l0 = new Label();
//        mv.visitLabel(l0);
//        mv.visitLineNumber(8, l0);
//        mv.visitVarInsn(ALOAD, 1);
//        mv.visitMethodInsn(INVOKESTATIC, "com/asm/plugin/autotrack/track/ASMTracker", "track", "(Landroid/view/View;)V", false);
//        Label l1 = new Label();
//        mv.visitLabel(l1);
//        mv.visitLineNumber(9, l1);
//        mv.visitInsn(RETURN);
//        Label l2 = new Label();
//        mv.visitLabel(l2);
//        mv.visitLocalVariable("this", "Lcom/example/testapplication/Test;", null, l0, l2, 0);
//        mv.visitLocalVariable("view", "Landroid/view/View;", null, l0, l2, 1);
//        mv.visitMaxs(1, 2);
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
    }
}
