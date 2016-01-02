package com.quantosauros.batch.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.quantosauros.batch.dao.MarketDataDao;
import com.quantosauros.batch.dao.MySqlMarketDataDao;
import com.quantosauros.batch.instrument.calculator.AbstractCalculator;
import com.quantosauros.batch.instrument.marketDataCreator.HullWhiteVolatilitySurfaceCreator;
import com.quantosauros.batch.instrument.marketDataCreator.IRCurveContainer;
import com.quantosauros.batch.types.CcyType;
import com.quantosauros.batch.types.RiskFactorType;
import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.OptionType;
import com.quantosauros.common.TypeDef.PayRcv;
import com.quantosauros.common.TypeDef.UnderlyingType;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.OptionInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;

public class ProcessPricer {

	protected AbstractCalculator _calculator;	
	protected IRCurveContainer _irCurveContainer;	
	protected HullWhiteVolatilitySurfaceCreator _surfaceContainer;
	protected double _epsilon = 0.0001;
	protected Date _processDate;
	protected MarketDataDao _marketDataDao = new MySqlMarketDataDao();
	
	public ProcessPricer(Date processDate, String instrumentCd, 
			int monitorFrequency, int simNum) {
				
		_processDate = processDate;
		_irCurveContainer = new IRCurveContainer(processDate, _epsilon);		
		_surfaceContainer = new HullWhiteVolatilitySurfaceCreator(processDate);
		
		HashMap paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);		
		List ircCdList = _marketDataDao.selectIRCCode(paramMap);
		HashMap riskFactorMap = genCurveContainer(ircCdList);		
		
