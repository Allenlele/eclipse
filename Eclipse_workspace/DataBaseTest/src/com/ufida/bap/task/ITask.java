package com.ufida.bap.task;

import com.ufida.bap.executor.SqlSetting;

/**
 * sql任务接口
 * @author chenfeic
 *
 */
public interface ITask {
	
	public void exec(SqlSetting sqlSetting);

}
