package com.quantosauros.manager.web.pricer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quantosauros.batch.process.ProcessPricer;
import com.quantosauros.common.TypeDef.CouponType;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.common.date.PaymentPeriod;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.underlying.EquityUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.FXUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.RateUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;
import com.quantosauros.manager.model.pricer.LegInfoPricerModel;
import com.quantosauros.manager.model.pricer.LegPeriodPricerModel;
import com.quantosauros.manager.model.pricer.LegPricerModelForm;
import com.quantosauros.manager.model.pricer.ProductInfoPricerModel;
import com.quantosauros.manager.model.pricer.UnderlyingInfoPricerModel;
import com.quantosauros.manager.model.products.InstrumentInfo;
import com.quantosauros.manager.service.products.InstrumentInfoService;
import com.quantosauros.manager.web.chart.DeltaChartController;

@Controller
public class PricerController {
	
	private ProcessPricer _processPricer;
	private final Logger logger = Logger.getLogger(DeltaChartController.class);
	private InstrumentInfoService instrumentInfoService;
	private String instrumentCd;
	private String processDt;
	
	@Autowired
	public void setInstrumentInfoService(InstrumentInfoService instrumentInfoService){
		this.instrumentInfoService = instrumentInfoService;
	}
	
	@RequestMapping(value = "/pricer", method = RequestMethod.GET)
	public String pricerIndex(Model model){
		
		logger.debug("pricerIndex()");		
		populateModel(model);
		return "/pricer/pricerMain";
	}
	
	@RequestMapping(value = "/pricer", method = RequestMethod.POST)
	public String pricerUpdate(
//			@ModelAttribute("productInfoModel") ProductInfoPricerModel productInfoPricerModel,						
			@ModelAttribute("legPricerModelForm") LegPricerModelForm legPricerModelForm,
			Model model, final RedirectAttributes redirectAttributes){
				
		
		return "redirect:/pricer";
	}
	
