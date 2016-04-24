package com.quantosauros.jpl.engine.pricer;

import java.util.ArrayList;
import java.util.HashSet;

import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.math.distribution.NormalDistributionRandomGenerator;
import com.quantosauros.common.math.matrix.Matrix;
import com.quantosauros.common.math.random.AbstractRandom;
import com.quantosauros.common.math.random.RandomFactory;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.LegStructuredCouponInfo;
import com.quantosauros.jpl.dto.OptionInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.market.MarketInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;
import com.quantosauros.jpl.dto.underlying.RateUnderlyingInfo;
import com.quantosauros.jpl.engine.data.AbstractData;


public abstract class AbstractPricer {

	protected int[] _newIndices;
	protected NormalDistributionRandomGenerator _randomGen;
	protected long _seed;
	protected int _legNum;
	protected int _periodNum;
	protected int _simNum;
	protected int[] _undNum;
	protected Date _asOfDate;
	protected int[] _monitorFrequencies;
	protected int _deferredCouponResetIndex;
		
	protected ProductInfo _productInfo;
	protected LegScheduleInfo[] _legScheduleInfos;
	protected LegDataInfo[] _legDataInfos;
	protected LegAmortizationInfo[] _legAmortizationInfos;
	protected LegCouponInfo[] _legCouponInfos;
	protected OptionInfo _optionInfo;	
		
	protected double[] _totalPayoff;	
	protected double[][][] _payoff;
	protected int[] _exerciseIndex;
	protected boolean _hasExercise;
	protected ArrayList<ArrayList<Double>> _tenors;
	protected AbstractData _data;
	protected int _payIndex;
	protected int _rcvIndex;
	
	protected ModelType _discModelType;
	
	//[legIndex][periodIndex][paramIndex]
	protected double[][][] _lsmcParams;
	protected Matrix _corrMatrix;
	
	public abstract void generate(MarketInfo[][] legMarketInfos,
			RateMarketInfo discountMarketInfo,
			double[][] correlations);
	
	protected void initRandomGen(double[][] correlations){
		
		Matrix corrMatrix = new Matrix(correlations);
		_corrMatrix = corrMatrix;		
		int length = correlations.length;
		int newlength = length * (length - 1) /2;		
		boolean[] corrFlag = new boolean[newlength];
		int index = 0;
		
		ArrayList<Integer> array = new ArrayList<Integer>();		
		for (int i = 0; i < length; i++){
			for (int j = i + 1; j < length; j++){
				if (correlations[i][j] == 1){
					corrFlag[index] = true;
					array.add(j);
				}
				index++;
			}
		}
		HashSet hs = new HashSet(array);
		ArrayList<Integer> newArray = new ArrayList<Integer>(hs);
		int[] newRowColumnIndex = new int[length - hs.size()];
		int indd = 0;		
		for (int i = 0; i < length; i++){
			if (!newArray.contains(i)){
				newRowColumnIndex[indd] = i;				
				indd++;
			}			
		}
		double[][] underlyingCorr = corrMatrix.getMatrix(
				newRowColumnIndex, newRowColumnIndex).getArray();
		
		_newIndices = new int[length];
		if (correlations.length == 2){
			if (corrFlag[0]){
				_newIndices[0] = 0;
				_newIndices[1] = 0;
			} else {
				_newIndices[0] = 0;
				_newIndices[1] = 1;
			}
		} else if (correlations.length == 3){
			if (corrFlag[0]){
				_newIndices[0] = 0;
				_newIndices[1] = 0;
				_newIndices[2] = 1;
			} else if (corrFlag[1]){
				_newIndices[0] = 0;
				_newIndices[1] = 1;
				_newIndices[2] = 0;
			} else if (corrFlag[2]){
				_newIndices[0] = 0;
				_newIndices[1] = 1;
				_newIndices[2] = 1;
			} 
			if (corrFlag[0] && corrFlag[1] || corrFlag[0] && corrFlag[2] ||
					corrFlag[1] && corrFlag[2] || 
					corrFlag[0] && corrFlag[1] && corrFlag[2]){
				_newIndices[0] = 0;
				_newIndices[1] = 0;
				_newIndices[2] = 0;				
			}
		} else if (correlations.length == 4){
			if (corrFlag[0]){
				//0 = 1
				_newIndices[0] = 0;
				_newIndices[1] = 0;
				_newIndices[2] = 1;
				_newIndices[3] = 2;
			} else if (corrFlag[1]){
				//0 = 2
				_newIndices[0] = 0;
				_newIndices[1] = 1;
				_newIndices[2] = 0;
				_newIndices[3] = 2;
			} else if (corrFlag[2]){
				//0 = 3
				_newIndices[0] = 0;
				_newIndices[1] = 1;
				_newIndices[2] = 2;
				_newIndices[3] = 0;
			} else if (corrFlag[3]){
				//1 = 2
				_newIndices[0] = 0;
				_newIndices[1] = 1;
				_newIndices[2] = 1;
				_newIndices[3] = 2;
			} else if (corrFlag[4]){
				//1 = 3
				_newIndices[0] = 0;
				_newIndices[1] = 1;
				_newIndices[2] = 2;
				_newIndices[3] = 1;
			} else if (corrFlag[5]){
				//2 = 3
				_newIndices[0] = 0;
				_newIndices[1] = 1;
				_newIndices[2] = 2;
				_newIndices[3] = 2;
			} 
			if (corrFlag[0] && corrFlag[1]){
				//0 = 1 = 2
				_newIndices[0] = 0;
				_newIndices[1] = 0;
				_newIndices[2] = 0;
				_newIndices[3] = 1;
			} else if (corrFlag[0] && corrFlag[2]){
				//0 = 1 = 3
				_newIndices[0] = 0;
				_newIndices[1] = 0;
				_newIndices[2] = 1;
				_newIndices[3] = 0;
			} else if (corrFlag[1] && corrFlag[2]){
				//0 = 2 = 3
				_newIndices[0] = 0;
				_newIndices[1] = 1;
				_newIndices[2] = 0;
				_newIndices[3] = 0;
			} else if (corrFlag[3] && corrFlag[4]){
				//1 = 2 = 3
				_newIndices[0] = 0;
				_newIndices[1] = 1;
				_newIndices[2] = 1;
				_newIndices[3] = 1;
			} else if (corrFlag[0] && corrFlag[5]){
				//0 = 1, 2 = 3
				_newIndices[0] = 0;
				_newIndices[1] = 0;
				_newIndices[2] = 1;
				_newIndices[3] = 1;
			} else if (corrFlag[1] && corrFlag[4]){
				//0 = 2, 1 = 3
				_newIndices[0] = 0;
				_newIndices[1] = 1;
				_newIndices[2] = 0;
				_newIndices[3] = 1;
			} else if (corrFlag[2] && corrFlag[3]){
				//0 = 3, 1 = 2
				_newIndices[0] = 0;
				_newIndices[1] = 1;
				_newIndices[2] = 1;
				_newIndices[3] = 0;
			}
			if (corrFlag[0] && corrFlag[1] && corrFlag[2]){
				//0 = 1 = 2 = 3
				_newIndices[0] = 0;
				_newIndices[1] = 0;
				_newIndices[2] = 0;
				_newIndices[3] = 0;
			}			
		}
		
		_randomGen = new NormalDistributionRandomGenerator(underlyingCorr,
				RandomFactory.getRandom(AbstractRandom.MERSENNE_TWISTER_FAST, _seed));
	}

