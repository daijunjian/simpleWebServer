package com.yc.tomcat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;

import com.yc.servlet.contanier.ServletRequest;
import com.yc.servlet.contanier.TomacatConstants;

//封装请求
//解析请求
//取出请求头中的资源名

public class HttpServletRequest implements ServletRequest {
	//请求资源地址   /index.html
	private String uri;
	//输入流
	private InputStream input;
	
	private String method;
	
	
	private Map<String ,String> parameter=new Hashtable<String, String>();
	
	//构造方法
	public HttpServletRequest(InputStream iis){
		this.input=iis;
	}
	
	//解析协议   ：最重要的是url
	public void parse(){
		//1从Input 中读出所有的内容（http请求协议=》 protocal)
		String protocal=null;
		StringBuffer sb=new StringBuffer(1024*10);  //指定初始容量的字符串缓冲区
		byte[] bs=new byte[1024*10];
		int length=-1;
		//从流中取protocal
		try {
			length=this.input.read(bs);
		} catch (IOException e) {
			e.printStackTrace();
			length=-1;
		}
		
		for(int j=0;j<length;j++){
			sb.append((char)bs[j]);  //循环字节流将其添加到缓存区间
		}
		
		protocal=sb.toString(); //将字符串缓冲区转化为String
		//2从protocal中取出  uri
		this.uri=parseUri(protocal);  //取到要访问的页面地址   /index.html
		//解析method
		this.method=parseMehtod(protocal);
		//解析参数
		this.parameter=parseParameter(protocal);
		
	}
	
	/**
	 * 解析参数
	 * GET/servlet/Hello?uname=zy&age=10 HTTP/1.1
	 * POST/servlet/Hello
	 * 
	 * unme=zy&age=20
	 */
	private Map<String, String> parseParameter(String protocal) {
		String parameterString=null;
		//取出参数部分 存到 paramenterString
		if(this.method.equals(TomacatConstants.REQUEST_METHOD_GET)){
			//从protocal中取出第一个空格到第二个空格之间的字符串
			String[] ss=protocal.split(" ");
			int questionindex=ss[1].indexOf("?");
			if(questionindex>0){
				parameterString=ss[1].substring(questionindex+1);
			}
			
		}else if(this.method.equals(TomacatConstants.REQUEST_METHOD_POST)){
			parameterString=protocal.substring(protocal.indexOf("\r\n\r\n")+4);
			
		}
		
		if(parameterString!=null && parameterString.length()>0){
			String[] keyvalues=parameterString.split("&");
	/*		if(parameterString.indexOf("&")>0){
				keyvalues=parameterString.split("&");
			}else{
				keyvalues=new String[]{parameterString};
			}*/
			
			for(int i=0;i<keyvalues.length;i++){
				String keyvalue=keyvalues[i];
				String[] kv=keyvalue.split("=");
				this.parameter.put(kv[0], kv[1]);
			}
		}
		
		return this.parameter;
	}
	/**
	 * 解析method
	 * @param protocal： GET /index.html?name HTTP/1.1
	 * @return
	 */
	private String parseMehtod(String protocal) {
		String method=protocal.substring(0,protocal.indexOf(" "));
		return method;
	}

	private String parseUri(String protocal){
		
		//todo:从protocal取出  uri
		String[] ss=protocal.split(" ");
		//校验操作
		int code=validateProtocal(ss);
		if(code!=200){
			return null;
		}
		if(ss[1].indexOf("?")>0){
			this.uri=ss[1].substring(0,ss[1].indexOf("?"));
		}else{
			this.uri=ss[1];
		}
		//System.out.println(ss+"=ss");
		return this.uri;
	}
	
	private int validateProtocal(String[] ss) {
		
		return 200;
	}

	public String getUri(){
		return uri;
	}

	public String getParameter(String key) {
		// TODO Auto-generated method stub
		return this.parameter.get(key);
	}


	public String getMethod() {
		// TODO Auto-generated method stub
		return this.method;
	}
	
}
