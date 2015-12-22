package com.quantosauros.aad.aad;

import java.util.ArrayList;

import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.interestrate.InterestRateCurve;

public class AbstractAAD {

	//Leg IR Curves
	//[legIndex][underlyingIndex]
	protected InterestRateCurve[][] _legIrCurves;	
	protected double[][] _legIrMeanReversions;
	protected HullWhiteParameters[][] _legIrHWParams;
	protected ModelType[][] _legModelTypes;
	protected ModelType _discModelType;
	protected double[][] _legIrTenors;
	//[legIndex][simIndex][periodIndex]
	protected double[][][] _legPayoffs;
	
	//Discount Information
	protected InterestRateCurve _discountCurve;
	protected double _discountMeanReversion;
	//[simIndex][periodIndex]
	protected double[][] _discountFactor;
	
	//Indices
	protected int[][] _indexDetector;
	protected int[] _couponResetIndex;
	protected int[] _exerciseIndex;
	protected int _sensiNum;
	protected int _simNum;
	protected int _periodNum;
	protected int _legNum;
	protected int _originalLength;
		
	//Tenors
	protected ArrayList<Double> _stepTime;
	protected double[] _periodTenors;
	protected double _timeDt; 
	
	//ETC
	protected double _ep = 0.0001;
	protected DayCountFraction _dcf;
	
	//Flags
	protected boolean _hasExercise;
	protected boolean _hasSwapLeg;
	
	//test
	protected double[][] _sensiResult;
	
	//[legIndex][underlyingIndex][periodIndex]
	protected double[][][] _leverage;
	protected boolean[][] _restriction;	
	
	//[legIndex][conditionIndex][periodIndex]
	private double[][][] _lowerLimits;
	private double[][][] _upperLimits;
	//[legIndex][simIndex][periodIndex]
	private double[][][] _coupon;
	//[legIndex][undIndex][simIndex][periodIndex][timeIndex]
	private double[][][][][] _refRates;
	
	private boolean _debug = false;
	
	public AbstractAAD(
			DayCountFraction dcf,
			//indices, tenors
			int[][] indexDetector,
			int[] couponResetIndex, 
			boolean hasExercise, int[] exerciseIndex,
			ArrayList<Double> stepTime,
			double[] periodTenors,			
			int simNum, int periodNum,
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
		//_numOfAsset = floatCurve.length;
		_sensiNum = stepTime.size();
		int[] tmpIndexDectetor = indexDetector[indexDetector.length - 1];
		_originalLength = tmpIndexDectetor[tmpIndexDectetor.length - 1];
		_legNum = legIrCurves.length;
		
		//leg Information
		_legIrCurves = legIrCurves;
		_legIrTenors = legIrTenors;
		_legIrMeanReversions = legIrMeanReversions;
		_legPayoffs = legPayoffs;
		
		//discount Information
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
		
		_indexDetector = indexDetector;
		_couponResetIndex = couponResetIndex;
		_periodTenors = periodTenors;
		_simNum = simNum;
		_periodNum = periodNum;
		_stepTime = stepTime;
		_exerciseIndex = exerciseIndex;
		_hasExercise = hasExercise;
		_dcf = dcf;
		
	}
	
	public AbstractAAD(
			DayCountFraction dcf,
			//indices, tenors
			int[][] indexDetector,
			int[] couponResetIndex, 
			boolean hasExercise, int[] exerciseIndex,
			ArrayList<Double> stepTime,
			double[] periodTenors,			
			int simNum, int periodNum,
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
		//_numOfAsset = floatCurve.length;
		_sensiNum = stepTime.size();
		int[] tmpIndexDectetor = indexDetector[indexDetector.length - 1];
		_originalLength = tmpIndexDectetor[tmpIndexDectetor.length - 1];
		_legNum = legIrCurves.length;
		
		//leg Information
		_legIrCurves = legIrCurves;
		_legIrTenors = legIrTenors;
		
		_legIrMeanReversions = legIrMeanReversions;
		
		_legModelTypes = legModelTypes;
		_legPayoffs = legPayoffs;
		
		//discount Information
		_discModelType = discModelType;
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
		
		_indexDetector = indexDetector;
		_couponResetIndex = couponResetIndex;
		_periodTenors = periodTenors;
		_simNum = simNum;
		_periodNum = periodNum;
		_stepTime = stepTime;
		_exerciseIndex = exerciseIndex;
		_hasExercise = hasExercise;
		_dcf = dcf;
		
	}
	
