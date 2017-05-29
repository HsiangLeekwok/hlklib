package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>功能描述：</b>九宫格图片控件<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/05/25 21:47 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/05/25 21:47 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class NineRectangleGridImageView<T> extends ViewGroup {

    private int mRowCount;//行数
    private int mColumnCount;//列数

    private int mMaxSize = 9;
    private int mSpacing;//宫格间距

    private int mWidth;//当前组件宽度
    private int mHeight;//当前组件高度

    private List<ImageView> mImageViewList = new ArrayList<>();
    private List<T> mImgDataList;

    private NineRectangleGridImageViewAdapter<T> mAdapter;

    public NineRectangleGridImageView(Context context) {
        this(context, null);
    }

    public NineRectangleGridImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineRectangleGridImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineRectangleGridImageView);
        this.mSpacing = (int) typedArray.getDimension(R.styleable.NineRectangleGridImageView_imageSpacing, Utility.ConvertDp(2));
        typedArray.recycle();
    }

    /**
     * 设置控件的宽高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = measureWidth(widthMeasureSpec);
        mHeight = measureHeight(heightMeasureSpec);

        setMeasuredDimension(mWidth, mHeight);
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 200;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 200;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildrenView();
    }

    /**
     * 为子ImageView布局
     */
    private void layoutChildrenView() {
        if (mImgDataList == null) {
            return;
        }
        int childrenCount = mImgDataList.size();
        for (int i = 0; i < childrenCount; i++) {
            ImageView childrenView = (ImageView) getChildAt(i);
            if (mAdapter != null) {
                mAdapter.onDisplayImage(getContext(), childrenView, mImgDataList.get(i));
            }
            int rowNum = i / mColumnCount;//当前行数
            int columnNum = i % mColumnCount;//当前列数

            int mImageSize = (mWidth - (mColumnCount + 1) * mSpacing) / mColumnCount;//图片心坟

            int t_center = (mHeight + mSpacing) / 2;//中间置以下的顶点（有宫格间距）
            int b_center = (mHeight - mSpacing) / 2;//中间位置以上的底部（有宫格间距）
            int l_center = (mWidth + mSpacing) / 2;//中间位置以右的左部（有宫格间距）
            int r_center = (mWidth - mSpacing) / 2;//中间位置以左的右部（有宫格间距）
            int center = (mHeight - mImageSize) / 2;//中间位置以上顶部（无宫格间距）

            int left = mImageSize * columnNum + mSpacing * (columnNum + 1);
            int top = mImageSize * rowNum + mSpacing * (rowNum + 1);
            int right = left + mImageSize;
            int bottom = top + mImageSize;

            /*
              不同子view情况下的不同显示
             */
            if (childrenCount == 1) {
                childrenView.layout(left, top, right, bottom);
            } else if (childrenCount == 2) {
                childrenView.layout(left, center, right, center + mImageSize);
            } else if (childrenCount == 3) {
                if (i == 0) {
                    childrenView.layout(center, top, center + mImageSize, bottom);
                } else {
                    childrenView.layout(mSpacing * i + mImageSize * (i - 1), t_center, mSpacing * i + mImageSize * i, t_center + mImageSize);
                }
            } else if (childrenCount == 4) {
                childrenView.layout(left, top, right, bottom);
            } else if (childrenCount == 5) {
                if (i == 0) {
                    childrenView.layout(r_center - mImageSize, r_center - mImageSize, r_center, r_center);
                } else if (i == 1) {
                    childrenView.layout(l_center, r_center - mImageSize, l_center + mImageSize, r_center);
                } else {
                    childrenView.layout(mSpacing * (i - 1) + mImageSize * (i - 2), t_center, mSpacing * (i - 1) + mImageSize * (i - 1), t_center + mImageSize);
                }
            } else if (childrenCount == 6) {
                if (i < 3) {
                    childrenView.layout(mSpacing * (i + 1) + mImageSize * i, b_center - mImageSize, mSpacing * (i + 1) + mImageSize * (i + 1), b_center);
                } else {
                    childrenView.layout(mSpacing * (i - 2) + mImageSize * (i - 3), t_center, mSpacing * (i - 2) + mImageSize * (i - 2), t_center + mImageSize);
                }
            } else if (childrenCount == 7) {
                if (i == 0) {
                    childrenView.layout(center, mSpacing, center + mImageSize, mSpacing + mImageSize);
                } else if (i > 0 && i < 4) {
                    childrenView.layout(mSpacing * i + mImageSize * (i - 1), center, mSpacing * i + mImageSize * i, center + mImageSize);
                } else {
                    childrenView.layout(mSpacing * (i - 3) + mImageSize * (i - 4), t_center + mImageSize / 2, mSpacing * (i - 3) + mImageSize * (i - 3), t_center + mImageSize / 2 + mImageSize);
                }
            } else if (childrenCount == 8) {
                if (i == 0) {
                    childrenView.layout(r_center - mImageSize, mSpacing, r_center, mSpacing + mImageSize);
                } else if (i == 1) {
                    childrenView.layout(l_center, mSpacing, l_center + mImageSize, mSpacing + mImageSize);
                } else if (i > 1 && i < 5) {
                    childrenView.layout(mSpacing * (i - 1) + mImageSize * (i - 2), center, mSpacing * (i - 1) + mImageSize * (i - 1), center + mImageSize);
                } else {
                    childrenView.layout(mSpacing * (i - 4) + mImageSize * (i - 5), t_center + mImageSize / 2, mSpacing * (i - 4) + mImageSize * (i - 4), t_center + mImageSize / 2 + mImageSize);
                }
            } else if (childrenCount == 9) {
                childrenView.layout(left, top, right, bottom);
            }
        }
    }

    /**
     * 设置图片数据
     *
     * @param data 图片数据集合
     */
    public void setImagesData(List data) {
        if (data == null || data.isEmpty()) {
            this.setVisibility(GONE);
            return;
        } else {
            this.setVisibility(VISIBLE);
        }

        if (mMaxSize > 0 && data.size() > mMaxSize) {
            data = data.subList(0, mMaxSize);
        }

        int[] gridParam = calculateGridParam(data.size());
        mRowCount = gridParam[0];
        mColumnCount = gridParam[1];
        if (mImgDataList == null) {
            int i = 0;
            while (i < data.size()) {
                ImageView iv = getImageView(i);
                if (iv == null)
                    return;
                addView(iv, generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldViewCount = mImgDataList.size();
            int newViewCount = data.size();
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = oldViewCount; i < newViewCount; i++) {
                    ImageView iv = getImageView(i);
                    if (iv == null) {
                        return;
                    }
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        mImgDataList = data;
        requestLayout();
    }

    /**
     * 设置适配器
     *
     * @param adapter 适配器
     */
    public void setAdapter(NineRectangleGridImageViewAdapter<T> adapter) {
        mAdapter = adapter;
    }

    /**
     * 设置宫格间距
     *
     * @param spacing 宫格间距 px
     */
    public void setSpacing(int spacing) {
        mSpacing = spacing;
    }

    /**
     * 设置宫格参数
     *
     * @param imagesSize 图片数量
     * @return 宫格参数 gridParam[0] 宫格行数 gridParam[1] 宫格列数
     */
    private static int[] calculateGridParam(int imagesSize) {
        int[] gridParam = new int[2];
        if (imagesSize < 3) {
            gridParam[0] = 1;
            gridParam[1] = imagesSize;
        } else if (imagesSize <= 4) {
            gridParam[0] = 2;
            gridParam[1] = 2;
        } else {
            gridParam[0] = imagesSize / 3 + (imagesSize % 3 == 0 ? 0 : 1);
            gridParam[1] = 3;
        }
        return gridParam;
    }

    /**
     * 获得 ImageView
     * 保证了ImageView的重用
     *
     * @param position 位置
     */
    private ImageView getImageView(final int position) {
        if (position < mImageViewList.size()) {
            return mImageViewList.get(position);

        } else {
            if (mAdapter != null) {
                ImageView imageView = mAdapter.generateImageView(getContext());
                mImageViewList.add(imageView);
                return imageView;
            } else {
                Log.e("9GridImageView", "你必须为LQRNineGridImageView设置LQRNineGridImageViewAdapter");
                return null;
            }
        }
    }

    /**
     * 九宫格图片适配器
     */
    public static abstract class NineRectangleGridImageViewAdapter<T> {
        /**
         * 重写该方法，使用任意第三方图片加载工具加载图片
         */
        protected abstract void onDisplayImage(Context context, ImageView imageView, T t);

        /**
         * 重写该方法自定义生成ImageView方式，用于九宫格头像中的一个个图片控件，可以设置ScaleType等属性
         */
        protected ImageView generateImageView(Context context) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }
    }
}
