package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
import com.RapidFeedback.PDFUtil;
import com.RapidFeedback.ProjectInfo;
import com.RapidFeedback.SendMail;
import com.RapidFeedback.StudentInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException; 
import org.apache.pdfbox.pdmodel.PDDocument;



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
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//System.out.println("根目录所对应的绝对路径"+request.getServletPath());
		//System.out.println("resource package所对应的绝对路径"+this.getServletContext().getRealPath("resource"));
		response.getWriter().append("Served at: ").append(request.getContextPath());
		//response.getWriter().append("Files are at: ").append(this.getServletContext().getRealPath(""));
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
		String primaryEmail = jsonReceive.getString("primaryEmail");
		
		ServletContext servletContext = this.getServletContext();
		
		boolean sendMail_ACK = false;
		
		/*
		 * add operation to get marklist, generate pdf and send mail.
		 */
		
		
		PDFUtil pdf = new PDFUtil();
		String userEmail = inside.token2user(servletContext, token);
		String filePath = servletContext.getRealPath("");
		String fileName = projectName+"_"+studentNumber+".pdf";
		
		
		
		try {
			int projectId=dbFunction.getProjectId(primaryEmail, projectName);
			ProjectInfo pj = dbFunction.returnProjectDetails(projectId);
			int studentId = dbFunction.ifStudentExists(projectId, studentNumber);
			StudentInfo studentInfo= dbFunction.returnOneStudentInfo(studentId);
			
			//get marklist
			ArrayList<String> assessors = dbFunction.returnAssessors(projectId);
			ArrayList<Mark> markList = new ArrayList<Mark>();
			for(String lecturer:assessors) {
				int lecturerId=dbFunction.getLecturerId(lecturer);
				
				Mark mark = dbFunction.returnMark(projectId, lecturerId, studentId);
				markList.add(mark);
			}
			//get marklist end
			
			pdf.create(markList, pj, studentInfo, filePath, fileName);
			
			sendMail_ACK = sendEmail(userEmail, servletContext, projectName, studentInfo.getEmail(), studentInfo.getFirstName(), studentNumber, filePath, fileName);
			
			//TODO:change sendEmail_flag
			
			//delete files
			pdf.deletePdf(filePath+fileName);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("sendMail_ACK", sendMail_ACK);
		
		//send
		PrintWriter output = response.getWriter();
	 	output.print(jsonSend.toJSONString());
	 	System.out.println("Send: "+jsonSend.toJSONString());
	}
	
	public boolean sendEmail(String userEmail, ServletContext servletContext, String projectName, String studentEmail, String firstName, String studentNumber, String filePath, String fileName) {
		boolean result = false;
		SendMail send = new SendMail();
		String subject = projectName + " Presentation Result for " + studentNumber; 
		String msg = "Hi " + firstName + ",\n\n" 
				+ "This is a feedback for your " + projectName
				+ "Presentation.\n\n"
				+ "If you have any problems, please don\'t hesitate to contact the lecturers/tutors: "
				+ userEmail + "\n\n" + "Regards,\n" + "RapidFeedback Team";
		// String subject = "";
		String host = "smtp.gmail.com";
		String user = "feedbackrapid@gmail.com";
		String pwd = "gkgkbzzbavwowfbh";
		//String affix = this.getServletContext().getRealPath("Assignment1.pdf");
		send.setAddress(user, studentEmail, subject);
		//System.out.println(affix);
		send.setAffix(filePath+fileName, fileName);
		result = send.send(host, user, pwd, msg);
		return result;
	}
}
