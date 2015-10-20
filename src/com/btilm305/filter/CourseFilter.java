package com.btilm305.filter;

import com.btilm305.object.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseFilter {

	private List<Course> courses;

	public CourseFilter(List<Course> courses) {
		this.courses = courses;
	}

	public CourseFilter abbreviation(String abbreviation) {
		List<Course> courses = new ArrayList<Course>();
		for (Course course : this.courses) {
			if (course.getAbbreviation().equals(abbreviation)) {
				courses.add(course);
			}
		}
		this.courses = courses;
		return this;
	}

	/**
	 * Filter the courses by the level of the class<br>
	 * A 1000-level class would have a level of "1"<br>
	 * A 2000-level class would have a level of "2"
	 *
	 * @param level the level of the class
	 * @return CourseFilter builder
	 */
	public CourseFilter classLevel(String level) {
		List<Course> courses = new ArrayList<Course>();
		for (Course course : this.courses) {
			if (course.getNumber().startsWith(level)) {
				courses.add(course);
			}
		}
		this.courses = courses;
		return this;
	}

	public List<Course> filter() {
		return courses;
	}
}
