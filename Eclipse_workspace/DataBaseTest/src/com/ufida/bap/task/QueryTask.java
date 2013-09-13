package com.ufida.bap.task;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import com.csvreader.CsvWriter;
import com.ufida.bap.executor.SqlExector;
import com.ufida.bap.executor.SqlSetting;
import com.ufida.bap.log.LoggerManager;
import com.ufida.bap.result.ResultSets;
import com.ufida.bap.result.ThreadInfo;
import com.ufida.bap.sqlManage.ISqlInfo;
import com.ufida.bap.sqlManage.SqlManager;

/**
 * 数据库查询任务
 * 
 * @author chenfeic
 * 
 */
public class QueryTask implements ITask {
	public static ResultSets reslutSets = new ResultSets();

	@Override
	public void exec(SqlSetting sqlSetting) {
		int count = sqlSetting.getUserCount();
		final CountDownLatch countDownLatch = new CountDownLatch(count);
		for (int i = 0; i < count; i++) {
			SqlExector sqlExector = new SqlExector(countDownLatch, sqlSetting);
			Thread thread = new Thread(sqlExector);
			thread.start();
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e1) {
			LoggerManager.getInstance().getLogger().info(e1.getMessage(), e1);
		}
		wirteResult(sqlSetting);
	}

	private void wirteResult(SqlSetting sqlSetting) {
		// 将结果写入到本地文件中
		CsvWriter csvWriter = null;
		try {
			String outputPath = sqlSetting.getOutputPath();
			// 为了区别不同查询的查询结果，在原有设置的文件名上+当前日期+查询的线程个数
			long curTime = System.currentTimeMillis();
			Date date = new Date(curTime);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
			String str_date = formatter.format(date);
			String str_count = "_" + String.valueOf(sqlSetting.getUserCount());
			if (StringUtils.isEmpty(outputPath)) {
				LoggerManager.getInstance().getLogger()
						.error("The Path of OuputFile is NULL!");
				return;
			}
			String[] splitPath = outputPath.split("\\.");// 需要转义字符
			if (splitPath.length != 2) {
				LoggerManager.getInstance().getLogger()
						.error("结果文件路径名不正确，格式必须是文件路径+文件后缀名，推荐使用csv格式");
				return;
			}
			String prefix = splitPath[0];// 前缀名
			String suffix = splitPath[1];// 后缀名（最好是csv格式）
			StringBuffer fileName = new StringBuffer();
			fileName.append(prefix).append(str_date).append(str_count)
					.append(".").append(suffix);// 文件名+时间+_当前查询线程数+后缀名
			String[] values = reslutSets.getResult()
					.split(ThreadInfo.DELIMITER);
			if (values.length == 1) {
				return;
			}
			csvWriter = new CsvWriter(
					new FileWriter(fileName.toString(), true), ',');
			for (int i = 0; i < values.length; i++) {
				if (i % 9 == 0 && i != 0) {
					csvWriter.endRecord();// 换行，8列
				}
				csvWriter.write(values[i]);
			}
			csvWriter.endRecord();
			String[] sqlStatistics = getStatistics4Sql();
			if (!ArrayUtils.isEmpty(sqlStatistics)) {
				csvWriter.write("Statistics for Each Query:");
				csvWriter.endRecord();
				String[] title = new String[]{"Sql_Num","ExectorCount","MinExecTime","MaxExecTime","AverageExecTime"};
				for(String s: title) {
					csvWriter.write(s);
				}
				csvWriter.endRecord();
				for (int i = 0; i < sqlStatistics.length; i++) {
					if (i % 5 == 0 && i != 0) {
						csvWriter.endRecord();
					}
					csvWriter.write(sqlStatistics[i]);
				}
			}
			csvWriter.endRecord();
			reslutSets.clearResultSets();// 清除所有缓存信息
		} catch (Exception e) {
			LoggerManager.getInstance().getLogger().info("结果写入文件出错", e);
			return;
		} finally {
			if (csvWriter != null) {
				try {
					if (csvWriter != null) {
						csvWriter.flush();
						csvWriter.close();
						csvWriter = null;
					}
				} catch (IOException e) {
					LoggerManager.getInstance().getLogger()
							.info(e.getMessage(), e);
				}
			}
		}

	}

	private String[] getStatistics4Sql() {
		List<ISqlInfo> SqlInfos = SqlManager.getInstance().getAllSqlInfo();
		List<String> sqlStatistics = new ArrayList<String>();
		for (ISqlInfo sqlInfo : SqlInfos) {
			sqlStatistics.add(String.valueOf(sqlInfo.getSqlId()));
			sqlStatistics.add(String.valueOf(sqlInfo.getExecCount()));
			sqlStatistics.add(String.valueOf(sqlInfo.getMinExecTime()));
			sqlStatistics.add(String.valueOf(sqlInfo.getMaxExecTime()));
			sqlStatistics.add(String.valueOf(sqlInfo.getVerageTime()));
		}
		return sqlStatistics.toArray(new String[0]);
	}
}
