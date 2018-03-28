package com.eastrobot.robotdev.nuskin.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

public class URLServlet extends HttpServlet {
	@Override
	public void service(ServletRequest request, ServletResponse response)throws ServletException, IOException {
		
		
		String host = request.getParameter("host");
		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String des = request.getParameter("des");
		String img = request.getParameter("img");
		
		title = URLEncoder.encode(title,"UTF-8");
		
		String url = host+"?id="+id+"&title="+title+"&des="+des+"&img="+img;
		PrintWriter out = response.getWriter();
		out.print(url);
	}
}
