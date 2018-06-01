package com.moemoe.lalala.view.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.moemoe.lalala.app.MoeMoeApplication;

/**
 * Created by Hygge on 2018/5/29.
 */

public class WindowView extends RelativeLayout {
    public WindowView(Context context) {
        super(context);
    }

    public WindowView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (MoeMoeApplication.getInstance().isMenu()) {
//            MoeMoeApplication.getInstance().GoneWindowMager(getContext());
//            return true;
        }
        return super.dispatchTouchEvent(event);
    }
}
