package com.quantosauros.batch.instrument.calculator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.quantosauros.aad.engine.AADEngine;
import com.quantosauros.batch.instrument.marketDataCreator.AbstractMarketDataCreator;
import com.quantosauros.batch.instrument.marketDataCreator.HullWhiteVolatilitySurfaceCreator;
import com.quantosauros.batch.instrument.marketDataCreator.IRCurveContainer;
import com.quantosauros.batch.instrument.modelCreator.ModelCreator;
import com.quantosauros.batch.instrument.variableCreator.VariableCreator;
import com.quantosauros.batch.model.ProductInfoModel;
import com.quantosauros.batch.model.ProductLegDataModel;
import com.quantosauros.batch.model.ProductLegModel;
import com.quantosauros.batch.model.ProductScheduleModel;
import com.quantosauros.batch.mybatis.SqlMapClient;
import com.quantosauros.batch.types.RiskFactorType;
import com.quantosauros.batch.types.RiskFactorType.RiskFactor;
import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.OptionInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.market.MarketInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;
import com.quantosauros.jpl.instrument.AbstractProduct;
import com.quantosauros.jpl.instrument.StructuredProduct;

public class AbstractCalculator {

	protected Date _asOfDate;
	protected String _instrumentCd;	
	protected AbstractProduct _instance;
	protected long _seed;	
	protected AADEngine _aadEngine;
	protected AADEngine _nonCallaadEngine;
	HashMap<RiskFactor, double[]> _aadGreeks;
	HashMap<RiskFactor, double[]> _noncallAADGreeks;	 
	protected HashMap _riskFactorMap;
	
	ProductInfo _productInfo;
	LegCouponInfo[] _legCouponInfos;
	LegScheduleInfo[] _legScheduleInfos;
	LegAmortizationInfo[] _legAmortizationInfos;
	LegDataInfo[] _legDataInfos;
	OptionInfo _optionInfo;	
		
	//market data
	RateMarketInfo _discountUnderlyingInfo;
	MarketInfo[][] _legMarketInfos;
	double[][] _quantoCorrelations;
	double[][] _quantoVolatilities;
	
	protected int _monitorFrequency;
	protected int _simNum;	
	
	public AbstractCalculator(String instrumentCd, Date asOfDate,
			HashMap riskFactorMap, int monitorFrequency, int simNum) {
		_asOfDate = asOfDate;		
		_instrumentCd = instrumentCd;
		_seed = VariableCreator.getRandomSeed(_instrumentCd, _asOfDate);
		_riskFactorMap = riskFactorMap;		
		_monitorFrequency = monitorFrequency;
		_simNum = simNum;
		generateConstructor();
		generateMarketDataMap();
	}
		
	public void reGenConstructor(){		
		_instance = new StructuredProduct(_asOfDate, 
				_productInfo, _legScheduleInfos, _legDataInfos, _legAmortizationInfos, 
				_legCouponInfos, _optionInfo,
				AbstractMarketDataCreator.getModelType(),
				_monitorFrequency, _seed, _simNum);
	}
	
