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

import org.apache.jasper.tagplugins.jstl.core.Out;

import com.RapidFeedback.InsideFunction;
import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.StudentInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class EditStudentServlet
 */
@WebServlet("/EditStudentServlet")
public class EditStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditStudentServlet() {
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
		String studentID = jsonReceive.getString("studentID");
		String firstName = jsonReceive.getString("firstName");
		String middleName = jsonReceive.getString("middleName");
		String lastName = jsonReceive.getString("lastName");
		String email = jsonReceive.getString("email");
		
		ServletContext servletContext = this.getServletContext();
		StudentInfo student = new StudentInfo(studentID, firstName, middleName, lastName, email);
				
		/*
		 * Attention:
		 * This method is very like 'AddStudent' method, the difference is:
		 * we assume the StudentID cannot change, while other attributes can be changed.
		 */
		
		boolean updateStudent_ACK;
	    //Mention:
		//call the SQL method to edit the student information whose studentID is studentID.
		//return the 'true' or 'false' value to update_ACK
		updateStudent_ACK = false;
		updateStudent_ACK = editStudent(dbFunction, servletContext, token, projectName, student);

		
		//construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateStudent_ACK", updateStudent_ACK);
		
		//send
		PrintWriter output = response.getWriter();
	 	output.print(jsonSend.toJSONString());
	}
	
	private boolean editStudent(MysqlFunction dbFunction, ServletContext servletContext, String token, String projectName, StudentInfo student) {
		boolean result = false;
		InsideFunction inside = new InsideFunction(dbFunction);
		String username=inside.token2user(servletContext, token);
		try {
			int pid = dbFunction.getProjectId(username, projectName);
			//System.out.println(username);
			//System.out.println(projectName);
			if(username!=null && projectName!=null) {
				if(dbFunction.ifStudentExists(pid, student.getNumber())>0) {
					//System.out.println("before result");
					result=dbFunction.editStudentInfo(pid, student.getNumber(), student.getEmail(), student.getFirstName(), student.getSurname(), student.getMiddleName(), student.getGroup());
					//System.out.println("after result");
					return result; 
				}else {
					//result=dbFunction.addStudentInfo(pid, student.getNumber(), student.getEmail(), student.getFirstName(), student.getSurname(), student.getMiddleName(), student.getGroup());
					return result;
				}
			}else {
				return result;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return result;
		}
	}

}
