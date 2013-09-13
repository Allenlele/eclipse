package com.ufida.bap.sqlManage;

public class SqlExecInfo implements ISqlExecInfo {
	
	private long execTime;
	
	@Override
	public long getExecTime() {
		return execTime;
	}

	@Override
	public void setExecTime(long execTime) {
		this.execTime = execTime;

	}

}
