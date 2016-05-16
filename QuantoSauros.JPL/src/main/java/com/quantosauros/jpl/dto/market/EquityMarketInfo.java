package com.quantosauros.jpl.dto.market;

import com.quantosauros.common.interestrate.ZeroRateCurve;

public class EquityMarketInfo extends MarketInfo {

	protected double _currEquityPrice;
	protected double _equityVolatility;
	protected double _equityDividend;
	protected ZeroRateCurve _riskFreeCurve;
	
	public EquityMarketInfo(double currEquityPrice,
			double equityVolatility, double equityDividend,
			ZeroRateCurve riskFreeCurve) {
		this._currEquityPrice = currEquityPrice;
		this._equityDividend = equityDividend;
		this._equityVolatility = equityVolatility;
		this._riskFreeCurve = riskFreeCurve;
	}
	
	public double getCurrEquityPrice(){
		return _currEquityPrice;
	}
	public double getEquityVolatility(){
		return _equityVolatility;
	}
	public double getEquityDividend(){
		return _equityDividend;
	}
	public ZeroRateCurve getRiskFreeCurve(){
		return _riskFreeCurve;
	}
	
}
