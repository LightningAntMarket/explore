package com.lightningant.explorer.web;

import com.lightningant.explorer.entity.Asset;
import com.lightningant.explorer.exception.BeidouchainException;
import com.lightningant.explorer.service.AddressService;
import com.lightningant.explorer.service.AssetService;
import com.lightningant.explorer.service.PermissionsService;
import com.lightningant.explorer.utils.JsonMapper;
import com.lightningant.explorer.utils.StringUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AddressController {	
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private OkHttpClient client;
	@Autowired
	private AssetService assetService;
	
	@RequestMapping("/address")
	public String streams(String address,Model model){
		
		if(StringUtils.isBlank(address)){
			return "error";
		}
		
		try {
			model.addAttribute("address", address);
			model.addAttribute("plist", permissionsService.listpermissions(address));
//			addressService.importaddress(address);
			List<Map<String,Object>> list = assetService.listassets();
//			List<Asset> assets = addressService.getaddressbalances(address);

			try {
				Request request = new Request.Builder()
						.url("http://91baisong.com/bs_cn/Android/BlockBrowser/getMyBlance/address/"+address)
						.build();
				Response response = client.newCall(request).execute();
				String str1 =response.body().string();

				if(null==str1 || str1.equals("")){
					List<Asset> assets = new ArrayList<>();
					model.addAttribute("assets", assets);
				}else {
					List<Asset> assets=	JsonMapper.getInstance().fromJson(str1, JsonMapper.getInstance().createCollectionType(List.class, Asset.class));
					for(Asset asset : assets){
						for(Map<String,Object> map:list){
							if(asset.getName().equalsIgnoreCase(String.valueOf(map.get("name")))){
								asset.setUnits((Double)map.get("units"));
							}
						}
					}
					model.addAttribute("assets", assets);
				}

			}catch (Exception e){
				System.out.println(e.toString());
			}

		} catch (BeidouchainException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "address";
	}		
}
