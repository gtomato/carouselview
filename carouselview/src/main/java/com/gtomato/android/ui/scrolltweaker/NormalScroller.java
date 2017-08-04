package com.gtomato.android.ui.scrolltweaker;

import com.gtomato.android.ui.widget.CarouselView;

/**
 * Normal scroller.
 *
 * @author  sunny-chung
 */

public class NormalScroller implements CarouselView.Scroller {
    @Override
    public int tweakScrollDx(int dx) {
        return dx;
    }

    @Override
    public int tweakScrollDy(int dy) {
        return dy;
    }

    @Override
    public int inverseTweakScrollDx(int dx) {
        return dx;
    }

    @Override
    public int inverseTweakScrollDy(int dy) {
        return dy;
    }

    @Override
    public float tweakScrollDx(float dx) {
        return dx;
    }

    @Override
    public float tweakScrollDy(float dy) {
        return dy;
    }
}
