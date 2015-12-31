package com.quantosauros.manager.dao.settings;

import java.util.List;

import com.quantosauros.manager.model.settings.ProcessInfo;

public interface ProcessInfoDao {

	List<ProcessInfo> selectProcessInfo();
	ProcessInfo findByProcId(String procId);
	String getMaxProcId();
	void insertProcessInfo(ProcessInfo processInfo);
	void updateProcessInfo(ProcessInfo processInfo);
	void deleteProcessInfo(String procId);
}
