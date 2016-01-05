package com.quantosauros.manager.model.pricer;

public class MarketInfoPricerModel {

	private double[] quantoCorrelation;
	private double[] quantoVolatility;

	private InterestRateCurveModel[] interestRateCurveModels;
	private double[] meanReversion1F;
	private double[] meanReversion2F1;
	private double[] meanReversion2F2;
	private double[] correlation;
	
	private int undNum;
	
	public void setQuantoCorrelation(double[] quantoCorrelation){
		this.quantoCorrelation = quantoCorrelation;
	}
	public double[] getQuantoCorrelation(){
		return quantoCorrelation;
	}
	public void setQuantoVolatility(double[] quantoVolatility){
		this.quantoVolatility = quantoVolatility;
	}
	public double[] getQuantoVolatility(){
		return quantoVolatility;
	}
	public void setInterestRateCurveModels(InterestRateCurveModel[] interestRateCurveModels){
		this.interestRateCurveModels = interestRateCurveModels;
	}
	public InterestRateCurveModel[] getInterestRateCurveModels(){
		return interestRateCurveModels;
	}
	public void setMeanReversion1F(double[] meanReversion1F){
		this.meanReversion1F = meanReversion1F;		
	}
	public double[] getMeanReversion1F(){
		return meanReversion1F;
	}
	public void setMeanReversion2F1(double[] meanReversion2F1){
		this.meanReversion2F1 = meanReversion2F1;		
	}
	public double[] getMeanReversion2F1(){
		return meanReversion2F1;
	}
	public void setMeanReversion2F2(double[] meanReversion2F2){
		this.meanReversion2F2 = meanReversion2F2;		
	}
	public double[] getMeanReversion2F2(){
		return meanReversion2F2;
	}
	public void setCorrelation(double[] correlation){
		this.correlation = correlation;		
	}
	public double[] getCorrelation(){
		return correlation;
	}
	public void setUndNum(){
		this.undNum = interestRateCurveModels.length;
	}
	public int getUndNum(){
		this.undNum = interestRateCurveModels.length;
		return undNum;
	}
}
