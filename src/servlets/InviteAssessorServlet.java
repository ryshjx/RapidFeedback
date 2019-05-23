package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.RapidFeedback.InsideFunction;
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
		HashMap<String, Integer> ids = getIDs(servletContext, token, assessorEmail, projectName, dbFunction);
		
		if(ids.size()==2) {
			try {
				invite_ACK = dbFunction.addOtherAssessor(ids.get("assessorID"), ids.get("projectID"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		JSONObject jsonSend = new JSONObject();
		jsonSend.put("invite_ACK", invite_ACK);
		jsonSend.put("assessorEmail", assessorEmail);
		
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
		
		HashMap<String, Integer> ids = getIDs(servletContext, token, assessorEmail, projectName, dbFunction);
		
		if(ids.size()==2) {
			try {
				delete_ACK = dbFunction.deleteAssessor(ids.get("assessorID"), ids.get("projectID"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("delete_ACK", delete_ACK);
		
		//send
		PrintWriter output = response.getWriter();
	 	output.print(jsonSend.toJSONString());
	 	System.out.println("Send: "+jsonSend.toJSONString());
	}
	
	private HashMap<String, Integer> getIDs(ServletContext servletContext, String token, String assessorEmail, String projectName, MysqlFunction dbFunction) {
		InsideFunction inside = new InsideFunction(dbFunction);
		String inviter=inside.token2user(servletContext, token);
		HashMap<String, Integer> ids = new HashMap<String, Integer>();
		
		try {
			int assessorID = dbFunction.getLecturerId(assessorEmail);
			if(assessorID<=0) {
				return ids;
			}
			int projectID = dbFunction.getProjectId(inviter, projectName);
			if(projectID<=0) {
				return ids;
			}
			ids.put("assessorID", assessorID);
			ids.put("projectID", projectID);
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return ids;
	}

}
