package com.bill.baseplayer.render;

import android.content.Context;

/**
 * author ywb
 * date 2021/11/24
 * desc
 */
public class SurfaceRenderViewFactory extends RenderViewFactory {

    public static SurfaceRenderViewFactory create() {
        return new SurfaceRenderViewFactory();
    }

    @Override
    public IRenderView createRenderView(Context context) {
        return new SurfaceRenderView(context);
    }
}