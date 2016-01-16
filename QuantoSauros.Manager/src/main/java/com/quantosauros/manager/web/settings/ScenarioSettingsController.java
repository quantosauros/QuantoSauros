package com.quantosauros.manager.web.settings;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quantosauros.manager.model.settings.ScenarioInfoModel;
import com.quantosauros.manager.service.settings.ScenarioInfoService;

@Controller
public class ScenarioSettingsController {

	private final Logger logger = Logger.getLogger(ScenarioSettingsController.class);
		
	public ScenarioInfoService scenarioInfoService;
			
	@Autowired
	public void setScenarioInfoService(ScenarioInfoService scenarioInfoService){
		this.scenarioInfoService = scenarioInfoService;
	}
	
	@RequestMapping(value = "/settings/scenario", method = RequestMethod.GET)
	public String scenarioSettingsIndex(Model model){
		logger.debug("scenarioSettingsIndex()");
		model.addAttribute("scenarioInfoModels", scenarioInfoService.selectScenarioInfo());		
		return "/settings/scenarioSettings";
	}
	
	@RequestMapping(value = "/settings/scenario", method = RequestMethod.POST)
	public String scenarioSettingsInsertOrUpdate(
			@ModelAttribute("scenarioInfoForm") ScenarioInfoModel scenarioInfoModel,
			Model model, final RedirectAttributes redirectAttributes){
		
		logger.debug("scenarioSettingsInsertOrUpdate()");
		
		redirectAttributes.addFlashAttribute("css", "success");
		if (scenarioInfoModel.isNew()){
			redirectAttributes.addFlashAttribute("msg", "Process added successfully!");
		} else {
			redirectAttributes.addFlashAttribute("msg", "Process updated successfully!");
		}
				
		scenarioInfoService.saveOrUpdate(scenarioInfoModel);
		
		return "redirect:/settings/scenario";
	}
	
	@RequestMapping(value = "/settings/scenario/{scenarioId}/delete", method = RequestMethod.POST)
	public String deleteScenarioInfo(@PathVariable("scenarioId") String scenarioId, 
			final RedirectAttributes redirectAttributes){
		logger.debug("deleteScenarioInfo()" + scenarioId);
		
		scenarioInfoService.delete(scenarioId);
		
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "Scenario " + scenarioId + " is deleted!");
		
		return "redirect:/settings/scenario";
		
	}
	
	@RequestMapping(value = "/settings/scenario/{scenarioId}/update", method = RequestMethod.GET)
	public String updateScenarioInfo(@PathVariable("scenarioId") String scenarioId, 
			Model model){
		
		logger.debug("updateScenarioInfo()" + scenarioId);
		
		ScenarioInfoModel scenarioInfoModel = scenarioInfoService.findById(scenarioId);
		model.addAttribute("scenarioInfoModel", scenarioInfoModel);
		
		return "settings/scenarioform";		
	}
	
	@RequestMapping(value = "/settings/scenario/add", method = RequestMethod.GET)
	public String showAddScenarioInfoForm(Model model){
		logger.debug("showAddScenarioInfoForm()");
		
		ScenarioInfoModel scenarioInfoModel = new ScenarioInfoModel();
		
		//set default value		
		scenarioInfoModel.setScenarioId("0");		
		scenarioInfoModel.setScenarioNM("");
		scenarioInfoModel.setDescription("");
				
		model.addAttribute("scenarioInfoModel", scenarioInfoModel);
		
		return "settings/scenarioform";
	}
}