	private void generateConstructor(){
		//Model
		ProductInfoModel productInfoModel = ModelCreator.getProductInfoModel(
				_instrumentCd);
		
		ProductLegModel[] productLegModels = new ProductLegModel[] {
				ModelCreator.getProductLegModel(_instrumentCd, "R"),
				ModelCreator.getProductLegModel(_instrumentCd, "P"),
		};
		
		ProductLegDataModel[] productLegDataModels = new ProductLegDataModel[]{
				ModelCreator.getProductLegDataModel(
						_instrumentCd, "R", _asOfDate.getDt()),
				ModelCreator.getProductLegDataModel(
						_instrumentCd, "P", _asOfDate.getDt()),		
		};
		List<ProductScheduleModel>[] productScheduleLists = new List[]{
				ModelCreator.getProductScheduleModel(
						_instrumentCd, "R"),
				ModelCreator.getProductScheduleModel(
						_instrumentCd, "P"),
		};
				
		List productOptionScheduleList = ModelCreator.getProductOptionScheduleModel(
				_instrumentCd);
		
		//SwapLegInfo		
		_productInfo = VariableCreator.getProductInfo(
				productInfoModel);
		_legCouponInfos = VariableCreator.getLegCouponInfos(
				productInfoModel, productLegModels, productScheduleLists);
		_legScheduleInfos = VariableCreator.getLegScheduleInfos(
				productLegModels, productScheduleLists);
		_legAmortizationInfos = VariableCreator.getLegAmortizationInfos(
				productLegModels);
		_legDataInfos = VariableCreator.getLegDataInfos(
				productLegModels, productLegDataModels);
		_optionInfo = VariableCreator.getOptionInfo(
				productInfoModel, productOptionScheduleList);			
		
		//Quanto Market Data
		double[][][] quantoMarketData = AbstractMarketDataCreator.getQuantoMarketData(
				productInfoModel, productLegModels);		
		_quantoCorrelations = quantoMarketData[0];				
		_quantoVolatilities = quantoMarketData[1];
				
		_instance = new StructuredProduct(_asOfDate, 
				_productInfo, _legScheduleInfos, _legDataInfos, _legAmortizationInfos, 
				_legCouponInfos, _optionInfo,
				AbstractMarketDataCreator.getModelType(),
				_monitorFrequency, _seed, _simNum);
	}
	
	private void generateMarketDataMap(){
		int legNum = _legCouponInfos.length;
		//Correlation 
		RiskFactor[][] corrIndices = new RiskFactor[legNum][];		
		RiskFactor[][] corrRiskFactors = new RiskFactor[][]{
			{RiskFactor.R_COUPON_IRC_CD1,RiskFactor.R_COUPON_IRC_CD2,RiskFactor.R_COUPON_IRC_CD3,},
			{RiskFactor.P_COUPON_IRC_CD1,RiskFactor.P_COUPON_IRC_CD2,RiskFactor.P_COUPON_IRC_CD3,},				
		};
		
		//HULLWHITE PARAMETERS		
		RiskFactor[][] hwRiskFactors = new RiskFactor[][]{
			{RiskFactor.R_HWPARAMS1,RiskFactor.R_HWPARAMS2,RiskFactor.R_HWPARAMS3,},
			{RiskFactor.P_HWPARAMS1,RiskFactor.P_HWPARAMS2,RiskFactor.P_HWPARAMS3,},						
		};		
		
		for (int legIndex = 0; legIndex < legNum; legIndex++){
			int undNum = _legCouponInfos[legIndex].getUnderlyingNumber();
			corrIndices[legIndex] = new RiskFactor[undNum];
			for (int undIndex = 0; undIndex < undNum; undIndex++){
				_riskFactorMap.put(hwRiskFactors[legIndex][undIndex], 
						AbstractMarketDataCreator.getHWParameters1());
				corrIndices[legIndex][undIndex] = corrRiskFactors[legIndex][undIndex];				
			}
		}
		_riskFactorMap.put(RiskFactorType.valueOf("DISC_HWPARAMS"), 
				AbstractMarketDataCreator.getHWParameters1());
		
		//CORRELATION
		_riskFactorMap.put(RiskFactorType.valueOf("CORRELATION"), 
				AbstractMarketDataCreator.getCorrelation(
						_instrumentCd, corrIndices));
		
	}
		
