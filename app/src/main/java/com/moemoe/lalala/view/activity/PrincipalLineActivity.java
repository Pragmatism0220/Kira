package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityPrincipalLineBinding;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.view.adapter.PrincipalLineAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.base.TestPrincipalInfo;
import com.moemoe.lalala.view.widget.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * zhangyan(do)
 * 主线剧情CardView选择页面
 */

public class PrincipalLineActivity extends BaseActivity {

    private ActivityPrincipalLineBinding binding;
    private PrincipalLineAdapter mAdapter;
    private List<TestPrincipalInfo> mLists;


    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_principal_line);
        binding.setPresenter(new Presenter());
        getData();
        initAdapter();
    }

    private void initAdapter() {
        mAdapter = new PrincipalLineAdapter(mLists, this);
        binding.principalLineRecycle.setLayoutManager(new GridLayoutManager(this, 2));
        int rightSpace = 22;
        int buttonSpace = 24;
        int top = 32;
        binding.principalLineRecycle.addItemDecoration(new SpacesItemDecoration(buttonSpace, rightSpace, top));
        binding.principalLineRecycle.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), mLists.get(position).getTitleName() + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PrincipalLineActivity.this, PrincipalListActivity.class);
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
        mLists = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mLists.add(new TestPrincipalInfo());
        }
        return mLists;
    }


    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.principal_line_back_btn:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