	public void generate(int legIndex, int underlyingIndex, 
			CouponType[] couponType, ConditionType conditionType, boolean isDiscount){
		ModelType modelType = null;
		if (_legModelTypes != null){
			if (!isDiscount){
				modelType = _legModelTypes[legIndex][underlyingIndex];
			} else {
				modelType = _discModelType;
			}
		} else {
			modelType = ModelType.HW1F;
		}
		
		_sensiResult = new double[_simNum][_sensiNum];
		for (int periodIndex = _periodNum - 1; periodIndex >= 0; periodIndex--){
			int[] tmpIndex = new int[_indexDetector[periodIndex].length + 1];
			for (int i = 0; i < _indexDetector[periodIndex].length; i++){
				tmpIndex[i] = _indexDetector[periodIndex][i];
			}
			tmpIndex[tmpIndex.length - 1] = tmpIndex[tmpIndex.length - 2] + 1;

			for (int timeIndex = tmpIndex[tmpIndex.length - 1] - 1; 
					timeIndex >= tmpIndex[0]; timeIndex--){
				_timeDt = _stepTime.get(timeIndex + 1) - _stepTime.get(timeIndex);				
				
				ArrayList<Integer> sensiIndexSet = new ArrayList<Integer>();
//				sensiIndexSet.add(timeIndex - 1);				
				sensiIndexSet.add(timeIndex);				
				sensiIndexSet.add(timeIndex + 1);
				sensiIndexSet.add(timeIndex + 2);
				
				//Tenor
				double tenor = 0;
				if (!isDiscount){
					tenor = _legIrTenors[legIndex][underlyingIndex];
				}				
				boolean addFlag = false;
				int addIndex = 1;
				int addtionalIndex = 0;
				while(!addFlag){
					if (_stepTime.get(timeIndex + addIndex) - _stepTime.get(timeIndex) >= tenor){
						addtionalIndex = addIndex;						
						addFlag = true;
					} else {
						addIndex++;
					}
				}		 
				
				int tmpTenorIndex = timeIndex + addtionalIndex;
				sensiIndexSet.add(tmpTenorIndex - 1);
				sensiIndexSet.add(tmpTenorIndex);
				sensiIndexSet.add(tmpTenorIndex + 1);
				sensiIndexSet.add(tmpTenorIndex + 2);
				sensiIndexSet.add(tmpTenorIndex + 3);
				
				for (int simIndex = 0; simIndex < _simNum; simIndex++){
					for(int sensiIndex = 1 ; sensiIndex < _sensiNum; sensiIndex++){
						
						//Calculate Greeks which is related to Sensi Indices
						if (sensiIndexSet.contains(sensiIndex)){
							//dShortRate/dSpotRate
							getShortSensiBySpot(sensiIndex, simIndex, timeIndex, 
									legIndex, underlyingIndex, isDiscount, modelType);
							
							//dPayoff/dSpotRate
							if (timeIndex >= _couponResetIndex[periodIndex]){								
								getDPayoffDSpotRate(simIndex, periodIndex, sensiIndex, 
										timeIndex, legIndex, underlyingIndex, 
										couponType[periodIndex], conditionType, isDiscount, modelType);
							}
						}
					}					
					//dShortRate(t)/dShortRate(t-1)
					getShortSensiByShort(simIndex, 
							legIndex, underlyingIndex, isDiscount);
					
					//dPayoff/dShortRate
					if (timeIndex >= _couponResetIndex[periodIndex]){						
						getDPayoffDShortRate(simIndex, periodIndex, 
								timeIndex, legIndex, underlyingIndex, 
								couponType[periodIndex], conditionType, isDiscount, modelType);
												
					}
					
					if(timeIndex == 0 ){
						for(int sensiIndex = 1 ; sensiIndex < _sensiNum; sensiIndex++){
							getShortSensiBySpot(sensiIndex, simIndex, -1, 
									legIndex, underlyingIndex, isDiscount, modelType);
						}
					}
				}									
			}		
		}		
	}
	
