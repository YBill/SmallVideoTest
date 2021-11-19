package com.bill.smallvideotest;

import android.os.Bundle;

public class SmallVideoListAct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(getWindow(), true);
        setContentView(R.layout.activity_small_video_list);

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

}