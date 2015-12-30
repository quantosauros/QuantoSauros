package com.quantosauros.jpl.engine.pricer;

import java.util.ArrayList;

import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.ModelType;
import com.quantosauros.common.TypeDef.OptionType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.TypeDef.RiskFreeType;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.PaymentPeriod;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.LegStructuredCouponInfo;
import com.quantosauros.jpl.dto.OptionInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.market.EquityMarketInfo;
import com.quantosauros.jpl.dto.market.MarketInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;
import com.quantosauros.jpl.dto.underlying.EquityUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;
import com.quantosauros.jpl.engine.data.StructuredData;
import com.quantosauros.jpl.engine.method.model.hullwhite.HullWhiteStripping;
import com.quantosauros.jpl.engine.method.montecarlo.lsmc.AbstractLSMC;
import com.quantosauros.jpl.engine.method.montecarlo.motion.GeneralizedWienerMotion;
import com.quantosauros.jpl.engine.method.montecarlo.motion.MotionGenerator;
import com.quantosauros.jpl.engine.method.montecarlo.motion.MultipleGeneralizedWienerProcess;

public class StructuredPricer extends AbstractPricer{

	public StructuredPricer(
			Date asOfDate, 
			ProductInfo productInfo,
			LegScheduleInfo[] legScheduleInfos,
			LegDataInfo[] legDataInfos,
			LegAmortizationInfo[] legAmortizationInfos,
			LegCouponInfo[] legCouponInfos,
			OptionInfo optionInfo,			
			ModelType discountModelType,
			int monitorFrequency,
			long seed, int simNum) {
		
		_asOfDate = asOfDate;
		_productInfo = productInfo;
		_legScheduleInfos = legScheduleInfos;
		_legDataInfos = legDataInfos;
		_legAmortizationInfos = legAmortizationInfos;
		_legCouponInfos = legCouponInfos;
		_optionInfo = optionInfo;		
		
		_legNum = _legScheduleInfos.length;
		_simNum = simNum;
		_undNum = new int[_legNum];
		for (int legIndex = 0; legIndex < _legNum; legIndex++){
			_undNum[legIndex] = _legCouponInfos[legIndex].getUnderlyingNumber();
		}
		
		_seed = seed;
		_hasExercise = _optionInfo.hasExercise();
		dividePeriods();
		genMonitorFrequency(monitorFrequency);
		_payoff = new double[_legNum][_simNum][_periodNum + 1];
		_totalPayoff = new double[_legNum];
		_tenors = new ArrayList<ArrayList<Double>>(_periodNum);	
		_lsmcParams = new double[_legNum][_periodNum][];
		
		if (_legCouponInfos[0].getPayRcv().equals(PayRcv.PAY)){
			_payIndex = 0;
			_rcvIndex = 1;
		} else {
			_payIndex = 1;
			_rcvIndex = 0;
		}
		
		_discModelType = discountModelType;
	}
			