	protected void getShortSensiBySpot(int sensiIndex, int simIndex, int timeIndex, 
			int legIndex, int underlyingIndex, boolean isDiscount, ModelType modelType){
		
		if (isDiscount){
			if (_sensiResult[simIndex][0] != 0){
				_sensiResult[simIndex][sensiIndex] += _sensiResult[simIndex][0] * 
						getShortDerBySpot(_discountCurve,
								sensiIndex, timeIndex + 1, 
								_discountMeanReversion);
			}
		} else {
			_sensiResult[simIndex][sensiIndex] += _sensiResult[simIndex][0] * 
					getShortDerBySpot(_legIrCurves[legIndex][underlyingIndex],
							sensiIndex, timeIndex + 1, 
							_legIrMeanReversions[legIndex][underlyingIndex]);
		}		
	}
	
	protected void getShortSensiByShort(int simIndex, 
			int legIndex, int underlyingIndex, boolean isDiscount){
		if (isDiscount){
			_sensiResult[simIndex][0] *= 
					1 - _discountMeanReversion * _timeDt;
		} else {
			_sensiResult[simIndex][0] *= 
					1 - _legIrMeanReversions[legIndex][underlyingIndex] * _timeDt;
		}				  
	}
			
	//dPayoff/dSpotRate
	protected void getDPayoffDSpotRate(int simIndex, int periodIndex, 
			int sensiIndex, int timeIndex, 
			int legIndex, int underlyingIndex, 
			CouponType couponType, ConditionType conditionType,
			boolean isDiscount, ModelType modelType){
		
		if (isDiscount){
			
		} else {
			_sensiResult[simIndex][sensiIndex] += 
					getDCouponDSpotRate(couponType,
							simIndex, periodIndex, timeIndex, sensiIndex,
							legIndex, underlyingIndex, modelType);
			
			double couponRate = _coupon[legIndex][simIndex][periodIndex];
			
			_sensiResult[simIndex][sensiIndex] += 
					getDTenorDSpotRate(conditionType, 
							simIndex, periodIndex, timeIndex, sensiIndex, 
							legIndex, underlyingIndex,
							couponRate, modelType);
		}
	}
	
	private double getDCouponDSpotRate(CouponType couponType,
			int simIndex, int periodIndex, int timeIndex, int sensiIndex, 
			int legIndex, int underlyingIndex, ModelType modelType){
		
		double result = 0;
		
		switch (couponType){
			case AVERAGE :
				if (_hasExercise){
					if (_exerciseIndex[simIndex] >= periodIndex){
						double DF = 1;
						for (int k = 0 ; k < periodIndex + 1; k++){
							DF *= _discountFactor[simIndex][k];
						}
						
						//FOR Path-Dependent Floating Rate Note
						result = getDRefDSpot(_legIrCurves[legIndex][underlyingIndex], 
										_legIrTenors[legIndex][underlyingIndex], 
										_legIrMeanReversions[legIndex][underlyingIndex],
										sensiIndex, timeIndex, modelType) 
								* _timeDt * DF 
								* _leverage[legIndex][underlyingIndex][periodIndex] 
								* indicatorFunction(_restriction[simIndex][periodIndex]);					
					}
				} else {
					double DF = 1;
					for (int k = 0 ; k < periodIndex + 1; k++){
						DF *= _discountFactor[simIndex][k];
					}
					
					//FOR Path-Dependent Floating Rate Note
					result = getDRefDSpot(_legIrCurves[legIndex][underlyingIndex], 
									_legIrTenors[legIndex][underlyingIndex], 
									_legIrMeanReversions[legIndex][underlyingIndex],
									sensiIndex, timeIndex, modelType) 
							* _timeDt * DF 
							* _leverage[legIndex][underlyingIndex][periodIndex] 
							* indicatorFunction(_restriction[simIndex][periodIndex]);
					
				}
				break;
			case FIXED :
				result = 0;
				break;
			case ACCRUAL :
				result = 0;
				break;
			case RESET :
				if (periodIndex == 0 && _couponResetIndex[periodIndex] == 0
					&& timeIndex == 0){
					//NONE
				} else {
					if (_hasExercise){
						if (_exerciseIndex[simIndex] >= periodIndex){
							if(_couponResetIndex[periodIndex] == timeIndex){
								double DF = 1;
								for (int k = 0 ; k < periodIndex + 1; k++){
									DF *= _discountFactor[simIndex][k];
								}
		
								result = getDRefDSpot(_legIrCurves[legIndex][underlyingIndex], 
												_legIrTenors[legIndex][underlyingIndex],
												_legIrMeanReversions[legIndex][underlyingIndex],
												sensiIndex, timeIndex, modelType) *
										_periodTenors[periodIndex] * DF;
							}
						}
					} else {
						if(_couponResetIndex[periodIndex] == timeIndex){
							double DF = 1;
							for (int k = 0 ; k < periodIndex + 1; k++){
								DF *= _discountFactor[simIndex][k];
							}
							result = getDRefDSpot(_legIrCurves[legIndex][underlyingIndex], 
											_legIrTenors[legIndex][underlyingIndex],
											_legIrMeanReversions[legIndex][underlyingIndex],
											sensiIndex, timeIndex, modelType) *
									_periodTenors[periodIndex] * DF;
						}
					}
				}
				break;
		}
		
		return result;
	}
	
