package com.hlk.hlklib.lib;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;

import com.hlk.hlklib.R;

/**
 * 自定义弹出窗口
 * Created by Hsiang Leekwok on 2015/07/15.
 */
public class CustomPopupWindow {
    /**
     * 弹出窗口主体
     */
    private PopupWindow mPopupWindow = null;
    private View mContentView = null;
    private View mTrigger = null;

    private int mBackgroundColor = 0x90000000;

    private boolean fullScreen = false;

    /**
     * ContentView的入场动画
     */
    private Animation contentInAnimation = null;
    /**
     * ContentView的出场动画
     */
    private Animation contentExitAnimation = null;

    /**
     * 创建自定义PopupWindow
     *
     * @param trigger 触发PopupWindow弹出事件的View
     */
    public CustomPopupWindow(View trigger) {
        this.mTrigger = trigger;
    }

    /**
     * 创建自定义PopupWindow
     *
     * @param trigger     触发PopupWindow弹出事件的View
     * @param contentView 要显示的内容View
     */
    public CustomPopupWindow(View trigger, View contentView) {
        this.mTrigger = trigger;
        this.mContentView = contentView;
    }

    /**
     * 设置触发PopupWindow弹出事件的View
     */
    public void setTrigger(View trigger) {
        this.mTrigger = trigger;
    }

    /**
     * 设置弹出窗口是否全屏
     */
    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
        //initializePopupWindow();
    }

    /**
     * 设置PopupWindow的内容视图
     */
    public void setContentView(View contentView) {
        this.mContentView = contentView;
        //initializePopupWindow();
    }

    /**
     * 设置内容视图可点击
     */
    private void initializeContentView() {
        if (null != mContentView) {
            mContentView.setClickable(true);
            mContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mPopupWindow)
                        mPopupWindow.dismiss();
                }
            });
        }
    }

    /**
     * 初始化弹出窗口
     */
    private void initializePopupWindow() {
        if (null == mPopupWindow) {
            mPopupWindow = new PopupWindow(mContentView, ViewGroup.LayoutParams.MATCH_PARENT,
                    fullScreen ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT);
            ColorDrawable cd = new ColorDrawable(mBackgroundColor);
            mPopupWindow.setBackgroundDrawable(cd);
            mPopupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
            // 产生背景变暗效果
            // Window window = ((Activity) mTrigger.getContext()).getWindow();
            // WindowManager.LayoutParams lp = window.getAttributes();
            // lp.alpha = 0.4f;
            // window.setAttributes(lp);

            mPopupWindow.setFocusable(true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setOutsideTouchable(outsideTouchable);
            mPopupWindow.setOnDismissListener(mOnDismissListener);
        }
        initializeContentView();
    }

    private Animation.AnimationListener mExitAnimationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (null != mPopupWindow) {
                mPopupWindow.dismiss();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

    private boolean outsideTouchable = true;

    /**
     * 设置UI外能否点击
     */
    public void setOutsideTouchable(boolean touchable) {
        outsideTouchable = touchable;
        if (null != mPopupWindow) {
            mPopupWindow.setOutsideTouchable(touchable);
        }
    }

    /**
     * 设置窗体的背景颜色
     */
    public void setWindowBackground(int color) {
        mBackgroundColor = color;
    }

    /**
     * 设置窗体的背景颜色
     */
    @SuppressWarnings("deprecation")
    public void setWindowBackgroundResources(int resColor) {
        if (null == mTrigger)
            throw new NullPointerException("No trigger view exist.");
        setWindowBackground(ContextCompat.getColor(mTrigger.getContext(), resColor));
    }

    /**
     * 设置ContentView的出场动画
     */
    public void setContentViewExitAnimation(int anim) {
        contentExitAnimation = AnimationUtils.loadAnimation(
                mTrigger.getContext(), anim);
        contentExitAnimation.setAnimationListener(mExitAnimationListener);
    }

    /**
     * 设置ContentView的出场动画
     */
    public void setContentViewExitAnimation(Animation anim) {
        contentExitAnimation = anim;
        contentExitAnimation.setAnimationListener(mExitAnimationListener);
    }

    /**
     * 设置ContentView的入场动画效果
     */
    public void setContentViewEnterAnimation(int anim) {
        contentInAnimation = AnimationUtils.loadAnimation(
                mTrigger.getContext(), anim);
    }

    /**
     * 设置ContentView的入场动画效果
     */
    public void setContentViewEnterAnimation(Animation anim) {
        contentInAnimation = anim;
    }

    /**
     * 关闭PopupWindow
     */
    public void dismiss() {
        if (null == contentExitAnimation) {
            if (null != mPopupWindow) {
                mPopupWindow.dismiss();
            }
        } else {
            mContentView.startAnimation(contentExitAnimation);
        }
    }

    /**
     * 显示PopupWindow<br>
     * 默认显示在trigger的底部
     */
    public void show() {
        show(Gravity.BOTTOM);
    }

    /**
     * 按照指定的位置显示PopupWindow
     */
    public void show(int gravity) {
        show(gravity, 0, 0);
    }

    /**
     * 按照指定位置和偏移量显示PopupWindow
     */
    public void show(int gravity, int x, int y) {
        initializePopupWindow();
        mPopupWindow.showAtLocation(mTrigger, gravity, x, y);
        mPopupWindow.update();
        // 此处如果ContentView有入场动画的话，显示动画
        if (null != contentInAnimation) {
            mContentView.startAnimation(contentInAnimation);
        }
    }

    private PopupWindow.OnDismissListener mOnDismissListener = new PopupWindow.OnDismissListener() {

        @Override
        public void onDismiss() {
            // 产生背景变暗效果恢复
            // Window window = ((Activity) mTrigger.getContext()).getWindow();
            // WindowManager.LayoutParams lp = window.getAttributes();
            // lp.alpha = 1f;
            // window.setAttributes(lp);
            if (null != onDismissListener) {
                onDismissListener.onDismiss();
            }
        }
    };

    private OnDismissListener onDismissListener;

    /**
     * 设置弹出窗口关闭事件的处理回调
     */
    public void addOnDismissListener(OnDismissListener l) {
        onDismissListener = l;
    }

    /**
     * 弹出窗口关闭事件的处理接口
     */
    public interface OnDismissListener {
        void onDismiss();
    }
}
