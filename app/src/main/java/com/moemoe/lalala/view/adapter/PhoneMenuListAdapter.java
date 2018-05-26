package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.PersonFollowEntity;
import com.moemoe.lalala.model.entity.PhoneMenuEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by yi on 2017/6/26.
 */

public class PhoneMenuListAdapter extends BaseRecyclerViewAdapter<PhoneMenuEntity, PhoneMenuListHolder> {

    private String type;

    public PhoneMenuListAdapter(String type) {
        super(R.layout.item_menber);
        this.type = type;
    }


    @Override
    protected void convert(PhoneMenuListHolder helper, final PhoneMenuEntity item, int position) {
        helper.createItem(item, type,position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
