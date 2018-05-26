package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerCommunityV1Component;
import com.moemoe.lalala.di.modules.CommunityModule;
import com.moemoe.lalala.model.entity.AllPersonnelEntity;
import com.moemoe.lalala.model.entity.FeedFollowType2Entity;
import com.moemoe.lalala.model.entity.SimpleUserEntity;
import com.moemoe.lalala.presenter.CommunityContract;
import com.moemoe.lalala.presenter.CommunityPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.DensityUtil;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.NewFolderDecoration;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.CommunityDetailsAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

import org.json.JSONArray;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.moemoe.lalala.utils.StartActivityConstant.REQ_COMMUNITY_INTRODUTION;

/**
 * Created by Sora on 2018/3/2.
 */

public class CommunityDetailsActivity extends BaseAppCompatActivity implements CommunityContract.View {

    @BindView(R.id.tv_title)
    TextView mTitle;
    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    @BindView(R.id.tv_cm_brief_introduction_text)
    TextView mTvntroduction;
    @BindView(R.id.tv_cm_minister)
    TextView mTvMinister;
    @BindView(R.id.ll_minister)
    LinearLayout mLlMinister;
    @BindView(R.id.tv_cm_member)
    TextView mTvMember;
    @BindView(R.id.rl_list)
    RecyclerView mList;
    @BindView(R.id.iv_mask)
    ImageView mIvMask;
    @BindView(R.id.iv_out)
    ImageView mIvOut;
    @BindView(R.id.rl_see_all_member)
    RelativeLayout mRlSeeAll;
    @BindView(R.id.fl_cm_minister)
    FrameLayout mFlMinister;
    @BindView((R.id.tv_cm_edit))
    TextView mTvEdit;
    @BindView((R.id.tv_cm_apple))
    TextView mTvApple;


    @Inject
    CommunityPresenter mPresenter;

