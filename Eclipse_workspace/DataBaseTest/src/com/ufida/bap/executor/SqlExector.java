package com.ufida.bap.executor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ufida.bap.connection.ConnectionManager;
import com.ufida.bap.log.LoggerManager;
import com.ufida.bap.result.ThreadInfo;
import com.ufida.bap.sqlManage.ISqlExecInfo;
import com.ufida.bap.sqlManage.ISqlInfo;
import com.ufida.bap.sqlManage.SqlExecInfo;
import com.ufida.bap.sqlManage.SqlManager;
import com.ufida.bap.task.QueryTask;
import com.ufida.bap.utils.OSUtils;

/**
 * Sql查询执行器
 * 
 * @author chenfeic
 * 
 */
public class SqlExector implements Runnable {
	private CountDownLatch countDownLatch;
	private SqlSetting sqlSetting;

	public SqlExector(CountDownLatch countDownLatch, SqlSetting sqlSetting) {
		this.countDownLatch = countDownLatch;
		this.sqlSetting = sqlSetting;
	}

	@Override
	public void run() {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ISqlInfo sqlInfo = null;
		ISqlExecInfo sqlExecInfo = new SqlExecInfo();
		String temp_sql = null;
		try {
			ConnectionManager connManager = ConnectionManager.getInstance();
			con = connManager.getConnection(sqlSetting);
			if (con == null) {
				return;
			}
			st = con.createStatement();
			List<String> sqlList = sqlSetting.getQuerySql();
			// 随机生成一个查询语句
			String sql = null;
			int sqlCount = sqlList.size();// 用于查询的sql的数目
			Random random = new Random();
			int sqlNum = random.nextInt(sqlCount);// 随机选择一条sql语句查询
			temp_sql = sqlList.get(sqlNum);
			if (SqlManager.getInstance().isDefaultSql(temp_sql)) {// 如果是系统默认的sql,则从缓存中取
				sqlInfo = SqlManager.getInstance().getSql(temp_sql);
				sql = sqlInfo.getContent();
			} else {
				sql = temp_sql;
			}
			ThreadInfo threadInfo = new ThreadInfo();
			long id = Thread.currentThread().getId();
			LoggerManager
					.getInstance()
					.getLogger()
					.debug("================================Thread_"
							+ id
							+ " begin to Query==================================");
			LoggerManager
					.getInstance()
					.getLogger()
					.debug("=================================The Sql or the Num of Sql is "
							+ temp_sql + "=================================");
			threadInfo.setThreadId(id);
			threadInfo.setBeforeMemoryUsage(OSUtils.memoryUsage());// OSUtils.memoryUsage()//查询前内存使用率
			threadInfo.setBeforeCpuUsage(OSUtils.cpuUsage());// OSUtils.cpuUsage()//查询前CPU利用率
			long beginTime = System.currentTimeMillis() / 1000;
			threadInfo.setBeginTime(beginTime);// 查询开始时间
			Pattern pattern = Pattern.compile("\\s+");
			Matcher matcher = pattern.matcher(sql);
			if (matcher.find()) {
				sql = matcher.replaceAll(" ");
			}
			String[] batch_sql = sql.split(";");// 默认的sql语句里包含有多条语句
			for (String s : batch_sql) {// TODO 批处理由于不支持select查询，不能使用，考虑使用存储过程。。
				rs = st.executeQuery(s);
			}
			long endTime = System.currentTimeMillis() / 1000;
			threadInfo.setEndTime(endTime);// 查询结束时间
			threadInfo.setAfter_MemoryUsage( OSUtils.memoryUsage());// OSUtils.memoryUsage()查询后内存使用率
			threadInfo.setAfter_CpuUsage(OSUtils.cpuUsage());// OSUtils.cpuUsage() 查询后cpu利用率
			threadInfo.setSql(temp_sql);
			sqlExecInfo.setExecTime(endTime - beginTime);
			if (sqlInfo != null) {
				sqlInfo.addExecInfo(sqlExecInfo);
			}
			QueryTask.reslutSets.getBuffer().put(
					Thread.currentThread().getName(), threadInfo);
		} catch (SQLException e) {
			LoggerManager.getInstance().getLogger()
					.info("查询出错，出错的语句为： " + temp_sql, e);
		} finally {
			countDownLatch.countDown();
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				LoggerManager.getInstance().getLogger().info(e.getMessage(), e);
			}
		}

	}

}
