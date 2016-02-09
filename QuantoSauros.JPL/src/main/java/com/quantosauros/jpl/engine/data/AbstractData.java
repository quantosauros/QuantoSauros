package com.quantosauros.jpl.engine.data;

import java.util.ArrayList;
import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.TypeDef.OptionType;
import com.quantosauros.common.TypeDef.RateType;
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
import com.quantosauros.jpl.dto.underlying.RateUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;
import com.quantosauros.jpl.engine.method.model.hullwhite.AbstractHullWhite;
import com.quantosauros.jpl.engine.method.model.hullwhite.HullWhite1F;
import com.quantosauros.jpl.engine.method.model.hullwhite.HullWhite2F;

public abstract class AbstractData {

	protected Date _asOfDate;
	protected Date _issueDate;
	protected Date _maturityDate;
	protected DayCountFraction _dcf;
	protected PaymentPeriod[] _periods;
	
	protected double[][] _startTime;
	protected ArrayList<Double> _tenors;
	
	//Num
	protected int _simNum;
	protected int _periodNum;
	protected int _timeNum;
	protected int _legNum;
	protected int[] _undNum;
	
	protected int[] _couponIndex;
	
	//Path 정보
	protected double[][] _generatedPath;
	//[assetIndex][factorIndex][timeIndex]
	protected double[][][] _hwX;
	//discount
	//[simIndex][periodIndex]
	protected double[][] _discountFactor;
	//[legIndex][underlyingIndex][simIndex][periodIndex][timeIndex]
	protected double[][][][][] _referenceRates;
						
	protected ArrayList<MarketInfo> _legMarketInfos;
	protected ArrayList<UnderlyingInfo> _legUnderlyingInfos;
	
	protected RateMarketInfo _discMarketInfo;	
	protected ModelType _discModelType;
	
	protected LegCouponInfo[] _legCouponInfos;
	protected LegDataInfo[] _legDataInfos;
	
	protected double[][][] _couponRate;
	protected double[][][] _condiTenor;
	protected double[][] _switchValue;
	
	protected boolean[] _hasExercises;
	//[legIndex][simIndex][periodIndex][rfIndex]
	protected double[][][][] _LSMCData;
	protected boolean[][] _restricted;
		
	protected OptionType _optionType;
	protected double _switchCoupon;	
	
	public AbstractData(Date asOfDate, Date issueDate, Date maturityDate,
			PaymentPeriod[] periods, DayCountFraction dcf,
			int simNum, int periodNum, int legNum,
			LegCouponInfo[] legCouponInfos,
			LegDataInfo[] legDataInfos) {
		
		_asOfDate = asOfDate;
		_issueDate = issueDate;
		_maturityDate = maturityDate;
		_periods = periods;
		_dcf = dcf;
		_simNum = simNum;
		_periodNum = periodNum;
		_legNum = legNum;
		_couponIndex = new int[_periodNum];
		_startTime = new double[_simNum][_periodNum];
		_discountFactor = new double[_simNum][_periodNum];
		_hasExercises = new boolean[_periodNum];		
		_legCouponInfos = legCouponInfos;
		_legDataInfos = legDataInfos;		
		_couponRate = new double[_legNum][_simNum][_periodNum];
		_condiTenor = new double[_legNum][_simNum][_periodNum];
		_switchValue = new double[_simNum][_periodNum];
		_LSMCData = new double[_legNum][_simNum][_periodNum][];
		_referenceRates = new double[_legNum][][][][];
		_undNum = new int[_legNum];
		for (int legIndex = 0; legIndex < legNum; legIndex++){
			_undNum[legIndex] = _legCouponInfos[legIndex].getUnderlyingNumber();
			_referenceRates[legIndex] = new double[_undNum[legIndex]][_simNum][_periodNum][];
		}		
	}
	
	protected void setTenor(int simIndex, int periodIndex, ArrayList<Double> tenors){
		_tenors = tenors;
		_timeNum = _tenors.size();
	}
	
	protected void setStartTime(int simIndex, int periodIndex, double startTime){
		_startTime[simIndex][periodIndex] = startTime;
	};
	
	protected void setGeneratedPath(int simIndex, int periodIndex, 
			double[][] path){
		_generatedPath = new double[path.length][];
		for (int i = 0; i < path.length; i++) {
			_generatedPath[i] = path[i];
		}
	};
	
