package com.quantosauros.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.quantosauros.common.Frequency;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.PaymentPeriod;
import com.quantosauros.common.date.PaymentPeriodGenerator;
import com.quantosauros.manager.dao.ProductInfoDao;
import com.quantosauros.manager.dao.ProductLegDao;
import com.quantosauros.manager.dao.ProductOptionScheduleDao;
import com.quantosauros.manager.dao.ProductScheduleDao;
import com.quantosauros.manager.model.ScheduleInfo;

@Controller
public class ProductRegistrationController {

	private ProductInfoDao productInfoDao;
	private ProductLegDao productLegDao;	
	private ProductScheduleDao productScheduleDao;
	private ProductOptionScheduleDao productOptionScheduleDao;
	
	@Autowired
	public ProductRegistrationController(
			ProductInfoDao productInfoDao,
			ProductLegDao productLegDao,
			ProductScheduleDao productScheduleDao,
			ProductOptionScheduleDao productOptionScheduleDao){
		this.productInfoDao = productInfoDao;
		this.productLegDao = productLegDao;		
		this.productOptionScheduleDao = productOptionScheduleDao;
		this.productScheduleDao = productScheduleDao;
	}	
	
	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView viewPage(){
		ModelAndView model = new ModelAndView("/registration");
		
		return model;
	}
	
