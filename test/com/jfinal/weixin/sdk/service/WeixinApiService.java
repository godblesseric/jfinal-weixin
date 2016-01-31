package com.jfinal.weixin.sdk.service;

import com.jfinal.aop.Duang;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.AccessTokenApi;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.jfinal.ApiService;
import com.jfinal.weixin.sdk.jfinal.ApiServiceInterceptor;

public class WeixinApiService implements ApiService {

	@Override
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		
		// 配置微信 API 相关常量
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));
		
		/**
		 *  是否对消息进行加密，对应于微信平台的消息加解密方式：
		 *  1：true进行加密且必须配置 encodingAesKey
		 *  2：false采用明文模式，同时也支持混合模式
		 */
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		return ac;
	}
	
	public void test () {
		System.out.println(AccessTokenApi.getAccessTokenStr());
	}
	
	public static void main(String[] args) {
		PropKit.use("a_little_config.txt");
		WeixinApiService was = Duang.duang(WeixinApiService.class, ApiServiceInterceptor.class);
		
		was.test();
	}
}
