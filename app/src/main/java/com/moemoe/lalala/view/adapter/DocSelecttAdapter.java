package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Sora on 2018/3/7.
 */

public class DocSelecttAdapter extends BaseRecyclerViewAdapter<UserFollowTagEntity, DocSelectHolder> {


    private boolean isSelect;

    public DocSelecttAdapter() {
        super(R.layout.item_doc_select);
        isSelect = false;
    }

    @Override
    protected void convert(DocSelectHolder helper, UserFollowTagEntity item, int position) {
        helper.createItem(item, position, isSelect);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
    public void setSelect(boolean select) {
        isSelect = select;
    }
}
