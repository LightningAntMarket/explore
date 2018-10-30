package com.lightningant.explorer.service;

import com.lightningant.explorer.entity.Asset;
import com.lightningant.explorer.exception.BeidouchainException;
import com.lightningant.explorer.utils.JsonMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService extends BaseService{
	
		
	public List<Asset> getaddressbalances(String address) throws BeidouchainException{
		String method  = "getaddressbalances";
		List<Object> params = new ArrayList<Object>();
		params.add(address);
		Object rtnObj = null;
		rtnObj = execute(method, params);
		List<Asset> addressBalanceAssets = new ArrayList<Asset>();
		if(verifyInstance(rtnObj, List.class)){			
			String str = JsonMapper.getInstance().toJson(rtnObj);
			addressBalanceAssets = JsonMapper.getInstance().fromJson(str, JsonMapper.getInstance().createCollectionType(List.class, Asset.class));
		}		
		return addressBalanceAssets;
	}	
	
	public void importaddress(String address) throws BeidouchainException{		
		String method  = "importaddress";
		List<Object> params = new ArrayList<Object>();
		params.add(address);
		params.add("");
		params.add(false);
		execute(method, params);
	}
}