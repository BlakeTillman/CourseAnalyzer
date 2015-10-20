package com.btilm305.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {

	private String abbreviation;
	private String number;
	private String title;
	private List<Section> sections = new ArrayList<Section>();

	public Course(String abbreviation, String number, String title) {
		this.abbreviation = abbreviation;
		this.number = number;
		this.title = title;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public String getNumber() {
		return number;
	}

	public String getTitle() {
		return title;
	}

	public void addSection(Section section) {
		sections.add(section);
		section.setCourse(this);
	}

	public List<Section> getSections() {
		return sections;
	}

	public int getEnrollmentCount() {
		int count = 0;
		for (Section section : sections) {
			count += section.getEnrolled();
		}
		return count;
	}

	@Override
	public String toString() {
		return "(" + getEnrollmentCount() + ") " + abbreviation + " " + number + " - " + title;
	}
}
