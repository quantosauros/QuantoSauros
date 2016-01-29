package com.quantosauros.batch.instrument;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.quantosauros.batch.dao.MarketDataDao;
import com.quantosauros.batch.dao.MySqlMarketDataDao;
import com.quantosauros.batch.dao.MySqlProductLegDao;
import com.quantosauros.batch.dao.MySqlProductScheduleDao;
import com.quantosauros.batch.dao.ProductLegDao;
import com.quantosauros.batch.dao.ProductScheduleDao;
import com.quantosauros.batch.instrument.marketDataCreator.AbstractMarketDataCreator;
import com.quantosauros.batch.model.IrCurveModel;
import com.quantosauros.batch.model.ProductLegModel;
import com.quantosauros.batch.model.ProductScheduleModel;
import com.quantosauros.batch.mybatis.SqlMapClient;
import com.quantosauros.common.Frequency;
import com.quantosauros.common.TypeDef;
import com.quantosauros.common.TypeDef.ConditionType;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.TypeDef.RateType;
import com.quantosauros.common.TypeDef.UnderlyingType;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.CalendarFactory;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.DayCountFraction;
import com.quantosauros.common.date.Vertex;
import com.quantosauros.common.interestrate.InterestRateCurve;

public class ProductDataGenerator {

	private Date _asOfDate;
	private String _instrumentCd;
	private String _countryCd;
	private SqlSession _session;
	
