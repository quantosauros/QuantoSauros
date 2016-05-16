package com.quantosauros.batch.instrument.marketDataCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.quantosauros.batch.dao.MarketDataDao;
import com.quantosauros.batch.dao.MySqlMarketDataDao;
import com.quantosauros.batch.model.IrCurveModel;
import com.quantosauros.common.calibration.BootStrapper;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.interestrate.AbstractRate;
import com.quantosauros.common.interestrate.AbstractRateCurve;
import com.quantosauros.common.interestrate.ZeroRate;
import com.quantosauros.common.interestrate.ZeroRateCurve;
import com.quantosauros.common.math.interpolation.Interpolation;
import com.quantosauros.common.math.interpolation.LinearInterpolation;

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
					
					result.put(str[2], new AbstractRateCurve[]{
						(AbstractRateCurve) _irCurveMap.get(codeUp),
						(AbstractRateCurve) _irCurveMap.get(codeDown)		
					});
					
				}
			}
		}	
		return result;
	}
	
	public void storeIrCurve(String ircCd){
		Interpolation interpolation = LinearInterpolation.getInstance();
		
		if (!_irCurveMap.containsKey(ircCd)){			
			HashMap paramMap = new HashMap();  	
			paramMap.put("dt", _processDate.getDt());
			paramMap.put("ircCd", ircCd);
			MarketDataDao marketDataDao = new MySqlMarketDataDao();
			List<IrCurveModel> irCurveDaoList = 
					marketDataDao.selectIrCurveModel(paramMap);
			
			AbstractRateCurve ytmCurve = AbstractMarketDataCreator.getIrCurve(
					_processDate, irCurveDaoList);
			
			//original
			_irCurveMap.put(ircCd, 
					new BootStrapper(ytmCurve, interpolation).calculate());
						
			//updown
			if (_calcScenario){
				ArrayList<AbstractRate> spotRates = ytmCurve.getRates();
				for (int sensiIndex = 0; sensiIndex < spotRates.size(); sensiIndex++){
					Vertex vertex = spotRates.get(sensiIndex).getVertex();
					//Up Curve
					AbstractRateCurve upCurve = ytmCurve.copy(sensiIndex, _epsilon);
					String upIrcCd = ircCd + "_UP" + "_" + vertex.getCode();
					_irCurveMap.put(upIrcCd,
							new BootStrapper(upCurve, interpolation).calculate());
					
					//DOWN Curve
					AbstractRateCurve downCurve =ytmCurve.copy(sensiIndex, -_epsilon);
					String downIrcCd = ircCd + "_DOWN" + "_" + vertex.getCode();
					_irCurveMap.put(downIrcCd,
							new BootStrapper(downCurve, interpolation).calculate());
				}				
			}
		}		
	}

	public ZeroRateCurve getIrCurve(String ircCd){
		Object irCurve = _irCurveMap.get(ircCd);
		if (irCurve != null){
			return (ZeroRateCurve) irCurve;
		} else {
			return null;
		}		
	}	
	
	public ZeroRateCurve getIrCurve(Object ircCd){		
		return getIrCurve((String) ircCd);
	}
	
	public ZeroRateCurve getIrCurve(Object ircCd, String flag){		
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
		ArrayList<AbstractRate> irs = ((AbstractRateCurve)_irCurveMap.get(ircCd)).getRates();
		return irs.size();
	}
	
	public Vertex[] getVertex(String ircCd){
		AbstractRateCurve irCurve = (AbstractRateCurve)_irCurveMap.get(ircCd);		
		ArrayList<AbstractRate> irs = irCurve.getRates();
		Vertex[] vertex = new Vertex[irs.size()];
		for (int i =0 ; i < vertex.length; i++){
			vertex[i] = irs.get(i).getVertex();
		}
		return vertex;		
	}
	public String[] getVertexStr(String ircCd){
		AbstractRateCurve irCurve = (AbstractRateCurve)_irCurveMap.get(ircCd);		
		ArrayList<AbstractRate> irs = irCurve.getRates();
		String[] vertex = new String[irs.size()];
		for (int i =0 ; i < vertex.length; i++){
			vertex[i] = irs.get(i).getVertex().toString();
		}
		return vertex;	
	}
}
