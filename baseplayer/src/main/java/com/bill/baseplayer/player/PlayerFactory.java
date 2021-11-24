package com.bill.baseplayer.player;

/**
 * author ywb
 * date 2021/11/24
 * desc
 */
public abstract class PlayerFactory<P extends AbstractPlayer> {

    public abstract P createPlayer();

}