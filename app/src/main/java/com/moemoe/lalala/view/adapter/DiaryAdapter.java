package com.moemoe.lalala.view.adapter;



import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.DiaryEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by zhangyan on 2018/6/5.
 */

public class DiaryAdapter extends BaseRecyclerViewAdapter<DiaryEntity,DiaryViewHolder> {


    public DiaryAdapter() {
        super(R.layout.diary_item);
    }

    @Override
    protected void convert(DiaryViewHolder helper, DiaryEntity item, int position) {
        helper.createItem(item);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
