package com.ufida.bap.executor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.ufida.bap.log.LoggerManager;
import com.ufida.bap.sqlManage.SqlManager;

/**
 * 封装查询所需的条件
 * 
 * @author chenfeic
 * 
 */
public class GenerateSql {
	private SqlSetting sqlSetting = null;
	
	private static final String SEPARATE =";";

	public GenerateSql() {
		this("DBTest.properties");
	}

	public GenerateSql(String propPath) {
		generateSql(propPath);
	}

	/***
	 * 初始化查询设置和查询条件
	 * 
	 * @param propPath
	 *            属性文件的路径
	 */
	private void generateSql(String propPath) {
		// 读取property文件内容
		Properties prop = new Properties();
		String driverName = null;
		String conn_Url = null;
		String userName = null;
		String pwd = null;
		String querySql = null;
		List<String> sqlList  = null;
		int userCount = 0;
		String str_count = null;
		String outputPath = null;
		int maxPoolSize = 0;
		int minPoolSize = 0;
		int initPoolSize = 0;
		int maxIdleTime = 0;
		try {
			InputStream in = new FileInputStream(propPath);
			prop.load(in);
			driverName = prop.getProperty("Driver_Name").trim();
			conn_Url = prop.getProperty("Connection_URL").trim();
			userName = prop.getProperty("User_Name").trim();
			pwd = prop.getProperty("Password").trim();
			querySql = prop.getProperty("Query_Sql").trim();
			sqlList = spliList(querySql);
			
			str_count = prop.getProperty("User_Count").trim();
			outputPath = prop.getProperty("Output_Path").trim();
			maxPoolSize = Integer.parseInt(prop.getProperty("MaxPoolSize")
					.trim());
			minPoolSize = Integer.parseInt(prop.getProperty("MinPoolSize")
					.trim());
			initPoolSize = Integer.parseInt(prop.getProperty("InitialPoolSize")
					.trim());
			maxIdleTime = Integer.parseInt(prop.getProperty("MaxIdleTime")
					.trim());
		} catch (FileNotFoundException e1) {
			LoggerManager.getInstance().getLogger().info("没有找到资源文件", e1);
			return;

		} catch (IOException e2) {
			LoggerManager.getInstance().getLogger().info("资源文件加载出错", e2);
			return;
		}
		boolean error = false;
		try {
			userCount = Integer.parseInt(str_count);
			if (userCount <= 0) {
				error = true;
			}
		} catch (NumberFormatException e1) {
			error = true;
		}
		if (error) {
			LoggerManager.getInstance().getLogger().error("请输入正确的用户数（必须为正整数）！");
			return;
		}
		getSqlSetting().setDriverName(driverName);
		getSqlSetting().setConnUrl(conn_Url);
		getSqlSetting().setUserName(userName);
		getSqlSetting().setPassword(pwd);
		getSqlSetting().setQuerySql(sqlList);
		
		getSqlSetting().setUserCount(userCount);
		getSqlSetting().setMaxPoolSize(maxPoolSize);
		getSqlSetting().setMinPoolSize(minPoolSize);
		getSqlSetting().setInitPoolSize(initPoolSize);
		getSqlSetting().setMaxIdleTime(maxIdleTime);
		getSqlSetting().setOutputPath(outputPath);

	}

	/**
	 * 对配置文件中的sql语句进行分割处理，
	 * 如果没有输入任何sql语句，则选择22条默认语句。
	 * @param querySql
	 * @return
	 */
	private List<String> spliList(String querySql) {
		if(StringUtils.isEmpty(querySql)) {
			//返回的是sql查询的编号
			return SqlManager.getInstance().getAllSqlNum();
		}
		String[] sqls = querySql.split(SEPARATE);
		List<String> sqlList = new ArrayList<String>();
		for (String sql : sqls) {
			sqlList.add(sql);
		}
		return sqlList;
	}

	public SqlSetting getSqlSetting() {
		if (sqlSetting == null) {
			sqlSetting = new SqlSetting();
		}
		return sqlSetting;
	}

}
