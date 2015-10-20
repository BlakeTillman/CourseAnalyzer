package com.btilm305.report;

import com.btilm305.PageBuilder;
import com.btilm305.School;
import com.btilm305.object.ClassTime;
import com.btilm305.object.Section;

import java.io.File;

public class PersonMinutesReport extends Report {

	@Override
	protected void generateReport(School school) {
		long personMinutes = 0;
		for (Section section : school.getSections()) {
			int days = 0;
			ClassTime t = section.getTime();
			for (int day = ClassTime.MONDAY; day <= ClassTime.FRIDAY; day *= 2) {
				if (t.occursOnDay(day)) {
					days++;
				}
			}
			int minutes = t.getStart().minutesUntil(t.getEnd());
			personMinutes += section.getEnrolled() * minutes * days;
		}
		new PageBuilder(getTitle()).addText(personMinutes + "").saveAsHTML(new File(getDirectory(), "index.html"));
	}

	@Override
	protected String getTitle() {
		return "Person Minutes per Week";
	}


	@Override
	protected String getDirectoryName() {
		return "person_minutes";
	}
}
