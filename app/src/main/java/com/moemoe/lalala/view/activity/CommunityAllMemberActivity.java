package com.moemoe.lalala.view.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerAllMemberComponent;
import com.moemoe.lalala.di.modules.AllMemberModule;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.presenter.AllMemberContract;
import com.moemoe.lalala.presenter.AllMemberPresenter;
import com.moemoe.lalala.utils.AllMenmberDecoration;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.MenuVItemDecoration;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.SoftKeyboardUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.AllMemBerAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sora on 2018/3/5.
 */

public class CommunityAllMemberActivity extends BaseAppCompatActivity implements AllMemberContract.View {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.tv_menu)
    TextView mTvMenu;
    @BindView(R.id.rl_list)
    PullAndLoadView mListDocs;
    @BindView(R.id.rl_search_root)
    View mSearchRoot;
    @BindView(R.id.ll_search)
    View mLlSearchRoot;
    @BindView(R.id.ll_search_root_1)
    View mLlSearchRoot1;
    @BindView(R.id.iv_search)
    ImageView mIvSearch;
    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.iv_clear)
    ImageView mIvClear;

    @Inject
    AllMemberPresenter mPresenter;

    private String mKeyWord="";
    private boolean isLoading;
    private AllMemBerAdapter adapter;
    private String tagId="";

    @Override
    protected int getLayoutId() {
        return R.layout.ac_all_member_cm;
    }


    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        DaggerAllMemberComponent.builder()
                .allMemberModule(new AllMemberModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        tagId = getIntent().getStringExtra("tagId");

        mListDocs.getRecyclerView().addItemDecoration(new AllMenmberDecoration((int) getResources().getDimension(R.dimen.y1)));
        mListDocs.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllMemBerAdapter();
        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDocs.setLoadMoreEnabled(true);
        mListDocs.getRecyclerView().setAdapter(adapter);

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SoftKeyboardUtils.dismissSoftKeyboard(CommunityAllMemberActivity.this);
                    String curKey = mEtSearch.getText().toString();
                    if (TextUtils.isEmpty(mKeyWord) || (!TextUtils.isEmpty(curKey) && !mKeyWord.equals(mEtSearch.getText().toString()))) {
                        mKeyWord = mEtSearch.getText().toString();
                        mPresenter.loadTagMembers(tagId, 0, mKeyWord);
                    }
                }
                return false;
            }
        });
        mIvClear.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mEtSearch.setText("");
            }
        });
        mSearchRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                int[] location = new int[2];
                mLlSearchRoot.getLocationOnScreen(location);
                ObjectAnimator cardAnimator = ObjectAnimator.ofFloat(mLlSearchRoot, "translationX", mLlSearchRoot.getTranslationX(), -(location[0] - (int) getResources().getDimension(R.dimen.x44))).setDuration(300);
                ObjectAnimator searchAnimator = ObjectAnimator.ofFloat(mIvSearch, "alpha", 0.5f, 1.0f).setDuration(300);
                AnimatorSet set = new AnimatorSet();
                set.play(cardAnimator).with(searchAnimator);
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLlSearchRoot.setVisibility(View.GONE);
                        mLlSearchRoot1.setVisibility(View.VISIBLE);
                        mEtSearch.setFocusable(true);
                        mEtSearch.setFocusableInTouchMode(true);
                        mEtSearch.requestFocus();
                        InputMethodManager imm = (InputMethodManager) CommunityAllMemberActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                set.start();
            }
        });
        mListDocs.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                if (adapter.getList().size()==0){
                    mPresenter.loadTagMembers(tagId, 0, mKeyWord);
                }else {
                    mPresenter.loadTagMembers(tagId,  adapter.getItemCount(), mKeyWord);
                }
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mPresenter.loadTagMembers(tagId, 0, mKeyWord);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return false;
            }
        });

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ViewUtils.toPersonal(CommunityAllMemberActivity.this, adapter.getItem(position).getUserId());
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mPresenter.loadTagMembers(tagId, 0, "");
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
        mTvTitle.setText("所有部员");
        mTvMenu.setText("选择");
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.release();
        }
    }
    @Override
    public void onFailure(int code, String msg) {
        isLoading = false;
        mListDocs.setComplete();
    }

    @Override
    public void onLoadTagMembersSuccess(ArrayList<UserTopEntity> entity, boolean isPull) {
        isLoading = false;
        mListDocs.setComplete();
        if (entity.size() >= ApiService.LENGHT) {
            mListDocs.setLoadMoreEnabled(true);
        } else {
            mListDocs.setLoadMoreEnabled(false);
        }
        if (isPull) {
            adapter.setList(entity);
        } else {
            adapter.addList(entity);
        }
    }
}
