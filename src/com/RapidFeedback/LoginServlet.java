package com.RapidFeedback;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import com.google.gson.*;



/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final MysqlFunction ServletContext = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        
        String uEmail = request.getParameter("Email");
		String password = request.getParameter("Password");
		Gson gson = new Gson();
		PrintWriter output = response.getWriter();
		Map<String, String> outputMap = new HashMap<String, String>();
		boolean userValid = verifyLogin(uEmail, password);
		String token = request.getSession().getId();
		
        if(userValid)
        {
        	outputMap.put("loginResult", "true");
        	outputMap.put("token", token);
        	
			ServletContext servletContext = this.getServletContext();
        	servletContext.setAttribute(token, uEmail);
        }
        else
        {
        	outputMap.put("loginResult", "false");
        }
        
        // convert map to JSON String and write it to response.
        output.write(gson.toJson(outputMap));
	}
	
	private boolean verifyLogin(String uEmail, String password)
	{
		//this is a function which revokes Dinghao's login verify function. if true, then:
		ServletContext servletContext = this.getServletContext();
		Enumeration e = servletContext.getAttributeNames();
		while(e.hasMoreElements()) 
		{
			String token = (String)e.nextElement();
			if(servletContext.getAttribute(token)==uEmail)
			{
				servletContext.removeAttribute(token);
			}
		}
		return true;
		//if verify false, return false.
	}

}
