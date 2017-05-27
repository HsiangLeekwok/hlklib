package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.Utility;

/**
 * <b>功能描述：</b>提供可清除输入文字的EditText<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/04/11 09:08 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/04/11 09:08 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class ClearEditText extends RelativeLayout {

    public ClearEditText(Context context) {
        super(context);
        initialize(context, null, 0);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private int normalBorder, activeBorder, editPadding, editPaddingLeft, editPaddingTop,
            editPaddingRight, editPaddingBottom, editGravity, editCorner, editType, editMaxLen, editMaxLine,
            editMinHeight, editMaxHeight, editTextSize, counterTextSize, counterTextColor;
    private String editHint, editValue, editExtractRegex, editVerifyRegex, iconEye, iconClear, counterFmt;
    private boolean allowCountInput = false;

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText, defStyleAttr, 0);
        try {
            normalBorder = array.getColor(R.styleable.ClearEditText_cet_edit_normal_border, Color.WHITE);
            activeBorder = array.getColor(R.styleable.ClearEditText_cet_edit_active_border, Color.WHITE);
            editPadding = array.getDimensionPixelOffset(R.styleable.ClearEditText_cet_edit_padding, 0);
            if (editPadding > 0) {
                editPaddingLeft = editPadding;
                editPaddingTop = editPadding;
                editPaddingRight = editPadding;
                editPaddingBottom = editPadding;
            } else {
                editPaddingLeft = array.getDimensionPixelOffset(R.styleable.ClearEditText_cet_edit_padding_left, 0);
                editPaddingTop = array.getDimensionPixelOffset(R.styleable.ClearEditText_cet_edit_padding_top, 0);
                editPaddingRight = array.getDimensionPixelOffset(R.styleable.ClearEditText_cet_edit_padding_right, 0);
                editPaddingBottom = array.getDimensionPixelOffset(R.styleable.ClearEditText_cet_edit_padding_bottom, 0);
            }
            editGravity = array.getInteger(R.styleable.ClearEditText_cet_edit_gravity, 0);
            editCorner = array.getDimensionPixelOffset(R.styleable.ClearEditText_cet_edit_corner_size, 0);
            editType = array.getInt(R.styleable.ClearEditText_cet_edit_input_type, TYPE_TEXT);
            editHint = array.getString(R.styleable.ClearEditText_cet_edit_hint);
            editValue = array.getString(R.styleable.ClearEditText_cet_edit_text);
            editExtractRegex = array.getString(R.styleable.ClearEditText_cet_edit_value_extract_regex);
            editVerifyRegex = array.getString(R.styleable.ClearEditText_cet_edit_value_verify_regex);
            iconEye = array.getString(R.styleable.ClearEditText_cet_edit_icon_eye);
            iconClear = array.getString(R.styleable.ClearEditText_cet_edit_icon_clear);
            editMaxLen = array.getInteger(R.styleable.ClearEditText_cet_edit_value_max_length, 0);
            editMaxLine = array.getInteger(R.styleable.ClearEditText_cet_edit_max_lines, 1);
            editMinHeight = array.getDimensionPixelOffset(R.styleable.ClearEditText_cet_edit_minimum_height, 0);
            editMaxHeight = array.getDimensionPixelOffset(R.styleable.ClearEditText_cet_edit_maximum_height, 0);
            editTextSize = array.getDimensionPixelSize(R.styleable.ClearEditText_cet_edit_text_size, 0);
            allowCountInput = array.getBoolean(R.styleable.ClearEditText_cet_edit_count_input, false);
            counterTextSize = array.getDimensionPixelSize(R.styleable.ClearEditText_cet_edit_count_input_text_size, 0);
            counterTextColor = array.getColor(R.styleable.ClearEditText_cet_edit_count_input_text_color, getAccentColor(context));
            counterFmt = array.getString(R.styleable.ClearEditText_cet_edit_count_text_format);
            if (TextUtils.isEmpty(counterFmt)) {
                counterFmt = "%d/%d";
            }
        } finally {
            array.recycle();
        }
        initializeView();
    }

    private int getAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorAccent});
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }

    private CorneredEditText editTextView;
    private CustomTextView eyeIcon;
    private CustomTextView clearIcon;
    private TextView inputTextCounter;

    private void initializeView() {
        View view = inflate(getContext(), R.layout.hlklib_clear_edit_text, this);

        editTextView = (CorneredEditText) view.findViewById(R.id.hlklib_clear_edit_text_text);
        eyeIcon = (CustomTextView) view.findViewById(R.id.hlklib_clear_edit_text_eye);
        clearIcon = (CustomTextView) view.findViewById(R.id.hlklib_clear_edit_text_clear);
        inputTextCounter = (TextView) view.findViewById(R.id.hlklib_clear_edit_text_counter);
        eyeIcon.setOnClickListener(clickListener);
        clearIcon.setOnClickListener(clickListener);

        if (counterTextSize > 0) {
            inputTextCounter.setTextSize(TypedValue.COMPLEX_UNIT_PX, counterTextSize);
        }
        inputTextCounter.setTextColor(counterTextColor);
        inputTextCounter.setVisibility(allowCountInput ? VISIBLE : GONE);

        editTextView.addTextChangedListener(defaultTextWatcher);
        editTextView.setOnFocusChangeListener(onFocusChangeListener);
        editTextView.setPadding(editPadding, editPadding, editPadding + (TextUtils.isEmpty(iconClear) ? 0 : Utility.ConvertDp(20)), editPadding);
        editTextView.setNormalBorderColor(normalBorder);
        editTextView.setActiveBorderColor(activeBorder);
        editTextView.setCornerSize(editCorner);
        editTextView.setHint(editHint);
        editTextView.setText(editValue);
        editTextView.setInputType(getInputType());
        editTextView.setMaxLength(editMaxLen);
        editTextView.setPadding(editPaddingLeft, editPaddingTop, editPaddingRight, editPaddingBottom);
        editTextView.setGravity(gravity());
        if (editTextSize > 0) {
            editTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, editTextSize);
        }
        resetMaxLines();
        resetMinHeight();
        resetMaxHeight();

        resetValueExtractRegex();
        resetValueVerifyRegex();
        clearIcon.setText(iconClear);
        eyeIcon.setText(iconEye);
    }

    private int gravity() {
        switch (editGravity) {
            case 1:
                return Gravity.TOP;
            case 2:
                return Gravity.RIGHT;
            case 3:
                return Gravity.BOTTOM;
            case 4:
                return Gravity.CENTER_VERTICAL;
            case 5:
                return Gravity.FILL_VERTICAL;
            case 6:
                return Gravity.CENTER_HORIZONTAL;
            case 7:
                return Gravity.FILL_HORIZONTAL;
            case 8:
                return Gravity.CENTER;
            case 9:
                return Gravity.FILL;
            case 10:
                return Gravity.START;
            case 11:
                return Gravity.END;
            default:
                return Gravity.LEFT;
        }
    }

    private void resetValueExtractRegex() {
        if (!TextUtils.isEmpty(editExtractRegex)) {
            // 值过滤正则表达式
            editTextView.setValueExtractRegex(editExtractRegex);
        }
    }

    private void resetValueVerifyRegex() {
        if (!TextUtils.isEmpty(editVerifyRegex)) {
            editTextView.setValueVerifyRegex(editVerifyRegex);
        }
    }

    private void resetMaxLines() {
        if (editMaxLine > 1) {
            editTextView.setMaxLines(editMaxLine);
        }
        editTextView.setSingleLine(editMaxLine <= 1);
    }

    private void resetMinHeight() {
        if (editMinHeight > 0) {
            editTextView.setMinHeight(editMinHeight);
        }
    }

    private void resetMaxHeight() {
        if (editMaxHeight > 0) {
            editTextView.setMaxHeight(editMaxHeight);
        }
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == eyeIcon) {
                displayPassword();
            } else if (v == clearIcon) {
                editTextView.setText(null);
            }
        }
    };

    /**
     * 显示密码
     */
    private void displayPassword() {
        if (isInputForPassword() && editType == TYPE_PASSWORD) {
            editType = TYPE_VISIBLE_PASSWORD;
            editTextView.setInputType(getInputType());
            editTextView.setSelection(editTextView.getText().length());
        }
    }

    /**
     * 输入文本
     */
    public static final int TYPE_TEXT = 0;
    /**
     * 输入电话号码
     */
    public static final int TYPE_PHONE = 1;
    /**
     * 输入数字
     */
    public static final int TYPE_NUMBER = 2;
    /**
     * 输入密码
     */
    public static final int TYPE_PASSWORD = 3;
    /**
     * 输入可视密码
     */
    public static final int TYPE_VISIBLE_PASSWORD = 4;

    /**
     * 文字输入方式
     */
    @IntDef({TYPE_TEXT, TYPE_PHONE, TYPE_NUMBER, TYPE_PASSWORD, TYPE_VISIBLE_PASSWORD})
    public @interface KeyType {
    }

    /**
     * 设置文本输入方式
     */
    public void setInputType(@KeyType int inputType) {
        editType = inputType;
        if (editType > 0) {
            editTextView.setInputType(getInputType());
        }
    }

    private int getInputType() {
        int type;
        switch (editType) {
            case TYPE_PHONE:
                type = InputType.TYPE_CLASS_PHONE;
                break;
            case TYPE_NUMBER:
                type = InputType.TYPE_CLASS_NUMBER;
                break;
            case TYPE_PASSWORD:
                type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;
            case TYPE_VISIBLE_PASSWORD:
                type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;
            default:
                // 默认文字输入方式
                type = InputType.TYPE_CLASS_TEXT;
                if (editMaxLine > 1) {
                    type |= InputType.TYPE_TEXT_FLAG_MULTI_LINE;
                }
                break;
        }
        // 末尾增加no suggestions避免多次调用onTextChangeListener
        return type;// | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
    }

    /**
     * 当前是否是密码输入方式
     */
    private boolean isInputForPassword() {
        return editType == TYPE_PASSWORD || editType == TYPE_VISIBLE_PASSWORD;
    }

    /**
     * 添加输入值的验证回调
     */
    public void addOnValueVerifyingListener(CorneredEditText.OnValueVerifyingListener l) {
        editTextView.addOnValueVerifyingListener(l);
    }

    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            visibleIcons(hasFocus);
            if (null != outerFocusChangeListener) {
                outerFocusChangeListener.onFocusChange(ClearEditText.this, hasFocus);
            }
        }
    };

    private OnFocusChangeListener outerFocusChangeListener;

    /**
     * 添加OnFocusChangeListener
     */
    public void addOnFocusChangeListener(OnFocusChangeListener l) {
        outerFocusChangeListener = l;
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
            // 自定义了textWatcher需要在onTextChanged加上setInputType以便重新设置密码的掩码
            editTextView.setInputType(getInputType());
        }

        @Override
        public void afterTextChanged(Editable s) {
            visibleIcons(editTextView.hasFocus());
            if (null != __textWatcher) {
                __textWatcher.afterTextChanged(s);
            }
            displayCounter(null == s ? 0 : s.length());
        }
    };

    private void displayCounter(int size) {
        inputTextCounter.setText(String.format(counterFmt, size, editMaxLen));
    }

    private void visibleIcons(boolean visible) {
        int len = visible ? editTextView.getText().toString().length() : 0;
        clearIcon.setVisibility(len > 0 ? VISIBLE : GONE);
        eyeIcon.setVisibility((isInputForPassword() && len > 0) ? VISIBLE : GONE);
    }

    /**
     * 获取输入的值
     */
    public String getValue() {
        String value = editTextView.getText().toString();
        if (TextUtils.isEmpty(editVerifyRegex)) {
            return value;
        }
        return editTextView.verifyValue() ? value : "";
    }

    /**
     * 设置显示的内容
     */
    public void setValue(String value) {
        editTextView.setText(value);
        visibleIcons(editTextView.hasFocus());
    }

    public void setTextHint(String hint) {
        editTextView.setHint(hint);
    }

    public void setTextHint(int resid) {
        editTextView.setHint(resid);
    }

    /**
     * 验证输入的值
     */
    public boolean verifyValue() {
        return editTextView.verifyValue();
    }

    /**
     * 设置值输入过滤正则
     */
    public void setValueExtract(String extractRegex) {
        editExtractRegex = extractRegex;
        resetValueExtractRegex();
    }

    public void setValueVerify(String verifyRegex) {
        editVerifyRegex = verifyRegex;
        resetValueVerifyRegex();
    }

    public void setMaxLength(int maxLength) {
        editTextView.setMaxLength(maxLength);
    }

    public void setMaxLines(int maxLines) {
        if (editMaxLine != maxLines) {
            editMaxLine = maxLines;
            resetMaxLines();
        }
    }

    public void setMinimumHeightLimit(int height) {
        if (editMinHeight != height) {
            editMinHeight = height;
            resetMinHeight();
        }
    }

    public void setMaximumHeightLimit(int height) {
        if (editMaxHeight != height) {
            editMaxHeight = height;
            resetMaxHeight();
        }
    }

    public void focusEnd() {
        editTextView.focusEnd();
    }

    /**
     * 添加输入控制
     */
    public void addTextChangedListener(TextWatcher watcher) {
        __textWatcher = watcher;
    }
}
