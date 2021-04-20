package com.xue.plugins.manager.rw;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class MyAdapter extends AdviceAdapter {
    protected MyAdapter(MethodVisitor methodVisitor, int i1, String s, String s1) {
        super(Opcodes.ASM5, methodVisitor, i1, s, s1);
    }
}
