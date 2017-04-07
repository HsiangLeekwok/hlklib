package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.Utility;

/**
 * 自定义边框形状的按钮
 * Created by Hsiang Leekwok on 2015/07/14.
 */
public class CustomButton extends AppCompatButton {

    // 无边角
    public static final int CORNER_NONE = 0;
    // 角在左侧
    public static final int CORNER_LEFT = -1;
    // 角在右侧
    public static final int CORNER_RIGHT = 1;
    // 两侧都有角
    public static final int CORNER_LEFT_RIGHT = 2;
    /**
     * 边角在底部
     */
    public static final int CORNER_BOTTOM = 3;
    /**
     * 边角在顶上
     */
    public static final int CORNER_TOP = 4;
    /**
     * 凹角
     */
    public static final int CORNER_CONCAVE = -1;
    /**
     * 凸角
     */
    public static final int CORNER_CONVEX = 1;
    /**
     * 默认背景颜色
     */
    private static final int DEFAULT_STATIC_COLOR = Color.parseColor("#2fd0c8");
    /**
     * 活动时的背景色
     */
    private static final int DEFAULT_ACTIVE_COLOR = Color.parseColor("#2cc3bb");
    /**
     * 默认禁用时的背景色
     */
    private static final int DEFAULT_DISABLED_COLOR = Color.parseColor("#bfbfbf");
    /**
     * 角宽度
     */
    private static final int DEFAULT_CORNER_WIDTH = Utility.ConvertDp(10);

    private int mStaticColor = DEFAULT_STATIC_COLOR, mActiveColor = DEFAULT_ACTIVE_COLOR,
            mDisabledColor = DEFAULT_DISABLED_COLOR,
            cornerSize = DEFAULT_CORNER_WIDTH, mCornerSide = CORNER_NONE,
            mLeftCorner = CORNER_NONE, mRightCorner = CORNER_NONE,
            mBottomCorner = CORNER_NONE, mTopCorner = CORNER_NONE;

