// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.hlk.hlklib.lib.wheelpicker;

import android.os.Handler;
import android.os.Message;

final class MessageHandler extends Handler {

    public static final int WHAT_INVALIDATE_LOOP_VIEW = 1000;
    public static final int WHAT_SMOOTH_SCROLL = 2000;
    public static final int WHAT_ITEM_SELECTED = 3000;

    private final WheelPicker picker;

    MessageHandler(WheelPicker picker) {
        this.picker = picker;
    }

    @Override
    public final void handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_INVALIDATE_LOOP_VIEW:
                picker.invalidate();
                break;

            case WHAT_SMOOTH_SCROLL:
                picker.smoothScroll(WheelPicker.ACTION.FLING);
                break;

            case WHAT_ITEM_SELECTED:
                picker.onItemSelected();
                break;
        }
    }

}
