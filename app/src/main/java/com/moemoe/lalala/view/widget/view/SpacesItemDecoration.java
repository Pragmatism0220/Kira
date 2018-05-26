package com.moemoe.lalala.view.widget.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2018/5/8.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int buttomSpace;
    private int lifeSpace;
    private int topSpace;

    public SpacesItemDecoration(int buttomSpace, int lifeSpace, int topSpace) {
        this.buttomSpace = buttomSpace;
        this.lifeSpace = lifeSpace;
        this.topSpace = topSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = lifeSpace;
        outRect.bottom = buttomSpace;
        outRect.top = topSpace;

        if (parent.getChildPosition(view) == 0) {
            outRect.top = topSpace;
        }
    }
}
