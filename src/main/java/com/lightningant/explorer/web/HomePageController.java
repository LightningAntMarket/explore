package com.lightningant.explorer.web;

import com.lightningant.explorer.config.LAPStringConst;
import com.lightningant.explorer.entity.LapAddressInfo;
import com.lightningant.explorer.exception.BeidouchainException;
import com.lightningant.explorer.service.ChainService;
import com.lightningant.explorer.service.HomePageService;
import com.lightningant.explorer.utils.JsonMapper;
import com.lightningant.explorer.utils.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
public class HomePageController {	
	
	@Autowired
	private HomePageService  homePageService;

	@Autowired
	private OkHttpClient client;
	@Autowired
	private ChainService chainService;
	private 	List<LapAddressInfo> rewardList;
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
			address.add(LAPStringConst.MINER_ADDRESS);
			address.add(LAPStringConst.TEAM_ADDRESS);
			address.add(LAPStringConst.OPERATION_ADDRESS);
			address.add(LAPStringConst.LAP_IN_OUT_ADDRESS);
			address.add(LAPStringConst.LAP_20_IN_OUT_ADDRESS);
			address.add(LAPStringConst.BLACKHOLE_ADDRESS);
//			model.addAttribute("allMap", chainService.getmultibalances(address));

			List<String> type = new ArrayList<>();
			type.add("Miner Rewards");
			type.add("Team");
			type.add("Operation");
			type.add("LAP Address");
			type.add("LAP ERC20 Address");
			type.add("Black hole");
			final Request request = new Request.Builder().url("").get()
					.build();
			List<LapAddressInfo> LapAddressInfo = new ArrayList<>();
			try (Response response = client.newCall(request).execute()) {
				String result = response.body().string();
				rewardList = JsonMapper.getInstance().fromJson(result, JsonMapper.getInstance().createCollectionType(List.class, LapAddressInfo.class));

				for(int i=0;i<20;i++){
					LapAddressInfo.add(rewardList.get(i));
				}

			}catch (IOException e) {
				e.printStackTrace();
			}
			model.addAttribute("pageNum",0);
			model.addAttribute("totalPages",49);
			model.addAttribute("totalElements",1000);
			model.addAttribute("laps", LapAddressInfo);
			model.addAttribute("sortMap", chainService.getaddressbalances(address,type));

		} catch (BeidouchainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "reward";
	}


	@GetMapping(value = "/page")
	public String findRewardPage(Model model, @RequestParam(value="pageNum") String pageNum){
		int a = 0;
		try {
			a = Integer.parseInt(pageNum);
		} catch (NumberFormatException e) {
			a = 0;
		}
		if(a<0 )
			a=0;
		else if(a>=50){
			a=49;
		}

		List<String> address = new ArrayList<>();
		address.add(LAPStringConst.MINER_ADDRESS);
		address.add(LAPStringConst.TEAM_ADDRESS);
		address.add(LAPStringConst.OPERATION_ADDRESS);
		address.add(LAPStringConst.LAP_IN_OUT_ADDRESS);
		address.add(LAPStringConst.LAP_20_IN_OUT_ADDRESS);
		address.add(LAPStringConst.BLACKHOLE_ADDRESS);
//			model.addAttribute("allMap", chainService.getmultibalances(address));

		List<String> type = new ArrayList<>();
		type.add("Miner Rewards");
		type.add("Team");
		type.add("Operation");
		type.add("LAP Address");
		type.add("LAP ERC20 Address");
		type.add("Black hole");

		List<LapAddressInfo> mlist = new ArrayList<>();

		for(int i=0;i<20;i++){
			mlist.add(rewardList.get(i+a*20));
		}

		int pagenum=a;
		model.addAttribute("page",mlist);
		model.addAttribute("pageNum",pagenum);
		model.addAttribute("totalPages",49);
		model.addAttribute("laps", mlist);
		model.addAttribute("totalElements",1000);
		try {
			model.addAttribute("sortMap", chainService.getaddressbalances(address,type));
		} catch (BeidouchainException e) {
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
		if(str.length() == 38 ||str.length() == 37){
			return "redirect:address?address=" +str;
		}
		
		//交易
		if(str.length() == 64){
			return "redirect:tx?txId=" +str;
		}
		return "redirect:/";
	}
}
