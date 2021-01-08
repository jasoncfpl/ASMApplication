package com.asm.plugin.autotrack.click;

import com.android.tools.r8.org.objectweb.asm.MethodVisitor;
import com.android.tools.r8.org.objectweb.asm.Opcodes;


public class OnLongClickMethodVisitor extends MethodVisitor {

    private String mMethodDesc = "";

    public OnLongClickMethodVisitor(MethodVisitor mv, String desc) {
        super(Opcodes.ASM6,mv);
        mMethodDesc = desc;
    }

    @Override
    public void visitCode() {
        if ("(Landroid/view/View;)Z".equals(mMethodDesc)) {
            mv.visitVarInsn(Opcodes.ALOAD,1);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,"com/meelive/ingkee/autotrack/ASMTracker",
                    "longClickTrack","(Landroid/view/View;)V",false);
        }
        super.visitCode();

    }
}
