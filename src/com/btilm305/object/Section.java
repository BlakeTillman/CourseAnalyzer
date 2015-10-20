package com.btilm305.object;

import java.io.Serializable;

public class Section implements Serializable {

	private Course course;
	private String available;
	private int enrolled;
	private boolean lab;
	private String sectionNumber;
	private String hours;
	private ClassTime time;
	private String room;
	private String building;
	private String instructor;

	protected void setCourse(Course course) {
		this.course = course;
	}

	public Course getCourse() {
		return course;
	}

	public String getAvailable() {
		return available;
	}

	public void setAvailable(String available) {
		this.available = available;
	}

	public int getEnrolled() {
		return enrolled;
	}

	public void setEnrolled(String enrolled) {
		if (enrolled.isEmpty()) {
			enrolled = "0";
		}
		this.enrolled = Integer.parseInt(enrolled);
	}

	public boolean isLab() {
		return lab;
	}

	public void setLab(boolean lab) {
		this.lab = lab;
	}

	public String getSectionNumber() {
		return sectionNumber;
	}

	public void setSectionNumber(String sectionNumber) {
		this.sectionNumber = sectionNumber;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public ClassTime getTime() {
		return time;
	}

	public void setTime(ClassTime time) {
		this.time = time;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		if (building.isEmpty()) {
			building = "undefined";
		}
		this.building = building;
	}

	public String getInstructor() {
		return instructor;
	}

	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	@Override
	public String toString() {
		return "Section{" +
				"available='" + available + '\'' +
				", enrolled=" + enrolled +
				", sectionNumber='" + sectionNumber + '\'' +
				", hours='" + hours + '\'' +
				", time=" + time +
				", room='" + room + '\'' +
				", building='" + building + '\'' +
				", instructor='" + instructor + '\'' +
				'}';
	}
}
