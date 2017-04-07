package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.Utility;


/**
 * 自定义Switch
 * 作者：Hsiang Leekwok on 2015/09/06 21:03<br />
 * 邮箱：xiang.l.g@gmail.com<br />
 */
public class CustomSwitch extends LinearLayout {

    /**
     * 选择方式
     */
    public enum SwitchMode {
        /**
         * 左边选中
         */
        LEFT,
        /**
         * 中间选中
         */
        CENTER,
        /**
         * 右边选中
         */
        RIGHT
    }

    /**
     * 2项选择
     */
    private static final int SWITCH_TYPE_TWO = 0;
    /**
     * 3项选择
     */
    private static final int SWITCH_TYPE_THREE = 1;

    /**
     * 默认选中的左边
     */
    private SwitchMode _Mode = SwitchMode.LEFT;

    private View _Root;
    private TextView _left, _center, _right;

    private float density;

    // 设置
    private int switch_mode = SWITCH_TYPE_TWO;// 默认只有2项选择
    private String _leftText, _rightText, _centerText;
    private int _fontSize;
    private Drawable leftOnBg, leftOffBg, centerOnBg, centerOffBg, rightOnBg, rightOffBg;
    int background, active, normal;
    private int _selectedText, _unselectedText, innerCorner;

    public CustomSwitch(Context context) {
        this(context, null);
    }

