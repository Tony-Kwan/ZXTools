package com.pygzx.zxtools.util;

import org.json.JSONObject;

public class Result {
	private static final String CODE = "code";
	private static final String MSG = "message";
	private static final String DATA = "data";

	public static final int SUCCESS_CODE         = 10_0000;
	public static final int ERROR_CODE           = 10_0001;

	public static final int INVALID_TOKEN        = -1;
	public static final int PERMISSION_DENIED    = 1 << 1;
	public static final int MISSING_FIELD        = 1 << 2;
	public static final int ACCESS_OVER_LIMIT    = 1 << 3;  // HTTP 429
	public static final int WRONG_SIGN           = 1 << 4;

	public static String success() {
		return response(SUCCESS_CODE, "success", null);
	}

	public static String success(JSONObject data) {
		return response(SUCCESS_CODE, "success", data);
	}

	public static String error() {
		return response(ERROR_CODE, "Bad request", null);
	}

	public static String missingField() {
		return response(MISSING_FIELD, "缺少字段", null);
	}

	public static String invalidToken() {
		return response(INVALID_TOKEN, "Token无效", null);
	}

	public static String permissionDenied() {
		return response(PERMISSION_DENIED, "Permission denied", null);
	}

	public static String wrongSign() {
		return response(WRONG_SIGN, "签名错误", null);
	}

	public static String response(int code, String msg, JSONObject data) {
		JSONObject ret = new JSONObject();
		ret.put(CODE, code);
		ret.put(MSG, msg);
		if (data != null) ret.put(DATA, data);
		return ret.toString();
	}
}
