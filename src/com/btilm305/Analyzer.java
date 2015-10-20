package com.btilm305;

import com.btilm305.filter.SectionFilter;
import com.btilm305.object.ClassTime;
import com.btilm305.object.Course;
import com.btilm305.object.Section;
import com.btilm305.object.Time24Hours;
import com.btilm305.report.Report;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Analyzer {

	public static void main(String[] args) {
		new Analyzer();
	}

	private School school = new School();
	private Section previousSection = null;

	public Analyzer() {
		try {
			long start = System.currentTimeMillis();
			File courseCacheFile = new File("course_cache");
			if (!courseCacheFile.exists()) {
				courseCacheFile.mkdirs();
				new CourseDownloader(new File("courses.txt"), courseCacheFile).run();
			}
			for (File file : courseCacheFile.listFiles()) {
				if (file.getName().endsWith(".txt")) {
					String text = Util.fileToString(file);
					for (String line : text.split("\n")) {
						parseLine(line);
					}
				}
			}
			System.out.println("Analysis took " + (System.currentTimeMillis() - start) + "ms");
			List<Section> things = new SectionFilter(school.getSections()).day(ClassTime.WEDNESDAY).time(new Time24Hours(12 + 9, 0)).filter();
			for (Section s : things) {
				System.out.println(s.getCourse().toString() + ": " + s);
			}
			Report.run(school);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void parseLine(String line) {
		boolean additional = isAdditionSection(line);
		if (line.trim().startsWith("*")) {
			handleNote(line);
			return;
		}
		if (!additional && (looksEmpty(line))) {
			return;
		}
		line = rightPad(line, 118);
		Section section = new Section();
		section.setAvailable(line.substring(0, 3).trim());
		String enrolled = line.substring(6, 9).trim();
		section.setEnrolled(enrolled);
		section.setLab(line.substring(21, 24).equals("LAB"));
		section.setSectionNumber(line.substring(27, 30).trim());
		section.setHours(line.substring(55, 60).trim());
		String timeRange = line.substring(60, 70).trim();
		if (timeRange.trim().equals("TBA")) {
			return;
		}
		section.setTime(new ClassTime(line.substring(72, 77), timeRange));
		section.setRoom(line.substring(79, 84).trim());
		section.setBuilding(line.substring(84, 100).trim());
		section.setInstructor(line.substring(117, line.length()).trim());
		String courseAbbreviation = line.substring(11, 15).trim();
		String courseNumber = line.substring(16, 20).trim();
		String courseTitle = line.substring(32, 55).trim();
		if (additional) {
			section.setEnrolled(previousSection.getEnrolled() + "");
			section.setAvailable(previousSection.getAvailable());
			courseAbbreviation = previousSection.getCourse().getAbbreviation();
			courseNumber = previousSection.getCourse().getNumber();
			if (section.getRoom().isEmpty()) {
				section.setRoom(previousSection.getRoom());
			}
			if (section.getBuilding().equals("undefined")) {
				section.setBuilding(previousSection.getBuilding());
			}
			if (section.getInstructor().isEmpty()) {
				section.setInstructor(previousSection.getInstructor());
			}
		}

		Course course = getCourse(courseAbbreviation, courseNumber);
		if (course == null) {
			course = new Course(courseAbbreviation, courseNumber, courseTitle);
			school.getCourses().add(course);
		}
		course.addSection(section);
		school.getSections().add(section);
		if (!additional) {
			previousSection = section;
		}
	}

	private void handleNote(String line) {
		int index = line.indexOf("CLASS WILL BE HELD IN ");
		if (index != -1 && previousSection != null) {
			String location = line.substring(index + 22, line.length());
			String[] words = location.split(" ");
			String building = location;
			String room = "";
			if (words[0].matches(".*\\d+.*")) {
				room = words[0];
				building = location.substring(room.length() + 1);
			} else if (words[words.length - 1].matches(".*\\d+.*")) {
				room = words[words.length - 1];
				building = location.substring(0, location.length() - room.length() - 1);
			}
			previousSection.setBuilding(building);
			previousSection.setRoom(room);
		}
	}

	private String rightPad(String string, int length) {
		StringBuilder sb = new StringBuilder(string);
		while (sb.length() < 117) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private void parseLab(String line) {
		Section section = new Section();
		section.setLab(true);
		String timeRange = line.substring(60, 70).trim();
		if (timeRange.trim().equals("TBA")) {
			return;
		}
		section.setTime(new ClassTime(line.substring(72, 77), timeRange));
		section.setRoom(line.substring(79, 84).trim());
		section.setBuilding(line.substring(84, 100).trim());
		section.setInstructor(line.substring(117, line.length()).trim());
		//fill in data from previous line
		section.setAvailable(previousSection.getAvailable());
		section.setEnrolled(previousSection.getEnrolled() + "");
		section.setSectionNumber(previousSection.getSectionNumber());
		previousSection.getCourse().addSection(section);
		school.getSections().add(section);
	}

	private boolean isAdditionSection(String line) {
		return line.length() > 70 && line.substring(0, 21).trim().isEmpty() && line.substring(24, 60).trim().isEmpty() && !line.substring(60, 70).trim().isEmpty();
	}

	private boolean looksEmpty(String line) {
		return line.length() < 117 || (line.charAt(0) == ' ' && line.charAt(1) == ' ' && line.charAt(2) == ' ') || (line.charAt(0) != ' ' && line.charAt(0) != '(' && (line.charAt(0) < '0' || line.charAt(0) > '9'));
	}

	private Course getCourse(String abbreviation, String number) {
		//using CourseFilter here would be really slow and would take up the same amount of code
		for (Course course : school.getCourses()) {
			if (course.getAbbreviation().equals(abbreviation) && course.getNumber().equals(number)) {
				return course;
			}
		}
		return null;
	}
}
