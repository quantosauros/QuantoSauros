package com.quantosauros.manager.service.settings;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantosauros.manager.dao.settings.PortfolioInfoDao;
import com.quantosauros.manager.model.settings.PortfolioDataModel;
import com.quantosauros.manager.model.settings.PortfolioDataModelForm;
import com.quantosauros.manager.model.settings.PortfolioInfoModel;

@Service("portfolioInfoService")
public class PortfolioInfoServiceImpl implements PortfolioInfoService{

	PortfolioInfoDao portfolioInfoDao;
	
	@Autowired
	public void setPortfolioInfoDao(PortfolioInfoDao portfolioInfoDao){
		this.portfolioInfoDao = portfolioInfoDao;
	}
	
	@Override
	public List<PortfolioInfoModel> getLists() {
		return portfolioInfoDao.getLists();
	}
	
	@Override
	public PortfolioInfoModel getOneById(String portfolioId) {
		return portfolioInfoDao.getOneById(portfolioId);
	}
	
	@Override
	public String getMaxPortfolioId() {
		// TODO Auto-generated method stub
		return portfolioInfoDao.getMaxPortfolioId();
	}
	
	@Override
	public void saveOrUpdate(PortfolioInfoModel portfolioInfoModel, 
			PortfolioDataModelForm portfolioDataModelForm) {
		//Portfolio Info
		String portfolioId = "";
		if (getOneById(portfolioInfoModel.getPortfolioId()) == null){
			portfolioId = portfolioInfoDao.getMaxPortfolioId();
			portfolioInfoModel.setPortfolioId(portfolioId);
			portfolioInfoDao.insertPortfolioInfo(portfolioInfoModel);			
		} else {
			portfolioId = portfolioInfoModel.getPortfolioId();
			portfolioInfoDao.updatePortfolioInfo(portfolioInfoModel);
		}
		//Portfolio Data
		portfolioInfoDao.deletePortfolioData(portfolioId);
		List<PortfolioDataModel> dataList = portfolioDataModelForm.getPortfolioDatas();
		for (int dataIndex = 0; dataIndex < dataList.size(); dataIndex++){
			PortfolioDataModel portfolioData = dataList.get(dataIndex);
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
	public List<PortfolioDataModel> getDataLists(String portfolioId) {
		return portfolioInfoDao.getDataListsById(portfolioId);
	}	
}