	@RequestMapping(value = "/pricer/list", method = RequestMethod.GET)
	public String pricerExecute(
			@RequestParam("instrumentCd") String instrumentCd,
			@RequestParam("processDt") String processDt,
			Model model,
			final RedirectAttributes redirectAttributes){

		logger.debug("pricerExecute()");
		
		Date processDate = Date.valueOf(processDt.replaceAll("-",""));		
		int monitorFrequency = 1;
		int simNum = 100;
		
		if (_processPricer == null){
			this.instrumentCd = instrumentCd;
			this.processDt = processDt;
			_processPricer = new ProcessPricer(
					processDate, instrumentCd, monitorFrequency, simNum);
		} else {
			if (!this.instrumentCd.equals(instrumentCd)){
				this.instrumentCd = instrumentCd;
				this.processDt = processDt;
				_processPricer = new ProcessPricer(
						processDate, instrumentCd, monitorFrequency, simNum);
			} else if (!this.processDt.equals(processDt)){
				this.instrumentCd = instrumentCd;
				this.processDt = processDt;
				_processPricer = new ProcessPricer(
						processDate, instrumentCd, monitorFrequency, simNum);
			} else {
				
			}
		}
		
		Money price = _processPricer.getPrice();
		DecimalFormat df = new DecimalFormat(price.getAppCurrency().getSymbol() + "#,##0.00");
		redirectAttributes.addFlashAttribute("resultPrice", df.format(price.getAmount()));
		
		int legNum = _processPricer.getLegNumber();
		int payIndex = _processPricer.getPayLegIndex();
		
		for (int legIndex = 0; legIndex < legNum; legIndex++){
			Money legPrice = _processPricer.getLegPrice(legIndex);
			String payRcv = "Rcv";
			if (legIndex == payIndex){
				payRcv = "Pay";
			}
			redirectAttributes.addFlashAttribute("result" + payRcv + "LegPrice", 
					df.format(legPrice.getAmount()));
		}
		
		//ProductInfoPricerModel
		ProductInfo productInfo = _processPricer.getProductInfo();
		ProductInfoPricerModel productInfoModel = new ProductInfoPricerModel();
		productInfoModel.setIssueDt(productInfo.getIssueDate().getDt());
		productInfoModel.setMrtyDt(productInfo.getMaturityDate().getDt());
		productInfoModel.setCurrency(productInfo.getCurrency().getSymbol());
		productInfoModel.setHasPrincipalExchange(productInfo.hasPrincipalExchange());	
		
		redirectAttributes.addFlashAttribute("productInfoModel", productInfoModel);
		
		//LegPricerModelForm
		LegPricerModelForm legPricerModelForm = new LegPricerModelForm();		
		List<LegInfoPricerModel> legInfoPricerModels = new ArrayList<>();
		List<LegPeriodPricerModel> legPeriodPricerModels = new ArrayList<>();
		
		for (int legIndex = 0; legIndex < legNum; legIndex++){
			int undNum = _processPricer.getUnderlyingNum(legIndex);
			
			LegInfoPricerModel legInfoPricerModel = new LegInfoPricerModel();
			LegPeriodPricerModel legPeriodPricerModel = new LegPeriodPricerModel();
						
			//legDataInfo
			LegDataInfo legDataInfo = _processPricer.getLegDataInfo(legIndex);
			legInfoPricerModel.setNextCouponDt(legDataInfo.getNextCouponDate().getDt());
			legInfoPricerModel.setNextCouponRate(legDataInfo.getNextCouponRate());
			legInfoPricerModel.setCumulatedAccrualDays(legDataInfo.getCumulatedAccrualDays());
			legInfoPricerModel.setCumulatedAvgCoupon(legDataInfo.getCumulatedAvgCoupon());
			
			//legCouponInfo
			LegCouponInfo legCouponInfo = _processPricer.getLegCouponInfo(legIndex);
			legInfoPricerModel.setPayRcv(legCouponInfo.getPayRcv().toString());
			legInfoPricerModel.setUnderlyingType(legCouponInfo.getUnderlyingType().toString());
			legInfoPricerModel.setConditionType(legCouponInfo.getConditionType().toString());
			legInfoPricerModel.setHasCap(legCouponInfo.hasCap());
			legInfoPricerModel.setHasFloor(legCouponInfo.hasFloor());
			//underlyingInfo
			UnderlyingInfoPricerModel[] underlyingInfoPricerModels = new UnderlyingInfoPricerModel[undNum]; 
			for (int undIndex = 0; undIndex < undNum; undIndex++){
				UnderlyingInfo underlyingInfo = legCouponInfo.getUnderlyingInfo(undIndex);				
				underlyingInfoPricerModels[undIndex] = new UnderlyingInfoPricerModel();
				if (underlyingInfo instanceof RateUnderlyingInfo){
					//Rate
					underlyingInfoPricerModels[undIndex].setRateType(
							((RateUnderlyingInfo)underlyingInfo).getRateType().toString());
					underlyingInfoPricerModels[undIndex].setSwapCouponFrequency(
							((RateUnderlyingInfo)underlyingInfo).getSwapCouponFrequency().toString());
					underlyingInfoPricerModels[undIndex].setTenor(
							((RateUnderlyingInfo)underlyingInfo).getTenor());
					underlyingInfoPricerModels[undIndex].setUnderlyingInfoFlag("Rate");
					
				} else if (underlyingInfo instanceof EquityUnderlyingInfo){
					//Equity
					underlyingInfoPricerModels[undIndex].setBasePrice(
							((EquityUnderlyingInfo)underlyingInfo).getBasePrice());
					underlyingInfoPricerModels[undIndex].setRiskFreeType(
							((EquityUnderlyingInfo)underlyingInfo).getRiskFreeType().toString());
					underlyingInfoPricerModels[undIndex].setUnderlyingInfoFlag("Equity");
				} else if (underlyingInfo instanceof FXUnderlyingInfo){
					//FX
					underlyingInfoPricerModels[undIndex].setUnderlyingInfoFlag("FX");
				}
				
				underlyingInfoPricerModels[undIndex].setModelType(underlyingInfo.getModelType().toString());
				
			}
			legInfoPricerModel.setUnderlyingInfoPricerModels(underlyingInfoPricerModels);
			
			//legScheduleInfo
			LegScheduleInfo legScheduleInfo = _processPricer.getLegScheduleInfo(legIndex);
			legInfoPricerModel.setDcf(legScheduleInfo.getDCF().toString());
			
			
			
			//legAmortizationInfo
			LegAmortizationInfo legAmortizationInfo = _processPricer.getLegAmortizationInfo(legIndex);
			legInfoPricerModel.setPrincipal(legAmortizationInfo.getPrincipal().getAmount());
			
			legInfoPricerModels.add(legInfoPricerModel);
			
			//legPeriodPricerModel
			PaymentPeriod[] paymentPeriods = legScheduleInfo.getPaymentPeriods();
			CouponType[] couponTypes = legCouponInfo.getCouponType();
			
//			legCouponInfo.getUpperLimits();
//			legCouponInfo.getLowerLimits();
			int periodNum = paymentPeriods.length;
			
			String[] couponTypeStr = new String[periodNum];
			String[] startDt = new String[periodNum];
			String[] endDt = new String[periodNum];
			String[] paymentDt = new String[periodNum];
			
			for (int periodIndex = 0; periodIndex < periodNum; periodIndex++){
				startDt[periodIndex] = paymentPeriods[periodIndex].getStartDate().getDt();
				endDt[periodIndex] = paymentPeriods[periodIndex].getEndDate().getDt();
				paymentDt[periodIndex] = paymentPeriods[periodIndex].getPaymentDate().getDt();
				couponTypeStr[periodIndex] = couponTypes[periodIndex].toString();
			}
			legPeriodPricerModel.setCouponType(couponTypeStr);
			legPeriodPricerModel.setStartDt(startDt);
			legPeriodPricerModel.setEndDt(endDt);
			legPeriodPricerModel.setPaymentDt(paymentDt);
			legPeriodPricerModel.setCap(legCouponInfo.getCap());
			legPeriodPricerModel.setFloor(legCouponInfo.getFloor());			
			legPeriodPricerModel.setSpreads(legCouponInfo.getSpreads());
			legPeriodPricerModel.setInCouponRates(legCouponInfo.getInCouponRates());
			legPeriodPricerModel.setOutCouponRates(legCouponInfo.getOutCouponRates());
			legPeriodPricerModel.setLeverages(legCouponInfo.getLeverageAll());
			legPeriodPricerModel.setUpperLimits(legCouponInfo.getUpperLimits());
			legPeriodPricerModel.setLowerLimits(legCouponInfo.getLowerLimits());
			
			legPeriodPricerModel.genCoupon(legCouponInfo.getUnderlyingType());
			
			legPeriodPricerModels.add(legPeriodPricerModel);
		}		
		legPricerModelForm.setLegInfoPricerModels(legInfoPricerModels);
		legPricerModelForm.setLegPeriodPricerModels(legPeriodPricerModels);		
		redirectAttributes.addFlashAttribute("legPricerModelForm", legPricerModelForm);
				
		//etc
		redirectAttributes.addFlashAttribute("selectedInstrumentCd", instrumentCd);
		redirectAttributes.addFlashAttribute("processDt", processDt);
		
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "Calculation Done! Instrument Code: " 
				+ instrumentCd 
				+ ", Process Date: " + processDt);
		
