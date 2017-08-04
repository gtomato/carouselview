package com.gtomato.android.ui.transformer;

import android.view.View;

import com.gtomato.android.ui.manager.CarouselLayoutManager;
import com.gtomato.android.ui.widget.CarouselView;

/**
 * <p>Immutable transformer.</p>
 *
 * @author  sunny-chung
 */

public final class ImmutableTransformer implements CarouselView.ViewTransformer {
    private CarouselView.ViewTransformer mTransformer;

    public ImmutableTransformer(CarouselView.ViewTransformer transformer) {
        mTransformer = transformer;
    }

    @Override
    public final void onAttach(CarouselLayoutManager layoutManager) {
        mTransformer.onAttach(layoutManager);
    }

    @Override
    public final void transform(View view, float position) {
        mTransformer.transform(view, position);
    }
}
