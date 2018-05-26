package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

import io.rong.imkit.model.UIMessage;

/**
 *
 * Created by yi on 2017/6/26.
 */

public class ChatRoomAdapter extends BaseRecyclerViewAdapter<UIMessage,ChatRoomHolder> {

    public ChatRoomAdapter() {
        super(R.layout.item_bushi_chat);
    }
    
    @Override
    protected void convert(ChatRoomHolder helper, final UIMessage item, int position) {
        helper.createItem(item,position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }

    public int findPosition(long id) {
        int index = getItemCount();
        int position = -1;

        while(index-- > 0) {
            if(getItemId(index) == id) {
                position = index;
                break;
            }
        }

        return position;
    }

    public long getItemId(int position) {
        UIMessage message = this.getItem(position);
        return message == null?-1L:(long)message.getMessageId();
    }
}
