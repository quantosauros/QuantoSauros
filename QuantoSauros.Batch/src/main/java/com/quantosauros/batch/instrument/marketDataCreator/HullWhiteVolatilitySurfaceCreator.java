package com.quantosauros.batch.instrument.marketDataCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.quantosauros.batch.dao.MarketDataDao;
import com.quantosauros.batch.dao.MySqlMarketDataDao;
import com.quantosauros.batch.model.IrCurveModel;
import com.quantosauros.batch.model.VolSurfModel;
import com.quantosauros.batch.mybatis.SqlMapClient;
import com.quantosauros.batch.types.CcyType;
import com.quantosauros.common.calibration.HWVolatilityCalibration;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.hullwhite.HWVolatilitySurface;
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.hullwhite.HullWhiteVolatility;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.common.volatility.VolatilitySurface;

public class HullWhiteVolatilitySurfaceCreator {

	private HashMap _container;
	private Date _processDate;
	
	public HullWhiteVolatilitySurfaceCreator(Date processDate){
		_container = new HashMap();
		_processDate = processDate;
	}
	
	public void genSurface(String ccyCd){		
		if (!_container.containsKey(ccyCd)){			
			HashMap paramMap = new HashMap();  	
			paramMap.put("dt", _processDate.getDt());
			paramMap.put("ccyCd", ccyCd);		
			MarketDataDao marketDataDao = new MySqlMarketDataDao();
			List<VolSurfModel> volSurfDaoList = marketDataDao.selectVolSurfModel(paramMap);
			
			VolatilitySurface surface = AbstractMarketDataCreator.getVolatilitySurface(
					_processDate, volSurfDaoList);
			
			HullWhiteParameters HWParams = AbstractMarketDataCreator.getHWParameters1();
			
			//get Risk-Free IRC CD from CCYCD
			paramMap = new HashMap();			
			paramMap.put("ccyCd", ccyCd);
			String rfIrcCd = marketDataDao.selectRiskFreeIrcCdFromCcyCd(paramMap);
			
			//get IRC Curve dao
			paramMap = new HashMap();  	
			paramMap.put("dt", _processDate.getDt());
			paramMap.put("ircCd", rfIrcCd);
			List<IrCurveModel> irCurveDaoList = marketDataDao.selectIrCurveModel(paramMap);					
			
			InterestRateCurve spotCurve = AbstractMarketDataCreator.getIrCurve(
					_processDate, irCurveDaoList);
			
			HWVolatilityCalibration cali = new HWVolatilityCalibration(
					ccyCd, _processDate, spotCurve, surface, HWParams);
						
			_container.put(CcyType.valueof(ccyCd), cali.calculate());	
			
//			_container.put(CcyType.valueof(ccyCd), 
//					AbstractMarketDataCreator.getHullWhiteVolatility1(ccyCd));			
		}		
	}
	
	public HullWhiteVolatility[] getHWVolatility(String ccyCd){
		Object hwVol = _container.get(CcyType.valueof(ccyCd));
		if (hwVol != null){
			return (HullWhiteVolatility[]) hwVol;
		} else {
			return null;
		}
	}
	
	public HullWhiteVolatility[] getHWVolatility(Object ccyCd){
		Object hwVol = _container.get(ccyCd);
		if (hwVol != null){
			return (HullWhiteVolatility[]) hwVol;
		} else {
			return null;
		}		
	}
	public HWVolatilitySurface getHWVolSurface(Object ccyCd){
		Object hwVol = _container.get(ccyCd);
		if (hwVol != null){
			return (HWVolatilitySurface) hwVol;
		} else {
			return null;
		}		
	}
}
