package com.quantosauros.manager.web.registration;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quantosauros.manager.model.products.ProductInfoModel;
import com.quantosauros.manager.model.products.ProductLegModel;
import com.quantosauros.manager.model.products.ProductModel;
import com.quantosauros.manager.model.products.ProductOptionScheduleModel;
import com.quantosauros.manager.model.products.ProductScheduleModel;
import com.quantosauros.manager.service.products.ProductRegistrationService;
import com.quantosauros.manager.web.chart.DeltaChartController;

@Controller
public class RegistrationController {

	private final Logger logger = Logger.getLogger(DeltaChartController.class);
	
	private ProductRegistrationService productRegistrationService;

	@Autowired
	public void setProductRegistrationService(ProductRegistrationService productRegistrationService){
		this.productRegistrationService = productRegistrationService;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registrationMain(Model model){
		
		logger.debug("pricerIndex()");
		
		ProductModel productModel = new ProductModel();
		model.addAttribute("productModel", productModel);
		
		return "/registration/productRegistration";
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String registrationInsert(
			@ModelAttribute("productModel") ProductModel productModel,
			BindingResult bindingResult,
			Model model, final RedirectAttributes redirectAttributes){
		
		logger.debug("registrationInsert()");
		
		redirectAttributes.addFlashAttribute("css", "success");		 
		redirectAttributes.addFlashAttribute("msg", "Product registered successfully!");
		
		ProductInfoModel productInfoModel = productModel.getProductInfoModel();
		ProductLegModel[] productLegModels = productModel.getProductLegModels();
		ProductScheduleModel[][] productScheduleModels = productModel.getProductScheduleModels();
		ProductOptionScheduleModel[] productOptionScheduleModels = productModel.getProductOptionScheduleModels();
		
		String instrumentCd = productInfoModel.getInstrumentCd();
		
		int legNum = productLegModels.length;
		for (int legIndex = 0; legIndex < legNum; legIndex++){
			String legPrincipal = productLegModels[legIndex].getNotionalPrincipal();
			
			productLegModels[legIndex].setInstrumentCd(instrumentCd);
			String payRcvCd = null;
			if (legIndex == 0){
				payRcvCd = "P";
			} else {
				payRcvCd = "R";
			}
			productLegModels[legIndex].setPayRcvCd(payRcvCd);
			
			//Product Schedule
			int periodNum = productScheduleModels[legIndex].length;
			for (int periodIndex = 0; periodIndex < periodNum; periodIndex++){				
				productScheduleModels[legIndex][periodIndex].setInstrumentCd(instrumentCd);
				productScheduleModels[legIndex][periodIndex].setPayRcvCd(payRcvCd);
				productScheduleModels[legIndex][periodIndex].setCouponResetDt(
						productScheduleModels[legIndex][periodIndex].getCouponStrtDt());
				productScheduleModels[legIndex][periodIndex].setPrincipal(legPrincipal);
				
			}
		}
				
		//Product Option Schedule
		if (productOptionScheduleModels != null){
			int optionNum = productOptionScheduleModels.length;
			for (int optionIndex = 0; optionIndex < optionNum; optionIndex++){				
				productOptionScheduleModels[optionIndex].setInstrumentCd(instrumentCd);
			}
		}
		
		productRegistrationService.register(productInfoModel, productLegModels, productScheduleModels, productOptionScheduleModels);
		
		return "redirect:/register";
	}
	
}