	public Money getPrice(Date asOfDate,
			IRCurveContainer irCurveContainer,	
			HullWhiteVolatilitySurfaceCreator hwSurfaceContainer,
			String flag){
		
		RiskFactor[][] irCurveRFs = new RiskFactor[][]{
				{RiskFactor.R_COUPON_IRC_CD1,RiskFactor.R_COUPON_IRC_CD2,RiskFactor.R_COUPON_IRC_CD3,},
				{RiskFactor.P_COUPON_IRC_CD1,RiskFactor.P_COUPON_IRC_CD2,RiskFactor.P_COUPON_IRC_CD3,},
		};
		RiskFactor[][] hwParamsRFs = new RiskFactor[][]{
				{RiskFactor.R_HWPARAMS1,RiskFactor.R_HWPARAMS2,RiskFactor.R_HWPARAMS3,},
				{RiskFactor.P_HWPARAMS1,RiskFactor.P_HWPARAMS2,RiskFactor.P_HWPARAMS3,},
		};
		RiskFactor[][] hwVolRFs = new RiskFactor[][]{
				{RiskFactor.R_COUPON_IRC_CD1_HWVOL, RiskFactor.R_COUPON_IRC_CD2_HWVOL, RiskFactor.R_COUPON_IRC_CD3_HWVOL,},
				{RiskFactor.P_COUPON_IRC_CD1_HWVOL, RiskFactor.P_COUPON_IRC_CD2_HWVOL, RiskFactor.P_COUPON_IRC_CD3_HWVOL,},
		};
		
		int legNum = _quantoCorrelations.length;
		_legMarketInfos = new MarketInfo[legNum][];
		
		for (int legIndex = 0; legIndex < legNum; legIndex++){
			int undNum = _quantoCorrelations[legIndex].length;
			_legMarketInfos[legIndex] = new MarketInfo[undNum];
			for (int undIndex = 0; undIndex < undNum; undIndex++){
				_legMarketInfos[legIndex][undIndex] = new RateMarketInfo(
						irCurveContainer.getIrCurve(_riskFactorMap.get(irCurveRFs[legIndex][undIndex]), flag),
						(HullWhiteParameters) _riskFactorMap.get(hwParamsRFs[legIndex][undIndex]), 
//						hwSurfaceContainer.getHWVolatility(_riskFactorMap.get(hwVolRFs[legIndex][undIndex])),
						hwSurfaceContainer.getHWVolSurface(_riskFactorMap.get(hwVolRFs[legIndex][undIndex])),
						_quantoCorrelations[legIndex][undIndex], 
						_quantoVolatilities[legIndex][undIndex]
				);
			}
		}
		
		_discountUnderlyingInfo = new RateMarketInfo(
				irCurveContainer.getIrCurve(_riskFactorMap.get(RiskFactor.DISC_IRC_CD), flag),
				(HullWhiteParameters) _riskFactorMap.get(RiskFactor.DISC_HWPARAMS),
//				hwSurfaceContainer.getHWVolatility(_riskFactorMap.get(RiskFactor.DISC_HWVOL))
				hwSurfaceContainer.getHWVolSurface(_riskFactorMap.get(RiskFactor.DISC_HWVOL))
		);
		
		double[][] correlations = (double[][]) _riskFactorMap.get(RiskFactor.CORRELATION);
				
		return _instance.getPrice(_legMarketInfos, _discountUnderlyingInfo, correlations);

	}
	
