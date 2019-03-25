package com.gtomato.android.ui.transformer;

import android.view.View;

import com.gtomato.android.ui.manager.CarouselLayoutManager;
import com.gtomato.android.ui.widget.CarouselView;

/**
 * A super-class implementation of ViewTransformer allowing to alter common transform properties.
 *
 * Developers to choose what methods to be public for users to alter.
 *
 * @see ParameterizedViewTransformer
 * @see LinearViewTransformer
 * @see WheelViewTransformer
 * @see CoverFlowViewTransformer
 *
 * @author  sunny-chung
 */

public abstract class ParameterizableViewTransformer implements CarouselView.ViewTransformer {
    private static final float EPS = 1e-3f;

    float mOffsetXPercent = 0f;
    private float mOffsetYPercent = 0f;
    float mScaleYFactor = Float.NaN;
    private float mRotateDegree = Float.NaN;

    private float mRotateDistFactor = Float.NaN; // depends on mRotateDegree

    ParameterizableViewTransformer() { }

    @Override
    public void onAttach(CarouselLayoutManager layoutManager) { }

    void setOffsetXPercent(float offsetXPercent) {
        mOffsetXPercent = offsetXPercent;
    }

    void setOffsetYPercent(float offsetYPercent) {
        mOffsetYPercent = offsetYPercent;
    }

    void setRotateDegree(float rotateDegree) {
        mRotateDegree = rotateDegree;
        if (Float.isNaN(rotateDegree)) {
            mRotateDistFactor = Float.NaN;
        } else if (!isNonZero(rotateDegree)) {
            mRotateDistFactor = 0;
        } else {
            mRotateDistFactor = (float) (1 / Math.sin(Math.toRadians(rotateDegree)));
        }
    }

    void setScaleYFactor(float scaleYFactor) {
        mScaleYFactor = scaleYFactor;
    }

    private static boolean isNonZero(float f) {
        return f > EPS || f < -EPS;
    }

    @Override
    public void transform(View view, float position) {
        int width = view.getMeasuredWidth(), height = view.getMeasuredHeight();

        boolean mScaleLargestAtCenter = false;
        float mScaleXFactor = Float.NaN;
        if (!Float.isNaN(mScaleXFactor)) {
            float scale = (mScaleLargestAtCenter ? 1 - Math.abs(position) : position) * mScaleXFactor;
            view.setPivotX(width / 2.0f);
            view.setPivotY(height / 2.0f);
            view.setScaleX(scale);
        }

        if (!Float.isNaN(mScaleYFactor)) {
            float scale = (mScaleLargestAtCenter ? 1 - Math.abs(position) : position) * mScaleYFactor;
            view.setPivotX(width / 2.0f);
            view.setPivotY(height / 2.0f);
            view.setScaleY(scale);
        }

        if (isNonZero(mRotateDegree)) {
            view.setPivotX(width / 2.0f);
            view.setPivotY(height + width * mRotateDistFactor);
            view.setRotation(position * mRotateDegree);
        }

        if (isNonZero(mOffsetXPercent)) {
            view.setTranslationX(position * width * mOffsetXPercent);
        }

        if (isNonZero(mOffsetYPercent)) {
            view.setTranslationY(position * height * mOffsetYPercent);
        }
    }
}
