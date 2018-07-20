package com.moemoe.lalala.view.activity;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.upDateEntity;
import com.moemoe.lalala.view.adapter.BottomSheetHolder;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Administrator on 2018/7/17.
 */

public class BottomSheetAdapter extends BaseRecyclerViewAdapter<upDateEntity, BottomSheetHolder> {

    public BottomSheetAdapter() {
        super(R.layout.bottom_sheet_item);
    }

    @Override
    protected void convert(BottomSheetHolder helper, upDateEntity item, int position) {
        helper.create(item);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
