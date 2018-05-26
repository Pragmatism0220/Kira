package com.moemoe.lalala.view.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2018/5/11.
 */

public class SlideScrollView extends ScrollView {

    private onScrollChangedListener listener;

    public SlideScrollView(Context context) {
        super(context);
    }

    public SlideScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(onScrollChangedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) {
            listener.onCcroll(l, t, oldl, oldt);
        }
    }

    public interface onScrollChangedListener {
        void onCcroll(int l, int t, int oldL, int oldT);
    }
}
