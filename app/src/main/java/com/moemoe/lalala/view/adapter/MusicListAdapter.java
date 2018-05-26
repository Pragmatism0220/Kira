package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.netamusic.data.model.Music;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 *
 * Created by yi on 2017/6/26.
 */

public class MusicListAdapter extends BaseRecyclerViewAdapter<Music,MusicListHolder> {


    public MusicListAdapter() {
        super(R.layout.item_music_list);
    }
    
    @Override
    protected void convert(MusicListHolder helper, final Music item, int position) {
        helper.createItem(item,position,this);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }
}
