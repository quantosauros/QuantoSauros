package com.quantosauros.batch.instrument.marketDataCreator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.quantosauros.batch.mybatis.SqlMapClient;
import com.quantosauros.common.Frequency;
import com.quantosauros.common.calibration.SpotCurveCalculator;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.interestrate.InterestRate;
import com.quantosauros.common.interestrate.InterestRateCurve;

public class IRCurveContainer {

	private HashMap _irCurveMap; 
	private Date _processDate;
	private double _epsilon;
	private boolean _calcScenario;
	
	public IRCurveContainer(Date processDate, double epsilon) {
		_irCurveMap = new HashMap();
		_processDate = processDate;
		_epsilon = epsilon;
		_calcScenario = true;
		if (_epsilon == 0){
			_calcScenario = false;
		}
	}
	
	public HashMap getScenarioCurves(String ircCd){

		HashMap result = new HashMap();
			
		Iterator keys = _irCurveMap.keySet().iterator();
		
		while (keys.hasNext()){
			String key = (String) keys.next();
			if (key.contains(ircCd)){
				if (key.contains("UP")){
					String[] str = key.split("_");
					String codeUp = str[0] + "_UP_" + str[2];
					String codeDown = str[0] + "_DOWN_" + str[2];
					
					result.put(str[2], new InterestRateCurve[]{
						(InterestRateCurve) _irCurveMap.get(codeUp),
						(InterestRateCurve) _irCurveMap.get(codeDown)		
					});
					
				}
			}
		}	
		return result;
	}
	
	public void storeIrCurve(String ircCd){
		if (!_irCurveMap.containsKey(ircCd)){
			SqlSession session = SqlMapClient.getSqlSession();
			Map paramMap = new HashMap();  	
			paramMap.put("dt", _processDate.getDt());
			paramMap.put("ircCd", ircCd);
			List irCurveDaoList = session.selectList(
	    			"MarketData.getIrCurveFromIrcCd", paramMap);
			
			InterestRateCurve ytmCurve = AbstractMarketDataCreator.getIrCurve(
					_processDate, irCurveDaoList);
			
			//original
			_irCurveMap.put(ircCd, SpotCurveCalculator.calculate(ytmCurve));
						
			//updown
			if (_calcScenario){
				InterestRate[] spotRates = ytmCurve.getSpotRates();
				for (int sensiIndex = 0; sensiIndex < spotRates.length; sensiIndex++){
					Vertex vertex = spotRates[sensiIndex].getVertex();
					
					//Up Curve
					InterestRate[] upRates = (InterestRate[]) spotRates.clone();
					upRates[sensiIndex] = upRates[sensiIndex].plusRate(_epsilon);
					InterestRateCurve upIrCurve = new InterestRateCurve
							(_processDate, upRates, Frequency.valueOf("C"), DayCountFraction.ACTUAL_365);
					String upIrcCd = ircCd + "_UP" + "_" + vertex.getCode();
					_irCurveMap.put(upIrcCd, SpotCurveCalculator.calculate(upIrCurve));
					
					//Down Curve
					InterestRate[] downRates = (InterestRate[]) spotRates.clone();
					downRates[sensiIndex] = downRates[sensiIndex].plusRate(-_epsilon);				
					InterestRateCurve downIrCurve = new InterestRateCurve
							(_processDate, downRates, Frequency.valueOf("C"), DayCountFraction.ACTUAL_365);
					String downIrcCd = ircCd + "_DOWN" + "_" + vertex.getCode();
					_irCurveMap.put(downIrcCd, SpotCurveCalculator.calculate(downIrCurve));				
				}				
			}
		}		
	}

	public InterestRateCurve getIrCurve(String ircCd){
		Object irCurve = _irCurveMap.get(ircCd);
		if (irCurve != null){
			return (InterestRateCurve) irCurve;
		} else {
			return null;
		}		
	}	
	
	public InterestRateCurve getIrCurve(Object ircCd){		
		return getIrCurve((String) ircCd);
	}
	
	public InterestRateCurve getIrCurve(Object ircCd, String flag){		
		if (ircCd == null){
			return null;
		} else {
			String[] codes = flag.split("_");
			if (ircCd.equals(codes[0])){			
				return getIrCurve(flag);
			} else {
				return getIrCurve((String) ircCd);
			}
		}				
	}	
	
	public int getVertexLength(String ircCd){
		InterestRate[] irs = ((InterestRateCurve)_irCurveMap.get(ircCd)).getSpotRates();
		return irs.length;
	}
	
	public Vertex[] getVertex(String ircCd){
		InterestRateCurve irCurve = (InterestRateCurve)_irCurveMap.get(ircCd);		
		InterestRate[] irs = irCurve.getSpotRates();
		Vertex[] vertex = new Vertex[irs.length];
		for (int i =0 ; i < vertex.length; i++){
			vertex[i] = irs[i].getVertex();
		}
		return vertex;		
	}
}
