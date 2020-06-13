package com.gtomato.android.ui.transformer;

import android.view.View;

import com.gtomato.android.ui.manager.CarouselLayoutManager;
import com.gtomato.android.ui.scrolltweaker.InverseScroller;
import com.gtomato.android.ui.widget.CarouselView;

/**
 * Time-machine like carousel.
 *
 * @author  sunny-chung
 */

public class TimeMachineViewTransformer implements CarouselView.ViewTransformer {
    private static final float translationXRate = (-1) * 0.5f;

    @Override
    public void onAttach(CarouselLayoutManager layoutManager) {
        layoutManager.setScroller(new InverseScroller());
        layoutManager.setDrawOrder(CarouselView.DrawOrder.FirstFront);
    }

    @Override
    public void transform(View view, float position) {
        int width = view.getMeasuredWidth();
        view.setTranslationX(width * position * translationXRate * (2f / (Math.abs(position) + 2)));
        view.setScaleX(2f / (position + 2));
        view.setScaleY(2f / (position + 2));
        view.setAlpha(position < 0 ? Math.max(1 + position, 0) : 1);
    }
}
