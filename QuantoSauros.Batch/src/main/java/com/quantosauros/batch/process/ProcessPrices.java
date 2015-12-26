package com.quantosauros.batch.process;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.quantosauros.batch.model.InstrumentInfoModel;
import com.quantosauros.common.TypeDef;
import com.quantosauros.common.TypeDef.ResultType;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.jpl.result.ResultDto;

public class ProcessPrices extends AbstractProcess {
	
	protected Money _originPrices;
	protected Money _nonExercisePrices;
	protected double _resultRcvValues;
	protected double _resultPayValues;
	
	public ProcessPrices(Date processDate, String procId, String idx) {
		super(processDate, procId, idx);
		//Insert DeltaGamma Info
		HashMap paramMap = new HashMap();
		paramMap.put("proc_id", _procId);
		paramMap.put("idx_id", _idx);
		String description = "default";
		paramMap.put("description", description);
		_procPriceDataDao.insertPriceInfo(paramMap);				
		
		//get a Market Data ID from proc Id
		paramMap = new HashMap();
		paramMap.put("procId", _procId);
		paramMap.put("idxId", _idx);
		_marketDataId = _procPriceDataDao.selectDataIdFromPriceByProcId(paramMap);
	}

	protected void calcInstrument(InstrumentInfoModel instrumentInfoModel){	
		_originPrices = _calculator.getPrice(
				_processDate, _irCurveContainer, _surfaceContainer, "");
		
		if (_insertResult)
			insertDetailResult(instrumentInfoModel.getInstrumentCd(), true);
				
		_resultRcvValues = _calculator.getLegPrice(0).getAmount();		
		_resultPayValues = _calculator.getLegPrice(1).getAmount();
		
		System.out.println("Original Price: " + _originPrices);
		System.out.println("Original RCV: " + _resultRcvValues);
		System.out.println("Original PAY: " + _resultPayValues);
		
		if (_calculator.getHasExercise()){
			_calculator.setHasExercise(false);
			_nonExercisePrices = _calculator.getPrice(
					_processDate, _irCurveContainer, _surfaceContainer, "");
			
			if (_insertResult)
				insertDetailResult(instrumentInfoModel.getInstrumentCd(), false);
			
			double noncallRcvValue = _calculator.getLegPrice(0).getAmount();
			double noncallPayValue = _calculator.getLegPrice(1).getAmount();
			System.out.println("NonCall Price: " + _nonExercisePrices);
			System.out.println("NonCall RCV: " + noncallRcvValue);
			System.out.println("NonCall PAY: " + noncallPayValue);
		}		
	}
	
	protected void insertDetailResult(String instrumentCd, boolean isOriginal){

		Map results = _calculator.getResults();
		
		int payIndex = _calculator.getPayIndex();
		int rcvIndex = _calculator.getRcvIndex();
		
		Iterator itr = results.keySet().iterator();
		while (itr.hasNext()){
			ResultType resultType = (ResultType)itr.next();
			Object result = results.get(resultType);
			String valueType = TypeDef.getResultTypeStr(resultType);
			if (result instanceof ResultDto[]){
				ResultDto[] resultDto = (ResultDto[])result;
				for (int legIndex = 0; legIndex < resultDto.length; legIndex++){					
					String legType = "";
					if (payIndex == legIndex){
						legType = "P";
					} else {
						legType = "R";
					}
					for (int periodIndex = 0; periodIndex < resultDto[legIndex].length(); periodIndex++){						
						double avg = resultDto[legIndex].getAvgValue(periodIndex);					
						double std = resultDto[legIndex].getStdValue(periodIndex);
						
						HashMap paramMap = new HashMap();
						paramMap.put("dt", _processDate.getDt());
				    	paramMap.put("procId", _procId);
						paramMap.put("instrumentCd", instrumentCd);
						paramMap.put("idx", _idx);
						paramMap.put("valueType", valueType);
						paramMap.put("legType", legType);
						paramMap.put("isNoncall", isOriginal);
						paramMap.put("periodNum", periodIndex);
						paramMap.put("value1", new BigDecimal(avg));
						paramMap.put("value2", new BigDecimal(std));
						
				    	_procPriceDataDao.insertDetailData(paramMap);			
						
						//System.out.println(valueType + legType + periodIndex + avg + std);
					}
				}
			} else if (result instanceof ResultDto){
				ResultDto resultDto = (ResultDto) result;
				for (int periodIndex = 0; periodIndex < resultDto.length(); periodIndex++){
					double avg = resultDto.getAvgValue(periodIndex);					
					double std = resultDto.getStdValue(periodIndex);	
					
					HashMap paramMap = new HashMap();
					paramMap.put("dt", _processDate.getDt());
			    	paramMap.put("procId", _procId);
					paramMap.put("instrumentCd", instrumentCd);
					paramMap.put("idx", _idx);
					paramMap.put("valueType", valueType);
					paramMap.put("isNoncall", isOriginal);
					paramMap.put("legType", "");
					paramMap.put("periodNum", periodIndex);
					paramMap.put("value1", new BigDecimal(avg));
					paramMap.put("value2", new BigDecimal(std));
					
					_procPriceDataDao.insertDetailData(paramMap);
				}
			}					
		}
	}
	
	protected void insertResult(InstrumentInfoModel instrumentInfoModel){
		HashMap paramMap = new HashMap();
		Money originPrice = _originPrices;
		BigDecimal originValue = 
				new BigDecimal(originPrice.getAmount());
		if (!_nonExercisePrices.equals(0)){			
			Money nonCallPrice = _nonExercisePrices;
			BigDecimal nonCallValue = 
					new BigDecimal(nonCallPrice.getAmount());
			paramMap.put("nonExercisePrice", nonCallValue);
		} else {
			paramMap.put("nonExercisePrice", "0");
		}
		
		double payPrice = _resultPayValues;
		double rcvPrice = _resultRcvValues;
		    	
    	paramMap.put("dt", _processDate.getDt());
    	paramMap.put("procId", _procId);
		paramMap.put("instrumentCd", instrumentInfoModel.getInstrumentCd());
		paramMap.put("idx", _idx);
		paramMap.put("price", originValue);
		paramMap.put("payPrice", new BigDecimal(payPrice));
		paramMap.put("rcvPrice", new BigDecimal(rcvPrice));		
		paramMap.put("ccyCd", instrumentInfoModel.getCcyCd());
    	_procPriceDataDao.insertPrice(paramMap);
		
	}
}
