package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.IntDef;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.hlk.hlklib.R;

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

    private int normalBorder, activeBorder, editPadding, editCorner, editType, editMaxLen, editMaxLine, editMinHeight;
    private String editHint, editValue, editExtractRegex, editVerifyRegex, iconEye, iconClear;

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText, defStyleAttr, 0);
        try {
            normalBorder = array.getColor(R.styleable.ClearEditText_cet_edit_normal_border, Color.WHITE);
            activeBorder = array.getColor(R.styleable.ClearEditText_cet_edit_active_border, Color.WHITE);
            editPadding = array.getDimensionPixelOffset(R.styleable.ClearEditText_cet_edit_padding, 0);
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
        } finally {
            array.recycle();
        }
        initializeView();
    }

    private CorneredEditText editTextView;
    private CustomTextView eyeIcon;
    private CustomTextView clearIcon;

    private void initializeView() {
        View view = inflate(getContext(), R.layout.hlklib_clear_edit_text, this);

        editTextView = (CorneredEditText) view.findViewById(R.id.hlklib_clear_edit_text_text);
        eyeIcon = (CustomTextView) view.findViewById(R.id.hlklib_clear_edit_text_eye);
        clearIcon = (CustomTextView) view.findViewById(R.id.hlklib_clear_edit_text_clear);
        eyeIcon.setOnClickListener(clickListener);
        clearIcon.setOnClickListener(clickListener);

        editTextView.addTextChangedListener(defaultTextWatcher);
        editTextView.setOnFocusChangeListener(onFocusChangeListener);
        editTextView.setPadding(editPadding, editPadding, editPadding, editPadding);
        editTextView.setNormalBorderColor(normalBorder);
        editTextView.setActiveBorderColor(activeBorder);
        editTextView.setCornerSize(editCorner);
        editTextView.setHint(editHint);
        editTextView.setText(editValue);
        editTextView.setInputType(getInputType());
        editTextView.setMaxLength(editMaxLen);
        editTextView.setSingleLine(editMaxLine <= 1);
        if (editMinHeight > 0) {
            editTextView.setMinHeight(editMinHeight);
        }
        if (!TextUtils.isEmpty(editExtractRegex)) {
            // 值过滤正则表达式
            editTextView.setValueExtractRegex(editExtractRegex);
        }
        if (!TextUtils.isEmpty(editVerifyRegex)) {
            editTextView.setValueVerifyRegex(editVerifyRegex);
        }
        clearIcon.setText(iconClear);
        eyeIcon.setText(iconEye);
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
    public @interface InputType {
    }

    /**
     * 设置文本输入方式
     */
    public void setInputType(@InputType int inputType) {
        editType = inputType;
        if (editType > 0) {
            editTextView.setInputType(getInputType());
        }
    }

    private int getInputType() {
        int type;
        switch (editType) {
            case TYPE_PHONE:
                type = android.text.InputType.TYPE_CLASS_PHONE;
                break;
            case TYPE_NUMBER:
                type = android.text.InputType.TYPE_CLASS_NUMBER;
                break;
            case TYPE_PASSWORD:
                type = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
                break;
            case TYPE_VISIBLE_PASSWORD:
                type = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
                break;
            default:
                // 默认文字输入方式
                type = android.text.InputType.TYPE_CLASS_TEXT;
                break;
        }
        // 末尾增加no suggestions避免多次调用onTextChangeListener
        return type | android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS;
    }

    /**
     * 当前是否是密码输入方式
     */
    private boolean isInputForPassword() {
        return editType == TYPE_PASSWORD || editType == TYPE_VISIBLE_PASSWORD;
    }

    private OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            visibleIcons(hasFocus);
        }
    };

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
            visibleIcons(true);
            if (null != __textWatcher) {
                __textWatcher.afterTextChanged(s);
            }
        }
    };

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
     * 验证输入的值
     */
    public boolean verifyValue() {
        return editTextView.verifyValue();
    }

    /**
     * 添加输入控制
     */
    public void addTextChangedListener(TextWatcher watcher) {
        __textWatcher = watcher;
    }
}
