package com.gtomato.android.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author  sunny-chung
 */
public class Metrics {
	/**
	 * Covert dp to px
	 * @param dp
	 * @param context
	 * @return pixel
	 */
	public static float convertDpToPixel(float dp, Context context){
		return dp * getDensity(context);
	}

	/**
	 * Get density
	 * 120dpi = 0.75
	 * 160dpi = 1 (default)
	 * 240dpi = 1.5
	 * @param context
	 * @return
	 */
	private static float getDensity(Context context){
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return metrics.density;
	}
}
