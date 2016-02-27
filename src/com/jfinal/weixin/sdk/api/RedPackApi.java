package com.jfinal.weixin.sdk.api;

import java.util.Map;

import com.jfinal.weixin.sdk.kit.PaymentKit;
import com.jfinal.weixin.sdk.utils.HttpUtils;

/**
 * 微信红包api
 * @author osc余书慧
 */
public class RedPackApi {
	
	// 文档地址：https://pay.weixin.qq.com/wiki/doc/api/cash_coupon.php?chapter=13_5#
	private static String sendRedPackUrl = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
	
	/**
	 * 
	 * 发送红包
	 * @param params 请求参数
	 * @param certPath 证书文件目录
	 * @param certPass 证书密码
	 * @return
	 */
	public static String sendRedPack(Map<String, String> params, String certPath, String partner) {
		return HttpUtils.postSSL(sendRedPackUrl, PaymentKit.toXml(params), certPath, partner);
	}
	
	// 文档地址：https://pay.weixin.qq.com/wiki/doc/api/cash_coupon.php?chapter=13_7&index=6
	private static String getHBInfo = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo ";
	
	/**
	 * 根据商户订单号查询信息
	 * @param params 请求参数
	 * @param certPath 证书文件目录
	 * @param certPass 证书密码
	 * @return
	 */
	public static String getHbInfo(Map<String, String> params, String certPath, String partner) {
		return HttpUtils.postSSL(getHBInfo, PaymentKit.toXml(params), certPath, partner);
	}

}
