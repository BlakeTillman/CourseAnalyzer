package com.btilm305.object;

public class Population implements Comparable<Population> {

	private Time24Hours time;
	private int day;
	private int population;

	public Population(Time24Hours time, int day, int population) {
		this.time = time;
		this.day = day;
		this.population = population;
	}

	public Time24Hours getTime() {
		return time;
	}

	public int getDay() {
		return day;
	}

	public int getPopulation() {
		return population;
	}

	@Override
	public String toString() {
		return String.format("(%d) @ %s on %s", population, time.toString(), ClassTime.getDayAbbreviation(day));
	}

	public int compareTo(Population o) {
		return o.population - population;
	}
}
