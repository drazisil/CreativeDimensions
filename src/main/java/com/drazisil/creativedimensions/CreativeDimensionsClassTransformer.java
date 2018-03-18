package com.drazisil.creativedimensions;


import javassist.*;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CreativeDimensionsClassTransformer implements net.minecraft.launchwrapper.IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        //Check if the JVM is about to process the tc.class or the EntityCreeper.class
        if (name.equals("amu") || name.equals("net.minecraft.world.World")) {
            System.out.println("********* INSIDE CREATIVE DIMENSIONS TRANSFORMER ABOUT TO PATCH: " + name);
            patchMethod(name, basicClass, name, CreativeDimensionsFMLLoadingPlugin.location);
//            basicClass = patchClassInJar(name, basicClass, name, CreativeDimensionsFMLLoadingPlugin.location);
        }
        return basicClass;
    }

    public void patchMethod(String name, byte[] bytes, String ObfName, File location) {
//        org.objectweb.asm.tree.ClassNode classNode = new org.objectweb.asm.tree.ClassNode();
//        ClassReader classReader = new ClassReader(bytes);
//        classReader.accept(classNode, 0);
//
//        // find method to inject into
//        Iterator<MethodNode> methods = classNode.methods.iterator();
//        while (methods.hasNext()) {
//            MethodNode m = methods.next();
//            if (m.name.equals("func_189509_E")) {
//                System.out.println("Located!");
//            }
//        }

        ClassPool pool = ClassPool.getDefault();
        CtClass cc = null;
        try {
            cc = pool.get(name);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Located " + cc.getName() + "!");
        CtMethod[] methods = cc.getDeclaredMethods();
        int i;
        for (i = 0; i < methods.length; i++) {
            CtMethod method = methods[i];
            if (methods[i].getName().equals("E")) {
                System.out.println("Located E!");
                try {
                    method.setBody(" { " +
                            "System.out.println(\"maxHeight: \" + aa());"
                    + "return $1.q() < 0 || $1.q() >= aa(); } ");
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
                System.out.println("Patched E!");
            }

        }
    }

    //a small helper method that takes the class name we want to replace and our jar file.
    //It then uses the java zip library to open up the jar file and extract the classes.
    //Afterwards it serializes the class in bytes and pushes it on to the JVM.
    //with the original bytes that JVM was about to process ignored completely

    public byte[] patchClassInJar(String name, byte[] bytes, String ObfName, File location) {
        try {
            //open the jar as zip
            ZipFile zip = new ZipFile(location);
            //find the file inside the zip that is called te.class or net.minecraft.entity.monster.EntityCreeper.class
            //replacing the . to / so it would look for net/minecraft/entity/monster/EntityCreeper.class
            ZipEntry entry = zip.getEntry(name.replace('.', '/') + ".class");
            if (entry == null) {
                System.out.println(name + " not found in " + location.getName());
            } else {
                //serialize the class file into the bytes array
                InputStream zin = zip.getInputStream(entry);
                bytes = new byte[(int) entry.getSize()];
                zin.read(bytes);
                zin.close();
                System.out.println("[" + "CreativeDimensions" + "]: " + "Class " + name + " patched!");
            }
            zip.close();
        } catch (Exception e) {
            throw new RuntimeException("Error overriding " + name + " from " + location.getName(), e);
        }
        //return the new bytes
        return bytes;
    }
}