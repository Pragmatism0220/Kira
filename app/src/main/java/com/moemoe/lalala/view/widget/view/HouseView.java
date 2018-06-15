package com.moemoe.lalala.view.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Hygge on 2018/6/4.
 */

@SuppressLint("AppCompatCustomView")
public class HouseView extends TextView {

    public final PointF position = new PointF();
    private float mapX;
    private float mapY;
    private boolean isTouch = true;

    public HouseView(Context context) {
        super(context);
    }

    public HouseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HouseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public float getMapX() {
        return mapX;
    }

    public void setMapX(float mapX) {
        this.mapX = mapX;
    }

    public float getMapY() {
        return mapY;
    }

    public void setMapY(float mapY) {
        this.mapY = mapY;
    }

    public void show(PointF position) {
        if (getBackground() == null) return;
        setMapMarkViewAtPosition(position);
    }

    private void setMapMarkViewAtPosition(PointF center) {
        float posX = center.x;
        float posY = center.y;
        setMapMarkViewAtPosition(posX, posY);
    }

    public void setTouch(boolean isTouch) {
        this.isTouch = isTouch;
    }

    public boolean onTouchEvent() {
        return isTouch;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onTouchEvent()) {
            return super.onTouchEvent(event);
        } else {
            return false;
        }
    }

    private void setMapMarkViewAtPosition(float x, float y) {

        // BUG : HTC SDK 2.3.3 界面会被不停的重绘,这个重绘请求是View.onDraw()方法发起的。
//        if (position.equals(x, y)) return;
        position.set(position.x + x, position.y + y);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        int left = (int) position.x;
        int top = (int) position.y;
        // HTC SDK 2.3.3 Required
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.leftMargin = left;
        params.topMargin = top;
        setLayoutParams(params);
        //}
    }

    public void scaleMark(float scaleCenterX, float scaleCenterY, float scale) {
        position.set(scaleByPoint(scaleCenterX, scaleCenterY, scale));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        int left = (int) position.x;
        int top = (int) position.y;
        // HTC SDK 2.3.3 Required
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.leftMargin = left;
        params.topMargin = top;
        setLayoutParams(params);
    }

    private PointF scaleByPoint(float scaleCenterX, float scaleCenterY, float scale) {
        Matrix matrix = new Matrix();
        matrix.preTranslate(position.x, position.y);
        matrix.postScale(scale, scale, scaleCenterX, scaleCenterY);
        float[] values = new float[9];
        matrix.getValues(values);
        return new PointF(values[Matrix.MTRANS_X], values[Matrix.MTRANS_Y]);
    }

}
