package com.lightningant.explorer.web;

import com.lightningant.explorer.exception.BeidouchainException;
import com.lightningant.explorer.service.ChainService;
import com.lightningant.explorer.service.HomePageService;
import com.lightningant.explorer.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class HomePageController {	
	
	@Autowired
	private HomePageService  homePageService;
	
	@Autowired
	private ChainService chainService;
	
	@RequestMapping("/")
	public String homepage(Model model){
		model.addAttribute("homePage", homePageService.getHomePage());	
		List<Object> transList  = Arrays.asList(homePageService.getTransactionArray());
		Collections.reverse(transList);
		model.addAttribute("transList", transList);
		return "index";
	}	
	
	@RequestMapping("/updateHomePage")
	public String updateHomePage(Model model){
		model.addAttribute("homePage", homePageService.getHomePage());	
		List<Object> transList  = Arrays.asList(homePageService.getTransactionArray());
		Collections.reverse(transList);
		model.addAttribute("transList", transList);
		return "updateHomePage";
	}	
	
	
	@RequestMapping("/chain")
	public String chain(Model model){
		
		try {
			model.addAttribute("homePage", homePageService.getHomePage());
			model.addAttribute("chainMap", chainService.getInfo());
			model.addAttribute("chainparamMap", chainService.getBlockChainParams());
		} catch (BeidouchainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "chain";
	}
	@RequestMapping("/reward")
	public String reward(Model model){

		try {
			List<String> address = new ArrayList<>();
			address.add("1Att7WxrtEWmbzfaEr5WUT7PTsThJaucBuy6hq");
			address.add("1aJuUtix6tGWfCJU4CuZ7DiR2kpigjvTxNt5BM");
			address.add("1ETiAT8rFN12iK2iB8Xtygr8qyQBgoY9abmynn");
			address.add("13TGqfGd3Bpuyyha5af6QakPjbcdmy6DspXCUd");
//			model.addAttribute("allMap", chainService.getmultibalances(address));

			List<String> type = new ArrayList<>();
			type.add("Miner Rewards");
			type.add("Team");
			type.add("Operation");
			type.add("Black hole");

			model.addAttribute("sortMap", chainService.getaddressbalances(address,type));
			System.out.print("list");
		} catch (BeidouchainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "reward";
	}
	@RequestMapping("/search")
	public String search(String str,Model model){
		
		//主页
		if(StringUtils.isBlank(str)){
			return "redirect:/";
		}
		
		str = str.trim();
		
		//主链名字
		if(homePageService.getHomePage().getChainName().equalsIgnoreCase(str)){
			return "redirect:/";
		}
		
		//区块高度
		if(StringUtils.isNumeric(str)){
			return "redirect:block?hash=" +str;
		}
		
		//地址长度
		if(str.length() == 38){
			return "redirect:address?address=" +str;
		}
		
		//交易
		if(str.length() == 64){
			return "redirect:tx?txId=" +str;
		}
		return "redirect:/";
	}
}