	protected void setHullWhiteX(int simIndex, int periodIndex, 
			double[][][] path){
		_hwX = new double[path.length][][];
		for (int i = 0; i < path.length; i++) {
			_hwX[i] = path[i];
		}
	}
		
	protected void setCouponIndex(int periodIndex, int couponIndex){
		_couponIndex[periodIndex] = couponIndex;
	}
	
	protected void setHasExercise(int periodIndex, boolean hasExercise){
		_hasExercises[periodIndex] = hasExercise;
	}
	
	public double getDiscountFactor(int simIndex, int periodIndex){
		return _discountFactor[simIndex][periodIndex];
	}	
		
	public double getPayoff(int legIndex, int simIndex, int periodIndex){
		
		double condiTenor = _condiTenor[legIndex][simIndex][periodIndex];
		double couponRate = _couponRate[legIndex][simIndex][periodIndex];
		
		return condiTenor * couponRate;
	}
	
	public boolean getHasExercise(int periodIndex){
		return _hasExercises[periodIndex];
	}
	
	public double[] getLSMC(int legIndex, int simIndex, int periodIndex){
		return _LSMCData[legIndex][simIndex][periodIndex];
	}
	
	public double getSwitchValue(int simIndex, int periodIndex){
		if (_optionType.equals(OptionType.SWITCH)){
			return _switchValue[simIndex][periodIndex];
		} else {
			return 1.0;
		}
		
	}
	
	protected void calcDiscountFactor(int simIndex, int periodIndex){
		_discountFactor[simIndex][periodIndex] = 1;
		int discIndex = _generatedPath.length - 1;
		for (int timeIndex = 0; timeIndex < _timeNum; timeIndex++){
			//TODO
			double discountRate = _generatedPath[discIndex][timeIndex];
			_discountFactor[simIndex][periodIndex] *= 
					Math.exp(-discountRate * _tenors.get(timeIndex));				
		}
	}
	
	protected void calcReferenceRate(int simIndex, int periodIndex){
		int assetIndex = 0;
		for (int legIndex = 0; legIndex < _legNum; legIndex++){			
			for (int undIndex = 0; undIndex < _undNum[legIndex]; undIndex++){
				_referenceRates[legIndex][undIndex][simIndex][periodIndex] =
						new double[_timeNum];
								
				for (int timeIndex = 0; timeIndex < _timeNum; timeIndex++){
					_referenceRates[legIndex][undIndex][simIndex][periodIndex][timeIndex] =
							deriveReferenceRate(simIndex, periodIndex, assetIndex, timeIndex);
				}
				assetIndex++;
			}			
		}
	}
	
