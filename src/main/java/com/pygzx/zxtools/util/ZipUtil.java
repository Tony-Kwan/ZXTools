package com.pygzx.zxtools.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ZipUtil {
	public static byte[] gunzip(InputStream in) {
		if (in == null) {
			return null;
		}
		GZIPInputStream gzis = null;
		try {
			gzis = new GZIPInputStream(in);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len;
			byte[] buf = new byte[1024];
			while ((len = gzis.read(buf)) > 0) {
				bos.write(buf, 0, len);
			}
			return bos.toByteArray();
		} catch (Exception e) {

		} finally {
			IOUtil.closeQuietly(gzis);
		}
		return null;
	}

	public static byte[] gzip(byte[] bt) {
		GZIPOutputStream gzip = null;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			gzip = new GZIPOutputStream(out);
			gzip.write(bt);
			return out.toByteArray();
		} catch (Exception e) {

		} finally {
			IOUtil.closeQuietly(gzip);
		}
		return null;
	}
}
