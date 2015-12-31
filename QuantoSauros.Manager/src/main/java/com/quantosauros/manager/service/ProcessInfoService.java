package com.quantosauros.manager.service;

import java.util.List;

import com.quantosauros.manager.model.settings.ProcessInfo;

public interface ProcessInfoService {

	List<ProcessInfo> selectProcessInfo();
	ProcessInfo findByProcId(String procId);
	String getMaxProcId();
	void saveOrUpdate(ProcessInfo processInfo);	
	void delete(String procId);
}
