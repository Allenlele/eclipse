package com.ufida.bap.result;

import org.apache.commons.lang.StringUtils;

import com.ufida.bap.log.LoggerManager;

/***
 * 查询操作后的线程信息
 * 
 * @author chenfeic
 * 
 */
public class ThreadInfo {

	private static final String THREAD_ID = "THREAD_";
	private static final String BEGIN_TIME = "BeginTime(Second)";
	private static final String END_TIME = "EndTime(Second)";
	private static final String COST_TIME = "CostTime(Second)";
	private static final String BEFORE_CPU_USAGE = "CPU Utilization Before the Query";
	private static final String AFTER_CPU_USAGE = "CPU Utilization After the Query";
	private static final String BEFORE_MEMORY_USAGE = "Memory Utilization Before the Query";
	private static final String AFTER_MEMORY_USAGE = "Memory Utilization After the Query";
	private static final String SQL = "Sql Or The Number of Sql(1-22)";
	public static final String DELIMITER = "！@#￥%￥#@！";
	// private static final String TABLE = "\r\n";

	/** 进程ID */
	private long threadId;

	/** 开始时间 */
	private long beginTime;

	/** 结束时间 */
	private long endTime;

	/** 查询前内存使用率 */
	private double before_MemoryUsage;

	/** 查询后内存使用率 */
	private double after_MemoryUsage;

	/** 查询前CPU使用率 */
	private double before_CpuUsage;

	/** 查询后CPU使用率 */
	private double after_CpuUsage;

	/** 查询的sql语句 */
	private String sql;

	public String getThreadId() {
		StringBuilder sb = new StringBuilder(THREAD_ID);
		sb.append(threadId).append(DELIMITER);
		return sb.toString();
	}

	public String getBeginTime() {
		return getColValue(beginTime);
	}

	public String getEndTime() {
		return getColValue(endTime);
	}

	public String getCostTime() {
		return getColValue(endTime - beginTime);
	}

	public String getBeforeMemoryUsage() {
		return getColValue(before_MemoryUsage);
	}

	public String getAfterMemoryUsage() {
		return getColValue(after_MemoryUsage);
	}

	public String getBeforeCpuUsage() {
		return getColValue(before_CpuUsage);
	}

	public String getAfterCpuUsage() {
		return getColValue(after_CpuUsage);
	}

	public String getSql() {
		if (StringUtils.isEmpty(sql)) {
			LoggerManager.getInstance().getLogger()
					.error("Error!!!Sql is NULL!");
			return null;
		}
		StringBuilder sb = new StringBuilder(sql);
		sb.append(DELIMITER);
		return sb.toString();
	}

	private String getColValue(double value) {
		StringBuilder sb = new StringBuilder();
		sb.append(value).append(DELIMITER);
		return sb.toString();
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public void setBeforeMemoryUsage(double beforememUsage) {
		this.before_MemoryUsage = beforememUsage;
	}

	public void setBeforeCpuUsage(double before_CpuUsage) {
		this.before_CpuUsage = before_CpuUsage;
	}

	public void setAfter_MemoryUsage(double after_MemoryUsage) {
		this.after_MemoryUsage = after_MemoryUsage;
	}

	public void setAfter_CpuUsage(double after_CpuUsage) {
		this.after_CpuUsage = after_CpuUsage;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getThreadInfoTitle() {
		StringBuilder sb = new StringBuilder();
		return sb.append("Thread_ID").append(DELIMITER).append(BEGIN_TIME)
				.append(DELIMITER).append(END_TIME).append(DELIMITER)
				.append(COST_TIME).append(DELIMITER).append(BEFORE_CPU_USAGE)
				.append(DELIMITER).append(AFTER_CPU_USAGE).append(DELIMITER)
				.append(BEFORE_MEMORY_USAGE).append(DELIMITER)
				.append(AFTER_MEMORY_USAGE).append(DELIMITER).append(SQL)
				.append(DELIMITER).toString();

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(getThreadId()).append(getBeginTime())
				.append(getEndTime()).append(getCostTime())
				.append(getBeforeCpuUsage()).append(getAfterCpuUsage())
				.append(getBeforeMemoryUsage()).append(getAfterMemoryUsage())
				.append(getSql()).toString();

	}

}
