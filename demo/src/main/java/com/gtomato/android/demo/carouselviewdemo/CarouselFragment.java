package com.gtomato.android.demo.carouselviewdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gtomato.android.ui.manager.CarouselLayoutManager;
import com.gtomato.android.ui.widget.CarouselView;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author  sunny-chung
 */
public class CarouselFragment extends Fragment {
	private CarouselView carousel;
	private TextView lblSelectedIndex;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final Bundle args = getArguments();

		carousel = getView().findViewById(R.id.carousel);
		lblSelectedIndex = getView().findViewById(R.id.lblSelectedIndex);

		ViewGroup.LayoutParams lp = carousel.getLayoutParams();
		lp.width = args.getInt("layoutWidth");
		lp.height = args.getInt("layoutHeight");
		carousel.setLayoutParams(lp);
		getView().requestLayout();

		carousel.setAdapter(new RandomPageAdapter(args.getInt("items"), args.getInt("itemWidth"), args.getInt("itemHeight")));

		boolean isClipChildren = args.getBoolean("isClipChildren");
		carousel.setClipChildren(isClipChildren);
		((ViewGroup) carousel.getParent()).setClipChildren(isClipChildren);
		((ViewGroup) carousel.getParent()).setClipToPadding(isClipChildren);

		carousel.setInfinite(args.getBoolean("isInfinite"));
		carousel.setExtraVisibleChilds(args.getInt("extraChilds"));
		carousel.getLayoutManager().setDrawOrder(CarouselView.DrawOrder.values()[args.getInt("drawOrder")]);

		carousel.setGravity(args.getInt("gravity"));

		carousel.setScrollingAlignToViews(args.getBoolean("isAlign"));

		carousel.setOnItemSelectedListener(new CarouselView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(int position) {
				lblSelectedIndex.setText("Selected Position " + position);
			}
		});

		int transformerSelectedPos = args.getInt("transformerPos");
		CarouselView.ViewTransformer transformer;
		if (transformerSelectedPos < CarouselParameters.TRANSFORMER_CLASSES.size()) {
			// built-in transformer
			transformer = CarouselParameters.createTransformer(
					CarouselParameters.TRANSFORMER_CLASSES.get(transformerSelectedPos),
					(Map<String, Number>) args.getSerializable("transformerParams")
			);
		} else {
			// custom transformer
			transformer = new CarouselView.ViewTransformer() {
				@Override
				public void onAttach(CarouselLayoutManager layoutManager) {

				}

				@Override
				public void transform(View view, float position) {
					int width = view.getWidth(), height = view.getHeight();
					float alpha, transX, scale;
					if (-5 < position && position < 1) { // (-5, 1)
						if (position <= 0) { // (-5, 0]
							// position		-∞	...	-5		-4		-3		-2		-1		0
							// alpha		0	...	0		0.2		0.4		0.6		0.8		1.0
							// transX		-∞	...	-1.0w	-0.8w	-0.6w	-0.4w	-0.2w	0w
							// scale		0	...	0.5		0.6		0.7		0.8		0.9		1.0
							alpha = Math.max(0f, 1.0f + position * 0.2f);
							transX = position * width * 0.2f;
							scale = Math.max(0f, 1.0f - 0.4f * (float) Math.pow(position * 0.2, 2.0)); // s = 1 - 0.4 * (0.2p)^2
						} else /*if (position < 1) */ { // (0, 1)
							// position		0		0.5		1		...		+∞
							// alpha		1.0		0.5		0		...		0
							// transX		0pw		0.25pw	0.5pw	...		+∞
							// scale		1.0		2.25	3.5		...		+∞
							alpha = Math.max(0f, 1.0f - position);
							transX = position * carousel.getWidth() / 2;
							scale = 1.0f + position * 2.5f;
						}
						view.setAlpha(alpha);
						view.setTranslationX(transX);
						view.setTranslationY(carousel.getHeight() / 2f - height / 2f); // center vertically
						view.setScaleX(scale);
						view.setScaleY(scale);
						view.setVisibility(View.VISIBLE);
					} else { // (-∞, 5] | [1, +∞)
						// explicitly set visibility instead of alpha to improve performance
						view.setVisibility(View.GONE);
					}
				}
			};
		}

		carousel.setTransformer(transformer);

		carousel.post(new Runnable() {
			@Override
			public void run() {
				// smooth scroll to the 'centermost' item
				carousel.smoothScrollToPosition((args.getInt("items") - 1) / 2);
			}
		});
	}

	static class ViewHolder extends CarouselView.ViewHolder {
		@InjectView(R.id.carousel_child_container) TextView textView;

		ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.inject(this, itemView);
		}
	}

	static class RandomPageAdapter extends CarouselView.Adapter<ViewHolder> {
		final int size;
		final int width;
		final int height;

		RandomPageAdapter(int size, int width, int height) {
			super();
			this.size = size;
			this.width = width;
			this.height = height;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			Context context = parent.getContext();
			View view = RandomPageFragment.createView(context, width, height, "0");
			return new ViewHolder(view);
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			RandomPageFragment.initializeTextView(holder.textView, (position + 1));
		}

		@Override
		public int getItemCount() {
			return size;
		}
	}
}
