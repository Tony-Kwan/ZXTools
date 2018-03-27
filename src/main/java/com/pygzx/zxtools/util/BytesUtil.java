package com.pygzx.zxtools.util;

public class BytesUtil {
	private final static char[] HEX_CHARSET = "0123456789abcdef".toCharArray();

	public static String byte2hex(byte[] data) {
		if (data == null || data.length == 0) {
			return "";
		}

		char[] ret = new char[data.length * 3 - 1];
		int i, v;
		for (i = 0; i < data.length - 1; i++) {
			v = data[i] & 0xff;
			ret[i * 3] = HEX_CHARSET[v >>> 4];
			ret[i * 3 + 1] = HEX_CHARSET[v & 0x0f];
			ret[i * 3 + 2] = 0x20;
		}
		v = data[i] & 0xff;
		ret[i * 3] = HEX_CHARSET[v >>> 4];
		ret[i * 3 + 1] = HEX_CHARSET[v & 0x0f];
		return new String(ret);
	}

	public static String byte2hex(byte b) {
		char[] ret = new char[2];
		ret[0] = HEX_CHARSET[(b & 0xf0) >>> 4];
		ret[1] = HEX_CHARSET[b & 0x0f];
		return new String(ret);
	}

	public static int strlen(byte[] buf) {
		if (buf == null)
			return 0;
		for (int i = 0; i < buf.length; i++) {
			if (buf[i] == 0x00)
				return i;
		}
		return buf.length;
	}

	public static byte[] subBytes(byte[] source, int from, int length) {
		byte[] ret = new byte[length];
		for (int i = 0; i < length; i++) {
			ret[i] = source[from + i];
		}
		return ret;
	}
}
