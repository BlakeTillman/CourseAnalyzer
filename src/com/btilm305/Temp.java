package com.btilm305;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Temp {

	public static void main(String[] args) {
		int days = 115;
		double dailyCost = 375.0 / 116.0;
		for (int i = 0; i <= days; i++) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date(2015, 7, 19));
			cal.add(Calendar.DATE, i); //minus number would decrement the days
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
			System.out.println(sdf.format(cal.getTime()) + String.format(",$%.02f", 375 - dailyCost * (i+1)));
		}
	}
}
