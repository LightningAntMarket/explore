package com.lightningant.explorer.web;

import com.lightningant.explorer.exception.BeidouchainException;
import com.lightningant.explorer.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lightningant.explorer.service.HomePageService;
import com.lightningant.explorer.utils.StringUtils;

@Controller
public class AssetController {	

	
	@Autowired
	private AssetService assetService;
	
	@Autowired
	private HomePageService  homePageService;
	
	@RequestMapping("/assets")
	public String assets(Model model){
		try {
			model.addAttribute("homePage", homePageService.getHomePage());
			model.addAttribute("assets", assetService.listassets());
			System.out.println(assetService.listassets().size());
		} catch (BeidouchainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "assets";
	}		
	
	@RequestMapping("/assetref")
	public String assets(String ref,Model model){
		
		if(StringUtils.isBlank(ref)){
			return "error";
		}
		
		try {
			model.addAttribute("homePage", homePageService.getHomePage());
			model.addAttribute("assetMap", assetService.assetref(ref).get(0));
		} catch (BeidouchainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "assetref";
	}		
}
