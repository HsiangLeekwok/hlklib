package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.Utility;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提供边框背景的EditText<br />
 * Created by Hsiang Leekwok on 2015/12/24.
 */
public class CorneredEditText extends AppCompatEditText {

    // setting
    private int act_color, dft_color, dsb_color, bg_color, corner, lftt, lftb, ritt, ritb, border_size, max_length,
            count_size, count_color;
    private String value_verify_regex = "", value_extract_regex = "", value_verify_warning = "", formatting = "";
    private boolean display_counter = false, focus_end = false;

    public CorneredEditText(Context context) {
        this(context, null);
    }

    public CorneredEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CorneredEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CorneredEditText, defStyle, 0);
        getAttributes(a);
        a.recycle();

        init();
    }

    private void getAttributes(TypedArray array) {
        act_color = array.getColor(R.styleable.CorneredEditText_cet_active_border, Default.ACT_COLOR);
        dft_color = array.getColor(R.styleable.CorneredEditText_cet_normal_border, Default.DFT_COLOR);
        dsb_color = array.getColor(R.styleable.CorneredEditText_cet_disabled_border, Default.DSB_COLOR);
        bg_color = array.getColor(R.styleable.CorneredEditText_cet_background, Color.WHITE);
        border_size = array.getDimensionPixelOffset(R.styleable.CorneredEditText_cet_border_size, Default.BORDER);
        corner = array.getDimensionPixelOffset(R.styleable.CorneredEditText_cet_corner_size, 0);
        value_verify_regex = array.getString(R.styleable.CorneredEditText_cet_value_verify_regex);
        value_extract_regex = array.getString(R.styleable.CorneredEditText_cet_value_extract_regex);
        value_verify_warning = array.getString(R.styleable.CorneredEditText_cet_value_verify_warning);
        display_counter = array.getBoolean(R.styleable.CorneredEditText_cet_display_counter, false);
        count_color = array.getColor(R.styleable.CorneredEditText_cet_counter_font_color, Default.CounterColor);
        count_size = array.getDimensionPixelSize(R.styleable.CorneredEditText_cet_counter_font_size, Default.CounterSize);
        formatting = array.getString(R.styleable.CorneredEditText_cet_counter_format);
        focus_end = array.getBoolean(R.styleable.CorneredEditText_cet_auto_focus_end, false);
        if (TextUtils.isEmpty(formatting) || formatting.equals("null")) {
            formatting = Default.CounterFormat;
        }
        max_length = array.getInt(R.styleable.CorneredEditText_cet_max_length, 0);
        if (corner > 0) {
            setCorners();
        } else {
            lftb = array.getDimensionPixelOffset(R.styleable.CorneredEditText_cet_left_bottom_corner, 0);
            lftt = array.getDimensionPixelOffset(R.styleable.CorneredEditText_cet_left_top_corner, 0);
            ritb = array.getDimensionPixelOffset(R.styleable.CorneredEditText_cet_right_bottom_corner, 0);
            ritt = array.getDimensionPixelOffset(R.styleable.CorneredEditText_cet_right_top_corner, 0);
        }
    }

    private void setCorners() {
        lftt = corner;
        lftb = corner;
        ritb = corner;
        ritt = corner;
    }

    private Drawable getDrawable(int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setStroke(border_size, color);
        gd.setColor(bg_color);
        gd.setCornerRadius(corner);
        if (lftt != corner || ritt != corner || lftb != corner || ritb != corner) {
            gd.setCornerRadii(new float[]{lftt, lftt, ritt, ritt, ritb, ritb, lftb, lftb});
        }
        return gd;
    }

    @SuppressWarnings("deprecation")
    private void init() {
        Drawable active = getDrawable(act_color), normal = getDrawable(dft_color), disabled = getDrawable(dsb_color);
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

        initMaxLength();
    }

    public void setActiveBorderColor(int color) {
        act_color = color;
        init();
    }

    public void setNormalBorderColor(int color) {
        dft_color = color;
        init();
    }

    public void setDisabledBorderColor(int color) {
        dsb_color = color;
        init();
    }

    public void setBorderSize(int size) {
        border_size = size;
        init();
    }

    public void setCornerSize(int size) {
        corner = size;
        setCorners();
        init();
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(Color.TRANSPARENT);
        bg_color = color;
        init();
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        __textWatcher = watcher;
        super.addTextChangedListener(defaultTextWatcher);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (max_length > 0) {
            String text = getText().toString();
            int len = text.length();
            if (len > max_length) {
                setText(text.substring(0, max_length));
            }
        }
        extractValue();
        super.onDraw(canvas);
        if (display_counter) {
            drawCounter(canvas);
        }
    }

    private void drawCounter(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(count_color);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setTextSize(count_size);
        paint.setAntiAlias(true);
        int positionX = getWidth() - Default.CounterPadding;
        int positionY;
        positionY = getHeight() - Default.CounterPadding;
        int len = getText().length();
        String text;
        try {
            text = String.format(Locale.getDefault(), formatting, len, max_length);
        } catch (Exception e) {
            text = String.format(Locale.getDefault(), Default.CounterFormat, len, max_length);
        }
        canvas.drawText(text, positionX, positionY, paint);
    }

    /**
     * 用户设置的TextWatcher
     */
    private TextWatcher __textWatcher;

    /**
     * 默认的TextWatcher
     */
    private TextWatcher defaultTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (null != __textWatcher) {
                __textWatcher.beforeTextChanged(s, start, count, after);
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (null != __textWatcher) {
                __textWatcher.onTextChanged(s, start, before, count);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            int len = s.toString().length();
            if (null != __textWatcher) {
                __textWatcher.afterTextChanged(s);
            }
            if (len <= 0) return;
            extractValue();
        }
    };

    /**
     * 过滤掉正则限制之外的内容
     */
    private void extractValue() {
        String text = getText().toString();
        if (!isEmpty(text)) {
            String got = gotExtractedValue(text);
            if (!text.equals(got)) {
                setText(got);
                // 修改完毕之后自动将光标设置到最后
                if (focus_end) {
                    setSelection(got.length());
                }
            }
        }
    }

    /**
     * 验证输入是否正确
     */
    private boolean verify(String string) {
        if (!isEmpty(value_verify_regex)) {
            Pattern p = Pattern.compile(value_verify_regex);
            Matcher m = p.matcher(string);
            return m.matches();
        }
        return true;
    }

    /**
     * 从输入中提取数据
     */
    private String gotExtractedValue(String string) {
        if (!isEmpty(value_extract_regex)) {
            Pattern pattern = Pattern.compile(value_extract_regex);
            Matcher ma = pattern.matcher(string);
            String result = "";
            while (ma.find()) {
                result += ma.group(0);
            }
            return result;
        }
        return string;
    }

    private boolean isEmpty(String string) {
        return TextUtils.isEmpty(string) || string.equals("null");
    }

    // ********************

    /**
     * 设置最大输入长度
     */
    public void setMaxLength(int maxLength) {
        if (max_length != maxLength) {
            max_length = maxLength;
            initMaxLength();
        }
    }

    /**
     * 初始化输入长度限制
     */
    private void initMaxLength() {
        display_counter = max_length > 0;
        if (max_length > 0) {
            setFilters(new InputFilter[]{new InputFilter.LengthFilter(max_length)});
        } else {
            if (null != getFilters()) {
                setFilters(new InputFilter[]{});
            }
        }
    }

    /**
     * 设置输入内容过滤的正则表达式
     */
    public void setValueExtractRegex(String regex) {
        value_extract_regex = regex;
        extractValue();
    }

    private static class Default {
        public static final int ACT_COLOR = Color.parseColor("#FF8909");
        public static final int DFT_COLOR = Color.parseColor("#2cc3bb");
        public static final int DSB_COLOR = Color.parseColor("#e7e7e7");
        public static final int CORNER = Utility.ConvertDp(5);
        public static final int BORDER = Utility.ConvertDp(1);
        public static final String CounterFormat = "%d/%d";
        public static final int CounterColor = Color.parseColor("#ff4081");
        public static final int CounterSize = Utility.ConvertDp(8);
        public static final int CounterPadding = Utility.ConvertDp(5);
    }
}
