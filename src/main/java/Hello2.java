import java.io.PrintWriter;

import com.yc.servlet.contanier.HttpServlet;
import com.yc.servlet.contanier.ServletRequest;
import com.yc.servlet.contanier.ServletResponse;

//使用了适配器模式
public class Hello2 extends HttpServlet {

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
