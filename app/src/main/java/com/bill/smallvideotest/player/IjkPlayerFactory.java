package com.bill.smallvideotest.player;

/**
 * author ywb
 * date 2021/11/24
 * desc
 */
public class IjkPlayerFactory extends PlayerFactory<IjkPlayer> {

    public static IjkPlayerFactory create() {
        return new IjkPlayerFactory();
    }

    @Override
    public IjkPlayer createPlayer() {
        return new IjkPlayer();
    }
}
