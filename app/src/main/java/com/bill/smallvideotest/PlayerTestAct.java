package com.bill.smallvideotest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bill.baseplayer.player.VideoView;

public class PlayerTestAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_test);

        VideoView videoView = findViewById(R.id.video_player);
        videoView.setUrl("https://rmrbtest-image.peopleapp.com/upload/video/201809/1537349021125fcfb438615c1b.mp4");
        videoView.start();

    }
}