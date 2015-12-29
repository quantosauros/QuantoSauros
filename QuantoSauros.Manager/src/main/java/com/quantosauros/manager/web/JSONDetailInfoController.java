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

import com.quantosauros.manager.dao.DetailInfoDao;
import com.quantosauros.manager.model.DetailInfo;
import com.quantosauros.manager.model.JSONDataTablesModel;

@Controller
public class JSONDetailInfoController {

	private DetailInfoDao detailInfoDao;
	
	@Autowired
	public JSONDetailInfoController(DetailInfoDao detailInfoDao){
		this.detailInfoDao = detailInfoDao;
	}
	
	@RequestMapping(value = "/json/detaillist", method = RequestMethod.GET)
	public @ResponseBody JSONDataTablesModel getDetailInfoList(
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
		String instrumentCd = request.getParameter("instrumentCd");
		String procId = request.getParameter("procId");
		String idxId = request.getParameter("idxId");
		String valueTypeInput = request.getParameter("valueType");	
		String legType = request.getParameter("legType");
		String nonCallCd = request.getParameter("nonCallCd");
		
		HashMap paramMap = new HashMap();
		paramMap.put("instrumentCd", instrumentCd);
		paramMap.put("procId", procId);
		paramMap.put("idx", idxId);
		paramMap.put("valueType", valueTypeInput);
		paramMap.put("nonCallCd", nonCallCd);
		if (legType != null)
			paramMap.put("legType", legType);
				
		List<DetailInfo> list = detailInfoDao.selectList(paramMap);
		
		int iTotalDisplayRecords = list.size();
		int iTotalRecords = list.size();
		JSONDataTablesModel detailInfoJSONObject = new JSONDataTablesModel();
		detailInfoJSONObject.setiTotalDisplayRecords(iTotalDisplayRecords);
		detailInfoJSONObject.setiTotalRecords(iTotalRecords);
		detailInfoJSONObject.setAaData(list);
		return detailInfoJSONObject;
	}
}
