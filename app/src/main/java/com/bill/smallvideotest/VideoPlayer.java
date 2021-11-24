package com.bill.smallvideotest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.bill.smallvideotest.player.AbstractPlayer;
import com.bill.smallvideotest.player.AndroidMediaPlayerFactory;
import com.bill.smallvideotest.player.IjkPlayerFactory;

/**
 * author ywb
 * date 2021/10/20
 * desc VideoView
 */
public class VideoPlayer extends RelativeLayout {

    private static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;

    private static final int PLAYER_STATE_UPDATE_INTERVAL_TIME = 500;

    private static final int STATE_NOT_START = 100;
    private static final int STATE_START = 101;
    private static final int STATE_PAUSE = 102;
    private static final int STATE_STOP = 103;
    private static final int STATE_FORCE_PAUSE = 104;

    private Activity mActivity;
    private GlobalListener mListener;

    private View mPlayerContainer;
    private ProgressBar mPlayerProgressBar;
    private AppCompatImageView mPlayBtn;
    private AppCompatImageView mThumbIv;
    private VideoTextureView mTextureView; // 渲染器使用TextureView
    private Surface mSurface;
    private AbstractPlayer mMediaPlayer; // 解码器

    private int mPlayerState = STATE_NOT_START;
    private PlayerStateSyncThread mPlayerStateSyncThread;
    private String mPath;
    private int mListPosition; // 列表位置，为了更好地预加载
    private boolean mListReverseScroll; // 列表滑动方向，为了更好地预加载
    private long mCurrentProgress = 0;
    private long mDuration = 0;

    private int mRepeatNum = 1; // 重复播放次数

    private OnPlayListener mPlayListener;

    public VideoPlayer(Context context) {
        this(context, null);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
        initListener();
        configViews();
    }

    public void setPlayListener(OnPlayListener mPlayListener) {
        this.mPlayListener = mPlayListener;
    }

    public void reset() {
        mRepeatNum = 1;
        mCurrentProgress = mDuration = 0;
        setProgress(0);
    }

    /**
     * 获取缩略图
     **/
    public AppCompatImageView getThumbIv() {
        return mThumbIv;
    }

    /**
     * 是否正在播放
     **/
    public boolean isPlaying() {
        try {
            if (mMediaPlayer != null)
                return mMediaPlayer.isPlaying();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置路径并播放
     **/
    public void setVideoPath(String path) {
        setVideoPath(path, -1, false);
    }

    /**
     * 设置路径并播放
     **/
    public void setVideoPath(String path, int position, boolean isReverseScroll) {
        this.mPath = path;
        this.mListPosition = position;
        this.mListReverseScroll = isReverseScroll;
        play();
    }

    /**
     * 播放
     **/
    public void play() {
        boolean isHasNet = true;
        if (!isHasNet) {
            // No NetWork
            return;
        }

        Log.e("VideoPlayer", "play = " + mPath);

        if (TextUtils.isEmpty(mPath)) {
            return;
        }

        if (mPlayerState == STATE_START) {
            return;
        }

        mPlayerState = STATE_START;
        if (mMediaPlayer != null) {
            restart();
            setScreenOn(true);
            syncPlayerState(true);
        } else {
            load();
        }
        mPlayBtn.setVisibility(GONE);
    }

    public void restart() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暂停
     **/
    public void pause() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.pause();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            mPlayerState = STATE_PAUSE;
            syncPlayerState(false);
            setScreenOn(false);
        }
    }

    /**
     * 销毁
     **/
    public void release() {
        Log.e("VideoPlayer", "release = " + mPath);

        if (mMediaPlayer != null) {
            reset();
            mMediaPlayer.setSurface(null);
            mMediaPlayer.release();
            mMediaPlayer = null;
            mPlayerState = STATE_STOP;
            syncPlayerState(false);
            setScreenOn(false);
        }
    }

