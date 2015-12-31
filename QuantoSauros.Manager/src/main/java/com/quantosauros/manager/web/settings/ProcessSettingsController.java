package com.quantosauros.manager.web.settings;

import java.util.LinkedHashMap;
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

import com.quantosauros.manager.model.ProcessInfo;
import com.quantosauros.manager.service.ProcessInfoService;

@Controller
public class ProcessSettingsController {

	private final Logger logger = Logger.getLogger(ProcessSettingsController.class);
	
	public ProcessInfoService processInfoService;
	
	@Autowired
	public void setProcessInfoService(ProcessInfoService processInfoService){
		this.processInfoService = processInfoService;
	}
	
	@RequestMapping(value = "/settings/process", method = RequestMethod.GET)
	public String processSettingsIndex(Model model){
		logger.debug("processSettingsIndex()");
		model.addAttribute("processInfo", processInfoService.selectProcessInfo());		
		return "/settings/processSettings";
	}
	
	@RequestMapping(value = "/settings/process", method = RequestMethod.POST)
	public String processSettingsInsertOrUpdate(
			@ModelAttribute("processInfoForm") ProcessInfo processInfo,
			Model model, final RedirectAttributes redirectAttributes){
		
		logger.debug("processSettingsInsertOrUpdate()");
		
		redirectAttributes.addFlashAttribute("css", "success");
		if (processInfo.isNew()){
			redirectAttributes.addFlashAttribute("msg", "Process added successfully!");
		} else {
			redirectAttributes.addFlashAttribute("msg", "Process updated successfully!");
		}
				
		processInfoService.saveOrUpdate(processInfo);
		
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
		
		ProcessInfo processInfo = processInfoService.findByProcId(procId);
		model.addAttribute("processInfo", processInfo);
		
		Map<String, String> scenarioList = new LinkedHashMap<>();
		scenarioList.put("0", "Normal");
		scenarioList.put("1", "Abnormal");
		model.addAttribute("scenarioList", scenarioList);		
		
		return "settings/processform";		
	}
	
	@RequestMapping(value = "/settings/process/add", method = RequestMethod.GET)
	public String showAddProcessInfoForm(Model model){
		logger.debug("showAddProcessInfoForm()");
		
		ProcessInfo processInfo = new ProcessInfo();
		
		//set default value		
		processInfo.setPortfolioId("0");
		processInfo.setScenarioId("0");
		processInfo.setProcNM("프로세스 이름을 적어주세요.");
		processInfo.setDescription("간단한 프로세스 설명을 적어주세요.");
				
		model.addAttribute("processInfo", processInfo);
		
		return "settings/processform";
	}
	
}
