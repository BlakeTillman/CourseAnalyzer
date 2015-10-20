package com.btilm305;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class CourseDownloader implements Runnable {

	private File courseFile;
	private File downloadDir;

	public CourseDownloader(File courseFile, File downloadDir) {
		this.courseFile = courseFile;
		this.downloadDir = downloadDir;
	}

	public void run() {
		try {
			FileReader fileReader = new FileReader(courseFile);
			BufferedReader reader = new BufferedReader(fileReader);
			String classId = null;
			while ((classId = reader.readLine()) != null) {
				HttpURLConnection connection = (HttpURLConnection) new URL("http://appl003.lsu.edu/booklet2.nsf/68a84f901daef98386257b43006b778a?CreateDocument").openConnection();
				connection.setDoOutput(true);
				connection.setRequestMethod("POST");
				connection.getOutputStream().write(("%25%25Surrogate_SemesterDesc=1&SemesterDesc=Fall+2015&%25%25Surrogate_Department=1&Department=" + classId).getBytes());
				String html = Util.inputStreamToString(connection.getInputStream());
				if (!html.contains("<PRE>")) {
					continue;
				}
				String[] split = html.split("\n");
				String[] lines = new String[split.length - 14];
				for (int i = 11; i < split.length - 3; i++) {
					lines[i - 11] = split[i];
				}
				StringBuilder sb = new StringBuilder();
				for (String line : lines) {
					sb.append(line);
					sb.append("\n");
				}
				FileOutputStream out = new FileOutputStream(new File(downloadDir, classId + ".txt"), false);
				out.write(sb.toString().getBytes());
				out.close();
			}
			reader.close();
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
