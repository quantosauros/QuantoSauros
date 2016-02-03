package com.quantosauros.jpl.engine.data;

import java.util.ArrayList;

import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.TypeDef.OptionType;
import com.quantosauros.common.TypeDef.UnderlyingType;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.PaymentPeriod;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.market.MarketInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;
import com.quantosauros.jpl.engine.method.model.hullwhite.AbstractHullWhite;
import com.quantosauros.jpl.engine.method.model.hullwhite.HullWhite1F;
import com.quantosauros.jpl.engine.method.model.hullwhite.HullWhite2F;

public class StructuredData extends AbstractData {
	
	public StructuredData(Date asOfDate, Date issueDate, Date maturityDate,
			PaymentPeriod[] periods, DayCountFraction dcf,			
			int simNum, int periodNum,
			LegCouponInfo[] legCouponInfos,
			LegDataInfo[] legDataInfos,
			//IR Information
			ArrayList<MarketInfo> legMarketInfoArray,
			ArrayList<UnderlyingInfo> legUnderlyingInfoArray,			
			//Discount Parameters
			RateMarketInfo discMarketInfo,
			ModelType discModelType,
			OptionType optionType,
			double switchCoupon) {
		
		super(asOfDate, issueDate, maturityDate, periods, dcf, 
				simNum, periodNum, legCouponInfos.length, 
				legCouponInfos, legDataInfos);
				 
		_restricted = new boolean[_simNum][_periodNum];
		_legMarketInfos = legMarketInfoArray;
		_legUnderlyingInfos = legUnderlyingInfoArray;
		_discMarketInfo = discMarketInfo;
		_discModelType = discModelType;
		_optionType = optionType;
		_switchCoupon = switchCoupon;
	}

	public void setData(int simIndex, int periodIndex, int couponIndex,
			ArrayList<Double> tenors,
			double startTime, double dt,
			boolean hasExercise,
			double[][] generatedPath, double[][][] hwX){
		
		setTenor(simIndex, periodIndex, tenors);
		setStartTime(simIndex, periodIndex, startTime);
		setGeneratedPath(simIndex, periodIndex, generatedPath);
		setHullWhiteX(simIndex, periodIndex, hwX);
		setCouponIndex(periodIndex, couponIndex);
		setHasExercise(periodIndex, hasExercise);
		
		init(simIndex, periodIndex);
	}
	
	protected void init(int simIndex, int periodIndex){
		calcReferenceRate(simIndex, periodIndex);
		calcDiscountFactor(simIndex, periodIndex);
		calcCoupon(simIndex, periodIndex);
		calcTenor(simIndex, periodIndex);
		calcLSMCData(simIndex, periodIndex);
		
		if (_optionType.equals(OptionType.SWITCH)){
			calcSwitchValue(simIndex, periodIndex);
		}		
	}
	
