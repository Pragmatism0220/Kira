package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Sora on 2018/2/28.
 */

public class NewCommunityChildAdapter extends BaseRecyclerViewAdapter<String, NewCommunityChildHolder> {


    public NewCommunityChildAdapter() {
        super(R.layout.item_new_community_child);
    }

    @Override
    protected void convert(NewCommunityChildHolder helper, String item, int position) {
        helper.createItem(item, position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
