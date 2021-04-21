package com.xue.plugins.manager.rw;


import com.xue.plugins.log.LogUtil;
import com.xue.plugins.manager.asm.ASMUtils;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

import java.util.Arrays;


public class MyClassVisitor extends ClassVisitor {
    private String TAG = "MyClassVisitor";
    private String mClassName;
    private String[] mInterfaces;
    private boolean isASM = true;

    public MyClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        mClassName = name;
        mInterfaces = interfaces;
        LogUtil.i(TAG, "visit() mClassName: " + mClassName + " mInterfaces: " + Arrays.toString(mInterfaces));
        if (mClassName.equals("com/example/testplugin/MainActivity")) {
            if (isASM) {
                ASMUtils.addMethodSum(cv);
            }else {
                //todo
            }
        }
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        LogUtil.i(TAG, "visitEnd()");
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        LogUtil.i(TAG, "visitField() access: " + access + " name: " + name + " descriptor: " + descriptor + " signature: " + signature + " value:" + value);
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        LogUtil.i(TAG, "visitAnnotation() descriptor: " + descriptor + " visible: " + visible);
        return super.visitAnnotation(descriptor, visible);
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
                    if(isASM){
                        ASMUtils.addLog(mv);
                    }
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


    @Override
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        LogUtil.i(TAG, "visitInnerClass() name: " + name + " outerName: " + outerName + " innerName: " + innerName + " access: " + access);
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        super.visitAttribute(attribute);
        LogUtil.i(TAG, "visitAttribute() attribute: " + attribute);
    }

    @Override
    public void visitOuterClass(String owner, String name, String descriptor) {
        super.visitOuterClass(owner, name, descriptor);
        LogUtil.i(TAG, "visitOuterClass() owner: " + owner + " name: " + name + " descriptor: " + descriptor);
    }

    @Override
    public ModuleVisitor visitModule(String name, int access, String version) {
        LogUtil.i(TAG, "visitModule() name: " + name + " access: " + access + " version: " + version);
        return super.visitModule(name, access, version);
    }

    @Override
    public void visitNestMember(String nestMember) {
        LogUtil.i(TAG, "visitNestMember() nestMember: " + nestMember);
        super.visitNestMember(nestMember);
    }

    @Override
    public void visitNestHost(String nestHost) {
        LogUtil.i(TAG, "visitNestHost() nestHost: " + nestHost);
        super.visitNestHost(nestHost);
    }

    @Override
    public void visitSource(String source, String debug) {
        LogUtil.i(TAG, "visitSource() source: " + source + " debug: " + debug);
        super.visitSource(source, debug);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        LogUtil.i(TAG, "visitTypeAnnotation() typeRef: " + typeRef + " typePath: " + typePath + " descriptor: " + descriptor + " visible: " + visible);
        return super.visitTypeAnnotation(typeRef, typePath, descriptor, visible);
    }


}
