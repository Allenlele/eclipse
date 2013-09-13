package com.ufida.bap.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerManager {
	
	private static LoggerManager instance = null;
	
	private LoggerManager() {
		PropertyConfigurator.configure("log.properties");
	}
	
	private Logger logger =null;
	
	public synchronized static LoggerManager getInstance() {
		if(instance == null) {
			instance = new LoggerManager();
		}
		return instance;
	}
	
	public Logger getLogger() {
		if(logger == null) {
			logger =Logger.getLogger(LoggerManager.class);
		}
		return logger;
	}

}
