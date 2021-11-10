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

    public void handleList(View view) {
        Intent intent = new Intent(this, SmallVideoListAct.class);
        startActivity(intent);
    }
}