	private double getDTenorDSpotRate(ConditionType conditionType,
			int simIndex, int periodIndex, int timeIndex, int sensiIndex,
			int legIndex, int underlyingIndex,
			double couponRate, ModelType modelType){
		
		double result = 0;
		if (conditionType.equals(ConditionType.NONE)){
			
		} else {
			if (_hasExercise){
				if (_exerciseIndex[simIndex] >= periodIndex){
					double DF = 1;
					for (int k = 0 ; k < periodIndex + 1; k++){
						DF *= _discountFactor[simIndex][k];
					}
					//RA
					double sum = getDDigitalPayoffDSpotRate(
							simIndex, periodIndex, sensiIndex, timeIndex, 
							legIndex, underlyingIndex,
							_lowerLimits[legIndex][0][periodIndex], 
							_upperLimits[legIndex][0][periodIndex], modelType);
					double coupon = sum * couponRate * _timeDt;		
					result = coupon * DF ;
				}
			} else {
				double DF = 1;
				for (int k = 0 ; k < periodIndex + 1; k++){
					DF *= _discountFactor[simIndex][k];
				}
				//RA
				double sum = getDDigitalPayoffDSpotRate(
						simIndex, periodIndex, sensiIndex, timeIndex, 
						legIndex, underlyingIndex,
						_lowerLimits[legIndex][0][periodIndex], 
						_upperLimits[legIndex][0][periodIndex], modelType);
				double coupon = sum * couponRate * _timeDt;		
				result = coupon * DF ;
				
//				if (sum != 0){
//					System.out.println("SPOT(" + simIndex + ", " + periodIndex+ ", " +
//							sensiIndex+ ", " + timeIndex+ ", " + sum +
//							", " + _refRates.get(periodIndex).get(timeIndex) );
//				}
			}	
		}
		
		return result;
		
	}
			
