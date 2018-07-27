package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.view.widget.adapter.BaseMultiItemRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Hygge on 2018/7/19.
 */

public class LibraryBagAdapter extends BaseMultiItemRecyclerViewAdapter<ShowFolderEntity, LibraryBagHolder> {
    private String type;

    public LibraryBagAdapter(String type) {
        super(null);
        addItemType(0, R.layout.item_hot_bag_new);
        addItemType(1, R.layout.item_history_record);
        this.type = type;

    }

    @Override
    protected void convert(LibraryBagHolder helper, ShowFolderEntity item, int position) {
        if (type.equals("历史记录")) {
            helper.createItem(item, position, type);
        } else {
            helper.createItem(item, position);
        }
    }

    @Override
    public int getItemType(int position) {
        if (type.equals("历史记录")) {
            return 1;
        } else {
            return 0;
        }
    }
}
