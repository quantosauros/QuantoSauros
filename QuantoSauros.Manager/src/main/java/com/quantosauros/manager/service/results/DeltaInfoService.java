package com.quantosauros.manager.service.results;

import java.util.List;

import com.quantosauros.manager.model.results.DeltaInfo;

public interface DeltaInfoService {

	List<DeltaInfo> selectAllList(String procId);
	List<DeltaInfo> selectDeltaForChart(String procId, String dt, 
			String instrumentCd, String greekCd, String ircCd, String nonCallCd);
	List<DeltaInfo> selectDeltaForChart2(String procId, String greekCd, 
			String ircCd, String mrtyCd, String nonCallCd, String instrumentCd);	
	List<String> selectMrtyCd(String instrumentCd, String ircCd);
	
}
