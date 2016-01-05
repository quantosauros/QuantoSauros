package com.quantosauros.manager.model.pricer;

public class LegPricerModelForm {

	private LegInfoPricerModel[] legInfoPricerModels;
	private LegPeriodPricerModel[] legPeriodPricerModels;
	private MarketInfoPricerModel[] marketInfoPricerModels;
	
	public void setLegInfoPricerModels(LegInfoPricerModel[] legInfoPricerModels){
		this.legInfoPricerModels = legInfoPricerModels;
	}
	public LegInfoPricerModel[] getLegInfoPricerModels(){
		return legInfoPricerModels;
	}
	public void setLegPeriodPricerModels(LegPeriodPricerModel[] legPeriodPricerModels){
		this.legPeriodPricerModels = legPeriodPricerModels;
	}
	public LegPeriodPricerModel[] getLegPeriodPricerModels(){
		return legPeriodPricerModels;
	}
	public void setMarketInfoPricerModels(MarketInfoPricerModel[] marketInfoPricerModels){
		this.marketInfoPricerModels = marketInfoPricerModels;
	}
	public MarketInfoPricerModel[] getMarketInfoPricerModels(){
		return marketInfoPricerModels;
	}
}
