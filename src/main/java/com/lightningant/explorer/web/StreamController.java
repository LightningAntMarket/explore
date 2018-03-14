package com.lightningant.explorer.web;

import com.lightningant.explorer.exception.BeidouchainException;
import com.lightningant.explorer.service.HomePageService;
import com.lightningant.explorer.service.StreamService;
import com.lightningant.explorer.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StreamController {	
	
	@Autowired
	private StreamService streamService;
	
	@Autowired
	private HomePageService homePageService;
	
	@RequestMapping("/streams")
	public String streams(Model model){
		try {
			model.addAttribute("homePage", homePageService.getHomePage());
			model.addAttribute("streams", streamService.liststreams());
		} catch (BeidouchainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "streams";
	}		
	
	@RequestMapping("/stream")
	public String stream(String name,Model model){
		
		if(StringUtils.isBlank(name)){
			return "error";
		}
		
		try {
			model.addAttribute("homePage", homePageService.getHomePage());
			model.addAttribute("streams", streamService.liststreams(name).get(0));
		} catch (BeidouchainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "stream";
	}	
}
