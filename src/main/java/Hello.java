import java.io.PrintWriter;

import com.yc.servlet.contanier.Servlet;
import com.yc.servlet.contanier.ServletRequest;
import com.yc.servlet.contanier.ServletResponse;
import com.yc.servlet.contanier.TomacatConstants;


public class Hello implements Servlet {

	public Hello(){
		System.out.println("constructor");
	}

	public void init() {
		System.out.println("init");

	}

	public void service(ServletRequest request, ServletResponse response) {
	//	System.out.println("service"+request.getMethod());
		//
		if(request.getMethod().equals(TomacatConstants.REQUEST_METHOD_GET)){
			doGet(request,response);
		}else if(request.getMethod().equals(TomacatConstants.REQUEST_METHOD_POST)){
			doPost(request,response);
		}

	}

	public void doGet(ServletRequest request, ServletResponse response) {
		doPost(request,response);

	}

	public void doPost(ServletRequest request, ServletResponse response) {
		//System.out.println(request.getParameter("uname")+"888888888");
		 String uname=request.getParameter("uname");
		int age=Integer.parseInt(request.getParameter("age"));
		String body="<html><head><title>hello world</title></head><body>uname:"+uname+"</br>age:"+age+"</body></thml";
		String protocal="HTTP/1.1 200 OK\r\nContent-Type: text/html;charset=utf-8;\r\nContent-Length: "+body.getBytes().length+"\r\n\r\n"+body;
		
		PrintWriter out=response.getWriter();
		out.println(protocal);
		out.flush();
	}

}
