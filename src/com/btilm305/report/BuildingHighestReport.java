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
import java.util.*;

public class BuildingHighestReport extends Report {

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
		//find highest population for buildings
		final Map<String, Integer> buildingPopulations = new HashMap<String, Integer>();
		for (final String building : buildings) {
			buildingPopulations.put(building, 0);
			final List<Section> buildingSections = new SectionFilter(school.getSections()).building(building).filter();
			TimeLoop.doLoop(new TimeLoop() {
				@Override
				public void loop(int day, Time24Hours time) {
					int count = 0;
					List<Section> matches = new SectionFilter(buildingSections).day(day).time(time).filter();
					for (Section section : matches) {
						count += section.getEnrolled();
					}
					if (buildingPopulations.get(building) < count) {
						buildingPopulations.put(building, count);
					}
				}
			});
		}
		List<BuildingPopulation> populations = new ArrayList<BuildingPopulation>();
		for (String building : buildingPopulations.keySet()) {
			populations.add(new BuildingPopulation(building, buildingPopulations.get(building)));
		}
		Collections.sort(populations, new Comparator<BuildingPopulation>() {
			public int compare(BuildingPopulation o1, BuildingPopulation o2) {
				return o2.population - o1.population;
			}
		});
		ASCIITableHeader[] header = {
				new ASCIITableHeader("Building", ASCIITable.ALIGN_RIGHT),
				new ASCIITableHeader("Average Population", ASCIITable.ALIGN_LEFT),
		};
		String[][] rows = new String[populations.size()][];
		for (int i = 0; i < populations.size(); i++) {
			BuildingPopulation population = populations.get(i);
			String hyperlink = "<a href='../building_population/" + PageBuilder.encode(population.building) + ".html'>" + population.building + "</a>";
			rows[i] = new String[]{hyperlink, population.population + ""};
		}
		String table = ASCIITable.getInstance().getTable(header, rows);
		new PageBuilder(getTitle()).addText(table).saveAsHTML(new File(getDirectory(), "index.html"));
	}

	@Override
	protected String getTitle() {
		return "Buildings by Highest Population";
	}

	@Override
	protected String getDirectoryName() {
		return "building_population";
	}

	private class BuildingPopulation {

		private String building;
		private int population;

		public BuildingPopulation(String building, int population) {
			this.building = building;
			this.population = population;
		}
	}
}
