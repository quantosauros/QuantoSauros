package com.quantosauros.manager.web.pricer;

import java.text.DecimalFormat;
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
import com.quantosauros.common.hullwhite.HullWhiteParameters;
import com.quantosauros.common.interestrate.InterestRate;
import com.quantosauros.common.interestrate.InterestRateCurve;
import com.quantosauros.jpl.dto.LegAmortizationInfo;
import com.quantosauros.jpl.dto.LegCouponInfo;
import com.quantosauros.jpl.dto.LegDataInfo;
import com.quantosauros.jpl.dto.LegScheduleInfo;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.jpl.dto.market.EquityMarketInfo;
import com.quantosauros.jpl.dto.market.MarketInfo;
import com.quantosauros.jpl.dto.market.RateMarketInfo;
import com.quantosauros.jpl.dto.underlying.EquityUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.FXUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.RateUnderlyingInfo;
import com.quantosauros.jpl.dto.underlying.UnderlyingInfo;
import com.quantosauros.manager.model.pricer.InterestRateCurveModel;
import com.quantosauros.manager.model.pricer.LegInfoPricerModel;
import com.quantosauros.manager.model.pricer.LegPeriodPricerModel;
import com.quantosauros.manager.model.pricer.LegPricerModelForm;
import com.quantosauros.manager.model.pricer.MarketInfoPricerModel;
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
			LegInfoPricerModel[] legInfoPricerModels = new LegInfoPricerModel[legNum];
			LegPeriodPricerModel[] legPeriodPricerModels = new LegPeriodPricerModel[legNum];
			
			//MarketInfoPricerModel
			MarketInfoPricerModel[] marketInfoPricerModels = new MarketInfoPricerModel[legNum];
			
			for (int legIndex = 0; legIndex < legNum; legIndex++){
				int undNum = _processPricer.getUnderlyingNum(legIndex);
				
				LegInfoPricerModel legInfoPricerModel = new LegInfoPricerModel();
				
				//LegInfos
				LegCouponInfo legCouponInfo = _processPricer.getLegCouponInfo(legIndex);
				LegDataInfo legDataInfo = _processPricer.getLegDataInfo(legIndex);
				LegScheduleInfo legScheduleInfo = _processPricer.getLegScheduleInfo(legIndex);
				LegAmortizationInfo legAmortizationInfo = _processPricer.getLegAmortizationInfo(legIndex);
				
				legInfoPricerModel.setNextCouponDt(legDataInfo.getNextCouponDate().getDt());
				legInfoPricerModel.setNextCouponRate(legDataInfo.getNextCouponRate());
				legInfoPricerModel.setCumulatedAccrualDays(legDataInfo.getCumulatedAccrualDays());
				legInfoPricerModel.setCumulatedAvgCoupon(legDataInfo.getCumulatedAvgCoupon());				
				legInfoPricerModel.setPayRcv(legCouponInfo.getPayRcv().toString());
				legInfoPricerModel.setUnderlyingType(legCouponInfo.getUnderlyingType().toString());
				legInfoPricerModel.setConditionType(legCouponInfo.getConditionType().toString());
				legInfoPricerModel.setHasCap(legCouponInfo.hasCap());
				legInfoPricerModel.setHasFloor(legCouponInfo.hasFloor());
				legInfoPricerModel.setDcf(legScheduleInfo.getDCF().toString());
				legInfoPricerModel.setPrincipal(legAmortizationInfo.getPrincipal().getAmount());
				
				//UnderlyingInfoPricerModel & InterestRateCurveModel
				UnderlyingInfoPricerModel[] underlyingInfoPricerModels = new UnderlyingInfoPricerModel[undNum];
				
				marketInfoPricerModels[legIndex] = new MarketInfoPricerModel();
				InterestRateCurveModel[] legIrCurveModel = new InterestRateCurveModel[undNum];
				double[] meanReversion1F = new double[undNum];
				double[] meanReversion2F1 = new double[undNum];
				double[] meanReversion2F2 = new double[undNum];
				double[] correlation = new double[undNum];
				for (int undIndex = 0; undIndex < undNum; undIndex++){
					//UNDERLYING INFO
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
					
					//MARKETINFO					
					MarketInfo legMarketInfo = _processPricer.getLegMarketInfo(legIndex, undIndex);					
					if (legMarketInfo instanceof RateMarketInfo){
						//InterestRateCurveModel
						InterestRateCurve irCurve = ((RateMarketInfo)legMarketInfo).getInterestRateCurve();
						
						legIrCurveModel[undIndex] = new InterestRateCurveModel();
						legIrCurveModel[undIndex].setCompoundFreq(
								irCurve.getCompoundingFrequency().toString());
						legIrCurveModel[undIndex].setDate(irCurve.getDate().toString());
						legIrCurveModel[undIndex].setDcf(irCurve.getDayCountFraction().toString());
						InterestRate[] discountIrRates = irCurve.getSpotRates();
						int irNum = discountIrRates.length;
						String[] rateTypes = new String[irNum];
						double[] rates = new double[irNum];
						String[] vertex = new String[irNum];
						for (int irIndex = 0; irIndex < irNum; irIndex++){
							rates[irIndex] = discountIrRates[irIndex].getRate();
							vertex[irIndex] = discountIrRates[irIndex].getVertex().toString();
							rateTypes[irIndex] = discountIrRates[irIndex].getFactorCode();
						}			
						legIrCurveModel[undIndex].setRateType(rateTypes);
						legIrCurveModel[undIndex].setRate(rates);
						legIrCurveModel[undIndex].setVertex(vertex);
						
						//
						HullWhiteParameters hwParams =((RateMarketInfo)legMarketInfo).getHullWhiteParameters();
						meanReversion1F[undIndex] = hwParams.getHullWhiteVolatility1F();
						meanReversion2F1[undIndex] = hwParams.getHullWhiteVolatility1_2F();
						meanReversion2F2[undIndex] = hwParams.getHullWhiteVolatility2_2F();
						correlation[undIndex] = hwParams.getCorrelation();
					} else if (legMarketInfo instanceof EquityMarketInfo){
						//TODO
						
					}					
					
				}				
				legInfoPricerModel.setUnderlyingInfoPricerModels(underlyingInfoPricerModels);
				legInfoPricerModels[legIndex] = legInfoPricerModel;
								
				marketInfoPricerModels[legIndex].setInterestRateCurveModels(legIrCurveModel);
				marketInfoPricerModels[legIndex].setMeanReversion1F(meanReversion1F);
				marketInfoPricerModels[legIndex].setMeanReversion2F1(meanReversion2F1);
				marketInfoPricerModels[legIndex].setMeanReversion2F2(meanReversion2F2);
				marketInfoPricerModels[legIndex].setCorrelation(correlation);
				
				//LEG PERIOD PRICER MODEL
				LegPeriodPricerModel legPeriodPricerModel = new LegPeriodPricerModel();
				PaymentPeriod[] paymentPeriods = legScheduleInfo.getPaymentPeriods();
				CouponType[] couponTypes = legCouponInfo.getCouponType();
				
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
				
				legPeriodPricerModels[legIndex] = legPeriodPricerModel;
			}		
			legPricerModelForm.setLegInfoPricerModels(legInfoPricerModels);
			legPricerModelForm.setLegPeriodPricerModels(legPeriodPricerModels);	
			legPricerModelForm.setMarketInfoPricerModels(marketInfoPricerModels);
			redirectAttributes.addFlashAttribute("legPricerModelForm", legPricerModelForm);
			
			//Discount Market Information
			MarketInfoPricerModel discMarketInfoPricerModel = new MarketInfoPricerModel();
			RateMarketInfo discountMarketInfo = _processPricer.getDiscountMarketInfo();
			//IR Curve
			InterestRateCurve discountRateCurve = discountMarketInfo.getInterestRateCurve();			
			
			InterestRateCurveModel[] discountRateCurveModel = new InterestRateCurveModel[]{
					new InterestRateCurveModel(),
			};
			discountRateCurveModel[0].setCompoundFreq(discountRateCurve.getCompoundingFrequency().toString());
			discountRateCurveModel[0].setDate(discountRateCurve.getDate().toString());
			discountRateCurveModel[0].setDcf(discountRateCurve.getDayCountFraction().toString());
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
			discountRateCurveModel[0].setRateType(rateTypes);
			discountRateCurveModel[0].setRate(rates);
			discountRateCurveModel[0].setVertex(vertex);			
			//hw params
			HullWhiteParameters discHWParams = discountMarketInfo.getHullWhiteParameters();
			
			discMarketInfoPricerModel.setInterestRateCurveModels(discountRateCurveModel);
			discMarketInfoPricerModel.setMeanReversion1F(new double[]{discHWParams.getMeanReversion1F()});
			discMarketInfoPricerModel.setMeanReversion2F1(new double[]{discHWParams.getMeanReversion1_2F()});
			discMarketInfoPricerModel.setMeanReversion2F2(new double[]{discHWParams.getMeanReversion2_2F()});
			discMarketInfoPricerModel.setCorrelation(new double[]{discHWParams.getCorrelation()});
			
			redirectAttributes.addFlashAttribute("discMarketInfoPricerModel", discMarketInfoPricerModel);
			
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
			
			instrumentList.put(instrumentCd, instrumentCd);			
		}
		model.addAttribute("instrumentList", instrumentList);
		if(!model.containsAttribute("processDt")){
			model.addAttribute("processDt", "2013-12-02");
		}
	}
}
