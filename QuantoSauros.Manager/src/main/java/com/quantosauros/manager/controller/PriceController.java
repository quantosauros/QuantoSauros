package com.quantosauros.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PriceController {

	@RequestMapping(value = "/pricelist", method = RequestMethod.GET)
	public ModelAndView getPriceList(){
		
		ModelAndView model = new ModelAndView("/result/PriceList");
		
		return model;
	}
	
	@RequestMapping(value = "/priceResult", method = RequestMethod.GET)
	public ModelAndView getDetailResult(){
		
		ModelAndView model = new ModelAndView("/result/PriceResult");
		
		return model;
	}
	
	@RequestMapping(value = "/priceExecute", method = RequestMethod.GET)
	public ModelAndView execute(HttpServletRequest request, 
			HttpServletResponse response){
				
		String procId = request.getParameter("procId");
		String idxId = request.getParameter("idxId");
				
		ModelAndView model = getDetailResult();
				
		model.addObject("procId", procId);
		model.addObject("idxId", idxId);
				
		return model;
	}
}
