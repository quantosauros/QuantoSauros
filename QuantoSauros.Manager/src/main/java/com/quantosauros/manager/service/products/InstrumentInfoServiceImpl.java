package com.quantosauros.manager.service.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.products.InstrumentInfoDao;
import com.quantosauros.manager.model.products.InstrumentInfo;

@Service("instrumentInfoService")
public class InstrumentInfoServiceImpl implements InstrumentInfoService {
	
	InstrumentInfoDao instrumentInfoDao;
	
	@Autowired
	public void setInstrumentInfoDao(InstrumentInfoDao instrumentInfoDao){
		this.instrumentInfoDao = instrumentInfoDao;
	}
	
	@Override
	public List<InstrumentInfo> getLists() {	
		return instrumentInfoDao.getLists();
	}

	@Override
	public InstrumentInfo getOne(String instrumentCd) {		
		return instrumentInfoDao.getOne(instrumentCd);
	}
	
}
