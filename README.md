# classLoader

Java内存区域
    
    1. 运行时数据区域
        Java虚拟机在执行Java程序的过程中会把它所管理的内存划分为若干个不同的数据区域。这些区域都有各自的用途，
        以及创建和销毁的时间，有的区域随着虚拟机进程的启动而存在，有些区域则是依赖用户线程的启动和结束而建立和销毁。
    1.1 程序计数器
        程序计数器是一块较小的内存空间，它的作用可以看作是当前线程所执行的字节码的行号指示器。字节码解释器工作时
        就是通过改变这个计数器的值类选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等基础功能
        都需要依赖这个计数器来完成。
        由于Java虚拟机的多线程是通过线程轮流切换并分配处理器执行时间的方式来实现的，在任何一个确定的时刻，一个处理器
        只会执行一条线程中的指令。为了线程切换后能恢复到正确的执行位置，每条线程都需要有一个独立的程序计数器，各条线程
        之间的计数器互不影响，独立存储，这类内存区域为"线程私有"的内存。
    1.2 Java虚拟机栈
        与程序计数器一样，Java虚拟机栈也是线程私有的，它的生命周期与线程相同。虚拟机栈描述的是Java方法执行的内存模型：
        每个方法被执行的时候都会同时创建一个栈帧用于存储局部变量表、操作栈、动态链接、方法出口等信息。每一个方法被调用
        直至执行完成的过程，就对应着一个栈帧在虚拟机栈中从入栈到出栈的过程。
        局部变量表存放了编译期可知的各种基本数据类型（boolean、byte、char、short、int、float、long、double）、对象引用
        （reference类型，它不等同于对象本身，根据不同的虚拟机实现，它可能是一个指向对象起始地址的引用指针，也可能指向一个
        代表对象的句柄或者其他与此对象相关的位置）和returnAddress类型（指向了一条字节码指令的地址）。
        64位长度的long和double类型的数据会占用2个局部变量空间，其余的数据类型只占用1个。局部变量表所需的内存空间在编译期间
        完成分配，当进入一个方法时，这个方法需要在帧中分配多大的局部变量空间是完全确定的，在方法运行期间不会改变局部变量表
        的大小。
    1.3 本地方法栈
        本地方法栈与虚拟机栈所发挥的作用是非常相似的，区别是虚拟机栈为虚拟机执行java方法（也就是字节码）服务，而本地方法
        栈则是为虚拟机使用到的Native方法服务。
    1.4 Java堆
        Java堆是Java虚拟机所管理的内存中最大的一块。Java堆是被所有线程共享的一块内存区域，在虚拟机启动时创建。此内存区域的
        唯一目的就是存放对象实例。所有的对象实例都在这里分配内存。
        java堆是垃圾收集器管理的主要区域，因此很多时候也被称为"GC堆"。从内存回收的角度看，由于现在收集器基本都是采用的分代
        收集算法，所以Java堆中还可以细分为：新生代和老年代；再细分一点的话有Eden空间、From Survivor空间、To Survivor空间等。
        如果从内存分配的角度看，线程共享的Java堆中划分出多个线程私有的分配缓冲区（TLAB）。无论如何划分，都与存放内容无关，无论
        哪个区域，存储的都仍然是对象实例，进一步划分的目的是为了更好地回收内存，或者更快地分配内存。
    1.5 方法区
        方法区与Java堆一样，是各个线程共享的内存区域，它用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码
        等数据。
    1.6 运行时常量池
        运行时常量池是方法区的一部分。Class文件中除了有类的版本、字段、方法、接口等描述信息外，还有一项信息是常量池，用于存放
        编译期生成的各种字面量和符号引用，这部分内容将在类加载后存放到方法区的运行时常量池中。
        运行时常量池相对于Class文件常量池的另外一个重要特征是具备动态性，Java语言并不要求常量一定只能在编译期产生，也就是并非
        预置入Class文件常量池的内容才能进入方法区运行时常量池，运行期间也可能将新的常量放入池中，这种特性被开发人员利用得比较
        多的便是String类的intern()方法。
        既然运行时常量池是方法区的一部分，自然会受到方法区内存的限制，当常量池无法再申请到内存时会抛出OutOfMemoryError异常。
    1.7 直接内存
        直接内存并不是虚拟机运行时数据区的一部分，也不是Java虚拟机规范中定义的内存区域，但是这部分内存也被频繁地使用，而且也可
        能导致OutOfMemoryError异常出现。
    2 对象访问
        对象访问在Java语言中无处不在，是最普通的程序行为，但即使是最简单的访问，也会涉及Java栈、Java堆、方法区这三个最重要内存
        区域之间的关联关系。
        Object obj = new Object();
        Object obj 这部分的语义将会反映到Java栈的本地变量表中，作为一个reference类型数据出现。而 new Object() 这部分的语义
        将会反映到Java堆中，形成一块存储了Object类型所有实例数据值的结构化内存，根据具体类型以及虚拟机实现的内存布局的不同，
        这块内存的长度是不固定的。另外，在Java堆中还必须包含能查找到此对象类型数据（如对象类型、父类、实现的接口、方法等）的
        地址信息，这些类型数据则存储在方法区中。
        由于reference类型在Java虚拟机规范里面只规定了一个指向对象的引用，并没有定义这个引用应该通过哪种方式去定位，以及访问
        到Java堆中的对象的具体位置，因此，不同虚拟机实现的对象访问方式会有所不同，主流的访问方式有两种：使用句柄和直接指针。
        
        使用句柄访问方式，Java堆中将会划分出一块内存来作为句柄池，reference中存储的就是对象的句柄地址，而句柄中包含了对象
        实例数据和类型数据各自的具体地址信息。
        使用直接指针访问方式，Java堆对象的布局中就必须考虑如何放置访问类型数据的相关信息，reference中直接存储的就是对象地址。
        
        
        
        
        