	public double[] getAADGreek(
			String ircCd,
			IRCurveContainer irCurveContainer,
			HashMap changedIrCurves,
			boolean isNonCall){	
		
		AADEngine aadEngine = null;
		
		if (!isNonCall){
			//Original Delta
			if (_aadEngine == null){
				int legNum = _legCouponInfos.length;
				int[] undNum = new int[legNum];
						
				double[][][] leverages = new double[legNum][][];
				double[][] legIrTenors = new double[legNum][];
				double[][] legIrMeanReversions = new double[legNum][];
				double[][][] legPayoffs = new double[legNum][][];
				double[][][] lowerLimits = new double[legNum][][];
				double[][][] upperLimits = new double[legNum][][];
				double[][][] coupon = new double[legNum][][];
				double[][][][][] refRates = new double[legNum][][][][];
				InterestRateCurve[][] legIrCurves = new InterestRateCurve[legNum][];
				
				for (int legIndex = 0; legIndex < legNum; legIndex++){
					undNum[legIndex] = _legCouponInfos[legIndex].getUnderlyingNumber();
					
					leverages[legIndex] = new double[undNum[legIndex]][];
					legIrTenors[legIndex] = new double[undNum[legIndex]];
					legIrMeanReversions[legIndex] = new double[undNum[legIndex]];
					refRates[legIndex] = new double[undNum[legIndex]][][][];
					legIrCurves[legIndex] = new InterestRateCurve[undNum[legIndex]];
					
					legPayoffs[legIndex] = _instance.getPayOffs(legIndex);
					lowerLimits[legIndex] = _instance.getLowerLimits(legIndex);
					upperLimits[legIndex] = _instance.getUpperLimits(legIndex);
					coupon[legIndex] = _instance.getCoupon(legIndex);
					
					for (int undIndex = 0; undIndex < undNum[legIndex]; undIndex++){
						leverages[legIndex][undIndex] = _instance.getLeverage(legIndex, undIndex);
						legIrTenors[legIndex][undIndex] = _instance.getReferenceTenor(legIndex, undIndex);
						legIrMeanReversions[legIndex][undIndex] = 
								((RateMarketInfo)_legMarketInfos[legIndex][undIndex]).
									getHullWhiteParameters().getMeanReversion1F();
										
//							_legHWParams[legIndex][undIndex].getMeanReversion1F();
						refRates[legIndex][undIndex] = _instance.getRefRates(legIndex, undIndex);
						legIrCurves[legIndex][undIndex] = 
								((RateMarketInfo)_legMarketInfos[legIndex][undIndex]).getInterestRateCurve();
					}
				}
				
				_aadEngine = new AADEngine(
						_instance.getPrincipal(0), _instance.getDCF(0), 
						_simNum, _instance.getPeriodNum(), _instance.getUnderlyingNum(),
						_instance.getDeferredCouponResetIndex(), 
						_instance.getMonitorFrequencies(), 
						_instance.getTenors(), 
						_instance.getHasExercise(), 
						_instance.getExerciseIndex(), 
						legIrCurves, 
						legIrTenors, legIrMeanReversions, 
						legPayoffs, 
						_discountUnderlyingInfo.getInterestRateCurve(),
						_discountUnderlyingInfo.getHullWhiteParameters().getMeanReversion1F(),
//						_discountIrCurve, _discountHWParams.getMeanReversion1F(), 
						_instance.getDiscounts(), 
						leverages, _instance.getRestrictionInfo(), 
						lowerLimits, upperLimits, coupon,
						refRates);
			}
			aadEngine = _aadEngine;
		} else {
			//Noncall Delta
			if (_nonCallaadEngine == null){
				int legNum = _legCouponInfos.length;
				int[] undNum = new int[legNum];
						
				double[][][] leverages = new double[legNum][][];
				double[][] legIrTenors = new double[legNum][];
				double[][] legIrMeanReversions = new double[legNum][];
				double[][][] legPayoffs = new double[legNum][][];
				double[][][] lowerLimits = new double[legNum][][];
				double[][][] upperLimits = new double[legNum][][];
				double[][][] coupon = new double[legNum][][];
				double[][][][][] refRates = new double[legNum][][][][];
				InterestRateCurve[][] legIrCurves = new InterestRateCurve[legNum][];
				
				for (int legIndex = 0; legIndex < legNum; legIndex++){
					undNum[legIndex] = _legCouponInfos[legIndex].getUnderlyingNumber();
					
					leverages[legIndex] = new double[undNum[legIndex]][];
					legIrTenors[legIndex] = new double[undNum[legIndex]];
					legIrMeanReversions[legIndex] = new double[undNum[legIndex]];
					refRates[legIndex] = new double[undNum[legIndex]][][][];
					legIrCurves[legIndex] = new InterestRateCurve[undNum[legIndex]];
					
					legPayoffs[legIndex] = _instance.getPayOffs(legIndex);
					lowerLimits[legIndex] = _instance.getLowerLimits(legIndex);
					upperLimits[legIndex] = _instance.getUpperLimits(legIndex);
					coupon[legIndex] = _instance.getCoupon(legIndex);
					
					for (int undIndex = 0; undIndex < undNum[legIndex]; undIndex++){
						leverages[legIndex][undIndex] = _instance.getLeverage(legIndex, undIndex);
						legIrTenors[legIndex][undIndex] = _instance.getReferenceTenor(legIndex, undIndex);
						legIrMeanReversions[legIndex][undIndex] = 
								((RateMarketInfo)_legMarketInfos[legIndex][undIndex]).
									getHullWhiteParameters().getMeanReversion1F();
						refRates[legIndex][undIndex] = _instance.getRefRates(legIndex, undIndex);
						legIrCurves[legIndex][undIndex] = 
								((RateMarketInfo)_legMarketInfos[legIndex][undIndex]).getInterestRateCurve();
						
					}
				}
				
				_nonCallaadEngine = new AADEngine(
						_instance.getPrincipal(0), _instance.getDCF(0), 
						_simNum, _instance.getPeriodNum(), _instance.getUnderlyingNum(),
						_instance.getDeferredCouponResetIndex(), 
						_instance.getMonitorFrequencies(), 
						_instance.getTenors(), 
						_instance.getHasExercise(), 
						_instance.getExerciseIndex(), 
						legIrCurves, 
						legIrTenors, legIrMeanReversions, 
						legPayoffs, 
						_discountUnderlyingInfo.getInterestRateCurve(),
						_discountUnderlyingInfo.getHullWhiteParameters().getMeanReversion1F(),
//						_discountIrCurve, _discountHWParams.getMeanReversion1F(), 
						_instance.getDiscounts(), 
						leverages, _instance.getRestrictionInfo(), 
						lowerLimits, upperLimits, coupon,
						refRates);
			}
			aadEngine = _nonCallaadEngine;
		}
		
		int vertexLength = irCurveContainer.getVertexLength(ircCd);		
		double[] aadGreek = new double[vertexLength];
		
		PayRcv payRcv = _legCouponInfos[0].getPayRcv();
		int payIndex = 0;
		int rcvIndex = 0;
		if (payRcv.equals(PayRcv.PAY)){
			payIndex = 0;
			rcvIndex = 1;
		} else {
			payIndex = 1;
			rcvIndex = 0;
		}
		Iterator itr = _riskFactorMap.keySet().iterator();
		while (itr.hasNext()){
			Object key = itr.next();
			if (key instanceof RiskFactor && _riskFactorMap.get(key) instanceof String){
				String aadIrcCd = (String) _riskFactorMap.get(key);
				if (ircCd.equals(aadIrcCd)){	
					double[] greek = null;
					if (key.equals(RiskFactor.P_COUPON_IRC_CD1)){					
						PayRcv payRcvCd = _legCouponInfos[payIndex].getPayRcv();
						CouponType[] couponType = _legCouponInfos[payIndex].getCouponType();
						ConditionType conditionType = _legCouponInfos[payIndex].getConditionType();
						int underlyingIndex = 0;
						greek = aadEngine.getDelta(payRcvCd, couponType, conditionType, 
								changedIrCurves, payIndex, underlyingIndex);	
						
					} else if (key.equals(RiskFactor.P_COUPON_IRC_CD2)){
						PayRcv payRcvCd = _legCouponInfos[payIndex].getPayRcv();
						CouponType[] couponType = _legCouponInfos[payIndex].getCouponType();
						ConditionType conditionType = _legCouponInfos[payIndex].getConditionType();
						int underlyingIndex = 1;
						greek = aadEngine.getDelta(payRcvCd, couponType, conditionType, 
								changedIrCurves, payIndex, underlyingIndex);
						
					} else if (key.equals(RiskFactor.P_COUPON_IRC_CD3)){
						PayRcv payRcvCd = _legCouponInfos[payIndex].getPayRcv();
						CouponType[] couponType = _legCouponInfos[payIndex].getCouponType();
						ConditionType conditionType = _legCouponInfos[payIndex].getConditionType();
						int underlyingIndex = 2;
						greek = aadEngine.getDelta(payRcvCd, couponType, conditionType, 
								changedIrCurves, payIndex, underlyingIndex);
						
					} else if (key.equals(RiskFactor.R_COUPON_IRC_CD1)){
						PayRcv payRcvCd = _legCouponInfos[rcvIndex].getPayRcv();
						CouponType[] couponType = _legCouponInfos[rcvIndex].getCouponType();
						ConditionType conditionType = _legCouponInfos[rcvIndex].getConditionType();
						int underlyingIndex = 0;
						greek = aadEngine.getDelta(payRcvCd, couponType, conditionType,
								changedIrCurves, rcvIndex, underlyingIndex);
						
					} else if (key.equals(RiskFactor.R_COUPON_IRC_CD2)){
						PayRcv payRcvCd = _legCouponInfos[rcvIndex].getPayRcv();
						CouponType[] couponType = _legCouponInfos[rcvIndex].getCouponType();
						ConditionType conditionType = _legCouponInfos[rcvIndex].getConditionType();
						int underlyingIndex = 1;
						greek = aadEngine.getDelta(payRcvCd, couponType, conditionType, 
								changedIrCurves, rcvIndex, underlyingIndex);
						
					} else if (key.equals(RiskFactor.R_COUPON_IRC_CD3)){
						PayRcv payRcvCd = _legCouponInfos[rcvIndex].getPayRcv();
						CouponType[] couponType = _legCouponInfos[rcvIndex].getCouponType();
						ConditionType conditionType = _legCouponInfos[rcvIndex].getConditionType();
						int underlyingIndex = 2;
						greek = aadEngine.getDelta(payRcvCd, couponType, conditionType, 
								changedIrCurves, rcvIndex, underlyingIndex);
						
					} else if (key.equals(RiskFactor.DISC_IRC_CD)){					
						greek = aadEngine.getDiscountDelta(changedIrCurves);
					}
							
					if (greek != null){
						for (int i = 0; i < greek.length; i++){
							aadGreek[i] += greek[i];
							
							System.out.println(
									"IRCCD: " + aadIrcCd +
									" key: " + key.toString() +
									" VALUE: " + greek[i]);				
						}
					}					
				}
			}
		}
		
		
		for (int i = 0; i < aadGreek.length; i++){
			System.out.println(
					"ISNONCALL: " + isNonCall + 
					": IRCCD: " + ircCd +  
					": AAD: " + aadGreek[i]);		
		}
		
		return aadGreek;
	}
	
