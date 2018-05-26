package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.SimpleUserEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Sora on 2018/3/2.
 */

public class CommunityDetailsAdapter extends BaseRecyclerViewAdapter<SimpleUserEntity, CommunityDetailHolder> {

    public CommunityDetailsAdapter() {
        super(R.layout.item_cm_details_minister);
    }

    @Override
    protected void convert(CommunityDetailHolder helper, SimpleUserEntity item, int position) {
        helper.createItem(item, position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