	//dPayoff/dShortRate
	protected void getDPayoffDShortRate(int simIndex, int periodIndex, 
			int timeIndex,
			int legIndex, int underlyingIndex,
			CouponType couponType, ConditionType conditionType, boolean isDiscount, ModelType modelType){
		
		if (isDiscount){
			if (_hasExercise){
				if (_exerciseIndex[simIndex] >= periodIndex){					
					//Discount Delta
					if (_originalLength >= timeIndex){
						int periodNum = (_periodNum == _exerciseIndex[simIndex]) ? 
								_exerciseIndex[simIndex] - 1 : _exerciseIndex[simIndex];

						for (int backStepIndex = periodNum; 
								backStepIndex >= periodIndex; backStepIndex--){
							double DF = 1;
							for(int dfIndex = 0; dfIndex < backStepIndex + 1; dfIndex++){
								DF *= _discountFactor[simIndex][dfIndex];
							}
				
							double tmpPrincipal = (backStepIndex == periodNum) ? 1 : 0;
							for (int discLegIndex = 0; discLegIndex < _legNum; discLegIndex++){
								int flag = (discLegIndex == 0) ? 1 : - 1;
								_sensiResult[simIndex][0] += flag *
										(-(_legPayoffs[discLegIndex][simIndex][backStepIndex] + tmpPrincipal) * 
										DF * _timeDt);
							}
															
						}
					}
				}			
			} else {
				//Discount Delta
				if (_originalLength >= timeIndex){
					for (int backStepIndex = _periodNum - 1; 
							backStepIndex >= periodIndex; backStepIndex--){
						double DF = 1;
						for(int dfIndex = 0; dfIndex < backStepIndex + 1; dfIndex++){
							DF *= _discountFactor[simIndex][dfIndex];
						}
			
						double tmpPrincipal = (backStepIndex == _periodNum - 1) ? 1 : 0;
						
						for (int discLegIndex = 0; discLegIndex < _legNum; discLegIndex++){
							int flag = (discLegIndex == 0) ? 1 : - 1;
		
							_sensiResult[simIndex][0] += flag *
									(-(_legPayoffs[discLegIndex][simIndex][backStepIndex] + tmpPrincipal) * 
									DF * _timeDt);
						}				
					}
				}
//				if (sum != 0){
//					System.out.println("SHORT(" + simIndex + ", " + periodIndex+ ", " +
//							timeIndex+ ", " + sum + ", " +
//							_refRates.get(periodIndex).get(timeIndex) );
//				}
			}
		} else {
			_sensiResult[simIndex][0] += getDCouponDShortRate(couponType,
					simIndex, periodIndex, timeIndex, 
					legIndex, underlyingIndex, modelType);
			
			double couponRate = _coupon[legIndex][simIndex][periodIndex];
			
			_sensiResult[simIndex][0] += getDTenorDShortRate(conditionType, 
					simIndex, periodIndex, timeIndex, 
					legIndex, underlyingIndex,
					couponRate, modelType);
		}		
	}
	
	private double getDCouponDShortRate(CouponType couponType,
			int simIndex, int periodIndex, int timeIndex,
			int legIndex, int underlyingIndex, ModelType modelType){
		
		double result = 0;
		
		switch(couponType){
			case AVERAGE :
				if (_hasExercise){
					if (_exerciseIndex[simIndex] >= periodIndex){
						double DF = 1;
						for (int k = 0 ; k < periodIndex + 1; k++){
							DF *= _discountFactor[simIndex][k];
						}
						
						//FOR Path-Dependent Floating Rate Note//					
						result = getDRefDShortRate(
									_legIrCurves[legIndex][underlyingIndex], 
									_legIrTenors[legIndex][underlyingIndex],
									_legIrMeanReversions[legIndex][underlyingIndex], modelType)
								* _timeDt * DF * _leverage[legIndex][underlyingIndex][periodIndex]
								* indicatorFunction(_restriction[simIndex][periodIndex]);
				
					}					
				} else {
					double DF = 1;
					for (int k = 0 ; k < periodIndex + 1; k++){
						DF *= _discountFactor[simIndex][k];
					}
					
					//FOR Path-Dependent Floating Rate Note
					result = getDRefDShortRate(
							_legIrCurves[legIndex][underlyingIndex], 
							_legIrTenors[legIndex][underlyingIndex],
							_legIrMeanReversions[legIndex][underlyingIndex], modelType)
						* _timeDt * DF * _leverage[legIndex][underlyingIndex][periodIndex]
						* indicatorFunction(_restriction[simIndex][periodIndex]);
				}
				break;
			case FIXED :
				result = 0;
				break;
			case ACCRUAL :
				result = 0;
				break;
			case RESET :
				if (periodIndex == 0 && _couponResetIndex[periodIndex] == 0 
						&& timeIndex == 0){
					
				} else {
					if (_hasExercise){
						if (_exerciseIndex[simIndex] >= periodIndex){
							if(_couponResetIndex[periodIndex] == timeIndex){
								double DF = 1;
								for (int i = 0 ; i < periodIndex + 1; i++){
									DF *= _discountFactor[simIndex][i];
								}
								result = getDRefDShortRate(
											_legIrCurves[legIndex][underlyingIndex],
											_legIrTenors[legIndex][underlyingIndex],
											_legIrMeanReversions[legIndex][underlyingIndex], modelType)
										* DF * _periodTenors[periodIndex];
							}					
						}				
					} else {
						if(_couponResetIndex[periodIndex] == timeIndex){
							double DF = 1;
							for (int i = 0 ; i < periodIndex + 1; i++){
								DF *= _discountFactor[simIndex][i];
							}
							result = getDRefDShortRate(
										_legIrCurves[legIndex][underlyingIndex],
										_legIrTenors[legIndex][underlyingIndex],
										_legIrMeanReversions[legIndex][underlyingIndex], modelType)
									* DF * _periodTenors[periodIndex];
						}				
					}
				}
				break;
		}
		
		return result;
	}
	
