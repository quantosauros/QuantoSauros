package com.quantosauros.manager.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.DeltaInfoDao;
import com.quantosauros.manager.model.DeltaInfo;

@Service("deltaInfoService")
public class DeltaInfoServiceImpl implements DeltaInfoService {

	DeltaInfoDao deltaInfoDao;
	
	@Autowired
	public void setDeltaInfoDao(DeltaInfoDao deltaInfoDao){
		this.deltaInfoDao = deltaInfoDao;
	}
	
	@Override
	public List<DeltaInfo> selectAllList(String procId) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("procId", procId);
				
		return deltaInfoDao.selectAllList(paramMap);
	}
	
	@Override
	public List<DeltaInfo> selectDeltaForChart(String procId, String dt, 
			String instrumentCd, String greekCd, String ircCd) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("procId", procId);
		paramMap.put("dt", dt);
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("greekCd", greekCd);
		paramMap.put("ircCd", ircCd);		
		
		
		return deltaInfoDao.selectDeltaForChart(paramMap);
	}
	
	@Override
	public List<DeltaInfo> selectDeltaForChart2(String procId, String greekCd, 
			String ircCd, String mrtyCd, String nonCallCd, String instrumentCd) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("procId", procId);
		paramMap.put("greekCd", greekCd);
		paramMap.put("ircCd", ircCd);
		paramMap.put("mrtyCd", mrtyCd);
		paramMap.put("nonCallCd", nonCallCd);
		paramMap.put("instrumentCd", instrumentCd);
		
		return deltaInfoDao.selectDeltaForChart2(paramMap);
	}
	
	@Override
	public List<String> selectMrtyCd(String instrumentCd, String ircCd) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("ircCd", ircCd);

		return deltaInfoDao.selectMrtyCd(paramMap);
	}
	
}
