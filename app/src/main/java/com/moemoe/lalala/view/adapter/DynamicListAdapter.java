package com.moemoe.lalala.view.adapter;

import com.google.gson.Gson;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.CommentV2Entity;
import com.moemoe.lalala.model.entity.DocDetailNormalEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.view.widget.adapter.BaseMultiItemRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by yi on 2017/6/26.
 */

public class DynamicListAdapter extends BaseMultiItemRecyclerViewAdapter<DocDetailNormalEntity, DynamicListHolder> {

    private String mId;
    private boolean showFavorite;
    private Gson gson = new Gson();

    public DynamicListAdapter(String id) {
        super(null);
        addItemType(0,R.layout.item_new_comment);
        addItemType(1,R.layout.item_menber);
        mId = id;
        showFavorite = true;
    }


    @Override
    protected void convert(DynamicListHolder helper, final DocDetailNormalEntity item, int position) {
        if (item.getType() == 0){
            CommentV2Entity commentV2Entity = gson.fromJson(item.getData(), CommentV2Entity.class);
            helper.createItem(commentV2Entity, position, mId, showFavorite);
        }else {
            UserTopEntity userTopEntity = gson.fromJson(item.getData(), UserTopEntity.class);
            helper.createItem(userTopEntity, position);
        }
    }

    @Override
    public int getItemType(int position) {
        DocDetailNormalEntity item = getItem(position);
        if (item.getType() == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setShowFavorite(boolean showFavorite) {
        this.showFavorite = showFavorite;
    }

    public boolean isShowFavorite() {
        return showFavorite;
    }
}
