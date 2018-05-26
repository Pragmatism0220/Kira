package com.moemoe.lalala.view.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridLayout;

import com.moemoe.lalala.R;
import com.moemoe.lalala.utils.FeedBagDecoration;
import com.moemoe.lalala.utils.NewFeedBagDecoration;
import com.moemoe.lalala.view.activity.CommunityV1Activity;
import com.moemoe.lalala.view.adapter.NewCommunityChildAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Sora on 2018/2/28.
 */

public class NewCommunityChildFragment extends BaseFragment {

    @BindView(R.id.list)
    RecyclerView mListDocs;
    private NewCommunityChildAdapter mAdapter;

    public static NewCommunityChildFragment newInstance() {
        NewCommunityChildFragment fragment = new NewCommunityChildFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_new_community_child;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setOrientation(GridLayout.VERTICAL);
        mListDocs.setLayoutManager(gridLayoutManager);
        mListDocs.addItemDecoration(new NewFeedBagDecoration(getResources().getDimensionPixelSize(R.dimen.x24), 3));
        mAdapter = new NewCommunityChildAdapter();
        mListDocs.setAdapter(mAdapter);
        mListDocs.setBackgroundColor(Color.WHITE);
        ArrayList<String> entities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entities.add("社区");
        }
        mAdapter.setList(entities);

        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(), CommunityV1Activity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }
}
