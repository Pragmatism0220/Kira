package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Hygge on 2018/7/19.
 */

public class LibraryBagAdapter extends BaseRecyclerViewAdapter<ShowFolderEntity, LibraryBagHolder> {

    public LibraryBagAdapter() {
        super(R.layout.item_hot_bag_new);
    }

    @Override
    protected void convert(LibraryBagHolder helper, ShowFolderEntity item, int position) {
        helper.createItem(item, position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
