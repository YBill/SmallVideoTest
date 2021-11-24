package com.bill.smallvideotest.player;

import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * author ywb
 * date 2021/11/23
 * desc
 */
public class IjkPlayer extends AbstractPlayer implements IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnCompletionListener, IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnVideoSizeChangedListener, IjkMediaPlayer.OnNativeInvokeListener {

    private IjkMediaPlayer mMediaPlayer;
    private int mBufferedPercent;

    @Override
    public void initPlayer() {
        mMediaPlayer = new IjkMediaPlayer();
        setOptions();
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        mMediaPlayer.setOnNativeInvokeListener(this);
    }

    @Override
    public void setOptions() {

    }

    private boolean isAvailable() {
        return mMediaPlayer != null;
    }

    @Override
    public void setDataSource(String path) {
        if (!isAvailable())
            return;
        try {
            mMediaPlayer.setDataSource(path);
        } catch (Exception e) {
            e.printStackTrace();
            if (mPlayerEventListener != null)
                mPlayerEventListener.onError();
        }
    }

    @Override
    public void start() {
        if (!isAvailable())
            return;
        try {
            mMediaPlayer.start();
        } catch (IllegalStateException e) {
            if (mPlayerEventListener != null)
                mPlayerEventListener.onError();
        }
    }

    @Override
    public void pause() {
        if (!isAvailable())
            return;
        try {
            mMediaPlayer.pause();
        } catch (IllegalStateException e) {
            if (mPlayerEventListener != null)
                mPlayerEventListener.onError();
        }
    }

    @Override
    public void stop() {
        if (!isAvailable())
            return;
        try {
            mMediaPlayer.stop();
        } catch (IllegalStateException e) {
            if (mPlayerEventListener != null)
                mPlayerEventListener.onError();
        }
    }

    @Override
    public void prepareAsync() {
        if (!isAvailable())
            return;
        try {
            mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            if (mPlayerEventListener != null)
                mPlayerEventListener.onError();
        }
    }

    @Override
    public void reset() {
        if (!isAvailable())
            return;
        mMediaPlayer.reset();
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        setOptions();
    }

    @Override
    public boolean isPlaying() {
        if (isAvailable())
            return mMediaPlayer.isPlaying();
        return false;
    }

    @Override
    public void seekTo(long time) {
        if (!isAvailable())
            return;
        try {
            mMediaPlayer.seekTo(time);
        } catch (IllegalStateException e) {
            if (mPlayerEventListener != null)
                mPlayerEventListener.onError();
        }
    }

    @Override
    public void release() {
        if (!isAvailable())
            return;
        mMediaPlayer.setOnErrorListener(null);
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.setOnInfoListener(null);
        mMediaPlayer.setOnBufferingUpdateListener(null);
        mMediaPlayer.setOnPreparedListener(null);
        mMediaPlayer.setOnVideoSizeChangedListener(null);
        IjkMediaPlayer tmpMediaPlayer = mMediaPlayer;
        new PlayerReleaseThread(tmpMediaPlayer).start();
        mMediaPlayer = null;
    }

    // 销毁播放器
    private static class PlayerReleaseThread extends Thread {
        private final IjkMediaPlayer mReleaseMediaPlayer;

        PlayerReleaseThread(IjkMediaPlayer releaseMediaPlayer) {
            this.mReleaseMediaPlayer = releaseMediaPlayer;
        }

        @Override
        public void run() {
            super.run();
            if (mReleaseMediaPlayer != null)
                mReleaseMediaPlayer.release();
        }
    }

    @Override
    public long getCurrentPosition() {
        if (isAvailable())
            return mMediaPlayer.getCurrentPosition();
        return 0;
    }

    @Override
    public long getDuration() {
        if (isAvailable())
            return mMediaPlayer.getDuration();
        return 0;
    }

    @Override
    public int getBufferedPercentage() {
        return mBufferedPercent;
    }

    @Override
    public void setSurface(Surface surface) {
        if (!isAvailable())
            return;
        mMediaPlayer.setSurface(surface);
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        if (!isAvailable())
            return;
        mMediaPlayer.setDisplay(holder);
    }

    @Override
    public void setVolume(float v1, float v2) {
        if (!isAvailable())
            return;
        mMediaPlayer.setVolume(v1, v2);
    }

    @Override
    public void setLooping(boolean isLooping) {
        if (!isAvailable())
            return;
        mMediaPlayer.setLooping(isLooping);
    }

    @Override
    public void setSpeed(float speed) {
        if (!isAvailable())
            return;
        mMediaPlayer.setSpeed(speed);
    }

    @Override
    public float getSpeed() {
        if (isAvailable())
            return mMediaPlayer.getSpeed(0);
        return 0;
    }

    //////////

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
        mBufferedPercent = percent;
        if (mPlayerEventListener != null)
            mPlayerEventListener.onBufferingUpdate(percent);
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (mPlayerEventListener != null)
            mPlayerEventListener.onCompletion();
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        if (mPlayerEventListener != null)
            mPlayerEventListener.onError();
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer mp, int what, int extra) {
        if (mPlayerEventListener != null)
            mPlayerEventListener.onInfo(what, extra);
        return true;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (mPlayerEventListener != null)
            mPlayerEventListener.onPrepared();
    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int sar_num, int sar_den) {
        int videoWidth = iMediaPlayer.getVideoWidth();
        int videoHeight = iMediaPlayer.getVideoHeight();
        if (videoWidth != 0 && videoHeight != 0) {
            if (mPlayerEventListener != null)
                mPlayerEventListener.onVideoSizeChanged(videoWidth, videoHeight);
        }
    }

    @Override
    public boolean onNativeInvoke(int i, Bundle bundle) {
        return true;
    }
}
