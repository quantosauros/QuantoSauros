package com.quantosauros.manager.service.settings;

import java.util.List;

import com.quantosauros.manager.model.settings.ProcessInfoModel;

public interface ProcessInfoService {

	List<ProcessInfoModel> selectProcessInfo();
	ProcessInfoModel findByProcId(String procId);
	String getMaxProcId();
	void saveOrUpdate(ProcessInfoModel processInfoModel);	
	void delete(String procId);
}
