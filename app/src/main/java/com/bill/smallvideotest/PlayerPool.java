package com.bill.smallvideotest;

import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * author ywb
 * date 2021/11/5
 * desc
 */
public class PlayerPool {

    private static class SingletonHolder {
        private static PlayerPool instance = new PlayerPool();
    }

    public static PlayerPool getInstance() {
        return SingletonHolder.instance;
    }

    private PlayerPool() {
    }

    private MediaPlayer mMediaPlayer;

    public MediaPlayer getPlayer() {
        createPlayer();
        return mMediaPlayer;
    }

    private void createPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
    }
}
