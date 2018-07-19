package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.Utility;

/**
 * <b>功能</b>：带边框的view<br />
 * <b>作者</b>：Hsiang Leekwok <br />
 * <b>时间</b>：2016/03/20 20:11 <br />
 * <b>邮箱</b>：xiang.l.g@gmail.com <br />
 */
public class CorneredView extends LinearLayout {

    // setting
    private int actBorder, dftBorder, dsbBorder, bgNormal, bgActive, corner, lftt, lftb, ritt, ritb, border, bgType;

    public CorneredView(Context context) {
        this(context, null);
    }

    public CorneredView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CorneredView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CorneredView, defStyle, 0);
        getAttributes(a);
        a.recycle();

        init();
    }

    private void getAttributes(TypedArray array) {
        actBorder = array.getColor(R.styleable.CorneredView_cv_active_border, Default.ACT_COLOR);
        dftBorder = array.getColor(R.styleable.CorneredView_cv_normal_border, Default.DFT_COLOR);
        dsbBorder = array.getColor(R.styleable.CorneredView_cv_disabled_border, Default.DSB_COLOR);

        bgNormal = array.getColor(R.styleable.CorneredView_cv_background, Color.WHITE);
        border = array.getDimensionPixelOffset(R.styleable.CorneredView_cv_border_size, Default.BORDER);
        corner = array.getDimensionPixelOffset(R.styleable.CorneredView_cv_corner_size, 0);
        bgType = array.getInt(R.styleable.CorneredView_cv_background_type, TYPE_BORDER);
        bgActive = array.getColor(R.styleable.CorneredView_cv_background_active, Default.ACT_COLOR);
        if (corner > 0) {
            lftt = corner;
            lftb = corner;
            ritb = corner;
            ritt = corner;
        } else {
            lftb = array.getDimensionPixelOffset(R.styleable.CorneredView_cv_left_bottom_corner, 0);
            lftt = array.getDimensionPixelOffset(R.styleable.CorneredView_cv_left_top_corner, 0);
            ritb = array.getDimensionPixelOffset(R.styleable.CorneredView_cv_right_bottom_corner, 0);
            ritt = array.getDimensionPixelOffset(R.styleable.CorneredView_cv_right_top_corner, 0);
        }
    }

    private Drawable getDrawable(int color, int state) {
        GradientDrawable gd = new GradientDrawable();
        // 边框
        if (border > 0) {
            gd.setStroke(border, color);
        }
        // 背景色
        if (bgType == TYPE_BORDER) {
            // 边框模式只显示不同颜色的边框，内容只是默认的背景不变
            gd.setColor(bgNormal);
        } else {
            gd.setColor(state > 0 ? bgActive : (state < 0 ? bgNormal : dsbBorder));
        }
        gd.setCornerRadius(corner);
        if (lftt != corner || ritt != corner || lftb != corner || ritb != corner) {
            gd.setCornerRadii(new float[]{lftt, lftt, ritt, ritt, ritb, ritb, lftb, lftb});
        }
        return gd;
    }

    @SuppressWarnings("deprecation")
    private void init() {
        Drawable active = getDrawable(actBorder, 1),
                normal = getDrawable(dftBorder, -1), disabled = getDrawable(dsbBorder, 0);
        StateListDrawable sld = new StateListDrawable();
        sld.addState(new int[]{-android.R.attr.state_enabled}, disabled);
        sld.addState(new int[]{android.R.attr.state_pressed}, active);
        sld.addState(new int[]{android.R.attr.state_focused}, active);
        sld.addState(new int[]{}, normal);

        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(sld);
        } else {
            setBackgroundDrawable(sld);
        }
        invalidate();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        _customListener = l;
        super.setOnClickListener(_defaultListener);
    }

    private OnClickListener _customListener;

    private OnClickListener _defaultListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null != _customListener) {
                _customListener.onClick(v);
            }
        }
    };

    /**
     * 设置背景颜色
     */
    public void setBackground(int color) {
        bgNormal = color;
        init();
    }

    public void setActiveBackground(int color) {
        bgActive = color;
        init();
    }

    /**
     * 设置边框大小
     */
    public void setBorderSize(int size) {
        border = size;
        init();
    }

    /**
     * 设置Active时的边框颜色
     */
    public void setActiveColor(int color) {
        actBorder = color;
        init();
    }

    /**
     * 设置默认边框颜色
     */
    public void setNormalColor(int color) {
        dftBorder = color;
        init();
    }

    public void setDisabledColor(int color) {
        dsbBorder = color;
        init();
    }

    public static final int TYPE_FILL = 0;
    public static final int TYPE_BORDER = 1;

    private static class Default {
        static final int ACT_COLOR = Color.parseColor("#FF8909");
        static final int DFT_COLOR = Color.parseColor("#2cc3bb");
        static final int DSB_COLOR = Color.parseColor("#e7e7e7");
        static final int BORDER = Utility.ConvertDp(1);
    }
}
