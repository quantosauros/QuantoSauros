package com.quantosauros.manager.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CalculatorController {

	@RequestMapping(value="/calculator", method = RequestMethod.GET)
	public ModelAndView executeCalculator(){
		
		ModelAndView model = new ModelAndView("/calculator/Calculator");
		
		return model;
	}
	
	@RequestMapping(value="/calculator/execute", method = RequestMethod.POST)
	public String execute(HttpServletRequest request,
			   HttpServletResponse response){
		
		String startDt = request.getParameter("startDt").replaceAll("-", "");
		String endDt = request.getParameter("endDt").replaceAll("-", "");
		//Date startDate = Date.valueOf(startDt);
		//Date endDate = Date.valueOf(endDt);
		String procId = request.getParameter("procId");
		String idxId = request.getParameter("idxId");
		int simNum = Integer.parseInt(request.getParameter("simNum"));
		int monitorFrequency = Integer.parseInt(request.getParameter("monitorFreq"));
//		Calendar cal = SouthKoreaCalendar.getCalendar(1);
//		Date processDate = cal.adjustDate(startDt, BusinessDayConvention.FOLLOWING);
//		while (!endDt.equals(processDate)){
//			System.out.println("processDate: " + processDate.getDt());
////			ProcessPrices process = new ProcessPrices(processDate, procId, idxId);
////			process.setSimNum(simNum);
////			process.setMonitorFrequency(monitorFrequency);			
////			process.setSpecificInstrument("APS002");
////			process.execute();
//			
//			processDate = cal.adjustDate(
//					processDate.plusDays(1), BusinessDayConvention.FOLLOWING);
//		}		
		
		System.out.println("startDt: " + startDt + 
				", endDt: " + endDt + 
				", procId: " + procId +
				", idxId: " + idxId +
				", simNum: " + simNum +
				", monitorFrequency: " + monitorFrequency);
		return "redirect:/calculator";
	}
}
