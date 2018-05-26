package com.moemoe.lalala.view.adapter;

import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.netamusic.data.model.Music;
import com.moemoe.lalala.netamusic.player.AudioPlayer;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

/**
 *
 * Created by yi on 2017/7/21.
 */

public class MusicListHolder extends ClickableViewHolder {

    public MusicListHolder(View itemView) {
        super(itemView);
    }

    public void createItem(final Music entity, final int position, final MusicListAdapter adapter){
        setText(R.id.tv_title,entity.getTitle());
        $(R.id.iv_remove).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if(position != AudioPlayer.get().getPlayPosition()){
                    AudioPlayer.get().delete(position);
                    adapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showShortToast(context,"正在播放中，不能删除");
                }
            }
        });
    }
}
