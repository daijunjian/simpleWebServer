package com.yc.servlet.contanier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.yc.tomcat.HttpServletRequest;
import com.yc.tomcat.HttpServletResponse;

public class ServerService implements Runnable {
	private Socket socket;
	private InputStream iis;
	private OutputStream oos;
	
	public ServerService(Socket socket){
		this.socket=socket;
	}

	public void run() {
		
		try {
			this.iis=this.socket.getInputStream();
			this.oos=this.socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		try {
		//创建请求对象
		ServletRequest  request=new HttpServletRequest(this.iis);
		request.parse();
		
		//创建响应对象
		ServletResponse response=new HttpServletResponse(request, this.oos);
		
		//定义一个处理器
		Processor processor=null;
		
		//判断uri是否以/serlvet/开头  是的 则表明请求是动态资源
		//工厂模式
		if(request.getUri()!=null && request.getUri().startsWith("/servlet/")){
			//System.out.println("来啦");
			processor=new DynamicProcessor();
		}else{
			//静态资源
			processor=new StaticProcessor();
		}
		
		//调用处理器方法
		//回调
		processor.process(request, response);
		
		
		//http协议是一个无状态，基于请求/响应
		this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}

}
