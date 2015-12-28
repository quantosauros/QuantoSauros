package com.quantosauros.batch.process;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import com.quantosauros.batch.instrument.DeltaResult;
import com.quantosauros.batch.model.InstrumentInfoModel;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.Vertex;

public class ProcessGreeks extends AbstractProcess {

	private boolean _calcBump = false;
	
	public ProcessGreeks(Date processDate, String procId, String idx) {
		super(processDate, procId, idx);
		//Insert DeltaGamma Info
		HashMap paramMap = new HashMap();
		paramMap.put("proc_id", _procId);
		paramMap.put("idx_id", _idx);
		String description = "default";
		paramMap.put("description", description);
		_procPriceDataDao.insertDeltaGammaInfo(paramMap);		
		
		//get a Market Data ID from proc Id
		paramMap = new HashMap();
		paramMap.put("procId", _procId);
		paramMap.put("idxId", _idx);
		_marketDataId = _procPriceDataDao.selectDataIdFromDeltaGammaByProcId(paramMap);
	}
	
	protected void calcInstrument(InstrumentInfoModel instrumentInfoModel) {	
					
		Iterator itr = new ArrayList(
				new HashSet(_calculator.getRiskFactorCdMap())).iterator();		
		//CALCULATE DELTA
		while (itr.hasNext()){
			Object itrNext = itr.next();
			if(itrNext.getClass() == String.class){				
				//CALCULATE PRICE
				Money price = _calculator.getPrice(
						_processDate, _irCurveContainer, _surfaceContainer, "");
				
				System.out.println("Price: " + price);
				System.out.println("Rcv Leg: " + _calculator.getLegPrice(0));
				System.out.println("Pay Leg: " + _calculator.getLegPrice(1));
				
				String ircCd = (String) itrNext;
				Vertex[] vertex = _irCurveContainer.getVertex(ircCd);
				//AAD
				HashMap changedIRCurves = _irCurveContainer.getScenarioCurves(ircCd);
				
				double[] AADGreeks = _calculator.getAADGreek(
						ircCd, _irCurveContainer, changedIRCurves, false);
				
				insertGreekResult(instrumentInfoModel, ircCd, "AAD", "N", vertex, AADGreeks);

				//BUMPING		
				if (_calcBump){
					double[] BumpingGreeks = calcDelta(instrumentInfoModel.getInstrumentCd(), ircCd);
					insertGreekResult(instrumentInfoModel, ircCd, "BUMP", "N", vertex, BumpingGreeks);					
				}
								
				if (_calculator.getHasExercise()){					
					_calculator.setHasExercise(false);
					//AAD
					price = _calculator.getPrice(_processDate, 
							_irCurveContainer, _surfaceContainer, "");
					System.out.println("===NONCALL PRICE===");
					System.out.println("Price: " + price);
					System.out.println("Rcv Leg: " + _calculator.getLegPrice(0));
					System.out.println("Pay Leg: " + _calculator.getLegPrice(1));
					
					changedIRCurves = _irCurveContainer.getScenarioCurves(ircCd);
										
					double[] nonCallAADResult = _calculator.getAADGreek(
							ircCd, _irCurveContainer, changedIRCurves, true);
					insertGreekResult(instrumentInfoModel, ircCd, "AAD", "Y", vertex, nonCallAADResult);					
					_calculator.setHasExercise(true);
					
//					BUMPING		
					if (_calcBump){
						double[] nonCallBumpingResult = calcDelta(instrumentInfoModel.getInstrumentCd(), ircCd);
						insertGreekResult(instrumentInfoModel, ircCd, "BUMP", "Y", vertex, nonCallBumpingResult);						
						_calculator.setHasExercise(true);
					}					
				}				
			}
		}		
	}	
	
	private double[] calcDelta(String instrumentCd, String ircCd){
		Vertex[] vertices = _irCurveContainer.getVertex(ircCd);
		int sensiNum = vertices.length;		
		boolean hasExercise = _calculator.getHasExercise();
		double[] greeks = new double[sensiNum];
		
		for (int sensiIndex = 0; sensiIndex < sensiNum; sensiIndex++){			
			Vertex vertex = vertices[sensiIndex];
			
			//Up Price
			String upFlag = ircCd + "_UP_" + vertex.getCode();
//			_calculator.setExerciseIndex(null);
			Money upPrice = _calculator.getPrice(_processDate, 
					_irCurveContainer, _surfaceContainer, upFlag);						
//			_calculator.getResults();
			
			//Down Price
			String downFlag = ircCd + "_DOWN_" + vertex.getCode();
//			_calculator.setExerciseIndex(null);
			Money downPrice = _calculator.getPrice(_processDate, 
					_irCurveContainer, _surfaceContainer, downFlag);
//			_calculator.getResults();
			
			//Calculate Delta 
			Money delta = upPrice.subtract(downPrice);
			greeks[sensiIndex] = delta.getAmount();
			
//			System.out.println(
//					"INSTRUMENTCD: " + instrumentCd
//					+ " : IRCCD: " + ircCd 
//					+ " : EXERCISE: " + hasExercise
//					+ " : VERTEX: " + vertex
//					+ " : DELTA : "  + delta.getAmount()
//					+ " : upPrice : " + upPrice.getAmount()
//					+ " : downPrice : " + downPrice.getAmount());
			
		}		
		
		return greeks;
	}
	
	protected void insertGreekResult(InstrumentInfoModel instrumentInfoModel,
			String ircCd, String greekCd, String nonCallCd, 
			Vertex[] vertexList, double[] greeks){		    
		
		System.out.println(
				"Greek Results of Product Code; " + instrumentInfoModel.getInstrumentCd() + 
				"; at; " + _processDate.getDt() + 
				";(IRCCD: " + ircCd + 
				";GREEKCD: " + greekCd + 
				";NONCALLCD: " + nonCallCd +  
				";)"				
		);
		for (int vtxIndex = 0; vtxIndex < vertexList.length; vtxIndex++){
			Vertex vtx = vertexList[vtxIndex];
			String factorCd = ircCd + vtx.getCode();	
			DecimalFormat dformat = new DecimalFormat(".####");					
			String greek = dformat.format(greeks[vtxIndex]);
			
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("greek", greek);			
			paramMap.put("greekCd", greekCd);				
			paramMap.put("factorCd", factorCd);
			paramMap.put("nonCallCd", nonCallCd);
			paramMap.put("dt", _processDate.getDt());
			paramMap.put("procId", _procId);
			paramMap.put("instrumentCd", instrumentInfoModel.getInstrumentCd());
			paramMap.put("idx", _idx);
			paramMap.put("ccyCd", instrumentInfoModel.getCcyCd());	
			if (_insertResult)
				_procPriceDataDao.insertDeltaGamma(paramMap);
			
			System.out.println(factorCd + "; " + greek); 
					
		}
	}
	
	public void setCalcBump(boolean calcBump){
		_calcBump = calcBump; 
	}

}
