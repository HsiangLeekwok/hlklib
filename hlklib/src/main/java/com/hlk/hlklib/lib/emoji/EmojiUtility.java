package com.hlk.hlklib.lib.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.hlk.hlklib.R;
import com.hlk.hlklib.lib.view.CenteredImageSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者：Hsiang Leekwok on 2015/11/09 21:54<br />
 * 邮箱：xiang.l.g@gmail.com<br />
 */
public class EmojiUtility {

    // 表情图标名称的正则表达式
    private static final String FACE_REGULAR = "\\[(.*?)\\]";// "[\\[\u4e00-\u9fa5\\]]+";
    public static List<EmojiItem> EmojiList = null;
    static int emojiSize = -1;

    public static void initEmojiItems(Context context) {

        EmojiList = new ArrayList<>();

        String[] emojidesc = context.getResources().getStringArray(R.array.hlklib_emoji_items);
        for (int i = 1; i <= 85; i++) {
            String emjId = "face" + String.format(Locale.getDefault(), "%1$,03d", i);
            int id = context.getResources().getIdentifier(emjId, "drawable", context.getPackageName());
            EmojiItem item = new EmojiItem();
            item.setDesc(emojidesc[i - 1]);
            item.setId(i);
            item.setResId(id);
            EmojiList.add(item);
        }
    }

    /**
     * 设置正常文字大小以便emoji调整大小
     */
    public static void setDefaultTextSize(int textSize) {
        emojiSize = textSize;
    }

    /**
     * 通过表情id查找表情节点
     */
    public static EmojiItem getEmojiItem(int emojiId) {
        for (EmojiItem item : EmojiList) {
            if (item.getId() == emojiId)
                return item;
        }
        return null;
    }

    /**
     * 通过表情的描述查找表情节点
     */
    private static EmojiItem getEmojiItem(String desc) {
        for (EmojiItem item : EmojiList) {
            if (item.getDesc().equals(desc))
                return item;
        }
        return null;
    }

    /**
     * 得到一个SpanableString对象。通过传入的字符串进行正则判断，将其中的表情符号转换成表情图片
     *
     * @param context     context
     * @param string      original text
     * @param adjustEmoji 是否将表情图片缩放成文字大小
     * @return SpannableStringBuilder
     */
    public static SpannableString getEmojiString(Context context, String string, boolean adjustEmoji) {
        if (null == EmojiList) {
            initEmojiItems(context);
        }
        // 转换 Html，去掉两个表情之间的多个空格
        Spanned spanned = Html.fromHtml(string.replace("&nbsp;&nbsp;", ""));
        SpannableString spannableString = new SpannableString(spanned);
        // 通过传入的正则表达式来生成一个pattern
        Pattern emojiPatten = Pattern.compile(FACE_REGULAR, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, emojiPatten, 0, adjustEmoji);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spannableString;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context         context
     * @param spannableString htmlstring
     * @param patten          patten
     * @param start           start index
     * @param adjustEmoji     是否缩放表情图
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws NumberFormatException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private static void dealExpression(Context context, SpannableString spannableString, Pattern patten, int start, boolean adjustEmoji)
            throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            // 通过上面匹配得到的字符串来生成图片资源id
            EmojiItem item = getEmojiItem(key);
            if (null != item) {
                // 计算该图片名字的长度，也就是要替换的字符串的长度
                int end = matcher.start() + key.length();
                // 通过图片资源id来得到图像，用一个ImageSpan来包装
                // 设置图片的大小为系统默认文字大小，以便文字图片对齐
                Drawable drawable = context.getResources().getDrawable(item.getResId());
                if (adjustEmoji && emojiSize > 0) {
                    drawable.setBounds(0, 0, emojiSize, emojiSize);
                } else {
                    // 不缩放时用默认的宽高
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                }
                CenteredImageSpan imageSpan = new CenteredImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
                // 将该图片替换字符串中规定的位置中
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    // 如果整个字符串还未验证完，则继续递归查询替换。。
                    dealExpression(context, spannableString, patten, end, adjustEmoji);
                }
                break;
            }
        }
    }
}
