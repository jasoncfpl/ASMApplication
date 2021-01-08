package com.asm.plugin.autotrack.click;

import com.android.tools.r8.org.objectweb.asm.MethodVisitor;
import com.android.tools.r8.org.objectweb.asm.Opcodes;


public class OnItemClickMethodVisitor extends MethodVisitor {

    private String mMethodDesc = "";

    public OnItemClickMethodVisitor(MethodVisitor mv, String desc) {
        super(Opcodes.ASM6,mv);
        mMethodDesc = desc;
    }

    @Override
    public void visitCode() {
        System.out.println("OnItemClickMethodVisitor ---:" + mMethodDesc);
        if ("(Landroid/widget/AdapterView;Landroid/view/View;IJ)V".equals(mMethodDesc)) {
            mv.visitVarInsn(Opcodes.ALOAD,1);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,"com/meelive/ingkee/autotrack/ASMTracker",
                    "itemClickTrack","(Landroid/view/View;)V",false);
        }
        super.visitCode();

    }
}
