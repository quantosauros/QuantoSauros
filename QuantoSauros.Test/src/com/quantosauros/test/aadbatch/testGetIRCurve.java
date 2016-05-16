package com.quantosauros.test.aadbatch;

import java.util.HashMap;
import java.util.List;

import com.quantosauros.batch.dao.MarketDataDao;
import com.quantosauros.batch.dao.MySqlMarketDataDao;
import com.quantosauros.batch.instrument.marketDataCreator.AbstractMarketDataCreator;
import com.quantosauros.batch.model.IrCurveModel;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.CalendarFactory;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.interestrate.AbstractRateCurve;
import com.quantosauros.common.interestrate.BondRateCurve;
import com.quantosauros.common.interestrate.SwapRate;
import com.quantosauros.common.interestrate.SwapRateCurve;
import com.quantosauros.test.util.TestBase;

public class testGetIRCurve extends TestBase {

	
	public void test(){
	
		Date processDate = Date.valueOf("20160511");
		Calendar calendar = CalendarFactory.getInstance("KR", 1);
		Date maturityDate = calendar.adjustDate(
				processDate.plusYears(1), BusinessDayConvention.MODIFIED_FOLLOWING);
		
		String ircCd = "USDIRS";
		
		HashMap<String, Object> paramMap = new HashMap<>();
		
		paramMap.put("dt", processDate.getDt());
		paramMap.put("ircCd", ircCd);
		MarketDataDao marketDataDao = new MySqlMarketDataDao();
		List<IrCurveModel> irCurveDaoList = 
				marketDataDao.selectIrCurveModel(paramMap);
		
		AbstractRateCurve ytmCurve = AbstractMarketDataCreator.getIrCurve(
				processDate, irCurveDaoList);
		log(ytmCurve);
		
		if (ytmCurve instanceof SwapRateCurve){
			log("SWAP RATE Curve");
		} else if (ytmCurve instanceof BondRateCurve){
			log("BOND RATE Curve");
		}
		
//		for (int i = 0; i < 5; i++){
//			SwapRateCurve upCurve = ytmCurve.copy(i, 0.0001);
//			SwapRateCurve downCurve = ytmCurve.copy(i, -0.0001);
//		
////			log("UP");
////			log(upCurve.getDiscountFactor(Date.valueOf("20140303")));
////			log(upCurve.getDiscountFactor(Date.valueOf("20140602")));
////			log(upCurve.getDiscountFactor(Date.valueOf("20140902")));
////			log(upCurve.getDiscountFactor(Date.valueOf("20141202")));
////			
////			log("DOWN");
////			log(downCurve.getDiscountFactor(Date.valueOf("20140303")));
////			log(downCurve.getDiscountFactor(Date.valueOf("20140602")));
////			log(downCurve.getDiscountFactor(Date.valueOf("20140902")));
////			log(downCurve.getDiscountFactor(Date.valueOf("20141202")));
//			
//			log("UP");
//			log(upCurve.getForwardRate(Date.valueOf("20140303"), Date.valueOf("20140602")));
//			log(upCurve.getForwardRate(Date.valueOf("20140602"), Date.valueOf("20140902")));
//			log(upCurve.getForwardRate(Date.valueOf("20140902"), Date.valueOf("20141202")));
//			
//			log("DOWN");
//			log(downCurve.getForwardRate(Date.valueOf("20140303"), Date.valueOf("20140602")));
//			log(downCurve.getForwardRate(Date.valueOf("20140602"), Date.valueOf("20140902")));
//			log(downCurve.getForwardRate(Date.valueOf("20140902"), Date.valueOf("20141202")));
//			
//			
//		}
		
		
//		
//		
//		
//		
//		
		double[] irsMaturities = new double[]{1,2,3,5,7,10};
		
		log(maturityDate);
		for (int index = 0; index < irsMaturities.length; index++){
			double swapRate = ytmCurve.getForwardSwapRate(processDate, irsMaturities[index]);
			log("Maturity: " + irsMaturities[index] + "yr : " + swapRate);
		}
		
		
	}	
}
