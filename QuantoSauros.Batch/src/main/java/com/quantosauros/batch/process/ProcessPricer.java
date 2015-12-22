package com.quantosauros.batch.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.quantosauros.batch.instrument.DeltaResult;
import com.quantosauros.batch.instrument.calculator.AbstractCalculator;
import com.quantosauros.batch.instrument.marketDataCreator.HullWhiteVolatilitySurfaceCreator;
import com.quantosauros.batch.instrument.marketDataCreator.IRCurveContainer;
import com.quantosauros.batch.mybatis.SqlMapClient;
import com.quantosauros.batch.types.CcyType;
import com.quantosauros.batch.types.RiskFactorType;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.OptionInfo;
import com.quantosauros.jpl.dto.ProductInfo;

public class ProcessPricer {

	protected AbstractCalculator _calculator;
	protected SqlSession _session;
	protected IRCurveContainer _irCurveContainer;	
	protected HullWhiteVolatilitySurfaceCreator _surfaceContainer;
	protected double _epsilon = 0.0001;
	protected Date _processDate;
	
	public ProcessPricer(Date processDate, String instrumentCd, 
			int monitorFrequency, int simNum) {
				
		_session = SqlMapClient.getSqlSession();
		_processDate = processDate;
		_irCurveContainer = new IRCurveContainer(processDate, _epsilon);		
		_surfaceContainer = new HullWhiteVolatilitySurfaceCreator(processDate);
		
		Map paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		List ircCdList = _session.selectList("MarketData.getIrcCd", paramMap);
		HashMap riskFactorMap = genCurveContainer(ircCdList);		
		
		_calculator = new AbstractCalculator(instrumentCd, 
				_processDate, riskFactorMap, monitorFrequency, simNum);
		
	}
	
	public void execute(){
		//Price
		Money price = _calculator.getPrice(
				_processDate, _irCurveContainer, _surfaceContainer, "");
	
		if (_calculator.getHasExercise()){
			_calculator.setHasExercise(false);
			Money nonExercisePrices = _calculator.getPrice(
					_processDate, _irCurveContainer, _surfaceContainer, "");			
		}
		
		//Delta
		Iterator itr = new ArrayList(
				new HashSet(_calculator.getRiskFactorCdMap())).iterator();		
		//CALCULATE DELTA
		while (itr.hasNext()){
			Object itrNext = itr.next();
			if(itrNext.getClass() == String.class){
				String ircCd = (String) itrNext;
				Vertex[] vertex = _irCurveContainer.getVertex(ircCd);
				//AAD
				HashMap changedIRCurves = _irCurveContainer.getScenarioCurves(ircCd);
				
				double[] AADGreeks = _calculator.getAADGreek(
						ircCd, _irCurveContainer, changedIRCurves, false);								
								
				if (_calculator.getHasExercise()){					
					_calculator.setHasExercise(false);
					//AAD
					price = _calculator.getPrice(_processDate, 
							_irCurveContainer, _surfaceContainer, "");
					
					changedIRCurves = _irCurveContainer.getScenarioCurves(ircCd);
					
					double[] nonCallAADResult = _calculator.getAADGreek(
							ircCd, _irCurveContainer, changedIRCurves, true);					
				}
			}
		}		
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
					
					Map paramMap = new HashMap(); 
					paramMap.put("ircCd", ircCd);
					String ccyCd = _session.selectOne(
							"MarketData.getCcyCodeFromIrcCd", paramMap);					
					
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
	public LegCouponInfo[] getLegCouponInfo(){
		return _calculator.getLegCouponInfo();
	}
	public void setLegCouponInfo(LegCouponInfo[] legCouponInfos){
		this._calculator.setLegCouponInfo(legCouponInfos);
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
	public OptionInfo getOptionInfo(){
		return _calculator.getOptionInfo();
	}
	public void setOptionInfo(OptionInfo optionInfo){
		this._calculator.setOptionInfo(optionInfo);
	}
	public void setProcessDate(Date processDate){
		this._processDate = processDate;
	}
}