	protected double deriveReferenceRate(int simIndex, int periodIndex,
			int assetIndex, int timeIndex) {
		
		//startDt가 index에 따라 맞게 증가하도록 수정(2015.01.02)
		double tenorSum = 0;
		for(int i = 0; i < timeIndex; i++){
			tenorSum = tenorSum + _tenors.get(i);
		}
		double startDt = _startTime[simIndex][periodIndex] + tenorSum;
		double endDt = 0;
		double rate = 0;
		double zeroBondSum = 0;
		double zeroBond = 0;		
		double vol = 0;
		double vol2 = 0;
		AbstractHullWhite hullWhite = null;
		ModelType modelType = _legUnderlyingInfos.get(assetIndex).getModelType();
		
		if (modelType.equals(ModelType.HW1F)){
			//Hull-White 1F
			RateMarketInfo marketInfo = (RateMarketInfo)_legMarketInfos.get(assetIndex);
			RateUnderlyingInfo underlyingInfo = (RateUnderlyingInfo) _legUnderlyingInfos.get(assetIndex);
			
			double tenor = underlyingInfo.getTenor();
			Frequency swapCouponFrequency = underlyingInfo.getSwapCouponFrequency();
			
			HullWhiteVolatility[] hwVols = marketInfo.getHWVolatilities();			
			HullWhiteParameters hwParams = marketInfo.getHullWhiteParameters();
			InterestRateCurve irCurve = marketInfo.getInterestRateCurve();

			endDt = startDt + tenor;
			
			if (underlyingInfo.getRateType().equals(RateType.SPOT)){
				//FORWARD RATE
				vol = hwVols[0].getVolatility(startDt, tenor);
				
				hullWhite = new HullWhite1F(hwParams.getMeanReversion1F(), 
						vol, irCurve);
				zeroBond = ((HullWhite1F)hullWhite).discountBond(startDt, endDt, 
						_generatedPath[assetIndex][timeIndex]);
				
				//Quanto Adjustment
				double a = hwParams.getMeanReversion1F();
				double tempIntegral = tenor +
						( Math.exp(-a * endDt) - Math.exp(-a * startDt)) / a ; 
				zeroBond *= Math.exp(- marketInfo.getQuantoCorrelation() * vol *
						marketInfo.getQuantoVolatility() * tempIntegral / a);
				
				rate = - Math.log(zeroBond) / tenor;
				
			} else if (underlyingInfo.getRateType().equals(RateType.SWAP)){
				//SWAP RATE
				double swapTenor = swapCouponFrequency.getInterval();
				int swapRateNum = (int)(tenor / swapTenor);
							
				for (int i = 0; i < swapRateNum; i++){
					double tmpTenor = swapTenor * (i + 1);				
					endDt = startDt + tmpTenor;
					
					//vol
					vol = hwVols[0].getVolatility(startDt, tmpTenor);
										
					hullWhite = new HullWhite1F(hwParams.getMeanReversion1F(),
							vol, irCurve);
					zeroBond = ((HullWhite1F)hullWhite).discountBond(startDt, endDt, 
							_generatedPath[assetIndex][timeIndex]);
					
					zeroBondSum += zeroBond  * swapTenor;				
				}
				rate = (1 - zeroBond) / zeroBondSum;	
				
			} else if (underlyingInfo.getRateType().equals(RateType.RMS)){
				//TODO RMS Hull-White 1F
//				double tempCurveTenor = _irTenors.get(assetIndex);
//				double swapTenor = 1;
//				double diffDt = _dcf.getYearFraction(_issueDate, _periods[periodIndex].getStartDate());
//				int criteraOfRMS = (int)Math.round((diffDt / _RMSdecreasingStartDt[assetIndex]) * 50) / 50;				
//				tempCurveTenor =  _irTenors.get(assetIndex) - 
//						criteraOfRMS * _RMSdecreasingTenor[assetIndex];
//				int swapRateNum = (int)(tempCurveTenor / swapTenor);
//				
//				for (int i = 0; i < swapRateNum; i++){
//					double tenor = swapTenor * (i + 1);				
//					endDt = startDt + tenor;
//					
//					//vol					
//					vol = _irHWVols.get(assetIndex).getVolatility(startDt, tenor);
//										
//					hullWhite = new HullWhite1F(
//							_irHWParams.get(assetIndex).getMeanReversion1F(),
//							vol, _irCurves.get(assetIndex));
//					zeroBond = ((HullWhite1F)hullWhite).discountBond(startDt, endDt, 
//							_hwPath[assetIndex][timeIndex]);
//					
//					zeroBondSum += zeroBond  * swapTenor;				
//				}
//				rate = (1 - zeroBond) / zeroBondSum;
			}			
			
		} else if (modelType.equals(ModelType.HW2F)){
			//Hull-White 2F
			RateMarketInfo marketInfo = (RateMarketInfo)_legMarketInfos.get(assetIndex);
			RateUnderlyingInfo underlyingInfo = (RateUnderlyingInfo) _legUnderlyingInfos.get(assetIndex);
			
			double tenor = underlyingInfo.getTenor();
			Frequency swapCouponFrequency = underlyingInfo.getSwapCouponFrequency();
			
			HullWhiteVolatility[] hwVols = marketInfo.getHWVolatilities();			
			HullWhiteParameters hwParams = marketInfo.getHullWhiteParameters();
			InterestRateCurve irCurve = marketInfo.getInterestRateCurve();
			//2F			
			endDt = startDt + tenor;
				
			if (underlyingInfo.getRateType().equals(RateType.SPOT)){
				//FORWARD RATE
				//vol1				
				vol = hwVols[0].getVolatility(startDt, tenor);					
				
				//vol2
				vol2 = hwVols[1].getVolatility(startDt, tenor);					
								
				hullWhite = new HullWhite2F(
						hwParams.getMeanReversion1_2F(),
						hwParams.getMeanReversion2_2F(),
						vol, vol2, hwParams.getCorrelation(), 
						irCurve);
				
				zeroBond = ((HullWhite2F)hullWhite).discountBond(startDt, endDt, 
						_hwX[assetIndex][0][timeIndex], _hwX[assetIndex][1][timeIndex]);
				
				rate = - Math.log(zeroBond) / tenor;
				
			} else if (underlyingInfo.getRateType().equals(RateType.SWAP)){
				//SWAP RATE
//						double swapTenor = _swapCouponFrequency.getInterval();
				double swapTenor = 1;				
				int swapRateNum = (int)(tenor / swapTenor);
							
				for (int i = 0; i < swapRateNum; i++){
					double tmpTenor = swapTenor * (i + 1);				
					endDt = startDt + tenor;
					
					//vol1
					vol = hwVols[0].getVolatility(startDt, tmpTenor);					
					
					//vol2
					vol2 = hwVols[1].getVolatility(startDt, tmpTenor);					
					
					hullWhite = new HullWhite2F(
							hwParams.getMeanReversion1_2F(),
							hwParams.getMeanReversion2_2F(),
							vol, vol2, hwParams.getCorrelation(), 
							irCurve);
					
					zeroBond = ((HullWhite2F)hullWhite).discountBond(startDt, endDt, 
							_hwX[assetIndex][0][timeIndex], _hwX[assetIndex][1][timeIndex]);
					
					zeroBondSum += zeroBond  * swapTenor;	
				}
				rate = (1 - zeroBond) / zeroBondSum;
			} else if (underlyingInfo.getRateType().equals(RateType.RMS)){
				//TODO RMS
				
			}
		} else if (modelType.equals(ModelType.BS)){
			//TODO Black Scholes Case
			rate = _generatedPath[assetIndex][timeIndex];
		}
		
		return rate;
	}
	