	protected void dividePeriods(){		
		for (int legIndex = 0; legIndex < _legNum; legIndex++){
			PaymentPeriod[] periods = _legScheduleInfos[legIndex].getPaymentPeriods();						
			int periodNum = periods.length;			
			int idx = 0;
			while (_asOfDate.diff(periods[idx].getEndDate()) >= 0){
				if (idx == periodNum)
					break;
				idx++;
			}
			
			_periodNum = periodNum - idx;
			
			//period
			PaymentPeriod[] newPeriods = new PaymentPeriod[_periodNum];
			System.arraycopy(periods, idx, newPeriods, 0, _periodNum);
			_legScheduleInfos[legIndex].setPaymentPeriods(newPeriods);
			
			//floor
			if (_legCouponInfos[legIndex].hasFloor()){				
				double[] floor = _legCouponInfos[legIndex].getFloor();
				double[] newFloor = new double[_periodNum];
				System.arraycopy(floor, idx, newFloor, 0, _periodNum);
				_legCouponInfos[legIndex].setFloor(newFloor);							
			}
			
			//cap
			if (_legCouponInfos[legIndex].hasCap()){
				double[] cap = _legCouponInfos[legIndex].getCap();
				double[] newCap = new double[_periodNum];
				System.arraycopy(cap, idx, newCap, 0, _periodNum);
				_legCouponInfos[legIndex].setCap(newCap);								
			}
						
			//spread
			double[] spread = _legCouponInfos[legIndex].getSpreads();	
			double[] newSpread = new double[_periodNum];
			System.arraycopy(spread, idx, newSpread, 0, _periodNum);
			_legCouponInfos[legIndex].setSpreads(newSpread);
			
			//Upper, Lower Limit
			if (!_legCouponInfos[legIndex].getConditionType().equals(ConditionType.NONE)){
				int condiNum = _legCouponInfos[legIndex].getConditionNumber();
				double[][] upperLimits = _legCouponInfos[legIndex].getUpperLimits();
				double[][] lowerLimits = _legCouponInfos[legIndex].getLowerLimits();
				
				double[][] newUpperLimits = new double[condiNum][_periodNum];
				double[][] newLowerLimits = new double[condiNum][_periodNum];
				for (int condIndex = 0; condIndex < condiNum; condIndex++){					
					System.arraycopy(upperLimits[condIndex], idx, newUpperLimits[condIndex],
							0, _periodNum);
					_legCouponInfos[legIndex].setUpperLimits(condIndex, 
							newUpperLimits[condIndex]);					
					
					System.arraycopy(lowerLimits[condIndex], idx, newLowerLimits[condIndex],
							0, _periodNum);
					_legCouponInfos[legIndex].setLowerLimits(condIndex, newLowerLimits[condIndex]);
				}
			}
			
			//Leverage
			if (_legCouponInfos[legIndex].hasLeverage()){
				//leverage
				for (int underIndex = 0; underIndex < 
						_legCouponInfos[legIndex].getUnderlyingNumber(); underIndex++){
					double[] leverage = _legCouponInfos[legIndex].getLeverages(underIndex);
					double[] newLeverage = new double[_periodNum];
					System.arraycopy(leverage, idx, newLeverage, 0, _periodNum);
					_legCouponInfos[legIndex].setLeverage(underIndex, newLeverage);					
				}				
			}
						
			//InCoupon, OutCoupon
			if (_legCouponInfos[legIndex].hasInOutCouponRates()){
				double[] inCouponRates = _legCouponInfos[legIndex].getInCouponRates();
				double[] outCouponRates = _legCouponInfos[legIndex].getOutCouponRates();
				double[] newInCouponRates = new double[_periodNum];
				double[] newOutCouponRates = new double[_periodNum];
				System.arraycopy(inCouponRates, idx, newInCouponRates, 0, _periodNum);
				System.arraycopy(outCouponRates, idx, newOutCouponRates, 0, _periodNum);
				_legCouponInfos[legIndex].setInCouponRates(newInCouponRates);
				_legCouponInfos[legIndex].setOutCouponRates(newOutCouponRates);
			}			
		}		
	}
		
