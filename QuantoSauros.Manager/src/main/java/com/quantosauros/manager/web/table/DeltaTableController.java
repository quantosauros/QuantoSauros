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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quantosauros.manager.model.JSONDataTablesModel;
import com.quantosauros.manager.model.results.DeltaInfo;
import com.quantosauros.manager.model.settings.ProcessInfo;
import com.quantosauros.manager.service.results.DeltaInfoService;
import com.quantosauros.manager.service.settings.ProcessInfoService;

@Controller
public class DeltaTableController {

	private final Logger logger = Logger.getLogger(DeltaTableController.class);
	
	private DeltaInfoService deltaInfoService;
	private ProcessInfoService processInfoService;
	
	@Autowired
	public void setDeltaInfoService(DeltaInfoService deltaInfoService){
		this.deltaInfoService = deltaInfoService;
	}
	
	@Autowired
	public void setProcessInfoService(ProcessInfoService processInfoService){
		this.processInfoService = processInfoService;
	}
	
	@RequestMapping(value = "/tables/delta", method = RequestMethod.GET)
	public String deltaTableIndex(Model model){
		logger.debug("deltaTableIndex()");
		
		populateModel(model);
		
		return "/tables/deltaTable";
	}

	@RequestMapping(value = "/tables/delta/json", method = RequestMethod.GET)
	public @ResponseBody JSONDataTablesModel getDeltaInfoList(
			@RequestParam("procId") String procId,
			Model model) throws Exception {
		
		List<DeltaInfo> list = deltaInfoService.selectAllList(procId);
		
		int iTotalDisplayRecords = list.size();
		int iTotalRecords = list.size();
		
		JSONDataTablesModel priceInfoJsonObject = new JSONDataTablesModel();
		priceInfoJsonObject.setiTotalDisplayRecords(iTotalDisplayRecords);
		priceInfoJsonObject.setiTotalRecords(iTotalRecords);		
				
		priceInfoJsonObject.setAaData(list);
				
		return priceInfoJsonObject;
	}
	
	
	public String genJsonDataTables(String procId){
		List<DeltaInfo> list = deltaInfoService.selectAllList(procId);
		
		int iTotalDisplayRecords = list.size();
		int iTotalRecords = list.size();
		
		JSONDataTablesModel jsonModel = new JSONDataTablesModel();
		jsonModel.setiTotalDisplayRecords(iTotalDisplayRecords);
		jsonModel.setiTotalRecords(iTotalRecords);		
				
		jsonModel.setAaData(list);
		ObjectMapper mapper = new ObjectMapper();
		String result = "";
		try {
			result = mapper.writeValueAsString(jsonModel);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;		
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
	}
}
