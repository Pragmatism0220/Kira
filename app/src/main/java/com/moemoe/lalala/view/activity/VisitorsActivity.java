package com.moemoe.lalala.view.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.BR;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityVisitorsBinding;
import com.moemoe.lalala.di.components.DaggerVisitorsComponent;
import com.moemoe.lalala.di.modules.VisitorsModule;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.presenter.VisitorPresenter;
import com.moemoe.lalala.presenter.VisitorsContract;
import com.moemoe.lalala.view.adapter.RootRecycleViewAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.squareup.haha.trove.THash;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.transform.Source;

/**
 * 访客记录
 */
public class VisitorsActivity extends BaseActivity implements VisitorsContract.View {

    private ActivityVisitorsBinding binding;
    private RootRecycleViewAdapter mAdapter;
    public static final String TAG = "VisitorsActivity";
    private List<VisitorsEntity> entityList = new ArrayList<>();


    private TextView mTvTitle;
    private ImageView mIvBack;

    @Inject
    VisitorPresenter mPresenter;

    @Override
    protected void initComponent() {
        DaggerVisitorsComponent.builder()
                .visitorsModule(new VisitorsModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_visitors);
        binding.setPresenter(new Presenter());
        mTvTitle = findViewById(R.id.tv_toolbar_title);
        mIvBack = findViewById(R.id.iv_back);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mTvTitle.setText("访客记录");
        mTvTitle.setTextColor(getResources().getColor(R.color.main_cyan));
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {
        mPresenter.getVisitorsInfo(10, 0);
    }

    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void getVisitorsInfoSuccess(ArrayList<VisitorsEntity> entities) {
        Log.i(TAG, "getVisitorsInfoSuccess: " + entities);
        this.entityList = entities;
        initAdapter(entities);
    }

    private void initAdapter(List<VisitorsEntity> itemBeanList) {
        Log.i(TAG, "initAdapter: " + entityList);


        binding.rootRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RootRecycleViewAdapter(divideList(itemBeanList), this);
        binding.rootRv.setAdapter(mAdapter);
    }

    private List<TempEntity> divideList(List<VisitorsEntity> source) {
        List<TempEntity> data = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            VisitorsEntity currentItemBean = source.get(i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(currentItemBean.getCreateTime()));
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date currentDate = calendar.getTime();

            if (i == 0) {
                TempEntity tempEntity = new TempEntity();
                tempEntity.setTitleDate(currentDate);
                data.add(tempEntity);
            }

            Date lastDate = data.get(data.size() - 1).getTitleDate();
            if (lastDate.compareTo(currentDate) == 0) {
                data.get(data.size() - 1).getItems().add(currentItemBean);
            } else {
                TempEntity tempEntity = new TempEntity();
                tempEntity.setTitleDate(currentDate);

                List<VisitorsEntity> itemBeen = new ArrayList<>();
                itemBeen.add(currentItemBean);
                tempEntity.setItems(itemBeen);

                data.add(tempEntity);
            }
        }
        return data;
    }


    public class Presenter {
        public void onClick(View v) {
            switch (v.getId()) {

            }

        }
    }


    public static class TempEntity {
        private Date titleDate;
        private List<VisitorsEntity> items = new ArrayList<>();

        public Date getTitleDate() {
            return titleDate;
        }

        public void setTitleDate(Date titleDate) {
            this.titleDate = titleDate;
        }

        public List<VisitorsEntity> getItems() {
            return items;
        }

        public void setItems(List<VisitorsEntity> items) {
            this.items = items;
        }
    }


}
