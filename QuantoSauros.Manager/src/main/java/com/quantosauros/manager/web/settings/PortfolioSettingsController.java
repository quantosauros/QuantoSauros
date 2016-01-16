package com.quantosauros.manager.web.settings;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quantosauros.manager.model.settings.PortfolioDataModelForm;
import com.quantosauros.manager.model.settings.PortfolioInfoModel;
import com.quantosauros.manager.service.settings.PortfolioInfoService;

@Controller
public class PortfolioSettingsController {

	private final Logger logger = Logger.getLogger(PortfolioSettingsController.class);
		
	public PortfolioInfoService portfolioInfoService;
	
	
	@Autowired
	public void setPortfolioInfoService(PortfolioInfoService portfolioInfoService){
		this.portfolioInfoService = portfolioInfoService;
	}
	
	@RequestMapping(value = "/settings/portfolio", method = RequestMethod.GET)
	public String portfolioSettingsIndex(Model model){
		logger.debug("portfolioSettingsIndex()");
		model.addAttribute("portfolioInfoModels", portfolioInfoService.getLists());		
		return "/settings/portfolioSettings";
	}
	
	@RequestMapping(value = "/settings/portfolio", method = RequestMethod.POST)
	public String portfolioSettingsInsertOrUpdate(
			@ModelAttribute("portfolioInfoModel") PortfolioInfoModel portfolioInfoModel,			
			BindingResult resultPortfolioInfo,			
			@ModelAttribute("portfolioDataModelForm") PortfolioDataModelForm portfolioDataModelForm,
			BindingResult resultPortfolioDataForm,
			Model model, final RedirectAttributes redirectAttributes){
		
		logger.debug("portfolioSettingsInsertOrUpdate()");
		
		redirectAttributes.addFlashAttribute("css", "success");
		if (portfolioInfoModel.isNew()){
			redirectAttributes.addFlashAttribute("msg", "Process added successfully!");
		} else {
			redirectAttributes.addFlashAttribute("msg", "Process updated successfully!");
		}
				
		portfolioInfoService.saveOrUpdate(portfolioInfoModel, portfolioDataModelForm);		
		
		return "redirect:/settings/portfolio";
	}
	
	@RequestMapping(value = "/settings/portfolio/{portfolioId}/delete", method = RequestMethod.POST)
	public String deletePortfolioInfo(@PathVariable("portfolioId") String portfolioId, 
			final RedirectAttributes redirectAttributes){
		logger.debug("deletePortfolioInfo()" + portfolioId);
		
		portfolioInfoService.delete(portfolioId);
		
		redirectAttributes.addFlashAttribute("css", "success");
		redirectAttributes.addFlashAttribute("msg", "Portfolio " + portfolioId + " is deleted!");
		
		return "redirect:/settings/portfolio";
		
	}
	
	@RequestMapping(value = "/settings/portfolio/{portfolioId}/update", method = RequestMethod.GET)
	public String updatePortfolioInfo(@PathVariable("portfolioId") String portfolioId, 
			Model model){
		
		logger.debug("updatePortfolioInfo()" + portfolioId);
		
		PortfolioInfoModel portfolioInfoModel = portfolioInfoService.getOneById(portfolioId);
		model.addAttribute("portfolioInfoModel", portfolioInfoModel);
				
		PortfolioDataModelForm portfolioDataModelForm = new PortfolioDataModelForm();
		portfolioDataModelForm.setPortfolioDatas(portfolioInfoService.getDataLists(portfolioId));
		model.addAttribute("portfolioDataModelForm", portfolioDataModelForm);
		
		return "settings/portfolioform";		
	}
	
	@RequestMapping(value = "/settings/portfolio/add", method = RequestMethod.GET)
	public String showAddPortfolioInfoForm(Model model){
		logger.debug("showAddPortfolioInfoForm()");
		
		PortfolioInfoModel portfolioInfoModel = new PortfolioInfoModel();		
		//set default value		
		portfolioInfoModel.setPortfolioNM("");
		portfolioInfoModel.setDescription("");
		model.addAttribute("portfolioInfoModel", portfolioInfoModel);
		
		PortfolioDataModelForm portfolioDataModelForm = new PortfolioDataModelForm();
		portfolioDataModelForm.setPortfolioDatas(portfolioInfoService.getDataLists(""));
		model.addAttribute("portfolioDataModelForm", portfolioDataModelForm);
		
		return "settings/portfolioform";
	}
//	private void popluateModel(Model model){
//		//portfolioData List
//		Map<String, String> portfolioDataList = new LinkedHashMap<>();
//		
//		
//		for (int i = 0; i < portfolioDatas.size(); i++){
//			PortfolioData portfolioData = portfolioDatas.get(i);
//			String id = portfolioData.get
//			String name = id + ". " + 
//					instrumentInfo.getScenarioNM()+ "(" + 
//					instrumentInfo.getDescription() + ")";
//			portfolioDataList.put(id, name);
//		}		
//		model.addAttribute("portfolioDataList", portfolioDataList);
//				
//	}
}
