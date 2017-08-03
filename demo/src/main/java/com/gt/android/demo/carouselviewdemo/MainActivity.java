package com.gt.android.demo.carouselviewdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.gt.android.demo.carouselviewdemo.R;
import com.gt.android.ui.widget.CarouselView;


public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new ConfigFragment())
					.commit();
		}
		CarouselView.setDebug(true);
	}


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//
//		//noinspection SimplifiableIfStatement
//		if (id == R.id.action_settings) {
//			return true;
//		}
//
//		return super.onOptionsItemSelected(item);
//	}

//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//		                         Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//			return rootView;
//		}
//
//		@Override
//		public void onStart() {
//			super.onStart();
//			CarouselViewPager pager = (CarouselViewPager) getView().findViewById(R.id.pager);
//			pager.setAdapter(new RandomPagerAdapter(getChildFragmentManager()));
//		}
//	}
//
//	public static class RandomPagerAdapter extends FragmentStateCarouselPagerAdapter {
//		static final int size = 2;
//
//		public RandomPagerAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public android.support.v4.app.Fragment getItem(int position) {
//			RandomPageFragment f = new RandomPageFragment();
//			Bundle args = new Bundle();
//			args.putString(RandomPageFragment.TEXT, "" + (position + 1));
//			f.setArguments(args);
//			return f;
//		}
//
//		@Override
//		public int getCount() {
//			return size;
//		}
//
////		@Override
////		public float getPageWidth(int position) {
////			return 1 / 5f;
////		}
//	}
}
