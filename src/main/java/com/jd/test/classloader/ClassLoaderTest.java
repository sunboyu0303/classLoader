package com.jd.test.classloader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sunboyu on 2017/8/22.
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {

        // 自定义类加载器
        ClassLoader myloader = new ClassLoader() {

            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {

                String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                InputStream is = getClass().getResourceAsStream(fileName);

                if (is == null) {
                    return super.loadClass(name);
                }

                try {
                    byte[] bytes = new byte[is.available()];
                    is.read(bytes);
                    return defineClass(name, bytes, 0, bytes.length);
                } catch (IOException e) {
                    throw new ClassNotFoundException();
                }
            }
        };

        // 使用ClassLoaderTest的类加载器加载本类
        Object obj1 = ClassLoaderTest.class.getClassLoader().loadClass("com.jd.test.classloader.ClassLoaderTest").newInstance();
        System.out.println(obj1.getClass());
        System.out.println(obj1 instanceof ClassLoaderTest);

        // 使用自定义类加载器加载本类
        Object obj2 = myloader.loadClass("com.jd.test.classloader.ClassLoaderTest").newInstance();
        System.out.println(obj2.getClass());
        System.out.println(obj2 instanceof ClassLoaderTest);
    }
}
