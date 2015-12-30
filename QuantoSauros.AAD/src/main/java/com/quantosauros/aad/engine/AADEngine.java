package com.quantosauros.aad.engine;

import java.util.ArrayList;
import java.util.HashMap;

import com.quantosauros.aad.aad.AbstractAAD;
import com.quantosauros.aad.aad.VertexSensitivityInfoForAAD;
import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.interestrate.InterestRate;
import com.quantosauros.common.interestrate.InterestRateCurve;

public class AADEngine {

	protected boolean _hasPrincipalExchange;
	protected int _periodNum;	
	protected int _simNum;
	protected int _deferredCouponResetIndex;
	protected int[] _monitorFrequency;
//	AssetIndex : differs by its number of reference rates.
	protected InterestRateCurve[][] _legIrCurves;
	protected InterestRateCurve _discountCurve;
	protected DayCountFraction _dcf;
	protected Money _principal;
	
//	AssetIndex : differs by its number of reference rates.
	protected double[][] _legIrMeanReversions;
	protected double[][] _legIrTenors;
	protected double _discountMeanReversion;
	protected ModelType[][] _legModelTypes;
	protected ModelType _discModelType;	
	
	protected double[][][] _legPayoffs;
	protected double[][] _discountFactor;
	
	protected ArrayList<ArrayList<Double>> _tenors;
		
	//SwapLeg Information
	protected InterestRateCurve _swapLegCurve;
	protected double[][] _swapPayoffs;
	protected double _swapLegTenor;
	protected double _swapLegMeanReversion;
	
	//RESULT
	protected ArrayList<Double> _stepTime;
	protected int[][] _indexDetector;	
	protected int[] _couponResetIndex;
	protected int[] _exerciseIndex;
	protected boolean _hasExercise;
	protected double[] _periodTenors;
	
	private double[][][] _leverage;
	private boolean[][] _restriction;
	//[legIndex][conditionIndex][periodIndex]
	private double[][][] _lowerLimits;
	private double[][][] _upperLimits;
	//[legIndex][simIndex][periodIndex]
	private double[][][] _coupon;
	//[legIndex][undIndex][simIndex][periodIndex][timeIndex]
	private double[][][][][] _refRates;
	
	protected double[] _sensitivities;	
	protected AbstractAAD _aad;	
	protected int _legNum;
	protected int[] _undNum;
//	
//	public enum GreekType {
//		FLOAT1, FLOAT2, DISCOUNT, SWAP
//	}
//	
	public AADEngine(
			Money principal,
			boolean hasPrincipalExchange,
			DayCountFraction dcf,
			int simNum, int periodNum, int[] underyingNum, 
			int deferredCouponResetIndex, 
			int[] monitorFrequency,
			ArrayList<ArrayList<Double>> tenors,
			boolean hasExercise, int[] exerciseIndex,
			//FloatLeg Information
			InterestRateCurve[][] legIrCurves,
			double[][] legIrTenors,
			double[][] legIrMeanReversions,
			double[][][] legPayoffs,			
			//Discount Information
			InterestRateCurve discountCurve,			
			double discountMeanReversion,
			double[][] discountFactor,
			//average
			double[][][] leverage,
			boolean[][] restriction,	
			//condition
			double[][][] lowerLimits, double[][][] upperLimits,
			double[][][] coupon,
			double[][][][][] refRates) {

		_principal = principal;
		_hasPrincipalExchange = hasPrincipalExchange;
		_dcf = dcf;
		_simNum = simNum;
		_periodNum = periodNum;		
		_deferredCouponResetIndex = deferredCouponResetIndex;
		_monitorFrequency = monitorFrequency;
		_tenors = tenors;
		_hasExercise = hasExercise;
		_exerciseIndex = exerciseIndex; 
		//FloatLeg Information		
		_legIrCurves = legIrCurves;
		_legIrMeanReversions = legIrMeanReversions;
		_legIrTenors = legIrTenors;
		_legPayoffs = legPayoffs;
		//Discount Information
		_discountCurve = discountCurve;
		_discountMeanReversion = discountMeanReversion;
		_discountFactor = discountFactor;
		//average
		_leverage = leverage;
		_restriction = restriction;
		//condition
		_lowerLimits = lowerLimits;
		_upperLimits = upperLimits;
		_coupon = coupon;
		_refRates = refRates;
		
		_indexDetector = new int[_periodNum][];	
		_couponResetIndex = new int[_periodNum];
		_periodTenors = new double[_periodNum];
		_legNum = _legIrCurves.length;
		_undNum = underyingNum;
		
		genSensitivities();
	}
	