	public void generate(MarketInfo[][] legMarketInfos,
			RateMarketInfo discountMarketInfo,
			double[][] correlations){
		
		initRandomGen(correlations);
		
		ArrayList<MarketInfo> legMarketInfoArray = new ArrayList<>();
		ArrayList<UnderlyingInfo> legUnderlyingInfoArray = new ArrayList<>();		
		
		for (int legIndex = 0; legIndex < _legNum; legIndex++){
			for (int undIndex = 0; undIndex < _undNum[legIndex]; undIndex++){				
				MarketInfo marketInfo = legMarketInfos[legIndex][undIndex];
				UnderlyingInfo underlyingInfo = _legCouponInfos[legIndex].getUnderlyingInfo(undIndex);
				
				if (marketInfo instanceof RateMarketInfo){
					RateMarketInfo rateMarketInfo = (RateMarketInfo)marketInfo;
					if (rateMarketInfo.isHWVolSurface()){
						HullWhiteStripping hwStripping = new HullWhiteStripping(
								_asOfDate, _productInfo.getMaturityDate(), 
								_optionInfo.getExerciseDates(),
								rateMarketInfo.getHWVolatilitySurface());
						rateMarketInfo.setHWVolatilities(
								hwStripping.getHWVolatility(underlyingInfo.getModelType()));
					}										
				}
								
				legMarketInfoArray.add(marketInfo);
				legUnderlyingInfoArray.add(underlyingInfo);
			}
		}
		
		if (discountMarketInfo.isHWVolSurface()){
			HullWhiteStripping hwStripping = new HullWhiteStripping(
					_asOfDate, _productInfo.getMaturityDate(), 
					_optionInfo.getExerciseDates(),
					discountMarketInfo.getHWVolatilitySurface());
			discountMarketInfo.setHWVolatilities(
					hwStripping.getHWVolatility(_discModelType));
		}
				
		declareDataClass(legMarketInfoArray, legUnderlyingInfoArray,
				discountMarketInfo, _discModelType);		
		
		genData(legMarketInfoArray, legUnderlyingInfoArray,
				discountMarketInfo, _discModelType);
		
		calcPayoff();
	}
	
	protected void declareDataClass(
			ArrayList<MarketInfo> legMarketInfoArray,
			ArrayList<UnderlyingInfo> legUnderlyingInfoArray,
			RateMarketInfo discMarketInfo,
			ModelType discModelType){
		
		_data = new StructuredData(_asOfDate, 
				_productInfo.getIssueDate(), _productInfo.getMaturityDate(), 
				_legScheduleInfos[0].getPaymentPeriods(),
				DayCountFraction.ACTUAL_365, _simNum, _periodNum,
				_legCouponInfos, _legDataInfos,
				legMarketInfoArray,
				legUnderlyingInfoArray,
				discMarketInfo,
				_discModelType);		
	}
	
