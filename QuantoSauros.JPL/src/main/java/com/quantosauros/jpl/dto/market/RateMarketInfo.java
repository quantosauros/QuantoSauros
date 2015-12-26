package com.quantosauros.jpl.dto.market;

import com.quantosauros.common.hullwhite.HWVolatilitySurface;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.interestrate.InterestRateCurve;


public class RateMarketInfo extends MarketInfo {

	protected InterestRateCurve _irCurve;
	protected HullWhiteParameters _hwParams;
	protected HWVolatilitySurface _hwVolSurface;	
	protected HullWhiteVolatility[] _hwVolatilities;
	
	//true : surface, false : curve
	protected boolean _hwVolSurfaceOrCurve;
	
	public RateMarketInfo(InterestRateCurve irCurve,
			HullWhiteParameters hwParams, HWVolatilitySurface hwVolSurface,
			double quantoCorrelation, double quantoVolatility) {		
		this._irCurve = irCurve;
		this._hwParams = hwParams;
		this._hwVolSurface = hwVolSurface;
		this._quantoCorrelation = quantoCorrelation;
		this._quantoVolatility = quantoVolatility;
		this._hwVolSurfaceOrCurve = true;
	}
	
	public RateMarketInfo(InterestRateCurve irCurve,
			HullWhiteParameters hwParams, HullWhiteVolatility[] hwVolatilities,
			double quantoCorrelation, double quantoVolatility) {		
		this._irCurve = irCurve;
		this._hwParams = hwParams;
		this._hwVolatilities = hwVolatilities;
		this._quantoCorrelation = quantoCorrelation;
		this._quantoVolatility = quantoVolatility;
		this._hwVolSurfaceOrCurve = false; 
	}
	
	public RateMarketInfo(InterestRateCurve irCurve,
			HullWhiteParameters hwParams, HWVolatilitySurface hwVolSurface) {		
		this._irCurve = irCurve;
		this._hwParams = hwParams;
		this._hwVolSurface = hwVolSurface;
		this._quantoCorrelation = 0;
		this._quantoVolatility = 0;
		this._hwVolSurfaceOrCurve = true;
	}
	
	public RateMarketInfo(InterestRateCurve irCurve,
			HullWhiteParameters hwParams, HullWhiteVolatility[] hwVolatilities) {		
		this._irCurve = irCurve;
		this._hwParams = hwParams;
		this._hwVolatilities = hwVolatilities;
		this._quantoCorrelation = 0;
		this._quantoVolatility = 0;
		this._hwVolSurfaceOrCurve = false; 
	}
	
	public InterestRateCurve getInterestRateCurve(){
		return _irCurve;
	}
	public HullWhiteParameters getHullWhiteParameters(){
		return  _hwParams;
	}
	public HWVolatilitySurface getHWVolatilitySurface(){
		return _hwVolSurface;
	}	
	public HullWhiteVolatility[] getHWVolatilities(){
		return _hwVolatilities;
	}
	public boolean isHWVolSurface(){
		return _hwVolSurfaceOrCurve;
	}
	public void setHWVolatilities(HullWhiteVolatility[] hwVolatilities){
		this._hwVolatilities = hwVolatilities;
	}
}
