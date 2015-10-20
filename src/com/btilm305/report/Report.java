package com.btilm305.report;

import com.btilm305.PageBuilder;
import com.btilm305.School;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public abstract class Report {

	private static List<Report> reports = new ArrayList<Report>();

	static {
		reports.add(new BuildingPopulationReport());
		reports.add(new CampusPopulationReport());
		reports.add(new BuildingAverageReport());
		reports.add(new PersonMinutesReport());
		reports.add(new DepartmentReport());
		reports.add(new HighestEnrollmentReport());
		reports.add(new BuildingHighestReport());
		reports.add(new Instructors());
		reports.add(new Courses());
		reports.add(new Buildings());
	}

	public static void run(School school) {
		StringBuilder sb = new StringBuilder();
		for (Report report : reports) {
			long start = System.currentTimeMillis();
			report.getDirectory().mkdirs();
			report.generateReport(school);
			System.out.println(report.getClass().getName() + " took " + (System.currentTimeMillis() - start) + "ms");
			sb.append("<a href='" + PageBuilder.encode(report.getDirectoryName()) + "/'>" + report.getTitle() + "</a>\n");
		}
		new PageBuilder("Navigation").addText(sb.toString()).saveAsHTML(new File("output", "index.html"));
	}

	protected File getDirectory() {
		return new File("output", getDirectoryName());
	}

	protected abstract void generateReport(School school);

	protected abstract String getTitle();

	protected abstract String getDirectoryName();
}
