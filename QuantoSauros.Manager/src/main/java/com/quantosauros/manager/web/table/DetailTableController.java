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
import com.quantosauros.manager.model.results.DetailInfo;
import com.quantosauros.manager.service.results.DetailInfoService;

@Controller
public class DetailTableController {

	private final Logger logger = Logger.getLogger(DetailTableController.class);
	
	private DetailInfoService detailInfoService;
	
	@Autowired
	public void setDetailInfoService(DetailInfoService detailInfoService){
		this.detailInfoService = detailInfoService;
	}
	
	@RequestMapping(value = "/detailTable", method = RequestMethod.GET)
	public String detailTableIndex(){
		logger.debug("detailTableIndex()");
		
		return "/result/DetailList";
	}
	
	@RequestMapping(value = "/detailTable/json", method = RequestMethod.GET)
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
}
