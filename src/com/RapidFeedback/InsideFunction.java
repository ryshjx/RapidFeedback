package com.RapidFeedback;

import java.awt.print.Printable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

//import com.sun.tools.classfile.StackMapTable_attribute.chop_frame;

//import sun.jvm.hotspot.runtime.StaticBaseConstructor;

public class InsideFunction {
	
	MysqlFunction dbFunction;
	
	public InsideFunction(MysqlFunction db) {
		this.dbFunction = db;
	}
	
	public InsideFunction() {
		this.dbFunction = new MysqlFunction();
	}
	
	public String token2user(ServletContext servletContext, String token) {
		Enumeration<String> e = servletContext.getAttributeNames();
		while(e.hasMoreElements()) 
		{
			if(token.equals((String)e.nextElement())) {
				return (String)servletContext.getAttribute(token);
			}
		}
		return null;
	}
	
	public boolean addStudent(ServletContext servletContext, String token, String projectName, StudentInfo student) {
		String username=this.token2user(servletContext, token);
		boolean result=false;
		try {
			int pid = dbFunction.getProjectId(username, projectName);
			//System.out.println("hello!!!!");
			if(username!=null && projectName!=null) {
				if(dbFunction.ifStudentExists(pid, student.getNumber())>0) {
					//result=dbFunction.editStudentInfo(pid, student.getNumber(), student.getEmail(), student.getFirstName(), student.getSurname(), student.getMiddleName(), student.getGroup());
					return result; 
				}else {
					result=dbFunction.addStudentInfo(pid, student.getNumber(), student.getEmail(), student.getFirstName(), student.getSurname(), student.getMiddleName(), student.getGroup());
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
	
	//public boolean getResult();
	
/*	public boolean addStudent(String token, ) {
		
	}*/
	
/*	public int projectP1(ServletContext servletContext, String projectName, String token, String subjectName, String subjectCode, String description) {
		String username = token2user(servletContext, token);
		return dbFunction.createProject();
	}
	
	public int projectP2(ServletContext servletContext, String projectName, String token, int durationMin, int durationSec, int warningMin, int warningSec) {
		String username = token2user(servletContext, token);
		return dbFunction.createProject();
	}
	*/
/*	public int projectP3(ServletContext servletContext, String token, String projectName, String username, ArrayList<String> criteriaNames) {
		String username = token2user(servletContext, token);
		if(criteriaNames!=null && criteriaNames.size()!=0) {
			for(String c:criteriaNames) {
				int pid = dbFunction.getProjectId();
				int criteriaID = dbFunction.addCriteria(pid, c);
				if(criteriaID == 0) {
					System.out.println("The criteria " + c + "is not added to the DB");
					return -1;
				}
			}	
		}
	}
	
	public int projectP4(ServletContext servletContext, String projectName, String username, ArrayList<Criteria> criteriaList) {
		String username = token2user(servletContext, token);
		if(criteriaList!=null && criteriaList.size()!=0) {
			for(Criteria c:criteriaList) {
				int pid = dbFunction.getProjectId();
				int criteriaID = dbFunction.updateCriteria(pid, c);
				if(criteriaID == 0) {
					System.out.println("The criteria " + c.getName() + "is not added to the DB");
					return -1;
				}
				int subSecResult = addSubSec(projectName, username, c.getSubsectionList());
			}
			
		}
	}
	
	private int addSubSec(String projectName, String username, ArrayList<SubSection> subSecList) {
		for(SubSection s:subSecList) {
			int result = dbFunction.addSubSection(critId, ss);
			if(result == 0) {
				System.out.println(s.getName());
				return -1;
			}
		}
		return 1;
	}*/
}
