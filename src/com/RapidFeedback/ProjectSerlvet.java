package com.RapidFeedback;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.CommunicationException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Servlet implementation class ProjectSerlvet
 */
@WebServlet("/Project")
public class ProjectSerlvet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	MysqlFunction dbFunction = new MysqlFunction();
	ProjectInfo project;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectSerlvet() {
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
	//create new project
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ProjectInfo project;
		int newPID;
		newPID = addProject(project);
		
		
		
		
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	/* return -1: failed to add project to DB
	 * return -2: error: assistantList is null
	 * return -3: error: failed to add a criteria
	 * 
	 */
	//
	private int addProject(ProjectInfo project) throws Exception {
		
		int createResult = dbFunction.createProject(project);
		
		ArrayList<String> assistantList = project.getAssistant();
		ArrayList<Criteria> criteriaList = project.getCriteria();
		ArrayList<StudentInfo> studentList = project.getStudentInfo();
		
		String username = project.getUsername();
		int pid = dbFunction.getProjectId(project);
		
		if(createResult == 0) {
			return -1;
		}
		//The assistantList must have at least 1 person (the primary lecturer).
		if(assistantList==null || assistantList.size()==0) {
			return -2;
		}else if(assistantList.size()>1) {
			for(String email: assistantList) {
				if(email.equals(username)==false) {
					int uid = dbFunction.getLecturerId(email);
					dbFunction.addOtherAssessor(uid, pid);
				}
			}
		}
		
		if(criteriaList!=null && criteriaList.size()!=0) {
			for(Criteria c:criteriaList) {
				int criteriaID = dbFunction.addCriteria(pid, c);
				if(criteriaID == 0) {
					return -3;
				}
				addSubSec(c.getSubsectionList())
			}
			
		}
		
		return createResult;
	}
	
	private void addSubSec(int critId, ArrayList<SubSection> subSecList) {
		
	}

}
