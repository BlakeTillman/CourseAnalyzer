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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CampusPopulationReport extends Report {

	@Override
	protected void generateReport(final School school) {
		final List<Population> populationList = new ArrayList<Population>();
		TimeLoop.doLoop(new TimeLoop() {
			@Override
			public void loop(int day, Time24Hours time) {
				int count = 0;
				List<Section> matches = new SectionFilter(school.getSections()).day(day).time(time).filter();
				for (Section section : matches) {
					count += section.getEnrolled();
				}
				Population population = new Population(time, day, count);
				populationList.add(population);
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
		new PageBuilder(getTitle()).addText(table).saveAsHTML(new File(getDirectory(), "index.html"));
	}

	@Override
	protected String getTitle() {
		return "Campus-wide Class Population by Day/Time";
	}


	@Override
	protected String getDirectoryName() {
		return "campus_population";
	}
}
