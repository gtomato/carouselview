package com.gtomato.android.ui.transformer;

import android.support.annotation.FloatRange;
import android.view.View;

import com.gtomato.android.ui.manager.CarouselLayoutManager;
import com.gtomato.android.ui.widget.CarouselView;

/**
 * <p>Simulates effect of cover flow.</p>
 *
 * Available parameters:<br/>
 * <ul>
 *     <li>{@link #setOffsetXPercent(float)} linear X-axis translation rate. Default 0.8</li>
 *     <li>{@link #setScaleYFactor(float)} linear Y-axis scale rate. Default -0.2</li>
 *     <li>{@link #setYProjection(double)} Y-axis rotation degree when position = 2. Default 60</li>
 * </ul>
 *
 * @author  sunny-chung
 */

public class CoverFlowViewTransformer extends ParameterizableViewTransformer {
    private double mYProjection = 60.0;

    public CoverFlowViewTransformer() {
        setOffsetXPercent(0.8f);
        setScaleYFactor(-0.2f);
    }

    @Override
    public void onAttach(CarouselLayoutManager layoutManager) {
        layoutManager.setDrawOrder(CarouselView.DrawOrder.CenterFront);
    }

    @Override
    public void setOffsetXPercent(float offsetXPercent) {
        super.setOffsetXPercent(offsetXPercent);
    }

    @Override
    public float getOffsetXPercent() {
        return super.getOffsetXPercent();
    }

    @Override
    public float getScaleYFactor() {
        return super.getScaleYFactor();
    }

    @Override
    public void setScaleYFactor(float scaleYFactor) {
        super.setScaleYFactor(scaleYFactor);
    }

    public double getYProjection() {
        return mYProjection;
    }

    public void setYProjection(@FloatRange(from = 0.0, to = 90.0) double yProjectionDegree) {
        mYProjection = yProjectionDegree;
    }

    @Override
    public void transform(View view, float position) {
        int width = view.getMeasuredWidth(), height = view.getMeasuredHeight();
        view.setPivotX(width / 2.0f);
        view.setPivotY(height / 2.0f);
        view.setTranslationX(width * position * mOffsetXPercent);
        view.setRotationY(Math.signum(position) * (float)(Math.log(Math.abs(position) + 1) / Math.log(3) * - mYProjection));
        view.setScaleY(1 + mScaleYFactor * Math.abs(position));
    }
}
