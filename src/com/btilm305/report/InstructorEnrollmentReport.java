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

public class InstructorEnrollmentReport extends Report {

	@Override
	protected void generateReport(School school) {
		//index
		Map<String, InstructorData> instructorMap = new HashMap<String, InstructorData>();
		for (Section section : school.getSections()) {
			String instructor = section.getInstructor();
			if (!instructorMap.containsKey(instructor)) {
				instructorMap.put(instructor, new InstructorData(instructor));
			}
			InstructorData data = instructorMap.get(instructor);
			data.enrollment += section.getEnrolled();
			data.sections++;
		}
		ArrayList<InstructorData> instructors = new ArrayList<InstructorData>(instructorMap.values());
		Collections.sort(instructors, new Comparator<InstructorData>() {
			@Override
			public int compare(InstructorData o1, InstructorData o2) {
				return o2.enrollment - o1.enrollment;
			}
		});
		ASCIITableHeader[] header = {
				new ASCIITableHeader("Instructor", ASCIITable.ALIGN_RIGHT),
				new ASCIITableHeader("Total Enrollments", ASCIITable.ALIGN_LEFT),
				new ASCIITableHeader("# of Classes", ASCIITable.ALIGN_LEFT),
		};
		String[][] rows = new String[instructors.size()][];
		for (int i = 0; i < instructors.size(); i++) {
			InstructorData instructor = instructors.get(i);
			String hyperlink = "<a href='" + PageBuilder.encode(instructor.name) + ".html'>" + instructor.name + "</a>";
			rows[i] = new String[]{hyperlink, instructor.enrollment + "", instructor.sections + ""};
		}
		String table = ASCIITable.getInstance().getTable(header, rows);
		new PageBuilder(getTitle()).addText(table).saveAsHTML(new File(getDirectory(), "index.html"));
		//instructor pages
		for (InstructorData instructor : instructors) {
			List<Section> sections = new SectionFilter(school.getSections()).instructor(instructor.name).filter();
			header = new ASCIITableHeader[]{
					new ASCIITableHeader("AVL", ASCIITable.ALIGN_RIGHT),
					new ASCIITableHeader("ENRL CNT", ASCIITable.ALIGN_RIGHT),
					new ASCIITableHeader("ABBR", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("NUM", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("SEC", ASCIITable.ALIGN_RIGHT),
					new ASCIITableHeader("TITLE", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("HOURS", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("TIME", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("ROOM", ASCIITable.ALIGN_LEFT),
					new ASCIITableHeader("BUILDING", ASCIITable.ALIGN_LEFT)
			};
			rows = new String[sections.size()][];
			for (int i = 0; i < sections.size(); i++) {
				Section section = sections.get(i);
				String[] row = new String[10];
				rows[i] = row;
				row[0] = section.getAvailable();
				row[1] = section.getEnrolled() + "";
				row[2] = section.getCourse().getAbbreviation();
				row[3] = section.getCourse().getNumber();
				row[4] = section.getSectionNumber();
				row[5] = section.getCourse().getTitle();
				row[6] = section.getHours();
				row[7] = section.getTime().toString();
				row[8] = section.getRoom();
				row[9] = section.getBuilding();
			}
			table = ASCIITable.getInstance().getTable(header, rows);
			new PageBuilder(instructor.name+"'s Classes").addText(table).saveAsHTML(new File(getDirectory(), PageBuilder.encode(instructor.name) + ".html"));
		}
	}

	private class InstructorData {

		public String name;
		public int sections;
		public int enrollment;

		public InstructorData(String name) {
			this.name = name;
		}
	}

	@Override
	protected String getTitle() {
		return "Instructors by Student Enrollment";
	}

	@Override
	protected String getDirectoryName() {
		return "instructors";
	}
}
