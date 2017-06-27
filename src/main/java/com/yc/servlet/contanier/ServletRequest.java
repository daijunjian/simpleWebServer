package com.yc.servlet.contanier;
//包装http请求
public interface ServletRequest {
	//取请求的参数
	//key:根据键取值
	//有则返回值 ，没有则返回null
	public String getParameter(String key);
	
	
	//HTTP:  GET /servlet/Hello?name=zy HTTP/1.1
	//       POST/servlet、Hello HTTP/1.1
	//      
	//        name=zy
	//解析请求  ：  1解析  uri  2解析参数 3解析出请求的方式  get/post
	public void parse();
	
	//获取解析出来的uri地址
	public String getUri();
	
	//获取请求的方法
	public String getMethod();
	
}
