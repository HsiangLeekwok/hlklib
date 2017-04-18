package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.TypefaceHelper;
import com.hlk.hlklib.etc.Utility;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 圆角边框的按钮
 * Created by Hsiang Leekwok on 2015/07/13.
 */
public class CorneredButton extends AppCompatButton {

    /**
     * 背景填充方式
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({FILL, BORDER})
    public @interface BackgroundType {
    }

    /**
     * 边角类型
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_NORMAL, TYPE_OVAL})
    public @interface CornerType {
    }

    /**
     * 边角方位
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SIDE_LEFT_TOP, SIDE_RIGHT_TOP, SIDE_RIGHT_BOTTOM, SIDE_LEFT_BOTTOM})
    public @interface CornerSide {
    }

    /**
     * 左上角边角
     */
    public static final int SIDE_LEFT_TOP = 1;
    /**
     * 右上角边角
     */
    public static final int SIDE_RIGHT_TOP = 2;
    /**
     * 右下角边角
     */
    public static final int SIDE_RIGHT_BOTTOM = 3;
    /**
     * 左下角边角
     */
    public static final int SIDE_LEFT_BOTTOM = 4;

    /**
     * 填充背景
     */
    public static final int FILL = 0;
    /**
     * 边框背景
     */
    public static final int BORDER = 1;

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_OVAL = 1;


    private int active, normal, disabled, corner, leftt, leftb, rightt, rightb, bgtype, font_style, bg, cornerType;
    private boolean autoDisable = true;
    private long disableDuration = EI_LONG;

    public CorneredButton(Context context) {
        this(context, null);
    }

    public CorneredButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CorneredButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化背景属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CorneredButton, defStyleAttr, 0);
        getAttributes(a);
        a.recycle();

        init();
    }

    private void getAttributes(TypedArray array) {
        active = array.getColor(R.styleable.CorneredButton_active_color, Default.ACT_COLOR);
        normal = array.getColor(R.styleable.CorneredButton_normal_color, Default.DFT_COLOR);
        disabled = array.getColor(R.styleable.CorneredButton_disabled_color, Default.DSB_COLOR);
        corner = array.getDimensionPixelOffset(R.styleable.CorneredButton_corner_size, 0);
        font_style = array.getInt(R.styleable.CorneredButton_font_type, 0);
        bg = array.getColor(R.styleable.CorneredButton_bg_color, Color.WHITE);
        if (corner > 0) {
            leftb = corner;
            leftt = corner;
            rightb = corner;
            rightt = corner;
        } else {
            leftt = array.getDimensionPixelOffset(R.styleable.CorneredButton_left_top_corner_size, 0);
            leftb = array.getDimensionPixelOffset(R.styleable.CorneredButton_left_bottom_corner_size, 0);
            rightt = array.getDimensionPixelOffset(R.styleable.CorneredButton_right_top_corner_size, 0);
            rightb = array.getDimensionPixelOffset(R.styleable.CorneredButton_right_bottom_corner_size, 0);
        }
        bgtype = array.getInteger(R.styleable.CorneredButton_background_type, FILL);
        cornerType = array.getInteger(R.styleable.CorneredButton_corner_type, TYPE_NORMAL);
        autoDisable = array.getBoolean(R.styleable.CorneredButton_disable_when_click, true);
        disableDuration = array.getInt(R.styleable.CorneredButton_disable_duration, (int) EI_LONG);
    }

    private Drawable getDrawable(int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setStroke(Default.BORDER, color);
        gd.setColor(bgtype == FILL ? color : bg);
        gd.setCornerRadius(corner);
        if (leftt != corner || rightt != corner || leftb != corner || rightb != corner) {
            gd.setCornerRadii(new float[]{leftt, leftt, rightt, rightt, rightb, rightb, leftb, leftb});
        }
        return gd;
    }

    private void initCorners() {
        if (cornerType == TYPE_OVAL) {
            corner = getMeasuredHeight() / 2;
            leftb = corner;
            leftt = corner;
            rightb = corner;
            rightt = corner;
        }
    }

    @SuppressWarnings("deprecation")
    private void init() {
        initCorners();
        Drawable active = getDrawable(this.active), normal = getDrawable(this.normal), disabled = getDrawable(this.disabled);
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

        //initColors()
        if (font_style != 0) {
            setTypeface(TypefaceHelper.get(getContext(), "fonts/iconfont.ttf"));
        }
    }

    private void initColors() {
        int dft = getTextColors().getDefaultColor();
        int[] colors = new int[]{Default.DSB_TEXT, dft};
        int[][] state = new int[2][];
        state[0] = new int[]{-android.R.attr.state_enabled};
        state[1] = new int[]{};
        ColorStateList csl = new ColorStateList(state, colors);
        setTextColor(csl);
    }

    public void setActiveColor(int color) {
        active = color;
        init();
    }

    public void setNormalColor(int color) {
        normal = color;
        init();
    }

    /**
     * 设置边角形式
     */
    public void setCornerType(@CornerType int type) {
        cornerType = type;
        init();
    }

    /**
     * 设置边角尺寸
     */
    public void setCornerSize(@CornerSide int side, int size) {
        switch (side) {
            case SIDE_LEFT_TOP:
                leftt = size;
                break;
            case SIDE_LEFT_BOTTOM:
                leftb = size;
                break;
            case SIDE_RIGHT_BOTTOM:
                rightb = size;
                break;
            case SIDE_RIGHT_TOP:
                rightt = size;
                break;
        }
        init();
    }

    /**
     * 设置背景类型
     */
    public void setBackgroundType(@BackgroundType int type) {
        bgtype = type;
        init();
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
            delayEnableMyself();
            if (null != _customListener) {
                _customListener.onClick(v);
            }
        }
    };

    /**
     * disable之后延时自动enable
     */
    private void delayEnableMyself() {
        if (!autoDisable) {
            return;
        }
        setEnabled(false);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setEnabled(true);
            }
        }, disableDuration);
    }

    /**
     * 按钮点击时，自动disable之后enable的时间间隔，长
     */
    public static final long EI_LONG = 3000;
    /**
     * 按钮点击时，自动disable之后enable的时间间隔，短
     */
    public static final long EI_SHORT = 100;

    private static class Default {
        /**
         * 默认背景类型为填充
         */
        public static final int BG_TYPE = 0;
        public static final int CORNER = Utility.ConvertDp(5);
        static final int BORDER = Utility.ConvertDp(1);
        static final int ACT_COLOR = Color.parseColor("#2cc3bb");
        static final int DFT_COLOR = Color.parseColor("#2fd0c8");
        static final int DSB_COLOR = Color.parseColor("#e7e7e7");
        static final int DSB_TEXT = Color.parseColor("#d0d0d0");
    }
}
