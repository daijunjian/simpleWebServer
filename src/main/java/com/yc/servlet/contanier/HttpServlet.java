package com.yc.servlet.contanier;
//适配器 隐藏方法
public abstract class HttpServlet implements Servlet {

	public void init() {
		

	}

	public void service(ServletRequest request, ServletResponse response) {
		//判断method是什么，调用doGet()/dopPost()
		if(request.getMethod().equals(TomacatConstants.REQUEST_METHOD_GET)){
			doGet(request,response);
		}else if(request.getMethod().equals(TomacatConstants.REQUEST_METHOD_POST)){
			doPost(request,response);
		}

	}

	public void doGet(ServletRequest request, ServletResponse response) {
		

	}

	public void doPost(ServletRequest request, ServletResponse response) {

	}

}
