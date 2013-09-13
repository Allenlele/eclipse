package com.ufida.bap.result;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * ��ʱ���ÿ���̵߳Ĳ�ѯ�����
 * @author chenfeic
 *
 */
public class ResultSets {
	
	/**key������id, value��������в�ѯ�Ľ����*/
	private Hashtable<String, ThreadInfo> results = new Hashtable<String, ThreadInfo>();

	public Hashtable<String, ThreadInfo> getBuffer() {
		return results;
	}

	public void setBuffer(Hashtable<String, ThreadInfo> buffer) {
		this.results = buffer;
	}
	
	/**
	 * 
	 * @return �����̵߳Ĳ�ѯ�����
	 */
	public String getResult() {
		Collection<ThreadInfo> values = results.values();
		StringBuilder sb = new StringBuilder();
		Iterator<ThreadInfo> iterator = values.iterator();
		boolean isTitle = true;//������
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
	 * ��������
	 */
	public void clearResultSets() {
		results.clear();
	}
	

}
