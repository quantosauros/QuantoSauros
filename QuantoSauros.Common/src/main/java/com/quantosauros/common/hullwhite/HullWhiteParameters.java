package com.quantosauros.common.hullwhite;

import java.io.Serializable;

/*------------------------------------------------------------------------------
* Implementation Note
* - Creation: RiskCraft.Market R3, 2014
* - Creation Date : 2014-04-18
* - Modified Date : 2014-06-11 (hull-white 2-factor model)
* - Creator : Jihoon Lee
------------------------------------------------------------------------------*/
public class HullWhiteParameters implements Serializable {
	
	private double _meanReversion_1F;	
	private double _hullWhiteVolatility_1F;
	
	//added by Jihoon Lee, 20140611 for Hull-White 2-factor Model
	private double _meanReversion1_2F;
	private double _meanReversion2_2F;
	private double _hullWhiteVolatility1_2F;
	private double _hullWhiteVolatility2_2F;
	private double _correlation;

	/**
	 * Hull White 1-Factor Model의 파라미터(평균회귀속도, 변동성)를 저장하는 클래스
	 * 
	 * @param meanReversion				평균회귀속도
	 * @param hullWhiteVolatility		변동성
	 */
	public HullWhiteParameters(double meanReversion, 
			double hullWhiteVolatility) {
		this._meanReversion_1F = meanReversion;
		this._hullWhiteVolatility_1F = hullWhiteVolatility;
	}
	/**
	 * Hull White 2-Factor Model의 파라미터(평균회귀속도1, 평균회귀속도2, 변동성1, 변동성2, 상관계수)
	 * 를 저장하는 클래스
	 * 
	 * @param meanReversion1			평균회귀속도1
	 * @param meanReversion2			평균회귀속도2
	 * @param hullWhiteVolatility1		변동성1
	 * @param hullWhiteVolatility2		변동성2
	 * @param correlation				상관계수
	 */
	public HullWhiteParameters(
			double meanReversion1, double meanReversion2, 
			double hullWhiteVolatility1, double hullWhiteVolatility2,
			double correlation) {
		this._meanReversion1_2F = meanReversion1;
		this._hullWhiteVolatility1_2F = hullWhiteVolatility1;
		this._meanReversion2_2F = meanReversion2;
		this._hullWhiteVolatility2_2F = hullWhiteVolatility2;
		this._correlation = correlation;
	}
	/**
	 * Hull-White 모델의 파라미터를 저장하는 생성자
	 * 1-Factor의 파라미터(meanReversion1_1F, hullWhiteVolatility_1F)와
	 * 2-Factor의 파라미터(meanReversion1_2F, meanReversion2_2F, 
	 * hullWhiteVolatility1_2F, hullWhiteVolatility2_2F, correlation)를
	 * 동시에 저장한다.
	 * 
	 * @param meanReversion1_1F
	 * @param hullWhiteVolatility_1F
	 * @param meanReversion1_2F
	 * @param meanReversion2_2F
	 * @param hullWhiteVolatility1_2F
	 * @param hullWhiteVolatility2_2F
	 * @param correlation
	 */
	public HullWhiteParameters(
			double meanReversion1_1F, double hullWhiteVolatility_1F,
			double meanReversion1_2F, double meanReversion2_2F, 
			double hullWhiteVolatility1_2F, double hullWhiteVolatility2_2F,
			double correlation) {
		//1F Parameters
		this._meanReversion_1F = meanReversion1_1F;
		this._hullWhiteVolatility_1F = hullWhiteVolatility_1F;
		
		//2F Parameters
		this._meanReversion1_2F = meanReversion1_2F;
		this._meanReversion2_2F = meanReversion2_2F;
		this._hullWhiteVolatility1_2F = hullWhiteVolatility1_2F;
		this._hullWhiteVolatility2_2F = hullWhiteVolatility2_2F;
		this._correlation = correlation;
	}
	
	public double getMeanReversion1F(){
		return this._meanReversion_1F;
	}
	public double getHullWhiteVolatility1F(){
		return this._hullWhiteVolatility_1F;
	}
	public double getMeanReversion1_2F(){
		return this._meanReversion1_2F;		
	}	
	public double getMeanReversion2_2F(){
		return this._meanReversion2_2F;
	}
	public double getHullWhiteVolatility1_2F(){
		return this._hullWhiteVolatility1_2F;
	}
	public double getHullWhiteVolatility2_2F(){
		return this._hullWhiteVolatility2_2F;
	}	
	public double getCorrelation(){
		return this._correlation;
	}
	
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("Hull-White 1-Factor Parameters");
		buf.append("\r\n");
		buf.append("MeanReversion(1F) : " + _meanReversion_1F);
		buf.append("\r\n");
		buf.append("Volatility(1F) : " + _hullWhiteVolatility_1F);
		buf.append("\r\n");		
		buf.append("Hull-White 2-Factor Parameters");
		buf.append("\r\n");
		buf.append("MeanReversion1(2F) : " + _meanReversion1_2F);
		buf.append("\r\n");
		buf.append("MeanReversion2(2F) : " + _meanReversion2_2F);
		buf.append("\r\n");
		buf.append("Volatility1(2F) : " + _hullWhiteVolatility1_2F);
		buf.append("\r\n");
		buf.append("Volatility2(2F) : " + _hullWhiteVolatility2_2F);
		buf.append("\r\n");
		buf.append("Correlation : " + _correlation);
		buf.append("\r\n");
		
		return buf.toString();
	}
	
}
