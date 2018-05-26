package com.moemoe.lalala.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moemoe.lalala.R;

/**
 * Created by Haru on 2016/4/28 0028.
 */
public class NewFolderDecoration extends RecyclerView.ItemDecoration {


    public NewFolderDecoration() {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = view.getContext().getResources().getDimensionPixelSize(R.dimen.y24);
        int x44 = view.getContext().getResources().getDimensionPixelSize(R.dimen.x44);
        int x4 = view.getContext().getResources().getDimensionPixelSize(R.dimen.x4);
        int x18 = view.getContext().getResources().getDimensionPixelSize(R.dimen.x18);
        if (parent.getChildLayoutPosition(view) % 6 == 0) {
            outRect.left = x44;
            outRect.right = x18;
        } else if (parent.getChildLayoutPosition(view) % 6 == 1) {
            outRect.left = x18;
            outRect.right = x18;
        } else if (parent.getChildLayoutPosition(view) % 6 == 2) {
            outRect.left = x18;
            outRect.right = x18;
        } else if (parent.getChildLayoutPosition(view) % 6 == 3) {
            outRect.left = x18;
            outRect.right = x18;
        } else if (parent.getChildLayoutPosition(view) % 6 == 4) {
            outRect.left = x18;
            outRect.right = x18;
        } else if (parent.getChildLayoutPosition(view) % 6 == 5) {
            outRect.left = x18;
            outRect.right = x44;
        }
    }
}
