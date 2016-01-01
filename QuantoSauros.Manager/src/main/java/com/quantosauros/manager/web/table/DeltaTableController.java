package com.quantosauros.manager.web.table;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quantosauros.manager.model.JSONDataTablesModel;
import com.quantosauros.manager.model.results.DeltaInfo;
import com.quantosauros.manager.service.results.DeltaInfoService;

@Controller
public class DeltaTableController {

	private final Logger logger = Logger.getLogger(DeltaTableController.class);
	
	private DeltaInfoService deltaInfoService;
	
	@Autowired
	public void setDeltaInfoService(DeltaInfoService deltaInfoService){
		this.deltaInfoService = deltaInfoService;
	}
	
	@RequestMapping(value = "/deltaTable", method = RequestMethod.GET)
	public String deltaTableIndex(){
		logger.debug("deltaTableIndex()");
		
		return "/result/DeltaList";
	}

	@RequestMapping(value = "/deltaTable/json", method = RequestMethod.GET)
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
}
