package com.RapidFeedback;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		InsideFunction function = new InsideFunction();

		JSONObject jsonReceive;
		BufferedReader reader = request.getReader();
		String str, wholeString = "";
	    while((str = reader.readLine()) != null)
	    {
	        wholeString += str;  
	    }
	    System.out.println("Receive: " + wholeString);
	    jsonReceive = JSON.parseObject(wholeString);
		String email = jsonReceive.getString("email");
		String password = jsonReceive.getString("password");
		String firstName = jsonReceive.getString("firstName");
		String middleName = jsonReceive.getString("middleName");
		String lastName = jsonReceive.getString("lastName");
	    boolean register_ACK;
		try {
			 	register_ACK = function.Register(email, password, firstName, middleName, lastName);
			 	JSONObject jsonSend = new JSONObject();
			 	jsonSend.put("register_ACK", register_ACK);
			 	PrintWriter output = response.getWriter();
			 	output.print(jsonSend.toJSONString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	   
	}

}
