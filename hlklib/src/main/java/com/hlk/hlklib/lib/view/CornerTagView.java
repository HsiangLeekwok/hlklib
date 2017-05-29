package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.Utility;

/**
 * <b>功能描述：</b>边角标签<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/05/29 21:20 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/05/29 21:20 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class CornerTagView extends View {

    public CornerTagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerTagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CornerTagView, defStyleAttr, 0);
        initializeAttributes(a);
        a.recycle();
    }

    private int mSide, mCorner, mWidth, mHeight, mBackground;

    private void initializeAttributes(TypedArray array) {
        mSide = array.getInteger(R.styleable.CornerTagView_ctvCornerSide, 0);
        mCorner = array.getDimensionPixelOffset(R.styleable.CornerTagView_ctvCornerSize, 0);
        mWidth = array.getDimensionPixelOffset(R.styleable.CornerTagView_ctvWidth, Utility.ConvertDp(16));
        mHeight = array.getDimensionPixelOffset(R.styleable.CornerTagView_ctvHeight, Utility.ConvertDp(25));
        mBackground = array.getColor(R.styleable.CornerTagView_ctvBackground, Color.BLUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWidth > 0 && mHeight > 0) {
            customDraw(canvas);
        }
    }

    private void customDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(mBackground);
        Path path = gotPath();
        canvas.drawPath(path, paint);
    }

    private static final int LEFT_TOP = 0;
    private static final int RIGHT_TOP = 1;
    private static final int RIGHT_BOTTOM = 2;
    private static final int LEFT_BOTTOM = 3;

    private Path gotPath() {
        switch (mSide) {
            case LEFT_TOP:
                return leftTop();
            case RIGHT_TOP:
                return rightTop();
            case RIGHT_BOTTOM:
                return rightBottom();
            default:
                return leftBottom();
        }
    }

    private Path leftTop() {
        Path path = new Path();
        int diameter = mCorner * 2;
        path.moveTo(0, mHeight);
        path.lineTo(mWidth, 0);
        if (mCorner > 0) {
            path.lineTo(mCorner, 0);
            RectF rectF = new RectF(0, 0, diameter, diameter);
            path.arcTo(rectF, 270, -90);
        } else {
            path.lineTo(0, 0);
        }
        path.close();
        return path;
    }

    private Path rightTop() {
        Path path = new Path();
        int diameter = mCorner * 2;
        path.moveTo(0, 0);
        if (mCorner > 0) {
            path.lineTo(mWidth - mCorner, 0);
            RectF rectF = new RectF(mWidth - diameter, 0, mWidth, diameter);
            path.arcTo(rectF, 270, 90);
            path.lineTo(mWidth, mHeight);
        } else {
            path.lineTo(mWidth, 0);
            path.lineTo(mWidth, mHeight);
        }
        path.close();
        return path;
    }

    private Path rightBottom() {
        Path path = new Path();
        int diameter = mCorner * 2;
        path.moveTo(mWidth, 0);
        if (mCorner > 0) {
            path.lineTo(mWidth, mHeight - mCorner);
            RectF rectF = new RectF(mWidth - diameter, mHeight - diameter, mWidth, mHeight);
            path.arcTo(rectF, 0, 90);
            path.lineTo(0, mHeight);
        } else {
            path.lineTo(mWidth, mHeight);
            path.lineTo(0, mHeight);
        }
        path.close();
        return path;
    }

    private Path leftBottom() {
        Path path = new Path();
        int diameter = mCorner * 2;
        path.moveTo(0, 0);
        path.lineTo(mWidth, mHeight);
        if (mCorner > 0) {
            path.lineTo(mCorner, mHeight);
            RectF rectF = new RectF(0, mHeight - diameter, diameter, mHeight);
            path.arcTo(rectF, 90, 90);
        } else {
            path.lineTo(0, mHeight);
        }
        path.close();
        return path;
    }
}
