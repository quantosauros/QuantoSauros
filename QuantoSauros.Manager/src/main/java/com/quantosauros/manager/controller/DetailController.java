package com.quantosauros.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DetailController {

	@RequestMapping(value = "/detaillist", method = RequestMethod.GET)
	public ModelAndView getPriceList(){
		
		ModelAndView model = new ModelAndView("/result/DetailList");
		
		return model;
	}
	
	@RequestMapping(value = "/detailResult", method = RequestMethod.GET)
	public ModelAndView getDetailResult(){
		
		ModelAndView model = new ModelAndView("/result/DetailResult");
		
		return model;
	}
	
	@RequestMapping(value = "/detailExecute", method = RequestMethod.GET)
	public ModelAndView execute(HttpServletRequest request, 
			HttpServletResponse response){
		
		String instrumentCd = request.getParameter("instrumentCd");
		String procId = request.getParameter("procId");
		String idxId = request.getParameter("idxId");
		String valueTypeInput = request.getParameter("valueType");		
		String isNonCall = request.getParameter("isNonCall");
		
		ModelAndView model = getDetailResult();
		
		model.addObject("instrumentCd", instrumentCd);
		model.addObject("procId", procId);
		model.addObject("idxId", idxId);
		model.addObject("valueType", valueTypeInput);
		model.addObject("isNonCall", isNonCall);
		
		return model;
	}
}
