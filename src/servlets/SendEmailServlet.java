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

import com.RapidFeedback.InsideFunction;
import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.SendMail;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class SendEmailServlet
 */
@WebServlet("/SendEmailServlet")
public class SendEmailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendEmailServlet() {
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
		//MysqlFunction dbFunction = new MysqlFunction();
		
		//get JSONObject from request
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
		String studentEmail = jsonReceive.getString("studentEmail");
		
		
		
		ServletContext servletContext = this.getServletContext();
		
		boolean sendMail_ACK = false;
		
		/*
		 * add operation to send mail and get ACK.
		 */
		sendMail_ACK = sendEmail(token, servletContext, projectName, studentEmail);
		
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("sendMail_ACK", sendMail_ACK);
		
		//send
		PrintWriter output = response.getWriter();
	 	output.print(jsonSend.toJSONString());
	 	System.out.println("Send: "+jsonSend.toJSONString());
	}
	
	public boolean sendEmail(String token, ServletContext servletContext, String projectName, String studentEmail) {
		boolean result = false;
		MysqlFunction dbFunction = new MysqlFunction();
		InsideFunction inside = new InsideFunction(dbFunction);
		SendMail send = new SendMail();
		String subject = projectName + " Presentation Result for " + studentEmail;
		String userEmail = inside.token2user(servletContext, token);
		String host = "smtp.gmail.com";
		String user = "feedbackrapid@gmail.com";
		String pwd = "gkgkbzzbavwowfbh";
		String affix = "/file/Assignment1.pdf";
		
		send.setAddress(userEmail, studentEmail, subject);
		send.setAffix(affix, subject);
		result=send.send(host, user, pwd);
		return result;
	}

}