		return "redirect:/pricer";		
	}
	
//	@RequestMapping(value = "/pricer/list", method = RequestMethod.GET)
//	public String pricerExecute(
//			@RequestParam("instrumentCd") String instrumentCd,
//			@RequestParam("processDt") String processDt,
//			Model model,
//			final RedirectAttributes redirectAttributes){
//
//		logger.debug("pricerExecute()");
//		
//		Date processDate = Date.valueOf(processDt.replaceAll("-",""));		
//		int monitorFrequency = 1;
//		int simNum = 100;
//		
//		if (_processPricer == null){
//			this.instrumentCd = instrumentCd;
//			this.processDt = processDt;
//			_processPricer = new ProcessPricer(
//					processDate, instrumentCd, monitorFrequency, simNum);
//		} else {
//			if (!this.instrumentCd.equals(instrumentCd)){
//				this.instrumentCd = instrumentCd;
//				this.processDt = processDt;
//				_processPricer = new ProcessPricer(
//						processDate, instrumentCd, monitorFrequency, simNum);
//			} else if (!this.processDt.equals(processDt)){
//				this.instrumentCd = instrumentCd;
//				this.processDt = processDt;
//				_processPricer = new ProcessPricer(
//						processDate, instrumentCd, monitorFrequency, simNum);
//			} else {
//				
//			}
//		}
//		
//		Money price = _processPricer.getPrice();
//		DecimalFormat df = new DecimalFormat(price.getAppCurrency().getSymbol() + "#,##0.00");
//		redirectAttributes.addFlashAttribute("resultPrice", df.format(price.getAmount()));
//		
//		int legNum = _processPricer.getLegNumber();
//		for (int legIndex = 0; legIndex < legNum; legIndex++){
//			Money legPrice = _processPricer.getLegPrice(legIndex);
//			redirectAttributes.addFlashAttribute("resultLegPrice" + legIndex, 
//					df.format(legPrice.getAmount()));
//		}
//		//productInfo
//		ProductInfo productInfo = _processPricer.getProductInfo();
//		ProductInfoPricerModel productInfoModel = new ProductInfoPricerModel();
//		productInfoModel.setIssueDt(productInfo.getIssueDate().getDt());
//		productInfoModel.setMrtyDt(productInfo.getMaturityDate().getDt());
//		productInfoModel.setCurrency(productInfo.getCurrency().getSymbol());
//		productInfoModel.setHasPrincipalExchange(productInfo.hasPrincipalExchange());		
//		redirectAttributes.addFlashAttribute("productInfoModel", productInfoModel);	
//		
//		LegDataInfoPricerModelForm legDataInfoPricerModelForm = new LegDataInfoPricerModelForm();
//		List<LegDataInfoPricerModel> legDataInfoPricerModels = new ArrayList<>();
//		LegCouponInfoPricerModelForm legCouponInfoPricerModelForm = new LegCouponInfoPricerModelForm();
//		List<LegCouponInfoPricerModel> legCouponInfoPricerModels = new ArrayList<>();
//		
//		
//		for (int legIndex = 0; legIndex < legNum; legIndex++){
//			int undNum = _processPricer.getUnderlyingNum(legIndex);
//			
//			//legDataInfo
//			LegDataInfo legDataInfo = _processPricer.getLegDataInfo(legIndex);
//			LegDataInfoPricerModel legDataInfoModel = new LegDataInfoPricerModel();
//			legDataInfoModel.setNextCouponDt(legDataInfo.getNextCouponDate().getDt());
//			legDataInfoModel.setNextCouponRate(legDataInfo.getNextCouponRate());
//			legDataInfoModel.setCumulatedAccrualDays(legDataInfo.getCumulatedAccrualDays());
//			legDataInfoModel.setCumulatedAvgCoupon(legDataInfo.getCumulatedAvgCoupon());
//			legDataInfoPricerModels.add(legDataInfoModel);			
//			
//			//legCouponInfo
//			LegCouponInfo legCouponInfo = _processPricer.getLegCouponInfo(legIndex);
//			LegCouponInfoPricerModel legCouponInfoPricerModel = new LegCouponInfoPricerModel();
//			legCouponInfoPricerModel.setConditionType(legCouponInfo.getConditionType().toString());
//			legCouponInfoPricerModel.setUnderlyingType(legCouponInfo.getUnderlyingType().toString());
//			legCouponInfoPricerModel.setPayRcv(legCouponInfo.getPayRcv().toString());
//			legCouponInfoPricerModel.setSpreads(legCouponInfo.getSpreads());
//			
//			//underlyingInfo
//			UnderlyingInfoPricerModel[] underlyingInfoPricerModels = new UnderlyingInfoPricerModel[undNum]; 
//			for (int undIndex = 0; undIndex < undNum; undIndex++){
//				UnderlyingInfo underlyingInfo = legCouponInfo.getUnderlyingInfo(undIndex);				
//				underlyingInfoPricerModels[undIndex] = new UnderlyingInfoPricerModel();
//				underlyingInfoPricerModels[undIndex].setModelType(underlyingInfo.getModelType().toString());
//				
//			}
//			legCouponInfoPricerModel.setUnderlyingInfoPricerModels(underlyingInfoPricerModels);
//						
//			legCouponInfoPricerModels.add(legCouponInfoPricerModel);
//			
////			//marketInfo
////			int undNum = _processPricer.getUnderlyingNum(legIndex);
////			for (int undIndex = 0; undIndex < undNum; undIndex++){
////				MarketInfo marketInfo = _processPricer.getLegMarketInfo(legIndex, undIndex);
////				MarketInfoPricerModel marketInfoModel = new MarketInfoPricerModel();
////				marketInfoModel.setQuantoCorrelation(marketInfo.getQuantoCorrelation());
////				marketInfoModel.setQuantoVolatility(marketInfo.getQuantoVolatility());
////				redirectAttributes.addFlashAttribute("marketInfoModel" + legIndex + undIndex, marketInfoModel);
////			}			
//		}		
//		
//		legDataInfoPricerModelForm.setLegDataInfoPricerModels(legDataInfoPricerModels);
//		legCouponInfoPricerModelForm.setLegCouponInfoPricerModels(legCouponInfoPricerModels);
//		
//		
//		redirectAttributes.addFlashAttribute("legDataInfoPricerModelForm", legDataInfoPricerModelForm);
//		redirectAttributes.addFlashAttribute("legCouponInfoPricerModelForm", legCouponInfoPricerModelForm);
//		
//		
//		redirectAttributes.addFlashAttribute("selectedInstrumentCd", instrumentCd);
//		redirectAttributes.addFlashAttribute("processDt", processDt);
//		
//		redirectAttributes.addFlashAttribute("css", "success");
//		redirectAttributes.addFlashAttribute("msg", "Calculation Done! Instrument Code: " 
//				+ instrumentCd 
//				+ ", Process Date: " + processDt);
//		
//		return "redirect:/pricer";		
//	}
	
	private void populateModel(Model model){
		//Instrument Code
		List<InstrumentInfo> instrumentInfoList = instrumentInfoService.getLists();
		Map<String, String> instrumentList = new LinkedHashMap<>();
		for (int index = 0; index < instrumentInfoList.size(); index++){
			InstrumentInfo instrumentInfo = instrumentInfoList.get(index);
			String instrumentCd = instrumentInfo.getInstrumentCd();			
			
			instrumentList.put(Integer.toString(index), instrumentCd);			
		}
		model.addAttribute("instrumentList", instrumentList);
		if(!model.containsAttribute("processDt")){
			model.addAttribute("processDt", "2013-12-02");
		}
	}
}
