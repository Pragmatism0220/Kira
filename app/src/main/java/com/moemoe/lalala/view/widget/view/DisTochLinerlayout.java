package com.moemoe.lalala.view.widget.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.moemoe.lalala.view.activity.FeedV3Activity;

/**
 * Created by Hygge on 2018/7/14.
 */

public class DisTochLinerlayout extends LinearLayout {

    public DisTochLinerlayout(Context context) {
        super(context);
    }

    public DisTochLinerlayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DisTochLinerlayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getContext() instanceof FeedV3Activity) {
            View sideLine = ((FeedV3Activity) getContext()).getSideLine();
            View sidaMenu = ((FeedV3Activity) getContext()).getSideMenu();
            if (sidaMenu != null && sidaMenu.getVisibility() == View.VISIBLE) {
                ((FeedV3Activity) getContext()).getGoneSideMenu();
                return true;
            }
            if (sideLine != null && sideLine.getVisibility() == View.VISIBLE) {
                ((FeedV3Activity) getContext()).getGoneSideLine();
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
