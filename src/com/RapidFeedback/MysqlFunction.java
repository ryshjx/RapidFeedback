package com.RapidFeedback;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes.Name;


public class MysqlFunction {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC ";

	static final String USER = "root";
	static final String PASS = "88213882ydh";

	public Connection connectToDB(String url, String userName, String password) {
		Connection conn = null;
		try{
			// Register JDBC driver
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Connect the DB 
			//System.out.println("Connecting the Database...");
			conn = DriverManager.getConnection(url,userName,password);

		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}catch(Exception e){
			// Class.forname faults
			e.printStackTrace();
		}

		// System.out.println("Successfully Connected to DB...");
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
			System.out.println(sql);
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
			System.out.println(sql);
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
			System.out.println(sql);
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

	public int createProject(String username, String projectName, String subjectCode, String subjectName, String description) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		int lecturerId = 0;
		int pjId = 0;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			lecturerId = getLecturerId(username);
			sql = "INSERT INTO Project(primaryMail, name, subjectCode, subjectName) "
					+ "values( '" + username 
					+"','"+ projectName
					+"','"+ subjectCode
					+"','"+subjectName+"' )";
			stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {  
				pjId = rs.getInt(1);  
				sql = "INSERT INTO Lecturers_has_Project(idLecturers, idProject,If_Primary) "
						+ "values( '" + lecturerId +"','"+pjId+"','"+ 1 +"' )";
				stmt.executeUpdate(sql);
				System.out.println(sql);
			}  
			if(description != null) {
				sql = "UPDATE Project SET Description = '"+ description +"' "+"WHERE idProject = "+"'"+pjId+"' ";
				stmt.executeUpdate(sql);
				System.out.println(sql);
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return pjId;
	}

	public boolean updateProjectInfo(String username, String projectName, String subjectCode, String subjectName, String description) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		int pjId = 0;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			pjId = getProjectId(username,projectName);
			sql = "UPDATE Project SET primaryMail = '"+ username+ "', name = '"
					+ projectName+"', subjectCode = '"+username+ "', subecjtName = '"
					+ subjectName +"' "+"WHERE idProject = "+"'"+pjId+"' ";
			stmt.executeUpdate(sql);
			System.out.println(sql);
			if(description != null) {
				sql = "UPDATE Project SET Description = '"+ description +"' "+"WHERE idProject = "+"'"+pjId+"' ";
				stmt.executeUpdate(sql);
				System.out.println(sql);
			}
			result = true;
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return result;
	}


	public boolean updateTimeInformation(int pjId, int durationMin, int durationSec, int warningMin, int warningSec ) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		boolean result = false;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "UPDATE Project SET durationMin = '"+ durationMin +"',  "+"durationSec = '"+ durationSec +"' "+"WHERE idProject = "+"'"+pjId+"' ";
			stmt.executeUpdate(sql);
			System.out.println(sql);

			sql = "UPDATE Project SET warningMin = '"+ warningMin +"',  "+"warningSec = '"+ warningSec +"' "+"WHERE idProject = "+"'"+pjId+"' ";
			stmt.executeUpdate(sql);
			System.out.println(sql);
			result = true;

		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return result;
	}

	public boolean deleteProject(int pjId) throws SQLException{
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "DELETE FROM Project WHERE idProject =" + pjId+ ";"; 
			stmt.executeUpdate(sql);
			result =true;
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return result;
	}

	public boolean deleteCriterias(int pjId) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();

			sql = "DELETE FROM Criteria WHERE idProject = " + pjId+ ";"; 
			stmt.executeUpdate(sql);
			result =true;
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return result;
	}

	public int addOnlyComment(int pjId, Criteria c) throws SQLException {
		int critId = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Criteria(name, idProject, weighting, maxMark, markIncrement, if_only_comment) "
					+ "values( '" + c.getName() +"','"+pjId
					+"','"+c.getWeighting()
					+"','"+c.getMaximunMark()
					+"','"+c.getMarkIncrement()
					+"','"+ 1 +"' )";
			stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {  
				critId = rs.getInt(1);  
			}
			for(int i=0;i<c.getSubsectionList().size();i++)    {  
				addSubSection(critId,c.getSubsectionList().get(i));
			}

		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return critId;
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
			sql = "INSERT INTO Criteria(name, idProject, weighting, maxMark, markIncrement, if_only_comment) "
					+ "values( '" + c.getName() +"','"+pjId
					+"','"+c.getWeighting()
					+"','"+c.getMaximunMark()
					+"','"+c.getMarkIncrement()
					+"','"+ 0 +"' )";
			stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {  
				critId = rs.getInt(1);  
			}
			for(int i=0;i<c.getSubsectionList().size();i++)    {  
				addSubSection(critId,c.getSubsectionList().get(i));
			}

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
			System.out.println(sql);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {  
				ssId = rs.getInt(1);  
			} 
			for(int i=0;i<ss.getShortTextList().size();i++)    {  
				addShortText(ssId,ss.getShortTextList().get(i));
			}
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
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			sql = "INSERT INTO ShortText(name, grade, idSubSection) "
					+ "values(?,?,?)";
			pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setString(1,st.getName());  
            pstmt.setInt(2, st.getGrade());  
            pstmt.setInt(3, subsId);
			pstmt.executeUpdate();
			System.out.println(sql);
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {  
				stId = rs.getInt(1);  
			} 
			for(int i=0;i<st.getLongtext().size();i++)    {  
				addLongText(stId,st.getLongtext().get(i));
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close3(conn,pstmt,rs);
		}
		return stId;
	}

	public void addLongText(int stId, String context) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			sql = "INSERT INTO `LongText`(context, idShortText) values(?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, context);  
            pstmt.setInt(2, stId);  
			pstmt.executeUpdate();
			System.out.println(sql);
			pstmt.close();
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close3(conn,pstmt,rs);
		}
	}