	protected void calcTenor(int simIndex, int periodIndex){		
		for (int legIndex = 0; legIndex < _legNum; legIndex++){
			double accrualTenor = 0;		
			if (_asOfDate.diff(_periods[periodIndex].getStartDate()) *
					_asOfDate.diff(_periods[periodIndex].getEndDate()) <= 0){
				accrualTenor += _legDataInfos[legIndex].getCumulatedAccrualDays() /
						_dcf.getDaysOfYear();				
			}			
			
			ConditionType condiType = _legCouponInfos[legIndex].getConditionType();			

			double[][] upperLimit = _legCouponInfos[legIndex].getUpperLimits();
			double[][] lowerLimit = _legCouponInfos[legIndex].getLowerLimits();

			for (int timeIndex = 0; timeIndex < _tenors.size(); timeIndex++){
				double refRate1 = 0;
				double refRate2 = 0;
				switch(condiType){
					case NONE:		
						accrualTenor += _tenors.get(timeIndex);
						break;
					case R1:
						refRate1 = _referenceRates[legIndex][0][simIndex][periodIndex][timeIndex];
						if (refRate1 <= upperLimit[0][periodIndex] && 
								refRate1 >= lowerLimit[0][periodIndex]){
							accrualTenor += _tenors.get(timeIndex);
						}
						break;
					case R2 :
						refRate1 = _referenceRates[legIndex][1][simIndex][periodIndex][timeIndex];
						if (refRate1 <= upperLimit[0][periodIndex] && 
								refRate1 >= lowerLimit[0][periodIndex]){
							accrualTenor += _tenors.get(timeIndex);
						}
						break;
					case R3:
						refRate1 = _referenceRates[legIndex][2][simIndex][periodIndex][timeIndex];
						if (refRate1 <= upperLimit[0][periodIndex] && 
								refRate1 >= lowerLimit[0][periodIndex]){
							accrualTenor += _tenors.get(timeIndex);
						}
						break;
					case R1mR2:
						refRate1 = _referenceRates[legIndex][0][simIndex][periodIndex][timeIndex] 
								- _referenceRates[legIndex][1][simIndex][periodIndex][timeIndex];
						if (refRate1 <= upperLimit[0][periodIndex] && 
								refRate1 >= lowerLimit[0][periodIndex]){
							accrualTenor += _tenors.get(timeIndex);
						}
						break;
					case R2mR3 :
						refRate1 = _referenceRates[legIndex][1][simIndex][periodIndex][timeIndex] 
								- _referenceRates[legIndex][2][simIndex][periodIndex][timeIndex];
						if (refRate1 <= upperLimit[0][periodIndex] && 
								refRate1 >= lowerLimit[0][periodIndex]){
							accrualTenor += _tenors.get(timeIndex);
						}
						break;
					case R1nR2 :						
						refRate1 = _referenceRates[legIndex][0][simIndex][periodIndex][timeIndex];
						refRate2 = _referenceRates[legIndex][1][simIndex][periodIndex][timeIndex];
						if (refRate1 <= upperLimit[0][periodIndex] && 
								refRate1 >= lowerLimit[0][periodIndex]){
							if (refRate2 <= upperLimit[1][periodIndex] && 
									refRate2 >= lowerLimit[1][periodIndex]){
								accrualTenor += _tenors.get(timeIndex);
							}							
						}
						break;
					case R1nR3 :
						refRate1 = _referenceRates[legIndex][0][simIndex][periodIndex][timeIndex];
						refRate2 = _referenceRates[legIndex][2][simIndex][periodIndex][timeIndex];
						if (refRate1 <= upperLimit[0][periodIndex] && 
								refRate1 >= lowerLimit[0][periodIndex]){
							if (refRate2 <= upperLimit[1][periodIndex] && 
									refRate2 >= lowerLimit[1][periodIndex]){
								accrualTenor += _tenors.get(timeIndex);
							}							
						}
						break;
					case R2nR3 :
						refRate1 = _referenceRates[legIndex][1][simIndex][periodIndex][timeIndex];
						refRate2 = _referenceRates[legIndex][2][simIndex][periodIndex][timeIndex];
						if (refRate1 <= upperLimit[0][periodIndex] && 
								refRate1 >= lowerLimit[0][periodIndex]){
							if (refRate2 <= upperLimit[1][periodIndex] && 
									refRate2 >= lowerLimit[1][periodIndex]){
								accrualTenor += _tenors.get(timeIndex);
							}							
						}
						break;
					case R1nR2mR3 :
						refRate1 = _referenceRates[legIndex][0][simIndex][periodIndex][timeIndex];
						refRate2 = _referenceRates[legIndex][1][simIndex][periodIndex][timeIndex] - 
								_referenceRates[legIndex][2][simIndex][periodIndex][timeIndex];
						if (refRate1 <= upperLimit[0][periodIndex] && 
								refRate1 >= lowerLimit[0][periodIndex]){
							if (refRate2 <= upperLimit[1][periodIndex] && 
									refRate2 >= lowerLimit[1][periodIndex]){
								accrualTenor += _tenors.get(timeIndex);
							}							
						}
						break;					
				}
			}
			
			_condiTenor[legIndex][simIndex][periodIndex] = accrualTenor;
					
		}		
	}
	
