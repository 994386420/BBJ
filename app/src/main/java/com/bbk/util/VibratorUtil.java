package com.bbk.util;

import android.content.Context;
import android.os.Vibrator;

public class VibratorUtil {
	
	public static void vibrate(Context context, long milliSeconds) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(milliSeconds);
	}

}
