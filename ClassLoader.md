类加载器虽然只用于实现类的加载动作，但是对于任意一个类，
都需要由加载它的类加载器和这个类本身共同确立其在Java虚拟机中的唯一性。

通俗的说，JVM中两个类是否“相等”，首先就必须是同一个类加载器加载的，
否则，即使这两个类来源于同一个Class文件，被同一个虚拟机加载，
只要类加载器不同，那么这两个类必定是不相等的。

双亲委派模型过程
某个特定的类加载器在接到加载类的请求时，首先将加载任务委托给父类加载器，
依次递归，如果父类加载器可以完成类加载任务，就成功返回；
只有父类加载器无法完成此加载任务时，才自己去加载。

使用双亲委派模型的好处：
Javz类随着它的类加载器一起具备了一种带有优先级的层次关系。
例如：
类java.lang.Object，它存在在rt.jar中，无论哪一个类加载器要加载这个类时，
最终都委派给处于模型最顶端的Bootstrap ClassLoader进行加载，因此，Object类
在程序的各种类加载器环境中都是同一个类。
相反，如果没有双亲委派模型而是由各个类加载器自行加载的话，如果用户编写了一个
java.lang.Object的同名类并放在ClassPath中，那系统中将会出现多个不同的Object类。
因此，如果开发者尝试编写一个与rt.jar类库中重名的Java类，可以正常编译，但是永远无法
被加载运行。

双亲委派模型的系统实现
在java.lang.ClassLoader的loadClass()方法中，先检查自己是否已经被加载过，若没有加载
则调用父类加载器的loadClass()方法，若父类加载器为空则默认使用启动类加载器作为父类加载器。
如果父加载失败，则抛出ClassNotFoundException异常后，再调用自己的findClass()方法进行加载。

    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                try {
                    if (parent != null) {
                        c = parent.loadClass(name, false);
                    } else {
                        c = findBootstrapClassOrNull(name);
                    }
                } catch (ClassNotFoundException e) {
                    // ClassNotFoundException thrown if class not found
                    // from the non-null parent class loader
                }
    
                if (c == null) {
                    // If still not found, then invoke findClass in order
                    // to find the class.
                    long t1 = System.nanoTime();
                    c = findClass(name);
    
                    // this is the defining class loader; record the stats
                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                    sun.misc.PerfCounter.getFindClasses().increment();
                }
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }