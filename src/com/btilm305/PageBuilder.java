package com.btilm305;

import com.btilm305.object.Section;
import com.btilm305.table.ASCIITable;
import com.btilm305.table.ASCIITableHeader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PageBuilder {

	private static String base = "http://blaketillman.github.io/CourseAnalyzer";

	private static String part1 = "<!DOCTYPE html><html><head lang=\"en\"><meta charset=\"UTF-8\"><title>";
	private static String part2 = "</title></head><body><pre>";
	private static String part3 = "\n<a href='" + base + "/'>Home</a></pre></body></html>";

	private String title;
	private String content = "";
	private String navigation = "";

	public PageBuilder(String title) {
		this.title = title;
		navigation("Home", "/");
	}

	public PageBuilder addText(String text) {
		return addText(text, true);
	}

	public PageBuilder addText(String text, boolean addTitle) {
		if (addTitle) {
			content += title + "\n";
		}
		content += text;
		return this;
	}

	public PageBuilder navigation(String title, String dir) {
		if (!navigation.isEmpty()) {
			navigation += " > ";
		}
		navigation += "<a href='" + base + dir + "'/>" + title + "</a>";
		return this;
	}

	public PageBuilder navigation(PageBuilder pageBuilder) {
		this.navigation = pageBuilder.navigation;
		return this;
	}

	public PageBuilder saveAsHTML(File file) {
		if (file.isDirectory()) {
			file.mkdirs();
		} else {
			file.getParentFile().mkdirs();
		}
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(part1);
			sb.append(title);
			sb.append(part2);
			if (!navigation.isEmpty()) {
				sb.append(navigation + "\n\n");
			}
			sb.append(content);
			sb.append(part3);
			FileWriter writer = new FileWriter(file, false);
			writer.write(sb.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public static String[] tableSectionRows(Section section) {
//		String base = "http://btilm305.github.io";
		String[] row = new String[11];
		row[0] = section.getAvailable();
		row[1] = section.getEnrolled() + "";
		row[2] = "<a href='" + base + "/courses/" + section.getCourse().getAbbreviation() + "/'>" + section.getCourse().getAbbreviation() + "</a>";
		row[3] = "<a href='" + base + "/courses/" + section.getCourse().getAbbreviation() + "/" + section.getCourse().getNumber() + ".html'>" + section.getCourse().getNumber() + "</a>";
		row[4] = section.getSectionNumber();
		row[5] = section.getCourse().getTitle();
		row[6] = section.getHours();
		row[7] = section.getTime().toString();
		row[8] = "<a href='" + base + "/buildings/" + PageBuilder.encode(section.getBuilding()) + "/" + section.getRoom() + ".html'>" + section.getRoom() + "</a>";
		row[9] = "<a href='" + base + "/buildings/" + PageBuilder.encode(section.getBuilding()) + "/'>" + section.getBuilding() + "</a>";
		row[10] = "<a href='" + base + "/instructors/" + PageBuilder.encode(section.getInstructor()) + ".html'>" + section.getInstructor() + "</a>";
		return row;
	}

	public static ASCIITableHeader[] tableSectionHeader() {
		return new ASCIITableHeader[]{
				new ASCIITableHeader("AVL", ASCIITable.ALIGN_RIGHT),
				new ASCIITableHeader("ENRL CNT", ASCIITable.ALIGN_RIGHT),
				new ASCIITableHeader("ABBR", ASCIITable.ALIGN_LEFT),
				new ASCIITableHeader("NUM", ASCIITable.ALIGN_LEFT),
				new ASCIITableHeader("SEC", ASCIITable.ALIGN_RIGHT),
				new ASCIITableHeader("TITLE", ASCIITable.ALIGN_LEFT),
				new ASCIITableHeader("HOURS", ASCIITable.ALIGN_LEFT),
				new ASCIITableHeader("TIME", ASCIITable.ALIGN_LEFT),
				new ASCIITableHeader("ROOM", ASCIITable.ALIGN_LEFT),
				new ASCIITableHeader("BUILDING", ASCIITable.ALIGN_LEFT),
				new ASCIITableHeader("INSTRUCTOR", ASCIITable.ALIGN_LEFT)
		};
	}

	public static String encode(String string) {
		return string.replaceAll(" ", "_").replaceAll("/", "");
	}
}