	public AADEngine(
			Money principal,
			boolean hasPrincipalExchange,
			DayCountFraction dcf,
			int simNum, int periodNum, int[] underyingNum, 
			int deferredCouponResetIndex, 
			int[] monitorFrequency,
			ArrayList<ArrayList<Double>> tenors,
			boolean hasExercise, int[] exerciseIndex,
			//FloatLeg Information
			InterestRateCurve[][] legIrCurves,
			double[][] legIrTenors,
			double[][] legIrMeanReversions,
			ModelType[][] legModelTypes,
			double[][][] legPayoffs,			
			//Discount Information
			InterestRateCurve discountCurve,			
			double discountMeanReversion,
			ModelType discModelType,
			double[][] discountFactor,
			//average
			double[][][] leverage,
			boolean[][] restriction,	
			//condition
			double[][][] lowerLimits, double[][][] upperLimits,
			double[][][] coupon,
			double[][][][][] refRates) {

		_principal = principal;
		_hasPrincipalExchange = hasPrincipalExchange;
		_dcf = dcf;
		_simNum = simNum;
		_periodNum = periodNum;		
		_deferredCouponResetIndex = deferredCouponResetIndex;
		_monitorFrequency = monitorFrequency;
		_tenors = tenors;
		_hasExercise = hasExercise;
		_exerciseIndex = exerciseIndex; 
		//FloatLeg Information		
		_legIrCurves = legIrCurves;
		_legIrMeanReversions = legIrMeanReversions;
		_legIrTenors = legIrTenors;
		_legPayoffs = legPayoffs;
		//Discount Information
		_discountCurve = discountCurve;
		_discountMeanReversion = discountMeanReversion;
		_discountFactor = discountFactor;
		
		_legModelTypes = legModelTypes;
		_discModelType = discModelType;
		
		//average
		_leverage = leverage;
		_restriction = restriction;
		//condition
		_lowerLimits = lowerLimits;
		_upperLimits = upperLimits;
		_coupon = coupon;
		_refRates = refRates;
		
		_indexDetector = new int[_periodNum][];	
		_couponResetIndex = new int[_periodNum];
		_periodTenors = new double[_periodNum];
		_legNum = _legIrCurves.length;
		_undNum = underyingNum;
		
		genSensitivities();
	}
	
	protected void genSensitivities(){		
		double dt = _monitorFrequency[_monitorFrequency.length - 1] /_dcf.getDaysOfYear();
		double tmpMaxTenor = 0;
		for (int legIndex = 0 ; legIndex < _legNum; legIndex++){
			for (int undIndex = 0; undIndex < _undNum[legIndex]; undIndex++){
				double tenor = _legIrTenors[legIndex][undIndex];
				if (tmpMaxTenor < tenor){
					tmpMaxTenor = tenor;
				}
			}			
		}
		int additionalIndex = (int)Math.ceil(tmpMaxTenor / dt);
		
		//SET StepTime
		_stepTime = new ArrayList<Double>();
		
		for (int periodIndex = 0; periodIndex < _periodNum; periodIndex++){
			//stepTime
			ArrayList<Double> tmpTenors = _tenors.get(periodIndex);
			if(periodIndex == 0){
				_stepTime.add(0.0);				
				for(int i = 0; i < tmpTenors.size(); i++){
					_stepTime.add(_stepTime.get(i) + (Double)(tmpTenors.get(i)));
				}
			} else {						
				for(int i = 0; i < tmpTenors.size(); i++){
					_stepTime.add(_stepTime.get(_stepTime.size() - 1) + (Double)(tmpTenors.get(i)));
				}
				
			}
			//add the float tenor
			if (periodIndex == _periodNum - 1){
				for (int j = 0; j < additionalIndex; j++){
					_stepTime.add(_stepTime.get(_stepTime.size() - 1) + dt);
				}					
			}
			//IndexDetector
			int length = tmpTenors.size();
			_indexDetector[periodIndex] = new int[length];
			
			for (int index = 0; index < length; index++){
				if (periodIndex == 0){
					_indexDetector[periodIndex][index] = index;
				} else {
					int[] tmpIndex = _indexDetector[periodIndex - 1];
					_indexDetector[periodIndex][index] = index + tmpIndex[tmpIndex.length - 1] + 1;
				}
			}	
			
			//couponResetIndex
			if (periodIndex == 0){
				_couponResetIndex[periodIndex] = _deferredCouponResetIndex;
			} else {
				int[] tmpIndex = _indexDetector[periodIndex - 1];
				_couponResetIndex[periodIndex] = tmpIndex[tmpIndex.length - 1] + 1;
			}
			
			//periodTenors
			_periodTenors[periodIndex] = 0;
			for (int i = 0; i < tmpTenors.size(); i++){
				_periodTenors[periodIndex] += tmpTenors.get(i);
			}			
		}	
	}
	
