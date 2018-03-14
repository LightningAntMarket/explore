package com.lightningant.explorer.config;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class OkHttpClientConfig {

	@Autowired
	private RPCServerConfig config;

	@Bean
	public OkHttpClient okHttpClient() {

		return new OkHttpClient.Builder().authenticator(new Authenticator() {

			@Override
			public Request authenticate(Route route, Response response) throws IOException {

				if (response.request().header("Authorization") != null) {
					return null; // 不重复认证
				}
				String credential = Credentials.basic(config.getRpcUser(), config.getRpcPassword());
				return response.request().newBuilder().header("Authorization", credential).build();
			}
		}).connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
				.build();
	}
}
