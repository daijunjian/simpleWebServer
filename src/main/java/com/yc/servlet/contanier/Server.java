package com.yc.servlet.contanier;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

	private int port=8080;
	
	public static void main(String[] arg) throws IOException{
		Server tomcat=new Server();
		tomcat.startServer();
	}
	
	public void startServer() throws IOException{
		ServerSocket ss=new ServerSocket(port);
		
		while(true){
			Socket s=ss.accept();
			Thread t=new Thread(new ServerService(s));
			t.start();
		}
	}
	
}
