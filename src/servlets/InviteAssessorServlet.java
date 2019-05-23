package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.RapidFeedback.MysqlFunction;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class InviteAssessorServlet
 */
@WebServlet("/InviteAssessorServlet")
public class InviteAssessorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InviteAssessorServlet() {
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
		MysqlFunction dbFunction = new MysqlFunction();

		JSONObject jsonReceive;
		BufferedReader reader = request.getReader();
		String str, wholeString = "";
	    while((str = reader.readLine()) != null)
	    {
	        wholeString += str;  
	    }
	    System.out.println("Receive: " + wholeString);
	    jsonReceive = JSON.parseObject(wholeString);
	    
	  //get values from received JSONObject
		String token = jsonReceive.getString("token");
		//other arguments
		String projectName = jsonReceive.getString("projectName");
		String assessorEmail = jsonReceive.getString("assessorEmail");
		
		ServletContext servletContext = this.getServletContext();
		
		boolean invite_ACK = false;
		String emailWithName = "";
		//如果确认有此人邀请成功，返回他的邮箱+名字，格式为："emailAdress::firstName middleName lastName"
		
		/*
		 * 
		 */
		
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("invite_ACK", invite_ACK);
		jsonSend.put("emailWithName", emailWithName);//两种处理方法：用if判断invite_ACK，true则发送false则不发送；或无论true/false都发送emailWithName，默认为空字符串。
		
		//send
		PrintWriter output = response.getWriter();
	 	output.print(jsonSend.toJSONString());
	 	System.out.println("Send: "+jsonSend.toJSONString());
		
		
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MysqlFunction dbFunction = new MysqlFunction();

		JSONObject jsonReceive;
		BufferedReader reader = request.getReader();
		String str, wholeString = "";
	    while((str = reader.readLine()) != null)
	    {
	        wholeString += str;  
	    }
	    System.out.println("Receive: " + wholeString);
	    jsonReceive = JSON.parseObject(wholeString);
	    
	  //get values from received JSONObject
		String token = jsonReceive.getString("token");
		//other arguments
		String projectName = jsonReceive.getString("projectName");
		String assessorEmail = jsonReceive.getString("assessorEmail");
		
		ServletContext servletContext = this.getServletContext();
		
		boolean delete_ACK = false;
		
		/*
		 * 
		 */
	
		
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("delete_ACK", delete_ACK);
		
		//send
		PrintWriter output = response.getWriter();
	 	output.print(jsonSend.toJSONString());
	 	System.out.println("Send: "+jsonSend.toJSONString());
	}

}
