package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
		String studentNumber = jsonReceive.getString("studentNumber");
		
		ServletContext servletContext = this.getServletContext();
		
		boolean mark_ACK=false;
		String markListString="";
		String username = inside.token2user(servletContext, token);
		try {
			int projectId=dbFunction.getProjectId(username, projectName);
			ArrayList<String> assessors = dbFunction.returnAssessors(projectId);
			ArrayList<Mark> markList = new ArrayList<Mark>();
			for(String lecturer:assessors) {
				int lecturerId=dbFunction.getLecturerId(lecturer);
				int studentID = dbFunction.ifStudentExists(projectId, studentNumber);
				
				Mark mark = dbFunction.returnMark(projectId, lecturerId, studentID);
				markList.add(mark);
			}
			
			markListString=JSON.toJSONString(markList);
			
			mark_ACK = true;
			//change mark to json string
			//markString = JSON.toJSONString(mark);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		
		
		//construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("mark_ACK", mark_ACK);
		jsonSend.put("markList", markListString);
		jsonSend.put("projectName", projectName);
		jsonSend.put("studentNumber", studentNumber);
		//send
		PrintWriter output = response.getWriter();
	 	output.print(jsonSend.toJSONString());
	}

}