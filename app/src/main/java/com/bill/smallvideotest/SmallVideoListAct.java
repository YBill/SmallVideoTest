package com.bill.smallvideotest;

import android.os.Bundle;

public class SmallVideoListAct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(getWindow(), true);
        setContentView(R.layout.activity_small_video_list);

        if (savedInstanceState == null) {
            SmallVideoFragment fragment = new SmallVideoFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, SmallVideoFragment.TAG).commit();
        }

    }

}