package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.Utility;

/**
 * 自定义打分<br />
 * Created by Hsiang Leekwok on 2015/12/03.
 */
public class CustomRating extends LinearLayout {

    // Static
    private static final int DEFAULT_BUTTONS = 3;// 默认3个button
    private static final int DEFAULT_SELECTED = -1;
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#65656a");
    private static final String[] DEFAULT_TITLES = new String[]{"好", "一般", "很差"};
    private static final int DEFAULT_TEXT_SIZE = Utility.ConvertDp(14);
    // UI
    private TextView mTitle;
    private LinearLayout mButtonContainer;
    private TextView[] mItems;

    // Data
    private float density;
    private int buttons;// 按钮数量
    private int selected;// 选中的按钮
    private int textSize;
    private String title;// 标题
    private ColorStateList textColor;// 文字颜色
    private Drawable staticBackground, activeBackground;// 按钮背景
    private String[] titles;

    public CustomRating(Context context) {
        this(context, null);
    }

    public CustomRating(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRating(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = getResources().getDisplayMetrics().density;
        initAttr(context, attrs, defStyleAttr);
    }

    private void initAttr(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomRating, defStyle, 0);
        initAttr(a);
        a.recycle();

        initView(context);
    }

    private void initAttr(TypedArray a) {
        buttons = a.getInteger(R.styleable.CustomRating_buttons, DEFAULT_BUTTONS);
        title = a.getString(R.styleable.CustomRating_ratingTitle);
        textColor = a.getColorStateList(R.styleable.CustomRating_textColor);
        if (null == textColor) {
            textColor = ColorStateList.valueOf(DEFAULT_TEXT_COLOR);
        }
        staticBackground = a.getDrawable(R.styleable.CustomRating_buttonBackgroundNormal);
        if (null == staticBackground) {
            staticBackground = ContextCompat.getDrawable(getContext(), R.drawable.hlklib_custom_rating_button_static);
            //getContext().getResources().getDrawable(R.drawable.hlklib_custom_rating_button_static);
        }
        activeBackground = a.getDrawable(R.styleable.CustomRating_buttonBackgroundActive);
        if (null == activeBackground) {
            activeBackground = ContextCompat.getDrawable(getContext(), R.drawable.hlklib_custom_rating_button_active);
            //getContext().getResources().getDrawable(R.drawable.hlklib_custom_rating_button_active);
        }
        selected = a.getInteger(R.styleable.CustomRating_selectedButton, DEFAULT_SELECTED);
        String buttonTitles = a.getString(R.styleable.CustomRating_buttonTitles);
        if (!TextUtils.isEmpty(buttonTitles)) {
            titles = buttonTitles.split("\\|", buttons);
        } else titles = DEFAULT_TITLES;

        textSize = a.getDimensionPixelSize(R.styleable.CustomRating_cr_textSize, 0);
        if (0 == textSize) textSize = DEFAULT_TEXT_SIZE;
        textSize = (int) (textSize / density);
    }

    private void initView(Context context) {
        LinearLayout view = (LinearLayout) View.inflate(context, R.layout.hlklib_custom_rating, this);
        mTitle = (TextView) view.findViewById(R.id.ui_custom_rating_title);
        mButtonContainer = (LinearLayout) view.findViewById(R.id.ui_custom_rating_button_container);
        mTitle.setText(title);
        mTitle.setTextSize(textSize);
        initButtons(context);
    }

    @SuppressWarnings("deprecation")
    private void initButtons(Context context) {
        mButtonContainer.removeAllViews();
        if (buttons < 2)
            throw new IllegalArgumentException("Items size limit to 2.");
        if (selected < -1 || selected >= buttons)
            throw new IllegalArgumentException("Can not access selected item.");
        if (titles.length < buttons)
            throw new IllegalArgumentException("Not enough item text.");
        int padding = Utility.ConvertDp(5);
        int minWidth = Utility.ConvertDp(60);
        mItems = new TextView[buttons];
        for (int i = 0; i < buttons; i++) {
            LinearLayout layout = new LinearLayout(context);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
            layout.setLayoutParams(params);
            //layout.setGravity(Gravity.RIGHT);

            // TextView
            TextView tv = new TextView(context);
            tv.setTextSize(textSize);
            LayoutParams tp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tp.gravity = Gravity.CENTER;
            tv.setLayoutParams(tp);
            tv.setPadding(padding, padding, padding, padding);
            //tv.setBackgroundDrawable(staticBackground);
            tv.setText(titles[i]);
            tv.setTextColor(textColor);
            tv.setTag(R.id.hlklib_ids_custom_rating_bar_tag_index, i);
            tv.setOnClickListener(mOnClickListener);
            tv.setGravity(Gravity.CENTER);
            tv.setMinWidth(minWidth);
            if (Build.VERSION.SDK_INT < 16) {
                tv.setBackgroundDrawable(i == selected ? activeBackground : staticBackground);
            } else {
                tv.setBackground(i == selected ? activeBackground : staticBackground);
            }

            mItems[i] = tv;
            layout.addView(tv);

            mButtonContainer.addView(layout);
        }
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag(R.id.hlklib_ids_custom_rating_bar_tag_index);
            setSelected(tag);
        }
    };

    private void changeSelected(int select) {
        if (selected != select) {
            selected = select;
            resetSelected();
            if (null != mOnRatingChangedListener) {
                mOnRatingChangedListener.onChanged(CustomRating.this, selected);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void resetSelected() {
        for (TextView text : mItems) {
            int tag = (int) text.getTag(R.id.hlklib_ids_custom_rating_bar_tag_index);
            if (Build.VERSION.SDK_INT < 16) {
                text.setBackgroundDrawable(tag == selected ? activeBackground : staticBackground);
            } else {
                text.setBackground(tag == selected ? activeBackground : staticBackground);
            }
        }
    }

    /**
     * 获取当前评价的分数
     */
    public int getRatingValue() {
        return selected;
    }

    public void setSelected(int selected) {
        if (selected < -1 || selected >= buttons)
            throw new IllegalArgumentException("Can not set selection item: " + selected);
        changeSelected(selected);
    }

    private OnRatingChangedListener mOnRatingChangedListener;

    public void addOnRatingChangedListener(OnRatingChangedListener l) {
        mOnRatingChangedListener = l;
    }

    /**
     * 评价更改时
     */
    public interface OnRatingChangedListener {
        /**
         * 评价更改回调
         *
         * @param view   被评价的view
         * @param rating 被选中的值，从0开始
         */
        void onChanged(View view, int rating);
    }
}
