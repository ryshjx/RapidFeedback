package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.ProjectInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
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
		String username = jsonReceive.getString("username");
		String password = jsonReceive.getString("password");
		
		ServletContext servletContext = this.getServletContext();
		JSONObject jsonSend = new JSONObject();
		
	    int login_ACK;
		try {
			 	login_ACK = dbFunction.logIn(username, password);
			
			 	if(login_ACK > 0) {
			 		delOldToken(servletContext, username);
			 		//save new token-username pair to the server.
			 		String token = newToken(request, username);
			 		servletContext.setAttribute(token, username);
			 		ProjectInfo[] projectList = getProjectList(dbFunction, username);
			 		String projectListString = JSON.toJSONString(projectList);
			 		jsonSend.put("projectList", projectListString);
			 		jsonSend.put("login_ACK", login_ACK);
			 		jsonSend.put("token", token);
			 	}
			 	else{
			 		jsonSend.put("login_ACK", login_ACK);
			 	}
			 				 	
			 	PrintWriter output = response.getWriter();
			 	output.print(jsonSend.toJSONString());
			 	System.out.println("Send: "+jsonSend.toJSONString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	   
	}
	
	private void delOldToken(ServletContext servletContext, String userName) {
		Enumeration<String> e = servletContext.getAttributeNames();
		while(e.hasMoreElements()) 
		{
			String token = (String)e.nextElement();
			if(servletContext.getAttribute(token)==userName)
			{
				servletContext.removeAttribute(token);
			}
		}
	}
	
	private String newToken(HttpServletRequest request, String userName) {
		String token = request.getSession().getId();
		return token;
	}
	
	private ProjectInfo[] getProjectList(MysqlFunction db, String userName) throws SQLException{
		List<Integer> pIDs = db.queryProjects(userName);
		ProjectInfo[] projectList = new ProjectInfo[pIDs.size()];
		for(int i = 0; i<pIDs.size(); i++) {
			projectList[i]=db.returnProjectInfo(pIDs.get(i));
		}
		return projectList;
	}

}
