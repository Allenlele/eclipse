package com.ufida.bap.sqlManage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.ufida.bap.log.LoggerManager;

/** Sql查询语句管理类 */
public class SqlManager {

	private static SqlManager instance = null;

	private Hashtable<String, ISqlInfo> sqlCache;

	private List<String> sqlList = new ArrayList<String>();

	private SqlManager() {
		sqlCache = new Hashtable<String, ISqlInfo>();
		initSqlInfo();
	}

	private void initSqlInfo() {
		// TODO 用配置文件配置进去
		Properties prop = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream("DefaultQuery.properties");
			prop.load(in);
		} catch (FileNotFoundException e) {
			LoggerManager.getInstance().getLogger().info("shuxingwqenjianmeizhaodao", e);
		} catch (IOException e) {
			LoggerManager.getInstance().getLogger().info("ziyuanwenjianduqucuowu", e);
		}
		//moren22tiao
		for(int i=0;i<22;i++) {
			sqlList.add( prop.getProperty(String.valueOf(i+1)));
			sqlCache.put(String.valueOf(i + 1), new SqlInfo(i + 1, sqlList
					.get(i)));
		}
	}

	public static synchronized SqlManager getInstance() {
		if (instance == null) {
			instance = new SqlManager();
		}
		return instance;
	}

	public ISqlInfo getSql(String sqlNum) {
		return sqlCache.get(sqlNum);
	}

	public void addSql(int sqlNum, String sql) {
		if (StringUtils.isEmpty(sql)) {
			LoggerManager.getInstance().getLogger().error(
					"Error! The sql is null");
			return;
		}
		sqlCache.put(String.valueOf(sqlNum), new SqlInfo(sqlNum, sql));

	}

	public List<ISqlInfo> getAllSqlInfo() {
		List<ISqlInfo> sqlList = new ArrayList<ISqlInfo>();
		Collection<ISqlInfo> values = sqlCache.values();
		Iterator<ISqlInfo> iterator = values.iterator();
		while (iterator.hasNext()) {
			ISqlInfo sqlInfo = iterator.next();
			sqlList.add(sqlInfo);
		}
		return sqlList;
	}

	public List<String> getAllSqlNum() {
		List<String> sqlNumList = new ArrayList<String>();
		Enumeration<String> keys = sqlCache.keys();
		while (keys.hasMoreElements()) {
			String sqlNum = keys.nextElement();
			sqlNumList.add(sqlNum);
		}
		return sqlNumList;
	}

	public boolean isDefaultSql(String key) {
		return sqlCache.containsKey(key);
	}

}
