package com.lightningant.explorer.service;

import com.lightningant.explorer.exception.BeidouchainException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChainService extends BaseService{	

	
	@SuppressWarnings("unchecked")
	public Map<String, Object>  getInfo() throws BeidouchainException{
		
		String method  = "getinfo";
		Object retObj = execute(method, null);
		if(retObj != null && verifyInstance(retObj, Map.class)){
			Map<String, Object>  map = (Map<String, Object>)retObj;
			return map;
		}
		return null;
	}	
	
	@SuppressWarnings("unchecked")
	public Map<String, Object>  getBlockChainParams() throws BeidouchainException{
		
		String method  = "getblockchainparams";
		Object retObj = execute(method, null);
		if(retObj != null && verifyInstance(retObj, Map.class)){
			Map<String, Object>  map = (Map<String, Object>)retObj;
			return map;
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getaddressbalances(List<String> address, List<String> type) throws BeidouchainException{
		String method  = "getaddressbalances";
		Object rtnObj = null;
		List<Map<String,Object>> sortList = new ArrayList<>();
		List<Object> params = new ArrayList<Object>();

		for(int i=0;i<address.size();i++){
			params.clear();
			params.add(address.get(i));
			rtnObj = execute(method, params);
			if(null==rtnObj ||((List<Map<String,Object>>)rtnObj).size()==0){
				Map<String, Object> map = new HashMap<String, Object>();

				map.put("typeName",type.get(i));
				map.put("qty","");
				map.put("pubAddress",address.get(i));
				if(i==address.size()-1){
					map.put("privatekey","VCFKsdYaf2ftwKT8PTBLXYzaq2GfrVQ8eW28NHzj15mL3SZwXTH6ZGru");
				}else{
					map.put("privatekey","");
				}
				sortList.add(map);
			}
			else if(verifyInstance(rtnObj, List.class)){
				List<Map<String,Object>>  list = (List<Map<String,Object>>)rtnObj;
				list.get(0).put("typeName",type.get(i));
				list.get(0).put("pubAddress",address.get(i));
				if(i==address.size()-1){
					list.get(0).put("privatekey","VCFKsdYaf2ftwKT8PTBLXYzaq2GfrVQ8eW28NHzj15mL3SZwXTH6ZGru");
				}else{
					list.get(0).put("privatekey","");
				}

				sortList.add(list.get(0));
			}

		}

		return sortList;
	}
}