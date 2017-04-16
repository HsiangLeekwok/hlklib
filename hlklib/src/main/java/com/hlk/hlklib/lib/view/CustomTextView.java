package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup.MarginLayoutParams;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.TypefaceHelper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <b>功能：</b>能够显示自定义icon的text view派生类<br />
 * <b>作者：</b>Hsiang Leekwok <br />
 * <b>时间：</b>2016/07/08 16:46 <br />
 * <b>邮箱：</b>xiang.l.g@gmail.com <br />
 */
public class CustomTextView extends AppCompatTextView {

    private static final String TAG = "CustomTextView";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_SYSTEM, TYPE_FONT_AWESOME, TYPE_ICO_MOON, TYPE_ICON_FONT, TYPE_MATERIAL, TYPE_ICON_FONT_NEW})
    public @interface SourceType {
    }

    /**
     * 默认字体源
     */
    private int sourceType = TYPE_ICON_FONT;
    private int backgroundColor, normalColor, activeColor, disabledColor;
    /**
     * 是否自动计算宽高
     */
    private boolean autoResize = false;

    public CustomTextView(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        // Load attributes
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyleAttr, 0);
        try {
            sourceType = array.getInt(R.styleable.CustomTextView_ctv_source, TYPE_ICON_FONT);
            autoResize = array.getBoolean(R.styleable.CustomTextView_ctv_auto_resize, false);
            backgroundColor = array.getColor(R.styleable.CustomTextView_ctv_oval_background_color, Color.TRANSPARENT);
            normalColor = array.getColor(R.styleable.CustomTextView_ctv_normal_color, getCurrentTextColor());
            activeColor = array.getColor(R.styleable.CustomTextView_ctv_active_color, getCurrentTextColor());
            disabledColor = array.getColor(R.styleable.CustomTextView_ctv_disabled_color, Color.parseColor("#d9d9d9"));
        } finally {
            array.recycle();
        }
        resetTypeface(context);
    }

    @SuppressWarnings("deprecation")
    private void resetTypeface(Context context) {
        if (sourceType >= TYPE_FONT_AWESOME) {
            setTypeface(TypefaceHelper.get(context, "fonts/" + source(sourceType)));
        }
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(getShapeBackground());
        } else {
            setBackgroundDrawable(getShapeBackground());
        }
        setTextColor(getColorStateList());
    }

    private ColorStateList getColorStateList() {
        int[][] states = new int[][]{
                new int[]{android.R.attr.state_focused},// focused
                new int[]{android.R.attr.state_checked},// checked
                new int[]{android.R.attr.state_pressed},// pressed
                new int[]{-android.R.attr.state_enabled},// disable
                new int[]{}// normal
        };
        int[] colors = new int[]{
                activeColor,
                activeColor,
                activeColor,
                disabledColor,
                normalColor
        };
        return new ColorStateList(states, colors);
    }

    private Drawable getShapeBackground() {
        ShapeDrawable sd = new ShapeDrawable();
        sd.setShape(new OvalShape());
        sd.getPaint().setColor(backgroundColor);
        return sd;
    }

    /**
     * 根据LayoutParams手动重置宽高
     */
    private void resetIconSize(final MarginLayoutParams params) {
        if (isInEditMode() || null == params) return;
        // 大分辨率的手机无须计算
        //if (getContext().getResources().getDisplayMetrics().densityDpi > 320) return;

        post(new Runnable() {
            @Override
            public void run() {
                int size = (int) getTextSize();
                int height = getMeasuredHeight();
                int diff = height > size ? height - size : 0;
                params.width = size;
                int add = (diff / 2) + (diff % 2);
                int dft = 10;
                params.height = size + (diff > dft ? add : 0);
                if (diff > dft) {
                    params.topMargin = -add;
                }
                setLayoutParams(params);
                invalidate();
            }
        });
    }

    @Override
    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        // 设置字体大小之后需要计算宽高
        resize();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setText(CharSequence text, BufferType type) {
        if (null == text || TextUtils.isEmpty(text.toString())) {
            super.setText(text, type);
        } else {
            super.setText(Html.fromHtml(text.toString()), type);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        resize();
    }

    private void resize() {
        if (autoResize) {
            resetIconSize((MarginLayoutParams) getLayoutParams());
        }
    }

    /**
     * 手动设置是否自动计算宽高以适应UI显示
     */
    public void setAutoResize(boolean resize) {
        if (autoResize != resize)
            autoResize = resize;
        resize();
    }

    /**
     * 设置圆形背景色
     */
    @SuppressWarnings("deprecation")
    public void setShapeBackground(int color) {
        backgroundColor = color;
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground(getShapeBackground());
        } else {
            setBackgroundDrawable(getShapeBackground());
        }
        invalidate();
    }

    private String source(int type) {
        switch (type) {
            case TYPE_FONT_AWESOME:
                return "fontawesome.ttf";
            case TYPE_ICO_MOON:
                return "icomoon.ttf";
            case TYPE_MATERIAL:
                return "materialicons.ttf";
            case TYPE_ICON_FONT_NEW:
                return "new_icon_font.ttf";
            case TYPE_ICON_FONT:
            default:
                return "iconfont.ttf";
        }
    }

    /**
     * 手动设置字体源
     */
    public void setTypeSource(@SourceType int type) {
        if (sourceType != type) {
            sourceType = type;
            resetTypeface(getContext());
            resize();
        }
    }

    /**
     * 跟随系统字体
     */
    public static final int TYPE_SYSTEM = -1;
    /**
     * Font Awesome字体
     */
    public static final int TYPE_FONT_AWESOME = 0;
    /**
     * IcoMoon字体
     */
    public static final int TYPE_ICO_MOON = 1;
    /**
     * Icon Font字体
     */
    public static final int TYPE_ICON_FONT = 2;
    /**
     * Google Material Icon字体
     */
    public static final int TYPE_MATERIAL = 3;
    /**
     * 新版IconFont集合
     */
    public static final int TYPE_ICON_FONT_NEW = 4;
}
