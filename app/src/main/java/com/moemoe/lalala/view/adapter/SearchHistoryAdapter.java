package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Sora on 2018/3/5.
 */

public class SearchHistoryAdapter extends BaseRecyclerViewAdapter<String, SearchHistoryHolder> {

    public SearchHistoryAdapter() {
        super(R.layout.item_search_history);
    }

    @Override
    protected void convert(SearchHistoryHolder helper, String item, int position) {
        helper.createItem(item, position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
