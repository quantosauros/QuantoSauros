package com.quantosauros.manager.model.pricer;

import java.util.List;

public class LegPricerModelForm {

	private List<LegInfoPricerModel> legInfoPricerModels;
	private List<LegPeriodPricerModel> legPeriodPricerModels;
	
	public void setLegInfoPricerModels(List<LegInfoPricerModel> legInfoPricerModels){
		this.legInfoPricerModels = legInfoPricerModels;
	}
	public List<LegInfoPricerModel> getLegInfoPricerModels(){
		return legInfoPricerModels;
	}
	public void setLegPeriodPricerModels(List<LegPeriodPricerModel> legPeriodPricerModels){
		this.legPeriodPricerModels = legPeriodPricerModels;
	}
	public List<LegPeriodPricerModel> getLegPeriodPricerModels(){
		return legPeriodPricerModels;
	}
}
