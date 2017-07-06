package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hlk.hlklib.R;

/**
 * <b>功能描述：</b>可清除内容的EditText<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/07/04 07:59 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/07/04 07:59 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class CleanableEditText extends CorneredEditText implements View.OnFocusChangeListener, TextWatcher {

    private Drawable right;
    private boolean hasFocus;
    private Drawable mClearDrawable;

    public CleanableEditText(Context context) {
        this(context, null);
    }

    public CleanableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public CleanableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 初始化删除的资源图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.hlklib_search_edit_text_delete_icon_pressed);
        }
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());

        clearText(false);

        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (hasFocus) {
            clearText(s.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            clearText(getText().length() > 0);
        } else {
            clearText(false);
        }
    }

    private void clearText(boolean visible) {
        if (visible) {
            right = mClearDrawable;

        } else {
            right = null;
        }
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
        // right.setBounds(0, 0, right.getIntrinsicWidth(),
        // right.getIntrinsicHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                //getTotalPaddingRight  返回 又padding加上图片占据的宽度 在这个范围内 即判断是否点击了删除按钮
                boolean t = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (t) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