//
//	public String SqlFilter(String source) {
//		source = source.replace(";", "；");
//		source = source.replace("(", "（");
//		source = source.replace(")", "）");
//		source = source.replace("Exec", "");
//		source = source.replace("Execute", "");
//		source = source.replace("xp_", "x p_");
//		source = source.replace("sp_", "s p_");
//		source = source.replace("0x", "0 x");
//		source = source.replace("'", "''");
//		source= source.replace("\"", "");
//		source = source.replace("&", "&amp");
//		source = source.replace("<", "&lt");
//		source = source.replace(">", "&gt");
//		source = source.replace("delete", "remove");
//		source = source.replace("update", "");
//		source = source.replace("insert", "");
//		return source;
//	}




	public int ifStudentExists(int idProject, String studentNumber) throws SQLException{
		int result = 0;
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Students";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while(rs.next()){
				if (rs.getInt("idProject")==idProject && rs.getString("studentNumber").equals(studentNumber)) {
					result= rs.getInt("idStudents");
				}
			}
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return result;
	}

	public boolean addStudentInfo(int projectId, String studentNumber, String mail, String firstName, String surName, String middleName, int group ) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			double mark = 00.00;
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql ="INSERT INTO Students(studentNumber, emailAddress, idProject, firstName, "
					+ "surName, middleName, groupNumber, mark) values( '" 
					+ studentNumber 
					+"','"+mail
					+"','"+projectId
					+"','"+firstName
					+"','"+surName
					+"','"+middleName
					+"','"+group
					+"','"+mark+"' );";
			stmt.execute(sql);
			System.out.println(sql);
			result = true;
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return result;

	}

	public boolean editStudentInfo(int projectId, String studentNumber, String mail, String firstName, String surName, String middleName, int group ) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			double mark = 00.00;
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql =	"UPDATE Students SET "
					+"emailAddress = '"+ mail + "',  "
					+"firstName = '" +firstName+ "', " 
					+"surName = '" +surName+ "', " 
					+"middleName = '" +middleName+ "', " 
					+"groupNumber = '" +group+ "', " 
					+"mark = '" +mark+ "' " 
					+"WHERE idProject= " + "'"+ projectId+ "' AND studentNumber= "+ "'"+ studentNumber+  "';  ";
			stmt.execute(sql);
			System.out.println(sql);
			result = true;
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return result;
	}
	
	public boolean editGroupNumber(int projectId, String studentNumber, int group) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			double mark = 00.00;
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql =	"UPDATE Students SET "
					+"groupNumber = '" +group+ "' " 
					+"WHERE idProject= " + "'"+ projectId+ "' AND studentNumber= "+ "'"+ studentNumber+  "';  ";
			stmt.execute(sql);
			System.out.println(sql);
			result = true;
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return result;
	}
	
	

	public boolean deleteStudent(int projectId, String studentNumber) throws SQLException {
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql =	"DELETE FROM Students "
					+"WHERE idProject= " + "'"+ projectId+ "' AND studentNumber= "+ "'"+ studentNumber+  "';  ";
			stmt.execute(sql);
			System.out.println(sql);
			result = true;
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
		return result;
	}

	public boolean addOtherAssessor(int lecturerId,int projectId) throws SQLException{
		boolean result = false;
		Connection conn = null;
		Statement stmt = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "INSERT INTO Lecturers_has_Project(idLecturers, idProject,If_Primary) "
					+ "values( '" + lecturerId +"','"+projectId+"','"+ 0 +"' )";
			stmt.executeUpdate(sql);
			result = true;
			System.out.println(sql);
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close1(conn,stmt);
		}	
		return result;
	}

	public int getProjectId(String username, String projectName) throws SQLException {
		int id = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql;
		try {
//			int idLecturer = getLecturerId(username);
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			sql = "SELECT * FROM Project";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while(rs.next()){
				if (rs.getString("primaryMail").equals(username)
						&& rs.getString("name").equals(projectName)) {
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
			System.out.println(sql);
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
			System.out.println(sql);
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
			System.out.println(sql);
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
			System.out.println(sql);
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
					pj.setStudentInfoList(returnStudents(projectId));
					pj.setAssistant(returnAssessors(projectId));
					pj.setCommentList(returnOnlyComment(projectId));

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

	public ArrayList<Criteria> returnOnlyComment(int projectId) throws SQLException {
		ArrayList<Criteria> criteriaList= new ArrayList<Criteria>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Criteria WHERE if_only_comment = 1";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
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
	
	
	public ArrayList<Criteria> returnCriteria(int projectId) throws SQLException {
		ArrayList<Criteria> criteriaList= new ArrayList<Criteria>();
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Criteria WHERE if_only_comment <> 1";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
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
			System.out.println(sql);
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
			System.out.println(sql);
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
			sql = "SELECT * FROM `LongText`";
			rs = stmt.executeQuery(sql);
			System.out.println(sql);
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
			System.out.println(sql);
			while(rs.next()){
				if (rs.getInt("idProject") == projectId) {
					StudentInfo studentInfo =new StudentInfo(rs.getString("studentNumber"),rs.getString("firstName"),rs.getString("middleName"),
							rs.getString("surName"), rs.getString("emailAddress"));
					studentInfo.setTotalMark(rs.getDouble("mark"));
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
			System.out.println(sql);
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
			System.out.println(sql);
			while(rs.next()){
				if (rs.getInt("idLecturers") == id) {
					mail = rs.getString("email");
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
	

	public int writeIntoMark(int idlecturer, int idStudent, Criteria cr, double mark) throws SQLException {
		int primaryKey = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			String sql;
			sql = "INSERT INTO Mark(idlecturers, idStudents, CriteriaName, mark) values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, idlecturer);  
            pstmt.setInt(2, idStudent);
            pstmt.setString(3, cr.getName());
            pstmt.setDouble(4, mark);
			pstmt.executeUpdate();
			System.out.println(sql);
			rs = pstmt.getGeneratedKeys();
			if (rs.next()) {  
				primaryKey = rs.getInt(1);  
			}
			int i = cr.getSubsectionList().size();
			for (int j=0;j<i;j++) {
				addSpecificComments(primaryKey, cr.getSubsectionList().get(j));
			}
			System.out.println("add successfully ! Grader: "+ idlecturer+ " student: "+ idStudent) ;
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close3(conn,pstmt,rs);
		}
		return primaryKey;
	}
	
	public void addSpecificComments(int primaryKey, SubSection ss) throws SQLException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			String sql;
			sql = "INSERT INTO CriteriaComment(idMark, subsection, shortText, comment) values(?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, primaryKey);  
            pstmt.setString(2, ss.getName());
            pstmt.setString(3, ss.getShortTextList().get(0).getName());
            pstmt.setString(4, ss.getShortTextList().get(0).getLongtext().get(0));
			pstmt.executeUpdate();
			System.out.println(sql);
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close3(conn,pstmt,rs);
		}
	}
	
	
	
	//write lines into table comment
	public boolean writeIntoComment(int idlecturer, int idStudent, String comment) throws SQLException {
		boolean result = false;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		try {
			conn=connectToDB(DB_URL,USER,PASS);
			sql = "INSERT INTO Lecturers_comment_Students(idlecturers, idStudents, comment) values(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, idlecturer);  
            pstmt.setInt(2, idStudent);
            pstmt.setString(3, comment);
			pstmt.executeUpdate();
			System.out.println(sql);
			result = true;
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close3(conn,pstmt,rs);
		}
		return result;
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
	
	public void close3(Connection conn, PreparedStatement stmt,
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