    public CustomButton(Context context) {
        super(context);
        initCorner();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton, defStyleAttr, 0);
        mStaticColor = a.getColor(R.styleable.CustomButton_static_color, DEFAULT_STATIC_COLOR);
        mActiveColor = a.getColor(R.styleable.CustomButton_positive_color, DEFAULT_ACTIVE_COLOR);
        mDisabledColor = a.getColor(R.styleable.CustomButton_disable_color, DEFAULT_DISABLED_COLOR);
        cornerSize = a.getDimensionPixelOffset(R.styleable.CustomButton_corner_width, DEFAULT_CORNER_WIDTH);
        mCornerSide = a.getInt(R.styleable.CustomButton_corner_side, CORNER_NONE);
        mLeftCorner = a.getInt(R.styleable.CustomButton_left_corner, CORNER_NONE);
        mRightCorner = a.getInt(R.styleable.CustomButton_right_corner, CORNER_NONE);
        mBottomCorner = a.getInt(R.styleable.CustomButton_bottom_corner, CORNER_NONE);
        mTopCorner = a.getInt(R.styleable.CustomButton_top_corner, CORNER_NONE);
        a.recycle();
        initCorner();
    }

    // 初始化左右角设定
    private void initCorner() {
        switch (mCornerSide) {
            case CORNER_NONE:
                mRightCorner = CORNER_NONE;
                mLeftCorner = CORNER_NONE;
                mBottomCorner = CORNER_NONE;
                mTopCorner = CORNER_NONE;
                break;
            case CORNER_LEFT:
                // 只设定了左边角时，右边角为none
                mRightCorner = CORNER_NONE;
                mBottomCorner = CORNER_NONE;
                mTopCorner = CORNER_NONE;
                break;
            case CORNER_RIGHT:
                // 只设定了右边角时，左边角为none
                mLeftCorner = CORNER_NONE;
                mBottomCorner = CORNER_NONE;
                mTopCorner = CORNER_NONE;
                break;
            case CORNER_LEFT_RIGHT:
                mBottomCorner = CORNER_NONE;
                mTopCorner = CORNER_NONE;
                break;
            case CORNER_BOTTOM:
                mLeftCorner = CORNER_NONE;
                mRightCorner = CORNER_NONE;
                mTopCorner = CORNER_NONE;
                break;
            case CORNER_TOP:
                mBottomCorner = CORNER_NONE;
                mLeftCorner = CORNER_NONE;
                mRightCorner = CORNER_NONE;
                break;
        }
    }

    public void setStaticColor(int staticColor) {
        this.mStaticColor = staticColor;
        invalidate();
    }

    public void setActiveColor(int activeColor) {
        this.mActiveColor = activeColor;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        customDraw(canvas);
        super.onDraw(canvas);
    }

    private boolean hasLeftCorner() {
        return mCornerSide == CORNER_LEFT_RIGHT || mCornerSide == CORNER_LEFT;
    }

    private boolean hasRightCorner() {
        return mCornerSide == CORNER_LEFT_RIGHT || mCornerSide == CORNER_RIGHT;
    }

    private boolean hasBottomCorner() {
        return mCornerSide == CORNER_BOTTOM;
    }

    private boolean hasTopCorner() {
        return mCornerSide == CORNER_TOP;
    }

    private Path getLeftPath(int width, int height) {
        int hw = width / 2, hh = height / 2;
        int c = cornerSize;
        Path p = new Path();
        switch (mLeftCorner) {
            // 无角
            case CORNER_NONE:
                // 左上角
                p.moveTo(0, 0);
                // 右上角
                p.lineTo((c > hw ? hw : c), 0);
                // 右下角
                p.lineTo((c > hw ? hw : c), height - (c > hh ? hh : c));
                // 左下角
                p.lineTo(0, height - (c > hh ? hh : c));
                break;
            // 凸角
            case CORNER_CONVEX:
                p.moveTo(0, hh);
                p.lineTo(cornerSize, 0);
                p.lineTo(cornerSize, height);
                break;
            // 凹角
            case CORNER_CONCAVE:
                p.moveTo(0, 0);
                p.lineTo(cornerSize, 0);
                p.lineTo(cornerSize, height);
                p.lineTo(0, height);
                p.lineTo(cornerSize, hh);
                break;
        }
        p.close();
        return p;
    }

    private Path getRightPath(int width, int height) {
        int hw = width / 2, hh = height / 2;
        int c = cornerSize;
        Path p = new Path();
        switch (mRightCorner) {
            // 无边角
            case CORNER_NONE:
                if (hasBottomCorner()) {
                    // 有下边角
                    p.moveTo(width - (c > hw ? hw : c), 0);
                    p.lineTo(width, 0);
                    p.lineTo(width, height - (c > hh ? hh : c));
                    p.lineTo(width - (c > hw ? hw : c), height - (c > hh ? hh : c));
                    p.close();
                } else {
                    // 左上角
                    p.moveTo(width - (c > hw ? hw : c), 0);
                    p.lineTo(width, 0);
                    p.lineTo(width, height);
                    p.lineTo(width - (c > hw ? hw : c), height);
                    p.close();
                }
                break;
            // 凹角
            case CORNER_CONCAVE:
                p.moveTo(width - (c > hw ? hw : c), 0);
                p.lineTo(width, 0);
                p.lineTo(width - (c > hw ? hw : c), (c > hh ? hh : c));
                //p.lineTo(width, height);
                //p.lineTo(width - (c > hw ? hw : c), height);
                p.close();
                break;
            // 凸角
            case CORNER_CONVEX:
                p.moveTo(width - (c > hw ? hw : c), 0);
                p.lineTo(width, (c > hh ? hh : c));
                p.lineTo(width - (c > hw ? hw : c), (c > hh ? hh : c));
                p.close();
                break;
        }
        return p;
    }

    // 获取底部除去左右边角部分之后剩余的部分填充
    private Path getBottomLeftoverPath(int width, int height) {
        int hw = width / 2, hh = height / 2;
        int c = cornerSize;
        Path p = new Path();
        p.moveTo((c > hw ? hw : c), (c > hh ? hh : c));
        p.lineTo(width - (c > hw ? hw : c), (c > hh ? hh : c));
        p.lineTo(width - (c > hw ? hw : c), height);
        p.lineTo((c > hw ? hw : c), height);
        p.close();
        return p;
    }

    /**
     * 获取顶部除去左右边角部分之后剩余的部分填充
     */
    private Path getTopLeftoverPath(int width, int height) {
        int hw = width / 2, hh = height / 2;
        int c = cornerSize;
        Path p = new Path();
        p.moveTo((c > hw ? hw : c), 0);
        p.lineTo(width - (c > hw ? hw : c), 0);
        p.lineTo(width - (c > hw ? hw : c), (c > hh ? hh : c));
        p.lineTo((c > hw ? hw : c), (c > hh ? hh : c));
        p.close();
        return p;
    }

    private Path getBottomPath(int width, int height) {
        int halfW = width / 2, halfH = height / 2;
        int c = cornerSize;
        int zero = height - c;
        Path p = new Path();
        switch (mBottomCorner) {
            case CORNER_NONE:
                if (hasLeftCorner()) {
                    // 有左边角
                    if (mLeftCorner == CORNER_CONCAVE) {
                        // 凹角
                        p.moveTo(0, height);
                        p.lineTo((c > halfW ? halfW : c), (c > halfH ? halfH : c));
                        p.lineTo((c > halfW ? halfW : c), height);
                    } else if (mLeftCorner == CORNER_CONVEX) {
                        // 凸角
                        p.moveTo(0, (c > halfH ? halfH : c));
                        p.lineTo((c > halfW ? halfW : c), (c > halfH ? halfH : c));
                        p.lineTo((c > halfW ? halfW : c), height);
                    }
                } else {
                    // 无左边角
                    p.moveTo(0, (c > halfH ? halfH : c));
                    p.lineTo((c > halfW ? halfW : c), (c > halfH ? halfH : c));
                    p.lineTo((c > halfW ? halfW : c), height);
                    p.lineTo(0, height);
                }
                if (hasRightCorner()) {
                    // 有右边角
                    if (mRightCorner == CORNER_CONCAVE) {
                        // 凹角
                        p.moveTo(width - (c > halfW ? halfW : c), (c > halfH ? halfH : c));
                        p.lineTo(width - (c > halfW ? halfW : c), height);
                        p.lineTo(width, height);
                    } else if (mRightCorner == CORNER_CONVEX) {
                        // 凸角
                        p.moveTo(width, (c > halfH ? halfH : c));
                        p.lineTo(width - (c > halfW ? halfW : c), (c > halfH ? halfH : c));
                        p.lineTo(width - (c > halfW ? halfW : c), height);
                    }
                } else {
                    // 无右边角
                    p.moveTo(width - (c > halfW ? halfW : c), (c > halfH ? halfH : c));
                    p.lineTo(width, (c > halfH ? halfH : c));
                    p.lineTo(width, height);
                    p.lineTo(width - (c > halfW ? halfW : c), height);
                }
                p.close();
                // 加上去除左右边角之后剩余的部分
                p.addPath(getBottomLeftoverPath(width, height));
                break;
            case CORNER_CONCAVE:
                // 凹角
                p.moveTo(0, zero);
                p.lineTo(0, height);
                p.lineTo(halfW, zero);
                p.lineTo(width, height);
                p.lineTo(width, zero);
                break;
            case CORNER_CONVEX:
                // 凸角
                p.moveTo(0, zero);
                p.lineTo(halfW, height);
                p.lineTo(width, zero);
                break;
        }
        return p;
    }

    /**
     * 获取去除所有边角之后的实体框
     */
    private Path getNoneCornerPath(int width, int height) {
        int halfW = width / 2, halfH = height / 2;
        int c = cornerSize;
        Path p = new Path();
        // 左上角
        p.moveTo(c > halfW ? halfH : c, c > halfH ? halfH : c);
        // 右上角
        p.lineTo(width - (c > halfW ? halfW : c), c > halfH ? halfH : c);
        // 右下角
        p.lineTo(width - (c > halfW ? halfW : c), height - (c > halfH ? halfH : c));
        // 左下角
        p.lineTo(c > halfW ? halfH : c, height - (c > halfH ? halfH : c));
        // 关闭
        p.close();
        return p;
    }

    private Path getPath() {
        int width = getMeasuredWidth(), height = getMeasuredHeight();
        Path p = new Path();
        // 画除去上下左右角的实体框
        //p.moveTo(cornerSize, 0);
        //p.lineTo(width - cornerSize, 0);
        //p.lineTo(width - cornerSize, height - cornerSize);
        //p.lineTo(cornerSize, height - cornerSize);
        //p.close();

        p.addPath(getNoneCornerPath(width, height));
        p.addPath(getLeftPath(width, height));
        p.addPath(getRightPath(width, height));
        p.addPath(getBottomPath(width, height));
        p.addPath(getTopLeftoverPath(width, height));
        return p;
    }

    private void customDraw(Canvas canvas) {
        boolean enabled = isEnabled();
        Path path = getPath();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(!enabled ? mDisabledColor : (isActive ? mActiveColor : mStaticColor));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);
    }

    private boolean isActive = false;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                isActive = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_OUTSIDE:
                isActive = false;
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }
}
