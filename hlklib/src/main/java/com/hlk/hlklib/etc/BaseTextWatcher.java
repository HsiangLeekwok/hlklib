package com.hlk.hlklib.etc;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 自定义EditText文本更改事件监视器
 * Created by Hsiang Leekwok on 2015/07/17.
 */
public class BaseTextWatcher implements TextWatcher {

    /** 文本框中文字改变事件处理接口 */
    public interface OnTextChangedListener {
        /** 文字改变事件处理过程 */
        void onTextChanged(CharSequence s);
    }

    private OnTextChangedListener mOnTextChangedListener;

    /** 添加自定义文字改变处理回调接口 */
    public void setOnTextChangedListener(OnTextChangedListener l) {
        mOnTextChangedListener = l;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (null != mOnTextChangedListener) {
            mOnTextChangedListener.onTextChanged(s);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
