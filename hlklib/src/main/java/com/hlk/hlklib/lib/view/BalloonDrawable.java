package com.hlk.hlklib.lib.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <b>功能描述：</b>聊天背景绘制<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/01/06 22:30 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/01/06 22:30 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class BalloonDrawable extends Drawable {

    private int mWidth, mHeight, bSize, bColor, aSide, aWidth, aHeight, aMargin, cSize, color;
    private Paint mFillPaint, mStrokePaint;

    public BalloonDrawable(int width, int height, int borderSize, int borderColor, @ArrowSide int arrowSide,
                           int arrowWidth, int arrowHeight, int arrowMargin, int cornerSize, int color) {
        mWidth = width;
        mHeight = height;
        bSize = borderSize;
        bColor = borderColor;
        aSide = arrowSide;
        aWidth = arrowWidth;
        aHeight = arrowHeight;
        aMargin = arrowMargin;
        cSize = cornerSize;
        this.color = color;
        initialize();
    }

    private void initialize() {
        mFillPaint = new Paint();
        mStrokePaint = new Paint();
        // 消除锯齿
        mFillPaint.setAntiAlias(true);
        // 既有边框也有填充
        mFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // 边框粗细
        mFillPaint.setStrokeWidth(bSize);
        mFillPaint.setColor(color);
    }

    private Path gotPath() {
        switch (aSide) {
//            case TOP_LEFT:
//                return gotTopLeftArrowPath();
//            case TOP_MIDDLE:
//                return null;
//            case TOP_RIGHT:
//                return null;
            case RIGHT_TOP:
                return gotRightTopArrowPath();
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
                return gotLeftTopArrowPath();
            default:
                return gotNoneArrowPath();
        }
    }

    // 箭头在右侧上边
    private Path gotRightTopArrowPath() {
        Path path = new Path();
        int diameter = cSize * 2;
        // 左上角圆角起点
        path.moveTo(0, cSize);
        RectF rectF = new RectF(0, 0, diameter, diameter);
        path.arcTo(rectF, 180, 90);
        // 右上角圆角起点
        path.lineTo(mWidth - cSize - aWidth, 0);
        rectF = new RectF(mWidth - diameter - aWidth, 0, mWidth - aWidth, diameter);
        path.arcTo(rectF, -90, 90);
        // 箭头起点
        path.lineTo(mWidth - aWidth, aMargin);
        // 箭头顶点
        path.lineTo(mWidth, aMargin + aHeight / 2);
        // 箭头结束点
        path.lineTo(mWidth - aWidth, aMargin + aHeight);
        // 右下角圆角起点
        path.lineTo(mWidth - aWidth, mHeight - cSize);
        rectF = new RectF(mWidth - aWidth - diameter, mHeight - diameter, mWidth - aWidth, mHeight);
        path.arcTo(rectF, 0, 90);
        // 左下角圆角起点
        path.lineTo(cSize, mHeight);
        rectF = new RectF(0, mHeight - diameter, diameter, mHeight);
        path.arcTo(rectF, 90, 90);
        path.close();
        return path;
    }

    // 箭头在左侧上边
    private Path gotLeftTopArrowPath() {
        Path path = new Path();
        int diameter = cSize * 2;
        // 左上角圆角起点
        path.moveTo(aWidth, cSize);
        RectF rectF = new RectF(aWidth, 0, aWidth + diameter, diameter);
        path.arcTo(rectF, 180, 90);
        // 右上角圆角起点
        path.lineTo(mWidth - cSize, 0);
        rectF = new RectF(mWidth - diameter, 0, mWidth, diameter);
        path.arcTo(rectF, -90, 90);
        // 右下角圆角起点
        path.lineTo(mWidth, mHeight - cSize);
        rectF = new RectF(mWidth - diameter, mHeight - diameter, mWidth, mHeight);
        path.arcTo(rectF, 0, 90);
        // 左下角圆角起点
        path.lineTo(aWidth + cSize, mHeight);
        rectF = new RectF(aWidth, mHeight - diameter, aWidth + diameter, mHeight);
        path.arcTo(rectF, 90, 90);
        // 箭头起点
        path.lineTo(aWidth, aMargin + aHeight);
        // 箭头顶点
        path.lineTo(0, aMargin + aHeight / 2);
        // 箭头结束点
        path.lineTo(aWidth, aMargin);
        path.close();
        return path;
    }

    private Path gotNoneArrowPath() {
        Path path = new Path();
        int diameter = cSize * 2;
        // 左上角圆角起点
        path.moveTo(0, cSize);
        RectF rectF = new RectF(0, 0, diameter, diameter);
        path.arcTo(rectF, 180, 90);
        // 右上角圆角起点
        path.lineTo(mWidth - cSize, 0);
        rectF = new RectF(mWidth - diameter, 0, mWidth, diameter);
        path.arcTo(rectF, -90, 90);
        // 右下角圆角起点
        path.lineTo(mWidth, mHeight - cSize);
        rectF = new RectF(mWidth - diameter, mHeight - diameter, mWidth, mHeight);
        path.arcTo(rectF, 0, 90);
        // 左下角圆角起点
        path.lineTo(cSize, mHeight);
        rectF = new RectF(0, mHeight - diameter, diameter, mHeight);
        path.arcTo(rectF, 90, 90);
        path.close();
        return path;
    }

    // 上左位
    private Path gotTopLeftArrowPath() {
        Path path = new Path();
        // 箭头高度 + 圆角高度
        path.moveTo(0, aHeight + cSize);
        int diameter = cSize * 2;
        // 左上角圆角
        RectF rectF = new RectF(0, aHeight, diameter, aHeight + diameter);
        path.arcTo(rectF, 180, 90);
        // 到箭头起始点
        path.lineTo(aMargin, aHeight);
        // 箭头顶点
        path.lineTo(aMargin + aWidth / 2, 0);
        // 箭头结束
        path.lineTo(aMargin + aWidth, aHeight);

        // 右上角半圆起始点
        path.lineTo(mWidth - cSize, aHeight);
        rectF = new RectF(mWidth - diameter, aHeight, mWidth, aHeight + diameter);
        // 右上角半圆
        path.arcTo(rectF, 270, 90);
        // 右下角半圆起点
        path.lineTo(mWidth, mHeight - cSize);
        rectF = new RectF(mWidth - diameter, mHeight - diameter, mWidth, mHeight);
        path.arcTo(rectF, 0, 90);
        // 左下角圆角起点
        path.lineTo(cSize, mHeight);
        rectF = new RectF(0, mHeight - diameter, diameter, mHeight);
        path.arcTo(rectF, 90, 90);
        // path 结束
        path.close();
        return path;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        // 透明
        canvas.drawColor(Color.TRANSPARENT);
        Path path = gotPath();
        canvas.drawPath(path, mFillPaint);
        if (bSize > 0) {
            // 绘制边框
            canvas.drawPath(path, mStrokePaint);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getIntrinsicHeight() {
        return mHeight;
    }

    @Override
    public void setAlpha(int alpha) {
        mFillPaint.setAlpha(alpha);
        mStrokePaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mFillPaint.setColorFilter(colorFilter);
        mStrokePaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        // 需要半透明
        return PixelFormat.TRANSLUCENT;
    }

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
     * 箭头在UI的右侧上边
     */
    public static final int RIGHT_TOP = 4;
    /**
     * 箭头在UI的右侧中间
     */
    public static final int RIGHT_MIDDLE = 5;
    /**
     * 箭头在UI的右侧下边
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
     * 箭头在UI的左侧下边
     */
    public static final int LEFT_BOTTOM = 10;
    /**
     * 箭头在UI的左侧中间
     */
    public static final int LEFT_MIDDLE = 11;
    /**
     * 箭头在UI的左侧上边
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
}
