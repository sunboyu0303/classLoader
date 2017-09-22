package com.jd.test.classloader;

import java.net.URL;

/**
 * Created by sunboyu on 2017/8/21.
 */
public class TestClassLoader {

    public static void main(String[] args) {

        /**
         * BootstrapClassloader
         *
         * 引导类加载器，又称启动类加载器，是最顶层的类加载器，主要用来加载Java核心类，
         *
         * 如rt.jar、resources.jar、charsets.jar等
         */
        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (URL url : urls) {
            System.out.println(url.toExternalForm());
        }

        /**
         * ExtClassloader
         *
         * 扩展类加载器，主要负责加载Java的扩展类库，
         *
         * 默认加载JAVA_HOME/jre/lib/ext/目下的所有jar包或者由java.ext.dirs系统属性指定的jar包。
         */
        System.out.println(System.getProperty("java.ext.dirs"));

        /**
         * AppClassloader
         *
         * 系统类加载器，又称应用加载器，本文说的SystemClassloader和APPClassloader是一个东西，
         *
         * 它负责在JVM启动时，加载来自在命令java中的-classpath或者java.class.path系统属性或者 CLASSPATH操作系统属性所指定的JAR类包和类路径。
         *
         * 调用ClassLoader.getSystemClassLoader()可以获取该类加载器。
         */
        System.out.println(System.getProperty("java.class.path"));

        System.out.println(ClassLoader.getSystemClassLoader());
        System.out.println(ClassLoader.getSystemClassLoader().getParent());
    }
}
