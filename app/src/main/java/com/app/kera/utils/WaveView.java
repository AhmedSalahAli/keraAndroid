package com.app.kera.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * author：Administrator on 2017/3/15 11:29
 * description: file description
 * version: version
 */
public class WaveView extends View {
    private int mWidth = 0;
    private int mHeight = 0;
    private Path path;
    private Paint paint;
    private float waveHeight = 180; //peak
    private int waveWidth = 0; //wavelength
    private int originalY = 450;
    private int dx = 0;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Initialization data
     */
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (int) (getPaddingLeft() + getPaddingRight());
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = (int) (getPaddingTop() + getPaddingBottom());
        }
        mWidth = width;
        mHeight = height;
        originalY = height / 2;
        waveWidth = mWidth;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPath(path, paint);
        setDrawData();
    }

    /**
     * Draw waves
     */
    private void setDrawData() {
        path.reset();
        path.rQuadTo((waveWidth / 2), waveHeight, waveWidth, 0);
        path.lineTo(mWidth, mHeight);
        path.lineTo(0, mHeight);
        path.close();
        requestLayout();
    }
}