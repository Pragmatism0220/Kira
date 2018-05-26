package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityBranchInfoBinding;
import com.moemoe.lalala.view.adapter.BranchInfoAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.base.BranchInfoBean;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class BranchInfoActivity extends BaseActivity {

    private ActivityBranchInfoBinding binding;
    private BranchInfoAdapter mAdapter;
    private List<BranchInfoBean> mlists;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branch_info);
        binding.setPresenter(new Presenter());
        getData();
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new BranchInfoAdapter(mlists, this);
        binding.branchInfoRecycleView.setLayoutManager(new GridLayoutManager(this, 3));
//        int lifeSpace = 18;
//        int buttonSpace = 0;
//        int top = 0;
//        binding.branchInfoRecycleView.addItemDecoration(new SpacesItemDecoration(buttonSpace, lifeSpace, top));
//        binding.branchInfoRecycleView.addItemDecoration(new SpacesItemDecoration(34,16,16));
        binding.branchInfoRecycleView.setAdapter(mAdapter);
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
        mlists = new ArrayList<>();
        for (int i = 0; i <= 2; i++) {
            mlists.add(new BranchInfoBean());
        }
        return mlists;
    }


    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.branch_back_btn:
                    finish();
                    break;
                case R.id.branch_info_recall:
                    if (binding.branchInfoRecall.isSelected()) {
                        binding.branchInfoRecall.setSelected(true);
                        Toast.makeText(getApplicationContext(), "回忆中", Toast.LENGTH_SHORT).show();
                    } else {
                        binding.branchInfoRecall.setSelected(false);
                        Toast.makeText(getApplicationContext(), "没有回忆", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }

        }
    }
}
