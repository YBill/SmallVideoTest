package com.bill.baseplayer.player;

/**
 * author ywb
 * date 2021/11/24
 * desc
 */
public class AndroidMediaPlayerFactory extends PlayerFactory<AndroidMediaPlayer> {

    public static AndroidMediaPlayerFactory create() {
        return new AndroidMediaPlayerFactory();
    }

    @Override
    public AndroidMediaPlayer createPlayer() {
        return new AndroidMediaPlayer();
    }
}