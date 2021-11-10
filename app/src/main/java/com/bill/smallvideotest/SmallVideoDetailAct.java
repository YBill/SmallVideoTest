package com.bill.smallvideotest;

import android.os.Bundle;

public class SmallVideoDetailAct extends BaseActivity {

    private VideoPlayer mVideoPlayer;

    private final String PATH = "http://kw-static.cognizepower.com/content/mp4/1ec0ef434243d4df0d89b4f481db2301.mp4";
//    private final String PATH = "https://rmrbtest-image.peopleapp.com/upload/video/201809/1537349021125fcfb438615c1b.mp4";
//    private final String PATH = "https://interactive-wallpaper-1252921383.cos.ap-beijing.myqcloud.com/online-earning/answer/video/298f98634080ad3e30d34f9561f5d82d.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(getWindow(), true);
        setContentView(R.layout.activity_small_video_detail);
        initViews();
    }

    private void initViews() {
        mVideoPlayer = findViewById(R.id.video_player);
        mVideoPlayer.setVideoPath(PATH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoPlayer.release();
    }
}