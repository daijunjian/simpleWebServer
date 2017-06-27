package com.yc.servlet.contanier;

import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class DynamicProcessor implements Processor {

	public void process(ServletRequest request, ServletResponse response) {
		//1取出 uri /servlet/servlet类名
		String uri=request.getUri();
		//2取出servletname
		String servletName=uri.substring(uri.lastIndexOf("/")+1);
		//3类加载 ->仍然要通过url地址来加载servlet字节码
		//URLClassLoader 类加载器
		//该类加载器用于从指向 JAR 文件和目录的 URL 的搜索路径加载类和资源
		URLClassLoader loader=null;
		
		URL url;
		Servlet servlet=null;
	//	System.out.println("11111111"+TomacatConstants.BASE_PATH);
		try {
			//  根据指定的 protocol 名称、host 名称和 file 名称创建 URL。
			//类 URL 代表一个统一资源定位符，它是指向互联网“资源”的指针。
			url = new URL("file",null, TomacatConstants.BASE_PATH);
			URL [] urls=new URL[]{url};
			//创建一个类加载器，这个加载器从 项目下的weabapps中读取servlet
			loader=new URLClassLoader(urls);
			//加载servletname
			//使用指定的二进制名称来加载类。此方法使用与 loadClass(String, boolean) 方法相同的方式搜索类
			//搜索该路径下的该资源
			Class c=loader.loadClass(servletName);
			//4通过字节码反射成一个对象
			 servlet=(Servlet) c.newInstance(); //创建此 Class 对象所表示的类的一个新实例。
			// System.out.println( servletName  );
		} catch (Exception e) {
			//e.printStackTrace();
			response.sendRedirect();
			return;
		}
		try {
		//控制生命周期 => init() ->service();
			if(servlet!=null && servlet instanceof Servlet){
				servlet.init();
				servlet.service(request, response);
			}
		}catch (Exception e) {
			String bodyentity=e.getMessage();
			bodyentity=gen500(bodyentity.getBytes().length)+bodyentity;
			PrintWriter out=response.getWriter();
			out.println(bodyentity);
			out.flush();
		}
		
	}
	
	//产生500错误
	private String gen500(long bodylength) {
		String protocal500="HTTP/1.1 500 Server Internal Error\r\nContent-Type: text/html\r\nContent-Length: "+bodylength+"\r\n\r\n";
		return protocal500;
	}

}