	protected void genMonitorFrequency(int monitor){
		_monitorFrequencies = new int[_periodNum];
		int legIndex = 0;
		Date startDate = _legScheduleInfos[legIndex].getPaymentPeriods()[0].getStartDate();
		Date endDate = _legScheduleInfos[legIndex].getPaymentPeriods()[0].getEndDate();
		DayCountFraction dcf = _legScheduleInfos[legIndex].getDCF();
		int freq = (int)Math.ceil(dcf.getDaysOfYear() / endDate.diff(startDate));
				
		int[] monitorNumber = new int[5];	
		if (monitor == 0){
			monitorNumber[0] = 1;
			monitorNumber[1] = 1;
			monitorNumber[2] = 1;
			monitorNumber[3] = 1;
			monitorNumber[4] = 1;
		} else if (monitor == 1){
			monitorNumber[0] = 1;
			monitorNumber[1] = 2;
			monitorNumber[2] = 5;
			monitorNumber[3] = 10;
			monitorNumber[4] = 15;
		} else if (monitor == 10){
			monitorNumber[0] = 5;
			monitorNumber[1] = 5;
			monitorNumber[2] = 10;
			monitorNumber[3] = 30;
			monitorNumber[4] = 30;
//			monitorNumber[0] = monitor;
//			monitorNumber[1] = monitor;
//			monitorNumber[2] = monitor;
//			monitorNumber[3] = monitor;
//			monitorNumber[4] = monitor;
		} else if (monitor == 30){		
			monitorNumber[0] = 5;
			monitorNumber[1] = 15;
			monitorNumber[2] = 30;
			monitorNumber[3] = 45;
			monitorNumber[4] = 45;
		} else {
			monitorNumber[0] = monitor;
			monitorNumber[1] = monitor;
			monitorNumber[2] = monitor;
			monitorNumber[3] = monitor;
			monitorNumber[4] = monitor;
		}
		
		for (int i = 0; i < _periodNum; i ++){
			int reminder = (int) Math.round(i / freq);
			if (reminder < 1){
				_monitorFrequencies[i] = monitorNumber[0];
			} else if (reminder >= 1 && reminder < 2){
				_monitorFrequencies[i] = monitorNumber[1];
			} else if (reminder >= 2 && reminder < 5){
				_monitorFrequencies[i] = monitorNumber[2];
			} else if (reminder >= 5 && reminder < 10){
				_monitorFrequencies[i] = monitorNumber[3];
			} else {
				_monitorFrequencies[i] = monitorNumber[4];
			}
		}	
	}

