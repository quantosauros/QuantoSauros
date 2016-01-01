package com.quantosauros.manager.service.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.settings.ProcessInfoDao;
import com.quantosauros.manager.model.settings.ProcessInfo;

@Service("processInfoService")
public class ProcessInfoServiceImpl implements ProcessInfoService {

	ProcessInfoDao processInfoDao;
	
	@Autowired
	public void setProcessInfoDao(ProcessInfoDao processInfoDao){
		this.processInfoDao = processInfoDao;
	}
	
	public List<ProcessInfo> selectProcessInfo(){
		return processInfoDao.selectProcessInfo();
	}
	public String getMaxProcId(){
		return processInfoDao.getMaxProcId();
	}
	public ProcessInfo findByProcId(String procId){
		return processInfoDao.findByProcId(procId);
	}
	
	public void saveOrUpdate(ProcessInfo processInfo){
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
