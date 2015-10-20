package com.btilm305.report;

import com.btilm305.PageBuilder;
import com.btilm305.School;
import com.btilm305.TimeLoop;
import com.btilm305.filter.SectionFilter;
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

public class BuildingAverageReport extends Report {

	@Override
	protected void generateReport(final School school) {
		//get building names
		List<String> buildings = new ArrayList<String>();
		for (Section section : school.getSections()) {
			String b = section.getBuilding();
			if (!buildings.contains(b)) {
				buildings.add(b);
			}
		}
		//find averages for buildings
		List<BuildingAverage> averages = new ArrayList<BuildingAverage>();
		for (final String building : buildings) {
			final int[] count = {0};
			final List<Section> buildingSections = new SectionFilter(school.getSections()).building(building).filter();
			TimeLoop.doLoop(new TimeLoop() {
				@Override
				public void loop(int day, Time24Hours time) {
					List<Section> matches = new SectionFilter(buildingSections).day(day).time(time).filter();
					for (Section section : matches) {
						count[0] += section.getEnrolled();
					}
				}
			});
			double average = count[0] / 6.0 / 24.0 / 5.0;
			averages.add(new BuildingAverage(building, average));
		}
		Collections.sort(averages, new Comparator<BuildingAverage>() {
			public int compare(BuildingAverage o1, BuildingAverage o2) {
				return Double.compare(o2.average, o1.average);
			}
		});
		ASCIITableHeader[] header = {
				new ASCIITableHeader("Building", ASCIITable.ALIGN_RIGHT),
				new ASCIITableHeader("Average Population", ASCIITable.ALIGN_LEFT),
		};
		String[][] rows = new String[averages.size()][];
		for (int i = 0; i < averages.size(); i++) {
			BuildingAverage average = averages.get(i);
			String hyperlink = "<a href='../building_population/" + PageBuilder.encode(average.building) + ".html'>" + average.building + "</a>";
			rows[i] = new String[]{hyperlink, String.format("%.2f", average.average)};
		}
		String table = ASCIITable.getInstance().getTable(header, rows);
		new PageBuilder(getTitle()).addText(table).saveAsHTML(new File(getDirectory(), "index.html"));
	}

	@Override
	protected String getTitle() {
		return "Buildings by Average Population";
	}

	@Override
	protected String getDirectoryName() {
		return "building_average";
	}

	private class BuildingAverage {

		private String building;
		private double average;

		public BuildingAverage(String building, double average) {
			this.building = building;
			this.average = average;
		}
	}
}
