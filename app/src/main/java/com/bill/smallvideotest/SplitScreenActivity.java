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

import com.bumptech.glide.Glide;

import java.io.IOException;

public class SplitScreenActivity extends AppCompatActivity {

    private final String URL1 = "http://kw-static.cognizepower.com/content/mp4/5b29f6fc54be8cea508c921769e00f8c.mp4";
    private final String URL2 = "http://kw-static.cognizepower.com/content/mp4/126049f559f703e28aadaa62d060ca12.mp4";

    private final String IMG_URL_1 = "http://kw-static.cognizepower.com/content/jpg/5b29f6fc54be8cea508c921769e00f8c.jpg";
    private final String IMG_URL_2 = "http://kw-static.cognizepower.com/content/jpg/126049f559f703e28aadaa62d060ca12.jpg";

    private VideoTextureView mTextureView1;
    private VideoTextureView mTextureView2;
    private Surface mSurface1;
    private Surface mSurface2;

    private AppCompatImageView coverIv1;
    private AppCompatImageView coverIv2;
    private AppCompatImageView currentPlayIv;

    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_screen);

        coverIv1 = findViewById(R.id.iv_cover_1);
        coverIv2 = findViewById(R.id.iv_cover_2);
        showCover(coverIv1, IMG_URL_1);
        showCover(coverIv2, IMG_URL_2);

        mTextureView1 = findViewById(R.id.render_view_1);
        mTextureView2 = findViewById(R.id.render_view_2);
        mTextureView1.setAspectRatio(ScaleType.AR_ASPECT_FIT_PARENT);
        mTextureView2.setAspectRatio(ScaleType.AR_ASPECT_FIT_PARENT);
        mTextureView1.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                mSurface1 = new Surface(surface);
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
        mTextureView2.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                mSurface2 = new Surface(surface);
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

    }

    private void showCover(AppCompatImageView imageView, String url) {
        Glide.with(this).load(url).into(imageView);
    }

    private MediaPlayer getPlayer() {
        MediaPlayer player = PlayerManager.getInstance().getAvailableMediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setLooping(true);
        player.setOnPreparedListener(mp -> {
            Log.d("Bill", "prepare = " + mp);
            mp.start();
        });
        player.setOnVideoSizeChangedListener((mp, width, height) -> {
            mTextureView1.setVideoSize(width, height);
            mTextureView2.setVideoSize(width, height);
        });
        player.setOnInfoListener((mp, what, extra) -> {
            if (what == 3 && currentPlayIv != null) {
                currentPlayIv.setVisibility(View.GONE);
            }
            return false;
        });
        player.setOnErrorListener((mp, what, extra) -> {
            Log.e("Bill", "onError (" + what + "," + extra + ")");
            return false;
        });
        return player;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.setSurface(null);
        }
        PlayerManager.getInstance().recycle();
    }

    public void handleItem1(View view) {
        currentPlayIv = coverIv1;
        showCover(coverIv2, IMG_URL_2);
        mPlayer = getPlayer();

        try {
            mPlayer.setDataSource(URL1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepareAsync();
        mPlayer.setSurface(mSurface1);
    }

    public void handleItem2(View view) {
        currentPlayIv = coverIv2;
        showCover(coverIv1, IMG_URL_1);
        mPlayer = getPlayer();

        try {
            mPlayer.setDataSource(URL2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepareAsync();
        mPlayer.setSurface(mSurface2);
    }
}