package com.hlk.hlklib.lib.voice;

import android.media.MediaPlayer;

/**
 * 声音文件播放工具类
 */
public class MediaPlayerManager {
    //播放音频API类：MediaPlayer
    private static MediaPlayer mMediaPlayer;
    //是否暂停
    private static boolean isPause;

    public static void setIsPause(boolean isPause) {
        MediaPlayerManager.isPause = isPause;
    }

    public static boolean isPause() {
        return isPause;
    }

    /**
     * @param filePath：文件路径 onCompletionListener：播放完成监听
     * @description 播放声音
     * @author ldm
     * @time 2016/6/25 11:30
     */
    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            //设置一个error监听器
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

                public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
                    mMediaPlayer.reset();
                    return false;
                }
            });
        } else {
            mMediaPlayer.reset();
        }

        try {
            mMediaPlayer.setAudioStreamType(android.media.AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(onCompletionListener);
            mMediaPlayer.setDataSource(filePath);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.prepareAsync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param
     * @description 暂停播放
     * @author ldm
     * @time 2016/6/25 11:31
     */
    public static void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) { //正在播放的时候
            mMediaPlayer.pause();
            isPause = true;
        }
    }

    /**
     * @param
     * @description 重新播放
     * @author ldm
     * @time 2016/6/25 11:31
     */
    public static void resume() {
        if (mMediaPlayer != null && isPause) {
            mMediaPlayer.start();
            isPause = false;
        }
    }

    /**
     * @param
     * @description 释放操作
     * @author ldm
     * @time 2016/6/25 11:32
     */
    public static void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
