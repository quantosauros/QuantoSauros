package com.quantosauros.manager.model.pricer;

public class MarketInfoPricerModel {

	private double quantoCorrelation;
	private double quantoVolatility;

	public void setQuantoCorrelation(double quantoCorrelation){
		this.quantoCorrelation = quantoCorrelation;
	}
	public double getQuantoCorrelation(){
		return quantoCorrelation;
	}
	public void setQuantoVolatility(double quantoVolatility){
		this.quantoVolatility = quantoVolatility;
	}
	public double getQuantoVolatility(){
		return quantoVolatility;
	}
}
