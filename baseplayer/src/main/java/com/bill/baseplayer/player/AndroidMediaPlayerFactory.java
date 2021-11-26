package com.bill.baseplayer.player;

import android.content.Context;

/**
 * author ywb
 * date 2021/11/24
 * desc
 */
public class AndroidMediaPlayerFactory extends PlayerFactory {

    public static AndroidMediaPlayerFactory create() {
        return new AndroidMediaPlayerFactory();
    }

    @Override
    public AndroidMediaPlayer createPlayer(Context context) {
        return new AndroidMediaPlayer(context);
    }
}