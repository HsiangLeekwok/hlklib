package com.hlk.hlklib.lib.view;

import android.content.Context;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import com.hlk.hlklib.etc.TypefaceHelper;

/**
 * Created by demi on 2016/12/23.
 */

public class CustomRadioButton extends AppCompatRadioButton {
    public CustomRadioButton(Context context) {
        super(context);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {

            setTypeface(TypefaceHelper.get(context, "fonts/iconfont.ttf"));

        }
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (!isInEditMode()) {

            setTypeface(TypefaceHelper.get(context, "fonts/iconfont.ttf"));

        }
    }


}
