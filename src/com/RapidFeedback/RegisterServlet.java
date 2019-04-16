package com.RapidFeedback;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.*;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/Register")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter output = response.getWriter();
        Map<String, String> outputMap = new HashMap<String, String>();
        Gson gson = new Gson();
        
        String uEmail = request.getParameter("Email");
        if(isNewEmail(uEmail))
        {
        	User user = new User();
        	user.setUserEmail(uEmail);
        	user.setFName(request.getParameter("firstName")); 
        	user.setMName(request.getParameter("middleName"));
        	user.setLName(request.getParameter("lastName"));
        	user.setPassword(request.getParameter("Password"));
        	//revoke database to add new user
        	outputMap.put("registerResult", "true");
        	//之后安卓程序跳转到login页面，用户自行login。安卓再请求LoginServlet        	
        }
        else 
        {
        	outputMap.put("registerResult", "false");
        	outputMap.put("exception", "This Email has already been registered.");
        }
		
        output.write(gson.toJson(outputMap));
		
	}
	
	private boolean isNewEmail(String email)
	{
		//revoke the search email function in DB
		return true;
	}

}
