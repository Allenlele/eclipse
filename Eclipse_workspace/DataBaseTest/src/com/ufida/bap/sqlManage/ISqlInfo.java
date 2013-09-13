package com.ufida.bap.sqlManage;

import java.util.List;

/**
 * sql��Ϣ��
 * 
 * @author chenfeic
 * 
 */
public interface ISqlInfo {

	/**
	 * 
	 * @return sql�����
	 */
	public int getSqlId();

	/**
	 * 
	 * @return ����sql���
	 */
	public String getContent();

	/**
	 * 
	 * @return Sql���ִ�еĴ���
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
	 * @return ���ִ��ʱ��
	 */
	public long getMaxExecTime();

	/**
	 * 
	 * @return ��Сִ��ʱ��
	 */
	public long getMinExecTime();

	/***
	 * 
	 * @return ƽ��ִ��ʱ��
	 */
	public long getVerageTime();

}
