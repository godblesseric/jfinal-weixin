package com.jfinal.weixin.sdk.kit;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.StrKit;

/**
 * 微信支付的统一下单工具类
 * @author L.cm
 */
public class PaymentKit {
	
	private static final String CHARSET = "UTF-8";
	
	/**
	 * 组装签名的字段
	 * @param params 参数
	 * @param urlEncoder 是否urlEncoder
	 * @return String
	 */
	public static String packageSign(Map<String, String> params, boolean urlEncoder) {
		// 先将参数以其参数名的字典序升序进行排序
		TreeMap<String, String> sortedParams = new TreeMap<String, String>(params);
		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Entry<String, String> param : sortedParams.entrySet()) {
			String value = param.getValue();
			if (StrKit.isBlank(value)) {
				continue;
			}
			if (first) {
				first = false;
			} else {
				sb.append("&");
			}
			sb.append(param.getKey()).append("=");
			if (urlEncoder) {
				try { value = urlEncode(value); } catch (UnsupportedEncodingException e) {}
			}
			sb.append(value);
		}
		return sb.toString();
	}
	
	/**
	 * urlEncode
	 * @param src 微信参数
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String urlEncode(String src) throws UnsupportedEncodingException {
		return URLEncoder.encode(src, CHARSET).replace("+", "%20");
	}
	
	/**
	 * 生成签名
	 * @return
	 */
	public static String createSign(Map<String, String> params, String paternerKey) {
		Map<String, String> signParams = new HashMap<String, String>(params);
		// 先去除sign
		signParams.remove("sign");
		String stringA = packageSign(signParams, false);
		String stringSignTemp = stringA + "&key=" + paternerKey;
		return HashKit.md5(stringSignTemp).toUpperCase();
	}
	
	/**
	 * 支付异步通知时校验sign
	 * @param params
	 * @param paternerKey
	 * @return
	 */
	public static boolean verifyNotify(Map<String, String> params, String paternerKey){
		String sign = PaymentKit.createSign(params, paternerKey);
		return sign.equals(params.get("sign"));
	}
	
	/**
	 * 微信下单，map to xml
	 * @param params 参数
	 * @return String
	 */
	public static String toXml(Map<String, String> params) {
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>");
		for (Entry<String, String> entry : params.entrySet()) {
			String key   = entry.getKey();
			String value = entry.getValue();
			// 略过空值
			if (StrKit.isBlank(value)) continue;
			xml.append("<").append(key).append(">");
				xml.append(entry.getValue());
			xml.append("</").append(key).append(">");
		}
		xml.append("</xml>");
		return xml.toString();
	}
	
	/**
	 * 针对支付的xml，没有嵌套节点的简单处理
	 * @param xml xml字符串
	 * @return Map<String, String> map集合
	 */
	public static Map<String, String> xmlToMap(String xmlStr) {
		Document document = XmlKit.parse(xmlStr);
		Element root = document.getDocumentElement();
		Map<String, String> params = new HashMap<String, String>();
		
		// 将节点封装成map形式
		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			params.put(node.getNodeName(), node.getTextContent());
		}
		// 含有空白符会生成一个#text参数
		params.remove("#text");
		return params;
	}

}