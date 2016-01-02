package com.quantosauros.manager.web.table;

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

import com.quantosauros.manager.model.JSONDataTablesModel;
import com.quantosauros.manager.model.products.InstrumentInfo;
import com.quantosauros.manager.model.results.DetailInfo;
import com.quantosauros.manager.model.settings.ProcessInfo;
import com.quantosauros.manager.service.products.InstrumentInfoService;
import com.quantosauros.manager.service.results.DetailInfoService;
import com.quantosauros.manager.service.settings.ProcessInfoService;

@Controller
public class DetailTableController {

	private final Logger logger = Logger.getLogger(DetailTableController.class);
	
	private DetailInfoService detailInfoService;
	private ProcessInfoService processInfoService;
	private InstrumentInfoService instrumentInfoService;
	
	@Autowired
	public void setDetailInfoService(DetailInfoService detailInfoService){
		this.detailInfoService = detailInfoService;
	}
	@Autowired
	public void setProcessInfoService(ProcessInfoService processInfoService){
		this.processInfoService = processInfoService;
	}
	@Autowired
	public void setInstrumentInfoService(InstrumentInfoService instrumentInfoService){
		this.instrumentInfoService = instrumentInfoService;
	}
	
	@RequestMapping(value = "/tables/detail", method = RequestMethod.GET)
	public String detailTableIndex(Model model){
		logger.debug("detailTableIndex()");
		populateModel(model);
		return "/tables/detailTable";
	}
	
	@RequestMapping(value = "/tables/detail/json", method = RequestMethod.GET)
	public @ResponseBody JSONDataTablesModel getDeltaInfoList(
			@RequestParam("instrumentCd") String instrumentCd,
			@RequestParam("procId") String procId,
			@RequestParam("valueType") String valueType,
			@RequestParam("nonCallCd") String nonCallCd,
			@RequestParam("legType") String legType,
			Model model) throws Exception {
		
		List<DetailInfo> list = detailInfoService.selectList(
				instrumentCd, procId, valueType, nonCallCd, legType);
		
		int iTotalDisplayRecords = list.size();
		int iTotalRecords = list.size();
		JSONDataTablesModel detailInfoJSONObject = new JSONDataTablesModel();
		detailInfoJSONObject.setiTotalDisplayRecords(iTotalDisplayRecords);
		detailInfoJSONObject.setiTotalRecords(iTotalRecords);
		detailInfoJSONObject.setAaData(list);
		return detailInfoJSONObject;
	}	
	
	private void populateModel(Model model){
		List<ProcessInfo> processInfoList = processInfoService.selectProcessInfo();
		Map<String, String> processList = new LinkedHashMap<>();
		for (int index = 0; index < processInfoList.size(); index++){
			ProcessInfo processInfo = processInfoList.get(index);
			String procId = processInfo.getProcId();
			String procNM = processInfo.getProcNM();
			
			processList.put(procId, procId + ". " + procNM);			
		}
		model.addAttribute("processList", processList);
		
		//Instrument Code
		List<InstrumentInfo> instrumentInfoList = instrumentInfoService.getLists();
		Map<String, String> instrumentList = new LinkedHashMap<>();
		for (int index = 0; index < instrumentInfoList.size(); index++){
			InstrumentInfo instrumentInfo = instrumentInfoList.get(index);
			String instrumentCd = instrumentInfo.getInstrumentCd();			
			
			instrumentList.put(instrumentCd, instrumentCd);			
		}
		model.addAttribute("instrumentList", instrumentList);
		
	}
	
}
