package com.ufida.bap.sqlManage;

import java.util.List;

/**
 * sql信息类
 * 
 * @author chenfeic
 * 
 */
public interface ISqlInfo {

	/**
	 * 
	 * @return sql语句编号
	 */
	public int getSqlId();

	/**
	 * 
	 * @return 返回sql语句
	 */
	public String getContent();

	/**
	 * 
	 * @return Sql语句执行的次数
	 */
	public int getExecCount();
	
	/**
	 * 
	 * @return
	 */
	public List<ISqlExecInfo> getExecInfoList();

	/**
	 * 
	 * @param sqlExecInfo
	 */
	public void addExecInfo(ISqlExecInfo sqlExecInfo);

	/**
	 * 
	 * @return 最大执行时间
	 */
	public long getMaxExecTime();

	/**
	 * 
	 * @return 最小执行时间
	 */
	public long getMinExecTime();

	/***
	 * 
	 * @return 平均执行时间
	 */
	public long getVerageTime();

}
