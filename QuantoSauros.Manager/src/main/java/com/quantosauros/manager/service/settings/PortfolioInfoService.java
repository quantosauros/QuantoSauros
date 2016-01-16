package com.quantosauros.manager.service.settings;

import java.util.List;

import com.quantosauros.manager.model.settings.PortfolioDataModel;
import com.quantosauros.manager.model.settings.PortfolioDataModelForm;
import com.quantosauros.manager.model.settings.PortfolioInfoModel;

public interface PortfolioInfoService {
	
	List<PortfolioInfoModel> getLists();
	PortfolioInfoModel getOneById(String portfolioId);
	String getMaxPortfolioId();
	void saveOrUpdate(PortfolioInfoModel portfolioInfoModel, PortfolioDataModelForm portfolioDataModelForm);
	void delete(String portfolioId);

	List<PortfolioDataModel> getDataLists(String portfolioId);	
	
}
