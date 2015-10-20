package com.btilm305.object;

public class Time24Hours implements Comparable<Time24Hours> {

	private int hours;
	private int minutes;

	public Time24Hours(int combined) {
		minutes = combined % 100;
		hours = (combined - minutes) / 100;
	}

	public Time24Hours(int hours, int minutes) {
		this.hours = hours;
		this.minutes = minutes;
	}

	public int getHours() {
		return hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public boolean isInclusive(Time24Hours t1, Time24Hours t2) {
		int time = hours * 100 + minutes;
		int time1 = t1.hours * 100 + t1.minutes;
		int time2 = t2.hours * 100 + t2.minutes;
		return time >= time1 && time <= time2;
	}

	public int minutesUntil(Time24Hours t) {
		int h = t.hours - hours;
		int m = t.minutes - minutes;
		return h * 60 + m;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Time24Hours that = (Time24Hours) o;

		if (hours != that.hours) {
			return false;
		}
		return minutes == that.minutes;
	}

	@Override
	public int hashCode() {
		int result = hours;
		result = 31 * result + minutes;
		return result;
	}

	@Override
	public String toString() {
		return String.format("%02d:%02d%s", ((hours + 11) % 12 + 1), minutes, (hours >= 12 ? "PM" : "AM"));
	}

	@Override
	public int compareTo(Time24Hours time) {
		return (hours * 100 + minutes) - (time.hours * 100 + time.minutes);
	}
}
