package com.quantosauros.manager.model.settings;

import java.util.List;

public class PortfolioDataModelForm {

	private List<PortfolioDataModel> portfolioDataModels;
	
	public List<PortfolioDataModel> getPortfolioDatas(){
		return portfolioDataModels;
	}
	public void setPortfolioDatas(List<PortfolioDataModel> portfolioDataModels){
		this.portfolioDataModels = portfolioDataModels;
	}
	
}
