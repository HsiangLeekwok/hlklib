package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.hlk.hlklib.etc.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>功能：</b>根据内容自动换行的layout<br />
 * <b>作者：</b>Hsiang Leekwok <br />
 * <b>时间：</b>2016/07/06 14:00 <br />
 * <b>邮箱：</b>xiang.l.g@gmail.com <br />
 */
public class AutoWrapLinearLayout extends ViewGroup {

    private final List<Integer> mLineHeights = new ArrayList<>();

    public AutoWrapLinearLayout(Context context) {
        super(context);
    }

    public AutoWrapLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoWrapLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        public final int horizontalSpacing;
        public final int verticalSpacing;

        /**
         * @param horizontalSpacing Pixels between items, horizontally
         * @param verticalSpacing   Pixels between items, vertically
         */
        public LayoutParams(int horizontalSpacing, int verticalSpacing) {
            super(0, 0);
            this.horizontalSpacing = horizontalSpacing;
            this.verticalSpacing = verticalSpacing;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED)
            throw new AssertionError();

        final int maxWidth = MeasureSpec.getSize(widthMeasureSpec);// - getPaddingLeft() - getPaddingRight();
        int width = 0;
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        final int count = getChildCount();
        int currentLineHeight = 0;
        int currentHeight = 0;
        mLineHeights.clear();

        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();
        int verticalSpacing = 0;

        int childHeightMeasureSpec;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        } else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                child.measure(MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST), childHeightMeasureSpec);
                final int childw = child.getMeasuredWidth();
                currentHeight = Math.max(currentLineHeight, child.getMeasuredHeight() + lp.verticalSpacing);
                verticalSpacing = Math.max(verticalSpacing, lp.verticalSpacing);

                if (xpos + childw > maxWidth) {
                    xpos = getPaddingLeft();
                    ypos += currentLineHeight;
                    mLineHeights.add(currentLineHeight);
                    currentLineHeight = currentHeight;
                } else {
                    width = Math.max(xpos + childw, width);
                    currentLineHeight = currentHeight;
                }

                xpos += childw + lp.horizontalSpacing;
            }
        }
        mLineHeights.add(currentHeight);

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = ypos + currentLineHeight + verticalSpacing;
        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            if (ypos + currentLineHeight + verticalSpacing < height) {
                height = ypos + currentLineHeight + verticalSpacing;
            }
        }

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = maxWidth;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        int spacing = Utility.ConvertDp(3);
        // default spacing
        return new LayoutParams(spacing, spacing);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams params) {
        return params instanceof LayoutParams;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int width = r - l;
        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();
        int currentLine = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final int childw = child.getMeasuredWidth();
                final int childh = child.getMeasuredHeight();
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (xpos + childw > width) {
                    xpos = getPaddingLeft();
                    ypos += mLineHeights.get(currentLine++);
                }
                child.layout(xpos, ypos, xpos + childw, ypos + childh);
                xpos += childw + lp.horizontalSpacing;
            }
        }
    }
}
