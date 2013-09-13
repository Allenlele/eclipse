package base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {

	private String user = "";
	private String password = "";
	private String host = "";
	private String port = "";
	private String database = "";
	private static DBManager dbm = null;	
	private String url = "";
	private Connection con = null;
	private Statement stmt = null;
	
	public DBManager(String user, String password, String host, String port, String database) {
		this.user = user;
		this.password = password;
		this.host = host;
		this.port = port;
		this.database = database;
		this.url = "jdbc:oracle:thin:@" + this.host + ":"+ this.port +":" + this.database;
		
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(this.url,this.user,this.password);
			stmt = con.createStatement();
		} catch (ClassNotFoundException e) {
			System.err.println("Class not found:" + e.getMessage());
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.err.println("Null pointer Exception:" + e.getMessage());
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("SQL Exception:" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e){
			System.err.println("Exception:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static DBManager getInstance(String user, String password, String host, String port, String database){
		if (dbm == null) {
			dbm = new DBManager(user, password, host, port, database);
		}
		return dbm;
	}
	
	public ResultSet executeQuery(String sql) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		}catch(SQLException sqlEx){
			sqlEx.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
		}
		return rs;
	}
	
	public void close(){
		try {
			if (stmt == null || con == null){
				return;
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