	@RequestMapping(value="/json/scheduleList", method = RequestMethod.GET)
	public @ResponseBody List<ScheduleInfo> getScheduleList(
			HttpServletRequest request, 
			HttpServletResponse response){
		//http://localhost:8080/AADManager/json/scheduleList?issueDt=20140901&mrtyDt=20290901&couponFreq=Q
		
		String startDt = request.getParameter("issueDt");
		String mrtyDt = request.getParameter("mrtyDt");
		String frequency = request.getParameter("couponFreq");
		String noncallYear = request.getParameter("noncallYear");
		
		Date startDate = null;
		if (noncallYear != null){
			startDate = Date.valueOf(startDt).plusYears(Integer.parseInt(noncallYear));
		} else {
			startDate = Date.valueOf(startDt);
		}
		PaymentPeriod[] periods = genPeriod(
				startDate, 
				Date.valueOf(mrtyDt), 
				Frequency.valueOf(frequency));
		
		List<ScheduleInfo> list = new ArrayList<>();
		int periodNum = (noncallYear == null) ? periods.length : periods.length - 1;
		for (int i = 0; i < periodNum; i++){
			ScheduleInfo scheduleInfo = new ScheduleInfo();
			scheduleInfo.setStartDate(periods[i].getStartDate().toString());
			scheduleInfo.setEndDate(periods[i].getEndDate().toString());
			scheduleInfo.setPaymentDate(periods[i].getPaymentDate().toString());
			list.add(scheduleInfo);
		}
		
		return list;		
	}
	@RequestMapping(value="/registration/execute", method = RequestMethod.POST)
	public void execute(
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		Map map = request.getParameterMap();
		
		String[] productInfoKeys = new String[]{
			//ProductInfo
			"productCd", "productCcyCd", "principalExchCd", "optionTypeCd", 
			"issueDt", "mrtyDt",			
		};
		String[] payLegKeys = new String[]{			
			//PayLeg
			"payLegTypeCd", "payLegUndTypeCd", "payLegCondiTypeCd", "payLegNotional", 
			"payLegCcyCd", "payLegDCF", "payLegCapFloorCd",
			"payLegIrCd1", "payLegIrTenor1", "payLegIrCouponFreq1", "payLegIrTypeCd1",
			"payLegIrCd2", "payLegIrTenor2", "payLegIrCouponFreq2", "payLegIrTypeCd2",
			"payLegIrCd3", "payLegIrTenor3", "payLegIrCouponFreq3", "payLegIrTypeCd3",
		};
		String[] payLegScheduleKeys = new String[]{
			//PayLegSchedule
			"paystartDate", "payendDate", "paypaymentDate", "payCouponType", 
			"payUpperLimit1", "payLowerLimit1", "payUpperLimit2", "payLowerLimit2",
			"payInCouponRate", "payOutCouponRate", "payLeverage1", "payLeverage2", "payLeverage3",
			"paySpread", "paycap", "payfloor",
		};
		String[] rcvLegKeys = new String[] {
			//RcvLeg
			"rcvLegTypeCd", "rcvLegUndTypeCd", "rcvLegCondiTypeCd", "rcvLegNotional", 
			"rcvLegCcyCd", "rcvLegDCF", "rcvLegCapFloorCd", 
			"rcvLegIrCd1", "rcvLegIrTenor1", "rcvLegIrCouponFreq1", "rcvLegIrTypeCd1",
			"rcvLegIrCd2", "rcvLegIrTenor2", "rcvLegIrCouponFreq2", "rcvLegIrTypeCd2",
			"rcvLegIrCd3", "rcvLegIrTenor3", "rcvLegIrCouponFreq3", "rcvLegIrTypeCd3",
		};
		String[] rcvLegScheduleKeys = new String[] {
			//RcvLegSchedule
			"rcvstartDate", "rcvendDate", "rcvpaymentDate", "rcvCouponType", 
			"rcvUpperLimit1", "rcvLowerLimit1", "rcvUpperLimit2", "rcvLowerLimit2",
			"rcvInCouponRate", "rcvOutCouponRate", "rcvLeverage1", "rcvLeverage2", "rcvLeverage3",
			"rcvSpread", "rcvcap", "rcvfloor",
		};
		String[] exerciseInfoKeys = new String[] {
			//ExerciseInfo
			"exerciseStrtDt", "exerciseEndDt", "exerciseTypeCd", "exerciseStrike"
		};
		
		
		String instrumentCd = ((String[])map.get("productCd"))[0];
		
		//ProductInfo		
		Map<String, String> params = new HashMap<>();
		for (int index = 0; index < productInfoKeys.length; index++){
			String key = productInfoKeys[index];
			if (map.containsKey(key)){
				String[] contents = (String[])map.get(key);				
				if (key.equals("issueDt") || key.equals("mrtyDt")){
					params.put(key, contents[0].replace("-", ""));					
				} else {
					params.put(key, contents[0]);
				}								
			}
		}		
		productInfoDao.insertProductInfo(params);
		
		//ProductLeg-Pay
		params = new HashMap<>();
		for (int index = 0; index < payLegKeys.length; index++){
			String key = payLegKeys[index];
			if (map.containsKey(key)){
				String[] contents = (String[])map.get(key);				
				params.put(key.substring(6), contents[0]);							
			}
		}		
		params.put("instrumentCd", instrumentCd);
		params.put("payRcvCd", "P");
		productLegDao.insertProductLeg(params);
		
		//ProductLeg-Rcv
		params = new HashMap<>();
		for (int index = 0; index < rcvLegKeys.length; index++){
			String key = rcvLegKeys[index];
			if (map.containsKey(key)){
				String[] contents = (String[])map.get(key);				
				params.put(key.substring(6), contents[0]);							
			}
		}		
		params.put("instrumentCd", instrumentCd);
		params.put("payRcvCd", "R");
		productLegDao.insertProductLeg(params);
		
		//ProductLegSchedule-Pay
		String payLegNotional = ((String[])map.get("payLegNotional"))[0];
		params = new HashMap<>();
		int schNum = ((String[])map.get("paystartDate")).length;
		for (int schIndex = 0; schIndex < schNum; schIndex++){
			for (int index = 0; index < payLegScheduleKeys.length; index++){
				String key = payLegScheduleKeys[index];
				if (map.containsKey(key)){
					String[] contents = (String[])map.get(key);
					String modifiedKey = key.substring(3);
					String modifiedContent = contents[schIndex];
					if (modifiedContent.isEmpty()){
						modifiedContent = null;
					} else {
						if (modifiedKey.equals("UpperLimit1") || modifiedKey.equals("LowerLimit1") ||
								modifiedKey.equals("UpperLimit2") || modifiedKey.equals("LowerLimit2")||
								modifiedKey.equals("InCouponRate") || modifiedKey.equals("OutCouponRate")||
								modifiedKey.equals("Spread") || modifiedKey.equals("cap")||
								modifiedKey.equals("floor")){
							
							double tmp = Double.parseDouble(modifiedContent) / 100.0;						
							modifiedContent = Double.toString(tmp);							
						}
					}
											
					params.put(modifiedKey, modifiedContent);							
				}
			}		
			params.put("instrumentCd", instrumentCd);
			params.put("payRcvCd", "P");
			params.put("principal", payLegNotional);
			productScheduleDao.insertProductSchedule(params);
		}
		
		//ProductLegSchedule-Rcv
		String rcvLegNotional = ((String[])map.get("rcvLegNotional"))[0];
		params = new HashMap<>();
		schNum = ((String[])map.get("rcvstartDate")).length;
		for (int schIndex = 0; schIndex < schNum; schIndex++){
			for (int index = 0; index < rcvLegScheduleKeys.length; index++){
				String key = rcvLegScheduleKeys[index];
				if (map.containsKey(key)){
					String[] contents = (String[])map.get(key);
					String modifiedKey = key.substring(3);
					String modifiedContent = contents[schIndex];
					if (modifiedContent.isEmpty()){
						modifiedContent = null;
					} else {
						if (modifiedKey.equals("UpperLimit1") || modifiedKey.equals("LowerLimit1") ||
								modifiedKey.equals("UpperLimit2") || modifiedKey.equals("LowerLimit2")||
								modifiedKey.equals("InCouponRate") || modifiedKey.equals("OutCouponRate")||
								modifiedKey.equals("Spread") || modifiedKey.equals("cap")||
								modifiedKey.equals("floor")){
							
							double tmp = Double.parseDouble(modifiedContent) / 100.0;						
							modifiedContent = Double.toString(tmp);							
						}
					}
						
					params.put(modifiedKey, modifiedContent);		
				}
			}		
			params.put("instrumentCd", instrumentCd);
			params.put("payRcvCd", "R");
			params.put("principal", rcvLegNotional);
			productScheduleDao.insertProductSchedule(params);
		}
		//exerciseInfoKeys
		String optionTypeCD = ((String[])map.get("optionTypeCd"))[0];
		if (!optionTypeCD.equals("NONE")){
			int exSchNum = ((String[])map.get("exerciseStrtDt")).length;
			params = new HashMap<>();
			for (int exSchIndex = 0; exSchIndex < exSchNum; exSchIndex++){
				for (int index = 0; index < exerciseInfoKeys.length; index++){
					String key = exerciseInfoKeys[index];
					if (map.containsKey(key)){
						String[] contents = (String[])map.get(key);
						
						params.put(key, contents[exSchIndex]);
					}
				}
				params.put("instrumentCd", instrumentCd);
				productOptionScheduleDao.insertProductOptionSchedule(params);
			}
		}
				
//		ModelAndView model = new ModelAndView("/registration");		
		
//		return model;
	}
	private static PaymentPeriod[] genPeriod(
			Date issueDt, Date maturityDt, Frequency periodFreq){
		
		PaymentPeriodGenerator pg = 
				new PaymentPeriodGenerator(
						issueDt, maturityDt, 
						issueDt.plusMonths(periodFreq.toMonthUnit()), 
						maturityDt, 
						periodFreq);
		
		PaymentPeriod[] period = pg.generate();
		
		return period;
		
	}
}
