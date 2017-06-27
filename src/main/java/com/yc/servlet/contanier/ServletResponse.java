package com.yc.servlet.contanier;

import java.io.PrintWriter;

//响应接口
public interface ServletResponse {
	//获取输出流的方法
	public PrintWriter getWriter();
	
	//重定向
	//区别： 与标准j2ee的区别，j2ee是带参数的
	//     我们的  uri地址是从request中取出来的
	public void sendRedirect();
	
	
}