	//for YTM
	public double[] getDelta(PayRcv payRcv, CouponType[] couponType, 
			ConditionType conditionType, HashMap changedIrCurves,
			int legIndex, int underlyingIndex){
		
		if (_aad == null){
			_aad = new AbstractAAD(_hasPrincipalExchange,
					_dcf, _indexDetector, _couponResetIndex, 
					_hasExercise, _exerciseIndex, _stepTime, _periodTenors, 
					_simNum, _periodNum, 
					_legIrCurves, _legIrTenors, _legIrMeanReversions, _legPayoffs, 
					_discountCurve, _discountMeanReversion, _discountFactor, 
					_leverage, _restriction, 
					_lowerLimits, _upperLimits, 
					_coupon, _refRates);
		}
		
		
		_aad.generate(legIndex, underlyingIndex, couponType, conditionType, false);
		_sensitivities = _aad.getDelta();
		
		VertexSensitivityInfoForAAD aad = new VertexSensitivityInfoForAAD(
				_stepTime, _legIrCurves[legIndex][underlyingIndex], _dcf);
		
		double[] greek = aad.getAllSensitivity(_sensitivities);
		double[] result = new double[greek.length];		
		
		int flag = 1;
		if (payRcv.equals(PayRcv.PAY)){
			flag = -1;
		}
		
		InterestRate[] spotRates = 
				_legIrCurves[legIndex][underlyingIndex].getSpotRates();
		
		for (int i = 0; i < greek.length; i++){
			Vertex vertex = spotRates[i].getVertex();
			InterestRateCurve[] irCurves = (InterestRateCurve[]) changedIrCurves.get(vertex.getCode());
			InterestRate[] upSpotRates = irCurves[0].getSpotRates();
			InterestRate[] downSpotRates = irCurves[1].getSpotRates();
			
			for (int j = 0; j < upSpotRates.length; j++){				
				double upSpot = upSpotRates[j].getRate();
				double downSpot = downSpotRates[j].getRate();
				result[i] += flag * (upSpot-downSpot) * greek[j] * _principal.getAmount();		
			}								
		}
		return result;
	}
	
	//for Spot
	public double[] getDelta(PayRcv payRcv, CouponType[] couponType, 
			ConditionType conditionType,
			double epsilon, int legIndex, int underlyingIndex){
		
		if (_aad == null){
			_aad = new AbstractAAD(_dcf, _indexDetector, _couponResetIndex, 
					_hasExercise, _exerciseIndex, _stepTime, _periodTenors, 
					_simNum, _periodNum, 
					_legIrCurves, _legIrTenors, _legIrMeanReversions, _legModelTypes, _legPayoffs, 
					_discountCurve, _discountMeanReversion,_discModelType, _discountFactor, 
					_leverage, _restriction, 
					_lowerLimits, _upperLimits, 
					_coupon, _refRates);
		}
		
		
		_aad.generate(legIndex, underlyingIndex, couponType, conditionType, false);
		_sensitivities = _aad.getDelta();
		
		VertexSensitivityInfoForAAD aad = new VertexSensitivityInfoForAAD(
				_stepTime, _legIrCurves[legIndex][underlyingIndex], _dcf);
		
		double[] greek = aad.getAllSensitivity(_sensitivities);
		double[] result = new double[greek.length];		
		
		int flag = 1;
		if (payRcv.equals(PayRcv.PAY)){
			flag = -1;
		}
		
		InterestRate[] spotRates = 
				_legIrCurves[legIndex][underlyingIndex].getSpotRates();
		
		for (int i = 0; i < greek.length; i++){			
			double upSpot = spotRates[i].getRate() + epsilon;
			double downSpot = spotRates[i].getRate() - epsilon;
			
			double dx = upSpot - downSpot;
		
			result[i] = flag * dx * greek[i] * _principal.getAmount();						
			//System.out.println(result[i]);
		}
		return result;
	}
	
