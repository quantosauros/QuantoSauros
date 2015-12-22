package com.quantosauros.batch.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.quantosauros.batch.dao.InstrumentInfoDao;
import com.quantosauros.batch.instrument.calculator.AbstractCalculator;
import com.quantosauros.batch.instrument.marketDataCreator.HullWhiteVolatilitySurfaceCreator;
import com.quantosauros.batch.instrument.marketDataCreator.IRCurveContainer;
import com.quantosauros.batch.mybatis.SqlMapClient;
import com.quantosauros.batch.types.CcyType;
import com.quantosauros.batch.types.RiskFactorType;
import com.quantosauros.common.date.Date;

public abstract class AbstractProcess {

	protected Date _processDate;
	//protected String _processInstTypeCd;
	protected String _procId;
	
	protected String _idx;
	protected SqlSession _session;
	protected List<InstrumentInfoDao> _infoDaoList;
	protected String _marketDataId;
	protected IRCurveContainer _irCurveContainer;	
	protected HullWhiteVolatilitySurfaceCreator _surfaceContainer;
	
	protected AbstractCalculator _calculator;
	
	protected int _simNum = 5000;	
	protected int _monitorFrequency = 1;	
	protected double _epsilon;
	protected boolean _insertResult = false;	
	
	public AbstractProcess(Date processDate, String procId, String idx) {
		_processDate = processDate;
		_procId = procId;
		_idx = idx;		
		_session = SqlMapClient.getSqlSession();	
		if (this instanceof ProcessPrices){
			_epsilon = 0;
		} else if (this instanceof ProcessGreeks){
			_epsilon = 0.0001;
		}
		_irCurveContainer = new IRCurveContainer(processDate, _epsilon);		
		_surfaceContainer = new HullWhiteVolatilitySurfaceCreator(processDate);
		
		//get an instrument list
		Map paramMap = new HashMap();
		paramMap.put("dt", _processDate.getDt());
		_infoDaoList = _session.selectList("DBCommon.getInstrumentInfo", paramMap);
	}
	
	public final void execute(){
		for (int infoDaoIndex = 0; infoDaoIndex < _infoDaoList.size(); infoDaoIndex++){
			InstrumentInfoDao instrumentInfoDao = _infoDaoList.get(infoDaoIndex);
			generate(instrumentInfoDao);
			calculate(instrumentInfoDao);
		}
	}
	private void generate(InstrumentInfoDao instrumentInfoDao){		
		//generate a Curve Container
		Map paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentInfoDao.getInstrumentCd());
		List ircCdList = _session.selectList("MarketData.getIrcCd", paramMap);
		HashMap riskFactorCdMap = genCurveContainer(ircCdList);
		
		//generate Calculator
		_calculator = new AbstractCalculator(
				instrumentInfoDao.getInstrumentCd(), _processDate, 
				riskFactorCdMap, _monitorFrequency, _simNum);
	}
	
	private void calculate(InstrumentInfoDao instrumentInfoDao){		
		calcInstrument(instrumentInfoDao);
		if (_insertResult)
			insertResult(instrumentInfoDao);		
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
					if (!_marketDataId.equals("0")){
			        	//Specific Market Scenario
			        	Map paramMap = new HashMap();        		
						paramMap.put("marketDataId", _marketDataId);
						paramMap.put("originCd", ircCd);
						ircCd = (String)_session.selectOne(
								"MarketData.getIrcCdFromMarketDataMap", paramMap);	        	
					}
					
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
	
	protected abstract void calcInstrument(InstrumentInfoDao instrumentInfoDao);	
	protected abstract void insertResult(InstrumentInfoDao instrumentInfoDao);
		
	public void setSimNum(int simNum){
		this._simNum = simNum;
	}
	public void setMonitorFrequency(int monitorFrequency){
		_monitorFrequency = monitorFrequency;
	}
	public void setSpecificInstrument(String instrumentCd){
		Map paramMap = new HashMap();
		paramMap.put("dt", _processDate.getDt());
		paramMap.put("instrumentCd", instrumentCd);
		_infoDaoList = _session.selectList(
				"DBCommon.getInstrumentInfoSpeicificProduct", paramMap);		
		
	}
	public void setInsertResults(boolean flag){
		_insertResult = flag;
	}
}
