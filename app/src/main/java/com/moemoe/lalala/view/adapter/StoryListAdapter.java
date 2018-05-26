package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.StoryListEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Sora on 2018/4/8.
 */

public class StoryListAdapter extends BaseRecyclerViewAdapter<StoryListEntity, StoryListHolder> {

    public StoryListAdapter() {
        super(R.layout.item_story_list);
    }

    @Override
    protected void convert(StoryListHolder helper, StoryListEntity item, int position) {
        helper.creatItem(item, position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
