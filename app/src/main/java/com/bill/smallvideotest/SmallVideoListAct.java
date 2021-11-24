package com.bill.smallvideotest;

import android.os.Bundle;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class SmallVideoListAct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(getWindow(), true);
        setContentView(R.layout.activity_small_video_list);

//        IjkMediaPlayer.loadLibrariesOnce(null);
//        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        String type = getIntent().getStringExtra("type");

        if (savedInstanceState == null) {
            if ("vp".equals(type)) {
                SmallVideoFragment2 fragment = new SmallVideoFragment2();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment, SmallVideoFragment2.TAG).commit();
            } else {
                SmallVideoFragment fragment = new SmallVideoFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment, SmallVideoFragment.TAG).commit();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        IjkMediaPlayer.native_profileEnd();
    }
}