	private double getDTenorDShortRate(ConditionType conditionType,
			int simIndex, int periodIndex, int timeIndex,
			int legIndex, int underlyingIndex,
			double couponRate, ModelType modelType){
		
		double result = 0;
		double DF = 0;
		if (conditionType.equals(ConditionType.NONE)){

		} else {
			if (_hasExercise){
				if (_exerciseIndex[simIndex] >= periodIndex){
					DF = 1;
					for (int k = 0 ; k < periodIndex + 1; k++){
						DF *= _discountFactor[simIndex][k];
					}
					//RA
					double sum = getDDigitalPayoffDShortRate(
							simIndex, periodIndex, timeIndex,
							legIndex, underlyingIndex,
							_lowerLimits[legIndex][0][periodIndex], 
							_upperLimits[legIndex][0][periodIndex], modelType);
					double coupon = sum * couponRate * _timeDt;		
					result = coupon * DF;					
				}			
			} else {
				DF = 1;
				for (int k = 0 ; k < periodIndex + 1; k++){
					DF *= _discountFactor[simIndex][k];
				}
				//RA
				double sum = getDDigitalPayoffDShortRate(
						simIndex, periodIndex, timeIndex,
						legIndex, underlyingIndex,
						_lowerLimits[legIndex][0][periodIndex], 
						_upperLimits[legIndex][0][periodIndex], modelType);
				double coupon = sum * couponRate * _timeDt;		
				result = coupon * DF;
						
			}
		}
		
		return result;
	}
		
	//dShort/dSpot
	protected double getShortDerBySpot(InterestRateCurve irCurve, 
			int sensiIndex, int timeIndex, double meanReversion){
		if( timeIndex >= 1){
			double tmpadt =  - meanReversion * 
					(_stepTime.get(timeIndex) - _stepTime.get(timeIndex - 1)) ;
			return forwardSensi(irCurve, timeIndex, sensiIndex) - 
					Math.exp(tmpadt) * forwardSensi(irCurve, timeIndex - 1, sensiIndex);
		} else {
			return forwardSensi(irCurve, timeIndex, sensiIndex); 
		}
	}

	//dRef/dSpot
	//Ref_i : timeIndex
	//spot_j : sensiIndex
	protected double getDRefDSpot(InterestRateCurve irCurve, 
			double tenor, double meanRev, int sensiIndex, int timeIndex, ModelType modelType){
		
		double tmpSensi = 0;
		if (modelType.equals(ModelType.HW1F)){
			tmpSensi = - getB(tenor, meanRev) * 
					forwardSensi(irCurve, timeIndex, sensiIndex) / tenor;
		}
 
		if (sensiIndex < timeIndex){
			return tmpSensi;
		}
		int refTenorStep = 0;		
		double T = _stepTime.get(timeIndex) + tenor;
		double w1 = 1;
		double w2 = 0;
		
		for (int index = timeIndex; index <= _stepTime.size() - 1; index++){
			double dt1 = _stepTime.get(index);			
			double dt2 = dt1;
			if (index != _stepTime.size() - 1){
				dt2 = _stepTime.get(index + 1);
			}
			refTenorStep = index - timeIndex;
			if ((dt1 < T) && (T < dt2)){				
				w1 = (dt2 - T) / (dt2 - dt1);
				w2 = (T - dt1) / (dt2 - dt1);
				break;
			} else if (dt1 == T){				
				break;
			}
		}
		
		if (timeIndex == sensiIndex){
			//sensiIndex = reset Date			
			tmpSensi += (- _stepTime.get(timeIndex)) / tenor;
		} else if (timeIndex + refTenorStep == sensiIndex){
			//sensiIndex = reset Date + Float Tenor
			//TODO
			tmpSensi += w1 * _stepTime.get(sensiIndex) / tenor;
		} else if (timeIndex + refTenorStep + 1 == sensiIndex){
			//sensiIndex = reset Date + Float Tenor + 1
			//TODO
			tmpSensi += w2 * _stepTime.get(sensiIndex) / tenor;
		}		
		return tmpSensi;
	}
	
