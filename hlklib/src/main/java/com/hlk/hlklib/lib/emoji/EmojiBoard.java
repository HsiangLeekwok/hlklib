package com.hlk.hlklib.lib.emoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hlk.hlklib.R;
import com.hlk.hlklib.etc.Utility;
import com.hlk.hlklib.lib.view.CenteredImageSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <b>功能描述：</b>提供Emoji分页控制<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/04/28 07:57 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/04/28 07:57 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class EmojiBoard extends LinearLayout {

    public EmojiBoard(Context context) {
        this(context, null);
    }

    public EmojiBoard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeAttributes(context, attrs);
    }

    private void initializeAttributes(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EmojiBoard);
        try {
            mPageLines = a.getInteger(R.styleable.EmojiBoard_emojiPageLines, 4);
            mLineSize = a.getInteger(R.styleable.EmojiBoard_emojiLineSize, 7);
            mEmojiPadding = a.getDimensionPixelOffset(R.styleable.EmojiBoard_emojiItemPadding, 0);
            if (0 >= mEmojiPadding) {
                mEmojiPadding = context.getResources().getDimensionPixelOffset(R.dimen.hlklib_emoji_layout_default_padding);
            }
        } finally {
            a.recycle();
        }
        initializeLayout();
    }

    /**
     * 每页的行数
     */
    private int mPageLines;
    /**
     * 每行的emoji个数
     */
    private int mLineSize;
    /**
     * 每个emoji的边框距离
     */
    private int mEmojiPadding;

    private ViewPager emojiPager;
    private LinearLayout pageIndicator;

    // 初始化layout布局
    private void initializeLayout() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.hlklib_emoji_layout, this);
        emojiPager = (ViewPager) view.findViewById(R.id.hlklib_emoji_pages);
        pageIndicator = (LinearLayout) view.findViewById(R.id.hlklib_emoji_page_indicator);
        initializeEmoji();
    }

    private int screenWidth() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    private void resetUiHeight(int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (null == params) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        } else {
            params.height = height;
        }
        setLayoutParams(params);
    }

    // 分页指示器列表
    private List<ImageView> mIndicator = new ArrayList<>();

    private void initializeEmoji() {
//        if (pageIndicator.getChildCount() > 0) {
//            return;
//        }
        // 整个UI的padding
        int uiPadding = getContext().getResources().getDimensionPixelOffset(R.dimen.hlklib_emoji_layout_default_padding);
        int perEmojiSize = (screenWidth() - uiPadding * 2) / mLineSize;
        int emojiPageHeight = perEmojiSize * mPageLines;
        // 重新设置 viewPager 的高度
        resetUiHeight(emojiPageHeight);

        // 每页emoji的个数
        int pageSize = mPageLines * mLineSize;
        int dotSize = Utility.ConvertDp(5);

        List<View> pages = new ArrayList<>();
        // 表情列表如果为空则需要初始化
        if (null == EmojiUtility.EmojiList) {
            EmojiUtility.initEmojiItems(getContext());
        }

        for (int i = 0, len = EmojiUtility.EmojiList.size(); i < len; i++) {
            int last = (i + pageSize) >= len ? (len) : (i + pageSize);
            List<EmojiItem> list = EmojiUtility.EmojiList.subList(i, last);
            i += pageSize - 1;
            GridView pager = (GridView) View.inflate(getContext(), R.layout.hlklib_emoji_page_layout, null);
            pager.setColumnWidth(perEmojiSize);
            EmojiPageAdapter adapter = new EmojiPageAdapter(list);
            pager.setAdapter(adapter);
            pager.setOnItemClickListener(mEmojiOnItemClickListener);
            pages.add(pager);
            // 加入页指示器
            ImageView dot = new ImageView(getContext());
            dot.setLayoutParams(new ViewGroup.LayoutParams(dotSize, dotSize));
            dot.setImageResource(R.drawable.hlklib_emoji_indicator);
            dot.setEnabled(false);
            pageIndicator.addView(dot);
            mIndicator.add(dot);
        }
        EmojiPagerAdapter mEmojiPagerAdapter = new EmojiPagerAdapter(pages);
        emojiPager.setAdapter(mEmojiPagerAdapter);
        emojiPager.addOnPageChangeListener(mOnPageChangeListener);
        // 默认在第一页
        changeScrolledDot(0);
    }

    /**
     * 每页Emoji的GridView适配器
     */
    private class EmojiPageAdapter extends BaseAdapter {
        private List<EmojiItem> list;

        EmojiPageAdapter(List<EmojiItem> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = View.inflate(getContext(), R.layout.hlklib_emoji_item, null);
                if (mEmojiPadding > 0) {
                    convertView.setPadding(mEmojiPadding, mEmojiPadding, mEmojiPadding, mEmojiPadding);
                }
            }
            EmojiItem item = list.get(position);
            ImageView view = (ImageView) convertView;
            view.setImageResource(item.getResId());
            view.setTag(item.getId());
            return view;
        }
    }

    private AdapterView.OnItemClickListener mEmojiOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int eId = (Integer) view.getTag();
            EmojiItem item = EmojiUtility.getEmojiItem(eId);
            if (null != item) {
                Drawable drawable = getContext().getResources().getDrawable(item.getResId());
                int size = EmojiUtility.emojiSize;
                if (size > 0) {
                    drawable.setBounds(0, 0, size, size);
                } else {
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                }
                CenteredImageSpan imageSpan = new CenteredImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
                String emojiDesc = item.getDesc();
                SpannableString spannableString = new SpannableString(emojiDesc);
                spannableString.setSpan(imageSpan, 0, emojiDesc.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (null != _mOnEmojiSelectedListener) {
                    _mOnEmojiSelectedListener.onSelected(spannableString);
                }
            }
        }
    };

    /**
     * 改变当前选中项的点
     */
    private void changeScrolledDot(int position) {
        for (int i = 0, len = mIndicator.size(); i < len; i++) {
            mIndicator.get(i).setEnabled(i == position);
        }
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            changeScrolledDot(position);
        }
    };

    /**
     * 表情分页适配器
     */
    private class EmojiPagerAdapter extends PagerAdapter {
        private List<View> mList;

        EmojiPagerAdapter(List<View> list) {
            super();
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    private OnEmojiSelectedListener _mOnEmojiSelectedListener;

    /**
     * 添加表情被选中的事件监听
     */
    public void setOnEmojiSelectedListener(OnEmojiSelectedListener l) {
        _mOnEmojiSelectedListener = l;
    }

    /**
     * Emoji被选中事件的监听接口
     */
    public interface OnEmojiSelectedListener {
        /**
         * Emoji被选中的事件回调处理
         */
        void onSelected(SpannableString emoji);
    }
}
