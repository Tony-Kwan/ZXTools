package com.pygzx.zxtools.util;

import org.apache.commons.codec.Charsets;

public class BytesReader {
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	private byte[] data;
	private int index = 0;
	private int tempIndex = 0;

	public BytesReader(byte[] data) throws NullPointerException {
		if (data == null)
			throw new NullPointerException("data can not be null");
		this.data = data;
	}

	public void reset() { index = 0; }

	public int skip(int i) {
		if (i > data.length)
			return index;
		index = i;
		return index;
	}

	public int getIndex() { return index; }

	public void markReaderIndex() {
		tempIndex = index;
	}

	public void resetReaderIndex() {
		index = tempIndex;
	}

	public int length() { return data.length; }

	public int readableBytes() { return data.length - index; };

	public byte readByte() {
		if (index + 1 > data.length)
			throw new IndexOutOfBoundsException();
		return data[index++];
	}

	public short readShort() {
		if (index + 2 > data.length)
			throw new IndexOutOfBoundsException();
		short ret = (short) ((data[index] & 0xff) | ((data[index + 1] & 0xff) << 8));
		index += 2;
		return ret;
	}

	public int readUnsignedShort() {
		return readShort() & 0x0000ffff;
	}

	public int readInt() {
		if (index + 4 > data.length)
			throw new IndexOutOfBoundsException();
		int ret = (data[index] & 0xff)
			| ((data[index + 1] & 0xff) << 8)
			| ((data[index + 2] & 0xff) << 16)
			| ((data[index + 3] & 0xff) << 24);
		index += 4;
		return ret;
	}

	public long readLong() {
		if (index + 8 > data.length)
			throw new IndexOutOfBoundsException();
		long ret = (data[index] & 0xff)
			| ((data[index + 1] & 0xff) << 8)
			| ((data[index + 2] & 0xff) << 16)
			| ((data[index + 3] & 0xff) << 24)
			| ((data[index + 4] & 0xff) << 32)
			| ((data[index + 5] & 0xff) << 40)
			| ((data[index + 6] & 0xff) << 48)
			| ((data[index + 7] & 0xff) << 56);
		index += 8;
		return ret;
	}

	public long readUnsignedInt() {
		return readInt() & 0x00000000ffffffffL;
	}

	public byte[] readBytes(int length) throws Exception {
		if (length < 0)
			throw new Exception(String.format("read length must gte 0 | %d", length));
		if (index + length > data.length)
			throw new IndexOutOfBoundsException();
		if (length == 0)
			return null;
		byte[] ret = new byte[length];
		System.arraycopy(data, index, ret, 0, length);
		index += length;
		return ret;
	}

	public String readString(int length) throws Exception {
		byte[] bytes = readBytes(length);
		if (bytes != null) {
			return new String(subBytes(bytes, 0, strlen(bytes)), Charsets.UTF_8);
		}
		return null;
	}

	@Override
	public String toString() {
		return byte2hex(data);
	}

	private static String byte2hex(byte[] bytes) {
		char[] buf = new char[bytes.length * 3];
		for ( int i = 0; i < bytes.length; i++ ) {
			int v = bytes[i] & 0xFF;
			buf[i * 3] = HEX_ARRAY[v >>> 4];
			buf[i * 3 + 1] = HEX_ARRAY[v & 0x0F];
			buf[i * 3 + 2] = ' ';
		}
		return new String(buf);
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
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = source[from + i];
		}
		return result;
	}
}
