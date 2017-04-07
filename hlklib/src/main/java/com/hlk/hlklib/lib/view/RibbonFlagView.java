package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.Utility;

/**
 * <b>功能：</b>带斜角标的图片<br />
 * <b>作者：</b>Hsiang Leekwok <br />
 * <b>时间：</b>2016/05/09 08:40 <br />
 * <b>邮箱：</b>xiang.l.g@gmail.com <br />
 */
public class RibbonFlagView extends RelativeLayout {

    // UI
    //@ViewId(R.id.hlklib_ribbon_flag_content)
    private RelativeLayout content;
    //@ViewId(R.id.hlklib_ribbon_flag_text)
    private TextView flag;

    // parameters
    private float density;
    private String text;
    private int position, textSize, textBackground, contentView;
    private ColorStateList textColor;


    public RibbonFlagView(Context context) {
        this(context, null);
    }

    public RibbonFlagView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RibbonFlagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        density = context.getResources().getDisplayMetrics().density;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RibbonFlagView, defStyleAttr, 0);
        initFromAttributes(a);
        a.recycle();
        initFromLayout();
    }

    private void initFromAttributes(TypedArray array) {
        position = array.getInt(R.styleable.RibbonFlagView_ri_flag_position, Default.POSITION);
        text = array.getString(R.styleable.RibbonFlagView_ri_flag_text);

        textSize = array.getDimensionPixelOffset(R.styleable.RibbonFlagView_ri_flag_text_size, Default.TEXT_SIZE);
        if (textSize > 0) {
            textSize = (int) (textSize / density);
        }
        textColor = array.getColorStateList(R.styleable.RibbonFlagView_ri_flag_text_color);
        if (null == textColor) {
            textColor = ColorStateList.valueOf(Default.TEXT_COLOR);
        }
        textBackground = array.getResourceId(R.styleable.RibbonFlagView_ri_flag_backgrond, 0);
        contentView = array.getResourceId(R.styleable.RibbonFlagView_ri_flag_content, 0);
    }

    private void initFromLayout() {
        View view = View.inflate(getContext(), R.layout.hlklib_ribbon_flag, this);
        //ViewUtility.bind(this, view);
        content = (RelativeLayout) view.findViewById(R.id.hlklib_ribbon_flag_content);
        flag = (TextView) view.findViewById(R.id.hlklib_ribbon_flag_text);
        setTextParameters();
    }

    private void setTextParameters() {
        if (TextUtils.isEmpty(text)) {
            position = 0;
        }
        flag.setVisibility(position == 0 ? GONE : VISIBLE);
        if (position == 0) return;

        flag.setText(text);
        flag.setTextColor(textColor);
        if (textBackground == 0) {
            flag.setBackgroundColor(Default.TEXT_BACKGROUND);
        } else {
            flag.setBackgroundResource(textBackground);
        }
        float size = textSize * density;
        int padding = (int) (size * 1.5);
        flag.setPadding(padding, 0, padding, 0);
        int margin = padding / 2;
        float rotation = position % 2 == 1 ? -45 : 45;
        flag.setRotation(rotation);
        flag.setTextSize(textSize);
        boolean left = position == 1 || position == 4;
        boolean top = position == 1 || position == 2;
        LayoutParams param = (LayoutParams) flag.getLayoutParams();
        param.leftMargin = left ? -padding : 0;
        param.topMargin = position <= 2 ? (int) size : padding;
        param.rightMargin = !left ? -padding : 0;
        param.bottomMargin = !top ? margin : 0;
        param.addRule(RelativeLayout.ALIGN_LEFT, left ? R.id.hlklib_ribbon_flag_content : RelativeLayout.NO_ID);
        param.addRule(RelativeLayout.ALIGN_TOP, top ? R.id.hlklib_ribbon_flag_content : RelativeLayout.NO_ID);
        param.addRule(RelativeLayout.ALIGN_RIGHT, !left ? R.id.hlklib_ribbon_flag_content : RelativeLayout.NO_ID);
        param.addRule(RelativeLayout.ALIGN_BOTTOM, !top ? R.id.hlklib_ribbon_flag_content : RelativeLayout.NO_ID);
        flag.setLayoutParams(param);
    }

    private void removeParent(View view) {
        ViewParent parent = view.getParent();
        if (null != parent) {
            ((ViewGroup) parent).removeAllViews();
        }
    }

    private void addContentView() {
        if (0 != contentView) {
            View view = View.inflate(getContext(), contentView, null);
            content.addView(view);
        }
    }

    /**
     * 设置内容View
     */
    public void addContentView(int resId) {
        contentView = resId;
        addContentView();
    }

    /**
     * 设置内容View
     */
    public void addContentView(View view) {
        removeParent(view);
        content.addView(view);
    }

    /**
     * 无标签
     */
    public static final int NONE = 0;
    /**
     * 标签位于内容布局的左上角
     */
    public static final int TOP_LEFT = 1;
    /**
     * 标签位于内容布局的右上角
     */
    public static final int TOP_RIGHT = 2;
    /**
     * 标签位于内容布局的右下角
     */
    public static final int BOTTOM_RIGHT = 3;
    /**
     * 标签位于内容布局的左下角
     */
    public static final int BOTTOM_LEFT = 4;

    public void setPosition(int position) {
        if (position >= NONE && position <= BOTTOM_LEFT) {
            this.position = position;
            setTextParameters();
        } else {
            throw new IllegalArgumentException("Not supported flag position value \"" + String.valueOf(position) + "\"");
        }
    }

    public void setFlagTextSize(int textSize) {
        this.textSize = textSize;
        setTextParameters();
    }

    /**
     * 设置标签文字
     */
    public void setFlagText(String text) {
        this.text = text;
        setTextParameters();
    }

    /**
     * 设置标签文字
     */
    public void setFlagText(int text) {
        this.text = getContext().getString(text);
        setTextParameters();
    }

    /**
     * 设置标签颜色
     */
    public void setFlagColor(int color) {
        flag.setTextColor(color);
    }

    /**
     * 设置标签背景颜色
     */
    public void setFlagBackground(int color) {
        flag.setBackgroundColor(color);
    }

    /**
     * 设置标签背景颜色
     */
    public void setFlagBackgroundResources(int resId) {
        flag.setBackgroundResource(resId);
    }

    private static class Default {
        public static final String TEXT = "Text";
        /**
         * 默认没有标签
         */
        public static final int POSITION = 0;
        /**
         * 默认标签上的字体大小
         */
        public static final int TEXT_SIZE = Utility.ConvertDp(10);
        public static final int TEXT_COLOR = Color.WHITE;
        public static final int TEXT_BACKGROUND = Color.parseColor("#fe8802");
    }
}
