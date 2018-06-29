package com.moemoe.lalala.view.adapter;

import android.content.Context;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.BranchStoryAllEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Administrator on 2018/5/15.
 */

public class BranchFragmentListAdapter extends BaseRecyclerViewAdapter<BranchStoryAllEntity, BranchHolder> {
    private boolean isSelect;

    public BranchFragmentListAdapter(Context context, boolean type) {
        super(R.layout.item_branch);
        this.isSelect = type;
    }

    @Override
    protected void convert(BranchHolder helper, BranchStoryAllEntity item, int position) {
        helper.createItem(item, position, isSelect);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
