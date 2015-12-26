package com.quantosauros.manager.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.quantosauros.batch.process.ProcessPricer;
import com.quantosauros.common.date.Date;

@Controller
public class PricerController {

	@RequestMapping(value = "/pricer", method = RequestMethod.GET)
	public ModelAndView execute(HttpServletRequest request, 
			HttpServletResponse response){
		
		Date processDate = Date.valueOf("20150428");
		String instrumentCd = "APS002";
		int monitorFrequency = 1;
		int simNum = 1000;
		
		ProcessPricer processPricer = new ProcessPricer(
				processDate, instrumentCd, monitorFrequency, simNum);
						
		processPricer.execute();
		
		ModelAndView model = new ModelAndView("/index");
				
		return model;
	}
	
}
