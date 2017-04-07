// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.hlk.hlklib.lib.wheelpicker;

final class OnItemSelectedRunnable implements Runnable {

    private final WheelPicker wheelPicker;

    OnItemSelectedRunnable(WheelPicker picker) {
        wheelPicker = picker;
    }

    @Override
    public final void run() {
        wheelPicker.onItemSelectedListener.onItemSelected(wheelPicker.getSelectedItem());
    }
}
