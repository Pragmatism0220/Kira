package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.BagVisitorEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Sora on 2018/3/6.
 */

public class BagVisitorAdapter extends BaseRecyclerViewAdapter<UserTopEntity, BagVisitorHolder> {

    public BagVisitorAdapter() {
        super(R.layout.item_bag_visitor);
    }

    @Override
    protected void convert(BagVisitorHolder helper, UserTopEntity item, int position) {
        helper.createItem(item, position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
