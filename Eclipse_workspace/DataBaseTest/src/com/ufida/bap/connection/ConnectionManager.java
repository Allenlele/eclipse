package com.ufida.bap.connection;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.ufida.bap.executor.SqlSetting;
import com.ufida.bap.log.LoggerManager;

/**
 * 数据库连接管理类
 * @author chenfeic
 *
 */
public class ConnectionManager {

	private static ConnectionManager instance = null;

	private ComboPooledDataSource dataSource = null;

	private ConnectionManager() {
		dataSource = new ComboPooledDataSource();
	}

	public synchronized static ConnectionManager getInstance() {
		if(instance == null) {
			instance = new ConnectionManager();
		}
		return instance;
	}

	public synchronized Connection getConnection(SqlSetting sqlSetting) {
		Connection connection = null;
		try {
			dataSource.setDriverClass(sqlSetting.getDriverName());
			dataSource.setJdbcUrl(sqlSetting.getConnUrl());
			dataSource.setUser(sqlSetting.getUserName());
			dataSource.setPassword(sqlSetting.getPassword());
			dataSource.setMaxPoolSize(sqlSetting.getMaxPoolSize());
			dataSource.setMinPoolSize(sqlSetting.getMinPoolSize());
			dataSource.setInitialPoolSize(sqlSetting.getInitPoolSize());
			dataSource.setMaxIdleTime(sqlSetting.getMaxIdleTime());
			connection = dataSource.getConnection();
		} catch (PropertyVetoException e1) {
			LoggerManager.getInstance().getLogger().info(e1.getMessage(), e1);
		} catch (SQLException e) {
			LoggerManager.getInstance().getLogger().info("获取连接失败", e);
		}
		return connection;
	}

}
