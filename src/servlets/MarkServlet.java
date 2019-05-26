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

import com.RapidFeedback.Criteria;
import com.RapidFeedback.InsideFunction;
import com.RapidFeedback.Mark;
import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.ProjectInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class MarkServlet
 */
@WebServlet("/MarkServlet")
public class MarkServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MarkServlet() {
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
//		
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
		String markString = jsonReceive.getString("mark");
		
		Mark mark = JSON.parseObject(markString, Mark.class);
		
		ServletContext servletContext = this.getServletContext();
		
		boolean mark_ACK;
	    //Mention:
		//call the SQL method to save mark and comments of this student.
		//return the 'true' or 'false' value to mark_ACK
		
		mark_ACK = addResult(inside, dbFunction, servletContext, token, projectName, studentID, mark);
		
		//construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("mark_ACK", mark_ACK);
		
		//send
		PrintWriter output = response.getWriter();
	 	output.print(jsonSend.toJSONString());
	 	System.out.println("Send: "+jsonSend.toJSONString());
	}
	
	private boolean addResult(InsideFunction inside, MysqlFunction dbFunction, ServletContext servletContext, String token, String projectName, String studentNumber, Mark grade) {
		boolean result=false;
		
		try {
			String username=inside.token2user(servletContext, token);
			int uid = dbFunction.getLecturerId(username);
			int pid = dbFunction.getProjectId(username, projectName);
			int studentID = dbFunction.ifStudentExists(pid, studentNumber);
			//System.out.println("studentID:"+studentID);
			ArrayList<Criteria> criteriaList = grade.getCriteriaList();
		    ArrayList<Double> markList = grade.getMarkList();
		    ArrayList<Criteria> commentList = grade.getCommentList();
			if(criteriaList.size() != markList.size()) {
				System.out.println("Error: MarkList and criteriaList does not have the same size");
				return result;
			}
			
			//System.out.println("studentInfo:"+dbFunction.returnOneStudentInfo(studentID));
			
			String studentName = dbFunction.returnOneStudentInfo(studentID).getFirstName();
			
			
			for(int i=0;i<markList.size();i++) {
				
				int ack = dbFunction.writeIntoMark(uid, studentID, criteriaList.get(i), markList.get(i).doubleValue(),0,studentName);
				
				if(ack<=0) {
					System.out.println("Error: The "+i+"th mark result cannot be added to the database.");
					return result;
				}
			}
			
			for(int i=0;i<commentList.size();i++) {
				int ack = dbFunction.writeIntoMark(uid, studentID, commentList.get(i), -1.0, 1, studentName);
				if(ack<=0) {
					System.out.println("Error: The "+i+"th comment result cannot be added to the database.");
					return result;
				}
			}
			
			if(dbFunction.writeIntoComment(uid, studentID, grade.getComment(),grade.getTotalMark())) {
				ProjectInfo pInfo = dbFunction.returnProjectDetails(pid);
				String primaryEmail = pInfo.getUsername();
				if(username.equals(primaryEmail)) {
					result = dbFunction.editStudentMark(studentID, grade.getTotalMark());
					if(!result) {
						System.out.println("Error:Cannot edit students' mark in student table");
					}
				}else {
					result=true;
				}
			}
			else {
				System.out.println("Error: Cannot insert additional comment.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
		return result;
	}

}