	//for YTM
	public double[] getDiscountDelta(HashMap changedIrCurves){
		if (_aad == null){
			_aad = new AbstractAAD(_dcf, _indexDetector, _couponResetIndex, 
					_hasExercise, _exerciseIndex, _stepTime, _periodTenors, 
					_simNum, _periodNum, 
					_legIrCurves, _legIrTenors, _legIrMeanReversions, _legModelTypes,_legPayoffs, 
					_discountCurve, _discountMeanReversion, _discModelType,_discountFactor, 
					_leverage, _restriction, 
					_lowerLimits, _upperLimits, 
					_coupon, _refRates);
		}
		CouponType[] couponType = new CouponType[_periodNum];
		for (int periodIndex = 0; periodIndex < _periodNum; periodIndex++){
			couponType[periodIndex] = CouponType.ACCRUAL;
		}
		
		_aad.generate(0, 0, couponType, ConditionType.NONE, true);
		_sensitivities = _aad.getDelta();
		
		VertexSensitivityInfoForAAD aad = new VertexSensitivityInfoForAAD(
				_stepTime, _discountCurve, _dcf);
		
		double[] greek = aad.getAllSensitivity(_sensitivities);
		double[] result = new double[greek.length];		

		InterestRate[] spotRates = _discountCurve.getSpotRates();
		
		for (int i = 0; i < greek.length; i++){
			Vertex vertex = spotRates[i].getVertex();
			InterestRateCurve[] irCurves = (InterestRateCurve[]) changedIrCurves.get(vertex.getCode());
			InterestRate[] upSpotRates = irCurves[0].getSpotRates();
			InterestRate[] downSpotRates = irCurves[1].getSpotRates();
			
			for (int j = 0; j < upSpotRates.length; j++){				
				double upSpot = upSpotRates[j].getRate();
				double downSpot = downSpotRates[j].getRate();
				result[i] += (upSpot-downSpot) * greek[j] * _principal.getAmount();		
			}								
		}
		return result;		
	}
	
	//for spot
	public double[] getDiscountDelta(double epsilon){
		if (_aad == null){
			_aad = new AbstractAAD(_hasPrincipalExchange,
					_dcf, _indexDetector, _couponResetIndex, 
					_hasExercise, _exerciseIndex, _stepTime, _periodTenors, 
					_simNum, _periodNum, 
					_legIrCurves, _legIrTenors, _legIrMeanReversions, _legPayoffs, 
					_discountCurve, _discountMeanReversion, _discountFactor, 
					_leverage, _restriction, 
					_lowerLimits, _upperLimits, 
					_coupon, _refRates);
		}
		CouponType[] couponType = new CouponType[_periodNum];
		for (int periodIndex = 0; periodIndex < _periodNum; periodIndex++){
			couponType[periodIndex] = CouponType.AVERAGE;
		}
		
		_aad.generate(0, 0, couponType, ConditionType.NONE, true);
		_sensitivities = _aad.getDelta();
		
		VertexSensitivityInfoForAAD aad = new VertexSensitivityInfoForAAD(
				_stepTime, _discountCurve, _dcf);
		
		double[] greek = aad.getAllSensitivity(_sensitivities);
		double[] result = new double[greek.length];		
		
		InterestRate[] spotRates = 
				_discountCurve.getSpotRates();
		for (int i = 0; i < greek.length; i++){
			double upSpot = spotRates[i].getRate() + epsilon;
			double downSpot = spotRates[i].getRate() - epsilon;
			
			double dx = upSpot - downSpot;
		
			result[i] = dx * greek[i] * _principal.getAmount();						
			//System.out.println(result[i]);
		}
		return result;
		
	}
	
}
