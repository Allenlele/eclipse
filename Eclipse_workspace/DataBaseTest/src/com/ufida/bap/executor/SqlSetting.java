package com.ufida.bap.executor;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户设置查询必须的数据，为后续查询做准备
 * @author chenfeic
 *
 */
public class SqlSetting {

	/** 驱动名称 */
	private String driverName = null;

	/** Connection_URL */
	private String connUrl = null;

	/** 用户名 */
	private String userName = null;

	/** 密码 */
	private String password = null;

	/** 查询语句 */
	private List<String> sqlList = null;

	/** 查询用户数 */
	private int userCount;
	
	/** 连接池最大连接数*/
	private int maxPoolSize;
	
	/** 连接池最小连接数*/
	private int minPoolSize;
	
	/** 连接池初始接数*/
	private int initPoolSize;
	
	/** 连接超时时间*/
	private int maxIdleTime;
	
	/** 结果输出路径 */
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
