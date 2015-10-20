package com.btilm305.report;

import com.btilm305.PageBuilder;
import com.btilm305.School;
import com.btilm305.TimeLoop;
import com.btilm305.filter.SectionFilter;
import com.btilm305.object.Population;
import com.btilm305.object.Section;
import com.btilm305.object.Time24Hours;
import com.btilm305.table.ASCIITable;
import com.btilm305.table.ASCIITableHeader;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BuildingPopulationReport extends Report {

	@Override
	protected void generateReport(final School school) {
		//index.html
		List<String> buildings = new ArrayList<String>();
		for (Section section : school.getSections()) {
			String b = section.getBuilding();
			if (!buildings.contains(b)) {
				buildings.add(b);
			}
		}
		Collections.sort(buildings);
		StringBuilder sb = new StringBuilder();
		for (String building : buildings) {
			sb.append("<a href='" + PageBuilder.encode(building) + ".html'>" + building + "</a><br>");
		}
		new PageBuilder("Select a Building").addText(sb.toString()).saveAsHTML(new File(getDirectory(), "index.html"));
		//building pages
		for (String building : buildings) {
			final List<Population> populationList = new ArrayList<Population>();
			final List<Section> buildingSections = new SectionFilter(school.getSections()).building(building).filter();
			TimeLoop.doLoop(new TimeLoop() {
				@Override
				public void loop(int day, Time24Hours time) {
					int count = 0;
					List<Section> matches = new SectionFilter(buildingSections).day(day).time(time).filter();
					for (Section section : matches) {
						count += section.getEnrolled();
					}
					populationList.add(new Population(time, day, count));
				}
			});
			Collections.sort(populationList, new Comparator<Population>() {
				@Override
				public int compare(Population o1, Population o2) {
					if (o1.getTime().equals(o2.getTime())) {
						return o1.getDay() - o2.getDay();
					} else {
						return o1.getTime().compareTo(o2.getTime());
					}
				}
			});
			ASCIITableHeader[] header = {
					new ASCIITableHeader("Time", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("Monday", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("Tuesday", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("Wednesday", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("Thursday", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("Friday", ASCIITable.ALIGN_LEFT),
			};
			List<String[]> data = new ArrayList<String[]>();
			String[] row = null;
			for (int i = 0; i < populationList.size(); i++) {
				Population pop = populationList.get(i);
				if (i % 5 == 0) {
					row = new String[6];
					data.add(row);
					row[0] = pop.getTime().toString();
				}
				row[(i % 5) + 1] = pop.getPopulation() + "";
				if (!row[0].equals(pop.getTime().toString())) {
					System.out.println("nope");
				}
			}
			List<String[]> finalData = new ArrayList<String[]>();
			for (String[] array : data) {
				for (int i = 1; i < array.length; i++) {
					if (!array[i].equals("0")) {
						finalData.add(array);
						break;
					}
				}
			}
			String table = ASCIITable.getInstance().getTable(header, finalData.toArray(new String[0][]));
			new PageBuilder(building + " Populations by Day/Time").addText(table).saveAsHTML(new File(getDirectory(), PageBuilder.encode(building).replace("%26", "&") + ".html")); //%26 -> &??? why, oh why
		}
	}

	@Override
	protected String getTitle() {
		return "Building Populations by Day/Time";
	}

	@Override
	protected String getDirectoryName() {
		return "building_population";
	}
}
