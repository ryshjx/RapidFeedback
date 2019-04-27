package com.RapidFeedback;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;
public class MysqlFunction {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://10.12.10.1:3306/mydb?serverTimezone=UTC&useSSL=false";

	static final String USER = "root";
	static final String PASS = "88213882ydh";
	
	public Connection connectToDB(String url, String userName, String password) {
		Connection conn = null;
		try{
			// Register JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Connect the DB 
			System.out.println("Connecting the Database...");
			conn = DriverManager.getConnection(url,userName,password);

		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}catch(Exception e){
			// Class.forname faults
			e.printStackTrace();
		}  

		System.out.println("Successfully Connected to DB...");
		return conn;   
	}

	public void addToLecturers(String mail, 
			String password, String firstName,
			String middleName,String familyName ) throws SQLException
	{
		Connection conn = null;
		Statement stmt = null;
		String sql;
		sql = "INSERT INTO Lecturers( email, password, FirstName, MiddleName, FamilyName) "
				+ "values( '" + mail +"','"+password + "','"+firstName +"','"+middleName+"','"+familyName +"' )";

		try {	
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close1(conn,stmt);
		}
	}

	//check the e-mail address, has existed return 0，can be registered return 1, sqlException return 2
	public int checkLecturerExists(String mail) throws SQLException {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		int tag = 1;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT email FROM Lecturers";
			rs = stmt.executeQuery(sql);

			while(rs.next()){
				if (rs.getString("email").equals(mail)) {
					tag = 0;
				}
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
			tag =2; 
		}finally {
			close2(conn,stmt,rs);
		}
		System.out.println("tag = " +tag);
		return tag;
	}

