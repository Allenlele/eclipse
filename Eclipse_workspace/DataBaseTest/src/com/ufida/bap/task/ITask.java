package com.ufida.bap.task;

import com.ufida.bap.executor.SqlSetting;

/**
 * sql����ӿ�
 * @author chenfeic
 *
 */
public interface ITask {
	
	public void exec(SqlSetting sqlSetting);

}
