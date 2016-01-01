package com.quantosauros.manager.dao.settings;

import java.util.List;

import com.quantosauros.manager.model.settings.PortfolioData;
import com.quantosauros.manager.model.settings.PortfolioInfo;

public interface PortfolioInfoDao {

	List<PortfolioInfo> getLists();
	PortfolioInfo getOneById(String portfolioId);
	String getMaxPortfolioId();
	void insertPortfolioInfo(PortfolioInfo portfolioInfo);
	void updatePortfolioInfo(PortfolioInfo portfolioInfo);
	void deletePortfolioInfo(String portfolioId);
	
	List<PortfolioData> getDataListsById(String portfolioId);	
	void insertPortfolioData(PortfolioData portfolioData);	
	void deletePortfolioData(String portfolioId);
	
}
