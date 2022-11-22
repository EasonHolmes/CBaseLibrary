package com.ethan.clibrary.widget;

import android.graphics.Outline;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewOutlineProvider;

public class SurfaceViewOutlineProvider extends ViewOutlineProvider {

    private float mRadius;

    public SurfaceViewOutlineProvider(float radius) {
        this.mRadius = radius;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int leftMargin = 0;
        int topMargin = 0;
        Rect selfRect = new Rect(leftMargin, topMargin,
                rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
        //这里还可以使用setRect()矩形  setOval()圆形
        outline.setRoundRect(selfRect, mRadius);
//        outline.setOval(selfRect);
    }
}