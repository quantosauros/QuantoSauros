package com.quantosauros.manager.service.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.settings.ProcessInfoDao;
import com.quantosauros.manager.model.settings.ProcessInfoModel;

@Service("processInfoService")
public class ProcessInfoServiceImpl implements ProcessInfoService {

	ProcessInfoDao processInfoDao;
	
	@Autowired
	public void setProcessInfoDao(ProcessInfoDao processInfoDao){
		this.processInfoDao = processInfoDao;
	}
	
	public List<ProcessInfoModel> selectProcessInfo(){
		return processInfoDao.selectProcessInfo();
	}
	public String getMaxProcId(){
		return processInfoDao.getMaxProcId();
	}
	public ProcessInfoModel findByProcId(String procId){
		return processInfoDao.findByProcId(procId);
	}
	
	public void saveOrUpdate(ProcessInfoModel processInfo){
		if (findByProcId(processInfo.getProcId()) == null){
			processInfo.setProcId(processInfoDao.getMaxProcId());			
			processInfoDao.insertProcessInfo(processInfo);
		} else {
			processInfoDao.updateProcessInfo(processInfo);
		}				
	}
	
	@Override
	public void delete(String procId) {
		processInfoDao.deleteProcessInfo(procId);		
	}
}
