package com.bill.baseplayer.render;

import android.content.Context;

/**
 * author ywb
 * date 2021/11/24
 * desc
 */
public class TextureRenderViewFactory extends RenderViewFactory{

    public static TextureRenderViewFactory create() {
        return new TextureRenderViewFactory();
    }

    @Override
    public IRenderView createRenderView(Context context) {
        return new TextureRenderView(context);
    }
}
