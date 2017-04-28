package com.hlk.hlklib.lib.voice;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hlk.hlklib.R;
import com.hlk.hlklib.lib.inject.ViewUtility;

/**
 * 作者：Hsiang Leekwok on 2015/11/09 12:35<br />
 * 邮箱：xiang.l.g@gmail.com<br />
 */
public class VoiceRecorderDialog {

    private Context context;
    private Dialog dialog;

    // UI
    private ImageView mImageView;
    private TextView mWarningText;

    // Volume
    private int[] volumes;

    public VoiceRecorderDialog(Context context) {
        this.context = context;
        volumes = new int[]{
                R.drawable.hlklib_record_animate_01,
                R.drawable.hlklib_record_animate_02,
                R.drawable.hlklib_record_animate_03,
                R.drawable.hlklib_record_animate_04,
                R.drawable.hlklib_record_animate_05,
                R.drawable.hlklib_record_animate_06,
                R.drawable.hlklib_record_animate_07,
                R.drawable.hlklib_record_animate_08,
                R.drawable.hlklib_record_animate_09,
                R.drawable.hlklib_record_animate_10,
                R.drawable.hlklib_record_animate_11,
                R.drawable.hlklib_record_animate_12,
                R.drawable.hlklib_record_animate_13,
                R.drawable.hlklib_record_animate_14
        };
    }

    /**
     * 显示录音对话框
     */
    public void show() {
        if (null == dialog) {
            dialog = new Dialog(context, R.style.HLKLIB_Voice_Recorder_Dialog);
            View view = View.inflate(context, R.layout.hlklib_voice_recorder_dialog, null);
            ViewUtility.bind(this, view);
            mImageView = (ImageView) view.findViewById(R.id.hlklib_voice_recorder_image);
            mWarningText = (TextView) view.findViewById(R.id.hlklib_voice_recorder_warning);
            dialog.setContentView(view);
            dialog.setOnDismissListener(dismissListener);
        }
        dialog.show();
    }

    private DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if (null != outListener) {
                outListener.onDismiss(dialog);
            }
        }
    };

    private DialogInterface.OnDismissListener outListener;

    /**
     * 添加dialog dismiss监听
     */
    public void addOnDismissListener(DialogInterface.OnDismissListener l) {
        outListener = l;
    }

    /**
     * 关闭录音对话框
     */
    public void dismiss() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 开始录制
     */
    public void recording() {
        if (null != dialog && dialog.isShowing()) {
            mImageView.setImageResource(R.drawable.hlklib_record_animate_01);
            mWarningText.setText(R.string.hlklib_voice_recorder_warning_text_cancel);
        }
    }

    public void wannaCancel() {
        if (null != dialog && dialog.isShowing()) {
            mImageView.setImageResource(R.drawable.hlklib_record_cancel);
            mWarningText.setText(R.string.hlklib_voice_recorder_warning_text_wanna_cancel);
        }
    }

    /**
     * 录音时间太短
     */
    public void recordedTooShort() {
        if (null != dialog && dialog.isShowing()) {
            mImageView.setImageResource(R.drawable.hlklib_record_too_short);
            mWarningText.setText(R.string.hlklib_voice_recorder_warning_text_too_short);
        }
    }

    /**
     * 录音时间太长
     */
    public void recordedTooLong() {
        if (null != dialog && dialog.isShowing()) {
            mImageView.setImageResource(R.drawable.hlklib_record_too_short);
            mWarningText.setText(R.string.hlklib_voice_recorder_warning_text_too_long);
        }
    }

    /**
     * 更新音量大小
     */
    public void updateVolume(int volumeValue) {
        if (null != dialog && dialog.isShowing()) {
            int index = volumeValue / 600;
            if (index >= volumes.length - 1) {
                index = volumes.length - 1;
            }
            mImageView.setImageResource(volumes[index]);
        }
    }
}