		_calculator = new AbstractCalculator(instrumentCd, 
				_processDate, riskFactorMap, monitorFrequency, simNum);
		
	}
	
	public Money getPrice(){
		//Price
		return _calculator.getPrice(_processDate,
				_irCurveContainer, _surfaceContainer, "");		 
	}
	
	public Money getLegPrice(int legIndex){
		return _calculator.getLegPrice(legIndex);
	}
	
	public void execute(){

		//Price
		Money price = _calculator.getPrice(
				_processDate, _irCurveContainer, _surfaceContainer, "");
				
		
		//Delta
//		Iterator itr = new ArrayList(
//				new HashSet(_calculator.getRiskFactorCdMap())).iterator();		
//		//CALCULATE DELTA
//		while (itr.hasNext()){
//			Object itrNext = itr.next();
//			if(itrNext.getClass() == String.class){
//				String ircCd = (String) itrNext;
//								
//				//Price
//				Money price = _calculator.getPrice(
//						_processDate, _irCurveContainer, _surfaceContainer, "");
//				System.out.println(price);				
//				
//				//AAD
//				HashMap changedIRCurves = _irCurveContainer.getScenarioCurves(ircCd);
//				
//				double[] AADGreeks = _calculator.getAADGreek(
//						ircCd, _irCurveContainer, changedIRCurves, false);								
//				
//				for (int i = 0; i < AADGreeks.length; i++){
//					System.out.println(AADGreeks[i]);
//				}
//				
//				if (_calculator.getHasExercise()){					
//					_calculator.setHasExercise(false);
//					//Price
//					Money nonExercisePrices = _calculator.getPrice(
//							_processDate, _irCurveContainer, _surfaceContainer, "");					
//					System.out.println(nonExercisePrices);
//					
//					//AAD					
//					changedIRCurves = _irCurveContainer.getScenarioCurves(ircCd);
//					
//					double[] nonCallAADResult = _calculator.getAADGreek(
//							ircCd, _irCurveContainer, changedIRCurves, true);
//					_calculator.setHasExercise(true);
//					
//					for (int i = 0; i < nonCallAADResult.length; i++){
//						System.out.println(nonCallAADResult[i]);
//					}
//				}
//			}
//		}		
	}
	
	private HashMap genCurveContainer(List ircCdList){
		HashMap tmpMap = new HashMap();	
				
		for (int payRcvIndex = 0; payRcvIndex < ircCdList.size(); payRcvIndex++){
			Map ircCdMap = (Map) ircCdList.get(payRcvIndex);
			String payRcvCd = (String) ircCdMap.get("PAY_RCV_CD");
			String legTypeCd = (String) ircCdMap.get("LEG_TYPE_CD");
			
			Iterator keys = ircCdMap.keySet().iterator();
			while (keys.hasNext()){
				String key = (String) keys.next();
				if (key.equals("PAY_RCV_CD") || key.equals("LEG_TYPE_CD")){
					
				} else {
					String ircCd = (String) ircCdMap.get(key);
					
					HashMap paramMap = new HashMap(); 
					paramMap.put("ircCd", ircCd);
					String ccyCd = _marketDataDao.selectCcyCodeFromIrcCd(paramMap);

					_irCurveContainer.storeIrCurve(ircCd);					
					
					String irRiskFactorCd = payRcvCd + "_" + key;
					String volSurfRiskFactorCd = "";
					
					if (key.contains("LEG_IRC_CD")){						
						
					} else if (key.equals("DISC_IRC_CD")){
						irRiskFactorCd = key;
						volSurfRiskFactorCd = "DISC_HWVOL";
						_surfaceContainer.genSurface(ccyCd);
						tmpMap.put(RiskFactorType.valueOf(
								volSurfRiskFactorCd), CcyType.valueof(ccyCd));
					} else {
						volSurfRiskFactorCd = payRcvCd + "_" + key + "_HWVOL";
						_surfaceContainer.genSurface(ccyCd);
						tmpMap.put(RiskFactorType.valueOf(
								volSurfRiskFactorCd), CcyType.valueof(ccyCd));
					}					
					//IR Curve
					tmpMap.put(RiskFactorType.valueOf(irRiskFactorCd), ircCd);
				}				
			}
		}
		return tmpMap;
	}
		
	public ProductInfo getProductInfo(){
		return _calculator.getProductInfo();				
	}
	public void setProductInfo(ProductInfo productInfo){
		this._calculator.setProductInfo(productInfo);
	}
	public int getLegNumber(){
		return _calculator.getLegCouponInfo().length;
	}
	public int getUnderlyingNum(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getUnderlyingNumber();
	}
	public int getConditionNum(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getConditionNumber();
	}
	public CouponType[] getCouponTypes(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getCouponType();
	}
	public UnderlyingType getUnderlyingType(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getUnderlyingType();
	}
	public ConditionType getConditionType(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getConditionType();
	}
	public PayRcv getPayRcv(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getPayRcv();			
	}
	public boolean hasLeverage(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].hasLeverage();
	}
	public double[] getLeverages(int legIndex, int underlyingIndex){
		return _calculator.getLegCouponInfo()[legIndex].getLeverages(underlyingIndex);
	}
	public boolean hasCap(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].hasCap();
	}
	public double[] getCap(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getCap();
	}
	public boolean hasFloor(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].hasFloor();
	}
	public double[] getFloor(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getFloor();
	}
	public boolean hasInOutCouponRates(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].hasInOutCouponRates();
	}
	public double[] getInCouponRates(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getInCouponRates();
	}
	public double[] getOutCouponRates(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getOutCouponRates();
	}
	public double[][] getUpperLimits(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getUpperLimits();
	}
	public double[][] getLowerLimits(int legIndex){
		return _calculator.getLegCouponInfo()[legIndex].getLowerLimits();
	}
	public UnderlyingInfo getUnderlyingInfo(int legIndex, int undIndex){
		return _calculator.getLegCouponInfo()[legIndex].getUnderlyingInfo(undIndex);
	}
	public LegScheduleInfo[] getLegScheduleInfo(){
		return _calculator.getLegScheduleInfo();
	}
	public void setLegScheduleInfos(LegScheduleInfo[] legScheduleInfos){
		this._calculator.setLegScheduleInfos(legScheduleInfos);
	}
	public LegAmortizationInfo[] getLegAmortizationInfo(){
		return _calculator.getLegAmortizationInfo();
	}
	public void setLegAmortizationInfos(LegAmortizationInfo[] legAmortizationInfos){
		this._calculator.setLegAmortizationInfos(legAmortizationInfos);
	}
	public LegDataInfo[] getLegDataInfo(){
		return _calculator.getLegDataInfo();
	}
	public void setLegDataInfos(LegDataInfo[] legDataInfos){
		this._calculator.setLegDataInfos(legDataInfos);
	}

	public OptionType getOptionType(){
		return _calculator.getOptionInfo().getOptionType();
	}
	public void setOptionType(OptionType optionType){
		OptionInfo optionInfo = _calculator.getOptionInfo();
		optionInfo.setOptionType(optionType);
		_calculator.setOptionInfo(optionInfo);		
	}
	public void setProcessDate(Date processDate){
		this._processDate = processDate;
	}
}