	protected void calcCoupon(int simIndex, int periodIndex){		
		for (int legIndex = 0; legIndex < _legNum; legIndex++){
			CouponType cpnType = _legCouponInfos[legIndex].getCouponType()[periodIndex];			
			double rate = 0;
			UnderlyingType underlyingType = _legCouponInfos[legIndex].getUnderlyingType();
			
			switch (cpnType) {
				case ACCRUAL :					
					double[] inCouponRate = _legCouponInfos[legIndex].getInCouponRates();
					double[] outCouponRate = _legCouponInfos[legIndex].getOutCouponRates();
					//TODO
					_couponRate[legIndex][simIndex][periodIndex] = inCouponRate[periodIndex];
					
					break;
					
				case AVERAGE :					
					double accruedPayoff = 0;
					double tenor = 0;
					//Accumulated Average Coupon
					if (_asOfDate.diff(_periods[periodIndex].getStartDate()) *
							_asOfDate.diff(_periods[periodIndex].getEndDate()) <= 0){
						tenor = Math.max(_dcf.getYearFraction(
								_periods[periodIndex].getStartDate(), _asOfDate), 0);
						
						accruedPayoff = (_legDataInfos[legIndex].getCumulatedAvgCoupon()) * tenor;
					}
					
					double sum = accruedPayoff;		
					double denominator = 0;
					for (int timeIndex = _couponIndex[periodIndex]; 
								timeIndex < _tenors.size(); timeIndex++){
						double partialDeno = _tenors.get(timeIndex);
						denominator += partialDeno;
						if (underlyingType.equals(UnderlyingType.R1mR2)){
							double[] leverages1 = _legCouponInfos[legIndex].getLeverages(0);
							double[] leverages2 = _legCouponInfos[legIndex].getLeverages(1);
							double[] spreads = _legCouponInfos[legIndex].getSpreads();
							
							sum += (leverages1[periodIndex] * 
									_referenceRates[legIndex][0][simIndex][periodIndex][timeIndex] -
									leverages2[periodIndex] * 
									_referenceRates[legIndex][1][simIndex][periodIndex][timeIndex] +
									spreads[periodIndex]) * partialDeno;					
						} else if (underlyingType.equals(UnderlyingType.R1)){
							double[] leverages1 = _legCouponInfos[legIndex].getLeverages(0);							
							double[] spreads = _legCouponInfos[legIndex].getSpreads();
							
							sum += (leverages1[periodIndex] * 
									_referenceRates[legIndex][0][simIndex][periodIndex][timeIndex] + 
									spreads[periodIndex]) * partialDeno;
						} else {
							
						}
					}
					
					double tmpCoupon = sum / (denominator + tenor);
					
					boolean hasCap = _legCouponInfos[legIndex].hasCap();
					boolean hasFloor = _legCouponInfos[legIndex].hasFloor();
					double[] cap = _legCouponInfos[legIndex].getCap();
					double[] floor = _legCouponInfos[legIndex].getFloor();
					
					if (hasFloor && hasCap){
						tmpCoupon = Math.min(Math.max(tmpCoupon, floor[periodIndex]), cap[periodIndex]);
						if (tmpCoupon != cap[periodIndex] && tmpCoupon != floor[periodIndex]){
							_restricted[simIndex][periodIndex] = false;
						} else {
							_restricted[simIndex][periodIndex] = true;
						}
					} else if (!hasFloor && hasCap){
						tmpCoupon = Math.min(tmpCoupon, cap[periodIndex]);
						if (tmpCoupon != cap[periodIndex]){
							_restricted[simIndex][periodIndex] = false;
						} else {
							_restricted[simIndex][periodIndex] = true;
						}
					} else if (hasFloor && !hasCap){
						tmpCoupon = Math.max(tmpCoupon, floor[periodIndex]);
						if (tmpCoupon != floor[periodIndex]){
							_restricted[simIndex][periodIndex] = false;
						} else {
							_restricted[simIndex][periodIndex] = true;
						}
					} else {
						_restricted[simIndex][periodIndex] = false;
					}
					_couponRate[legIndex][simIndex][periodIndex] = tmpCoupon;
					break;
					
				case FIXED :
					rate = _legCouponInfos[legIndex].getSpreads()[periodIndex];
					_couponRate[legIndex][simIndex][periodIndex] = rate;
					break;
					
				case RESET:
					if (_asOfDate.diff(_periods[periodIndex].getStartDate()) * 
							_asOfDate.diff(_periods[periodIndex].getEndDate()) <= 0){
						//1. 차기지급이자 적용여부 체크 : startDate < asOfDate < endDate
						rate = _legDataInfos[legIndex].getNextCouponRate();
					} else {
						//2. Forward Rate 계산 : startDate에 tenor에 해당하는 HW 금리 계산 + spread
						if (underlyingType.equals(UnderlyingType.R1)){
							double[] leverages1 = _legCouponInfos[legIndex].getLeverages(0);							
							double[] spreads = _legCouponInfos[legIndex].getSpreads();

							rate = leverages1[periodIndex] * 
									_referenceRates[legIndex][0][simIndex][periodIndex][_couponIndex[periodIndex]] 
											+ spreads[periodIndex];
						} else if (underlyingType.equals(UnderlyingType.R1mR2)){
							double[] leverages1 = _legCouponInfos[legIndex].getLeverages(0);
							double[] leverages2 = _legCouponInfos[legIndex].getLeverages(1);
							double[] spreads = _legCouponInfos[legIndex].getSpreads();
							rate = leverages1[periodIndex] * 
									_referenceRates[legIndex][0][simIndex][periodIndex][_couponIndex[periodIndex]] - 
									leverages2[periodIndex] * 
									_referenceRates[legIndex][1][simIndex][periodIndex][_couponIndex[periodIndex]]
											+ spreads[periodIndex];							
						}						
					}					
					_couponRate[legIndex][simIndex][periodIndex] = rate;
					break;	
			}
		}		
	}
	
