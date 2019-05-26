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
}
