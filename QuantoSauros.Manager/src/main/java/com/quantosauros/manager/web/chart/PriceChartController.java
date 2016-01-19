package com.quantosauros.manager.web.chart;

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
import com.quantosauros.manager.chart.CreateHighChart;
import com.quantosauros.manager.model.products.InstrumentInfoModel;
import com.quantosauros.manager.model.results.PriceInfo;
import com.quantosauros.manager.model.settings.ProcessInfoModel;
import com.quantosauros.manager.service.products.InstrumentInfoService;
import com.quantosauros.manager.service.results.PriceInfoService;
import com.quantosauros.manager.service.settings.ProcessInfoService;

@Controller
public class PriceChartController {

	private final Logger logger = Logger.getLogger(PriceChartController.class);

	private PriceInfoService priceInfoService;
	private ProcessInfoService processInfoService;
	private InstrumentInfoService instrumentInfoService;
	
	@Autowired
	public void setPriceInfoService(PriceInfoService priceInfoService){
		this.priceInfoService = priceInfoService;
	}
	
	@Autowired
	public void setProcessInfoService(ProcessInfoService processInfoService){
		this.processInfoService = processInfoService;
	}
	@Autowired
	public void setInstrumentInfoService(InstrumentInfoService instrumentInfoService){
		this.instrumentInfoService = instrumentInfoService;
	}
	
	@RequestMapping(value = "/priceChart", method = RequestMethod.GET)
	public String priceChartIndex(Model model){
		logger.debug("priceChartIndex()");
		populateModel(model);
		return "/chart/PriceChart";
	}
	
	@RequestMapping(value = "/priceChart/json", method = RequestMethod.GET)
	public @ResponseBody String getPriceChart(
			@RequestParam("procId") String procId,
			@RequestParam("nonCallCd") String nonCallCd,
			@RequestParam("instrumentCd") String instrumentCd,
			@RequestParam("startDate") String startDt,
			@RequestParam("endDate") String endDt,
			Model model) throws Exception {
		
		startDt = startDt.replace("-", "");
		endDt = endDt.replace("-", "");
		
		List<PriceInfo> priceInfoList = priceInfoService.selectListForChart(procId,
				nonCallCd, instrumentCd, startDt, endDt);
		
		int seriesNum = 3;
		int valueNum = priceInfoList.size();
		Number[][] seriesValues = new Number[seriesNum][valueNum];		
		String[] seriesNames = new String[]{
			"PRICE", "PAYLEG", "RCVLEG" 
		};
		String[] xAxisValue = new String[valueNum];
		
		for (int valueIndex = 0; valueIndex < valueNum; valueIndex++){			
			xAxisValue[valueIndex] = priceInfoList.get(valueIndex).getProcessDt();
							
			seriesValues[0][valueIndex] = priceInfoList.get(valueIndex).getNonFormattedPrice();
			seriesValues[1][valueIndex] = priceInfoList.get(valueIndex).getNonFormattedPayPrice();
			seriesValues[2][valueIndex] = priceInfoList.get(valueIndex).getNonFormattedRcvPrice();
		}
		
		String titleStr = "PRICE CHART by Process Dates";
		String subtitleStr = "Source: quantosauros";
		String yAxisTitle = "Price";
		
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
