package com.ufida.bap.sqlManage;

/**
 * 每个sql的执行情况
 * 
 * @author chenfeic
 *
 */
public interface ISqlExecInfo {
	
	/**
	 * 执行一次Sql语句所消耗的事件
	 * @return
	 */
	public long getExecTime();
	
	/**
	 * 
	 * @param execTime 执行一次sql语句消耗的时间
	 */
	public void setExecTime(long execTime);
	
	

}
