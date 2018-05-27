package com.moemoe.lalala.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.moemoe.lalala.R;
import com.moemoe.lalala.utils.MenuVItemDecoration;
import com.moemoe.lalala.view.adapter.ShoppingItemAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Hygge on 2018/5/22.
 */

public class ShoppingFragment extends BaseFragment {

    @BindView(R.id.list)
    PullAndLoadView mListDocs;
    private ShoppingItemAdapter mAdapter;

    public static ShoppingFragment newInstance() {
        return new ShoppingFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_shopping;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDocs.setLoadMoreEnabled(false);
        mListDocs.setLayoutManager(new LinearLayoutManager(getContext()));
        mListDocs.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
        mAdapter = new ShoppingItemAdapter();
        mListDocs.getRecyclerView().addItemDecoration(new MenuVItemDecoration((int) getResources().getDimension(R.dimen.y24)));
        mListDocs.getRecyclerView().setAdapter(mAdapter);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("你好");
        }
        mAdapter.setList(list);
    }
}
