package base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import base.PropertiesRW;

public class SqlFileExec {

	/**
	 * @param args
	 */
	File file;

	public SqlFileExec(String file) {
		this.file = new File(file);
	}

	public ArrayList<String> getSql() {
		ArrayList<String> sqlList = new ArrayList<String>();
		StringBuffer temp = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new FileReader(this.file));
			String str;
			while ((str = in.readLine()) != null) {
				temp.append(str);
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		String sqls[] = temp.toString().split(";");
		for (String sql : sqls) {
			sqlList.add(sql);
		}
		return sqlList;
	}

	public void insert() {
		ArrayList<String> sqlList = getSql();
		PropertiesRW pp = new PropertiesRW(new File("./conf/db.properties"));
		String dbUrl = String.format("jdbc:oracle:thin:@%s:%s:%s", pp
				.readModeValue("ip"), pp.readModeValue("port"), pp
				.readModeValue("odbc"));
		Connection c = null;
		Properties conProps = new Properties();
		conProps.put("user", pp.readModeValue("user"));
		conProps.put("password", pp.readModeValue("passwd"));
		Statement conn;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			c = DriverManager.getConnection(dbUrl, conProps);
			c.setAutoCommit(true);
			conn = c.createStatement();
			for (String sql : sqlList) {
				try {
					conn.execute(sql);
				} catch (Exception e) {
				}
			}
			conn.close();
			c.close();
		} catch (Exception e) {
		}
	}

}
