package com.quantosauros.batch.process;

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
		
	//[riskFactor][vertex]	
	private ArrayList<DeltaResult> _deltaResultsBump;
	private ArrayList<DeltaResult> _deltaResultsBumpNoncall;
	private ArrayList<DeltaResult> _deltaResultsAAD;
	private ArrayList<DeltaResult> _deltaResultsAADNoncall;
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
		if (_calcBump){
	        _deltaResultsBump = new ArrayList<DeltaResult>();
	        _deltaResultsBumpNoncall = new ArrayList<DeltaResult>();
		}
        _deltaResultsAAD = new ArrayList<DeltaResult>();        
        _deltaResultsAADNoncall = new ArrayList<DeltaResult>();
		
		//CALCULATE PRICE
		Money price = _calculator.getPrice(
				_processDate, _irCurveContainer, _surfaceContainer, "");
//		_calculator.getResults();
		
		System.out.println("Price: " + price);
		System.out.println("CouponPrice: " + _calculator.getLegPrice(0));
		System.out.println("SwapPrice: " + _calculator.getLegPrice(1));
				
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
				_deltaResultsAAD.add(new DeltaResult(ircCd, vertex, "AAD", AADGreeks));				
								
				//BUMPING		
				if (_calcBump){
					double[] BumpingGreeks = calcDelta(instrumentInfoModel.getInstrumentCd(), ircCd);
					_deltaResultsBump.add(new DeltaResult(ircCd, vertex, "BUMP", BumpingGreeks));
				}
								
				if (_calculator.getHasExercise()){					
					_calculator.setHasExercise(false);
					//AAD
					price = _calculator.getPrice(_processDate, 
							_irCurveContainer, _surfaceContainer, "");
					System.out.println("===NONCALL PRICE===");
					System.out.println("Price: " + price);
					System.out.println("CouponPrice: " + _calculator.getLegPrice(0));
					System.out.println("SwapPrice: " + _calculator.getLegPrice(1));
					
					changedIRCurves = _irCurveContainer.getScenarioCurves(ircCd);
										
					double[] nonCallAADResult = _calculator.getAADGreek(
							ircCd, _irCurveContainer, changedIRCurves, true);
					_deltaResultsAADNoncall.add(new DeltaResult(ircCd, vertex, "AAD", nonCallAADResult));
					
//					BUMPING		
					if (_calcBump){
						double[] nonCallBumpingResult = calcDelta(instrumentInfoModel.getInstrumentCd(), ircCd);
						_deltaResultsBumpNoncall.add(new DeltaResult(ircCd, vertex, "BUMP", nonCallBumpingResult));
						
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
			_calculator.setExerciseIndex(null);
			Money upPrice = _calculator.getPrice(_processDate, 
					_irCurveContainer, _surfaceContainer, upFlag);						
//			_calculator.getResults();
			
			//Down Price
			String downFlag = ircCd + "_DOWN_" + vertex.getCode();
			_calculator.setExerciseIndex(null);
			Money downPrice = _calculator.getPrice(_processDate, 
					_irCurveContainer, _surfaceContainer, downFlag);
//			_calculator.getResults();
			
			//Calculate Delta 
			Money delta = upPrice.subtract(downPrice);
			greeks[sensiIndex] = delta.getAmount();
			
			System.out.println(
					"INSTRUMENTCD: " + instrumentCd
					+ " : IRCCD: " + ircCd 
					+ " : EXERCISE: " + hasExercise
					+ " : VERTEX: " + vertex
					+ " : DELTA : "  + delta.getAmount()
					+ " : upPrice : " + upPrice.getAmount()
					+ " : downPrice : " + downPrice.getAmount());
			
		}		
		
		return greeks;
	}
	
	protected void insertResult(InstrumentInfoModel instrumentInfoDao){		
    	System.out.println("INSTRUMENTCD: " + instrumentInfoDao.getInstrumentCd());
    	
    	//BUMPING
    	ArrayList<DeltaResult> bumpArray = null;
    	ArrayList<DeltaResult> bumpNoncallArray = null;
    	if (_calcBump){
	    	bumpArray = _deltaResultsBump;
	    	bumpNoncallArray = _deltaResultsBumpNoncall;
    	}
    	//AAD
    	ArrayList<DeltaResult> AADArray = _deltaResultsAAD;
    	ArrayList<DeltaResult> AADNoncallArray = _deltaResultsAADNoncall;
    	
    	for (int rfIdx = 0; rfIdx < AADArray.size(); rfIdx++){
    		//AAD
    		DeltaResult deltaAAD = AADArray.get(rfIdx);    		
    		double[] greeksAAD = deltaAAD.getGreeks();    		
    		//NONCALL
    		double[] greeksNoncallAAD = null;
    		if (_calculator.getHasExercise()){
	    		DeltaResult deltaAADNoncall = AADNoncallArray.get(rfIdx);
	    		greeksNoncallAAD = deltaAADNoncall.getGreeks();
    		}   		
    		
    		//BUMPING
    		double[] greeksBump = null;
    		double[] greeksNoncallBump = null;
    		if (_calcBump){
	    		DeltaResult deltaBump = bumpArray.get(rfIdx);    		
	    		greeksBump = deltaBump.getGreeks();    		
	    		//NONCALL	    		
	    		if (_calculator.getHasExercise()){
		    		DeltaResult deltaBumpNoncall = bumpNoncallArray.get(rfIdx);
		    		greeksNoncallBump = deltaBumpNoncall.getGreeks();
	    		}
    		}
    		String riskFactorCd = deltaAAD.getRiskFactorCode();		
    		Vertex[] vertexList = deltaAAD.getVertex();    		
    		for (int vtxIndex = 0; vtxIndex < vertexList.length; vtxIndex++){
    			
    			Vertex vtx = vertexList[vtxIndex];
    			String factorCd = riskFactorCd + vtx.getCode();
    			    			
				//AAD
				double aadValue = greeksAAD[vtxIndex];				
				HashMap paramMapForAAD = new HashMap();
				paramMapForAAD.put("greek", aadValue);    			
    			//NONCALL
				double aadNoncallValue = 0;
    			if (_calculator.getHasExercise()){
    				aadNoncallValue = greeksNoncallAAD[vtxIndex];    				
    			}
    			
    			paramMapForAAD.put("nonExerciseGreek", aadNoncallValue);
    			paramMapForAAD.put("greekCd", "AAD");				
				paramMapForAAD.put("factorCd", factorCd);
				paramMapForAAD.put("dt", _processDate.getDt());
				paramMapForAAD.put("procId", _procId);
				paramMapForAAD.put("instrumentCd", instrumentInfoDao.getInstrumentCd());
				paramMapForAAD.put("idx", _idx);
				paramMapForAAD.put("ccyCd", instrumentInfoDao.getCcyCd());		
				_procPriceDataDao.insertDeltaGamma(paramMapForAAD);		    					
    		
		    	System.out.println(
						"RISKFACTOR_CD: " + factorCd + "_" + "AAD" + 
						"GREEK: " + aadValue +
						"NONCALL_GREEK: " + aadNoncallValue);
		    	
		    	//BUMP
		    	if (_calcBump){
	    			double bumpValue = greeksBump[vtxIndex];    			    			
	    			HashMap paramMapForBumping = new HashMap();    							
	    			paramMapForBumping.put("greek", bumpValue);    			
	    			//NONCALL
	    			double bumpNoncallValue = 0;
	    			if (_calculator.getHasExercise()){
	    				bumpNoncallValue = greeksNoncallBump[vtxIndex];    				
	    			}
	    			paramMapForBumping.put("nonExerciseGreek", bumpNoncallValue);
					paramMapForBumping.put("greekCd", "BUMP");				
					paramMapForBumping.put("factorCd", factorCd);
			    	paramMapForBumping.put("dt", _processDate.getDt());
			    	paramMapForBumping.put("procId", _procId);
					paramMapForBumping.put("instrumentCd", instrumentInfoDao.getInstrumentCd());
					paramMapForBumping.put("idx", _idx);
					paramMapForBumping.put("ccyCd", instrumentInfoDao.getCcyCd());
					_procPriceDataDao.insertDeltaGamma(paramMapForBumping);					
					
					System.out.println(
							"RISKFACTOR_CD: " + factorCd + "_" + "BUMP" + 
							"GREEK: " + bumpValue +
							"NONCALL_GREEK: " + bumpNoncallValue);			
		    	}
    		}
    	}
	}
	
	public void setCalcBump(boolean calcBump){
		_calcBump = calcBump; 
	}
}
