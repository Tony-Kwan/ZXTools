package com.pygzx.zxtools.util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;

public class IOUtil {
	public static void closeQuietly(Reader input) {
		closeQuietly((Closeable)input);
	}

	public static void closeQuietly(Writer output) {
		closeQuietly((Closeable)output);
	}

	public static void closeQuietly(InputStream input) {
		closeQuietly((Closeable)input);
	}

	public static void closeQuietly(OutputStream output) {
		closeQuietly((Closeable)output);
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			if(closeable != null) {
				closeable.close();
			}
		} catch (IOException var2) {
			;
		}

	}

	public static void closeQuietly(Socket sock) {
		if(sock != null) {
			try {
				sock.close();
			} catch (IOException var2) {
				;
			}
		}

	}

	public static void closeQuietly(Selector selector) {
		if(selector != null) {
			try {
				selector.close();
			} catch (IOException var2) {
				;
			}
		}

	}

	public static void closeQuietly(ServerSocket sock) {
		if(sock != null) {
			try {
				sock.close();
			} catch (IOException var2) {
				;
			}
		}

	}
}
