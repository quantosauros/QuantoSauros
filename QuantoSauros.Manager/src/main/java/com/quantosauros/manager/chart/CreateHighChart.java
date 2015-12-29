package com.quantosauros.manager.chart;

import java.util.Arrays;
import java.util.Collections;

import com.googlecode.wickedcharts.highcharts.jackson.JsonRenderer;
import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.HorizontalAlignment;
import com.googlecode.wickedcharts.highcharts.options.Legend;
import com.googlecode.wickedcharts.highcharts.options.LegendLayout;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.PlotLine;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.VerticalAlignment;
import com.googlecode.wickedcharts.highcharts.options.color.HexColor;
import com.googlecode.wickedcharts.highcharts.options.series.Series;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;

public class CreateHighChart {

	//[seriesIndex]
	private String[] seriesNames;
	//[seriesIndex][xIndex]
	private Number[][] seriesValues;
	private SeriesType seriesType;
	private Options options;
	
	public CreateHighChart(SeriesType seriesType) {
		options = new Options();	
		this.seriesType = seriesType;
	}
	
	public void setXAxisValue(String[] xAxisValue){		
	    Axis xAxis = new Axis();
	    xAxis.setCategories(Arrays.asList(xAxisValue));
	    options.setxAxis(xAxis);
	}
	public void setYAxisValue(String yAxisTitle){
		PlotLine plotLines = new PlotLine();
		plotLines.setValue(0f);
		plotLines.setWidth(1);
		plotLines.setColor(new HexColor("#999999"));
		
		Axis yAxis = new Axis();
		yAxis.setTitle(new Title(yAxisTitle));
		yAxis.setPlotLines(Collections.singletonList(plotLines));
		options.setyAxis(yAxis);
	}	
	public void setLegend(){
		Legend legend = new Legend();
	    legend.setLayout(LegendLayout.VERTICAL);
	    legend.setAlign(HorizontalAlignment.RIGHT);
	    legend.setVerticalAlign(VerticalAlignment.TOP);
	    legend.setX(-10);
	    legend.setY(100);
	    legend.setBorderWidth(0);
	    options.setLegend(legend);
	}
	
	public void setSeriesNames(String[] seriesNames){
		this.seriesNames = seriesNames;
	}	
	public void setSeriesValues(Number[][] seriesValues){
		this.seriesValues = seriesValues;
	}
	public void setTitle(String titleStr){		
		//Title
		Title title = new Title(titleStr);
		title.setX(-20);
		options.setTitle(title);
	}
	public void setSubTitle(String subTitleStr){
		//SubTitle
		Title subTitle = new Title(subTitleStr);
	    subTitle.setX(-20);
	    options.setSubtitle(subTitle);
	}
	
	public String getChart(String renderTo){
				
		ChartOptions chartOptions= new ChartOptions();
		chartOptions.setType(seriesType);
		chartOptions.setMarginRight(130);
		chartOptions.setMarginBottom(25);
		chartOptions.setRenderTo(renderTo);
		options.setChartOptions(chartOptions);
		
		for (int seriesIndex = 0; seriesIndex < seriesValues.length; seriesIndex++){
	    	Series<Number> series = new SimpleSeries();
	    	series.setName(seriesNames[seriesIndex]);
		    series.setData(Arrays.asList(seriesValues[seriesIndex]));
		    options.addSeries(series);
	    }
		
		return new JsonRenderer().toJson(options);		
	}
}
