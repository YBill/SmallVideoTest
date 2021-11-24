package com.bill.smallvideotest;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.bill.smallvideotest.cache.PreloadManager;
import com.bill.smallvideotest.cache.VideoCacheManager;
import com.bumptech.glide.Glide;
import com.danikula.videocache.CacheListener;

import java.io.File;
import java.io.IOException;

public class CacheTestActivity extends AppCompatActivity {

    //    private final String PATH = "https://rmrbtest-image.peopleapp.com/upload/video/201809/1537349021125fcfb438615c1b.mp4";
//    private final String PATH = "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/25617215fb4f0f49a41a44141c67e7f9.mp4";
//    private final String PATH = "https://v26-web.douyinvod.com/ea696ca7517498b358bd2c2a513400b7/619623b6/video/tos/cn/tos-cn-ve-15-alinc2/810af64904de47c086a89bc7bfac1c93/?a=6383&br=2324&bt=2324&cd=0%7C0%7C0&ch=5&cr=3&cs=0&cv=1&dr=0&ds=3&er=&ft=OyFYlOZZI0J.17Tz7Th9D8FxuhsdrhhcHqY&l=202111181657430102100651650B097E40&lr=all&mime_type=video_mp4&net=0&pl=0&qs=0&rc=amU1cmc6ZjxsOTMzNGkzM0ApOjNmaWY2Mzw7Nzk3M2RkO2dtMjNhcjRnajBgLS1kLS9zczVeNjYtMzYvXmAwL2JjMzY6Yw%3D%3D&vl=&vr=";

    private final String PATH = "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/video_v4/38fa92f8dae47b1645aa35d7bb72e682.mp4";
//    private final String PATH = "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/test/test.mp4";


    private AppCompatImageView coverIv;
    private VideoTextureView mTextureView;
    private Surface mSurface;

    private MediaPlayer mPlayer;

    private long startPlayTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_test);
        coverIv = findViewById(R.id.iv_cover);
        mTextureView = findViewById(R.id.render_view);
        showCover(coverIv, PATH);

        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                mSurface = new Surface(surface);
            }

            @Override
            public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

            }
        });

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setLooping(true);
        mPlayer.setOnPreparedListener(mp -> {
            Log.d("Bill", "prepare = " + mp);
            mp.start();

            long time = System.currentTimeMillis() - startPlayTime;
            Log.i("Bill", "播放用时：" + time + "s");
        });
        mPlayer.setOnVideoSizeChangedListener((mp, width, height) -> {
            mTextureView.setVideoSize(width, height);
        });
        mPlayer.setOnInfoListener((mp, what, extra) -> {
            if (what == 3) {
                coverIv.setVisibility(View.GONE);
            }
            return false;
        });
        mPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e("Bill", "onError (" + what + "," + extra + ")");
            return false;
        });

        VideoCacheManager.getInstance().getProxy().registerCacheListener(listener, PATH);
    }

    private void showCover(AppCompatImageView imageView, String url) {
        Glide.with(this).load(url).into(imageView);
    }

    private CacheListener listener = new CacheListener() {
        @Override
        public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
            Log.d("Bill", "percentsAvailable = " + percentsAvailable);
        }
    };

    public void handlePlay(View view) {
        startPlayTime = System.currentTimeMillis();
        try {
            String proxyUrl = PreloadManager.getInstance().getPlayUrl(PATH);
            Log.e("Bill", "path = " + proxyUrl);
            mPlayer.setDataSource(proxyUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepareAsync();
        mPlayer.setSurface(mSurface);
    }

    public void handleStop(View view) {
        mPlayer.stop();
        mPlayer.reset();
    }

    public void handleLoad(View view) {
//        boolean fullyCached = mProxy.isCached(PATH);
//        Log.e("Bill", "fullyCached = " + fullyCached);

        PreloadManager.getInstance().addPreloadTask(PATH, 0, true);
    }

    public void handleClear(View view) {
        boolean result = VideoCacheManager.getInstance().clearAllCache();
        Log.e("Bill", "result = " + result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.setSurface(null);
            mPlayer.release();
            mPlayer = null;
        }
        VideoCacheManager.getInstance().getProxy().unregisterCacheListener(listener);
    }
}