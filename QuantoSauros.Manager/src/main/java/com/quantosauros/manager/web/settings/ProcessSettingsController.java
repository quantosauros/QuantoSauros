package com.quantosauros.manager.web.settings;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quantosauros.manager.model.settings.PortfolioInfoModel;
import com.quantosauros.manager.model.settings.ProcessInfoModel;
import com.quantosauros.manager.model.settings.ScenarioInfoModel;
import com.quantosauros.manager.service.settings.PortfolioInfoService;
import com.quantosauros.manager.service.settings.ProcessInfoService;
import com.quantosauros.manager.service.settings.ScenarioInfoService;

@Controller
public class ProcessSettingsController {

	private final Logger logger = Logger.getLogger(ProcessSettingsController.class);
	
	public ProcessInfoService processInfoService;
	public ScenarioInfoService scenarioInfoService;
	public PortfolioInfoService portfolioInfoService;
	
	@Autowired
	public void setProcessInfoService(ProcessInfoService processInfoService){
		this.processInfoService = processInfoService;
	}
	
	@Autowired
	public void setPortfolioInfoService(PortfolioInfoService portfolioInfoService){
		this.portfolioInfoService = portfolioInfoService;
	}
	
	@Autowired
	public void setScenarioInfoService(ScenarioInfoService scenarioInfoService){
		this.scenarioInfoService = scenarioInfoService;
	}
	
	@RequestMapping(value = "/settings/process", method = RequestMethod.GET)
	public String processSettingsIndex(Model model){
		logger.debug("processSettingsIndex()");
		model.addAttribute("processInfoModel", processInfoService.selectProcessInfo());		
		return "/settings/processSettings";
	}
	
	@RequestMapping(value = "/settings/process", method = RequestMethod.POST)
	public String processSettingsInsertOrUpdate(
			@ModelAttribute("processInfoForm") ProcessInfoModel processInfoModel,
			Model model, final RedirectAttributes redirectAttributes){
		
		logger.debug("processSettingsInsertOrUpdate()");
		
		redirectAttributes.addFlashAttribute("css", "success");
		if (processInfoModel.isNew()){
			redirectAttributes.addFlashAttribute("msg", "Process added successfully!");
		} else {
			redirectAttributes.addFlashAttribute("msg", "Process updated successfully!");
		}
				
		processInfoService.saveOrUpdate(processInfoModel);
		
		return "redirect:/settings/process";
	}
	
	@RequestMapping(value = "/settings/process/{procId}/delete", method = RequestMethod.POST)
	public String deleteProcessInfo(@PathVariable("procId") String procId, 
			final RedirectAttributes redirectAttributes){
		logger.debug("deleteProcessInfo()" + procId);
		
		processInfoService.delete(procId);
		
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "Process " + procId + " is deleted!");
		
		return "redirect:/settings/process";
		
	}
	
	@RequestMapping(value = "/settings/process/{procId}/update", method = RequestMethod.GET)
	public String updateProcessInfo(@PathVariable("procId") String procId, 
			Model model){
		
		logger.debug("updateProcessInfo()" + procId);
		
		ProcessInfoModel processInfoModel = processInfoService.findByProcId(procId);
		model.addAttribute("processInfoModel", processInfoModel);
		
		popluateModel(model);
		
		return "settings/processform";		
	}
	
	@RequestMapping(value = "/settings/process/add", method = RequestMethod.GET)
	public String showAddProcessInfoForm(Model model){
		logger.debug("showAddProcessInfoForm()");
		
		ProcessInfoModel processInfoModel = new ProcessInfoModel();
		
		//set default value		
		processInfoModel.setPortfolioId("0");
		processInfoModel.setScenarioId("0");
		processInfoModel.setProcNM("");
		processInfoModel.setDescription("");
				
		model.addAttribute("processInfoModel", processInfoModel);
		popluateModel(model);
		
		return "settings/processform";
	}
	private void popluateModel(Model model){
		//scenario list
		List<ScenarioInfoModel> scenarioInfoModels = scenarioInfoService.selectScenarioInfo();
		Map<String, String> scenarioList = new LinkedHashMap<>();
		for (int i = 0; i < scenarioInfoModels.size(); i++){
			ScenarioInfoModel scenarioInfo = scenarioInfoModels.get(i);
			String id = scenarioInfo.getScenarioId();
			String name = id + ". " + 
					scenarioInfo.getScenarioNM()+ "(" + 
					scenarioInfo.getDescription() + ")";
			scenarioList.put(id, name);
		}		
		model.addAttribute("scenarioList", scenarioList);
		
		//portfolio list
		List<PortfolioInfoModel> portfolioInfoList = portfolioInfoService.getLists();
		Map<String, String> portfolioList = new LinkedHashMap<>();
		for (int i = 0; i < portfolioInfoList.size(); i++){
			PortfolioInfoModel portfolioInfo = portfolioInfoList.get(i);
			String id = portfolioInfo.getPortfolioId();
			String name = id + ". " + 
					portfolioInfo.getPortfolioNM()+ "(" + 
					portfolioInfo.getDescription() + ")";
			portfolioList.put(id, name);
		}		
		model.addAttribute("portfolioList", portfolioList);		
		
	}
}
