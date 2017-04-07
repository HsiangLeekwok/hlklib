// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.hlk.hlklib.lib.wheelpicker;

import android.view.MotionEvent;

final class WheelPickerGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    private final WheelPicker wheelPicker;

    WheelPickerGestureListener(WheelPicker picker) {
        wheelPicker = picker;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        wheelPicker.scrollBy(velocityY);
        return true;
    }
}
