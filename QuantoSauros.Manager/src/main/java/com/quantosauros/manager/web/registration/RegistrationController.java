package com.quantosauros.manager.web.registration;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quantosauros.manager.model.products.ProductInfoModel;
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
		
		return "/registration/productRegistration";
	}
	
	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String registrationInsert(
			@ModelAttribute("productInfoModel") ProductInfoModel productInfoModel,
			Model model, final RedirectAttributes redirectAttributes){
		
		logger.debug("registrationInsert()");
		
		ProductInfoModel productInfo = productRegistrationService.getProductInfoModelByInstrumentCd(productInfoModel.getInstrumentCd());
		
		if (productInfo == null){
			redirectAttributes.addFlashAttribute("css", "success");		
			redirectAttributes.addFlashAttribute("msg", "Product registered successfully!");
			
			productRegistrationService.register(productInfoModel);
		} else {
			redirectAttributes.addFlashAttribute("css", "danger");		
			redirectAttributes.addFlashAttribute("msg", "The product code already exists!");
		}
		
		return "redirect:/register";
	}
	
	
}