	public Money getPrice(){
		if (_legNum == 1 ){			
			return getLegPrice(0);
		} else {
			Money payMoney = getLegPrice(_payIndex);
			Money rcvMoney = getLegPrice(_rcvIndex);
			return rcvMoney.subtract(payMoney);
		}				
	}

	public Money getLegPrice(int legIndex){
		return _legAmortizationInfos[legIndex].getPrincipal().multiplyAmount(
				_totalPayoff[legIndex]/ _simNum);
	}
	
	public int getPeriodNum(){
		return _periodNum;
	}	
	public ArrayList<ArrayList<Double>> getTenors(){
		return _tenors;
	}
	public int[] getExerciseIndex(){
		if (_hasExercise){
			return _exerciseIndex;
		} else { 
			return null;
		}
		
	}	
	public void setExerciseIndex(int[] exerciseIndex){
		this._exerciseIndex = exerciseIndex;
	}
	public int getDeferredCouponResetIndex(){
		return _deferredCouponResetIndex;
	}
	public double[][] getCoupon(int legIndex){
		return _data.getCoupon(legIndex);
	}
	public double[][] getTenor(int legIndex){
		return _data.getTenor(legIndex);
	}
	public double[][] getPayOffs(int legIndex){
		return _data.extractPayoffArray(legIndex);
	}	
	public double[][] getDiscounts(){
		return _data.getDiscountFactors();
	}
	public double[][][] getRefRates(int legIndex, int undIndex){
		return _data.getRefRates(legIndex, undIndex);
	}
	
	public boolean getHasExercise(){
		return _hasExercise;
	}
	
	public Money getPrincipal(int legIndex){
		return _legAmortizationInfos[legIndex].getPrincipal();
	}
	
	public DayCountFraction getDCF(int legIndex){
		return _legScheduleInfos[legIndex].getDCF();
	}
	
	public double[][] getLowerLimits(int legIndex){
		return _legCouponInfos[legIndex].getLowerLimits();
	}
	
	public double[][] getUpperLimits(int legIndex){
		return _legCouponInfos[legIndex].getUpperLimits();
	}
	
	public double[] getInCouponRates(int legIndex){
		if (_legCouponInfos[legIndex] instanceof LegStructuredCouponInfo){
			return ((LegStructuredCouponInfo)_legCouponInfos[legIndex]).getInCouponRates();
		} else {
			return null;
		}		
	}
	
	public double[] getOutCouponRates(int legIndex){
		if (_legCouponInfos[legIndex] instanceof LegStructuredCouponInfo){
			return ((LegStructuredCouponInfo)_legCouponInfos[legIndex]).getOutCouponRates();
		} else {
			return null;
		}			
	}
	public double getReferenceTenor(int legIndex, int underlyingIndex){
		return ((RateUnderlyingInfo)_legCouponInfos[legIndex].
					getUnderlyingInfo(underlyingIndex)).getTenor();
	}
	
	public boolean[][][] getRestrictionInfo(){
		return _data.getRestrictionInfo();
	}
	
	public double[] getLeverage(int legIndex, int underlyingIndex){
		return _legCouponInfos[legIndex].getLeverages(underlyingIndex);
	}
	
	public int[] getMonitorFrequencies(){
		return _monitorFrequencies;
	}
	
	public void setHasExercise(boolean hasExercise){
		_hasExercise = hasExercise;
	}
	public int[] getUnderlyingNum(){
		return _undNum;
	}
	public int getSimNum(){
		return _simNum;
	}
	public int getLegNum(){
		return _legNum;
	}
	public int getPayIndex(){
		return _payIndex;
	}
	public int getRcvIndex(){
		return _rcvIndex;
	}
	public double[][] getLSMCParams(int legIndex){
		return _lsmcParams[legIndex];
	}
	public boolean hasPrincipalExchange(){
		return _productInfo.hasPrincipalExchange();
	}
	public ModelType getDiscountModelType(){
		return _discModelType;
	}
	public ModelType[][] getLegModelTypes(){
		ModelType[][] modelTypes = new ModelType[_legNum][];
		for (int legIndex = 0; legIndex < _legNum; legIndex++){
			int undNum = _undNum[legIndex];
			modelTypes[legIndex] = new ModelType[undNum];
			for (int undIndex = 0; undIndex < undNum; undIndex++){
				modelTypes[legIndex][undIndex] = 
						_legCouponInfos[legIndex].getUnderlyingInfo(undIndex).getModelType();
			}
			
		}
		return modelTypes; 
	}
}
