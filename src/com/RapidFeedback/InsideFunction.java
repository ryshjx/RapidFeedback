package com.RapidFeedback;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.ServletContext;

import com.alibaba.fastjson.JSON;


public class InsideFunction {
	
	MysqlFunction dbFunction;
	
	public InsideFunction(MysqlFunction db) {
		this.dbFunction = db;
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
			if(username!=null && projectName!=null) {
				int pid = dbFunction.getProjectId(username, projectName);
				if(pid<=0) {
					throw new Exception("Exception: Cannot find the project, or the user is not the primary assessor of the project.");
				}
				if(dbFunction.ifStudentExists(pid, student.getNumber())>0) {
					return result; 
				}else {
					result=dbFunction.addStudentInfo(pid, student.getNumber(), student.getEmail(), student.getFirstName(), student.getSurname(), student.getMiddleName(), student.getGroup());
					return result;
				}
			}else {
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
