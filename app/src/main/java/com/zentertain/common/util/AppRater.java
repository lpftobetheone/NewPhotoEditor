/*
 * Comet Keyboard Library
 * Copyright (C) 2011-2012 Comet Inc.
 * All Rights Reserved
 */

package com.zentertain.common.util;


import android.content.Context;
import android.content.SharedPreferences;

public class AppRater {
	// private final static int MILLISECONDS_PER_DAY = 60 * 60 * 24 * 1000;
	// private final static int WAIT_BETWEEN_REQUESTS = 7 * MILLISECONDS_PER_DAY;
	private final static int RUNNING_COUNT_FOR_RATING = 5;

	public static void promptUserToRate(Context context) {
		// Rating
		if (isReadyToShow(context) && (!AdManager.isAdEnabled || (AdManager.isActive == true && AdManager.isShowingAdMob == false))) {
			DialogTool.showRate(context);
		}
	}
	
	
	private static boolean isReadyToShow(Context context) {
		SharedPreferences prefs = context.getSharedPreferences("apprater", 0);
		if (prefs.getBoolean("dont_show_again", false)) {
			// User either rated app, or doesn't want to
			return false;
		}

		// Check time of last request
		long running_count = prefs.getLong("running_count", 0);
		// This is the first call. Save the time of this request.
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("running_count", ++running_count);
		editor.commit();
		if(running_count >= RUNNING_COUNT_FOR_RATING) {
			return true;
		}

		return false;
	}
}