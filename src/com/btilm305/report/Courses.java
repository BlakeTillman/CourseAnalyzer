package com.btilm305.report;

import com.btilm305.PageBuilder;
import com.btilm305.School;
import com.btilm305.filter.CourseFilter;
import com.btilm305.object.Course;
import com.btilm305.object.Section;
import com.btilm305.table.ASCIITable;
import com.btilm305.table.ASCIITableHeader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Courses extends Report {

	@Override
	protected void generateReport(School school) {
		//index
		List<String> departments = new ArrayList<String>();
		for (Course course : school.getCourses()) {
			if (!departments.contains(course.getAbbreviation())) {
				departments.add(course.getAbbreviation());
			}
		}
		Collections.sort(departments);
		StringBuilder sb = new StringBuilder();
		for (String department : departments) {
			sb.append("<a href='" + department + "/'>" + department + "</a>\n");
		}
		PageBuilder pb = new PageBuilder(getTitle()).navigation("Courses", "/courses/").addText(sb.toString()).saveAsHTML(new File(getDirectory(), "index.html"));
		//directories
		for (String department : departments) {
			List<Course> courses = new CourseFilter(school.getCourses()).abbreviation(department).filter();
			Collections.sort(courses, new Comparator<Course>() {
				@Override
				public int compare(Course o1, Course o2) {
					return o1.getNumber().compareTo(o2.getNumber());
				}
			});
			ASCIITableHeader[] header = {
					new ASCIITableHeader("Course", ASCIITable.ALIGN_RIGHT),
					new ASCIITableHeader("Sections", ASCIITable.ALIGN_RIGHT),
					new ASCIITableHeader("Title", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("Enrolled", ASCIITable.ALIGN_LEFT)
			};
			String[][] rows = new String[courses.size()][];
			for (int i = 0; i < courses.size(); i++) {
				Course course = courses.get(i);
				String hyperlink = "<a href='" + course.getNumber() + ".html'>" + department + " " + course.getNumber() + "</a>";
				rows[i] = new String[]{hyperlink, course.getSections().size() + "", course.getTitle(), course.getEnrollmentCount() + ""};
			}
			String table = ASCIITable.getInstance().getTable(header, rows);
			PageBuilder pb2 = new PageBuilder(department + " Courses").navigation(pb).navigation(department, "/courses/" + department + "/").addText(table).saveAsHTML(new File(getDirectory(), department + "/index.html"));
			//courses
			for (Course course : courses) {
				header = PageBuilder.tableSectionHeader();
				rows = new String[course.getSections().size()][];
				for (int i = 0; i < course.getSections().size(); i++) {
					Section section = course.getSections().get(i);
					rows[i] = PageBuilder.tableSectionRows(section);
				}
				table = ASCIITable.getInstance().getTable(header, rows);
				String name = course.getAbbreviation() + " " + course.getNumber() + " Sections";
				new PageBuilder(name).navigation(pb2).navigation(course.getNumber(), "/courses/" + department + "/" + course + ".html").addText(table).saveAsHTML(new File(getDirectory(), course.getAbbreviation() + "/" + course.getNumber() + ".html"));
			}
		}
	}

	@Override
	protected String getTitle() {
		return "Course List";
	}

	@Override
	protected String getDirectoryName() {
		return "courses";
	}
}
