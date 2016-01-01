package com.quantosauros.manager.service.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.settings.PortfolioInfoDao;
import com.quantosauros.manager.model.settings.PortfolioData;
import com.quantosauros.manager.model.settings.PortfolioDataForm;
import com.quantosauros.manager.model.settings.PortfolioInfo;

@Service("portfolioInfoService")
public class PortfolioInfoServiceImpl implements PortfolioInfoService{

	PortfolioInfoDao portfolioInfoDao;
	
	@Autowired
	public void setPortfolioInfoDao(PortfolioInfoDao portfolioInfoDao){
		this.portfolioInfoDao = portfolioInfoDao;
	}
	
	@Override
	public List<PortfolioInfo> getLists() {
		return portfolioInfoDao.getLists();
	}
	
	@Override
	public PortfolioInfo getOneById(String portfolioId) {
		return portfolioInfoDao.getOneById(portfolioId);
	}
	
	@Override
	public String getMaxPortfolioId() {
		// TODO Auto-generated method stub
		return portfolioInfoDao.getMaxPortfolioId();
	}
	
	@Override
	public void saveOrUpdate(PortfolioInfo portfolioInfo, 
			PortfolioDataForm portfolioDataForm) {
		//Portfolio Info
		String portfolioId = "";
		if (getOneById(portfolioInfo.getPortfolioId()) == null){
			portfolioId = portfolioInfoDao.getMaxPortfolioId();
			portfolioInfo.setPortfolioId(portfolioId);
			portfolioInfoDao.insertPortfolioInfo(portfolioInfo);			
		} else {
			portfolioId = portfolioInfo.getPortfolioId();
			portfolioInfoDao.updatePortfolioInfo(portfolioInfo);
		}
		//Portfolio Data
		portfolioInfoDao.deletePortfolioData(portfolioId);
		List<PortfolioData> dataList = portfolioDataForm.getPortfolioDatas();
		for (int dataIndex = 0; dataIndex < dataList.size(); dataIndex++){
			PortfolioData portfolioData = dataList.get(dataIndex);
			String flag = portfolioData.getFlag();
			if (flag != null){
				portfolioData.setPortfolioId(portfolioId);				
				portfolioInfoDao.insertPortfolioData(portfolioData);				
			}			
		}		
	}
	
	@Override
	public void delete(String portfolioId) {
		portfolioInfoDao.deletePortfolioInfo(portfolioId);
		portfolioInfoDao.deletePortfolioData(portfolioId);
	}
	
	@Override
	public List<PortfolioData> getDataLists(String portfolioId) {
		return portfolioInfoDao.getDataListsById(portfolioId);
	}	
}
