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
 * ���ݿ��ѯ����
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
		// �����д�뵽�����ļ���
		CsvWriter csvWriter = null;
		try {
			String outputPath = sqlSetting.getOutputPath();
			// Ϊ������ͬ��ѯ�Ĳ�ѯ�������ԭ�����õ��ļ�����+��ǰ����+��ѯ���̸߳���
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
			String[] splitPath = outputPath.split("\\.");// ��Ҫת���ַ�
			if (splitPath.length != 2) {
				LoggerManager.getInstance().getLogger()
						.error("����ļ�·��������ȷ����ʽ�������ļ�·��+�ļ���׺�����Ƽ�ʹ��csv��ʽ");
				return;
			}
			String prefix = splitPath[0];// ǰ׺��
			String suffix = splitPath[1];// ��׺���������csv��ʽ��
			StringBuffer fileName = new StringBuffer();
			fileName.append(prefix).append(str_date).append(str_count)
					.append(".").append(suffix);// �ļ���+ʱ��+_��ǰ��ѯ�߳���+��׺��
			String[] values = reslutSets.getResult()
					.split(ThreadInfo.DELIMITER);
			if (values.length == 1) {
				return;
			}
			csvWriter = new CsvWriter(
					new FileWriter(fileName.toString(), true), ',');
			for (int i = 0; i < values.length; i++) {
				if (i % 9 == 0 && i != 0) {
					csvWriter.endRecord();// ���У�8��
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
			reslutSets.clearResultSets();// ������л�����Ϣ
		} catch (Exception e) {
			LoggerManager.getInstance().getLogger().info("���д���ļ�����", e);
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
