项目是一个简单的http、servlet服务器，转载于http://blog.csdn.net/young_so_nice/article/details/49966619。

一，项目分析： 
1.v1 是一个http服务器. 
2.v2 是一个servlet容器, 可以提供servlet的服务. => 动态load servlet字节码，并运行它（ 按生命周期）. 
3.servlet容器它来控制servlet的生命周期. 
4.Servlet类必须要实现一个接口 Servlet , 提供所有的Servlet都要有的方法( 生命周期) 
5.对于要处理的资源有两种: 静态资源/动态资源. 定义一个接口，写两个实现. 
动态资源: http://localhost:8888/servlet/hello

        GET  /servlet/hello HTTP/1.1

        静态资源:     http://localhost:8888/index.html

        GET /index.html HTTP/1.1

=>   将这种处理定义成一个接口  Processor  (  process() )  ->   StaticProcessor
                                         ->   DynamicProcessor

Servlet运行;
 第一次访问: 构造方法 ->  init()  ->  service()  ->   doGet()/doPost()
 第二次访问:                    ->  service()  ->   doGet()/doPost()

get
http://localhost:8080/servlet/Hi?name=zy&age=20 

GET /servlet/Hi?name=zy&age=20


功能: 
1. 等待http请求, 接收请求，做一些解析  ->   uri  ( 静态资源/动态资源)
2. 解析http请求, 构造成一个  HttpServletRequest对象, HttpServletResponse对象. 
3. 判断请求的资源的类型静态的资源/动态的资源  , 静态的资源  ->   StaticProcessor类
                                               动态资源    ->   DynamicProcessor类,    必须要有  Request和Response对象
4. 动态加载Servlet的字节码，并调用service()  ->  判断请求的方法, 调用对应的  Servlet中的doGet()/doPost()
二，具体代码分析： 
接口： 
1，Servlet接口：作用 ：规范servlet生命周期； 
带有init()，service(),doGet(),doPost()方法；

    2，ServletRequest接口：作用：包装http请求
        parse()：解析请求=>1解析  uri  2解析参数 3解析出请求的方式  get/post
        getParameter()：取请求的参数
        getUri():获取解析出来的uri地址
        getMethod():获取请求方法

    3，ServletResponse接口：
        getWriter():获取输出流的方法。
        sendRedirect（）：重定向

    4，Processor接口：运用了工厂模式，即有多个实现类，根据不同的条件
        调用实现类。
        process（）资源处理器：处理静态或动态资源

实现类：
    1，HttpServletRequest类：
        HttpServletRequest实现了ServletRequest接口
            A，构造方法，参数：输入流

            B，parse()解析协议：
                1，指定初始容量的字符串缓冲区StringBuffer sb
                2,创建字节流 bute[]
                3,从输入流中将数据读到字节流里
                4，循环字节流将其添加到缓存区间
                5，将字符串缓冲区转化为String protocal
                6，取到要访问的页面地址parseUri():
                    A,根据协议，通过split(空格)分割字符串将其存在一个
                        字符串数组内。
                    B，判断字符是否为空，不为空则返回200
                    C,知道ss字符数组中第二个就是地址和参数通过indexof（?)判断是否带有参数
                    如果有参数则需要截取，如果没有则直接返回ss[1];

                7,取到method:parseMethod():
                    通过substring直接截取protocal；

                8，解析参数parseParameter():
                    GET/servlet/Hello?uname=zy&age=10 HTTP/1.1

                    POST/servlet/Hello

                    unme=zy&age=20
                    上面是参数的两种类型：GET  POST
                    A,判断为那个方法：
                        GET方法：
                            1，通过split(空格)分割protocal
                            并将其存到字符数组ss中
                            2，取到数组的第二位即路径参数部分，通过indexOf("?")返回一个索引，
                            再通过substring截取到参数部分
                        POST方法：
                            直接通过截取获取参数。

                    B，将参数通过split("&"),拆分，循环数组
                    取到键值，并将其存入map.

    2，HttpServletResponse类：  响应的功能
        HttpServletResponse实现了ServletResponse接口：
            A,构造方法：参数： request ，输出流
            B，以输出流将文件写到客户端，加入响应协议sendRedirect（）
                1，从request中取出要访问的地址（uri)
                2,根据 wbroot 路径名字符串和 uri 路径名字符串创建一个新 File 实例 
                3,如果文件不存在，则返回404错误
                    a,通过readFile（File）取到404页面内容
                        1，通过FileImageInputStream构造一个将从给定 File 进行读取的 FileImageInputStream。
                        2，通过byte[]从流中将数据读取出来，并且存到一个StringBusffer中
                        3，将StringBuffer转化为字符串返回

                4，文件存在则返回页面
                5，将该字符串，转化为字节数组，以流的形式，写出

    3，HttpServlet类：
        HttpServlet实现了Servlet接口：
            这里是对接口，进行了空实现，采用适配器的方式，隐藏方法，后面的类继承它就可以了。
            在其service（）中还是实现了判断了，是通过什么请求的 GET/POST

    4，StaticProcessor类：
        StaticProcessor实现了Processor接口：
            它是用来实现加载静态资源的。
            process（）就是返回响应。

    5，DynamicProcessor类：
        DynamicProcessor实现了Processor接口：
            它是用来加载动态资源的。
            process（）参数 ServletRequest ServletResponse:
                A,取出要访问的地址（uri）通过request
                B，从uri中截取到文件名
                C，创建一个RUL,并将初始要搜索的资源类型，（这里是file）和
                初始路径赋给他。
                D，将URL转为URL[]
                E,创建URLClassLoader并将urls赋给他，再通过
                 .loadClass(servletName)搜索该路径下的该资源
                 注意：这里用了Class泛型，再通过.newInstance(); 
                   创建此 Class 对象所表示的类的一个新实例。
                    并强转为Servlet类型

                F.在通过方法控制Servlet的生命周期。
                    servlet.init();
                    servlet.service(request, response);

    6，服务器运行类ServerService：
        ServerService实现了Runnable接口：
            A，构造方法：参数 Scoket.
            B,run():
                1,取到流
                2，创建请求对象，request
                    调用request.parse();  //解析协议
                3，创建响应对象
                4，定义一个处理器    Processor

                5，判断资源类型： 这里即运用了工厂模式
                    uri是否以/serlvet/开头  是的 则表明请求是动态资源
                    否则静态资源
                    创建对应的处理对象

                6，以回调的形式 调用处理器方法
                    processor.process(request, response);

服务器：
    HttpTomcat类：
        startServer（）：
            1，创建服务器和分配端口号
            2，设置死循环，不断接受来自客户的消息
            3，将ServerService设置到线程并启动线程
            4，在主方法中启动服务。


测试：
    Hello2类
        继承HttpServlet类，实现其doGet/doPost,
        这里即使用了适配器模式。
