package com.xue.plugins.manager.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
public class ASMUtils {

    public static void addMethodSum(ClassVisitor cw) {
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "add", "(II)I", null, null);
        mv.visitCode();
        mv.visitVarInsn(ILOAD, 1);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitInsn(IADD);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "com/example/testplugin/MainActivity", "num1", "I");
        mv.visitInsn(IADD);
        mv.visitInsn(IRETURN);
        Label l0 = new Label();
        Label l1 = new Label();
        mv.visitLabel(l0);
        mv.visitLabel(l1);
        mv.visitLocalVariable("this", "com/example/testplugin/MainActivity;", null, l0, l1, 0);
        mv.visitLocalVariable("a", "I", null, l0, l1, 1);
        mv.visitLocalVariable("b", "I", null, l0, l1, 2);
        mv.visitMaxs(2, 3);
        mv.visitEnd();
    }

    public static void addLog(MethodVisitor mv){
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("点击结束");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }
}
