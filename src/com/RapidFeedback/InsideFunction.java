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
			int pid = dbFunction.getProjectId(username, projectName);
			if(username!=null && projectName!=null) {
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
			return result;
		}
	}

	/*
	public HashMap<String, String> string2HashMap(String otherComments){
		HashMap<String,String> comments = JSON.parseObject(otherComments,HashMap.class);
		return comments;
	}*/
}
