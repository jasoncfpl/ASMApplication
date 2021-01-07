package com.asm.plugin.autotrack.click;

import com.android.tools.r8.org.objectweb.asm.MethodVisitor;
import com.android.tools.r8.org.objectweb.asm.Opcodes;
import com.android.tools.r8.org.objectweb.asm.commons.AdviceAdapter;


public class OnClickMethodVisitor extends AdviceAdapter {

    public OnClickMethodVisitor(final int api, final MethodVisitor mv, final int access, final String name, final String desc) {
        super(api,mv,access,name,desc);
    }


    @Override
    protected void onMethodEnter() {
        System.out.println("onMethodEnter");

        mv.visitVarInsn(ALOAD,1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,"com/asm/plugin/autotrack/track/ASMTracker",
                "track","(Landroid/view/View;)V",false);
    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
    }
}
