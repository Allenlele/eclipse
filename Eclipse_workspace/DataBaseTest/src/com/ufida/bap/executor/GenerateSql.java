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
 * ��װ��ѯ���������
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
	 * ��ʼ����ѯ���úͲ�ѯ����
	 * 
	 * @param propPath
	 *            �����ļ���·��
	 */
	private void generateSql(String propPath) {
		// ��ȡproperty�ļ�����
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
			LoggerManager.getInstance().getLogger().info("û���ҵ���Դ�ļ�", e1);
			return;

		} catch (IOException e2) {
			LoggerManager.getInstance().getLogger().info("��Դ�ļ����س���", e2);
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
			LoggerManager.getInstance().getLogger().error("��������ȷ���û���������Ϊ����������");
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
	 * �������ļ��е�sql�����зָ��
	 * ���û�������κ�sql��䣬��ѡ��22��Ĭ����䡣
	 * @param querySql
	 * @return
	 */
	private List<String> spliList(String querySql) {
		if(StringUtils.isEmpty(querySql)) {
			//���ص���sql��ѯ�ı��
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
