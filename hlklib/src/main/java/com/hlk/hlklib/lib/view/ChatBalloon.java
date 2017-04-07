package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.hlk.hlklib.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <b>功能描述：</b>简单的聊天背景<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/01/06 18:51 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/01/06 18:51 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class ChatBalloon extends LinearLayout {

    public ChatBalloon(Context context) {
        this(context, null);
    }

    public ChatBalloon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatBalloon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChatBalloon, defStyleAttr, 0);
        getAttributes(a);
        a.recycle();
        resetPadding();
    }

    // parameters
    private int cornerSize, cornerSizeLeftTop, cornerSizeLeftBottom, cornerSizeRightTop, cornerSizeRightBottom,
            arrowWidth, arrowHeight, arrowSide, arrowMargin, backgroundNormal, backgroundActive,
            borderSize, borderNormal, borderActive;

    /**
     * 获取初始化属性
     */
    private void getAttributes(TypedArray array) {
        Resources res = getContext().getResources();
        cornerSize = array.getDimensionPixelSize(R.styleable.ChatBalloon_cb_corner_size, 0);
        if (cornerSize > 0) {
            cornerSizeLeftBottom = cornerSize;
            cornerSizeLeftTop = cornerSize;
            cornerSizeRightBottom = cornerSize;
            cornerSizeRightTop = cornerSize;
        } else {
            // 默认
            int size = res.getDimensionPixelOffset(R.dimen.hlklib_chat_balloon_corner_size);
            cornerSizeLeftBottom = array.getDimensionPixelSize(R.styleable.ChatBalloon_cb_corner_size_left_bottom, size);
            cornerSizeLeftTop = array.getDimensionPixelSize(R.styleable.ChatBalloon_cb_corner_size_left_top, size);
            cornerSizeRightBottom = array.getDimensionPixelSize(R.styleable.ChatBalloon_cb_corner_size_right_bottom, size);
            cornerSizeRightTop = array.getDimensionPixelSize(R.styleable.ChatBalloon_cb_corner_size_right_top, size);
        }
        arrowWidth = array.getDimensionPixelSize(R.styleable.ChatBalloon_cb_arrow_width, res.getDimensionPixelOffset(R.dimen.hlklib_chat_balloon_arrow_width));
        arrowHeight = array.getDimensionPixelSize(R.styleable.ChatBalloon_cb_arrow_width, res.getDimensionPixelOffset(R.dimen.hlklib_chat_balloon_arrow_height));
        // 箭头默认在右侧上方
        arrowSide = array.getInteger(R.styleable.ChatBalloon_cb_arrow_side, RIGHT_TOP);
        arrowMargin = array.getDimensionPixelOffset(R.styleable.ChatBalloon_cb_arrow_margin, res.getDimensionPixelOffset(R.dimen.hlklib_chat_balloon_arrow_margin));
        backgroundNormal = array.getColor(R.styleable.ChatBalloon_cb_background_normal, Color.WHITE);
        backgroundActive = array.getColor(R.styleable.ChatBalloon_cb_background_active, ContextCompat.getColor(getContext(), R.color.hlklib_chat_balloon_active_color));
        borderSize = array.getDimensionPixelSize(R.styleable.ChatBalloon_cb_border_size, res.getDimensionPixelOffset(R.dimen.hlklib_chat_balloon_border_size));
        borderNormal = array.getColor(R.styleable.ChatBalloon_cb_border_normal, ContextCompat.getColor(getContext(), R.color.hlklib_chat_balloon_active_color));
        borderActive = array.getColor(R.styleable.ChatBalloon_cb_border_active, Color.WHITE);

        padding = gotExistedPadding();
    }

    private int[] padding;

    /**
     * 获取已设置了的padding值
     */
    private int[] gotExistedPadding() {
        int[] ret = new int[4];
        ret[0] = getPaddingLeft();
        if (0 == ret[0]) {
            ret[0] = cornerSizeLeftTop > cornerSizeLeftBottom ? cornerSizeLeftTop : cornerSizeLeftBottom;
        }

        ret[1] = getPaddingTop();
        if (0 == ret[1]) {
            ret[1] = cornerSizeLeftTop > cornerSizeRightTop ? cornerSizeLeftTop : cornerSizeRightTop;
        }

        ret[2] = getPaddingRight();
        if (0 == ret[2]) {
            ret[2] = cornerSizeRightTop > cornerSizeRightBottom ? cornerSizeRightTop : cornerSizeRightBottom;
        }

        ret[3] = getPaddingBottom();
        if (0 == ret[3]) {
            ret[3] = cornerSizeLeftBottom > cornerSizeRightBottom ? cornerSizeLeftBottom : cornerSizeRightBottom;
        }
        return ret;
    }

    /**
     * 根据箭头的方位不同，设置不同的padding
     */
    private void resetPadding() {
        int[] padding = new int[4];
        System.arraycopy(this.padding, 0, padding, 0, 4);
        switch (arrowSide) {
            case RIGHT_BOTTOM:
            case RIGHT_MIDDLE:
            case RIGHT_TOP:
                padding[2] += arrowWidth;
                break;
            case LEFT_BOTTOM:
            case LEFT_MIDDLE:
            case LEFT_TOP:
                padding[0] += arrowWidth;
                break;
            case TOP_CENTER:
            case TOP_LEFT:
            case TOP_RIGHT:
                padding[1] += arrowWidth;
                break;
            case BOTTOM_CENTER:
            case BOTTOM_LEFT:
            case BOTTOM_RIGHT:
                padding[3] += arrowWidth;
                break;
        }
        setPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    private Path gotPath() {
        int width = getMeasuredWidth(), height = getMeasuredHeight();
        switch (arrowSide) {
//            case TOP_LEFT:
//                return gotTopLeftArrowPath();
//            case TOP_CENTER:
//                return null;
//            case TOP_RIGHT:
//                return null;
            case RIGHT_TOP:
            case RIGHT_MIDDLE:
            case RIGHT_BOTTOM:
                return gotRightTopArrowPath(width, height);
//            case RIGHT_MIDDLE:
//                return null;
//            case RIGHT_BOTTOM:
//                return null;
//            case BOTTOM_RIGHT:
//                return null;
//            case BOTTOM_CENTER:
//                return null;
//            case BOTTOM_LEFT:
//                return null;
//            case LEFT_BOTTOM:
//                return null;
//            case LEFT_MIDDLE:
//                return null;
            case LEFT_TOP:
            case LEFT_MIDDLE:
            case LEFT_BOTTOM:
                return gotLeftTopArrowPath(width, height);
            default:
                return gotNoneArrowPath(width, height);
        }
    }

    // 箭头在右侧上边
    private Path gotRightTopArrowPath(int width, int height) {
        Path path = new Path();
        RectF rectF;

        // 左上角
        int diameter = cornerSizeLeftTop * 2;
        if (cornerSizeLeftTop > 0) {
            // 左上角圆角起点
            path.moveTo(0, cornerSizeLeftTop);
            rectF = new RectF(0, 0, diameter, diameter);
            path.arcTo(rectF, 180, 90);
        } else {
            path.moveTo(0, 0);
        }

        // 右上角
        diameter = cornerSizeRightTop * 2;
        if (cornerSizeRightTop > 0) {
            // 右上角圆角起点
            path.lineTo(width - cornerSizeRightTop - arrowWidth, 0);
            rectF = new RectF(width - diameter - arrowWidth, 0, width - arrowWidth, diameter);
            path.arcTo(rectF, -90, 90);
        } else {
            path.lineTo(width - arrowWidth, 0);
        }

        switch (arrowSide) {
            case RIGHT_TOP:
                // 箭头起点
                path.lineTo(width - arrowWidth, arrowMargin);
                // 箭头顶点
                path.lineTo(width, arrowMargin + arrowHeight / 2);
                // 箭头结束点
                path.lineTo(width - arrowWidth, arrowMargin + arrowHeight);
                break;
            case RIGHT_MIDDLE:
                path.lineTo(width - arrowWidth, height / 2 - arrowHeight / 2);
                path.lineTo(width, height / 2);
                path.lineTo(width - arrowWidth, height / 2 + arrowHeight / 2);
                break;
            default:
                // 箭头起点
                path.lineTo(width - arrowWidth, height - arrowMargin - arrowHeight);
                // 箭头顶点
                path.lineTo(width, height - arrowMargin - arrowHeight / 2);
                // 箭头结束点
                path.lineTo(width - arrowWidth, height - arrowMargin);
                break;
        }

        // 右下角
        diameter = cornerSizeRightBottom * 2;
        if (cornerSizeRightBottom > 0) {
            // 右下角圆角起点
            path.lineTo(width - arrowWidth, height - cornerSizeRightBottom);
            rectF = new RectF(width - arrowWidth - diameter, height - diameter, width - arrowWidth, height);
            path.arcTo(rectF, 0, 90);
        } else {
            path.lineTo(width - arrowWidth, height);
        }

        // 左下角
        diameter = cornerSizeLeftBottom * 2;
        if (cornerSizeLeftBottom > 0) {
            // 左下角圆角起点
            path.lineTo(cornerSizeLeftBottom, height);
            rectF = new RectF(0, height - diameter, diameter, height);
            path.arcTo(rectF, 90, 90);
        } else {
            path.lineTo(0, height);
        }
        path.close();
        return path;
    }

    // 箭头在左侧上边
    private Path gotLeftTopArrowPath(int width, int height) {
        Path path = new Path();
        RectF rectF;

        // 左上角
        int diameter = cornerSizeLeftTop * 2;
        if (cornerSizeLeftTop > 0) {
            // 左上角圆角起点
            path.moveTo(arrowWidth, cornerSizeLeftTop);
            rectF = new RectF(arrowWidth, 0, arrowWidth + diameter, diameter);
            path.arcTo(rectF, 180, 90);
        } else {
            path.moveTo(arrowWidth, 0);
        }

        // 右上角
        diameter = cornerSizeRightTop * 2;
        if (cornerSizeRightTop > 0) {
            // 右上角圆角起点
            path.lineTo(width - cornerSizeRightTop, 0);
            rectF = new RectF(width - diameter, 0, width, diameter);
            path.arcTo(rectF, -90, 90);
        } else {
            path.lineTo(width, 0);
        }

        // 右下角
        diameter = cornerSizeRightBottom * 2;
        if (cornerSizeRightBottom > 0) {
            // 右下角圆角起点
            path.lineTo(width, height - cornerSizeRightBottom);
            rectF = new RectF(width - diameter, height - diameter, width, height);
            path.arcTo(rectF, 0, 90);
        } else {
            path.lineTo(width, height);
        }

        // 左下角
        diameter = cornerSizeLeftBottom * 2;
        if (cornerSizeLeftBottom > 0) {
            // 左下角圆角起点
            path.lineTo(arrowWidth + cornerSizeLeftBottom, height);
            rectF = new RectF(arrowWidth, height - diameter, arrowWidth + diameter, height);
            path.arcTo(rectF, 90, 90);
        } else {
            path.lineTo(arrowWidth, height);
        }

        switch (arrowSide) {
            case LEFT_BOTTOM:
                path.lineTo(arrowWidth, height - arrowMargin);
                path.lineTo(0, height - arrowMargin - arrowHeight / 2);
                path.lineTo(arrowWidth, height - arrowMargin - arrowHeight);
                break;
            case LEFT_MIDDLE:
                path.lineTo(arrowWidth, height / 2 + arrowHeight / 2);
                path.lineTo(0, height / 2);
                path.lineTo(arrowWidth, height / 2 - arrowHeight / 2);
                break;
            case LEFT_TOP:
                // 箭头起点
                path.lineTo(arrowWidth, arrowMargin + arrowHeight);
                // 箭头顶点
                path.lineTo(0, arrowMargin + arrowHeight / 2);
                // 箭头结束点
                path.lineTo(arrowWidth, arrowMargin);
                break;
        }
        path.close();
        return path;
    }

    private Path gotNoneArrowPath(int width, int height) {
        Path path = new Path();
        RectF rectF;

        int diameter;
        if (cornerSizeLeftTop > 0) {
            // 左上角圆角起点
            diameter = cornerSizeLeftTop * 2;
            path.moveTo(0, cornerSizeLeftTop);
            rectF = new RectF(0, 0, diameter, diameter);
            path.arcTo(rectF, 180, 90);
        } else {
            path.moveTo(0, 0);
        }

        if (cornerSizeRightTop > 0) {
            // 右上角圆角起点
            diameter = cornerSizeRightTop * 2;
            path.lineTo(width - cornerSizeRightTop, 0);
            rectF = new RectF(width - diameter, 0, width, diameter);
            path.arcTo(rectF, -90, 90);
        } else {
            path.lineTo(width, 0);
        }

        if (cornerSizeRightBottom > 0) {
            // 右下角圆角起点
            diameter = cornerSizeRightBottom * 2;
            path.lineTo(width, height - cornerSizeRightBottom);
            rectF = new RectF(width - diameter, height - diameter, width, height);
            path.arcTo(rectF, 0, 90);
        } else {
            path.lineTo(width, height);
        }

        if (cornerSizeLeftBottom > 0) {
            // 左下角圆角起点
            diameter = cornerSizeLeftBottom * 2;
            path.lineTo(cornerSizeLeftBottom, height);
            rectF = new RectF(0, height - diameter, diameter, height);
            path.arcTo(rectF, 90, 90);
        } else {
            path.lineTo(0, height);
        }
        path.close();
        return path;
    }

    // 上左位
    private Path gotTopLeftArrowPath(int mWidth, int mHeight) {
        Path path = new Path();
        // 箭头高度 + 圆角高度
        path.moveTo(0, arrowHeight + cornerSize);
        int diameter = cornerSize * 2;
        // 左上角圆角
        RectF rectF = new RectF(0, arrowHeight, diameter, arrowHeight + diameter);
        path.arcTo(rectF, 180, 90);
        // 到箭头起始点
        path.lineTo(arrowMargin, arrowHeight);
        // 箭头顶点
        path.lineTo(arrowMargin + arrowWidth / 2, 0);
        // 箭头结束
        path.lineTo(arrowMargin + arrowWidth, arrowHeight);

        // 右上角半圆起始点
        path.lineTo(mWidth - cornerSize, arrowHeight);
        rectF = new RectF(mWidth - diameter, arrowHeight, mWidth, arrowHeight + diameter);
        // 右上角半圆
        path.arcTo(rectF, 270, 90);
        // 右下角半圆起点
        path.lineTo(mWidth, mHeight - cornerSize);
        rectF = new RectF(mWidth - diameter, mHeight - diameter, mWidth, mHeight);
        path.arcTo(rectF, 0, 90);
        // 左下角圆角起点
        path.lineTo(cornerSize, mHeight);
        rectF = new RectF(0, mHeight - diameter, diameter, mHeight);
        path.arcTo(rectF, 90, 90);
        // path 结束
        path.close();
        return path;
    }

    private void customDraw(Canvas canvas) {
        Path path = gotPath();
        Paint fill = new Paint();
        // 消除锯齿
        fill.setAntiAlias(true);
        // 既有边框也有填充
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(isActive ? backgroundActive : backgroundNormal);
        canvas.drawPath(path, fill);
        if (borderSize > 0) {
            // 绘制边框
            Paint border = new Paint();
            border.setAntiAlias(true);
            border.setStyle(Paint.Style.STROKE);
            border.setColor(isActive ? borderActive : borderNormal);
            // 边框粗细
            border.setStrokeWidth(borderSize);
            canvas.drawPath(path, border);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // call block() here if you want to draw behind children
        //canvas.drawColor(Color.TRANSPARENT);
        customDraw(canvas);
        super.dispatchDraw(canvas);
        // call block() here if you want to draw over children
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

    // ****************** getter & setter

    /**
     * 设置箭头的位置
     */
    public void setArrowSide(@ArrowSide int side) {
        arrowSide = side;
        resetPadding();
        invalidate();
    }

    /**
     * 设置边角
     */
    public void setCornerSize(int size) {
        setCornerSize(ALL, size);
    }

    /**
     * 设置指定的边角大小
     */
    public void setCornerSize(@CornerSide int side, int size) {
        switch (side) {
            case ALL:
                cornerSize = size;
                break;
            case RIGHT_TOP:
                cornerSizeRightTop = size;
                break;
            case RIGHT_BOTTOM:
                cornerSizeRightBottom = size;
                break;
            case LEFT_BOTTOM:
                cornerSizeLeftBottom = size;
                break;
            case LEFT_TOP:
                cornerSizeLeftTop = size;
                break;
        }
        resetPadding();
        invalidate();
    }

    /**
     * 设置背景颜色
     */
    public void setBackgroundNormal(int color) {
        backgroundNormal = color;
        invalidate();
    }

    /**
     * 设置激活时的背景颜色
     */
    public void setBackgroundActive(int color) {
        backgroundActive = color;
        invalidate();
    }

    // ****************** 箭头方位定义
    /**
     * 全部
     */
    public static final int ALL = -1;
    /**
     * 无箭头
     */
    public static final int NONE = 0;
    /**
     * 箭头在UI的顶部左边
     */
    public static final int TOP_LEFT = 1;
    /**
     * 箭头在UI的顶部中间
     */
    public static final int TOP_CENTER = 2;
    /**
     * 箭头在UI的顶部右边
     */
    public static final int TOP_RIGHT = 3;
    /**
     * 右侧上边
     */
    public static final int RIGHT_TOP = 4;
    /**
     * 箭头在UI的右侧中间
     */
    public static final int RIGHT_MIDDLE = 5;
    /**
     * 右侧下边
     */
    public static final int RIGHT_BOTTOM = 6;
    /**
     * 箭头在UI的底部右边
     */
    public static final int BOTTOM_RIGHT = 7;
    /**
     * 箭头在UI的底部中间
     */
    public static final int BOTTOM_CENTER = 8;
    /**
     * 箭头在UI的底部左边
     */
    public static final int BOTTOM_LEFT = 9;
    /**
     * 左侧下边
     */
    public static final int LEFT_BOTTOM = 10;
    /**
     * 箭头在UI的左侧中间
     */
    public static final int LEFT_MIDDLE = 11;
    /**
     * 左侧上边
     */
    public static final int LEFT_TOP = 12;

    /**
     * 箭头所在位置
     */
    @IntDef({NONE, TOP_LEFT, TOP_CENTER, TOP_RIGHT, RIGHT_TOP, RIGHT_MIDDLE, RIGHT_BOTTOM,
            BOTTOM_RIGHT, BOTTOM_CENTER, BOTTOM_LEFT, LEFT_BOTTOM, LEFT_MIDDLE, LEFT_TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ArrowSide {
    }

    /**
     * 圆角位置
     */
    @IntDef({ALL, RIGHT_TOP, RIGHT_BOTTOM, LEFT_BOTTOM, LEFT_TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CornerSide {
    }
}
