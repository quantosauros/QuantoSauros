package com.quantosauros.manager.web.pricer;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quantosauros.batch.process.ProcessPricer;
import com.quantosauros.common.currency.Money;
import com.quantosauros.common.date.Date;
import com.quantosauros.jpl.dto.ProductInfo;
import com.quantosauros.manager.model.pricer.ProductInfoPricerModel;
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
	
	@RequestMapping(value = "/pricer/list", method = RequestMethod.GET)
	public String getInstrumentLists(
			@RequestParam("instrumentCd") String instrumentCd,
			@RequestParam("processDt") String processDt,
			Model model,
			final RedirectAttributes redirectAttributes){

		logger.debug("deltaChartIndex()");
		
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
		for (int legIndex = 0; legIndex < legNum; legIndex++){
			Money legPrice = _processPricer.getLegPrice(legIndex);
			redirectAttributes.addFlashAttribute("resultLegPrice" + legIndex, 
					df.format(legPrice.getAmount()));
		}
				
		ProductInfo productInfo = _processPricer.getProductInfo();
		ProductInfoPricerModel productInfoModel = new ProductInfoPricerModel();
		productInfoModel.setIssueDt(productInfo.getIssueDate().getDt());
		productInfoModel.setMrtyDt(productInfo.getMaturityDate().getDt());
		productInfoModel.setCurrency(productInfo.getCurrency().getSymbol());
		productInfoModel.setHasPrincipalExchange(productInfo.hasPrincipalExchange());
		
		redirectAttributes.addFlashAttribute("productInfoModel", productInfoModel);	
		
		
		redirectAttributes.addFlashAttribute("selectedInstrumentCd", instrumentCd);
		redirectAttributes.addFlashAttribute("processDt", processDt);
		
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "Calculation Done! Instrument Code: " 
				+ instrumentCd 
				+ ", Process Date: " + processDt);
		
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
