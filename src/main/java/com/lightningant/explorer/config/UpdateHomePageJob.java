package com.lightningant.explorer.config;

import com.lightningant.explorer.entity.Asset20;
import com.lightningant.explorer.entity.HomePage;
import com.lightningant.explorer.entity.Transaction;
import com.lightningant.explorer.exception.BeidouchainException;
import com.lightningant.explorer.service.*;
import com.lightningant.explorer.utils.JsonMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UpdateHomePageJob {

	private Logger logger = LoggerFactory.getLogger(UpdateHomePageJob.class);

	@Autowired
	private HomePageService homePageService;

	@Autowired
	private ChainService chainService;

	@Autowired
	private AssetService assetService;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private BlockService blockService;

	@Autowired
	private OkHttpClient client;

	@Scheduled(fixedRate = 100000) // 10秒更新一次
	public void runUpdateTransActionJob() {

		logger.info("runUpdateTransActionJob!");

		HomePage homePage = homePageService.getHomePage();

		Map<String, Object> map;

		try {

			map = chainService.getInfo();
			int curruteBlockHeight = 0;
			int lastBlockHeight = homePage.getChainBlockHeight();
			if (map != null) {

				if (Integer.class.isInstance(map.get("blocks"))) {
					curruteBlockHeight = (Integer) map.get("blocks");
					homePage.setChainBlockHeight(curruteBlockHeight);
				}

				if (Integer.class.isInstance(map.get("connections"))) {
					homePage.setPeerNum((Integer) map.get("connections"));
				}
			} else {
				logger.warn("runUpdateTransActionJob 更新失败！");
			}

			if (curruteBlockHeight > lastBlockHeight) {
				logger.info("lastBlockHight : " + lastBlockHeight + ",currentBlockHight:" + curruteBlockHeight);

				Map<String, Object> txMap = transactionService.gettxoutsetinfo();
				if (txMap != null && Integer.class.isInstance(txMap.get("txouts"))) {
					Integer txouts = (Integer) txMap.get("txouts");
					homePage.setTxoutNum(txouts);
				}

				List<Map<String, Object>> blockList = blockService.listblocks(lastBlockHeight + 1, curruteBlockHeight,
						false);

				for (Map<String, Object> transMap : blockList) {

					Object txCount = transMap.get("txcount");
					// 忽略coinbase交易 block第一个交易为coinbase交易
					if (txCount != null && Integer.class.isInstance(txCount) && (Integer) txCount > 1) {

						List<Map<String, Object>> assetList = assetService.listassets();
						if (assetList != null) {
							homePage.setAssetNum(assetList.size());
						}

						Map<String, Object> txOutMap = transactionService.gettxoutsetinfo();
						if (txOutMap != null && Integer.class.isInstance(txOutMap.get("txouts"))) {
							Integer txouts = (Integer) txOutMap.get("txouts");
							homePage.setTxoutNum(txouts);
						}

						String blockHash = transMap.get("hash").toString();
						List<Transaction> tempList = blockService.getTransactionListByBlock(blockHash, 4);
						if (tempList != null && tempList.size() > 0) {

							logger.info("updateJob startBlock:" + (lastBlockHeight + 1) + ",endBlock:"
									+ curruteBlockHeight + ",totalSuccess:" + tempList.size());
							for (Transaction transaction : tempList) {
								homePageService.AddTransaction(transaction);
							}
						}

					}
				}

			}

		} catch (BeidouchainException e) {
			logger.error("runUpdateTransActionJob 更新失败！" + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Scheduled(fixedRate = 10000) // 10秒更新一次
	public void runUpdateETHJob() {
		String url="http://91baisong.com/bs_cn/Android/Erc20/Erc20LapAmount";
		Request request = new Request.Builder()
				.url(url)
				.build();
		try (Response response = client.newCall(request).execute()) {
			Asset20 assets20=	JsonMapper.getInstance().fromJson(response.body().string(),Asset20.class);
			if(null!= assets20){
				LAPStringConst.ERC20_BALANCE = Double.parseDouble(assets20.getResult())/10000000000000000L;
			}
		}catch (Exception e){
			logger.info("eeeee="+e.getMessage());
		}
	}
}