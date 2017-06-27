package com.yc.tomcat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.imageio.stream.FileImageInputStream;

import com.yc.servlet.contanier.ServletRequest;
import com.yc.servlet.contanier.ServletResponse;

//响应的功能
//从request中取出uri
//判断是否在本地存在这个uri指代的文件
//     没有  404
//    有
// 以输入流 读取这个文件
//以输出流将文件写到客户端，要加入响应的协议

public class HttpServletResponse implements ServletResponse {
													//File.separator:名称分隔符 在各个系统下的表现形式不同 这里是  \	
	private String wbroot=System.getProperty("user.dir")+File.separator+"src\\main\\webapp";
	
	private ServletRequest request;
	private OutputStream output;
	private PrintWriter writer;
	
	public HttpServletResponse(ServletRequest request,OutputStream output){
		this.request=request;
		this.output=output;
		
	}
	
	
	
	//从request中取出uri
	//判断是否在本地存在这个uri指代的文件
//	     没有  404
//	    有
	// 以输入流 读取这个文件
	//以输出流将文件写到客户端，要加入响应的协议
	public void sendRedirect(){
		//uri=/index.html
		String uri=request.getUri();
		
		//判断服务下是否有这个页面
		File f=new File(wbroot,uri);  //根据 wbroot 路径名字符串和 uri 路径名字符串创建一个新 File 实例
		String responseprotocal=null;
		if(!f.exists()){
			String httpBody=readFile(new File(wbroot,"404.html"));
			responseprotocal=gen404(httpBody.getBytes().length);
			responseprotocal+=httpBody;
		}else{
			String httpBody=readFile(f);
			//System.out.println(File.separator);
			
			responseprotocal=gen200(httpBody.getBytes().length);
			responseprotocal+=httpBody;
		}
		
		try {
			//responseprotocal.getBytes()
			//使用指定的字符集将此 String 编码为 byte 序列，并将结果存储到一个新的 byte 数组中。 
			output.write(responseprotocal.getBytes());  //以流的方式写出
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}


	private String gen404(long bodylength) {
		String protocal404="HTTP/1.1 404 File Not Found\r\nContent-Type: text/html\r\nContent-Length: "+bodylength+"\r\n\r\n";
		return protocal404;
	}


	private String gen200(long bodylength) {
		String protocal200="HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: "+bodylength+"\r\n\r\n";
		return protocal200;
	}


	private String readFile(File f) {
		FileImageInputStream fis=null; //从 File 或 RandomAccessFile 中获取输入的 ImageInputStream 的实现
		StringBuffer sb=new StringBuffer();
		
		try {
			fis=new FileImageInputStream(f);//构造一个将从给定 File 进行读取的 FileImageInputStream。
			byte[] bs=new byte[1024];
			int length=-1;
			while((length=fis.read(bs,0,bs.length))!=-1){
				sb.append(new String(bs,0,length));//通过使用平台的默认字符集解码指定的 byte 子数组，构造一个新的 String。
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return sb.toString();
	}

	public PrintWriter getWriter() {
		this.writer=new PrintWriter(output);
		return this.writer;
	}
	
}
