package com.moemoe.lalala.view.adapter;

import android.content.Context;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.CompoundEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Hygge on 2018/6/12.
 */

public class CompoundAdapter extends BaseRecyclerViewAdapter<CompoundEntity, CompoundHolder> {


    public CompoundAdapter(Context context) {
        super(R.layout.item_compoun);
    }

    @Override
    protected void convert(CompoundHolder helper, CompoundEntity item, int position) {
        helper.createItem(item, position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
