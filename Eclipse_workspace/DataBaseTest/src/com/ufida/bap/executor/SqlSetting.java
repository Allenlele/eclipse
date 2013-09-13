package com.ufida.bap.executor;

import java.util.ArrayList;
import java.util.List;

/**
 * �û����ò�ѯ��������ݣ�Ϊ������ѯ��׼��
 * @author chenfeic
 *
 */
public class SqlSetting {

	/** �������� */
	private String driverName = null;

	/** Connection_URL */
	private String connUrl = null;

	/** �û��� */
	private String userName = null;

	/** ���� */
	private String password = null;

	/** ��ѯ��� */
	private List<String> sqlList = null;

	/** ��ѯ�û��� */
	private int userCount;
	
	/** ���ӳ����������*/
	private int maxPoolSize;
	
	/** ���ӳ���С������*/
	private int minPoolSize;
	
	/** ���ӳس�ʼ����*/
	private int initPoolSize;
	
	/** ���ӳ�ʱʱ��*/
	private int maxIdleTime;
	
	/** ������·�� */
	private String outputPath = null;
	
	

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getConnUrl() {
		return connUrl;
	}

	public void setConnUrl(String connUrl) {
		this.connUrl = connUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getQuerySql() {
		if(sqlList == null) {
			sqlList = new ArrayList<String>();
		}
		return sqlList;
	}

	public void setQuerySql(List<String> sqlList) {
		this.sqlList = sqlList;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public int getInitPoolSize() {
		return initPoolSize;
	}

	public void setInitPoolSize(int initPoolSize) {
		this.initPoolSize = initPoolSize;
	}

	public int getMaxIdleTime() {
		return maxIdleTime;
	}

	public void setMaxIdleTime(int maxIdleTime) {
		this.maxIdleTime = maxIdleTime;
	}
}
