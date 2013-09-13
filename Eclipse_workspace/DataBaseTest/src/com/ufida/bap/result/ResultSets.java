package com.ufida.bap.result;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * 临时存放每个线程的查询结果集
 * @author chenfeic
 *
 */
public class ResultSets {
	
	/**key：进程id, value：对象进行查询的结果集*/
	private Hashtable<String, ThreadInfo> results = new Hashtable<String, ThreadInfo>();

	public Hashtable<String, ThreadInfo> getBuffer() {
		return results;
	}

	public void setBuffer(Hashtable<String, ThreadInfo> buffer) {
		this.results = buffer;
	}
	
	/**
	 * 
	 * @return 所有线程的查询结果集
	 */
	public String getResult() {
		Collection<ThreadInfo> values = results.values();
		StringBuilder sb = new StringBuilder();
		Iterator<ThreadInfo> iterator = values.iterator();
		boolean isTitle = true;//标题行
		while(iterator.hasNext()) {
			ThreadInfo result = iterator.next();
			if(isTitle) {
				sb.append(result.getThreadInfoTitle());
			}
			sb.append(result.toString());
			isTitle = false;
		}
		return sb.toString();
	}
	
	/***
	 * @author chenfeic
	 * 
	 * 清除结果集
	 */
	public void clearResultSets() {
		results.clear();
	}
	

}
