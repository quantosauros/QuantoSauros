package com.quantosauros.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.quantosauros.common.calendar.BusinessDayConvention;
import com.quantosauros.common.calendar.Calendar;
import com.quantosauros.common.calendar.SouthKoreaCalendar;
import com.quantosauros.common.date.Date;
import com.quantosauros.manager.chart.CreateHighChart;
import com.quantosauros.manager.dao.DeltaInfoDao;
import com.quantosauros.manager.dao.PriceInfoDao;
import com.quantosauros.manager.model.DeltaInfo;
import com.quantosauros.manager.model.PriceInfo;

@Controller
public class highChartController {

	private DeltaInfoDao deltaInfoDao;
	private PriceInfoDao priceInfoDao;
	
	@Autowired
	public highChartController(
			DeltaInfoDao deltaInfoDao,
			PriceInfoDao priceInfoDao){
		this.deltaInfoDao = deltaInfoDao;
		this.priceInfoDao = priceInfoDao;
	}
	
	@RequestMapping(value = "/priceChart", method = RequestMethod.GET)
	public ModelAndView getPriceView() throws Exception {
		ModelAndView model = new ModelAndView("/chart/PriceChart");
		return model;
	}
	
	@RequestMapping(value = "/deltaChart", method = RequestMethod.GET)
	public ModelAndView getDeltaView() throws Exception {
		ModelAndView model = new ModelAndView("/chart/DeltaChart");
		return model;
	}
	
	@RequestMapping(value = "/json/priceChart", method = RequestMethod.GET)
	public @ResponseBody String getPriceChart(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		String procId = request.getParameter("procId");
		String idxId = request.getParameter("idxId");
		String nonCallCd = request.getParameter("nonCallCd");
		String instrumentCd = request.getParameter("instrumentCd");
		Date startDate = Date.valueOf(request.getParameter("startDate").replace("-", ""));
		Date endDate = Date.valueOf(request.getParameter("endDate").replace("-", ""));
		
		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("procId", procId);
		paramMap.put("idxId", idxId);
		paramMap.put("nonCallCd", nonCallCd);
		paramMap.put("instrumentCd", instrumentCd);		
		paramMap.put("startDate", startDate.getDt());
		paramMap.put("endDate", endDate.getDt());
		
		List<PriceInfo> priceInfoList = priceInfoDao.selectListForChart(paramMap);
		
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
	
	@RequestMapping(value = "/json/deltaChart", method = RequestMethod.GET)
	public @ResponseBody String getDeltaChart(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
//		String procId = "123";
//		String idxId = "123";
//		String[] dt = new String[]{"20150428", "20150429"};
//		String instrumentCd = "APS002";
//		String greekCd = "AAD";		
//		String ircCd = "KRWIRS";
		
		String procId = request.getParameter("procId");
		String idxId = request.getParameter("idxId");		
		String instrumentCd = request.getParameter("instrumentCd");
		String greekCd = request.getParameter("greekCd");		
		String ircCd = request.getParameter("ircCd");		
		Date startDate = Date.valueOf(request.getParameter("startDate").replace("-", ""));
		Date endDate = Date.valueOf(request.getParameter("endDate").replace("-", ""));
		String nonCallCd = request.getParameter("nonCallCd");
		
		Calendar calendar = SouthKoreaCalendar.getCalendar(1);
		Date processDate = calendar.adjustDate(startDate, BusinessDayConvention.FOLLOWING);
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
			HashMap paramMap = new HashMap();
			paramMap.put("procId", procId);
			paramMap.put("idxId", idxId);
			paramMap.put("dt", dateArray.get(seriesIndex));
			paramMap.put("instrumentCd", instrumentCd);
			paramMap.put("greekCd", greekCd);
			paramMap.put("ircCd", ircCd);
			paramMap.put("nonCallCd", nonCallCd);
			
			List<DeltaInfo> list = deltaInfoDao.selectDeltaForChart(paramMap);
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
	
	@RequestMapping(value = "/highChart", method = RequestMethod.GET)
	public ModelAndView getViewTest(){
		
		//data
		String[] xAxisValue = new String[] {
			"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
		
		String[] seriesNames = new String[]{
			"Tokyo", "NewYork", "Seoul", "Hongkong",	
		};
		Number[][] seriesValues = new Number[][]{
			{7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6 },
			{-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5 },
			{-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0 },
			{3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8 },
		};
		String titleStr = "Price Chart by date";
		String subtitleStr = "Source: quantosauros";
		String yAxisTitle = "Price";
		
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
				
		ModelAndView model = new ModelAndView("/chart/HighChart");		
		model.addObject("chart1", highChartJSON);		
		return model;
	}
}
