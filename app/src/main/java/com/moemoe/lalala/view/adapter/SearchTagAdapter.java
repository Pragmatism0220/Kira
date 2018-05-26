package com.moemoe.lalala.view.adapter;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

import java.util.ArrayList;

/**
 *
 * Created by yi on 2017/6/26.
 */

public class SearchTagAdapter extends BaseRecyclerViewAdapter<UserFollowTagEntity,SearchTagHolder> {

    private ArrayList<UserFollowTagEntity> userIds;

    private boolean showClose;

    public SearchTagAdapter() {
        super(R.layout.item_tags);
        showClose = true;
    }

    public SearchTagAdapter(ArrayList<UserFollowTagEntity> list){
        super(list);
    }
    
    @Override
    protected void convert(SearchTagHolder helper, final UserFollowTagEntity item, int position) {
        helper.createItem(item);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }

    public boolean isShowClose() {
        return showClose;
    }

    public void setShowClose(boolean showClose) {
        this.showClose = showClose;
    }

    public ArrayList<UserFollowTagEntity> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<UserFollowTagEntity> userIds) {
        this.userIds = userIds;
    }
}
