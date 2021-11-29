package com.bill.smallvideotest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handleDetail(View view) {
        Intent intent = new Intent(this, SmallVideoDetailAct.class);
        startActivity(intent);
    }

    public void handleRV(View view) {
        Intent intent = new Intent(this, SmallVideoListAct.class);
        intent.putExtra("type", "rv");
        startActivity(intent);
    }

    public void handleVP(View view) {
//        Intent intent = new Intent(this, SmallVideoListAct.class);
//        intent.putExtra("type", "vp");
//        startActivity(intent);
    }

    public void handleSplitScreen(View view) {
        Intent intent = new Intent(this, SplitScreenActivity.class);
        startActivity(intent);
    }

    public void handleCache(View view) {
        Intent intent = new Intent(this, CacheTestActivity.class);
        startActivity(intent);
    }
}