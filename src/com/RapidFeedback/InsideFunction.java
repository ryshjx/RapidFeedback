package com.RapidFeedback;

import java.sql.SQLException;
import java.util.ArrayList;

public class InsideFunction {
	
	MysqlFunction db = new MysqlFunction();
	
	public boolean Register(String email, String password, String firstName, String middleName, String lastName) throws SQLException {
		int checkResult = db.checkLecturerExists(email);
		if(checkResult == 1) {
			db.addToLecturers(email, password, firstName, middleName, lastName);
			return true;
		}
		else {
			return false;
		}
	}
	
	// Login: wrong e-mail address reutrn -1，wrong password return 0，
	//  successful login return lecturer's id, sqlexception return -2
	public int Login(String email, String password) throws SQLException {
		return db.logIn(email, password);
	}
	
/*	public ArrayList<Project> getProjectList(int uid){
		ArrayList projectList = new ArrayList();
		int[] projectIDs = db.projectIDs(uid);
		
		
	}*/
	
}
