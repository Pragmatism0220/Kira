package com.moemoe.lalala.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moemoe.lalala.R;

/**
 * Created by Haru on 2016/4/28 0028.
 */
public class BagVisitorDecoration extends RecyclerView.ItemDecoration {


    public BagVisitorDecoration() {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int x24 = view.getContext().getResources().getDimensionPixelSize(R.dimen.x24);
        int x8 = view.getContext().getResources().getDimensionPixelSize(R.dimen.x8);
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.right=x8;
        } else if (parent.getChildLayoutPosition(view) == 4) {
            outRect.left=x8;
            outRect.right=x24;
        }else {
            outRect.left=x8;
            outRect.right=x8;
        }
    }
}
