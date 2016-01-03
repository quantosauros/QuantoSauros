package com.quantosauros.manager.model.pricer;

import java.util.List;

public class LegDataInfoPricerModelForm {

	List<LegDataInfoPricerModel> legDataInfoPricerModels;
	
	public void setLegDataInfoPricerModels(List<LegDataInfoPricerModel> legDataInfoPricerModels){
		this.legDataInfoPricerModels = legDataInfoPricerModels;
	}
	public List<LegDataInfoPricerModel> getLegDataInfoPricerModels(){
		return legDataInfoPricerModels;
	}
}
