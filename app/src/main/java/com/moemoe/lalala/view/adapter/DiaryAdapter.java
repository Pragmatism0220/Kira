package com.moemoe.lalala.view.adapter;



import com.moemoe.lalala.model.entity.DiaryEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by Administrator on 2018/6/5.
 */

public class DiaryAdapter extends BaseRecyclerViewAdapter<DiaryEntity,DiaryViewHolder> {


    public DiaryAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(DiaryViewHolder helper, DiaryEntity item, int position) {

    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
