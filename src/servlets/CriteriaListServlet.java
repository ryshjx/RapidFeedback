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
import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.StudentInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class CriteriaListServlet
 */
@WebServlet("/CriteriaListServlet")
public class CriteriaListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CriteriaListServlet() {
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
		String markedCriteriaListString = jsonReceive.getString("markedCriteriaList");
		String commentCriteriaListString = jsonReceive.getString("commentCriteriaList");
		
		List<Criteria> markedCriteria = JSONObject.parseArray(markedCriteriaListString, Criteria.class);
		ArrayList<Criteria> markedCriteriaList = new ArrayList<Criteria>();
		markedCriteriaList.addAll(markedCriteria);
		
		List<Criteria> commentCriteria = JSONObject.parseArray(commentCriteriaListString, Criteria.class);
		ArrayList<Criteria> commentCriteriaList = new ArrayList<Criteria>();
		commentCriteriaList.addAll(commentCriteria);
		
		ServletContext servletContext = this.getServletContext();
				
		boolean update_ACK;
	    //Mention:
		//call the SQL method to save two criteriaList: markedCriteriaList and commentCriteriaList
		//return the 'true' or 'false' value to update_ACK
		update_ACK = false;
	//	update_ACK = 
		
		
		//construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateProject_ACK", update_ACK);
		
		//send
		PrintWriter output = response.getWriter();
	 	output.print(jsonSend.toJSONString());
	}
	
	private boolean addCriteriaList(MysqlFunction dbFunction, ServletContext servletContext, String token, String projectName, ArrayList<Criteria> clist) {
		boolean result = false;
		if(clist.size()==0 || clist==null) {
			result = true;
			return result;
		}
		InsideFunction inside = new InsideFunction(dbFunction);
		String username = inside.token2user(servletContext, token);
		try {
			int pid = dbFunction.getProjectId(username, projectName);
			boolean delete = dbFunction.deleteCriterias(pid);
			if(!delete) {
				return result;
			}
			for(Criteria c : clist) {
				result=dbFunction.addCriteria(pid, c)>0? true:false;
				if(!result) {
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}

}
