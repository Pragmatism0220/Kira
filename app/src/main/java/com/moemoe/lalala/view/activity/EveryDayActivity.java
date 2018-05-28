package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityEveryDayBinding;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.view.adapter.EveryDayRecycleAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.base.EveryDayBean;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class EveryDayActivity extends BaseActivity {

    private ActivityEveryDayBinding binding;
    private EveryDayRecycleAdapter mAdapter;
    private List<EveryDayBean> lists;

    @Override

    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_every_day);
        binding.setPresenter(new Presenter());
        getData();
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new EveryDayRecycleAdapter(this, lists);
        binding.everyDayRecycleView.setLayoutManager(new GridLayoutManager(this, 2));
        int rightSpace = 22;
        int buttonSpace = 24;
        int top = 32;
        binding.everyDayRecycleView.addItemDecoration(new SpacesItemDecoration(buttonSpace, rightSpace, top));
        binding.everyDayRecycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), lists.get(position).getTitleName() + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EveryDayActivity.this, EveryDayInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    public List getData() {
        lists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            lists.add(new EveryDayBean());

        }
        return lists;
    }

    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.every_day_back_btn:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
