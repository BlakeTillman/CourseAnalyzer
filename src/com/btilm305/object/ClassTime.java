package com.btilm305.object;

import java.io.Serializable;

public class ClassTime implements Serializable {

	public static final int MONDAY = 1;
	public static final int TUESDAY = 1 << 1;
	public static final int WEDNESDAY = 1 << 2;
	public static final int THURSDAY = 1 << 3;
	public static final int FRIDAY = 1 << 4;

	private int days = 0;
	private Time24Hours start;
	private Time24Hours end;

	public ClassTime(String dateLetters, String timeRange) {
		if (dateLetters.charAt(0) == 'M') {
			days |= MONDAY;
		}
		if (dateLetters.charAt(1) == 'T') {
			days |= TUESDAY;
		}
		if (dateLetters.charAt(2) == 'W') {
			days |= WEDNESDAY;
		}
		if (dateLetters.charAt(3) == 'T') {
			days |= THURSDAY;
		}
		if (dateLetters.charAt(4) == 'F') {
			days |= FRIDAY;
		}
		boolean night = false;
		if (timeRange.endsWith("N")) {
			night = true;
			timeRange = timeRange.substring(0, timeRange.length() - 1);
		}
		String[] times = timeRange.split("-");
		int start = Integer.parseInt(times[0]);
		int end = Integer.parseInt(times[1]);
		if (start < 730 || night) {
			start += 1200;
			end += 1200;
		}
		if (end < start) {
			end += 1200;
		}
		this.start = new Time24Hours(start);
		this.end = new Time24Hours(end);
		if (dateLetters.charAt(0) != 'M' && dateLetters.charAt(0) != ' ') {
			System.out.println("Unknown1: " + dateLetters);
		} else if (dateLetters.charAt(1) != 'T' && dateLetters.charAt(1) != ' ') {
			System.out.println("Unknown2: " + dateLetters);
		} else if (dateLetters.charAt(2) != 'W' && dateLetters.charAt(2) != ' ') {
			System.out.println("Unknown3: " + dateLetters);
		} else if (dateLetters.charAt(3) != 'T' && dateLetters.charAt(3) != ' ') {
			System.out.println("Unknown4: " + dateLetters);
		} else if (dateLetters.charAt(4) != 'F' && dateLetters.charAt(4) != 'H' && dateLetters.charAt(4) != ' ') {
			System.out.println("Unknown5: " + dateLetters);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ClassTime{");
		if (occursOnDay(MONDAY)) {
			sb.append("M,");
		}
		if (occursOnDay(TUESDAY)) {
			sb.append("T,");
		}
		if (occursOnDay(WEDNESDAY)) {
			sb.append("W,");
		}
		if (occursOnDay(THURSDAY)) {
			sb.append("Th,");
		}
		if (occursOnDay(FRIDAY)) {
			sb.append("F,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" " + start.toString() + "-" + end.toString());
		sb.append("}");
		return sb.toString();
	}

	public boolean occursOnDay(int day) {
		return (days & day) == day;
	}

	public static String getDayAbbreviation(int day) {
		switch (day) {
			case MONDAY:
				return "M";
			case TUESDAY:
				return "T";
			case WEDNESDAY:
				return "W";
			case THURSDAY:
				return "Th";
			case FRIDAY:
				return "F";
		}
		return "undefined";
	}

	public Time24Hours getStart() {
		return start;
	}

	public Time24Hours getEnd() {
		return end;
	}
}
