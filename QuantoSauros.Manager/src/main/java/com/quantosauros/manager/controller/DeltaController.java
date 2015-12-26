package com.quantosauros.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeltaController {

	@RequestMapping(value = "/deltalist", method = RequestMethod.GET)
	public ModelAndView getPriceList(){
		
		ModelAndView model = new ModelAndView("/result/DeltaList");
		
		return model;
	}
	
	@RequestMapping(value = "/deltaResult", method = RequestMethod.GET)
	public ModelAndView getDetailResult(){
		
		ModelAndView model = new ModelAndView("/result/DeltaResult");
		
		return model;
	}
	
	@RequestMapping(value = "/deltaExecute", method = RequestMethod.GET)
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
