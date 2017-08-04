package com.gtomato.android.demo.carouselviewdemo;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gtomato.android.util.Metrics;

import java.util.Random;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class RandomPageFragment extends Fragment {
	public final static String TEXT = "TEXT";

	private String text;

	public RandomPageFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		text = getArguments().getString(TEXT);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return createView(getActivity(), 100, 150, text);
	}

	public static View createView(Context context, int widthDp, int heightDp, String text) {
//		FrameLayout l = new FrameLayout(context);
//		l.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		Log.d("page", "w = " + widthDp + ", h = " + heightDp);

		TextView textView = new TextView(context);
		textView.setId(R.id.carousel_child_container);
		textView.setLayoutParams(new FrameLayout.LayoutParams(
				widthDp > 0 ? (int) Metrics.convertDpToPixel(widthDp, context) : ViewGroup.LayoutParams.MATCH_PARENT,
				heightDp > 0 ? (int) Metrics.convertDpToPixel(heightDp, context) : ViewGroup.LayoutParams.MATCH_PARENT
		));
		textView.setGravity(Gravity.CENTER);
		textView.setTextSize(48);

		initializeTextView(textView, Integer.parseInt(text));

//		((FrameLayout.LayoutParams) textView.getLayoutParams()).gravity = Gravity.CENTER_HORIZONTAL;
//		l.addView(textView);

//		return l;
		return textView;
	}

	public static void initializeTextView(TextView textView, int num) {
		int bgColor = randomColor(num);
		textView.setText("" + num);
		textView.setBackgroundColor(bgColor);
		textView.setTextColor(getContrastColor(bgColor));
	}

	private static int randomColor(int seed) {
		return new Random(seed).nextInt(0x1000000) + 0xFF000000;
	}

	private static int getContrastColor(int color) {
		return Color.rgb(255 - Color.red(color), 255 - Color.green(color), 255 - Color.blue(color));
	}


}