    public CustomSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
        initDefaults(context);
    }

    private Drawable getDrawable(boolean active, SwitchMode mode) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(active ? this.active : normal);
        float c = innerCorner;
        gd.setCornerRadii(new float[]{
                mode == SwitchMode.LEFT ? c : 0, mode == SwitchMode.LEFT ? c : 0,
                mode == SwitchMode.RIGHT ? c : 0, mode == SwitchMode.RIGHT ? c : 0,
                mode == SwitchMode.RIGHT ? c : 0, mode == SwitchMode.RIGHT ? c : 0,
                mode == SwitchMode.LEFT ? c : 0, mode == SwitchMode.LEFT ? c : 0});
        return gd;
    }

    private void resetBackgrounds() {
        leftOffBg = getDrawable(false, SwitchMode.LEFT);
        leftOnBg = getDrawable(true, SwitchMode.LEFT);
        centerOffBg = getDrawable(false, SwitchMode.CENTER);
        centerOnBg = getDrawable(true, SwitchMode.CENTER);
        rightOffBg = getDrawable(false, SwitchMode.RIGHT);
        rightOnBg = getDrawable(true, SwitchMode.RIGHT);
    }

    @Deprecated
    private void initDefaults(Context context) {
        Resources res = context.getResources();
        background = Color.WHITE;
        innerCorner = res.getDimensionPixelOffset(R.dimen.hlklib_custom_switch_corner_size_inner);
        resetBackgrounds();
        findViews();
        initViews();
    }

    private void findViews() {
        _Root = View.inflate(getContext(), R.layout.hlklib_custom_switch, this);
        _left = (TextView) _Root.findViewById(R.id.hlklib_custom_switch_left_text);
        _right = (TextView) _Root.findViewById(R.id.hlklib_custom_switch_right_text);
        _center = (TextView) _Root.findViewById(R.id.hlklib_custom_switch_center_text);
        _left.setOnClickListener(mOnClickListener);
        _right.setOnClickListener(mOnClickListener);
        _center.setOnClickListener(mOnClickListener);
    }

    private int getColor(int res) {
        return ContextCompat.getColor(getContext(), res);
    }

    private void initialize(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSwitch);
        String temp = a.getString(R.styleable.CustomSwitch_switch_off_text);
        if (!TextUtils.isEmpty(temp)) {
            _rightText = temp;
        } else {
            _rightText = context.getResources().getString(R.string.hlklib_custom_switch_right_text);
        }
        temp = a.getString(R.styleable.CustomSwitch_switch_on_text);
        if (!TextUtils.isEmpty(temp)) {
            _leftText = temp;
        } else {
            _leftText = context.getResources().getString(R.string.hlklib_custom_switch_left_text);
        }
        temp = a.getString(R.styleable.CustomSwitch_switch_center_text);
        if (!TextUtils.isEmpty(temp)) {
            _centerText = temp;
        } else {
            _centerText = context.getResources().getString(R.string.hlklib_custom_switch_left_text);
        }
        int color = a.getColor(R.styleable.CustomSwitch_switch_active_background, 0);
        if (0 != color) {
            active = color;
        } else {
            active = getColor(R.color.hlklib_custom_switch_selected_color);
        }
        color = a.getColor(R.styleable.CustomSwitch_switch_static_background, 0);
        if (0 != color) {
            normal = color;
        } else {
            normal = getColor(R.color.hlklib_custom_switch_unselected_color);
        }
        int font = a.getDimensionPixelOffset(R.styleable.CustomSwitch_switch_font_size, 0);
        if (font > 0) {
            _fontSize = (int) (font / density);
        } else {
            _fontSize = Utility.ConvertDp(14);
        }
        switch_mode = a.getInteger(R.styleable.CustomSwitch_switch_mode, SWITCH_TYPE_TWO);
        font = a.getColor(R.styleable.CustomSwitch_switch_off_text_color, 0);
        if (0 != font) {
            _unselectedText = font;
        } else {
            _selectedText = getColor(R.color.hlklib_custom_switch_selected_text_color);
        }
        font = a.getColor(R.styleable.CustomSwitch_switch_on_text_color, 0);
        if (0 != font) {
            _selectedText = font;
        } else {
            _unselectedText = getColor(R.color.hlklib_custom_switch_unselected_text_color);
        }
        a.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    private void initViews() {
        _center.setVisibility(switch_mode == SWITCH_TYPE_THREE ? View.VISIBLE : View.GONE);
        setLeftText(_leftText);
        setRightText(_rightText);
        setCenterText(_centerText);
    }

    public void setLeftText(String text) {
        if (null != _left) _left.setText(text);
    }

    public void setRightText(String text) {
        if (null != _right) _right.setText(text);
    }

    public void setCenterText(String text) {
        if (null != _center) _center.setText(text);
    }

    public void setFontSize(float size) {
        if (null != _left) _left.setTextSize(size);
        if (null != _right) _right.setTextSize(size);
    }

    public SwitchMode getSwitchMode() {
        return _Mode;
    }

    /**
     * 设置当前选中的选项
     */
    public void setSelectedSwitch(SwitchMode mode) {
        if (_Mode != mode) {
            _Mode = mode;
            changeSelectedBackground();
            if (null != mOnSwitchChangeListener) {
                mOnSwitchChangeListener.onChanged(_Mode);
            }
        }
    }

    /**
     * 手动设置亮色
     */
    public void setActiveColor(int color) {
        if (active != color) {
            active = color;
            resetBackgrounds();
            changeSelectedBackground();
        }
    }

    /**
     * 手动设置暗色
     */
    public void setNormalColor(int color) {
        if (normal != color) {
            normal = color;
            resetBackgrounds();
            changeSelectedBackground();
        }
    }

    @Deprecated
    private void changeSelectedBackground() {
        _left.setBackgroundDrawable(_Mode == SwitchMode.LEFT ? leftOnBg : leftOffBg);
        _left.setTextColor(_Mode == SwitchMode.LEFT ? _selectedText : _unselectedText);
        _right.setBackgroundDrawable(_Mode == SwitchMode.RIGHT ? rightOnBg : rightOffBg);
        _right.setTextColor(_Mode == SwitchMode.RIGHT ? _selectedText : _unselectedText);
        _center.setBackgroundDrawable(_Mode == SwitchMode.CENTER ? centerOnBg : centerOffBg);
        _center.setTextColor(_Mode == SwitchMode.CENTER ? _selectedText : _unselectedText);
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == _left) {
                if (_Mode != SwitchMode.LEFT) {
                    _Mode = SwitchMode.LEFT;
                    changeSelectedBackground();
                    if (null != mOnSwitchChangeListener) {
                        mOnSwitchChangeListener.onChanged(_Mode);
                    }
                }
            } else if (v == _right) {
                if (_Mode != SwitchMode.RIGHT) {
                    _Mode = SwitchMode.RIGHT;
                    changeSelectedBackground();
                    if (null != mOnSwitchChangeListener) {
                        mOnSwitchChangeListener.onChanged(_Mode);
                    }
                }
            } else if (v == _center) {
                if (_Mode != SwitchMode.CENTER) {
                    _Mode = SwitchMode.CENTER;
                    changeSelectedBackground();
                    if (null != mOnSwitchChangeListener) {
                        mOnSwitchChangeListener.onChanged(_Mode);
                    }
                }
            }
        }
    };

    private OnSwitchChangeListener mOnSwitchChangeListener;

    /**
     * 为开关设置状态改变时的处理回调
     */
    public void addOnSwitchChangeListener(OnSwitchChangeListener l) {
        mOnSwitchChangeListener = l;
    }

    /**
     * 开关状态改变事件
     */
    public interface OnSwitchChangeListener {
        /**
         * 开关状态改变事件的处理回调
         *
         * @param mode 标记已经该表到的状态
         */
        void onChanged(SwitchMode mode);
    }
}
