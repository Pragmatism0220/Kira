package com.moemoe.lalala.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moemoe.lalala.R;

/**
 * Created by Haru on 2016/4/28 0028.
 */
public class DepartmentDecoration extends RecyclerView.ItemDecoration {


    public DepartmentDecoration() {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = view.getResources().getDimensionPixelSize(R.dimen.y24);
    }
}
