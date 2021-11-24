package com.bill.baseplayer.player;

import android.content.Context;

/**
 * author ywb
 * date 2021/11/24
 * desc
 */
public abstract class PlayerFactory<P extends AbstractPlayer> {

    public abstract P createPlayer(Context context);

}