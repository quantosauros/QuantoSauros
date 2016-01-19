package com.quantosauros.manager.web.chart;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.SouthKoreaCalendar;
import com.quantosauros.common.date.Date;
import com.quantosauros.manager.chart.CreateHighChart;
import com.quantosauros.manager.model.products.InstrumentInfoModel;
import com.quantosauros.manager.model.results.DeltaInfo;
import com.quantosauros.manager.model.settings.ProcessInfoModel;
import com.quantosauros.manager.service.products.InstrumentInfoService;
import com.quantosauros.manager.service.results.DeltaInfoService;
import com.quantosauros.manager.service.settings.ProcessInfoService;

@Controller
public class DeltaChartController {

	private final Logger logger = Logger.getLogger(DeltaChartController.class);
	
	private DeltaInfoService deltaInfoService;
	private ProcessInfoService processInfoService;
	private InstrumentInfoService instrumentInfoService;
	
	@Autowired
	public void setDeltaInfoService(DeltaInfoService deltaInfoService){
		this.deltaInfoService = deltaInfoService;
	}
	
	@Autowired
	public void setProcessInfoService(ProcessInfoService processInfoService){
		this.processInfoService = processInfoService;
	}
	@Autowired
	public void setInstrumentInfoService(InstrumentInfoService instrumentInfoService){
		this.instrumentInfoService = instrumentInfoService;
	}
	@RequestMapping(value = "/deltaChart", method = RequestMethod.GET)
	public String deltaChartIndex(Model model){
		logger.debug("deltaChartIndex()");
		populateModel(model);
		return "/chart/DeltaChart";
	}
	
	@RequestMapping(value = "/deltaChart2", method = RequestMethod.GET)
	public String deltaChart2Index(Model model){
		logger.debug("deltaChart2Index()");
		populateModel(model);
		return "/chart/DeltaChart2";
	}
	
	@RequestMapping(value = "/deltaChart/json", method = RequestMethod.GET)
	public @ResponseBody String getDeltaChart(
			@RequestParam("procId") String procId,
			@RequestParam("nonCallCd") String nonCallCd,
			@RequestParam("instrumentCd") String instrumentCd,
			@RequestParam("ircCd") String ircCd,
			@RequestParam("greekCd") String greekCd,
			@RequestParam("startDate") String startDt,
			@RequestParam("endDate") String endDt,
			Model model) throws Exception {
		
		Date startDate = Date.valueOf(startDt.replace("-", ""));
		Date endDate = Date.valueOf(endDt.replace("-", ""));
		
		Calendar calendar = SouthKoreaCalendar.getCalendar(1);
		Date processDate = calendar.adjustDate(startDate, 
				BusinessDayConvention.FOLLOWING);
		ArrayList<String> dateArray = new ArrayList<>(); 
		dateArray.add(processDate.getDt());
		while (!endDate.equals(processDate)){			
			processDate = calendar.adjustDate(
					processDate.plusDays(1), BusinessDayConvention.FOLLOWING);
			dateArray.add(processDate.getDt());
		}		
		
		int seriesNum = dateArray.size();
		Number[][] seriesValues = new Number[seriesNum][];		
		String[] seriesNames = new String[seriesNum];
		String[] xAxisValue = null;
		for (int seriesIndex = 0; seriesIndex < seriesNum; seriesIndex++){
			String dt = dateArray.get(seriesIndex); 
			List<DeltaInfo> list = deltaInfoService.selectDeltaForChart(procId, 
					dt, instrumentCd, greekCd, ircCd, nonCallCd);
			
			int valueNum = list.size();
			seriesValues[seriesIndex] = new Number[valueNum];
			
			seriesNames[seriesIndex] = ircCd + ", " + dateArray.get(seriesIndex);			
			if (seriesIndex == 0){
				xAxisValue = new String[valueNum];
			}			
			for (int valueIndex = 0; valueIndex < valueNum; valueIndex++){
				if (seriesIndex == 0){
					xAxisValue[valueIndex] = list.get(valueIndex).getMrtyCd();
				}				
				seriesValues[seriesIndex][valueIndex] = list.get(valueIndex).getGreek();	
			}			
		}
		
		String titleStr = "DELTA CHART by TENOR";
		String subtitleStr = "Source: quantosauros";
		String yAxisTitle = "VALUES";
		
		CreateHighChart highChart = new CreateHighChart(SeriesType.AREA);
		highChart.setTitle(titleStr);
		highChart.setSubTitle(subtitleStr);
		highChart.setLegend();
		highChart.setXAxisValue(xAxisValue);
		highChart.setYAxisValue(yAxisTitle);
		highChart.setSeriesNames(seriesNames);
		highChart.setSeriesValues(seriesValues);
		
		String renderTo = "chart_1";
		String highChartJSON = highChart.getChart(renderTo);
				
		return highChartJSON;
	}
		
