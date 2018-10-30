package com.lightningant.explorer.service;

import com.lightningant.explorer.config.RPCServerConfig;
import com.lightningant.explorer.entity.LightningAntRPCParams;
import com.lightningant.explorer.entity.LightningAntRPCResult;
import com.lightningant.explorer.exception.BeidouchainException;
import com.lightningant.explorer.utils.JsonMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BaseService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	@Autowired
	private OkHttpClient client;

	@Autowired
	private RPCServerConfig config;

	public Object execute(String method, List<Object> params) throws BeidouchainException {

		LightningAntRPCResult beidouChainRPCResult = null;
		LightningAntRPCParams paramObject = new LightningAntRPCParams();
		paramObject.setMethod(method);
		paramObject.setParams(params);
		String paramToJson = JsonMapper.getInstance().toJson(paramObject);

//		logger.info("paramToJson:" + paramToJson);
		RequestBody body = RequestBody.create(JSON, paramToJson);

		final Request request = new Request.Builder().url("http://" + config.getRpcIp() + ":" + config.getRpcPort())
				.post(body).build();

		try (Response response = client.newCall(request).execute()) {

			String result = response.body().string();
//			logger.info("result : " + result);
			beidouChainRPCResult = JsonMapper.getInstance().fromJson(result, LightningAntRPCResult.class);
			if (beidouChainRPCResult != null && beidouChainRPCResult.getError() == null) {
				return beidouChainRPCResult.getResult();
			} else if (beidouChainRPCResult != null && beidouChainRPCResult.getError() != null) {
				throw new BeidouchainException(beidouChainRPCResult.getError().get("code").toString(),
						",message : " + beidouChainRPCResult.getError().get("message").toString());
			} else {
				throw new BeidouchainException("-201", "json解析异常 返回值为空!");
			}
		} catch (IOException e) {
			logger.error("method:" + method + ",reason" + e.getMessage());
			throw new BeidouchainException("-202", "http连接IO异常   ！");
		} catch (BeidouchainException e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	protected boolean verifyInstance(Object obj, Class TheClass) {
		return TheClass.isInstance(obj);
	}
}
