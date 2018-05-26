package com.moemoe.lalala.view.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.utils.FolderDecoration;
import com.moemoe.lalala.view.activity.CommunityV1Activity;
import com.moemoe.lalala.view.adapter.CommunityItemAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;

import static com.moemoe.lalala.utils.StartActivityConstant.REQ_GO_COMMUNITY;

/**
 * Created by Sora on 2018/3/1.
 */

@SuppressLint("ValidFragment")
public class CommunityTagsFragment extends BaseFragment {

    @BindView(R.id.list)
    RecyclerView list;
    private CommunityItemAdapter adapter;
    private ArrayList<UserFollowTagEntity> entitie = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public CommunityTagsFragment(ArrayList<UserFollowTagEntity> entitie) {
        this.entitie = entitie;
    }

    public static CommunityTagsFragment newInstance(ArrayList<UserFollowTagEntity> entitie) {
        return new CommunityTagsFragment(entitie);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.frag_item_community;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        adapter = new CommunityItemAdapter();
        list.setLayoutManager(new GridLayoutManager(getContext(), 3));
        list.addItemDecoration(new FolderDecoration());
        list.setBackgroundColor(Color.WHITE);
        list.setAdapter(adapter);
        if (entitie.size() > 0) {
            adapter.setList(entitie);
        }

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                clickEvent("社团-社团-进入社团");
                Intent intent = new Intent(getContext(), CommunityV1Activity.class);
                String id = entitie.get(position).getId();
                String text = entitie.get(position).getText();
                intent.putExtra("uuid", id + "");
                intent.putExtra("title", text + "");
                startActivityForResult(intent,REQ_GO_COMMUNITY);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    public void setList(ArrayList<UserFollowTagEntity> entitie) {
        if (adapter != null) {
            if (entitie.size() > 0) {
                adapter.setList(entitie);
            }
        }
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
