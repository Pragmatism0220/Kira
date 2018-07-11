package com.moemoe.lalala.view.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.OfficialTag;
import com.moemoe.lalala.utils.FolderDecoration;
import com.moemoe.lalala.view.activity.CommunityV1Activity;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.adapter.ClickableViewHolder;

/**
 * Created by Administrator on 2018/4/19.
 */

public class ClubChildHolder extends ClickableViewHolder {

    TextView tvMore;
    RecyclerView llRoot;
    View mainRoot;
    CommunityItemAdapter adapter;

    public ClubChildHolder(View itemView) {
        super(itemView);
        tvMore = $(R.id.tv_more_add);
        llRoot = $(R.id.ll_folder_root);
        mainRoot = $(R.id.ll_root);
    }

    public void createItem(final OfficialTag entity, final int position, String type) {
        if ("我的".equals(type) && position == 0) {
            setText(R.id.tv_title, "已加入社团");
        } else {
            setText(R.id.tv_title, entity.getText());
        }
        setVisible(R.id.tv_num, false);
        llRoot.setLayoutManager(new GridLayoutManager(context, 3));
//        llRoot.addItemDecoration(new FolderDecoration());
        llRoot.setBackgroundColor(Color.WHITE);
        adapter = new CommunityItemAdapter();
        llRoot.setAdapter(adapter);

        if (entity.getTagSec().get(0).getTagThi() != null && entity.getTagSec().get(0).getTagThi().size() > 0) {
            llRoot.setVisibility(View.VISIBLE);
            adapter.setList(entity.getTagSec().get(0).getTagThi());
        } else {
            llRoot.setVisibility(View.GONE);
        }

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, CommunityV1Activity.class);
                String id = adapter.getList().get(position).getId();
                String text = adapter.getList().get(position).getText();
                intent.putExtra("uuid", id + "");
                intent.putExtra("title", text + "");
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }
}
