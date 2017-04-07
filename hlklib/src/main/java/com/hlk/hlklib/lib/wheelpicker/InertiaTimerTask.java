// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.hlk.hlklib.lib.wheelpicker;

import java.util.TimerTask;

final class InertiaTimerTask extends TimerTask {

    private float a;
    private final float velocityY;
    private final WheelPicker wheelPicker;

    InertiaTimerTask(WheelPicker picker, float velocityY) {
        super();
        wheelPicker = picker;
        this.velocityY = velocityY;
        a = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        if (a == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                if (velocityY > 0.0F) {
                    a = 2000F;
                } else {
                    a = -2000F;
                }
            } else {
                a = velocityY;
            }
        }
        if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
            wheelPicker.cancelFuture();
            wheelPicker.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int i = (int) ((a * 10F) / 1000F);
        WheelPicker wheelPicker = this.wheelPicker;
        wheelPicker.totalScrollY = wheelPicker.totalScrollY - i;
        if (!this.wheelPicker.isLoop) {
            float itemHeight = this.wheelPicker.lineSpacingMultiplier * this.wheelPicker.maxTextHeight;
            if (this.wheelPicker.totalScrollY <= (int) ((float) (-this.wheelPicker.initPosition) * itemHeight)) {
                a = 40F;
                this.wheelPicker.totalScrollY = (int) ((float) (-this.wheelPicker.initPosition) * itemHeight);
            } else if (this.wheelPicker.totalScrollY >= (int) ((float) (this.wheelPicker.items.size() - 1 - this.wheelPicker.initPosition) * itemHeight)) {
                this.wheelPicker.totalScrollY = (int) ((float) (this.wheelPicker.items.size() - 1 - this.wheelPicker.initPosition) * itemHeight);
                a = -40F;
            }
        }
        if (a < 0.0F) {
            a = a + 20F;
        } else {
            a = a - 20F;
        }
        this.wheelPicker.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }
}
