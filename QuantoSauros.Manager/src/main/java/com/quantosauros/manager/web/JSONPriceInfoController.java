package com.quantosauros.manager.web;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quantosauros.manager.dao.PriceInfoDao;
import com.quantosauros.manager.model.JSONDataTablesModel;
import com.quantosauros.manager.model.PriceInfo;

@Controller
public class JSONPriceInfoController {

	private PriceInfoDao priceInfoDao;
	
	@Autowired
	public JSONPriceInfoController(PriceInfoDao priceInfoDao){
		this.priceInfoDao = priceInfoDao;
	}
	
	@RequestMapping(value = "/json/pricelist", method = RequestMethod.GET)
	public @ResponseBody JSONDataTablesModel getPriceInfoList(
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception{

		String procId = request.getParameter("procId");		
		String nonCallCd = "N";
		
		HashMap paramMap = new HashMap();
		paramMap.put("procId", procId);		
		paramMap.put("nonCallCd", nonCallCd);
		List<PriceInfo> list = priceInfoDao.selectAllList(paramMap);
		
		int iTotalDisplayRecords = list.size();
		int iTotalRecords = list.size();
		
		JSONDataTablesModel priceInfoJsonObject = new JSONDataTablesModel();
		priceInfoJsonObject.setiTotalDisplayRecords(iTotalDisplayRecords);
		priceInfoJsonObject.setiTotalRecords(iTotalRecords);		
				
		priceInfoJsonObject.setAaData(list);
				
		return priceInfoJsonObject;
	}
	
}
