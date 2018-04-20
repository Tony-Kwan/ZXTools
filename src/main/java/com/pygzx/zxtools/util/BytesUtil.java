package com.pygzx.zxtools.util;

public class BytesUtil {
	private final static char[] DIGITS = {
		'0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b',
		'c', 'd', 'e', 'f', 'g', 'h',
		'i', 'j', 'k', 'l', 'm', 'n',
		'o', 'p', 'q', 'r', 's', 't',
		'u', 'v', 'w', 'x', 'y', 'z'
	};

	public static String byte2hex(byte[] data) {
		if (data == null || data.length == 0) {
			return "";
		}

		char[] ret = new char[data.length * 3 - 1];
		int i, v;
		for (i = 0; i < data.length - 1; i++) {
			v = data[i] & 0xff;
			ret[i * 3] = DIGITS[v >>> 4];
			ret[i * 3 + 1] = DIGITS[v & 0x0f];
			ret[i * 3 + 2] = 0x20;
		}
		v = data[i] & 0xff;
		ret[i * 3] = DIGITS[v >>> 4];
		ret[i * 3 + 1] = DIGITS[v & 0x0f];
		return new String(ret);
	}

	public static String byte2hex(byte b) {
		char[] ret = new char[2];
		ret[0] = DIGITS[(b & 0xf0) >>> 4];
		ret[1] = DIGITS[b & 0x0f];
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

	public static String toBinaryString(byte n) {
		char[] buf = new char[8];
		int mask = 1;
		for (int i = 7; i >= 0; i--) {
			buf[i] = DIGITS[n & mask];
			n >>>= 1;
		}
		return new String(buf);
	}

	public static String toBinaryString(short n) {
		char[] buf = new char[16];
		int mask = 1;
		for (int i = 15; i >= 0; i--) {
			buf[i] = DIGITS[n & mask];
			n >>>= 1;
		}
		return new String(buf);
	}

	public static String toBinaryString(int n) {
		char[] buf = new char[32];
		int mask = 1;
		for (int i = 31; i >= 0; i--) {
			buf[i] = DIGITS[n & mask];
			n >>>= 1;
		}
		return new String(buf);
	}

	public static String toBinaryString(long n) {
		char[] buf = new char[64];
		long mask = 1;
		for (int i = 63; i >= 0; i--) {
			buf[i] = DIGITS[(int) (n & mask)];
			n >>>= 1;
		}
		return new String(buf);
	}
}
