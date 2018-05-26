package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Hygge on 2018/5/23.
 */

public class ShoppingItemAdapter extends BaseRecyclerViewAdapter<String, ShoppingItemHolder> {


    public ShoppingItemAdapter() {
        super(R.layout.item_shopping_item);
    }


    @Override
    protected void convert(ShoppingItemHolder helper, final String item, int position) {
        helper.createItem(item, position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }

}
