package com.quantosauros.manager.service.products;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.products.InstrumentInfoDao;
import com.quantosauros.manager.model.products.InstrumentInfoModel;

@Service("instrumentInfoService")
public class InstrumentInfoServiceImpl implements InstrumentInfoService {
	
	InstrumentInfoDao instrumentInfoDao;
	
	@Autowired
	public void setInstrumentInfoDao(InstrumentInfoDao instrumentInfoDao){
		this.instrumentInfoDao = instrumentInfoDao;
	}
	
	@Override
	public List<InstrumentInfoModel> getLists() {	
		return instrumentInfoDao.getLists();
	}

	@Override
	public InstrumentInfoModel getOne(String instrumentCd) {		
		return instrumentInfoDao.getOne(instrumentCd);
	}
	
}
