package com.quantosauros.batch.process;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.quantosauros.batch.model.InstrumentInfoModel;
import com.quantosauros.common.TypeDef;
import com.quantosauros.common.TypeDef.ResultType;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.jpl.result.ResultDto;

public class ProcessPrices extends AbstractProcess {
			
	public ProcessPrices(Date processDate, String procId) {
		super(processDate, procId);			
		
	}

	protected void calcInstrument(InstrumentInfoModel instrumentInfoModel){	
		Money originPrice = _calculator.getPrice(
				_processDate, _irCurveContainer, _surfaceContainer, "");
//		_calculator.getResults();
		
		if (_insertResult)
			insertDetailResult(instrumentInfoModel.getInstrumentCd(), "N");
				
		Money rcvPrice = _calculator.getLegPrice(0);		
		Money payPrice = _calculator.getLegPrice(1);
		
		insertPriceResult(instrumentInfoModel, "N", 
				originPrice, rcvPrice, payPrice);
				
		if (_calculator.getHasExercise()){
			_calculator.setHasExercise(false);
			Money nonCallPrices = _calculator.getPrice(
					_processDate, _irCurveContainer, _surfaceContainer, "");
			
			if (_insertResult)
				insertDetailResult(instrumentInfoModel.getInstrumentCd(), "Y");
			
			Money noncallRcvValue = _calculator.getLegPrice(0);
			Money noncallPayValue = _calculator.getLegPrice(1);
			
			insertPriceResult(instrumentInfoModel, "Y", 
					nonCallPrices, noncallRcvValue, noncallPayValue);
			
			_calculator.setHasExercise(true);
		}		
	}
	
	protected void insertPriceResult(InstrumentInfoModel instrumentInfoModel,
			String nonCallCd, Money originalPrice, Money rcvPrice, Money payPrice){
		HashMap paramMap = new HashMap();
		DecimalFormat dformat = new DecimalFormat(".####");
		String originalValue = dformat.format(originalPrice.getAmount());		
		String payValue = dformat.format(payPrice.getAmount());
		String rcvValue = dformat.format(rcvPrice.getAmount());
		
		System.out.println(
				"Price Results of Product Code; " + instrumentInfoModel.getInstrumentCd() + 
				"; at; " + _processDate.getDt() +				 
				"; NONCALLCD: " + nonCallCd +  
				";)"				
		);

		System.out.println("Original Price: " + originalPrice);
		System.out.println("Original RCV: " + rcvPrice);
		System.out.println("Original PAY: " + payPrice);
		
    	paramMap.put("dt", _processDate.getDt());
    	paramMap.put("procId", _procId);
		paramMap.put("instrumentCd", instrumentInfoModel.getInstrumentCd());		
		paramMap.put("nonCallCd", nonCallCd);
		paramMap.put("price", originalValue);
		paramMap.put("payPrice", payValue);
		paramMap.put("rcvPrice", rcvValue);		
		paramMap.put("ccyCd", instrumentInfoModel.getCcyCd());
		if(_insertResult)
			_procPriceDataDao.insertPrice(paramMap);
		
	}
	
	protected void insertDetailResult(String instrumentCd, String nonCallCd){

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
						paramMap.put("valueType", valueType);
						paramMap.put("legType", legType);
						paramMap.put("nonCallCd", nonCallCd);
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
					paramMap.put("valueType", valueType);
					paramMap.put("nonCallCd", nonCallCd);
					paramMap.put("legType", "");
					paramMap.put("periodNum", periodIndex);
					paramMap.put("value1", new BigDecimal(avg));
					paramMap.put("value2", new BigDecimal(std));
					
					_procPriceDataDao.insertDetailData(paramMap);
				}
			}					
		}
	}
	
}
