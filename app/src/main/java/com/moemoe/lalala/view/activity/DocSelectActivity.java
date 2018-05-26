package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerSelectTagComponent;
import com.moemoe.lalala.di.modules.SelectTagModule;
import com.moemoe.lalala.model.entity.OfficialTag;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.presenter.SelectTagContract;
import com.moemoe.lalala.presenter.SelectTagPresenter;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.DocSelectDecoration;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.DocSelecttAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sora on 2018/3/7.
 */

public class DocSelectActivity extends BaseAppCompatActivity implements SelectTagContract.View {

    @BindView(R.id.rv_list)
    PullAndLoadView mListDosc;
    @BindView(R.id.iv_back)
    ImageView mIVBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.tv_menu)
    TextView mTVMenu;
    private DocSelecttAdapter adapter;


    @Inject
    SelectTagPresenter mPresenter;
    private ArrayList<UserFollowTagEntity> officialTags;
    private String type="";
    private View inflate;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_bar_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        DaggerSelectTagComponent.builder()
                .selectTagModule(new SelectTagModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);

        type = getIntent().getStringExtra("uuid");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mListDosc.setLayoutManager(layoutManager);
        mListDosc.getRecyclerView().addItemDecoration(new DocSelectDecoration());
        mListDosc.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_e8e8e8));
        mListDosc.getSwipeRefreshLayout().setEnabled(false);
        mListDosc.setLoadMoreEnabled(false);
        adapter = new DocSelecttAdapter();
        mListDosc.getRecyclerView().setAdapter(adapter);
        inflate = LayoutInflater.from(this).inflate(R.layout.my_community, null);
        adapter.addHeaderView(inflate);
        mPresenter.loadTagAllList();
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIVBack.setVisibility(View.VISIBLE);
        mIVBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvTitle.setText("选择社团");
        mTvTitle.setTextColor(getResources().getColor(R.color.black));

        mTVMenu.setText("确定");
        mTVMenu.setVisibility(View.VISIBLE);
        ViewUtils.setRightMargins(mTVMenu, (int) getResources().getDimension(R.dimen.x36));
        mTVMenu.getPaint().setFakeBoldText(true);
    }

    @Override
    protected void initListeners() {
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (officialTags != null) {
                    UserFollowTagEntity adapterItem = adapter.getItem(position);
                    boolean select = adapterItem.getSelect();
                    if (select) {
                        return;
                    } else {
                        for (int i = 0; i < officialTags.size(); i++) {
                            if (position == i) {
                                officialTags.get(i).setSelect(!select);
                            } else {
                                officialTags.get(i).setSelect(false);
                            }
                        }
                        adapter.setList(officialTags);
                    }

                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        mTVMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < officialTags.size(); i++) {
                    boolean select = officialTags.get(i).getSelect();
                    if (select) {
                        String text = officialTags.get(i).getText();
                        String id = officialTags.get(i).getId();
                        Intent intent = new Intent();
                        intent.putExtra("uuid", id);
                        intent.putExtra("context", text);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {

    }

    @Override
    public void onLoadOfficialTags(ArrayList<OfficialTag> tags) {

    }

    @Override
    public void onLoadOfficialV2Tags(ArrayList<OfficialTag> tags) {
        if (tags == null || tags.size() == 0) {
            inflate.setVisibility(View.GONE);
        } else {
            inflate.setVisibility(View.VISIBLE);
            officialTags = new ArrayList<>();
            for (int i = 0; i < tags.size(); i++) {
                if ("all".equals(tags.get(i).getId())) {
                    ArrayList<UserFollowTagEntity> tagThi = tags.get(i).getTagSec().get(0).getTagThi();
                    for (int j = 0; j < tagThi.size(); j++) {
                        if (!TextUtils.isEmpty(type)){
                            if (type.equals(tagThi.get(j).getId())) {
                                tagThi.get(j).setSelect(true);
                            } else {
                                tagThi.get(j).setSelect(false);
                            }
                        }
                    }
                    officialTags = tagThi;
                }
            }
            if (officialTags.size() > 0) {
                adapter.setList(officialTags);
            }
        }
    }

    @Override
    public void onSaveUserTagsSuccess() {

    }
}
