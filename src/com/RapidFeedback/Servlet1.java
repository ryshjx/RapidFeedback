package com.RapidFeedback;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class SecondServlet
 */
@WebServlet("/Servlet1")
public class Servlet1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
     public Servlet1() {
		// TODO Auto-generated constructor stub
	
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DataInputStream input = new DataInputStream(request.getInputStream());
		JSONObject jsonReceive;
		try {
			jsonReceive = JSON.parseObject(input.readUTF());
			System.out.println(jsonReceive.toJSONString());
			processReceive(jsonReceive, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sendBack()
	{
		
	}
	
	private void processReceive(JSONObject receive, HttpServletResponse response) throws Exception
	{
		JSONObject jsonSend= new JSONObject();
		InsideFunction function = new InsideFunction();
		String command = receive.getString("command");
		switch(command)
		{
		case "register":
			String email = receive.getString("email");
			String password = receive.getString("password");
			String firstName = receive.getString("firstName");
			String middleName = receive.getString("middleName");
			String lastName = receive.getString("lastName");
			boolean register_ACK = function.Register(email, password, firstName, middleName, lastName);
			jsonSend.put("command", "register_ACK");
			jsonSend.put("register_ACK", register_ACK);
			break;
		case "login":
			String username = receive.getString("username");
			String password_login = receive.getString("password");
			int login_ACK = function.Login(username, password_login);
			jsonSend.put("command", "login_ACK");
			jsonSend.put("login_ACK", login_ACK);
			break;
		default:
			;
		}
		
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		System.out.println(jsonSend.toJSONString());
		output.writeUTF(jsonSend.toJSONString());
		output.flush();
		output.close();
	}

}
