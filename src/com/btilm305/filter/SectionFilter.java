package com.btilm305.filter;

import com.btilm305.object.ClassTime;
import com.btilm305.object.Section;
import com.btilm305.object.Time24Hours;

import java.util.ArrayList;
import java.util.List;

public class SectionFilter {

	private List<Section> sections;

	public SectionFilter(List<Section> sections) {
		this.sections = sections;
	}

	public SectionFilter room(String room) {
		List<Section> sections = new ArrayList<Section>();
		for (Section section : this.sections) {
			if (section.getRoom().equals(room)) {
				sections.add(section);
			}
		}
		this.sections = sections;
		return this;
	}

	public SectionFilter building(String building) {
		List<Section> sections = new ArrayList<Section>();
		for (Section section : this.sections) {
			if (section.getBuilding().equals(building)) {
				sections.add(section);
			}
		}
		this.sections = sections;
		return this;
	}

	public SectionFilter instructor(String instructor) {
		List<Section> sections = new ArrayList<Section>();
		for (Section section : this.sections) {
			if (section.getInstructor().equals(instructor)) {
				sections.add(section);
			}
		}
		this.sections = sections;
		return this;
	}

	public SectionFilter time(Time24Hours time) {
		List<Section> sections = new ArrayList<Section>();
		for (Section section : this.sections) {
			ClassTime t = section.getTime();
			if (time.isInclusive(t.getStart(), t.getEnd())) {
				sections.add(section);
			}
		}
		this.sections = sections;
		return this;
	}

	public SectionFilter day(int day) {
		List<Section> sections = new ArrayList<Section>();
		for (Section section : this.sections) {
			if (section.getTime().occursOnDay(day)) {
				sections.add(section);
			}
		}
		this.sections = sections;
		return this;
	}

	public SectionFilter lab(boolean lab) {
		List<Section> sections = new ArrayList<Section>();
		for (Section section : this.sections) {
			if (section.isLab() == lab) {
				sections.add(section);
			}
		}
		this.sections = sections;
		return this;
	}

	public List<Section> filter() {
		return sections;
	}
}
