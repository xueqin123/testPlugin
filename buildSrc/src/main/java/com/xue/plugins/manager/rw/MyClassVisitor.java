package com.xue.plugins.manager.rw;


import com.xue.plugins.log.LogUtil;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;


public class MyClassVisitor extends ClassVisitor {
    private String TAG = "MyClassVisitor";
    private String mClassName;
    private String[] mInterfaces;

    public MyClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mClassName = name;
        mInterfaces = interfaces;
        LogUtil.i(TAG, "mClassName: " + mClassName + "mInterfaces: " + Arrays.deepToString(mInterfaces));
    }

    //访问类方法是回调
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
        LogUtil.i(TAG, "visitMethod() access: " + access + " name: " + name + " descriptor: " + descriptor + " signature: " + signature + " exceptions: " + Arrays.toString(exceptions));
        if (isMatchOnClickListener(name)) {
            methodVisitor = new MyAdapter(methodVisitor, access, name, descriptor) {
                @Override
                protected void onMethodExit(int opcode) {
                    super.onMethodExit(opcode);
                    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    mv.visitLdcInsn("点击结束");
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                }
            };
        }
        return methodVisitor;
    }

    private boolean isMatchOnClickListener(String methodName) {
        if (mInterfaces == null) {
            return false;
        }
        for (String mInterface : mInterfaces) {
            if (mInterface.equals("android/view/View$OnClickListener") && "onClick".equals(methodName)) {
                return true;
            }
        }
        return false;
    }
}
