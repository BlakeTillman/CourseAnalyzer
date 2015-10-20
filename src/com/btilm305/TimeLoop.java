package com.btilm305;

import com.btilm305.object.ClassTime;
import com.btilm305.object.Time24Hours;

public abstract class TimeLoop {

	public abstract void loop(int day, Time24Hours time);

	public static void doLoop(TimeLoop loop) {
		for (int day = ClassTime.MONDAY; day <= ClassTime.FRIDAY; day *= 2) {
			for (int h = 1; h < 23; h++) {
				for (int m = 0; m < 60; m += 10) {
					Time24Hours t = new Time24Hours(h, m);
					loop.loop(day, t);
				}
			}
		}
	}
}