	protected double getDRefDShortRate(InterestRateCurve irCurve,
			double tenor, double meanRev, ModelType modelType){
		if (modelType.equals(ModelType.HW1F)){
			return getB(tenor, meanRev) / tenor;
		} else {
			return 0;
		}
	}
	
	//dForward/dSpot
	protected double forwardSensi(InterestRateCurve irCurve, 
			int forwardTime, int RefTime){
				
		if(RefTime > forwardTime + 1 || RefTime < forwardTime){
			return 0;
		}
		if (RefTime == forwardTime + 1){
			return ((_stepTime.get(forwardTime) + 1 / _dcf.getDaysOfYear()) 
					/ (_stepTime.get(forwardTime + 1) - _stepTime.get(forwardTime)));
		} else {
			return 1 - ((_stepTime.get(RefTime) + 1 / _dcf.getDaysOfYear()) 
					/ (_stepTime.get(RefTime + 1) - _stepTime.get(RefTime)));
		}
	}
		
	protected double getB(double tenor, double meanRev){		
		double exponentialB = (1 - Math.exp(-meanRev * tenor)) / meanRev;
		return exponentialB; 
	}

	public double[] getDelta(){
		double[] result = new double[_sensiNum];
		for (int j = 0; j < result.length ; j++){	
			for(int i = 0; i < _simNum; i++){
				result[j] += _sensiResult[i][j];
			}
			result[j] = result[j] / _simNum;
		}
		return result;
	}	

	protected int indicatorFunction(boolean restricted){
		if (restricted){
			return 0;
		} else {
			return 1;
		}	
	}
	
	private double getDDigitalPayoffDShortRate(
			int simIndex, int periodIndex, int timeIndex,
			int legIndex, int underlyingIndex,
			double lowerBound, double upperBound, ModelType modelType){
		
		//Replicate
//		double op1 = getDVanillaPayoffDShortRate(
//				simIndex, periodIndex, timeIndex, lowerBound - _ep, true);
//		double op2 = getDVanillaPayoffDShortRate(
//				simIndex, periodIndex, timeIndex, lowerBound, true);
//		double op3 = getDVanillaPayoffDShortRate(
//				simIndex, periodIndex, timeIndex, upperBound, true);
//		double op4 =getDVanillaPayoffDShortRate(
//				simIndex, periodIndex, timeIndex, upperBound + _ep, true);
//		return (op1 - op2 - op3 + op4) / _ep;
		
		//Gaussian function
//		double ref = _refRates.get(simIndex).get(timeIndex);
//		double v1 = Math.exp(- Math.pow((ref-lowerBound),2) / (_ep * _ep)) / (_ep * Math.sqrt(Math.PI));
//		double v2 = Math.exp(- Math.pow((ref-upperBound),2) / (_ep * _ep)) / (_ep * Math.sqrt(Math.PI));
//		return (v1 - v2) * getDRefDShortRate(_floatCurve[0], _floatTenor[0],
//				_floatMeanReversion[0]);
		
		//Cauchy-Lorentz distribution
		int initialIndex = _indexDetector[periodIndex][0];
		double ref = _refRates[legIndex][underlyingIndex][simIndex][periodIndex][timeIndex - initialIndex];
		double v1 = 1.0 / (_ep + Math.pow((ref - lowerBound),2) / _ep);
		double v2 = 1.0 / (_ep + Math.pow((ref - upperBound),2) / _ep);
		
		double tmpSensi = (v1 - v2) / Math.PI;
		double tmpEp = 0.00001;
		if (_debug){
			if (Math.abs(ref - lowerBound) <= tmpEp || Math.abs(ref - upperBound) <= tmpEp){
				System.out.println("SHORT(" + simIndex + ": " + periodIndex+ ": " +
						timeIndex+ ": " + tmpSensi + ": " + ref);
			}
		}
		return tmpSensi * getDRefDShortRate(_legIrCurves[legIndex][underlyingIndex],
				_legIrTenors[legIndex][underlyingIndex],
				_legIrMeanReversions[legIndex][underlyingIndex], modelType);		
		
		//Sinc function
//		double ref = _refRates.get(simIndex).get(timeIndex);
//		double t_a1 = (ref - lowerBound) / _ep;
//		double t_a2 = (ref - upperBound) / _ep;
//		double sincFunction1 = Math.sin(t_a1) / t_a1;
//		double sincFunction2 = Math.sin(t_a2) / t_a2;		
//		
//		return (sincFunction1 - sincFunction2) / (_ep * Math.PI) 
//				* getDRefDShortRate(_floatCurve[0], _floatTenor[0],
//						_floatMeanReversion[0]);


	}
	
