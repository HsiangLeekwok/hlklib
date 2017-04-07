package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.Utility;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <b>功能描述：</b>自定义画图的背景图<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2016/12/29 08:17 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2016/12/29 08:17 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class CustomBezierCurveView extends View {

    /**
     * 曲线的高度
     */
    private int bezier_size = 0, bezier_side = 3;

    public CustomBezierCurveView(Context context) {
        this(context, null);
    }

    public CustomBezierCurveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomBezierCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);// 初始化背景属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomBezierCurveView, defStyleAttr, 0);
        getAttributes(a);
        a.recycle();
    }

    private void getAttributes(TypedArray array) {
        bezier_size = array.getDimensionPixelSize(R.styleable.CustomBezierCurveView_cdv_bezier_curve_size, Utility.ConvertDp(20));
        bezier_side = array.getInteger(R.styleable.CustomBezierCurveView_cdv_bezier_curve_side, BEZIER_SIZE_NONE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        customDraw(canvas);
    }

    // 自定义绘制
    private void customDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Path path = gotPath(width, height);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int[] colors = new int[]{
                ContextCompat.getColor(getContext(), R.color.hlklib_bezier_curve_background_start),
                ContextCompat.getColor(getContext(), R.color.hlklib_bezier_curve_background_center),
                ContextCompat.getColor(getContext(), R.color.hlklib_bezier_curve_background_end)
        };
        Shader shader = new LinearGradient(0, height, width, 0, colors, null, Shader.TileMode.MIRROR);
        paint.setShader(shader);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, paint);
    }

    private Path gotPath(int width, int height) {
        switch (bezier_side) {
            case BEZIER_SIDE_LEFT:
                return gotLeftBezierPath(width, height);
            case BEZIER_SIDE_TOP:
                return gotTopBezierPath(width, height);
            case BEZIER_SIDE_RIGHT:
                return gotRightBezierPath(width, height);
            case BEZIER_SIDE_BOTTOM:
                return gotBottomBezierPath(width, height);
            default:
                return gotNoneBezierPath(width, height);
        }
    }

    private Path gotNoneBezierPath(int width, int height) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
        return path;
    }

    private Path gotLeftBezierPath(int width, int height) {
        Path path = new Path();
        path.moveTo(width, 0);
        path.lineTo(width, height);
        path.lineTo(bezier_size, height);
        path.quadTo(0, height / 2, bezier_size, 0);
        path.close();
        return path;
    }

    private Path gotTopBezierPath(int width, int height) {
        Path path = new Path();
        path.moveTo(0, height);
        path.lineTo(width, height);
        path.lineTo(width, bezier_size);
        path.quadTo(width / 2, 0, 0, bezier_size);
        path.close();
        return path;
    }

    private Path gotRightBezierPath(int width, int height) {
        Path path = new Path();
        path.moveTo(0, height);
        path.lineTo(0, 0);
        path.lineTo(width - bezier_size, 0);
        path.quadTo(width, height / 2, width - bezier_size, height);
        path.close();
        return path;
    }

    private Path gotBottomBezierPath(int width, int height) {
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width, height - bezier_size);
        path.quadTo(width / 2, height, 0, height - bezier_size);
        path.close();
        return path;
    }

    /**
     * 曲线部分在View的位置
     */
    @IntDef({BEZIER_SIZE_NONE, BEZIER_SIDE_LEFT, BEZIER_SIDE_TOP, BEZIER_SIDE_RIGHT, BEZIER_SIDE_BOTTOM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BezierSide {
    }

    /**
     * 曲线所在View的位置：无
     */
    public static final int BEZIER_SIZE_NONE = 0;
    /**
     * 曲线所在View的位置：左侧
     */
    public static final int BEZIER_SIDE_LEFT = 1;
    /**
     * 曲线所在View的位置：顶部
     */
    public static final int BEZIER_SIDE_TOP = 2;
    /**
     * 曲线所在View的位置：右侧
     */
    public static final int BEZIER_SIDE_RIGHT = 3;
    /**
     * 曲线所在View的位置：底部
     */
    public static final int BEZIER_SIDE_BOTTOM = 4;
}
