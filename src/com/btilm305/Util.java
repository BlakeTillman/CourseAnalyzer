package com.btilm305;

import java.io.*;

public class Util {

	public static String fileToString(File file) throws IOException {
		return inputStreamToString(new FileInputStream(file));
	}

	public static String inputStreamToString(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length;
		while ((length = in.read(buffer)) > 0) {
			out.write(buffer, 0, length);
		}
		out.close();
		in.close();
		return new String(out.toByteArray());
	}
}
