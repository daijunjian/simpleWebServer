package com.yc.servlet.contanier;

public class StaticProcessor implements Processor {

	public void process(ServletRequest request, ServletResponse response) {
		response.sendRedirect();

	}

}