	public ProductDataGenerator(Date asOfDate, 
			String instrumentCd, String countryCd) {
		_asOfDate = asOfDate;
		_instrumentCd = instrumentCd;
		_countryCd = countryCd;
		
		_session = SqlMapClient.getSqlSessionFactory().openSession();
		
	}
	public void generate(){
		generateLeg("P");
		generateLeg("R");
	}
	public void generateLeg(String payRcvCd){
		
		//Generate Calendar
		Calendar calendar = CalendarFactory.getInstance(_countryCd, 1);
				
		//get Schedule
		ProductScheduleDao productScheduleDao = new MySqlProductScheduleDao();
		HashMap paramMap = new HashMap();
		paramMap.put("instrumentCd", _instrumentCd);
		paramMap.put("payRcvCd", payRcvCd);
		List<ProductScheduleModel> scheduleModelList = productScheduleDao.selectProductSchedule(paramMap);
		
		//get leg
		ProductLegDao productLegDao = new MySqlProductLegDao();
		paramMap = new HashMap();
		paramMap.put("instrumentCd", _instrumentCd);
		paramMap.put("payRcvCd", payRcvCd);
		ProductLegModel productLegModel = productLegDao.selectProductLegInfo(paramMap);
		UnderlyingType undType = TypeDef.getUnderlyingType(productLegModel.getUnderlyingType());
		ConditionType condiType = TypeDef.getConditionType(productLegModel.getConditionType());
		String capFloorCd =productLegModel.getCapFloorCd();
		
		int undNum = TypeDef.getNumOfUnderNum(undType);
		String[] ircCds = new String[] {
				productLegModel.getCouponIrcCd1(),
				productLegModel.getCouponIrcCd2(),
				productLegModel.getCouponIrcCd3(),
		};
		Vertex[] ircMrtyCds = new Vertex[]{
				Vertex.valueOf(productLegModel.getCouponIrcMrtyCd1()),
				Vertex.valueOf(productLegModel.getCouponIrcMrtyCd2()),
				Vertex.valueOf(productLegModel.getCouponIrcMrtyCd3()),
		};		
		RateType[] irRateTypeCds = new RateType[]{
				TypeDef.getRateType(productLegModel.getCouponIrcTypeCd1()),
				TypeDef.getRateType(productLegModel.getCouponIrcTypeCd2()),
				TypeDef.getRateType(productLegModel.getCouponIrcTypeCd3()),
		};
		Frequency[] irCouponFreqs = new Frequency[]{
				Frequency.valueOf(productLegModel.getCouponIrcCouponFreqCd1()),
				Frequency.valueOf(productLegModel.getCouponIrcCouponFreqCd2()),
				Frequency.valueOf(productLegModel.getCouponIrcCouponFreqCd3()),
		};
				
		DayCountFraction dcf = DayCountFraction.valueOf(productLegModel.getDcf());
		
		for (int schIndex = 0; schIndex < scheduleModelList.size(); schIndex++){
			ProductScheduleModel productScheduleModel = scheduleModelList.get(schIndex);
			
			Date startDt = Date.valueOf(productScheduleModel.getCouponStrtDt());			
			Date endDt = Date.valueOf(productScheduleModel.getCouponEndDt());
			Date paymentDt = Date.valueOf(productScheduleModel.getCouponPayDt());
			
			CouponType couponType = TypeDef.getCouponType(productScheduleModel.getCouponType());
			
			Date dt = startDt;
			Date nextDt = calendar.adjustDate(dt.plusDays(1), BusinessDayConvention.FOLLOWING);
			
			double nextCoupon = 0;
			int accrualDayCnt = 0;
			double accumulateAvgCoupon = 0;
			String nextCouponPayDt = paymentDt.getDt();
			
			double spread = Double.parseDouble(productScheduleModel.getFixedCoupon());
			double leverage1 = Double.parseDouble(productScheduleModel.getLeverage1());
			double leverage2 = Double.parseDouble(productScheduleModel.getLeverage2());
						
			double floor = Double.parseDouble(productScheduleModel.getFloor());
			double cap = Double.parseDouble(productScheduleModel.getCap());			
						
			double totalTenor = 0;
			double totalAmount = 0;			
			while (!dt.equals(endDt)){
				if (dt.diff(_asOfDate) >= 0){
					break;
				}
				
				//get Rates
				InterestRateCurve[] irCurves = new InterestRateCurve[undNum];
				double[] rates = new double[undNum];
				for (int ircIndex = 0; ircIndex < undNum; ircIndex++){						
					irCurves[ircIndex] = getIrCurves(startDt, ircCds[ircIndex]);
					rates[ircIndex] = getRate(irCurves[ircIndex], irRateTypeCds[ircIndex], ircMrtyCds[ircIndex], irCouponFreqs[ircIndex]);
				}
				
				if (couponType.equals(CouponType.FIXED)){				
					paramMap = new HashMap();
					paramMap.put("dt", dt.getDt());
			        paramMap.put("instrumentCd", _instrumentCd);        
			        paramMap.put("payRcvCd", payRcvCd);
			        paramMap.put("nextCouponPayDt", nextCouponPayDt);
			        paramMap.put("nextCoupon", nextCoupon * 100);		        
			        paramMap.put("accrualDayCnt", accrualDayCnt);
			        paramMap.put("accumulateAvgCoupon", accumulateAvgCoupon * 100);
			        
				} else if (couponType.equals(CouponType.RESET)){					
					if (undType.equals(UnderlyingType.R1)){		
						nextCoupon = leverage1 * rates[0] + spread;
					} 					
					//INSERT
					paramMap = new HashMap();
					paramMap.put("dt", dt.getDt());
			        paramMap.put("instrumentCd", _instrumentCd);        
			        paramMap.put("payRcvCd", payRcvCd);
			        paramMap.put("nextCouponPayDt", nextCouponPayDt);
			        paramMap.put("nextCoupon", nextCoupon * 100);		        
			        paramMap.put("accrualDayCnt", accrualDayCnt);
			        paramMap.put("accumulateAvgCoupon", accumulateAvgCoupon * 100);
				} else if (couponType.equals(CouponType.ACCRUAL)){
					
					int condiNum = TypeDef.getNumOfCondition(condiType);
					double[] lowerBound = new double[condiNum]; 							
					double[] upperBound = new double[condiNum];			
					
					int diffDays = nextDt.diff(dt);
					if (condiNum == 1){
						lowerBound[0] = Double.parseDouble(productScheduleModel.getLowerBound1());
						upperBound[0] = Double.parseDouble(productScheduleModel.getUpperBound1());
						double refRate = 0;
						if (condiType.equals(ConditionType.R1)){
							refRate = rates[0];
						} else if (condiType.equals(ConditionType.R2)){
							refRate = rates[1];
						} else if (condiType.equals(ConditionType.R3)){
							refRate = rates[2];
						} else if (condiType.equals(ConditionType.R1mR2)){
							refRate = rates[0] - rates[1];
						} else if (condiType.equals(ConditionType.R2mR3)){
							refRate = rates[1] - rates[2];
						}
						if (lowerBound[0] < refRate && upperBound[0] > refRate){
							accrualDayCnt = accrualDayCnt + diffDays;
						}						
						
					} else if (condiNum == 2){
						lowerBound[0] = Double.parseDouble(productScheduleModel.getLowerBound1());
						upperBound[0] = Double.parseDouble(productScheduleModel.getUpperBound1());
						lowerBound[1] = Double.parseDouble(productScheduleModel.getLowerBound2());					
						upperBound[1] = Double.parseDouble(productScheduleModel.getUpperBound2());
						double refRate1 = 0;
						double refRate2 = 0;
						if (condiType.equals(ConditionType.R1nR2)){
							refRate1 = rates[0];
							refRate2 = rates[1];
						} else if (condiType.equals(ConditionType.R1nR3)){
							refRate1 = rates[0];
							refRate2 = rates[2];
						} else if (condiType.equals(ConditionType.R2nR3)){
							refRate1 = rates[1];
							refRate2 = rates[2];
						} else if (condiType.equals(ConditionType.R1nR2mR3)){
							refRate1 = rates[0];
							refRate2 = rates[1] - rates[2];
						}
						if (lowerBound[0] < refRate1 && upperBound[0] > refRate1){
							if (lowerBound[1] < refRate2 && upperBound[1] > refRate2){
								accrualDayCnt = accrualDayCnt + diffDays;
							}							
						}
					}	
					paramMap = new HashMap();
					paramMap.put("dt", dt.getDt());
			        paramMap.put("instrumentCd", _instrumentCd);        
			        paramMap.put("payRcvCd", payRcvCd);
			        paramMap.put("nextCouponPayDt", nextCouponPayDt);
			        paramMap.put("nextCoupon", nextCoupon * 100);		        
			        paramMap.put("accrualDayCnt", accrualDayCnt);
			        paramMap.put("accumulateAvgCoupon", accumulateAvgCoupon * 100);
					
					
				} else if (couponType.equals(CouponType.AVERAGE)){
					
					double tenor = dcf.getYearFraction(dt, nextDt);
					double coupon = 0;
					if (undType.equals(UnderlyingType.R1mR2)){
						coupon = leverage1 * rates[0] - leverage2 * rates[1] + spread;
					} 
					if (capFloorCd.equals("0")){
						//none						
					} else if (capFloorCd.equals("1")){
						//Cap only
						coupon = Math.min(cap, coupon);
					} else if (capFloorCd.equals("2")){
						//Floor only
						coupon = Math.max(floor, coupon);
					} else if (capFloorCd.equals("3")){
						//both
						coupon = Math.max(floor, Math.min(cap, coupon));
					} 
					totalAmount += coupon * tenor;
					totalTenor += tenor;					
					accumulateAvgCoupon = totalAmount / totalTenor;
					
					int condiNum = TypeDef.getNumOfCondition(condiType);
					double[] lowerBound = new double[condiNum]; 							
					double[] upperBound = new double[condiNum];			
					
					int diffDays = nextDt.diff(dt);
					if (condiNum == 1){
						lowerBound[0] = Double.parseDouble(productScheduleModel.getLowerBound1());
						upperBound[0] = Double.parseDouble(productScheduleModel.getUpperBound1());
						double refRate = 0;
						if (condiType.equals(ConditionType.R1)){
							refRate = rates[0];
						} else if (condiType.equals(ConditionType.R2)){
							refRate = rates[1];
						} else if (condiType.equals(ConditionType.R3)){
							refRate = rates[2];
						} else if (condiType.equals(ConditionType.R1mR2)){
							refRate = rates[0] - rates[1];
						} else if (condiType.equals(ConditionType.R2mR3)){
							refRate = rates[1] - rates[2];
						}
						if (lowerBound[0] < refRate && upperBound[0] > refRate){
							accrualDayCnt = accrualDayCnt + diffDays;
						}							
					} else if (condiNum == 2){
						lowerBound[0] = Double.parseDouble(productScheduleModel.getLowerBound1());
						upperBound[0] = Double.parseDouble(productScheduleModel.getUpperBound1());
						lowerBound[1] = Double.parseDouble(productScheduleModel.getLowerBound2());					
						upperBound[1] = Double.parseDouble(productScheduleModel.getUpperBound2());
						double refRate1 = 0;
						double refRate2 = 0;
						if (condiType.equals(ConditionType.R1nR2)){
							refRate1 = rates[0];
							refRate2 = rates[1];
						} else if (condiType.equals(ConditionType.R1nR3)){
							refRate1 = rates[0];
							refRate2 = rates[2];
						} else if (condiType.equals(ConditionType.R2nR3)){
							refRate1 = rates[1];
							refRate2 = rates[2];
						} else if (condiType.equals(ConditionType.R1nR2mR3)){
							refRate1 = rates[0];
							refRate2 = rates[1] - rates[2];
						}
						if (lowerBound[0] < refRate1 && upperBound[0] > refRate1){
							if (lowerBound[1] < refRate2 && upperBound[1] > refRate2){
								accrualDayCnt = accrualDayCnt + diffDays;
							}							
						}
					}						
					
					paramMap = new HashMap();
					paramMap.put("dt", dt.getDt());
			        paramMap.put("instrumentCd", _instrumentCd);        
			        paramMap.put("payRcvCd", payRcvCd);
			        paramMap.put("nextCouponPayDt", nextCouponPayDt);
			        paramMap.put("nextCoupon", nextCoupon * 100);		        
			        paramMap.put("accrualDayCnt", accrualDayCnt);
			        paramMap.put("accumulateAvgCoupon", accumulateAvgCoupon * 100);
				}
				
				dt = calendar.adjustDate(dt.plusDays(1), BusinessDayConvention.FOLLOWING);
				nextDt = calendar.adjustDate(nextDt.plusDays(1), BusinessDayConvention.FOLLOWING);
				_session.insert("InsertProduct.insertProductLegData", paramMap);				
//				System.out.println(paramMap.get("dt")+ ", " +
//						paramMap.get("payRcvCd") + ", " +
//						paramMap.get("nextCouponPayDt") + ", " +
//						paramMap.get("nextCoupon") + ", " +
//						paramMap.get("accrualDayCnt") + ", " + 
//						paramMap.get("accumulateAvgCoupon") + ", ");
				
			}						
		}   
		_session.commit();
	}
	
	private InterestRateCurve getIrCurves(Date processDate, String ircCd){
		HashMap<String, Object> paramMap = new HashMap<>();		
		paramMap.put("dt", processDate.getDt());
		paramMap.put("ircCd", ircCd);
		MarketDataDao marketDataDao = new MySqlMarketDataDao();
		List<IrCurveModel> irCurveDaoList = 
				marketDataDao.selectIrCurveModel(paramMap);
		
		return AbstractMarketDataCreator.getIrCurve(processDate, irCurveDaoList);
	}
	
	private double getRate(InterestRateCurve rateCurve, 
			RateType rateType, Vertex maturity, Frequency frequency){
		if (rateType.equals(RateType.SPOT)){
			return rateCurve.getSpotRate(maturity.getVertex(rateCurve.getDayCountFraction()));
		} else if (rateType.equals(RateType.SWAP)){
			return rateCurve.getForwardSwapRate(rateCurve.getDate(), 
					maturity.getVertex(rateCurve.getDayCountFraction()), frequency);
		} else if (rateType.equals(RateType.RMS)){
			return 0;
		} else {
			return 0;
		}
		
	}
}
