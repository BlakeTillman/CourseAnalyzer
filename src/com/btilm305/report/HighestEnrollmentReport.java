package com.btilm305.report;

import com.btilm305.PageBuilder;
import com.btilm305.School;
import com.btilm305.object.Course;
import com.btilm305.table.ASCIITable;
import com.btilm305.table.ASCIITableHeader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighestEnrollmentReport extends Report {

	@Override
	protected void generateReport(School school) {
		List<Course> courses = new ArrayList<Course>(school.getCourses());
		Collections.sort(courses, new Comparator<Course>() {
			@Override
			public int compare(Course o1, Course o2) {
				return o2.getEnrollmentCount() - o1.getEnrollmentCount();
			}
		});
		ASCIITableHeader[] header = {
				new ASCIITableHeader("Course", ASCIITable.ALIGN_RIGHT),
				new ASCIITableHeader("Title", ASCIITable.ALIGN_CENTER),
				new ASCIITableHeader("Total Enrollments", ASCIITable.ALIGN_LEFT),
		};
		String[][] rows = new String[courses.size()][];
		for (int i = 0; i < courses.size(); i++) {
			Course course = courses.get(i);
			String hyperlink = "<a href='../courses/" + course.getAbbreviation() + "/" + course.getNumber() + ".html'>" + course.getAbbreviation() + " " + course.getNumber() + "</a>";
			rows[i] = new String[]{hyperlink, course.getTitle(), course.getEnrollmentCount() + ""};
		}
		String table = ASCIITable.getInstance().getTable(header, rows);
		new PageBuilder(getTitle()).addText(table).saveAsHTML(new File(getDirectory(), "index.html"));
	}

	@Override
	protected String getTitle() {
		return "Courses by Enrollment Count";
	}


	@Override
	protected String getDirectoryName() {
		return "highest_enrollment";
	}
}