	protected void genData(
			ArrayList<MarketInfo> legMarketInfoArray,
			ArrayList<UnderlyingInfo> legUnderlyingInfoArray,
			RateMarketInfo discMarketInfo,
			ModelType discModelType){
		
		int motionSize = legUnderlyingInfoArray.size();
		int legIndex = 0;
		for (int simIndex = 0; simIndex < _simNum; simIndex++){
			
			double startTenor = 0;
			double[][] pathInitial = new double[motionSize + 1][];
			for (int motionIndex = 0; motionIndex < motionSize; motionIndex++){
				ModelType modelType = legUnderlyingInfoArray.get(motionIndex).getModelType();
				if (modelType.equals(ModelType.HW1F)){
					pathInitial[motionIndex] = new double[]{0};
				} else if (modelType.equals(ModelType.HW2F)) {
					pathInitial[motionIndex] = new double[]{0, 0};
				} else if (modelType.equals(ModelType.BS)){
					double currEQPrice = ((EquityMarketInfo)legMarketInfoArray.get(motionIndex)).getCurrEquityPrice();
					double baseEQPrice = ((EquityUnderlyingInfo)legUnderlyingInfoArray.get(motionIndex)).getBasePrice();
					pathInitial[motionIndex] = new double[]{Math.log(currEQPrice / baseEQPrice)};
				}				
			}
			
			if (discModelType.equals(ModelType.HW1F)){
				pathInitial[motionSize] = new double[]{0};
			} else if (discModelType.equals(ModelType.HW2F)){
				pathInitial[motionSize] = new double[]{0, 0};
			}
			
			for (int periodIndex = 0; periodIndex < _periodNum; periodIndex++){
				Date startDate = _legScheduleInfos[legIndex].getPaymentPeriods()[periodIndex].getStartDate();
				Date endDate = _legScheduleInfos[legIndex].getPaymentPeriods()[periodIndex].getEndDate();
				
				DayCountFraction dcf = _legScheduleInfos[legIndex].getDCF();
				double deferredTenor = dcf.getYearFraction(_asOfDate, startDate);
				startDate = (periodIndex == 0) ? _asOfDate : startDate;
				double periodTenor = dcf.getYearFraction(startDate, endDate);
				double dt = _monitorFrequencies[periodIndex] / dcf.getDaysOfYear();
				// 남은 기간이 dt 미만일 경우 예외 처리
				if (periodTenor < dt) {
					dt = 1.0 / dcf.getDaysOfYear();
				}
				int couponIndex = (periodIndex == 0) ?
						Math.max((int) Math.round(deferredTenor / dt), 0) : 0;
				_deferredCouponResetIndex = (couponIndex != 0) ? 
						couponIndex : _deferredCouponResetIndex ;
				
				//Exercise
				double exerciseTenor = 0;
				boolean hasExerciseAtThisPeriod = false;		
				if (_hasExercise){
					for (int k = 0; k < _optionInfo.getExerciseDates().length; k++){						
						if (_optionInfo.getExerciseDates()[k].diff(startDate.plusDays(1)) * 
								endDate.diff(_optionInfo.getExerciseDates()[k]) >= 0){
							Date exerciseDate = _optionInfo.getExerciseDates()[k];		
							hasExerciseAtThisPeriod = true;
							exerciseTenor = dcf.getYearFraction(startDate, exerciseDate);
							break;
						}
					}
				}
				
				GeneralizedWienerMotion[] processes = new GeneralizedWienerMotion[motionSize + 1];				
				MarketInfo[] marketInfos = new MarketInfo[motionSize + 1];
				
				for (int motionIndex = 0; motionIndex < motionSize; motionIndex++){
					processes[motionIndex] = MotionGenerator.getInstance(
							periodTenor, dt, startTenor, exerciseTenor, dcf, 
							legUnderlyingInfoArray.get(motionIndex).getModelType(),
							legMarketInfoArray.get(motionIndex),
							_randomGen);	
					marketInfos[motionIndex] = legMarketInfoArray.get(motionIndex);
				}
				processes[motionSize] = MotionGenerator.getInstance(
						periodTenor, dt, startTenor, exerciseTenor, dcf,
						discModelType, discMarketInfo, _randomGen);
				marketInfos[motionSize] = discMarketInfo;				
				
				MultipleGeneralizedWienerProcess multiMotion = 
						new MultipleGeneralizedWienerProcess(processes, _randomGen);
				double[] tenorArray = new double[processes[0].getTenors().size()];
				for (int arrayInt = 0; arrayInt < tenorArray.length; arrayInt++){
					tenorArray[arrayInt] = (double) processes[0].getTenors().get(arrayInt);
				}
								
				//genRnd
				double[][][] totalRndsModified = multiMotion.pathGenerate(
						_newIndices, tenorArray, simIndex, marketInfos);
				
				//generate hull-white X
				double[][][] hwX = new double[motionSize + 1][][];								
				double[][] generatedPath = new double[motionSize + 1][];
				int bsIndex = 0;
				boolean hasBSUnd = false;
				for (int motionIndex = 0; motionIndex < motionSize; motionIndex++){
					//hwX
					hwX[motionIndex] = processes[motionIndex].generate(
							totalRndsModified[motionIndex], pathInitial[motionIndex]);
					
					ModelType modelType = legUnderlyingInfoArray.get(motionIndex).getModelType();
					
					if (modelType.equals(ModelType.BS)){
						bsIndex = motionIndex;	
						hasBSUnd = true;
					} else {
						//Initial Value
						int length = hwX[motionIndex].length;
						for (int assetIndex = 0; assetIndex < length; assetIndex++){
							int lastIndex = hwX[motionIndex][assetIndex].length - 1;
							pathInitial[motionIndex][assetIndex] = 
									processes[motionIndex].getInitialValue(
											hwX[motionIndex][assetIndex][lastIndex],
											totalRndsModified[motionIndex][assetIndex][lastIndex],
											assetIndex);						
						}
						//Path Generation
						generatedPath[motionIndex] = processes[motionIndex].addAlpha(
								hwX[motionIndex]);
					}
				}
				//Discount Path
				hwX[motionSize] = processes[motionSize].generate(
						totalRndsModified[motionSize], pathInitial[motionSize]);
				int length = hwX[motionSize].length;
				for (int assetIndex = 0; assetIndex < length; assetIndex++){
					int lastIndex = hwX[motionSize][assetIndex].length - 1;
					pathInitial[motionSize][assetIndex] = 
							processes[motionSize].getInitialValue(
									hwX[motionSize][assetIndex][lastIndex],
									totalRndsModified[motionSize][assetIndex][lastIndex],
									assetIndex);						
				}
				generatedPath[motionSize] = processes[motionSize].addAlpha(hwX[motionSize]);
				
				//TODO Black Scholes
				if (hasBSUnd){
					RiskFreeType rfType = ((EquityUnderlyingInfo)legUnderlyingInfoArray.get(bsIndex)).getRiskFreeType();
					int rfIndex = 0;
					if (rfType.equals(RiskFreeType.L1U1)){
						rfIndex = 0;
					}
					generatedPath[bsIndex] = processes[bsIndex].addAlpha(
							hwX[bsIndex], generatedPath[rfIndex]);
					
					int lastIndex = hwX[bsIndex][0].length - 1;
					pathInitial[bsIndex][0] = processes[bsIndex].getInitialValue(
							generatedPath[bsIndex][lastIndex],
							totalRndsModified[bsIndex][0][lastIndex],
							generatedPath[rfIndex][lastIndex]);		
				}						
				
				if (simIndex == 0){
					_tenors.add(processes[0].getTenors());
				}
				
				((StructuredData)_data).setData(simIndex, periodIndex, couponIndex,
						processes[0].getTenors(), startTenor, dt,
						hasExerciseAtThisPeriod,
						generatedPath, hwX);
				startTenor += periodTenor;
								
			}
		}		
	}
	