	protected void calcLSMCData(int simIndex, int periodIndex){
		int discIndex = _generatedPath.length - 1;
		for (int legIndex = 0; legIndex < _legNum; legIndex++){
			
			CouponType couponType = _legCouponInfos[legIndex].getCouponType()[periodIndex];
			_LSMCData[legIndex][simIndex][periodIndex] = new double[2];
			int index = 0;
			switch (couponType){
			case ACCRUAL :
				//TODO
				_LSMCData[legIndex][simIndex][periodIndex][0] = 
						_referenceRates[legIndex][0][simIndex][periodIndex][_tenors.size() - 1];
				index++;
				break;
			case AVERAGE :
				//TODO
				_LSMCData[legIndex][simIndex][periodIndex][0] = 
						_referenceRates[legIndex][0][simIndex][periodIndex][_tenors.size() - 1] -
						_referenceRates[legIndex][1][simIndex][periodIndex][_tenors.size() - 1];
				index++;
				break;
			case FIXED :
				
				break;
			case RESET :
				//TODO
				_LSMCData[legIndex][simIndex][periodIndex][0] = 
						_referenceRates[legIndex][0][simIndex][periodIndex][_tenors.size() - 1];
				index++;
				break;				
			}

			double startDt = _dcf.getYearFraction(_asOfDate, _periods[periodIndex].getEndDate());
			double endDt = _dcf.getYearFraction(_asOfDate, _maturityDate);
			double discountTenor = endDt - startDt;
			double zeroBond = 0;
			HullWhiteVolatility[] discHWVols = _discMarketInfo.getHWVolatilities();
			HullWhiteParameters discHWParams = _discMarketInfo.getHullWhiteParameters();
			
			if (_discModelType.equals(ModelType.HW1F)){			
				
				double vol = discHWVols[0].getVolatility(
						startDt, discountTenor);
				
				AbstractHullWhite hullWhite = new HullWhite1F(
						discHWParams.getMeanReversion1F(), vol, 
						_discMarketInfo.getInterestRateCurve());
				
				zeroBond = ((HullWhite1F)hullWhite).discountBond(startDt, endDt, 
						_generatedPath[discIndex][_generatedPath[discIndex].length - 1]);
			} else if (_discModelType.equals(ModelType.HW2F)){
				double vol1 = discHWVols[0].getVolatility(startDt, discountTenor);
				double vol2 = discHWVols[1].getVolatility(startDt, discountTenor);
				
				AbstractHullWhite hullWhite = new HullWhite2F(
						discHWParams.getMeanReversion1_2F(), 
						discHWParams.getMeanReversion2_2F(), vol1, vol2, 
						discHWParams.getCorrelation(), _discMarketInfo.getInterestRateCurve());
				
				zeroBond = ((HullWhite2F)hullWhite).discountBond(startDt, endDt, 
						_hwX[discIndex][0][_generatedPath[discIndex].length - 1], _hwX[discIndex][1][_generatedPath[discIndex].length - 1]);
				
//				(startDt, endDt, 
//						_hwX[assetIndex][0][timeIndex], _hwX[assetIndex][1][timeIndex]);
			} else {
				
			}
			
			
			_LSMCData[legIndex][simIndex][periodIndex][index] = -Math.log(zeroBond)/discountTenor;
		}		
	}			

