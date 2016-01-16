package com.quantosauros.manager.web.registration;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.quantosauros.manager.service.products.ProductRegistrationService;
import com.quantosauros.manager.web.chart.DeltaChartController;

@Controller
public class RegistrationController {

	private final Logger logger = Logger.getLogger(DeltaChartController.class);
	
	private ProductRegistrationService productRegistrationService;
		
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registrationMain(Model model){
		
		logger.debug("pricerIndex()");
		
		return "/registration/productRegistration";
	}
	
	
	
}
