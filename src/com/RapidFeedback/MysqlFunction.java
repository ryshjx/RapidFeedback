package com.RapidFeedback;
import java.sql.*;
public class MysqlFunction {

	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost:3306/mydb?serverTimezone=UTC&useSSL=false";

	static final String USER = "root";
	static final String PASS = "Feedback123456";

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
		sql = "INSERT INTO Lecturers( email, pass, FirstName, MiddleName, FamilyName) "
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

//	public ResultSet queryAll(String tableName) throws SQLException {
//		Connection conn = null;
//		ResultSet rs = null;
//		Statement stmt = null;
//		try {
//			conn=connectToDB(DB_URL,USER,PASS);
//			stmt = conn.createStatement();
//			String sql;
//			sql = "SELECT * FROM "+tableName;
//			rs = stmt.executeQuery(sql);
//		}catch(SQLException se){
//			// JDBC faults
//			se.printStackTrace();
//		}finally {
//			close1(conn,stmt);
//		}
//		return rs;
//	}

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
					if(rs.getString("pass").equals(password)) {
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

	public void addProject(int idLecturer, String pj, String description) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql,sql2;
		sql = "INSERT INTO Project(name, description) "
				+ "values( '" + pj +"','"+description+"' )";
		try {

			conn=connectToDB(DB_URL,USER,PASS);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {  
				int pjId = rs.getInt(1);  
				sql2 = "INSERT INTO Lecturers_has_Project(idLecturers, idProject,If_Primary) "
						+ "values( '" + idLecturer +"','"+pjId+"','"+1+"' )";
				stmt.executeUpdate(sql2);;  
			}  
		}catch(SQLException se){
			// JDBC faults
			se.printStackTrace();
		}finally {
			close2(conn,stmt,rs);
		}
	}
	
	public int[] projectIDs(int uid) {
		int[] test = {1,2,3};
		return test;
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
