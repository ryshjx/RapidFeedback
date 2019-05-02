package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.RapidFeedback.InsideFunction;
import com.RapidFeedback.MysqlFunction;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class UpdateProject_About_Servlet
 */
@WebServlet("/UpdateProject_About_Servlet")
public class UpdateProject_About_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateProject_About_Servlet() {
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

		//		
		MysqlFunction dbFunction = new MysqlFunction();
		
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
		String subjectName = jsonReceive.getString("subjectName");
		String subjectCode = jsonReceive.getString("subjectCode");
		String description = jsonReceive.getString("description");
		
		
		ServletContext servletContext = this.getServletContext();
				
		boolean updateProject_ACK = false;
	    //Mention:
		//call the SQL method to save the 'About' page
		//return the '0' or <projectID>
		try {
			updateProject_ACK = projectP1(dbFunction, servletContext, token, projectName, subjectCode, subjectName, description);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		
		//construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateProject_ACK", updateProject_ACK);
		
		//send
		PrintWriter output = response.getWriter();
	 	output.print(jsonSend.toJSONString());
	 	System.out.println("Send: "+jsonSend.toJSONString());
		
	}
	
	//if success, return projectID, otherwise return 0.
	public boolean projectP1(MysqlFunction dbFunction, ServletContext servletContext, String token, String projectName, String subjectCode, String subjectName, String description) throws SQLException{
		InsideFunction in = new InsideFunction();
		String username = in.token2user(servletContext, token);
		boolean result = false;
		int check = dbFunction.getProjectId(username, projectName);
		
		if(username == null || username.isEmpty()) {
			return result;
		}
		
		if(check == 0) {
			if(dbFunction.createProject(username, projectName, subjectCode, subjectName, description)>0) {
				result = true;
			}
		}else if(check > 0 && dbFunction.updateProjectInfo(username, projectName, subjectCode, subjectName, description)) {
			result = true;
		}
		
		return result;
	}

}
