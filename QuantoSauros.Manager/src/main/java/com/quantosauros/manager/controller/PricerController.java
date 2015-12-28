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
	
	private ProcessPricer _processPricer;
	
	@RequestMapping(value = "/pricer", method = RequestMethod.GET)
	public ModelAndView getView(HttpServletRequest request, 
			HttpServletResponse response){
		
		ModelAndView model = new ModelAndView("/pricer");
		
		return model;
	}
	
	@RequestMapping(value = "/pricer/list", method = RequestMethod.GET)
	public void getInstrumentLists(HttpServletRequest request, 
			HttpServletResponse response){

		Date processDate = Date.valueOf("20131209");
		String instrumentCd = "APSSWAP001";
		int monitorFrequency = 1;
		int simNum = 100;
		
		if (_processPricer == null){
			_processPricer = new ProcessPricer(
					processDate, instrumentCd, monitorFrequency, simNum);
		}		
						
		_processPricer.execute();
		
	}
}
