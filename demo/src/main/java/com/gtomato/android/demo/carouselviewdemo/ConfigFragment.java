package com.gtomato.android.demo.carouselviewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.gtomato.android.ui.layout.ConfigRowLayout;
import com.gtomato.android.ui.widget.CarouselView;
import com.gtomato.android.util.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import butterknife.OnItemSelected;

/**
 * @author  sunny-chung
 */
public class ConfigFragment extends Fragment {

	@InjectView(R.id.tabContent) FrameLayout tabContentFrame;

	@InjectView(R.id.spnTransformer) Spinner spnTransformer;
	@InjectView(R.id.spnDrawOrder) Spinner spnDrawOrder;
	@InjectView(R.id.swhIsInfinite) Switch swhIsInfinite;
	@InjectView(R.id.swhIsScrollingAlignToViews) Switch swhIsScrollingAlignToViews;
	@InjectView(R.id.swhIsClipChildren) Switch swhIsClipChildren;
	@InjectView(R.id.sekItems) SeekBar sekItems;
	@InjectView(R.id.sekExtraChilds) SeekBar sekExtraChilds;
	@InjectView(R.id.sekItemWidth) SeekBar sekItemWidth;
	@InjectView(R.id.sekItemHeight) SeekBar sekItemHeight;
	@InjectView(R.id.spnGravity) Spinner spnGravity;
	@InjectView(R.id.spnLayoutWidth) Spinner spnLayoutWidth;
	@InjectView(R.id.spnLayoutHeight) Spinner spnLayoutHeight;
	@InjectView(R.id.rcvTransformerParameters) RecyclerView rcvTransformerParameters;
	@InjectView(R.id.tabPanel) TabLayout tabPanel;
	private ConfigRecyclerAdapter mConfigRecyclerAdapter;
	@InjectViews({R.id.sekItems, R.id.sekExtraChilds, R.id.sekItemWidth, R.id.sekItemHeight}) List<SeekBar> seekBars;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.option, container, false);
		ButterKnife.inject(this, rootView);
		setupView();
		return rootView;
	}

	private <T> ArrayAdapter<T> spinnerAdapterFromList(List<T> list) {
		ArrayAdapter<T> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}

	private void setupView() {
		List<String> transformerNames = new ArrayList<>(CarouselParameters.getTransformerNames());
		transformerNames.add("Custom");
		spnTransformer.setAdapter(spinnerAdapterFromList(transformerNames));

		spnDrawOrder.setAdapter(spinnerAdapterFromList(Arrays.asList(CarouselView.DrawOrder.values())));

		spnLayoutWidth.setAdapter(spinnerAdapterFromList(Arrays.asList(getResources().getStringArray(R.array.carousel_layout_width))));
		spnLayoutHeight.setAdapter(spinnerAdapterFromList(Arrays.asList(getResources().getStringArray(R.array.carousel_layout_height))));

		List<String> gravity = new ArrayList<>(CarouselParameters.GRAVITY.keySet());
		spnGravity.setAdapter(spinnerAdapterFromList(gravity));
		spnGravity.setSelection(gravity.indexOf("CENTER_HORIZONTAL"));

		for (SeekBar sek : seekBars) {
			sek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					updateDisplay();
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {

				}
			});
		}

		rcvTransformerParameters.setLayoutManager(new LinearLayoutManager(getContext()));

		mConfigRecyclerAdapter = new ConfigRecyclerAdapter(CarouselParameters.TRANSFORMER_CLASSES.get(0));
		rcvTransformerParameters.setAdapter(mConfigRecyclerAdapter);

		tabPanel.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				updateDisplay();
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tabPos", tabPanel.getSelectedTabPosition());
	}

	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("tabPos")) {
				tabPanel.getTabAt(savedInstanceState.getInt("savedInstanceState")).select();
			}
		}
	}

	@OnClick({R.id.swhIsInfinite, R.id.swhIsScrollingAlignToViews, R.id.swhIsClipChildren})
	@OnItemSelected({R.id.spnGravity, R.id.spnLayoutWidth, R.id.spnLayoutHeight})
	void _update3() {
		updateDisplay();
	}

	@OnItemSelected(R.id.spnTransformer)
	void onSelectDisplayMode() {
		mConfigRecyclerAdapter.setClass(
				spnTransformer.getSelectedItemPosition() < CarouselParameters.TRANSFORMER_CLASSES.size()
						? CarouselParameters.TRANSFORMER_CLASSES.get(spnTransformer.getSelectedItemPosition())
						: null
		);
		mConfigRecyclerAdapter.notifyDataSetChanged();
	}

	private void updateDisplay() {
		// update data
		((ConfigRowLayout) spnTransformer.getParent()).setText(spnTransformer.getSelectedItem().toString());
		((ConfigRowLayout) spnDrawOrder.getParent()).setText(spnDrawOrder.getSelectedItem().toString());
		((ConfigRowLayout) spnGravity.getParent()).setText(spnGravity.getSelectedItem().toString());
		((ConfigRowLayout) spnLayoutWidth.getParent()).setText(spnLayoutWidth.getSelectedItem().toString());
		((ConfigRowLayout) spnLayoutHeight.getParent()).setText(spnLayoutHeight.getSelectedItem().toString());
		((ConfigRowLayout) swhIsInfinite.getParent()).setText(swhIsInfinite.isChecked() ? "Yes" : "No");
		((ConfigRowLayout) swhIsScrollingAlignToViews.getParent()).setText(swhIsScrollingAlignToViews.isChecked() ? "Yes" : "No");
		((ConfigRowLayout) swhIsClipChildren.getParent()).setText(swhIsClipChildren.isChecked() ? "Yes" : "No");
		((ConfigRowLayout) sekItems.getParent()).setText("" + (sekItems.getProgress() + 1));
		((ConfigRowLayout) sekExtraChilds.getParent()).setText("" + sekExtraChilds.getProgress());
		((ConfigRowLayout) sekItemWidth.getParent()).setText(sekItemWidth.getProgress() == sekItemWidth.getMax() ? "Fill Parent" : "" + (sekItemWidth.getProgress() * 10 + 40));
		((ConfigRowLayout) sekItemHeight.getParent()).setText(sekItemHeight.getProgress() == sekItemHeight.getMax() ? "Fill Parent" : "" + (sekItemHeight.getProgress() * 10 + 40));

		// update "tab"
		int nChilds = tabContentFrame.getChildCount();
		for (int i = nChilds - 1; i >= 0; --i) {
			View child = tabContentFrame.getChildAt(i);
			child.setVisibility(tabPanel.getSelectedTabPosition() == i ? View.VISIBLE : View.INVISIBLE);
		}
	}

	@OnClick(R.id.btnPreview)
	public void previewCarousel(View view) {
		updateDisplay();

		Bundle args = new Bundle();
		String s;

		// carousel tab
		args.putBoolean("isInfinite", swhIsInfinite.isChecked());
		args.putBoolean("isAlign", swhIsScrollingAlignToViews.isChecked());
		args.putInt("drawOrder", spnDrawOrder.getSelectedItemPosition());
		args.putInt("extraChilds", Integer.parseInt(((ConfigRowLayout) sekExtraChilds.getParent()).getText().toString()));
		args.putInt("gravity", CarouselParameters.GRAVITY.get(spnGravity.getSelectedItem()));
		args.putBoolean("isClipChildren", swhIsClipChildren.isChecked());

		int layoutWidth;
		switch (spnLayoutWidth.getSelectedItemPosition()) {
			case 0:
			default: {
				layoutWidth = ViewGroup.LayoutParams.MATCH_PARENT;
			} break;

			case 1: {
				layoutWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
			} break;

			case 2: {
				layoutWidth = (int) Metrics.convertDpToPixel(160, getContext());
			} break;
		}
		args.putInt("layoutWidth", layoutWidth);

		int layoutHeight;
		switch (spnLayoutHeight.getSelectedItemPosition()) {
			case 0:
			default: {
				layoutHeight = ViewGroup.LayoutParams.MATCH_PARENT;
			} break;

			case 1: {
				layoutHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
			} break;

			case 2: {
				layoutHeight = (int) Metrics.convertDpToPixel(200, getContext());
			} break;
		}
		args.putInt("layoutHeight", layoutHeight);

		// transformer tab
		args.putInt("transformerPos", spnTransformer.getSelectedItemPosition());
		args.putSerializable("transformerParams", (HashMap<String, Number>) mConfigRecyclerAdapter.getSelectedValues());

		// adapter tab
		args.putInt("items", Integer.parseInt(((ConfigRowLayout) sekItems.getParent()).getText().toString()));
		s = ((ConfigRowLayout) sekItemWidth.getParent()).getText().toString();
		args.putInt("itemWidth", !s.equals("Fill Parent") ? Integer.parseInt(s) : -1);
		s = ((ConfigRowLayout) sekItemHeight.getParent()).getText().toString();
		args.putInt("itemHeight", !s.equals("Fill Parent") ? Integer.parseInt(s) : -1);

		Fragment f = new CarouselFragment();
		f.setArguments(args);

		getFragmentManager().beginTransaction()
				.replace(R.id.container, f)
				.addToBackStack("CarouselFragment")
				.commit();
	}
}
