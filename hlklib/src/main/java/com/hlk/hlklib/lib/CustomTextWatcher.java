package com.hlk.hlklib.lib;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * 文本框文字更改事件
 * 作者：Hsiang Leekwok on 2015/11/12 20:24<br />
 * 邮箱：xiang.l.g@gmail.com<br />
 */
public class CustomTextWatcher implements TextWatcher {

    private View view;

    public CustomTextWatcher(View view) {
        super();
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (null != mOnTextChangedListener) {
            mOnTextChangedListener.onTextChanged(view, s);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private OnTextChangedListener mOnTextChangedListener;

    /**
     * 添加文本更改事件处理回调
     */
    public CustomTextWatcher addOnTextChangedListener(OnTextChangedListener l) {
        mOnTextChangedListener = l;
        return this;
    }

    public interface OnTextChangedListener {
        void onTextChanged(View view, CharSequence s);
    }
}
