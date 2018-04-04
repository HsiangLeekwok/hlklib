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
import android.view.KeyEvent;
import android.widget.EditText;

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
        TypedArray a = context.obtainStyledAttributes(attrs, com.hlk.hlklib.R.styleable.CorneredEditText, defStyle, 0);
        getAttributes(a);
        a.recycle();

        init();
    }

    private void getAttributes(TypedArray array) {
        act_color = array.getColor(com.hlk.hlklib.R.styleable.CorneredEditText_cet_active_border, Default.ACT_COLOR);
        dft_color = array.getColor(com.hlk.hlklib.R.styleable.CorneredEditText_cet_normal_border, Default.DFT_COLOR);
        dsb_color = array.getColor(com.hlk.hlklib.R.styleable.CorneredEditText_cet_disabled_border, Default.DSB_COLOR);
        bg_color = array.getColor(com.hlk.hlklib.R.styleable.CorneredEditText_cet_background, Color.WHITE);
        border_size = array.getDimensionPixelOffset(com.hlk.hlklib.R.styleable.CorneredEditText_cet_border_size, Default.BORDER);
        corner = array.getDimensionPixelOffset(com.hlk.hlklib.R.styleable.CorneredEditText_cet_corner_size, 0);
        value_verify_regex = array.getString(com.hlk.hlklib.R.styleable.CorneredEditText_cet_value_verify_regex);
        value_extract_regex = array.getString(com.hlk.hlklib.R.styleable.CorneredEditText_cet_value_extract_regex);
        value_verify_warning = array.getString(com.hlk.hlklib.R.styleable.CorneredEditText_cet_value_verify_warning);
        display_counter = array.getBoolean(com.hlk.hlklib.R.styleable.CorneredEditText_cet_display_counter, false);
        count_color = array.getColor(com.hlk.hlklib.R.styleable.CorneredEditText_cet_counter_font_color, Default.CounterColor);
        count_size = array.getDimensionPixelSize(com.hlk.hlklib.R.styleable.CorneredEditText_cet_counter_font_size, Default.CounterSize);
        formatting = array.getString(com.hlk.hlklib.R.styleable.CorneredEditText_cet_counter_format);
        focus_end = array.getBoolean(com.hlk.hlklib.R.styleable.CorneredEditText_cet_auto_focus_end, false);
        if (TextUtils.isEmpty(formatting) || formatting.equals("null")) {
            formatting = Default.CounterFormat;
        }
        max_length = array.getInt(com.hlk.hlklib.R.styleable.CorneredEditText_cet_max_length, 0);
        if (corner > 0) {
            setCorners();
        } else {
            lftb = array.getDimensionPixelOffset(com.hlk.hlklib.R.styleable.CorneredEditText_cet_left_bottom_corner, 0);
            lftt = array.getDimensionPixelOffset(com.hlk.hlklib.R.styleable.CorneredEditText_cet_left_top_corner, 0);
            ritb = array.getDimensionPixelOffset(com.hlk.hlklib.R.styleable.CorneredEditText_cet_right_bottom_corner, 0);
            ritt = array.getDimensionPixelOffset(com.hlk.hlklib.R.styleable.CorneredEditText_cet_right_top_corner, 0);
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
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (onImeBackKeyListener != null) {
                onImeBackKeyListener.onBackKey(this);
            }
        }
        return super.onKeyPreIme(keyCode, event);
    }

    /**
     * 设置在键盘打开的状态下处理 back key 的回调
     */
    public void setOnImeBackKeyListener(OnImeBackKeyListener l) {
        onImeBackKeyListener = l;
    }

    private OnImeBackKeyListener onImeBackKeyListener;

    /**
     * IME状态下的back key处理接口
     */
    public interface OnImeBackKeyListener {
        /**
         * IME状态下的 back key 处理回调
         */
        void onBackKey(EditText editText);
    }

    @Override
    protected void onDraw(Canvas canvas) {
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

    private boolean isInputTypePassword = false;

    public void setInputTypePassword(boolean hidden) {
        isInputTypePassword = hidden;
        if (!isInputTypePassword) {
            addTextChangedListener(defaultTextWatcher);
        }
    }

    /**
     * 默认的TextWatcher
     */
    private TextWatcher defaultTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // 不是密码输入状态时，用正则验证输入内容
            if (!isInputTypePassword) {
                if (!isEmpty(value_extract_regex)) {
                    // 过滤输入内容
                    extractValue(s);
                }

                if (!isEmpty(value_verify_regex) && null != valueVerifyingListener) {
                    // 如果设置了值验证正则则向用户提供验证结果
                    valueVerifyingListener.onVerifying(verifyValue());
                }
            }
        }
    };

    /**
     * 过滤掉正则限制之外的内容
     */
    private void extractValue(Editable s) {
        String text = s.toString();
        if (!isEmpty(text)) {
            String got = gotExtractedValue(text);
            if (!text.equals(got)) {
                s.replace(0, s.length(), got);
                // 修改完毕之后自动将光标设置到最后
                if (focus_end) {
                    focusEnd();
                }
            }
        }
    }

    /**
     * 将光标移到最后
     */
    public void focusEnd() {
        setSelection(getText().length());
    }

    /**
     * 验证输入是否正确
     */
    public boolean verifyValue() {
        if (isEmpty(value_verify_regex)) {
            throw new IllegalArgumentException("no value verify regex found.");
        }
        String value = getText().toString();
        Pattern p = Pattern.compile(value_verify_regex);
        Matcher m = p.matcher(value);
        return m.matches();
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
        return TextUtils.isEmpty(string);
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
        extractValue(getText());
    }

    /**
     * 设置值验证正则
     */
    public void setValueVerifyRegex(String regex) {
        value_verify_regex = regex;
    }

    /**
     * 根据指定的正则验证用户输入的值
     */
    public String getValue() {
        String value = getText().toString();
        return isEmpty(value_verify_regex) ? value : (verifyValue() ? value : "");
    }

    private static class Default {
        static final int ACT_COLOR = Color.parseColor("#FF8909");
        static final int DFT_COLOR = Color.parseColor("#2cc3bb");
        static final int DSB_COLOR = Color.parseColor("#e7e7e7");
        static final int CORNER = Utility.ConvertDp(5);
        static final int BORDER = Utility.ConvertDp(1);
        static final String CounterFormat = "%d/%d";
        static final int CounterColor = Color.parseColor("#ff4081");
        static final int CounterSize = Utility.ConvertDp(8);
        static final int CounterPadding = Utility.ConvertDp(5);
    }

    private OnValueVerifyingListener valueVerifyingListener;

    /**
     * 添加输入值验证情况的回调
     */
    public void addOnValueVerifyingListener(OnValueVerifyingListener l) {
        valueVerifyingListener = l;
    }

    /**
     * 输入的值验证通过
     */
    public interface OnValueVerifyingListener {
        void onVerifying(boolean passed);
    }
}