    private String id;
    private CommunityDetailsAdapter adapter;
    private ArrayList<SimpleUserEntity> listManagers = new ArrayList<>();
    private ArrayList<SimpleUserEntity> listMembers = new ArrayList<>();
    private boolean join;
    private boolean manager;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_community_deils;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), null);
        AndroidBug5497Workaround.assistActivity(this);
        DaggerCommunityV1Component.builder()
                .communityModule(new CommunityModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        id = getIntent().getStringExtra("uuid");

        mList.setLayoutManager(new GridLayoutManager(this, 6));
        mList.addItemDecoration(new NewFolderDecoration());
        mList.setBackgroundColor(Color.WHITE);
        adapter = new CommunityDetailsAdapter();
        mList.setAdapter(adapter);

        mPresenter.loadData(id + "");
        mPresenter.loadTagAllPersonnel(id);
    }

    private View getMinisterView(final SimpleUserEntity entity, int position) {
        View v = View.inflate(this, R.layout.item_cm_details_minister, null);
        ImageView ivCover = v.findViewById(R.id.iv_cover);
        TextView tvUserName = v.findViewById(R.id.tv_name);
        int size = (DensityUtil.getScreenWidth(this) - (int) getResources().getDimension(R.dimen.y80)) / 6;
        ivCover.setVisibility(View.VISIBLE);
        int w = (DensityUtil.getScreenWidth(this) - (int) getResources().getDimension(R.dimen.y80)) / 6;
        LinearLayout.LayoutParams lp2;
        if (position == 0) {
            lp2 = new LinearLayout.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.setMargins((int) getResources().getDimension(R.dimen.x20), 0, (int) getResources().getDimension(R.dimen.x18), 0);
//            v.setPadding((int) getResources().getDimension(R.dimen.x18), 0, 0, 0);
        } else {
            lp2 = new LinearLayout.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp2.setMargins((int) getResources().getDimension(R.dimen.x18), 0, (int) getResources().getDimension(R.dimen.x18), 0);
            v.setPadding(0, 0, 0, 0);
        }

        v.setLayoutParams(lp2);
        Glide.with(this)
                .load(StringUtils.getUrl(this, entity.getUserIcon(), size, size, false, true))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(ivCover);
        tvUserName.setText(entity.getUserName() + "");
        ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.toPersonal(CommunityDetailsActivity.this, entity.getUserId());
            }
        });
        return v;
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ViewUtils.toPersonal(CommunityDetailsActivity.this, listMembers.get(position).getUserId());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.tv_cm_edit, R.id.tv_cm_apple, R.id.rl_see_all_member, R.id.iv_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_cm_edit:
                String string = mTvntroduction.getText().toString();
                if (!TextUtils.isEmpty(string)) {
                    Intent intent = new Intent(CommunityDetailsActivity.this, CommunityEditActivity.class);
                    intent.putExtra("text", string + "");
                    intent.putExtra("uuid", id);
                    startActivityForResult(intent, REQ_COMMUNITY_INTRODUTION);
                }
                break;
            case R.id.tv_cm_apple:
                ApplyAdminActivity.startActivity(CommunityDetailsActivity.this, id);
                break;
            case R.id.rl_see_all_member:
                Intent intent = new Intent(this, CommunityAllMemberActivity.class);
                intent.putExtra("tagId", id);
                startActivity(intent);
                break;
            case R.id.iv_out:
                final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                alertDialogUtil.createPromptNormalDialog(this, "是否退出社团？");
                alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                    @Override
                    public void CancelOnClick() {
                        alertDialogUtil.dismissDialog();
                    }

                    @Override
                    public void ConfirmOnClick() {
                        alertDialogUtil.dismissDialog();
                        mPresenter.loadTagJoin(id, false);
                    }
                });
                alertDialogUtil.showDialog();

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_COMMUNITY_INTRODUTION) {
            if (resultCode == RESULT_OK && data != null) {
                String extra = data.getStringExtra("text");
                if (!TextUtils.isEmpty(extra)) {
                    mTvntroduction.setText(extra);
                }
            }
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void onLoadListSuccess(FeedFollowType2Entity entity) {
        manager = entity.getManager();
        if (manager) {
            mTvEdit.setVisibility(View.VISIBLE);

        }
        int size = (DensityUtil.getScreenWidth(this) - (int) getResources().getDimension(R.dimen.y80)) / 6;
        Glide.with(this)
                .load(StringUtils.getUrl(this, entity.getBg(), size, size, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new RoundedCornersTransformation(this, getResources().getDimensionPixelSize(R.dimen.y16), 0))
                .into((ImageView) $(R.id.iv_cover));
        int w = DensityUtil.getScreenWidth(this);
        int h = getResources().getDimensionPixelSize(R.dimen.y296);
        Glide.with(this)
                .load(StringUtils.getUrl(this, entity.getBg(), w, h, false, true))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .bitmapTransform(new CropTransformation(this, w, h)
                        , new BlurTransformation(this, 10, 4)
                        , new ColorFilterTransformation(this, ContextCompat.getColor(this, R.color.alpha_20)))
                .into(mIvBg);
        mTitle.setText(entity.getTitle());
        mTitle.setTextColor(getResources().getColor(R.color.white));
        mTvntroduction.setText(entity.getContent());
        join = entity.getJoin();
        if (join) {
            mIvOut.setVisibility(View.VISIBLE);
            if (!manager) {
                mTvApple.setVisibility(View.VISIBLE);
            } else {
                mTvApple.setVisibility(View.GONE);

            }
        } else {
            mIvOut.setVisibility(View.GONE);
            mTvApple.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadTagJoinSuccess(boolean isJoin) {
        showToast("退出成功");
        mIvOut.setVisibility(View.GONE);
        mTvApple.setVisibility(View.GONE);
        join = false;
    }


    @Override
    public void onLoadTagAllPersonnelSuccess(AllPersonnelEntity entity) {
        ArrayList<SimpleUserEntity> managers = entity.getManagers();
        ArrayList<SimpleUserEntity> members = entity.getMembers();
        if (managers == null || managers.size() == 0) {
            mLlMinister.setVisibility(View.GONE);
            mTvMinister.setText("部长(0/5)");
        } else {
            mLlMinister.setVisibility(View.VISIBLE);
            mTvMinister.setText("部长(" + managers.size() + "/5)");
            listManagers = managers;
            for (int i = 0; i < listManagers.size(); i++) {
                mLlMinister.addView(getMinisterView(listManagers.get(i), i));
            }
        }

        if (members == null || members.size() == 0) {
            mList.setVisibility(View.GONE);
            mIvMask.setVisibility(View.GONE);
            mRlSeeAll.setVisibility(View.GONE);
            mTvMember.setText("部员");
        } else {
            mList.setVisibility(View.VISIBLE);
            mIvMask.setVisibility(View.VISIBLE);
            mRlSeeAll.setVisibility(View.VISIBLE);
            mTvMember.setText("部员(" + members.size() + ")");
            int size = members.size();
            int i1 = size > 31 ? 31 : size;
            for (int i = 0; i < i1; i++) {
                listMembers.add(members.get(i));
            }
            adapter.setList(listMembers);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.release();
        }
        stayEvent("社团-社团详情");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime();
    }

    @Override
    public void finish() {
        Intent intent = getIntent();
        intent.putExtra("join", join);
        intent.putExtra("manager", manager);
        setResult(RESULT_OK, intent);
        super.finish();
    }
}