	protected void calcSwitchValue(int simIndex, int periodIndex){
		if (_hasExercises[periodIndex]){			
			AbstractHullWhite hullWhite = null;
			double vol = 0;
			double vol2 = 0;
			double startDt = _startTime[simIndex][periodIndex];
			double endDt = 0;
			double zeroBondSum = 0;
			double zeroBond = 0;				
			double totalTenor = 0;
			int discIndex = _generatedPath.length - 1;
			
			HullWhiteVolatility[] hwVols = _discMarketInfo.getHWVolatilities();
			HullWhiteParameters hwParams = _discMarketInfo.getHullWhiteParameters();
			InterestRateCurve irCurve = _discMarketInfo.getInterestRateCurve();
			for (int index = periodIndex; index < _periodNum; index++){
				
				Date startDate = _periods[index].getStartDate();
				Date endDate = _periods[index].getEndDate();
				
				double tenor = index == periodIndex ? Math.min(
						_dcf.getYearFraction(startDate, endDate), 
						_dcf.getYearFraction(_asOfDate, endDate)) :
							_dcf.getYearFraction(startDate, endDate);
						
				totalTenor += tenor;
				
				endDt = startDt + totalTenor;
				if (_discModelType.equals(ModelType.HW1F)){
					vol = hwVols[0].getVolatility(startDt, totalTenor);
					hullWhite = new HullWhite1F(hwParams.getMeanReversion1F(),
							vol, irCurve);
					zeroBond = ((HullWhite1F)hullWhite).discountBond(startDt, endDt, 
							_generatedPath[discIndex][0]);
				} else if (_discModelType.equals(ModelType.HW2F)){
					//vol1
					vol = hwVols[0].getVolatility(startDt, totalTenor);					
					
					//vol2
					vol2 = hwVols[1].getVolatility(startDt, totalTenor);					
					
					hullWhite = new HullWhite2F(
							hwParams.getMeanReversion1_2F(),
							hwParams.getMeanReversion2_2F(),
							vol, vol2, hwParams.getCorrelation(), 
							irCurve);
					
					zeroBond = ((HullWhite2F)hullWhite).discountBond(startDt, endDt, 
							_hwX[discIndex][0][0], _hwX[discIndex][1][0]);
				} else {
					//NONE
				}
				
				zeroBondSum += zeroBond * _switchCoupon * tenor;
				if (index == _periodNum - 1){
					zeroBondSum += zeroBond;
				}
			} 
			
			_switchValue[simIndex][periodIndex] = zeroBondSum;
		} else {
			_switchValue[simIndex][periodIndex] = 1.0;
		}
	}
}
