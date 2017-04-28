package com.hlk.hlklib.lib.emoji;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import com.hlk.hlklib.lib.view.CorneredEditText;

/**
 * <b>功能描述：</b>从剪切板复制黏贴表情时自动转换表情图的EditText<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/04/28 09:35 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/04/28 09:35 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class EmojiClipboardCatchableEditText extends CorneredEditText {

    public EmojiClipboardCatchableEditText(Context context) {
        super(context);
    }

    public EmojiClipboardCatchableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmojiClipboardCatchableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (null == text) {
            super.setText(null, type);
        } else if (text instanceof SpannableString) {
            super.setText(text, type);
        } else {
            String s = text.toString();
            SpannableString spannableString = EmojiUtility.getEmojiString(getContext(), s, true);
            super.setText(spannableString, type);
        }
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id) {
            case android.R.id.paste:
                // 复制过来的文字也需要转换表情图
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                String value = clipboard.getText().toString();
                SpannableString pastedText = EmojiUtility.getEmojiString(getContext(), value, true);
                Editable edit = getEditableText();
                int cursorSelect = getSelectionStart();
                edit.insert(cursorSelect, pastedText);
                return true;
        }
        return super.onTextContextMenuItem(id);
    }
}
