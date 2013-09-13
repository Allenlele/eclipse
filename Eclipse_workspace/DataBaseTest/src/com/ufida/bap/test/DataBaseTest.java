package com.ufida.bap.test;

import org.apache.commons.lang.ArrayUtils;

import com.ufida.bap.executor.GenerateSql;
import com.ufida.bap.log.LoggerManager;
import com.ufida.bap.task.ITask;
import com.ufida.bap.task.QueryTask;

public class DataBaseTest {

	public static void main(String[] args) {
		GenerateSql generateSql = null;
		if(ArrayUtils.isEmpty(args)) {
			generateSql = new GenerateSql();
		} else {
			generateSql = new GenerateSql(args[0]);
		}
		ITask queryTask = new QueryTask();
		queryTask.exec(generateSql.getSqlSetting());
		LoggerManager.getInstance().getLogger().debug("±æ¥Œ≤‚ ‘Ω· ¯");
	}

}
