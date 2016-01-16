package com.quantosauros.manager.dao.settings;

import java.util.List;

import com.quantosauros.manager.model.settings.PortfolioDataModel;
import com.quantosauros.manager.model.settings.PortfolioInfoModel;

public interface PortfolioInfoDao {

	List<PortfolioInfoModel> getLists();
	PortfolioInfoModel getOneById(String portfolioId);
	String getMaxPortfolioId();
	void insertPortfolioInfo(PortfolioInfoModel portfolioInfoModel);
	void updatePortfolioInfo(PortfolioInfoModel portfolioInfoModel);
	void deletePortfolioInfo(String portfolioId);
	
	List<PortfolioDataModel> getDataListsById(String portfolioId);	
	void insertPortfolioData(PortfolioDataModel portfolioDataModel);	
	void deletePortfolioData(String portfolioId);
	
}
