package com.moemoe.lalala.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moemoe.lalala.R;

/**
 * Created by Haru on 2016/4/28 0028.
 */
public class NewCommunityDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int spanCount;

    public NewCommunityDecoration(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = view.getResources().getDimensionPixelSize(R.dimen.y1);
        } else {
            outRect.top = view.getResources().getDimensionPixelSize(R.dimen.y24);
        }
        int x24 = view.getContext().getResources().getDimensionPixelSize(R.dimen.x24);
        int x12 = view.getContext().getResources().getDimensionPixelSize(R.dimen.x12);
//        if (parent.getChildLayoutPosition(view) % 2 == 0) {
//            outRect.right = x24;
//            outRect.left = x12;
//        } else {
//            outRect.left = x24;
//            outRect.right = x12;
//        }
    }
}
