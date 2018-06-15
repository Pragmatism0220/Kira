package com.moemoe.lalala.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class BranchFragmentListAdapter extends BaseRecyclerViewAdapter<BranchStoryAllEntity, BranchHolder> {
    private boolean isSelect;
    public BranchFragmentListAdapter(Context context,boolean type) {
        super(R.layout.item_branch);
        this.isSelect=type;
    }

    @Override
    protected void convert(BranchHolder helper, BranchStoryAllEntity item, int position) {
        helper.createItem(item, position,isSelect);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
