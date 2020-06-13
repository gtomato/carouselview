package com.gtomato.android.ui.transformer;

import android.view.View;

import com.gtomato.android.ui.manager.CarouselLayoutManager;
import com.gtomato.android.ui.widget.CarouselView;

/**
 * Simulate a Merry-go-round carousel. <br/>
 * <br/>
 * This is a 'flat' version that item views are not rotated and always face front. <br/>
 * <br/>
 * Available parameters: <br/>
 * <ul>
 *   <li>number of item views to be distributed across the circle evenly. Default 7</li>
 *   <li>% of parent width to be used for display. Default 80%</li>
 *   <li>normalized camera height. Default 0.6</li>
 *   <li>scale of itemview when it is layout at a far distance (y = 0). Default 0.35</li>
 *   <li>alpha of itemview when it is layout at a far distance (y = 0). Default 0</li>
 * </ul>
 *
 * @author  sunny-chung
 */

public class FlatMerryGoRoundTransformer implements CarouselView.ViewTransformer {

    @Override
    public void onAttach(CarouselLayoutManager layoutManager) {
        layoutManager.setDrawOrder(CarouselView.DrawOrder.CenterFront);
    }

    @Override
    public void transform(View view, float position) {
        int parentWidth = ((View) view.getParent()).getMeasuredWidth();

        // depends on mNumPies
        int mNumPies = 7;
        double mPieRad = Math.PI * 2.0 / mNumPies;
        double rotateRad = Math.PI * 1.5 + position * mPieRad;
        double mHorizontalViewPort = 0.8;
        double a = parentWidth * mHorizontalViewPort / 2.0;
        double mViewPerspective = 0.6;
        double b = a * mViewPerspective;

        double x = a * Math.cos(rotateRad);
        double y = b * (1 - Math.sin(rotateRad)); // (1 - sin t) because y axis is reversed

        double maxY = 2 * b; // since maximum of (1 - sin t) => 2

        // TODO scale should depend on mViewPerspective as well
        double mFarScale = 0.35;
        double scale = Math.max(0, (mFarScale - 1) * (y - maxY) / (0 - maxY) + 1);
        double mFarAlpha = 0;
        double alpha = Math.max(0, (mFarAlpha - 1) * (y - maxY) / (0 - maxY) + 1);

        y -= maxY / 2; // reposition center so that y ∈ [-maxY/2, maxY/2]

        view.setTranslationX((float) x);
        view.setTranslationY((float) y);
        view.setScaleX((float) scale);
        view.setScaleY((float) scale);
        view.setAlpha((float) alpha);
    }
}
