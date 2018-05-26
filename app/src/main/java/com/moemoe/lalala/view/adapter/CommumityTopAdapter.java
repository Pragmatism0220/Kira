package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Sora on 2018/3/7.
 */

public class CommumityTopAdapter extends BaseRecyclerViewAdapter<DocResponse, CmDocTopHolder> {

    public CommumityTopAdapter() {
        super(R.layout.item_doc_top);
    }

    @Override
    protected void convert(CmDocTopHolder helper, DocResponse item, int position) {
        helper.createItem(item, position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