	protected void calcPayoff(){
				
		//Exercise Index 고정 여부
		boolean useExerciseIndex = false;
		if (_exerciseIndex == null){
			_exerciseIndex = new int[_simNum];			
		} else {
			useExerciseIndex = true;
		}
		
		double principal = 0.0;
		if (_productInfo.hasPrincipalExchange()){
			principal = 1.0;
		}
		
		//1. 만기 payoff에 원금 넣어주기
		for (int legIndex = 0; legIndex < _legNum; legIndex++){
			for (int simIndex = 0; simIndex < _simNum; simIndex++){	
									
				_payoff[legIndex][simIndex][_periodNum] = principal;
				
				if (!useExerciseIndex){
					_exerciseIndex[simIndex] = _periodNum;
				}
			}
			_totalPayoff[legIndex] = 0;
		}
		
		//2. LSMC 적용 (구간별 -> 시뮬레이션별)		
		for (int periodIndex = _periodNum - 1; periodIndex >= 0; periodIndex--){			
			//Determine the Exercise			
			boolean hasExerciseAtThisPeriod = _data.getHasExercise(periodIndex);				
			if (hasExerciseAtThisPeriod){
				double exercisePrice = principal;
				if (!useExerciseIndex){
					double[][][] X = new double[_legNum][][];						
					double[][] Y = new double[_legNum][_simNum];					
					int structuredLegIndex = 0;
					
					for (int legIndex = 0; legIndex < _legNum; legIndex++){			
						int rfNum = 2;
						X[legIndex] = new double[rfNum][_simNum];
						for (int simIndex = 0; simIndex < _simNum; simIndex++) {	
							X[legIndex][0][simIndex] = 
									_data.getLSMC(legIndex, simIndex, periodIndex)[0];
							X[legIndex][1][simIndex] = 
									_data.getLSMC(legIndex, simIndex, periodIndex)[1];
							Y[legIndex][simIndex] = 
									_payoff[legIndex][simIndex][periodIndex + 1];						
						}
						if (_legCouponInfos[legIndex] instanceof LegStructuredCouponInfo){
							structuredLegIndex = legIndex;
						}
					}					
					
					AbstractLSMC lsmc = new AbstractLSMC(X, Y, exercisePrice, structuredLegIndex);
					for (int legIndex = 0; legIndex < _legNum; legIndex++){
						if (_lsmcParams[legIndex][periodIndex] != null){
							lsmc.setLSMCParams(legIndex, _lsmcParams[legIndex][periodIndex]);
						}
					}
					
					lsmc.generate();
					
					double[][] contiValue = new double[_legNum][];
					for (int legIndex = 0; legIndex < _legNum; legIndex++){
						contiValue[legIndex] = lsmc.getContinuationValues(legIndex);
						_lsmcParams[legIndex][periodIndex] = lsmc.getLSMCParams(legIndex);
					}
					
					for (int simIndex = 0; simIndex < _simNum; simIndex++){
						double rcvContiValue = 0;
						double payContiValue = 0;
						if (_legNum == 1 ){
							rcvContiValue = _rcvIndex == 1 ? principal : contiValue[_rcvIndex][simIndex];
							payContiValue = _payIndex == 1 ? principal : contiValue[_payIndex][simIndex];
						} else {
							rcvContiValue = contiValue[_rcvIndex][simIndex];
							payContiValue = contiValue[_payIndex][simIndex];
						}
						
						if (_optionInfo.getOptionType().equals(OptionType.CALL)){							
							if (rcvContiValue - payContiValue < 0){
								for (int legIndex = 0; legIndex < _legNum; legIndex++){
									_payoff[legIndex][simIndex][periodIndex + 1] = principal;
								}	 	 						
	 	 						_exerciseIndex[simIndex] = periodIndex;
							}							
						} else if (_optionInfo.getOptionType().equals(OptionType.PUT)){
							if (rcvContiValue - payContiValue > 0){
								for (int legIndex = 0; legIndex < _legNum; legIndex++){
									_payoff[legIndex][simIndex][periodIndex + 1] = principal;
								}	 	 						
	 	 						_exerciseIndex[simIndex] = periodIndex;
							}
						} 	
					}
					
				} else {
					for (int simIndex = 0; simIndex < _simNum; simIndex++) {	
						if (_exerciseIndex[simIndex] == periodIndex) {
							for (int legIndex = 0; legIndex < _legNum; legIndex++){
								_payoff[legIndex][simIndex][periodIndex + 1] = principal;
							}
						} 							
					}
				}
			}
			
			//Calculate the Payoff				
			for (int simIndex = 0; simIndex < _simNum; simIndex++){
				for (int legIndex = 0; legIndex < _legNum; legIndex++){
					double curPayoff =  _data.getPayoff(legIndex, simIndex, periodIndex);
					double prevPayoff = _payoff[legIndex][simIndex][periodIndex + 1];
					double DF = _data.getDiscountFactor(simIndex, periodIndex);
					
					_payoff[legIndex][simIndex][periodIndex] = (curPayoff + prevPayoff) * DF;					
				}
			}				
		}
		
		for (int i = 0; i < _simNum; i++){
			for (int legIndex = 0; legIndex < _legNum; legIndex++){
				_totalPayoff[legIndex] += _payoff[legIndex][i][0];
			}
		}		
	}	

}
