package com.btilm305;

import com.btilm305.object.Course;
import com.btilm305.object.Section;

import java.util.ArrayList;
import java.util.List;

public class School {

	private List<Course> courses = new ArrayList<Course>();
	private List<Section> sections = new ArrayList<Section>();

	public List<Course> getCourses() {
		return courses;
	}

	public List<Section> getSections() {
		return sections;
	}
}
