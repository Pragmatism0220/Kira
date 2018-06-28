package com.moemoe.lalala.view.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.databinding.ActivityAlarmBinding;
import com.moemoe.lalala.event.AlarmEvent;
import com.moemoe.lalala.greendao.gen.AlarmClockEntityDao;
import com.moemoe.lalala.model.entity.AlarmClockEntity;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.Utils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.PhoneAlarmAdapter;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.fragment.PhoneAlarmEditV2Fragment;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends BaseActivity {

    private ActivityAlarmBinding binding;
    private PhoneAlarmAdapter mAdapter;
    private ArrayList<AlarmClockEntity> mAlarmClockList;
    private ImageView mIvList;
    private TextView mTvTitle;
    private ImageView mIvBack;

    @Override
    protected void initComponent() {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm);
        binding.setPresenter(new Presenter());
        mIvBack = findViewById(R.id.iv_back);
        mTvTitle = findViewById(R.id.tv_toolbar_title);
        mTvTitle.setTextColor(ContextCompat.getColor(this, R.color.main_cyan));
        mIvList = findViewById(R.id.iv_menu_list);
        mIvList.setImageResource(R.drawable.btn_add_alarm);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mAlarmClockList = new ArrayList<>();
        binding.list.getSwipeRefreshLayout().setEnabled(false);
        binding.list.setLoadMoreEnabled(false);
        binding.list.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PhoneAlarmAdapter();
        binding.list.getRecyclerView().setAdapter(mAdapter);
        mAdapter.setList(mAlarmClockList);
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AlarmClockEntity entity = mAlarmClockList.get(position);
                AlarmEditActivity.newInstance(AlarmActivity.this, entity);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        EventBus.getDefault().register(this);
        updateList();
    }

    private void updateList() {
        mAlarmClockList.clear();
        AlarmClockEntityDao dao = GreenDaoManager.getInstance().getSession().getAlarmClockEntityDao();
        List<AlarmClockEntity> list = dao.loadAll();
        for (AlarmClockEntity entity : list) {
            mAlarmClockList.add(entity);
            if (entity.isOnOff()) {
                Utils.startAlarmClock(this, entity);
            } else {
                Utils.cancelAlarmClock(this, (int) entity.getId());
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void addList(AlarmClockEntity entity) {
        mAlarmClockList.clear();
        int id = (int) entity.getId();
        int count = 0;
        int position = 0;
        AlarmClockEntityDao dao = GreenDaoManager.getInstance().getSession().getAlarmClockEntityDao();
        List<AlarmClockEntity> list = dao.loadAll();
        for (AlarmClockEntity entity1 : list) {
            mAlarmClockList.add(entity1);
            if (id == (int) entity1.getId()) {
                position = count;
                if (entity1.isOnOff()) {
                    Utils.startAlarmClock(this, entity1);
                }
            }
            count++;
        }
        mAdapter.notifyItemInserted(position);
        binding.list.getRecyclerView().scrollToPosition(position);
    }

    private void deleteList() {
        mAlarmClockList.clear();
        AlarmClockEntityDao dao = GreenDaoManager.getInstance().getSession().getAlarmClockEntityDao();
        List<AlarmClockEntity> list = dao.loadAll();
        mAlarmClockList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        mTvTitle.setText("定时提醒");
        mIvList.setVisibility(View.VISIBLE);
        mIvList.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent(AlarmActivity.this, AlarmEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void alarmEvent(AlarmEvent event) {
        if (event.getType() == 1) {
            GreenDaoManager.getInstance().getSession().getAlarmClockEntityDao().insertOrReplace(event.getEntity());
            addList(event.getEntity());
        } else if (event.getType() == 2) {
            GreenDaoManager.getInstance().getSession().getAlarmClockEntityDao().delete(event.getEntity());
            Utils.cancelAlarmClock(this, (int) event.getEntity().getId());
            NotificationManager notificationManager = (NotificationManager) getSystemService(Activity.NOTIFICATION_SERVICE);
            // 取消下拉列表通知消息
            assert notificationManager != null;
            notificationManager.cancel((int) event.getEntity().getId());
//            ((PhoneMainV2Activity) getContext()).onBackPressed();
            deleteList();
        } else {
            updateList();
        }
    }

    public class Presenter {

    }
}
