package com.btilm305.report;

import com.btilm305.PageBuilder;
import com.btilm305.School;
import com.btilm305.filter.SectionFilter;
import com.btilm305.object.Section;
import com.btilm305.table.ASCIITable;
import com.btilm305.table.ASCIITableHeader;

import java.io.File;
import java.net.URLEncoder;
import java.util.*;

public class Instructors extends Report {

	@Override
	protected void generateReport(School school) {
		//index
		List<String> instructors = new ArrayList<String>();
		for (Section section : school.getSections()) {
			if (!instructors.contains(section.getInstructor())) {
				instructors.add(section.getInstructor());
			}
		}
		Collections.sort(instructors);
		StringBuilder sb = new StringBuilder();
		for (String instructor : instructors) {
			sb.append("<a href='" + PageBuilder.encode(instructor) + ".html'>" + instructor + "</a>\n");
		}
		new PageBuilder(getTitle()).addText(sb.toString()).saveAsHTML(new File(getDirectory(), "index.html"));
		//instructor pages
		for (String instructor : instructors) {
			List<Section> sections = new SectionFilter(school.getSections()).instructor(instructor).filter();
			String[][] rows = new String[sections.size()][];
			for (int i = 0; i < sections.size(); i++) {
				Section section = sections.get(i);
				rows[i] = PageBuilder.tableSectionRows(section);
			}
			String table = ASCIITable.getInstance().getTable(PageBuilder.tableSectionHeader(), rows);
			new PageBuilder(instructor + "'s Classes").addText(table).saveAsHTML(new File(getDirectory(), PageBuilder.encode(instructor) + ".html"));
		}
	}

	@Override
	protected String getTitle() {
		return "Instructors";
	}

	@Override
	protected String getDirectoryName() {
		return "instructors";
	}
}
