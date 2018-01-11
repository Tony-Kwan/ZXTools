package com.pygzx.zxtools.util;

import com.quickutil.platform.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class AliSmsUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(AliSmsUtil.class);

	private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
		.setConnectionRequestTimeout(5000)
		.setConnectTimeout(5000)
		.setSocketTimeout(5000)
		.build();
	private static final String CODE_OK = "OK";

	private static Map<String, Map<String, String>> dayuMap = new HashMap<>();

	public static void init(Properties properties) {
		Set<String> keySet = new HashSet<>();
		properties.forEach((k, v) -> {
			String key = ((String) k).split("\\.")[0];
			keySet.add(key);
		});
		keySet.forEach(key -> {
			try {
				Map<String, String> map = new HashMap<>();
				map.put("signname", properties.getProperty(key + ".signname"));
				map.put("appkey", properties.getProperty(key + ".appkey"));
				map.put("appsecret", properties.getProperty(key + ".appsecret") + "&");
				map.put("template", properties.getProperty(key + ".template"));
				dayuMap.put(key, map);
			} catch (Exception e) {
				LOGGER.error("读取阿里大于配置失败", e);
			}
		});
		LOGGER.info("Read config: {}", dayuMap.keySet());
	}

	public static boolean sendMessage(String name, String phone, Map<String, Object> param, int retry) {
		boolean ret;
		do {
			ret = sendMessage(name, Collections.singletonList(phone), param);
		} while (!ret && --retry > 0);
		return ret;
	}

	public static boolean sendMessage(String name, List<String> phoneList, Map<String, Object> param, int retry) {
		boolean ret;
		do {
			ret = sendMessage(name, phoneList, param);
		} while (!ret && --retry > 0);
		return ret;
	}

	private static boolean sendMessage(String name, List<String> phoneList, Map<String, Object> param) {
		if (!dayuMap.containsKey(name) || phoneList == null || phoneList.isEmpty()) {
			return false;
		}

		try {
			Map<String, String> config = dayuMap.get(name);
			String appkey = config.get("appkey");
			String appsecret = config.get("appsecret");
			String signname = config.get("signname");
			String template = config.get("template");

			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			fmt.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));

			Map<String, String> map = new HashMap<>();
			map.put("SignatureMethod", "HMAC-SHA1");
			map.put("SignatureNonce", java.util.UUID.randomUUID().toString());
			map.put("AccessKeyId", appkey);
			map.put("SignatureVersion", "1.0");
			map.put("Timestamp", fmt.format(new Date()));
			map.put("Format", "JSON");

			map.put("Action", "SendSms");
			map.put("Version", "2017-05-25");
			map.put("RegionId", "cn-hangzhou");
			String pns = String.join(",", phoneList);
			map.put("PhoneNumbers", pns);
			map.put("SignName", signname);
			if (param != null && !param.isEmpty()) {
				map.put("TemplateParam", new JSONObject(param).toString());
			}
			map.put("TemplateCode", template);

			TreeMap<String, String> sortedMap = new TreeMap<>(map);
			java.util.Iterator<String> it = sortedMap.keySet().iterator();
			StringBuilder sortQueryStringTmp = new StringBuilder();
			while (it.hasNext()) {
				String key = it.next();
				sortQueryStringTmp.append("&").append(specialUrlEncode(key)).append("=").append(specialUrlEncode(map.get(key)));
			}
			String sortedQueryString = sortQueryStringTmp.substring(1);

			StringBuilder sb = new StringBuilder();
			sb.append("GET").append("&");
			sb.append(specialUrlEncode("/")).append("&");
			sb.append(specialUrlEncode(sortedQueryString));

			String signature = specialUrlEncode(sign(appsecret, sb.toString()));
			String url = "http://dysmsapi.aliyuncs.com/?Signature=" + signature + sortQueryStringTmp;
			HttpResponse response = HttpUtil.httpGet(url, null, null, REQUEST_CONFIG);
			if (response != null) {
				String resContent = EntityUtils.toString(response.getEntity());
				JSONObject resBody = new JSONObject(resContent);
				if (CODE_OK.equals(resBody.get("Code"))) {
					return true;
				} else {
					LOGGER.warn(String.format("SendMessage: phone=%s, error=%s", pns, resContent));
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return false;
	}

	private static String specialUrlEncode(String value) throws Exception {
		return URLEncoder.encode(value, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
	}

	private static String sign(String accessSecret, String stringToSign) throws Exception {
		javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
		mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
		byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
		return new sun.misc.BASE64Encoder().encode(signData);
	}
}
