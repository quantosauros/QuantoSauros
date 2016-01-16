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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quantosauros.manager.model.JSONDataTablesModel;
import com.quantosauros.manager.model.results.PriceInfo;
import com.quantosauros.manager.model.settings.ProcessInfoModel;
import com.quantosauros.manager.service.results.PriceInfoService;
import com.quantosauros.manager.service.settings.ProcessInfoService;

@Controller
public class PriceTableController {

	private final Logger logger= Logger.getLogger(PriceTableController.class);
	
	private PriceInfoService priceInfoService;
	private ProcessInfoService processInfoService;
	
	@Autowired
	public void setPriceInfoService(PriceInfoService priceInfoService){
		this.priceInfoService = priceInfoService;
	}
	
	@Autowired
	public void setProcessInfoService(ProcessInfoService processInfoService){
		this.processInfoService = processInfoService;
	}
	
	@RequestMapping(value = "/tables/price", method = RequestMethod.GET)
	public String priceTableIndex(Model model){
		logger.debug("priceTableIndex()");
		
		populateModel(model);		
		
		return "/tables/priceTable";
	}
	
	@RequestMapping(value ="/table/price/result", method = RequestMethod.GET)
	public String priceTableResult(
			@RequestParam("procId") String procId,
			Model model, final RedirectAttributes redirectAttributes){
		
		logger.debug("priceTableResult()");
		
		String jsonResult = genJsonDataTables(procId);		
		redirectAttributes.addAttribute("jsonResult", jsonResult);
				
		return "redirect:/tables/price";
	}
	
	@RequestMapping(value = "/tables/price/json", method = RequestMethod.GET)
	public @ResponseBody JSONDataTablesModel getPriceInfoList(
			@RequestParam("procId") String procId, Model model) throws Exception {
				
		List<PriceInfo> list = priceInfoService.selectAllList(procId);
		
		int iTotalDisplayRecords = list.size();
		int iTotalRecords = list.size();
		
		JSONDataTablesModel priceInfoJsonObject = new JSONDataTablesModel();
		priceInfoJsonObject.setiTotalDisplayRecords(iTotalDisplayRecords);
		priceInfoJsonObject.setiTotalRecords(iTotalRecords);		
				
		priceInfoJsonObject.setAaData(list);
				
		return priceInfoJsonObject;
	}
	
	private String genJsonDataTables(String procId){
		List<PriceInfo> list = priceInfoService.selectAllList(procId);
		
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
		List<ProcessInfoModel> processInfoList = processInfoService.selectProcessInfo();
		Map<String, String> processList = new LinkedHashMap<>();
		for (int index = 0; index < processInfoList.size(); index++){
			ProcessInfoModel processInfo = processInfoList.get(index);
			String procId = processInfo.getProcId();
			String procNM = processInfo.getProcNM();
			
			processList.put(procId, procId + ". " + procNM);			
		}
		model.addAttribute("processList", processList);		
	}
}
