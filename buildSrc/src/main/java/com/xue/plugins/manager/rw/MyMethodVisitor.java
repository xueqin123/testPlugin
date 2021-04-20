package com.xue.plugins.manager.rw;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;

public class MyMethodVisitor extends MethodVisitor {

    public MyMethodVisitor(int api) {
        super(api);
    }

    public MyMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }
    //访问本地变量类型指令 操作码可以是LOAD,STORE，RET中一种；
    @Override
    public void visitParameter(String name, int access) {
        super.visitParameter(name, access);
    }

    //域操作指令，用来加载或者存储对象的Field
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    //访问方法操作指令
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
}