	@RequestMapping(value = "/deltaChart2/json", method = RequestMethod.GET)
	public @ResponseBody String getDeltaChart2(
			@RequestParam("procId") String procId,
			@RequestParam("nonCallCd") String nonCallCd,
			@RequestParam("instrumentCd") String instrumentCd,
			@RequestParam("ircCd") String ircCd,
			@RequestParam("greekCd") String greekCd,
			@RequestParam("startDate") String startDt,
			@RequestParam("endDate") String endDt,
			Model model) throws Exception {
		
		startDt = startDt.replace("-", "");
		endDt = endDt.replace("-", "");
		
		List<String> dateArray = deltaInfoService.selectMrtyCd(instrumentCd, ircCd);
		
		int seriesNum = dateArray.size();
		Number[][] seriesValues = new Number[seriesNum][];		
		String[] seriesNames = new String[seriesNum];
		String[] xAxisValue = null;
		for (int seriesIndex = 0; seriesIndex < seriesNum; seriesIndex++){
			String mrtyCd = dateArray.get(seriesIndex);
			List<DeltaInfo> list = deltaInfoService.selectDeltaForChart2(procId, 
					greekCd, ircCd, mrtyCd, nonCallCd, instrumentCd);
			
			int valueNum = list.size();
			seriesValues[seriesIndex] = new Number[valueNum];
			
			seriesNames[seriesIndex] = dateArray.get(seriesIndex);			
			if (seriesIndex == 0){
				xAxisValue = new String[valueNum];
			}			
			for (int valueIndex = 0; valueIndex < valueNum; valueIndex++){
				if (seriesIndex == 0){
					xAxisValue[valueIndex] = list.get(valueIndex).getProcessDt();
				}				
				seriesValues[seriesIndex][valueIndex] = list.get(valueIndex).getGreek();	
			}			
		}
		
		String titleStr = "DELTA CHART of Factor Code by Time";
		String subtitleStr = "Source: quantosauros";
		String yAxisTitle = "VALUES";
		
		CreateHighChart highChart = new CreateHighChart(SeriesType.LINE);
		highChart.setTitle(titleStr);
		highChart.setSubTitle(subtitleStr);
		highChart.setLegend();
		highChart.setXAxisValue(xAxisValue);
		highChart.setYAxisValue(yAxisTitle);
		highChart.setSeriesNames(seriesNames);
		highChart.setSeriesValues(seriesValues);
		
		String renderTo = "chart_1";
		String highChartJSON = highChart.getChart(renderTo);
				
		return highChartJSON;	
		
	}
	
	private void populateModel(Model model){
		//ProcessInfoList
		List<ProcessInfoModel> processInfoList = processInfoService.selectProcessInfo();
		Map<String, String> processList = new LinkedHashMap<>();
		for (int index = 0; index < processInfoList.size(); index++){
			ProcessInfoModel processInfo = processInfoList.get(index);
			String procId = processInfo.getProcId();
			String procNM = processInfo.getProcNM();
			
			processList.put(procId, procId + ". " + procNM);			
		}
		model.addAttribute("processList", processList);		
		//Instrument Code
		List<InstrumentInfoModel> instrumentInfoModelList = instrumentInfoService.getLists();
		Map<String, String> instrumentList = new LinkedHashMap<>();
		for (int index = 0; index < instrumentInfoModelList.size(); index++){
			InstrumentInfoModel instrumentInfoModel = instrumentInfoModelList.get(index);
			String instrumentCd = instrumentInfoModel.getInstrumentCd();			
			
			instrumentList.put(instrumentCd, instrumentCd);			
		}
		model.addAttribute("instrumentList", instrumentList);
		if(!model.containsAttribute("processDt")){
			model.addAttribute("processDt", "2013-12-02");
		}
	}
	
	
}