    private long getCurrentProgress() {
        if (mMediaPlayer == null)
            return 0;
        int position = 0;
        try {
            position = (int) mMediaPlayer.getCurrentPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return position;
    }

    private long getDuration() {
        if (mMediaPlayer == null) {
            return 0;
        }
        int duration = 0;
        try {
            if (mMediaPlayer == null) return 0;
            duration = (int) mMediaPlayer.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duration;
    }

    private void initViews() {
        mActivity = (Activity) getContext();
        LayoutInflater.from(mActivity).inflate(R.layout.video_player, this);
        mPlayerContainer = this.findViewById(R.id.player_container);
        mPlayerProgressBar = this.findViewById(R.id.player_bottom_progress);
        mPlayBtn = this.findViewById(R.id.player_play_btn);
        mThumbIv = this.findViewById(R.id.player_cover);
        mTextureView = this.findViewById(R.id.player_content);
        mTextureView.setAspectRatio(ScaleType.AR_ASPECT_FILL_PARENT);
    }

    private void initListener() {
        mListener = new GlobalListener() {
            @Override
            public void onError() {
//                Log.e("VideoPlayer", "onError (" + what + "," + extra + ")");
                pause();
                release();
            }

            @Override
            public void onCompletion() {
                setVideoPlayCompletion();
            }

            @Override
            public void onInfo(int what, int extra) {
                if (what == MEDIA_INFO_VIDEO_RENDERING_START) {
                    mThumbIv.setVisibility(GONE);
                }
            }

            @Override
            public void onPrepared() {
                setScreenOn(true);
                syncPlayerState(true);

                if (mPlayListener != null) {
                    mPlayListener.prepareFinish(mListPosition, mListReverseScroll);
                }
            }

            @Override
            public void onVideoSizeChanged(int width, int height) {
                if (mTextureView != null)
                    mTextureView.setVideoSize(width, height);
            }

            @Override
            public void onBufferingUpdate(int percent) {
                setLoadingProgress(percent);
            }

            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                mSurface = new Surface(surface);
                if (mMediaPlayer != null) {
                    mMediaPlayer.setSurface(mSurface);
                    if (mPlayerState == STATE_FORCE_PAUSE) {
                        play();
                    }
                }
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying()) {
                        mPlayerState = STATE_FORCE_PAUSE;
                        mMediaPlayer.pause();
                    }
                    mMediaPlayer.setSurface(null);
                }
                surface.release();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

            }

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.player_container) {
                    handlePlayButtonClick();
                }
            }
        };
    }

    private void configViews() {
        setBackgroundColor(Color.BLACK);
        mTextureView.setSurfaceTextureListener(mListener);
        mPlayerContainer.setOnClickListener(mListener);
    }

    private void handlePlayButtonClick() {
        if (mPlayerState != STATE_START) {
            play();
            mPlayBtn.setVisibility(GONE);
        } else {
            pause();
            mPlayBtn.setVisibility(VISIBLE);
        }
    }

    private void load() {
        createPlayer();
        mMediaPlayer.setDataSource(mPath);
        if (mSurface != null) {
            mMediaPlayer.setSurface(mSurface);
//            mMediaPlayer.setScreenOnWhilePlaying(true);
        }
        try {
            mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void createPlayer() {
        if (mMediaPlayer != null) {
            try {
                mMediaPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            mMediaPlayer.setSurface(null);
            mMediaPlayer.release();
        }

        mMediaPlayer = IjkPlayerFactory.create().createPlayer();
        mMediaPlayer.setPlayerEventListener(mListener);
        mMediaPlayer.initPlayer();
    }

    private void setScreenOn(boolean status) {
        if (status)
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void setVideoPlayCompletion() {
        if (mPlayerState == STATE_STOP)
            return;
        restart();
        mRepeatNum++;
        if (mPlayListener != null) {
            mPlayListener.repeatNum(mRepeatNum);
        }
    }

    private void setProgress(int progress) {
        if (mPlayerProgressBar != null)
            mPlayerProgressBar.setProgress(progress);
    }

    private void setLoadingProgress(int progress) {
        mPlayerProgressBar.setSecondaryProgress(progress);
    }

    private void syncPlayerState(boolean flag) {
        if (mPlayerStateSyncThread != null && mPlayerStateSyncThread.isAlive())
            mPlayerStateSyncThread.stopRun();

        if (flag) {
            mPlayerStateSyncThread = new PlayerStateSyncThread();
            mPlayerStateSyncThread.start();
        }
    }

    // 处理进度
    private class PlayerStateSyncThread extends Thread {
        private boolean isKeepUpdatingPlayerPosition = true;

        @Override
        public void run() {
            super.run();
            while (isKeepUpdatingPlayerPosition) {
                try {
                    sleep(PLAYER_STATE_UPDATE_INTERVAL_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mPlayerState == STATE_START || mPlayerState == STATE_PAUSE) {
                    if (mMediaPlayer != null) {
                        mCurrentProgress = getCurrentProgress();
                        mDuration = getDuration();
                        int progress = (int) (mCurrentProgress * 100 / (mDuration == 0 ? 1 : mDuration));
                        VideoPlayer.this.post(() -> {
                            setProgress(progress);
                        });
                    }
                }

            }
        }

        private void stopRun() {
            isKeepUpdatingPlayerPosition = false;
        }
    }

    private abstract static class GlobalListener
            implements OnClickListener,
            TextureView.SurfaceTextureListener,
            AbstractPlayer.PlayerEventListener {
    }

    public interface OnPlayListener {
        void repeatNum(int repeatNum);

        void prepareFinish(int position, boolean isReverseScroll);
    }

}
