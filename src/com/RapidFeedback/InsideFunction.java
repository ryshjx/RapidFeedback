package com.RapidFeedback;

import java.sql.SQLException;
import java.util.ArrayList;

//import sun.jvm.hotspot.runtime.StaticBaseConstructor;

public class InsideFunction {
	
	MysqlFunction db = new MysqlFunction();
	
/*	public boolean Register(String email, String password, String firstName, String middleName, String lastName) throws SQLException {
		int checkResult = db.checkLecturerExists(email);
		if(checkResult == 1) {
			db.addToLecturers(email, password, firstName, middleName, lastName);
			return true;
		}
		else {
			return false;
		}
	}*/
	
	// Login: wrong e-mail address reutrn -1，wrong password return 0，
	//  successful login return lecturer's id, sqlexception return -2
/*	public int Login(String email, String password) throws SQLException {
		return db.logIn(email, password);
	}*/
	
/*	public Project[] getProjectList(int uid){
		int[] pIDs = db.projectIDs(uid);
		Project[] projectList = new Project[pIDs.length];
		for(int i = 0; i<pIDs.length; i++) {
			projectList[i]=db.getProject(pIDs[i]);
		}
		return projectList;
	}*/
	
	
	
}
