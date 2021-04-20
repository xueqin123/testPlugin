package com.xue.plugins.manager.asm;

import com.xue.plugins.manager.rw.MyClassVisitor;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;


public class ASMUtils {


    public static byte[] modifyClass(byte[] src) {
        byte[] classBytesCodes = null;
        try {
            classBytesCodes = modifyClasses(src);
            if (classBytesCodes == null) {
                classBytesCodes = src;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classBytesCodes;
    }

    private static byte[] modifyClasses(byte[] src) {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new MyClassVisitor(classWriter);
        ClassReader classReader = new ClassReader(src);
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
}
