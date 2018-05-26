package com.moemoe.lalala.view.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.DepartmentEntity;
import com.moemoe.lalala.model.entity.DiscoverEntity;
import com.moemoe.lalala.model.entity.DocResponse;
import com.moemoe.lalala.model.entity.DocSearchEntity;
import com.moemoe.lalala.model.entity.FeedFollowType1Entity;
import com.moemoe.lalala.model.entity.NewDynamicEntity;
import com.moemoe.lalala.model.entity.SeachAllEntity;
import com.moemoe.lalala.model.entity.SearchNormalEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.view.widget.adapter.BaseMultiItemRecyclerViewAdapter;

/**
 * Created by Sora on 2018/3/8.
 */

public class SearchAllAdapter extends BaseMultiItemRecyclerViewAdapter<SearchNormalEntity, SearchAllHolder> {
    private Gson gson = new Gson();

    public SearchAllAdapter() {
        super(null);
        addItemType(0, R.layout.item_new_feed_list);
        addItemType(1, R.layout.item_menber);
        addItemType(2, R.layout.item_feed_type_2_v3);
        addItemType(3, R.layout.item_feed_type_1_v3);
        addItemType(4, R.layout.item_type_top);
    }

    @Override
    protected void convert(SearchAllHolder helper, final SearchNormalEntity item, int position) {
        if ("dynamic".equals(item.getType())) {
            NewDynamicEntity element = gson.fromJson(item.getData(), NewDynamicEntity.class);
            helper.createItem(element, position);
        } else if ("user".equals(item.getType())) {
            UserTopEntity userTopEntity = gson.fromJson(item.getData(), UserTopEntity.class);
            helper.createUserItem(userTopEntity, position);
        } else if ("doc".equals(item.getType())) {
            DocResponse docSearchEntity = gson.fromJson(item.getData(), DocResponse.class);
            helper.createDocItem(docSearchEntity, position);
        } else if ("folder".equals(item.getType())) {
            ShowFolderEntity showFolderEntity = gson.fromJson(item.getData(), ShowFolderEntity.class);
            helper.createBagItem(showFolderEntity, position);
        } else if ("top".equals(item.getType())) {
            helper.createTopItem(item.getData(), position);
        }
    }

    @Override
    public int getItemType(int position) {
        SearchNormalEntity entity = getItem(position);
        //SearchNormalEntity tile
        String type = entity.getType();
        if (type.equals("dynamic")) {
            return 0;
        } else if (type.equals("user")) {
            return 1;
        } else if (type.equals("folder")) {
            return 2;
        } else if (type.equals("doc")) {
            return 3;
        } else {
            return 4;
        }

    }
}
