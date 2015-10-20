package com.btilm305.report;

import com.btilm305.PageBuilder;
import com.btilm305.School;
import com.btilm305.object.Section;
import com.btilm305.table.ASCIITable;
import com.btilm305.table.ASCIITableHeader;

import java.io.File;
import java.util.*;

public class DepartmentReport extends Report {

	@Override
	protected void generateReport(School school) {
		Map<String, Integer> enrollments = new HashMap<String, Integer>();
		for (Section section : school.getSections()) {
			String department = section.getCourse().getAbbreviation();
			if (!enrollments.containsKey(department)) {
				enrollments.put(department, 0);
			}
			enrollments.put(department, enrollments.get(department) + section.getEnrolled());
		}
		List<DepartmentCount> counts = new ArrayList<DepartmentCount>();
		for (String department : enrollments.keySet()) {
			counts.add(new DepartmentCount(department, enrollments.get(department)));
		}
		Collections.sort(counts, new Comparator<DepartmentCount>() {
			@Override
			public int compare(DepartmentCount o1, DepartmentCount o2) {
				return o2.count - o1.count;
			}
		});
		ASCIITableHeader[] header = {
				new ASCIITableHeader("Department", ASCIITable.ALIGN_RIGHT),
				new ASCIITableHeader("Total Enrollments", ASCIITable.ALIGN_LEFT),
		};
		String[][] rows = new String[counts.size()][];
		for (int i = 0; i < counts.size(); i++) {
			DepartmentCount count = counts.get(i);
			String hyperlink = "<a href='../courses/" + count.department + "/'>" + count.department + "</a>";
			rows[i] = new String[]{hyperlink, count.count + ""};
		}
		String table = ASCIITable.getInstance().getTable(header, rows);
		new PageBuilder(getTitle()).addText(table).saveAsHTML(new File(getDirectory(), "index.html"));
	}

	@Override
	protected String getTitle() {
		return "Departments by Enrollment Count";
	}

	private class DepartmentCount {

		public String department;
		public int count;

		public DepartmentCount(String department, int count) {
			this.department = department;
			this.count = count;
		}
	}

	@Override
	protected String getDirectoryName() {
		return "department_count";
	}
}
