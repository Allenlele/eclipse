package com.ufida.bap.sqlManage;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于封装sql信息
 * 
 * @author chenfeic
 * 
 */
public class SqlInfo implements ISqlInfo {

	private List<ISqlExecInfo> execInfoList;

	/** sql语句编号 */
	private int id;

	/** sql语句内容 */
	private String content;

	public SqlInfo(int id, String content) {
		this.id = id;
		this.content = content;
		execInfoList = new ArrayList<ISqlExecInfo>();
	}

	@Override
	public int getSqlId() {
		return id;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public int getExecCount() {
		return execInfoList.size();
	}

	@Override
	public List<ISqlExecInfo> getExecInfoList() {
		return this.execInfoList;
	}

	@Override
	public long getMaxExecTime() {
		if (execInfoList.size() == 0) {
			return 0;
		}
		long maxExecTime = execInfoList.get(0).getExecTime();
		for (ISqlExecInfo execInfo : execInfoList) {
			if (execInfo.getExecTime() > maxExecTime) {
				maxExecTime = execInfo.getExecTime();
			}
		}

		return maxExecTime;
	}

	@Override
	public long getMinExecTime() {
		if(execInfoList.size()==0) {
			return 0;
		}
		long minEexcTime = execInfoList.get(0).getExecTime();
		for (ISqlExecInfo execInfo : execInfoList) {
			if (execInfo.getExecTime() < minEexcTime) {
				minEexcTime = execInfo.getExecTime();
			}
		}
		return minEexcTime;
	}

	@Override
	public long getVerageTime() {
		if(execInfoList.size()==0) {
			return 0;
		}
		long totalExecTime = 0;
		for (ISqlExecInfo execInfo : execInfoList) {
			totalExecTime += execInfo.getExecTime();
		}
		return totalExecTime / execInfoList.size();
	}

	@Override
	public synchronized void addExecInfo(ISqlExecInfo sqlExecInfo) {
		getExecInfoList().add(sqlExecInfo);
	}

}
