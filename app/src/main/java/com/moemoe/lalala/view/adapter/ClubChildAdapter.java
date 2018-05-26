package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.OfficialTag;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Administrator on 2018/4/19.
 */

public class ClubChildAdapter extends BaseRecyclerViewAdapter<OfficialTag, ClubChildHolder> {
    private String type;
    public ClubChildAdapter(String type) {
        super(R.layout.item_club);
        this.type=type;
    }
    public ClubChildAdapter() {
        super(R.layout.item_club);
    }

    @Override
    protected void convert(ClubChildHolder helper, final OfficialTag item, int position) {
            helper.createItem(item, position,type);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }

}
