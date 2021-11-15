package com.bill.smallvideotest;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

import com.bill.smallvideotest.cache.PreloadManager;
import com.bill.smallvideotest.cache.ProxyVideoCacheManager;
import com.bumptech.glide.Glide;
import com.danikula.videocache.CacheListener;

import java.io.File;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

public class CacheTestActivity extends AppCompatActivity {

    private final String PATH = "https://rmrbtest-image.peopleapp.com/upload/video/201809/1537349021125fcfb438615c1b.mp4";


    private AppCompatImageView coverIv;
    private VideoTextureView mTextureView;
    private Surface mSurface;

    private MediaPlayer mPlayer;

//    private HttpProxyCacheServer mProxy;

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

//        mProxy = MyApplication.getProxy(this);
//        mProxy.registerCacheListener(listener, PATH);

        ProxyVideoCacheManager.getProxy(this).registerCacheListener(listener, PATH);
    }

    private void showCover(AppCompatImageView imageView, String url) {
        Glide.with(this).load(url).into(imageView);
    }

    CacheListener listener = new CacheListener() {
        @Override
        public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
//            Log.d("Bill", "cacheFile = " + cacheFile.getAbsolutePath());
//            Log.d("Bill", "url = " + url);
            Log.d("Bill", "percentsAvailable = " + percentsAvailable);
        }
    };

    public void handlePlay(View view) {
        try {
            String proxyUrl = PreloadManager.getInstance(this).getPlayUrl(PATH);

//            String proxyUrl = mProxy.getProxyUrl(PATH);
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
//        String proxyUrl = mProxy.getProxyUrl(PATH);
//        Log.e("Bill", "path = " + proxyUrl);

        PreloadManager.getInstance(this).addPreloadTask(PATH, 0);
    }

    public void handleClear(View view) {
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
//        mProxy.unregisterCacheListener(listener);
        ProxyVideoCacheManager.getProxy(this).unregisterCacheListener(listener);
    }
}