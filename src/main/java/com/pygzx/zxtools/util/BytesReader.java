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
		long ret = ((long) data[index] & 0xff)
			| ((long) (data[index + 1] & 0xff) << 8)
			| ((long) (data[index + 2] & 0xff) << 16)
			| ((long) (data[index + 3] & 0xff) << 24)
			| ((long) (data[index + 4] & 0xff) << 32)
			| ((long) (data[index + 5] & 0xff) << 40)
			| ((long) (data[index + 6] & 0xff) << 48)
			| ((long) (data[index + 7] & 0xff) << 56);
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
			return new String(BytesUtil.subBytes(bytes, 0, BytesUtil.strlen(bytes)), Charsets.UTF_8);
		}
		return null;
	}

	@Override
	public String toString() {
		return BytesUtil.byte2hex(data);
	}
}
