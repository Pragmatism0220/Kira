package com.moemoe.lalala.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moemoe.lalala.R;

import io.rong.imlib.IGetNotificationQuietHoursCallback;

/**
 * Created by Haru on 2016/4/28 0028.
 */
public class MenuVItemDecoration extends RecyclerView.ItemDecoration {

    private int topSpace;

    public MenuVItemDecoration(int space) {
        this.topSpace = topSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = view.getResources().getDimensionPixelSize(R.dimen.y1);
        } else {
            outRect.top = view.getResources().getDimensionPixelSize(R.dimen.y24);
        }

    }
}
