package com.btilm305.report;

import com.btilm305.PageBuilder;
import com.btilm305.School;
import com.btilm305.filter.SectionFilter;
import com.btilm305.object.Section;
import com.btilm305.table.ASCIITable;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Buildings extends Report {

	@Override
	protected void generateReport(School school) {
		//index
		List<String> buildings = new ArrayList<String>();
		for (Section section : school.getSections()) {
			if (!buildings.contains(section.getBuilding())) {
				buildings.add(section.getBuilding());
			}
		}
		Collections.sort(buildings);
		StringBuilder sb = new StringBuilder();
		for (String building : buildings) {
			sb.append("<a href='" + PageBuilder.encode(building) + "/'>" + building + "</a>\n");
		}
		new PageBuilder(getTitle()).addText(sb.toString()).saveAsHTML(new File(getDirectory(), "index.html"));
		//directories
		for (String building : buildings) {
			List<Section> sections = new SectionFilter(school.getSections()).building(building).filter();
			List<String> rooms = new ArrayList<String>();
			for (Section section : sections) {
				if (!rooms.contains(section.getRoom())) {
					rooms.add(section.getRoom());
				}
			}
			Collections.sort(rooms);
			sb = new StringBuilder();
			for (String room : rooms) {
				sb.append("<a href='" + room + ".html'>" + room + "</a>" + "\n");
			}
			new PageBuilder("Classes in " + building).addText(sb.toString()).saveAsHTML(new File(getDirectory(), PageBuilder.encode(building) + "/index.html"));
			//rooms sections
			for (String room : rooms) {
				List<Section> filteredSections = new SectionFilter(sections).room(room).filter();
				String[][] rows = new String[filteredSections.size()][];
				for (int i = 0; i < filteredSections.size(); i++) {
					Section section = filteredSections.get(i);
					rows[i] = PageBuilder.tableSectionRows(section);
				}
				String table = ASCIITable.getInstance().getTable(PageBuilder.tableSectionHeader(), rows);
				new PageBuilder("Rooms in " + room + " " + building).addText(table).saveAsHTML(new File(getDirectory(), PageBuilder.encode(building) + "/" + PageBuilder.encode(room) + ".html"));
			}
		}
	}

	@Override
	protected String getTitle() {
		return "Buildings";
	}

	@Override
	protected String getDirectoryName() {
		return "buildings";
	}
}
