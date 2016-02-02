package com.quantosauros.batch.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.quantosauros.batch.dao.InstrumentInfoDao;
import com.quantosauros.batch.dao.MarketDataDao;
import com.quantosauros.batch.dao.MySqlInstrumentInfoDao;
import com.quantosauros.batch.dao.MySqlMarketDataDao;
import com.quantosauros.batch.dao.MySqlProcPriceDataDao;
import com.quantosauros.batch.dao.ProcPriceDataDao;
import com.quantosauros.batch.instrument.calculator.AbstractCalculator;
import com.quantosauros.batch.instrument.marketDataCreator.HullWhiteVolatilitySurfaceCreator;
import com.quantosauros.batch.instrument.marketDataCreator.IRCurveContainer;
import com.quantosauros.batch.model.InstrumentInfoModel;
import com.quantosauros.batch.mybatis.SqlMapClient;
import com.quantosauros.batch.types.CcyType;
import com.quantosauros.batch.types.RiskFactorType;
import com.quantosauros.common.date.Date;

public abstract class AbstractProcess {

	protected Date _processDate;
	//protected String _processInstTypeCd;
	protected String _procId;
			
	protected List<InstrumentInfoModel> _infoDaoList;
	protected IRCurveContainer _irCurveContainer;	
	protected HullWhiteVolatilitySurfaceCreator _surfaceContainer;
	
	protected AbstractCalculator _calculator;
	
	protected InstrumentInfoDao _instrumentInfoDao = new MySqlInstrumentInfoDao();
	protected MarketDataDao _marketDataDao = new MySqlMarketDataDao();
	protected ProcPriceDataDao _procPriceDataDao = new MySqlProcPriceDataDao();
	
	protected int _simNum = 5000;	
	protected int _monitorFrequency = 1;	
	protected double _epsilon;
	protected boolean _insertResult = false;	
	
	public AbstractProcess(Date processDate, String procId) {
		_processDate = processDate;
		_procId = procId;		
		if (this instanceof ProcessPrices){
			_epsilon = 0;
		} else if (this instanceof ProcessGreeks){
			_epsilon = 0.0001;
		}
		_irCurveContainer = new IRCurveContainer(processDate, _epsilon);		
		_surfaceContainer = new HullWhiteVolatilitySurfaceCreator(processDate);
		
		//get an instrument list
		HashMap paramMap = new HashMap();
		paramMap.put("dt", _processDate.getDt());
		_infoDaoList = _instrumentInfoDao.selectInstrumentInfo(paramMap);
	}
	
	public final void execute(){
		for (int infoDaoIndex = 0; infoDaoIndex < _infoDaoList.size(); infoDaoIndex++){
			InstrumentInfoModel instrumentInfoModel = _infoDaoList.get(infoDaoIndex);
			generate(instrumentInfoModel);
			calculate(instrumentInfoModel);
		}
	}
	
	private void generate(InstrumentInfoModel instrumentInfoModel){		
		//generate a Curve Container
		HashMap paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentInfoModel.getInstrumentCd());
		List ircCdList = _marketDataDao.selectIRCCode(paramMap);
		
		HashMap riskFactorCdMap = genCurveContainer(ircCdList);
		
		//generate Calculator
		_calculator = new AbstractCalculator(
				instrumentInfoModel.getInstrumentCd(), _processDate, 
				riskFactorCdMap, _monitorFrequency, _simNum);
	}
	
	private void calculate(InstrumentInfoModel instrumentInfoModel){		
		calcInstrument(instrumentInfoModel);				
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
//					if (!_marketDataId.equals("0")){
//			        	//Specific Market Scenario
//			        	HashMap paramMap = new HashMap();        		
//						paramMap.put("marketDataId", _marketDataId);
//						paramMap.put("originCd", ircCd);
//						ircCd = _marketDataDao.selectIRCCodeFromMarketDataMap(paramMap);	        	
//					}
					
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
	
	protected abstract void calcInstrument(InstrumentInfoModel instrumentInfoModel);	
		
	public void setSimNum(int simNum){
		this._simNum = simNum;
	}
	
	public void setMonitorFrequency(int monitorFrequency){
		_monitorFrequency = monitorFrequency;
	}
	
	public void setSpecificInstrument(String instrumentCd){
		HashMap paramMap = new HashMap();
		paramMap.put("dt", _processDate.getDt());
		paramMap.put("instrumentCd", instrumentCd);		
		
		_infoDaoList = _instrumentInfoDao.selectSpecificInstrumentInfo(paramMap);		
		
	}
	
	public void setInsertResults(boolean flag){
		_insertResult = flag;
	}
}
