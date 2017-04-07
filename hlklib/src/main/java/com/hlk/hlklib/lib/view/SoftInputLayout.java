package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.hlk.hlklib.etc.Utility;

/**
 * <b>功能：</b><br />
 * <b>作者：</b>Hsiang Leekwok <br />
 * <b>时间：</b>2016/12/09 20:56 <br />
 * <b>邮箱：</b>xiang.l.g@gmail.com <br />
 */

public class SoftInputLayout extends LinearLayout {

    private static final String TAG = SoftInputLayout.class.getSimpleName();
    /**
     * 内容+键盘的实际最大允许的高度
     */
    private int mRawLayoutHeight = 0;

    /**
     * 当前内容的高度
     */
    private int mCurrentContentHeight = 0;

    /**
     * 内容布局
     */
    private View mContentLayout;

    /**
     * 表情布局
     */
    private View mEmojiLayout;

    private boolean isSoftInputShow = false;

    private boolean isEmojiLayoutShow = false;

    private OnSoftInputChangeListener mOnSoftInputChangeListener;

    public SoftInputLayout(Context context) {
        super(context);
    }

    public SoftInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static void hideSoftInput(Context context, View editText) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount != 2) {
            throw new IllegalArgumentException("Only allow 2 children include in SoftInputLayout.");
        }
        mContentLayout = getChildAt(0);
        mEmojiLayout = getChildAt(1);
        mContentLayout.setVisibility(VISIBLE);
        mEmojiLayout.setVisibility(GONE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //拿到第一次的高度, 一般这个时候都是布局的真实高度.之后resize会改变这个高度
        if (oldh == 0) {
            mRawLayoutHeight = h;
        }
        if (mRawLayoutHeight == h) {
            isSoftInputShow = false;
        } else {
            //键盘弹出的时候,高度会变化, 需要隐藏表情布局
            if (mCurrentContentHeight != h) {
                fixContentLayoutHeight(mCurrentContentHeight, h);
            }
            mCurrentContentHeight = h;
            isSoftInputShow = true;
            hideEmojiLayout();
        }
        if (mOnSoftInputChangeListener != null) {
            mOnSoftInputChangeListener.onSoftInputChange(isSoftInputShow, mRawLayoutHeight, h);
        }
    }

    /**
     * 修改第一次打开表情之前, 键盘没有弹出之前的BUG.
     */
    private void fixContentLayoutHeight(final int oldH, final int newH) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                LayoutParams params = (LayoutParams) mContentLayout.getLayoutParams();
                params.height = newH;
                params.weight = 0;
                requestLayout();
                unlockContentLayoutHeight();
            }
        }, 100);
    }

    /**
     * 此方法的作用就是在键盘弹出后,然后按下了键盘上的关闭键盘按钮, 布局能自适应.
     */
    private void unlockContentLayoutHeight() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                LayoutParams params = (LayoutParams) mContentLayout.getLayoutParams();
                params.height = 0;
                params.weight = 1;
                requestLayout();
            }
        }, 300);
    }

    /**
     * 设置预保存的键盘高度，当首次打开页面且键盘在这个页面从未打开时，可以以此高度初始化表情layout
     */
    public void setDefaultEmojiLayoutHeight(int height) {
        defaultEmojiHeight = height;
    }

    /**
     * 强制内容和表情布局的高度,这样在键盘弹出的时候,就不会导致布局跳动了
     */
    public void showEmojiLayout() {
        hideSoftInput(getContext(), this);
        int keyboardHeight;
        if (mCurrentContentHeight == 0) {
            keyboardHeight = getDefaultEmojiHeight();
            mCurrentContentHeight = mRawLayoutHeight - keyboardHeight;
        } else {
            keyboardHeight = mRawLayoutHeight - mCurrentContentHeight;
        }

        LayoutParams contentParams = (LayoutParams) mContentLayout.getLayoutParams();
        contentParams.height = mCurrentContentHeight;
        contentParams.weight = 0;
        LayoutParams emojiParams = (LayoutParams) mEmojiLayout.getLayoutParams();
        emojiParams.height = keyboardHeight;
        emojiParams.weight = 0;
        mEmojiLayout.setVisibility(VISIBLE);
        requestLayout();
        isEmojiLayoutShow = true;
    }

    /**
     * 恢复成默认就行了
     */
    public void hideEmojiLayout() {
        LayoutParams params = (LayoutParams) mContentLayout.getLayoutParams();
        params.height = 0;
        params.weight = 1;
        LayoutParams params2 = (LayoutParams) mEmojiLayout.getLayoutParams();
        params2.height = 0;
        params2.weight = 0;
        mEmojiLayout.setVisibility(GONE);
        isEmojiLayoutShow = false;
        requestLayout();
    }

    /**
     * 当表情显示的时候, 按下返回键,优先隐藏表情, 然后再finish activity
     */
    public boolean handleBack() {
        if (isEmojiLayoutShow) {
            hideEmojiLayout();
            return true;
        }
        return false;
    }

    public boolean isSoftInputShow() {
        return isSoftInputShow;
    }

    public boolean isEmojiLayoutShow() {
        return isEmojiLayoutShow;
    }

    public void setOnSoftInputChangeListener(OnSoftInputChangeListener onSoftInputChangeListener) {
        mOnSoftInputChangeListener = onSoftInputChangeListener;
    }

    private int defaultEmojiHeight = 0;

    /**
     * 默认的表情布局高度: 150dp
     */
    private int getDefaultEmojiHeight() {
        return 0 == defaultEmojiHeight ? Utility.ConvertDp(150) : defaultEmojiHeight;
    }

    public interface OnSoftInputChangeListener {
        void onSoftInputChange(boolean show, int layoutHeight, int contentHeight);
    }
}
