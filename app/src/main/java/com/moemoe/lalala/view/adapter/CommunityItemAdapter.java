package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Sora on 2018/3/1.
 */

public class CommunityItemAdapter extends BaseRecyclerViewAdapter<UserFollowTagEntity,CommunityHolder > {

    public CommunityItemAdapter() {
        super(R.layout.item_community);
    }

    @Override
    protected void convert(CommunityHolder helper, UserFollowTagEntity item, int position) {
            helper.createItem(item,position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
