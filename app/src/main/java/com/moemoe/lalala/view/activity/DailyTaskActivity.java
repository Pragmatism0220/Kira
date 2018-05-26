package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerPhoneMainComponent;
import com.moemoe.lalala.di.modules.PhoneMainModule;
import com.moemoe.lalala.model.entity.DailyTaskEntity;
import com.moemoe.lalala.model.entity.PersonalMainEntity;
import com.moemoe.lalala.model.entity.SignEntity;
import com.moemoe.lalala.presenter.PhoneMainContract;
import com.moemoe.lalala.presenter.PhoneMainPresenter;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.DailTaskDecoration;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sora on 2018/3/24.
 */

public class DailyTaskActivity extends BaseAppCompatActivity implements PhoneMainContract.View, View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTitle;
    @BindView(R.id.tv_menu)
    TextView mTvSelect;
    @BindView(R.id.tv_level)
    TextView mTvLevel;
    @BindView(R.id.tv_coin)
    TextView mTvCoin;
    @BindView(R.id.rv_sign_info)
    RecyclerView mRvSignList;
    @BindView(R.id.iv_sign)
    ImageView mSign;
    @BindView(R.id.tv_total_score)
    TextView mTotalScore;
    @BindView(R.id.pb_score)
    ProgressBar mProgress;
    @BindView(R.id.rv_task_list)
    RecyclerView mRvTaskList;

    @Inject
    PhoneMainPresenter mPresenter;

    private boolean mIsSignPress;
    private boolean mIsSign;
    private DailyTaskEntity mEntity;
    private SimpleAdapter mAdapter;
    private TaskAdapter mTaskAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DailyTaskActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ac_daily_task;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        DaggerPhoneMainComponent.builder()
                .phoneMainModule(new PhoneMainModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mIsSignPress = true;
        mPresenter.getDailyTask();
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitle.setText("每日任务");
        mTitle.setTextColor(ContextCompat.getColor(this, R.color.main_cyan));

        /**
         * 2018年5月2日取消说明按钮
         *
         */
//       mTvSelect.setVisibility(View.GONE);
//        mTvSelect.setText("说明");
//        mTvSelect.setOnClickListener(this);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTvSelect.getLayoutParams();
        layoutParams.setMargins(0, 0, (int) getResources().getDimension(R.dimen.y24), 0);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {
        mIsSignPress = false;
    }

    @Override
    public void onDailyTaskLoad(DailyTaskEntity entity) {
        mIsSignPress = false;

        mEntity = entity;
        LinearLayoutManager l1 = new LinearLayoutManager(this);
        mRvTaskList.setLayoutManager(l1);
        mRvTaskList.setHasFixedSize(true);
        mTaskAdapter = new TaskAdapter();
        mRvTaskList.setAdapter(mTaskAdapter);

        LinearLayoutManager l = new LinearLayoutManager(this);
        l.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRvSignList.setHasFixedSize(true);
        mRvSignList.setLayoutManager(l);
        mTvLevel.setText(getString(R.string.label_level, mEntity.getLevel()));
        mTvCoin.setText("" + mEntity.getSignCoin());
        mTotalScore.setText(mEntity.getNowScore() + "/" + mEntity.getUpperLimit());
        mAdapter = new SimpleAdapter();
        mRvSignList.addItemDecoration(new DailTaskDecoration());
        mRvSignList.setAdapter(mAdapter);
        mSign.setOnClickListener(this);

        mProgress.setMax(mEntity.getUpperLimit());
        mProgress.setProgress(mEntity.getNowScore());
        mIsSign = mEntity.isCheckState();
        if (mIsSign) {
            mSign.setImageResource(R.drawable.btn_signmain_signed);
        } else {
            mSign.setImageResource(R.drawable.btn_sign);
        }
    }

    @Override
    public void changeSignState(SignEntity entity, boolean sign) {
        if (sign) showToast(R.string.label_sign_suc);
        mIsSign = true;
        mEntity.setCheckState(mIsSign);
        changeSignState(entity.getDay());
    }

    public void changeSignState(int mDay) {
        if (mIsSign) {
            mSign.setEnabled(false);
        } else {
            mSign.setEnabled(true);
        }
        mAdapter.setCoin(mDay);
    }

    @Override
    public void onPersonMainLoad(PersonalMainEntity entity) {
        PersonalLevelActivity.startActivity(this, entity.getLevelName(), entity.getLevelColor(), entity.getScore(), entity.getLevelScoreStart(), entity.getLevelScoreEnd(), entity.getLevel());

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_sign) {
            if (NetworkUtils.checkNetworkAndShowError(DailyTaskActivity.this)) {
                mPresenter.signToday(null);
            }
        } else if (id == R.id.tv_menu) {
            mPresenter.requestPersonMain();
        }
    }


    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

        public TaskAdapter() {
        }

        @Override
        public TaskAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TaskAdapter.TaskViewHolder(LayoutInflater.from(DailyTaskActivity.this).inflate(R.layout.item_daily_task, parent, false));
        }

        @Override
        public void onBindViewHolder(final TaskAdapter.TaskViewHolder holder, int position) {
            final DailyTaskEntity.TaskItem taskItem = mEntity.getItems().get(position);
            holder.title.setText(taskItem.getTaskName());
            holder.desc.setText(taskItem.getDesc());
            holder.score.setText(taskItem.getNowScore() + "");
            holder.limit.setText(getString(R.string.label_task_limit, taskItem.getUpperLimit()));
            if (taskItem.getNowScore() == taskItem.getUpperLimit()) {
                holder.title.setSelected(true);
                holder.score.setSelected(true);
                holder.limit.setSelected(true);
                holder.goContent.setText("已完成");
                holder.goContent.setTextColor(ContextCompat.getColor(DailyTaskActivity.this, R.color.white));
                holder.goContent.setSelected(true);
            } else {
                holder.title.setSelected(false);
                holder.score.setSelected(false);
                holder.limit.setSelected(false);
                holder.goContent.setSelected(false);
                holder.goContent.setText("前往完成");
                holder.goContent.setTextColor(ContextCompat.getColor(DailyTaskActivity.this, R.color.main_cyan));
            }

            holder.goContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.goContent.getText().toString().equals("前往完成")) {
                        if (taskItem.getTaskName().contains("动态")) {
                            Intent i5 = new Intent(DailyTaskActivity.this, NewDynamicActivity.class);
                            startActivity(i5);
                            finish();
                        } else if (taskItem.getTaskName().contains("帖") || taskItem.getTaskName().contains("标签")) {
                            Intent i3 = new Intent(DailyTaskActivity.this, FeedV3Activity.class);
                            startActivity(i3);
                            finish();
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mEntity.getItems().size();
        }

        public void setCoin(int day) {
            mEntity.setSignDay(day);
            notifyDataSetChanged();
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {

            public TextView title, desc, score, limit, goContent;

            public TaskViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.tv_title);
                desc = (TextView) itemView.findViewById(R.id.tv_desc);
                score = (TextView) itemView.findViewById(R.id.tv_score);
                limit = (TextView) itemView.findViewById(R.id.tv_limit);
                goContent = itemView.findViewById(R.id.tv_go_content);
            }
        }
    }

    public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder> {

        public SimpleAdapter() {
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SimpleViewHolder(LayoutInflater.from(DailyTaskActivity.this).inflate(R.layout.item_sign_info, parent, false));
        }

        @Override
        public void onBindViewHolder(SimpleViewHolder holder, int position) {
            DailyTaskEntity.SignItem signItem = mEntity.getSignItem().get(position);
            if (position < mEntity.getSignDay()) {
                holder.root.setSelected(true);
                holder.score.setTextColor(getResources().getColor(R.color.white));
                holder.coin.setTextColor(getResources().getColor(R.color.white));
            } else {
                holder.root.setSelected(false);
                holder.score.setTextColor(getResources().getColor(R.color.pink_fb7ba2));
                holder.coin.setTextColor(getResources().getColor(R.color.pink_fb7ba2));
            }
            holder.score.setText(getString(R.string.label_sign_info_score, signItem.getScore()));
            holder.coin.setText(getString(R.string.label_sign_info_coin, signItem.getCoin()));
        }

        @Override
        public int getItemCount() {
            return mEntity.getSignItem().size();
        }

        public void setCoin(int day) {
            mEntity.setSignDay(day);
            notifyDataSetChanged();
        }

        public class SimpleViewHolder extends RecyclerView.ViewHolder {

            public LinearLayout root;
            public TextView score, coin;

            public SimpleViewHolder(View itemView) {
                super(itemView);
                root = (LinearLayout) itemView.findViewById(R.id.ll_root);
                score = (TextView) itemView.findViewById(R.id.tv_score);
                coin = (TextView) itemView.findViewById(R.id.tv_coin);
            }
        }
    }
}