	private double getDDigitalPayoffDSpotRate(
			int simIndex, int periodIndex, int sensiIndex, int timeIndex,
			int legIndex, int underlyingIndex,
			double lowerBound, double upperBound, ModelType modelType){
		
		//Replicate
//		double op1 = getDVanillaPayoffDSpotRate(
//				simIndex, periodIndex, sensiIndex, timeIndex, lowerBound - _ep, true);
//		double op2 = getDVanillaPayoffDSpotRate(
//				simIndex, periodIndex, sensiIndex, timeIndex, lowerBound, true);
//		double op3 = getDVanillaPayoffDSpotRate(
//				simIndex, periodIndex, sensiIndex, timeIndex, upperBound, true);
//		double op4 =getDVanillaPayoffDSpotRate(
//				simIndex, periodIndex, sensiIndex, timeIndex, upperBound + _ep, true);
//		return (op1 - op2 - op3 + op4) / _ep;
		
		//Gaussian function
//		double ref = _refRates.get(simIndex).get(timeIndex);
//		double v1 = Math.exp(- Math.pow((ref-lowerBound),2) / (_ep * _ep)) / (_ep * Math.sqrt(Math.PI));
//		double v2 = Math.exp(- Math.pow((ref-upperBound),2) / (_ep * _ep)) / (_ep * Math.sqrt(Math.PI));
//		return (v1 - v2) * getDRefDSpot(_floatCurve[0], _floatTenor[0],
//				_floatMeanReversion[0], sensiIndex, timeIndex);
		
		//Cauchy-Lorentz distribution
		int initialIndex = _indexDetector[periodIndex][0];
		double ref = _refRates[legIndex][underlyingIndex][simIndex][periodIndex][timeIndex - initialIndex];
		double v1 = 1.0 / (_ep + Math.pow((ref - lowerBound),2) / _ep);
		double v2 = 1.0 / (_ep + Math.pow((ref - upperBound),2) / _ep);
		
		double tmpSensi = (v1 - v2) / Math.PI;
		double tmpEp = 0.00001;
		if (_debug){
			if (Math.abs(ref - lowerBound) <= tmpEp || Math.abs(ref - upperBound) <= tmpEp){
				System.out.println("SPOT(" + simIndex + ": " + periodIndex+ ": " +
						timeIndex+ ": " + tmpSensi + ": " + ref);
			}
		}
		return tmpSensi * getDRefDSpot(_legIrCurves[legIndex][underlyingIndex],
				_legIrTenors[legIndex][underlyingIndex],
				_legIrMeanReversions[legIndex][underlyingIndex],
				sensiIndex, timeIndex, modelType);
		
		//Sinc function
//		double ref = _refRates.get(simIndex).get(timeIndex);
//		double t_a1 = (ref - lowerBound) / _ep;
//		double t_a2 = (ref - upperBound) / _ep;
//		double sincFunction1 = Math.sin(t_a1) / t_a1;
//		double sincFunction2 = Math.sin(t_a2) / t_a2;		
//		
//		return (sincFunction1 - sincFunction2) / (_ep * Math.PI) 
//				* getDRefDSpot(_floatCurve[0], _floatTenor[0],
//				_floatMeanReversion[0], sensiIndex, timeIndex);
		
//		double ref = _refRates.get(simIndex).get(timeIndex);
//		double ep = _ep;
//		double v1 = 1.0 / (ep + Math.pow((ref - lowerBound),2) / ep);
//		double v2 = 1.0 / (ep + Math.pow((ref - upperBound),2) / ep);
//		
//		double tmpSensi = (v1 - v2) / Math.PI;
//		return tmpSensi * getDRefDSpot(_floatCurve[0], _floatTenor[0],
//				_floatMeanReversion[0], sensiIndex, timeIndex);

	}
	
}
