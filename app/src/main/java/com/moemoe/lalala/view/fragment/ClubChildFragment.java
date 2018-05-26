package com.moemoe.lalala.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.OfficialTag;
import com.moemoe.lalala.view.adapter.ClubChildAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/18.
 */

@SuppressLint("ValidFragment")
public class ClubChildFragment extends BaseFragment {

    @BindView(R.id.list)
    RecyclerView mListDocs;
    private ClubChildAdapter adapter;
    private ArrayList<OfficialTag> entitey;
    private String type;

    @SuppressLint("ValidFragment")
    public ClubChildFragment(ArrayList<OfficialTag> entitie) {
        this.entitey = entitie;
    }

    public static ClubChildFragment newInstance(ArrayList<OfficialTag> entitie, String type) {
        ClubChildFragment fragment = new ClubChildFragment(entitie);
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_club_child;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        type = getArguments().getString("type");
        adapter = new ClubChildAdapter(type);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mListDocs.setLayoutManager(layoutManager);
        mListDocs.setAdapter(adapter);
        mListDocs.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        if (entitey != null && entitey.size() > 0) {
            if ("我的".equals(type)) {
                ArrayList<OfficialTag> list = new ArrayList<>();
                list.add(entitey.get(0));
                adapter.setList(list);
            } else {
                entitey.remove(0);
                adapter.setList(entitey);
            }
        }
    }
}
