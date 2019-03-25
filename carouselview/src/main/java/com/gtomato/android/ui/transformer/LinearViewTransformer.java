package com.gtomato.android.ui.transformer;

/**
 * <p>Linear carousel. </p>
 *
 * <p>Default it is a horizontal linear carousel.
 * You may set parameter offsetXPercent=0, offsetYPercent=1 for a vertical linear carousel.</p>
 *
 * @author  sunny-chung
 */

public class LinearViewTransformer extends ParameterizableViewTransformer {
    public LinearViewTransformer() {
        setOffsetXPercent(1);
        setOffsetYPercent(0);
    }
}
