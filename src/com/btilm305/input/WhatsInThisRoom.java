package com.btilm305.input;

import com.btilm305.object.ClassTime;
import com.btilm305.object.Course;
import com.btilm305.object.Section;
import com.btilm305.object.Time24Hours;

import java.util.List;
import java.util.Scanner;

public class WhatsInThisRoom {

	public static void main(String[] args) {
		List<Course> courses = null;
		Scanner scanner = new Scanner(System.in);
		System.out.print("Building --> ");
		String building = scanner.nextLine();
		System.out.print("Room --> ");
		String room = scanner.nextLine();
		System.out.print("24 hour time (hh:mm) --> ");
		String[] timeParts = scanner.nextLine().split(":");
		Time24Hours time = new Time24Hours(Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
		System.out.print("Day (M,T,W,Th,F) --> ");
		String dayString = scanner.nextLine();
		int day = 0;
		if (dayString.equals("M")) {
			day = ClassTime.MONDAY;
		} else if (dayString.equals("T")) {
			day = ClassTime.TUESDAY;
		} else if (dayString.equals("W")) {
			day = ClassTime.WEDNESDAY;
		} else if (dayString.equals("T")) {
			day = ClassTime.THURSDAY;
		} else if (dayString.equals("F")) {
			day = ClassTime.FRIDAY;
		}
		for (Course course : courses) {
			for (Section section : course.getSections()) {
				ClassTime t = section.getTime();
				if (section.getBuilding().equals(building) && section.getRoom().equals(room) && t.occursOnDay(day) && time.isInclusive(t.getStart(), t.getEnd())) {
					System.out.println(course + ", " + section);
				}
			}
		}
	}
}
