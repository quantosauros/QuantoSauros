package com.quantosauros.manager.dao.settings;

import java.util.List;

import com.quantosauros.manager.model.settings.ProcessInfoModel;

public interface ProcessInfoDao {

	List<ProcessInfoModel> selectProcessInfo();
	ProcessInfoModel findByProcId(String procId);
	String getMaxProcId();
	void insertProcessInfo(ProcessInfoModel processInfo);
	void updateProcessInfo(ProcessInfoModel processInfo);
	void deleteProcessInfo(String procId);
}
