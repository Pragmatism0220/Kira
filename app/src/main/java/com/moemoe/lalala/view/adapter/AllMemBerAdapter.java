package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.BagVisitorEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Sora on 2018/3/5.
 */

public class AllMemBerAdapter extends BaseRecyclerViewAdapter<UserTopEntity, AllMemberHolder> {

    public AllMemBerAdapter() {
        super(R.layout.item_menber);
    }

    @Override
    protected void convert(AllMemberHolder helper, UserTopEntity item, int position) {
        helper.createItem(item, position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
