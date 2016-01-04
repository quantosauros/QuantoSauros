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
import com.quantosauros.common.interestrate.InterestRate;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;
import com.quantosauros.jpl.dto.underlying.EquityUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.FXUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.RateUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;
import com.quantosauros.manager.model.pricer.InterestRateCurveModel;
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
			@RequestParam("pricerType") String pricerType,
			Model model,
			final RedirectAttributes redirectAttributes){

		logger.debug("pricerExecute()");
		
		Date processDate = Date.valueOf(processDt.replaceAll("-",""));		
		int monitorFrequency = 1;
		int simNum = 100;
		
		Date issueDate = Date.valueOf(instrumentInfoService.getOne(instrumentCd).getIssueDt().replace("-",""));
		if (processDate.diff(issueDate) < 0){
			redirectAttributes.addFlashAttribute("css", "danger");
			redirectAttributes.addFlashAttribute("msg", "The Product(" 
					+ instrumentCd + ") is not issued at "
					+ "the process Date(" + processDt + ").");
		} else {			
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
			if (pricerType.equals("price")){
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
			} else if (pricerType.equals("delta")){
				_processPricer.genDelta();
				
				redirectAttributes.addFlashAttribute("resultDelta", 
						_processPricer.getDelta());
				redirectAttributes.addFlashAttribute("resultRiskFactor", 
						_processPricer.getRiskFactors());
				redirectAttributes.addFlashAttribute("resultVertex", 
						_processPricer.getVertex());
				
			}			
			
			//RESULT Info	
			int legNum = _processPricer.getLegNumber();
			
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
				
//				legCouponInfo.getUpperLimits();
//				legCouponInfo.getLowerLimits();
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
				legPeriodPricerModel.genCondition(legCouponInfo.getConditionType());
				
				legPeriodPricerModels.add(legPeriodPricerModel);
			}		
			legPricerModelForm.setLegInfoPricerModels(legInfoPricerModels);
			legPricerModelForm.setLegPeriodPricerModels(legPeriodPricerModels);		
			redirectAttributes.addFlashAttribute("legPricerModelForm", legPricerModelForm);
			
			//Market Information
			RateMarketInfo discountMarketInfo = _processPricer.getDiscountMarketInfo();
			InterestRateCurve discountRateCurve = discountMarketInfo.getInterestRateCurve();
			
			InterestRateCurveModel discountRateCurveModel = new InterestRateCurveModel();
			discountRateCurveModel.setCompoundFreq(discountRateCurve.getCompoundingFrequency().toString());
			discountRateCurveModel.setDate(discountRateCurve.getDate().toString());
			discountRateCurveModel.setDcf(discountRateCurve.getDayCountFraction().toString());
			InterestRate[] discountIrRates = discountRateCurve.getSpotRates();
			int irNum = discountIrRates.length;
			String[] rateTypes = new String[irNum];
			double[] rates = new double[irNum];
			String[] vertex = new String[irNum];
			for (int irIndex = 0; irIndex < irNum; irIndex++){
				rates[irIndex] = discountIrRates[irIndex].getRate();
				vertex[irIndex] = discountIrRates[irIndex].getVertex().toString();
				rateTypes[irIndex] = discountIrRates[irIndex].getFactorCode();
			}			
			discountRateCurveModel.setRateType(rateTypes);
			discountRateCurveModel.setRate(rates);
			discountRateCurveModel.setVertex(vertex);
			redirectAttributes.addFlashAttribute("discountRateCurveModel", discountRateCurveModel);
			
			//etc
			redirectAttributes.addFlashAttribute("selectedInstrumentCd", instrumentCd);
			redirectAttributes.addFlashAttribute("processDt", processDt);
			
			redirectAttributes.addFlashAttribute("css", "success");
			redirectAttributes.addFlashAttribute("msg", "Calculation Done! Instrument Code: " 
					+ instrumentCd 
					+ ", Process Date: " + processDt);
			
		}
				
		return "redirect:/pricer";		
	}
	
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
