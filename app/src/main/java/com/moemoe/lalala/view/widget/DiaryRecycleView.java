package com.moemoe.lalala.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2018/6/5.
 */

public class DiaryRecycleView extends FrameLayout {


    public DiaryRecycleView(Context context) {
        super(context);
    }

    public DiaryRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DiaryRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//        super.onScrolled(recyclerView, dx, dy);
//    }
//
//    @Override
//    public void onScrollStateChanged(int state) {
//        super.onScrollStateChanged(state);
//    }
}
