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
import com.RapidFeedback.Mark;
import com.RapidFeedback.MysqlFunction;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class GetMarkServlet
 */
@WebServlet("/GetMarkServlet")
public class GetMarkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetMarkServlet() {
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
		InsideFunction inside = new InsideFunction(dbFunction);
		
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
		String projectName = jsonReceive.getString("projectName");
		String studentID = jsonReceive.getString("studentID");
		
		ServletContext servletContext = this.getServletContext();
		
		boolean mark_ACK=false;
		//Mark mark = ;
		
		/*
		 * implementation
		 */
		
		
		//change mark to json string
		String markString = ;
		
		//construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("mark_ACK", mark_ACK);
		jsonSend.put("mark", markString);
		jsonSend.put("projectName", projectName);
		jsonSend.put("studentID", studentID);
		//send
		PrintWriter output = response.getWriter();
	 	output.print(jsonSend.toJSONString());
	}

}
