package com.quantosauros.jpl.dto;

import com.quantosauros.common.TypeDef;
import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.TypeDef.UnderlyingType;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;

public class LegCouponInfo extends AbstractInfo {

//	protected double[] _tenors;
//	protected Frequency[] _swapCouponFrequencies;	
//	protected RateType[] _rateTypes;
	
	//[underlying]
	protected UnderlyingInfo[] _underlyingInfos; 
	
	//[condition][period]
	protected double[][] _upperLimits;
	protected double[][] _lowerLimits;
	
	//[underlying][period]
	protected double[][] _leverages;
	protected boolean _hasLeverage;
	//[period]
	protected double[] _cap;
	protected double[] _floor;
	protected double[] _spreads;
	protected boolean _hasCap;
	protected boolean _hasFloor;
	
	protected CouponType[] _couponType;
	protected UnderlyingType _underlyingType;
	protected ConditionType _conditionType;
	protected PayRcv _payRcv;
	//[underlyingIndex]
//	protected ModelType[] _modelTypes;
	
	//[period]
	protected double[] _inCouponRates;
	protected double[] _outCouponRates;
	protected boolean _hasInOutCouponRates;
	
	protected int _underlyingNumber;	
	protected int _conditionNumber;	
	
	public LegCouponInfo(PayRcv payRcv, CouponType[] couponType, 
			UnderlyingType underlyingType, ConditionType conditionType) {
		
		_payRcv = payRcv;
		_couponType = couponType;
		_underlyingType = underlyingType;
		_conditionType = conditionType;	
		
		_underlyingNumber = TypeDef.getNumOfUnderNum(underlyingType);		
		_conditionNumber = TypeDef.getNumOfCondition(conditionType);		
	}
	
	public PayRcv getPayRcv(){
		return _payRcv;
	}
	
	public int getUnderlyingNumber(){
		return _underlyingNumber;
	}
	public int getConditionNumber(){
		return _conditionNumber;
	}
		
	public CouponType[] getCouponType(){
		return _couponType;
	}
	public void setCouponType(CouponType[] couponType){
		this._couponType = couponType;
	}
	public UnderlyingType getUnderlyingType(){
		return _underlyingType;
	}
	public void setUnderlyingType(UnderlyingType underlyingType){
		this._underlyingType = underlyingType;
	}
	public ConditionType getConditionType(){
		return _conditionType;
	}
	public void setConditionType(ConditionType conditionType){
		this._conditionType = conditionType;
	}
	public double[] getSpreads(){
		return _spreads;
	}
	public void setSpreads(double[] spreads){
		_spreads = spreads;
	}
	public double[] getLeverages(int underlyingIndex){
		if (_leverages != null){
			return _leverages[underlyingIndex];
		} else {
			return null;
		}		
	}
	public void setLeverage(int underlyingIndex, double[] leverages){
		_leverages[underlyingIndex] = leverages;
		_hasLeverage = true;
	}
	public boolean hasLeverage(){		
		return _hasLeverage;
	}
	public double[] getCap(){
		return _cap;
	}
	public void setCap(double[] cap){
		_cap = cap;
	}
	public double[] getFloor(){
		return _floor;
	}
	public void setFloor(double[] floor){
		_floor = floor;
	}
	public boolean hasCap(){
		return _hasCap;
	}
	public boolean hasFloor(){
		return _hasFloor;
	}
	public double[][] getUpperLimits(){
		if (_upperLimits != null){
			return _upperLimits;
		} else {
			return null;
		}		
	}
	public void setUpperLimits(int conditionIndex, double[] upperLimits){
		_upperLimits[conditionIndex] = upperLimits;
	}	
	public double[][] getLowerLimits(){
		if (_lowerLimits != null){
			return _lowerLimits;			
		} else {
			return null;
		}
	}
	public void setLowerLimits(int conditionIndex, double[] lowerLimits){
		_lowerLimits[conditionIndex] = lowerLimits;
	}
	
//	public ModelType getModelType(int underlyingIndex){
//		return _modelTypes[underlyingIndex];
//	}
		
	public double[] getInCouponRates(){
		if (_inCouponRates != null){
			return _inCouponRates;
		} else {
			return null;
		}
	}
	public void setInCouponRates(double[] inCouponRates){
		this._inCouponRates = inCouponRates;
	}
	public double[] getOutCouponRates(){
		if (_outCouponRates != null){
			return _outCouponRates;
		} else {
			return null;
		}
	}
	public void setOutCouponRates(double[] outCouponRates){
		this._outCouponRates = outCouponRates;
	}
	public boolean hasInOutCouponRates(){
		return _hasInOutCouponRates;
	}
	public UnderlyingInfo getUnderlyingInfo(int undIndex){
		return _underlyingInfos[undIndex];
	}
	public double[][] getLeverageAll(){
		return _leverages;
	}
	public String toString(){
		StringBuffer buf = new StringBuffer();
		buf.append("\r\n");
		buf.append("==" + getClass().getName() + "==");
		
		buf.append("\r\n");
		buf.append("CouponType=" + _couponType);
		buf.append("\r\n");
		buf.append("ReferenceAssetType=" + _underlyingType);
		buf.append("\r\n");
		buf.append("ConditionType=" + _conditionType);
		
		if (_spreads != null){
			for (int i = 0; i < _spreads.length; i++){				
				buf.append("\r\n");
				buf.append("spreads[" + i + "]=" + _spreads[i] + "; ");
			}
		}
		if (_leverages != null){
			for (int i = 0; i < _leverages.length; i++){
				for (int j = 0; j < _leverages[i].length; j++){
					buf.append("\r\n");
					buf.append("leverage[" + i + "][" + j + "]=" + _leverages[i][j] + "; ");
				}				
			}
		}
		if (_cap != null){
			buf.append("hasCap=" + _hasCap);
			for (int i = 0; i < _cap.length; i++){
				buf.append("cap[" + i + "]=" + _cap[i] + "; ");				
			}
		}
		if (_floor != null){
			buf.append("hasFloor=" + _hasFloor);
			for (int i = 0; i < _floor.length; i++){				
				buf.append("\r\n");
				buf.append("floor[" + i + "]=" + _floor[i] + "; ");
								
			}
		}
//		if (_tenors != null){
//			for (int i = 0; i < _tenors.length; i++){				
//				buf.append("\r\n");
//				buf.append("tenors[" + i + "]=" + _tenors[i] + "; ");
//			}
//		}
//		
//		if (_swapCouponFrequencies != null){
//			for (int i = 0; i < _swapCouponFrequencies.length; i++){				
//				buf.append("\r\n");
//				buf.append("swapCouponFrequencies[" + i + "]=" + _swapCouponFrequencies[i] + "; ");
//			}
//		}
//		
//		if (_rateTypes != null){
//			for (int i = 0; i < _rateTypes.length; i++){				
//				buf.append("\r\n");
//				buf.append("rateTypes[" + i + "]=" + _rateTypes[i] + "; ");
//			}
//		}
		
		return buf.toString();
	}
}


