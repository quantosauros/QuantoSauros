package com.quantosauros.manager.service.results;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.results.DetailInfoDao;
import com.quantosauros.manager.model.results.DetailInfo;

@Service("detailInfoService")
public class DetailInfoServiceImpl implements DetailInfoService {

	DetailInfoDao detailInfoDao;
	
	@Autowired
	public void setDetailInfoDao(DetailInfoDao detailInfoDao){
		this.detailInfoDao = detailInfoDao;
	}
	
	@Override
	public List<DetailInfo> selectList(String instrumentCd,
			String procId, String valueType, String nonCallCd, String legType) {
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("procId", procId);
		paramMap.put("valueType", valueType);
		paramMap.put("nonCallCd", nonCallCd);		
		paramMap.put("legType", legType);
		
		return detailInfoDao.selectList(paramMap);
	}
	
}
