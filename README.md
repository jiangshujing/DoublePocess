# DoublePocess
为了保护我们的app进程常驻，使用双进程守护

为了防止我们的程序不那么容易就被别的安全软件杀死，或者在内存不足的情况下被系统给kill 掉

方案：使用两个服务，分别守护彼此
           如果A挂了，B将其开启，如果B挂了，A将其开启
注意：两个服务不能在同一个进程中，那样一旦被干掉，两个会同时被干掉
           那样就使用aidl（远程服务）

需要确定一个服务为远程服务，在清淡文件中设置属性process
<service android:name=".service.RemoteCastielService"
    android:process=":remote”/>

Android声明文件中的android:process属性的作用：
一般情况下一个服务没有自己独立的进程，它一般是作为一个线程运行于它所在的应用的进程中。但是也有例外，Android声明文件中的android:process属性却可以为任意组件包括应用指定进程，换句话说，通过在声明文件中设置android:process属性,我们可以让组件（例如Activity, Service等）和应用(Application)创建并运行于我们指定的进程中
属性值含义：
：（冒号） — 这个前缀将把这个名字附加到你的包所运行的标准进程名字的后面作为新的进程名称，例如：一个应用的包名为com.jsj.doubleprocess, 则本例中服务将运行的新进程的名称为com.jsj.doubleprocess:remote
如果被设置的进程名是以一个冒号开头的，则这个新的进程对于这个应用来说是私有的，如果不使用冒号则这个服务将运行在一个以这个名字命名的全局的进程中，当然前提是它有相应的权限
字：这里选择”remote”这个名字是随意的，例如一个应用运行在进程com.jsj.doubleprocess中，android:process属性设置为
com.jsj.newprocess,则新的进程名字为com.jsj.newprocess
