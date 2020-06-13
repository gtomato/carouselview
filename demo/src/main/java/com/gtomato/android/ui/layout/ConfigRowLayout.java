package com.gtomato.android.ui.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author  sunny-chung
 */
public class ConfigRowLayout extends ViewGroup {
	private TextView mTextView = null;
	private View mContentView = null;

	private CharSequence mText;

	// layout-pass variables
	private int mMaxHeight, mContentViewAvailableWidth;
	private Size mTextViewSize, mContentViewSize;

	public ConfigRowLayout(Context context) {
		super(context);
	}

	public ConfigRowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ConfigRowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int childCount = getChildCount();

		if (childCount > 2) {
			throw new IllegalArgumentException("Number of childs cannot be larger than 2 for " + getClass().getSimpleName() + ".");
		}

		int topOffset = getPaddingTop();
		int leftOffset = getPaddingLeft();
		int rightOffset = getPaddingRight();
		int bottomOffset = getPaddingBottom();

		int contentWidth = getMeasuredWidth() - leftOffset - rightOffset;

		mMaxHeight = 0;
		for (int i = childCount - 1; i >= 0; --i) {
			View child = getChildAt(i);

			if (child.getVisibility() != View.GONE) {
				if (child.getClass().equals(TextView.class)) {
					mTextView = (TextView) child;
				} else {
					mContentView = child;
				}
			}
		}

		if (mContentView == null) {
			mContentView = mTextView;
			mTextView = null;
		}

		updatePendingText();

		if (mTextView != null) {
			if (mTextView.getGravity() != Gravity.END) {
				mTextView.setGravity(Gravity.END);
			}
			mTextViewSize = measureChildSize(mTextView, contentWidth);
			mMaxHeight = Math.max(mMaxHeight, mTextViewSize.height);
		}

		mContentViewAvailableWidth = contentWidth - (mTextViewSize != null ? mTextViewSize.width /*+ (int) Metrics.convertDpToPixel(8, getContext())*/ : 0);

		if (mContentView != null) {
			mContentViewSize = measureChildSize(mContentView, mContentViewAvailableWidth);
			mMaxHeight = Math.max(mMaxHeight, mContentViewSize.height);
		}

		setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(mMaxHeight + topOffset + bottomOffset, MeasureSpec.EXACTLY));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int leftOffset = getPaddingLeft();
		int rightOffset = getPaddingRight();

		int contentWidth = getMeasuredWidth() - leftOffset - rightOffset;

		if (mTextView != null) {
			mTextView.layout(leftOffset, (mMaxHeight - mTextViewSize.height) / 2, leftOffset + contentWidth, (mMaxHeight + mTextViewSize.height) / 2);
		}

		if (mContentView != null) {
			int width = mContentView.getLayoutParams().width == LayoutParams.MATCH_PARENT ? mContentViewAvailableWidth : mContentViewSize.width;
			mContentView.layout(leftOffset, (mMaxHeight - mContentViewSize.height) / 2, leftOffset + width, (mMaxHeight + mContentViewSize.height) / 2);
		}
	}

	public void setText(CharSequence text) {
		if (mTextView != null) {
			mTextView.setText(text);
			mText = null;
		} else {
			mText = text;
		}
	}

	public CharSequence getText() {
		return mTextView.getText();
	}

	private void updatePendingText() {
		if (mTextView != null && mText != null) {
			mTextView.setText(mText);
			mText = null;
		}
	}

	private Size measureChildSize(View child, int parentWidth) {
		child.measure(
				MeasureSpec.UNSPECIFIED == MeasureSpec.UNSPECIFIED
						? MeasureSpec.UNSPECIFIED
						: MeasureSpec.makeMeasureSpec(parentWidth, MeasureSpec.AT_MOST),
				MeasureSpec.UNSPECIFIED == MeasureSpec.UNSPECIFIED
						? MeasureSpec.UNSPECIFIED
						: MeasureSpec.makeMeasureSpec(MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST)
		);
		return new Size(child.getMeasuredWidth(), child.getMeasuredHeight());
	}

	private static class Size {
		final int width;
		final int height;

		Size(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}
}
