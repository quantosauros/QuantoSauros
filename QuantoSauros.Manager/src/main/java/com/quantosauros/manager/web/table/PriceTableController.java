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
import com.quantosauros.manager.model.results.PriceInfo;
import com.quantosauros.manager.service.PriceInfoService;

@Controller
public class PriceTableController {

	private final Logger logger= Logger.getLogger(PriceTableController.class);
	
	private PriceInfoService priceInfoService;
	
	@Autowired
	public void setPriceInfoService(PriceInfoService priceInfoService){
		this.priceInfoService = priceInfoService;
	}
	
	@RequestMapping(value = "/priceTable", method = RequestMethod.GET)
	public String priceTableIndex(Model model){
		logger.debug("priceTableIndex()");
		
		return "/result/PriceList";
	}
	
	@RequestMapping(value = "/priceTable/json", method = RequestMethod.GET)
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
			
}
