package com.ufida.bap.sqlManage;

/**
 * ÿ��sql��ִ�����
 * 
 * @author chenfeic
 *
 */
public interface ISqlExecInfo {
	
	/**
	 * ִ��һ��Sql��������ĵ��¼�
	 * @return
	 */
	public long getExecTime();
	
	/**
	 * 
	 * @param execTime ִ��һ��sql������ĵ�ʱ��
	 */
	public void setExecTime(long execTime);
	
	

}
