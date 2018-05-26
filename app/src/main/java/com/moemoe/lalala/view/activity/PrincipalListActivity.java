package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityPrincipalListBinding;
import com.moemoe.lalala.event.OnItemListener;
import com.moemoe.lalala.utils.SpacesItemDecoration;
import com.moemoe.lalala.view.adapter.PrincipalListAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.base.PrincipalListBean;

import java.util.ArrayList;
import java.util.List;

public class PrincipalListActivity extends BaseActivity {

    private ActivityPrincipalListBinding binding;
    private PrincipalListAdapter mAdapter;
    private List<PrincipalListBean> data;
    private List<Bitmap> extraBmps;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_principal_list);
        binding.setPresenter(new Presenter());
        getData();
        initAdapter();
    }

    private void getData() {
        data = new ArrayList<>();
        extraBmps = new ArrayList<>();
        extraBmps.add(BitmapFactory.decodeResource(getResources(), R.drawable.ic_demo_example));

        data.add(new PrincipalListBean("1", "意外的惊喜", false, null));
        data.add(new PrincipalListBean("2", "意外的惊喜", false, extraBmps));
        data.add(new PrincipalListBean("3", "意外的惊喜", false, null));
        data.add(new PrincipalListBean("4", "意外的惊喜", true, null));

    }

    private void initAdapter() {
        mAdapter = new PrincipalListAdapter(this, data);
        binding.principalListRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        binding.principalListRecycleView.addItemDecoration(new SpacesItemDecoration(30));
        binding.principalListRecycleView.setAdapter(mAdapter);

//        mAdapter.setOnItemClickListener(new OnItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(getApplicationContext(), data.get(position).getTitleName() + position, Toast.LENGTH_SHORT).show();
//            }
//        });
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


    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.principal_list_back_btn:
                    finish();
                    break;
                default:
                    break;
            }
        }

    }
}