	public double[][] getDiscountFactors(){
		return _discountFactor;
	}
	
	public double[][][] getRefRates(
			int legIndex, int undIndex){		
		//[simIndex][periodIndex][timeIndex]
		double[][][] results = new double[_simNum][_periodNum][];
		
		for (int simIndex = 0; simIndex < _simNum; simIndex++){
			double[][] tmp1 = _referenceRates[legIndex][undIndex][simIndex];			
			for (int periodIndex = 0; periodIndex < _periodNum; periodIndex++){
				double[] tmp2 = tmp1[periodIndex];
				results[simIndex][periodIndex] = new double[tmp2.length];
				
				for (int timeIndex = 0; timeIndex < tmp2.length; timeIndex++){
					results[simIndex][periodIndex][timeIndex] = tmp2[timeIndex];					
				}				
			}			
		}
		return results;		
	}
	
	public boolean[][] getRestrictionInfo(){
		return _restricted;
	}

	public double[][] extractPayoffArray(int legIndex){
		double[][] payoffArray = new double[_simNum][_periodNum];
		
		for (int simIndex = 0; simIndex < _simNum; simIndex++){
			for (int periodIndex = 0; periodIndex < _periodNum; periodIndex++){		
				double condiTenor = _condiTenor[legIndex][simIndex][periodIndex];
				double couponRate = _couponRate[legIndex][simIndex][periodIndex];
				payoffArray[simIndex][periodIndex] = condiTenor * couponRate;
			}
		}
		return payoffArray;	
	}
	
	public double[][] getCoupon(int legIndex){
		double[][] couponArray = new double[_simNum][_periodNum];
		
		for (int simIndex = 0; simIndex < _simNum; simIndex++){
			for (int periodIndex = 0; periodIndex < _periodNum; periodIndex++){
				double couponRate = _couponRate[legIndex][simIndex][periodIndex];
				couponArray[simIndex][periodIndex] = couponRate;
			}
		}
		return couponArray;	
	}
	
	public double[][] getTenor(int legIndex){
		double[][] tenorArray = new double[_simNum][_periodNum];
	
		for (int simIndex = 0; simIndex < _simNum; simIndex++){
			for (int periodIndex = 0; periodIndex < _periodNum; periodIndex++){
				double couponRate = _condiTenor[legIndex][simIndex][periodIndex];
				tenorArray[simIndex][periodIndex] = couponRate;
			}
		}
		return tenorArray;	
	
	}	
	
	public int[] getUnderlyingNum(){
		return _undNum;
	}

}

