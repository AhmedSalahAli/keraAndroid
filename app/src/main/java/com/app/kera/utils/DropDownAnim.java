package com.app.kera.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class DropDownAnim extends Animation {
    private final int sourceHeight;
    private final int targetHeight;
    private final View view;
    private final boolean down;

    public DropDownAnim(View view, int sourceHeight, int targetHeight, boolean down) {
        this.view = view;
        this.sourceHeight = sourceHeight;
        this.targetHeight = targetHeight;
        this.down = down;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int newHeight;
        if (down) {
            newHeight = sourceHeight + (int) (targetHeight * interpolatedTime);
        } else {
            newHeight = sourceHeight + targetHeight - (int) (targetHeight * interpolatedTime);//(int) (targetHeight * (1 - interpolatedTime));
        }
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
        view.setVisibility(down ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}