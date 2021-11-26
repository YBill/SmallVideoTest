package com.bill.baseplayer.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Map;

/**
 * author ywb
 * date 2021/11/23
 * desc
 */
public class AndroidMediaPlayer extends AbstractPlayer implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener {

    private Context mContext;
    protected MediaPlayer mMediaPlayer;
    private int mBufferedPercent;
    private boolean mIsPreparing;

    public AndroidMediaPlayer(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void initPlayer() {
        mMediaPlayer = new MediaPlayer();
        setOptions();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnErrorListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnVideoSizeChangedListener(this);
    }

    @Override
    public void setOptions() {

    }

    private boolean isAvailable() {
        return mMediaPlayer != null;
    }

    @Override
    public void setDataSource(String path, Map<String, String> headers) {
        if (!isAvailable())
            return;
        try {
            mMediaPlayer.setDataSource(mContext, Uri.parse(path), headers);
        } catch (Exception e) {
            if (mPlayerEventListener != null)
                mPlayerEventListener.onError();
        }
    }

    @Override
    public void setDataSource(AssetFileDescriptor fd) {
        if (!isAvailable())
            return;
        try {
            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
        } catch (Exception e) {
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
            mIsPreparing = true;
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
        stop();
        mMediaPlayer.reset();
        mMediaPlayer.setSurface(null);
        mMediaPlayer.setDisplay(null);
        mMediaPlayer.setVolume(1, 1);
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
            mMediaPlayer.seekTo((int) time);
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
        stop();
        MediaPlayer tmpMediaPlayer = mMediaPlayer;
        new PlayerReleaseThread(tmpMediaPlayer).start();
        mMediaPlayer = null;
    }


    // 销毁播放器
    private static class PlayerReleaseThread extends Thread {
        private final MediaPlayer mReleaseMediaPlayer;

        PlayerReleaseThread(MediaPlayer releaseMediaPlayer) {
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
        // only support above Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mMediaPlayer.setPlaybackParams(mMediaPlayer.getPlaybackParams().setSpeed(speed));
            } catch (Exception e) {
                if (mPlayerEventListener != null)
                    mPlayerEventListener.onError();
            }
        }
    }

    @Override
    public float getSpeed() {
        // only support above Android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (isAvailable())
                    return mMediaPlayer.getPlaybackParams().getSpeed();
            } catch (Exception e) {
                if (mPlayerEventListener != null)
                    mPlayerEventListener.onError();
            }
        }
        return 0;
    }

    @Override
    public long getTcpSpeed() {
        // no support
        return 0;
    }

    //////////

    @Override
    public void onBufferingUpdate(MediaPlayer iMediaPlayer, int percent) {
        mBufferedPercent = percent;
    }

    @Override
    public void onCompletion(MediaPlayer iMediaPlayer) {
        if (mPlayerEventListener != null)
            mPlayerEventListener.onCompletion();
    }

    @Override
    public boolean onError(MediaPlayer iMediaPlayer, int what, int extra) {
        if (mPlayerEventListener != null)
            mPlayerEventListener.onError();
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        //解决MEDIA_INFO_VIDEO_RENDERING_START多次回调问题
        if (what == AbstractPlayer.MEDIA_INFO_RENDERING_START) {
            if (mIsPreparing) {
                mPlayerEventListener.onInfo(what, extra);
                mIsPreparing = false;
            }
        } else {
            mPlayerEventListener.onInfo(what, extra);
        }
        return true;
    }

    @Override
    public void onPrepared(MediaPlayer iMediaPlayer) {
        start();
        if (mPlayerEventListener != null)
            mPlayerEventListener.onPrepared();
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        if (videoWidth != 0 && videoHeight != 0) {
            if (mPlayerEventListener != null)
                mPlayerEventListener.onVideoSizeChanged(videoWidth, videoHeight);
        }
    }

}