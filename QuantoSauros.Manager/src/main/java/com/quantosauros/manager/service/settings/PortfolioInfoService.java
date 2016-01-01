package com.quantosauros.manager.service.settings;

import java.util.List;

import com.quantosauros.manager.model.settings.PortfolioData;
import com.quantosauros.manager.model.settings.PortfolioDataForm;
import com.quantosauros.manager.model.settings.PortfolioInfo;

public interface PortfolioInfoService {
	
	List<PortfolioInfo> getLists();
	PortfolioInfo getOneById(String portfolioId);
	String getMaxPortfolioId();
	void saveOrUpdate(PortfolioInfo portfolioInfo, PortfolioDataForm portfolioDataForm);
	void delete(String portfolioId);

	List<PortfolioData> getDataLists(String portfolioId);	
	
}
