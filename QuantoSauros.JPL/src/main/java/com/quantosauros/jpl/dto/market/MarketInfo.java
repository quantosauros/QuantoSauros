package com.quantosauros.jpl.dto.market;

import com.quantosauros.jpl.dto.AbstractInfo;

public class MarketInfo extends AbstractInfo {
	
	protected double _quantoCorrelation;
	protected double _quantoVolatility;
	
	public double getQuantoCorrelation(){
		return _quantoCorrelation;
	}
	public double getQuantoVolatility(){
		return _quantoVolatility;
	}
	
}