	// Login: wrong e-mail address reutrn -1，wrong password return 0，
	//  successful login return lecturer's id, sqlexception return -2
	public int logIn(String mail, String password) throws SQLException {
		int num = -1;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Lecturers";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if (rs.getString("email").equals(mail)) {
					if(rs.getString("password").equals(password)) {
						num = rs.getInt("idLecturers");
					}
					else {
						num = 0;
					}
				} 	
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
			num = -2; 
		}finally {
			close2(conn,stmt,rs);
		}
		return num;
	}

	public int createProject(ProjectInfo pj) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		String mail= pj.getUsername();
		int lecturerId = 0;
		int pjId = 0;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			lecturerId = getLecturerId(mail);
			sql = "INSERT INTO Project(primaryMail,name, subjectCode, subjectName) "
					+ "values( '" + pj.getUsername() 
					+"','"+ pj.getProjectName() 
					+"','"+pj.getSubjectCode()
					+"','"+pj.getSubjectName()+"' )";
			stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {  
				pjId = rs.getInt(1);  
				sql = "INSERT INTO Lecturers_has_Project(idLecturers, idProject,If_Primary) "
						+ "values( '" + lecturerId +"','"+pjId+"','"+ 1 +"' )";
				stmt.executeUpdate(sql);
			}  
			if(pj.getDescription() != null) {
				sql = "INSERT INTO Project(description) "
						+ "values( '" + pj.getDescription()+"' )";
				stmt.executeUpdate(sql);
			}
			if(pj.getDurationMin() != 0) {
				sql = "INSERT INTO Project(durationMin) "
						+ "values( '" + pj.getDurationMin()+"' )";
				stmt.executeUpdate(sql);
			}
			if(pj.getDurationSec() != 0) {
				sql = "INSERT INTO Project(durationSec) "
						+ "values( '" + pj.getDurationSec()+"' )";
				stmt.executeUpdate(sql);
			}
			if(pj.getWarningMin() != 0) {
				sql = "INSERT INTO Project(warningMin) "
						+ "values( '" + pj.getWarningMin()+"' )";
				stmt.executeUpdate(sql);
			}
			if(pj.getWarningSec() != 0) {
				sql = "INSERT INTO Project(warningSec) "
						+ "values( '" + pj.getWarningSec()+"' )";
				stmt.executeUpdate(sql);
			}

		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return pjId;
	}
	


	public int addCriteria(int pjId, Criteria c) throws SQLException {
		int critId = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Project(name, idProject, "
					+ "weighting, maxMark, markIncrement) "
					+ "values( '" + c.getName() +"','"+pjId
					+"','"+c.getWeighting()
					+"','"+c.getMaximunMark()
					+"','"+c.getMarkIncrement()+"' )";
			stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			critId = rs.getInt(1);  
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return critId;
	}

	public int addSubSection(int critId, SubSection ss) throws SQLException {
		int ssId = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO SubSection(name, idCriteria) "
					+ "values( '" + ss.getName() +"','"+critId+"' )";
			stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			ssId = rs.getInt(1);  
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return ssId;
	}

	public int addShortText(int subsId, ShortText st) throws SQLException {
		int stId = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql1, sql2;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql1 = "INSERT INTO ShortText(name, grade, idSubSection) "
					+ "values( '" + st.getName() 
					+"','"+st.getGrade()
					+"','"+subsId+"' )";
			stmt.executeUpdate(sql1,Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			stId = rs.getInt(1);  
			sql2 = "INSERT INTO LongText(name, grade, idSubSection) "
					+ "values( '" + st.getName() 
					+"','"+st.getGrade()
					+"','"+subsId+"' )";
			stmt.executeUpdate(sql2);
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return stId;
	}

	public void addStudent(int projectId, StudentInfo student) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Students(studentNumber, e-mailAddress, idProject, firstName, surName, middleName, groupNumber, mark) "
					+ "values( '" + student.getNumber() 
					+"','"+student.getEmail()
					+"','"+projectId
					+"','"+student.getFirstName()
					+"','"+student.getSurname()
					+"','"+student.getMiddleName()
					+"','"+student.getGroup()
					+"','"+student.getMark()+"' )";
			stmt.executeUpdate(sql);
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
	}



	public void addOtherAssessor(int lecturerId,int projectId) throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Lecturers_has_Project(idLecturers, idProject,If_Primary) "
					+ "values( '" + lecturerId +"','"+projectId+"','"+ 0 +"' )";
			stmt.executeUpdate(sql);
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close1(conn,stmt);
		}	
	}

	public int getProjectId(ProjectInfo pj) throws SQLException {
		int id = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			int idLecturer = getLecturerId(pj.getUsername());
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM Projects";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if (rs.getString("primaryMail").equals(pj.getUsername())
						&& rs.getString("name").equals(pj.getProjectName())) {
					id = rs.getInt("idProject");
				}else {
					continue;
				}
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return id;	
	}


	public int getLecturerId(String mail) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		int id = 0;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM Lecturers";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				//				id = rs.getInt("idLecturers");
				//				System.out.println("kkkkk"+id); 
				if (rs.getString("email").equals(mail)) {
					id = rs.getInt("idLecturers");
				}else {
					continue;
				}
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return id;
	}


	public List<Integer> queryProjects(String mail) throws SQLException {
		List<Integer> numList = new ArrayList<Integer>();
		int id = 0;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Lecturers";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				//				id = rs.getInt("idLecturers");
				//				System.out.println("kkkkk"+id); 
				if (rs.getString("email").equals(mail)) {
					id = rs.getInt("idLecturers");
				}else {
					continue;
				}
			}
			sql = "SELECT * FROM Lecturers_has_Project WHERE idLecturers =" + id;
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				int pjId = rs.getInt(2);  
				numList.add(pjId);
			}	
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return numList;
	}



	public ProjectInfo returnProjectInfo(int projectId) throws SQLException {
		ProjectInfo pj = new ProjectInfo();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Project";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if (rs.getInt("idProject") == projectId) {

					pj.setUsername(rs.getString("primaryMail"));
					pj.setProjectName(rs.getString("name"));
					pj.setSubjectCode(rs.getString("subjectCode"));
					pj.setSubjectName(rs.getString("subjectName"));
					pj.setDescription(rs.getString("description"));
					pj.setTimer(rs.getInt("durationMin"),rs.getInt("durationSec"),
							rs.getInt("warningMin"),rs.getInt("warningSec"));
					pj.setCriteria(returnCriteria(projectId));
					pj.setStudentList(returnStudents(projectId));
					pj.setAssistant(returnAssessors(projectId));

				}else {
					continue;
				}
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return pj;
	}

	public ArrayList<Criteria> returnCriteria(int projectId) throws SQLException {
		ArrayList<Criteria> criteriaList= new ArrayList<Criteria>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Criteria";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if (rs.getInt("idProject") == projectId) {
					Criteria cr = new Criteria();
					cr.setName(rs.getString("name"));
					cr.setWeighting(rs.getInt("weighting"));
					cr.setMaximunMark(rs.getInt("maxMark"));
					cr.setMarkIncrement(rs.getString("markIncrement"));
					cr.setSubsectionList(returnSubsection(rs.getInt("idCriteria")));
					criteriaList.add(cr);
				}else {
					continue;
				}
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return criteriaList;
	}

	private ArrayList<SubSection> returnSubsection(int idCriteria) throws SQLException {
		ArrayList<SubSection> subsectionList = new ArrayList<SubSection>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM SubSection";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if (rs.getInt("idCriteria") == idCriteria) {
					SubSection ss = new SubSection();
					ss.setName(rs.getString("name"));
					ss.setShortTextList(returnShortText(rs.getInt("idSubSection")));
					subsectionList.add(ss);
				}else {
					continue;
				}
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		
		return subsectionList;
	}

	private ArrayList<ShortText> returnShortText(int idSubSection) throws SQLException {
		ArrayList<ShortText> shortTextList = new ArrayList<ShortText>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM ShortText";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if (rs.getInt("idSubSection") == idSubSection) {
					ShortText st = new ShortText();
					st.setGrade(rs.getInt("grade"));
					st.setName(rs.getString("name"));
					st.setLongtext(returnLongText(rs.getInt("idShortText")));
					shortTextList.add(st);
				}else {
					continue;
				}
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return shortTextList;
	}
	
	

	private ArrayList<String> returnLongText(int idShortText) throws SQLException {
		ArrayList<String> longtext = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM LongText";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if (rs.getInt("idShortText") == idShortText) {
					longtext.add(rs.getString("context"));
				}else {
					continue;
				}
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return longtext;
	}

	public ArrayList<StudentInfo> returnStudents(int projectId) throws SQLException{
		ArrayList<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Students";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if (rs.getInt("idProject") == projectId) {
					StudentInfo studentInfo =new StudentInfo(rs.getString("studentNumber"),rs.getString("firstName"),rs.getString("middleName"),
							rs.getString("surName"), rs.getString("e-mailAddress"));
					studentInfo.setMark(rs.getDouble("mark"));
					studentInfo.setGroup(rs.getInt("groupNumber"));
					studentInfoList.add(studentInfo);
				}else {
					continue;
				}
			}
			
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
	
		return studentInfoList;
	}
	
	
	
	

	public ArrayList<String> returnAssessors(int projectId) throws SQLException{
		ArrayList<String> assessorList = new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Lecturers_has_Project";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if (rs.getInt("idProject") == projectId) {
					assessorList.add(getLecturerMail(rs.getInt("idLecturers")));

				}else {
					continue;
				}
			}

		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return assessorList;
	}

	public String getLecturerMail(int id) throws SQLException {
		String mail = null;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Lecturers";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				if (rs.getInt("idLecturers") == id) {
					mail = rs.getString("e-mail");
				}else {
					continue;
				}
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return mail;
	}
	

	public void close1(Connection conn, Statement stmt) throws SQLException {
		try{
			if(stmt != null){
				stmt.close();
				stmt = null;
			}
		}finally{
			if(conn != null){
				conn.close();
				conn = null;
			}
		}
	}


	public void close2(Connection conn, Statement stmt,
			ResultSet rs) throws SQLException {
		try {
			if(rs != null){
				rs.close();
				rs = null;
			}
		} finally{
			try{
				if(stmt != null){
					stmt.close();
					stmt = null;
				}
			}finally{
				if(conn != null){
					conn.close();
					conn = null;
				}
			}
		}
	}


}
