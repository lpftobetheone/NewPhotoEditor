package com.zentertain.common.util;

import android.content.Context;
import android.graphics.Point;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class UITool {
	
	public static void toastCenter(Context context, String text) {
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static int dp2px(Context context, int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5F);
	}

	public static boolean inRangeOfView(View view, Point pp){
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		if(pp.x < x || pp.x > (x + view.getWidth()) || pp.y < y || pp.y > (y + view.getHeight())){
			return false;
		}
		return true;
	}

}
