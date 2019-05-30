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
import com.RapidFeedback.SendMail;
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ")
				.append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		MysqlFunction dbFunction = new MysqlFunction();
		InsideFunction inside = new InsideFunction(dbFunction);

		JSONObject jsonReceive;
		BufferedReader reader = request.getReader();
		String str, wholeString = "";
		while ((str = reader.readLine()) != null) {
			wholeString += str;
		}
		System.out.println("Receive: " + wholeString);
		jsonReceive = JSON.parseObject(wholeString);

		// get values from received JSONObject
		String token = jsonReceive.getString("token");
		// other arguments
		String projectName = jsonReceive.getString("projectName");
		String assessorEmail = jsonReceive.getString("assessorEmail");

		ServletContext servletContext = this.getServletContext();

		boolean invite_ACK = false;
		String inviter = inside.token2user(servletContext, token);
		HashMap<String, Integer> ids = getIDs(inside, servletContext, inviter,
				assessorEmail, projectName, dbFunction);
		if (ids.size() == 2) {
			try {
				invite_ACK = dbFunction.addOtherAssessor(ids.get("assessorID"),
						ids.get("projectID"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		boolean sendMail_ACK = false;
		if (invite_ACK) {
			try {
				String subject = "Invitation from the project <" + projectName + ">";
				String inviterName = dbFunction
						.getLecturerName(dbFunction.getLecturerId(inviter));
				String assessorName = dbFunction
						.getLecturerName(ids.get("assessorID"));
				String msg = "Hi " + assessorName + ",\r\n\r\n"
						+ "We’re just writing to let you know that you’ve joined the assessment of"
						+ " the project <" + projectName+"> on RapidFeedback, which was invited by the "
						+ "project's primary assessor " + inviterName + "\r\n\r\n" + "Regards,\r\n" 
						+ "RapidFeedback Team";
				sendMail_ACK = sendInvitation(inside,
						servletContext, projectName, assessorEmail, dbFunction,
						subject, msg);
				sendMail_ACK = true;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}

		JSONObject jsonSend = new JSONObject();
		jsonSend.put("invite_ACK", invite_ACK);
		jsonSend.put("assessorEmail", assessorEmail);
		jsonSend.put("sendMail_ACK", sendMail_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
		System.out.println("Send: " + jsonSend.toJSONString());

	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		MysqlFunction dbFunction = new MysqlFunction();
		InsideFunction inside = new InsideFunction(dbFunction);

		JSONObject jsonReceive;
		BufferedReader reader = request.getReader();
		String str, wholeString = "";
		while ((str = reader.readLine()) != null) {
			wholeString += str;
		}
		System.out.println("Receive: " + wholeString);
		jsonReceive = JSON.parseObject(wholeString);

		// get values from received JSONObject
		String token = jsonReceive.getString("token");
		// other arguments
		String projectName = jsonReceive.getString("projectName");
		String assessorEmail = jsonReceive.getString("assessorEmail");

		ServletContext servletContext = this.getServletContext();

		boolean delete_ACK = false;

		String inviter = inside.token2user(servletContext, token);

		HashMap<String, Integer> ids = getIDs(inside, servletContext, inviter,
				assessorEmail, projectName, dbFunction);

		if (ids.size() == 2) {
			try {
				delete_ACK = dbFunction.deleteAssessor(ids.get("assessorID"),
						ids.get("projectID"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		boolean sendMail_ACK = false;
		if (delete_ACK) {
			try {
				String subject = "Notification from the project <" + projectName + ">";
				String assessorName = dbFunction
						.getLecturerName(ids.get("assessorID"));
				String msg = "Hi " + assessorName + ",\r\n\r\n"
						+ "We’re just writing to let you know that you’ve left the assessment of"
						+ " the project <" + projectName + "> on RapidFeedback\r\n\r\n" + "Regards,\r\n" 
						+ "RapidFeedback Team";
				sendMail_ACK = sendInvitation(inside, servletContext,
						projectName, assessorEmail, dbFunction, subject, msg);
				sendMail_ACK = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		JSONObject jsonSend = new JSONObject();
		jsonSend.put("delete_ACK", delete_ACK);
		jsonSend.put("assessorEmail", assessorEmail);
		jsonSend.put("sendMail_ACK", sendMail_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
		System.out.println("Send: " + jsonSend.toJSONString());
	}

	private HashMap<String, Integer> getIDs(InsideFunction inside,
			ServletContext servletContext, String inviter, String assessorEmail,
			String projectName, MysqlFunction dbFunction) {
		// String inviter=inside.token2user(servletContext, token);
		HashMap<String, Integer> ids = new HashMap<String, Integer>();

		try {
			int assessorID = dbFunction.getLecturerId(assessorEmail);
			if (assessorID <= 0) {
				System.out.println("assessor not exists");
				return ids;
			}
			int projectID = dbFunction.getProjectId(inviter, projectName);
			System.out.println(inviter);
			System.out.println(projectName);
			System.out.println(projectID);
			if (projectID <= 0) {
				System.out.println("project not exists");
				throw new Exception("Exception: Cannot find the project, or the user is not the primary assessor of the project.");
			}
			ids.put("assessorID", assessorID);
			ids.put("projectID", projectID);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ids;
	}

	public boolean sendInvitation(InsideFunction inside,
			ServletContext servletContext, String projectName,
			String assessorEmail, MysqlFunction dbFunction, String subject,
			String msg) {
		boolean result = false;
		SendMail send = new SendMail();
		String host = "smtp.gmail.com";
		String user = "feedbackrapid@gmail.com";
		String pwd = "gkgkbzzbavwowfbh";
		send.setAddress(user, assessorEmail, subject);
		result = send.sendSimpleMail(host, user, pwd, msg);
		return result;
	}

}