	protected AADEngine calcAADGreeks(String ircCd,
			IRCurveContainer irCurveContainer,
			int simNum,
			double epsilon){return null;};
	
	public Collection getRiskFactorCdMap(){
		return _riskFactorMap.values();
	}	
	public Money getLegPrice(int legIndex){
		return _instance.getLegPrice(legIndex);
	}	
	public void setHasExercise(boolean hasExercise){
		_instance.setHasExercise(hasExercise);
	}
	public boolean getHasExercise(){
		return _instance.getHasExercise();
	}	
	public int[] getExerciseIndex(){
		return _instance.getExerciseIndex();
	}
	public void setExerciseIndex(int[] exerciseIndex){
		_instance.setExerciseIndex(exerciseIndex);
	}
	public int getPeriodNum(){
		return _instance.getPeriodNum();
	}	
	public Map getResults(){
		return _instance.getResults();		
	}
	public int getPayIndex(){
		return _instance.getPayIndex();
	}
	public int getRcvIndex(){
		return _instance.getRcvIndex();
	}
	public ProductInfo getProductInfo(){
		return _productInfo;				
	}
	public void setProductInfo(ProductInfo productInfo){
		this._productInfo = productInfo;
	}
	public LegCouponInfo[] getLegCouponInfo(){
		return _legCouponInfos;
	}
	public void setLegCouponInfo(LegCouponInfo[] legCouponInfos){
		this._legCouponInfos = legCouponInfos;
	}
	public LegScheduleInfo[] getLegScheduleInfo(){
		return _legScheduleInfos;
	}
	public void setLegScheduleInfos(LegScheduleInfo[] legScheduleInfos){
		this._legScheduleInfos = legScheduleInfos;
	}
	public LegAmortizationInfo[] getLegAmortizationInfo(){
		return _legAmortizationInfos;
	}
	public void setLegAmortizationInfos(LegAmortizationInfo[] legAmortizationInfos){
		this._legAmortizationInfos = legAmortizationInfos;
	}
	public LegDataInfo[] getLegDataInfo(){
		return _legDataInfos;
	}
	public void setLegDataInfos(LegDataInfo[] legDataInfos){
		this._legDataInfos = legDataInfos;
	}
	public OptionInfo getOptionInfo(){
		return _optionInfo;
	}
	public void setOptionInfo(OptionInfo optionInfo){
		this._optionInfo = optionInfo;
	}
	public void setAsOfDate(Date asOfDate){
		_asOfDate = asOfDate;
		_seed = VariableCreator.getRandomSeed(_instrumentCd, _asOfDate);
	}
}
