package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.modules.SearchAllModule;
import com.moemoe.lalala.di.components.DaggerSearchAallComponent;
import com.moemoe.lalala.model.entity.FeedFollowType1Entity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.NewDynamicEntity;
import com.moemoe.lalala.model.entity.RecommendTagEntity;
import com.moemoe.lalala.model.entity.SeachAllEntity;
import com.moemoe.lalala.model.entity.SearchEntity;
import com.moemoe.lalala.model.entity.SearchNormalEntity;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.model.entity.SimpleUserEntity;
import com.moemoe.lalala.model.entity.UserFollowTagEntity;
import com.moemoe.lalala.model.entity.UserTopEntity;
import com.moemoe.lalala.presenter.SearchAllContract;
import com.moemoe.lalala.presenter.SearchAllPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.SoftKeyboardUtils;
import com.moemoe.lalala.utils.StartDecoration;
import com.moemoe.lalala.utils.TopDecoration;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.HotTagAdapter;
import com.moemoe.lalala.view.adapter.SearchAllAdapter;
import com.moemoe.lalala.view.adapter.SearchHistoryAdapter;
import com.moemoe.lalala.view.adapter.SearchTagAdapter;
import com.moemoe.lalala.view.adapter.TagAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.moemoe.lalala.view.widget.recycler.FlowLayoutManager;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;
import com.qiniu.android.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Sora on 2018/3/5.
 */

public class AllSearchActivity extends BaseAppCompatActivity implements SearchAllContract.View {

    public static final String SHOW_ALL = "all";
    public static final String SHOW_DOC = "doc";
    public static final String SHOW_BAG = "folder";
    public static final String SHOW_USER = "user";
    public static final String SHOW_KIRA = "dynamic";

    @BindView(R.id.et_search)
    EditText mEditSearch;
    @BindView(R.id.iv_clear)
    ImageView mIvEditBack;
    @BindView(R.id.tv_Choice_condition)
    TextView mTvCondition;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.ll_history)
    LinearLayout mLlhistory;
    @BindView(R.id.ll_hot)
    LinearLayout mLlHot;
    @BindView(R.id.tv_history_canle)
    TextView mTvHistoryCanle;
    @BindView(R.id.rl_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.list)
    PullAndLoadView mListDosc;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.rl_hot_list)
    RecyclerView mList;
    @BindView(R.id.fl_history)
    FrameLayout mFlHistory;
    @BindView(R.id.rl_plompt)
    RecyclerView mListPlompt;
    @BindView(R.id.rv_all_tag)
    RecyclerView mRvTags;
    @BindView(R.id.view_alpha)
    View mAlpha;

    @Inject
    SearchAllPresenter mPresenter;

    private String showType;
    private String mKeyWord;
    private SearchHistoryAdapter historyAdapter;
    private boolean isSearch;
    private boolean isLoading = false;
    private SearchAllAdapter mAdapter;
    private BottomMenuFragment bottomFragment;
    private HotTagAdapter mHotAdapter;
    private int mCurPage = 1;
    private Gson gson = new Gson();
    private ArrayList<String> strings = new ArrayList<>();
    private ArrayList<String> list;
    private SearchHistoryAdapter plomptAdapter;
    private boolean isChick;
    private SearchTagAdapter tagAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_all_search;
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime();

    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTime();
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        DaggerSearchAallComponent.builder()
                .searchAllModule(new SearchAllModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);

        initPopupMenus();

        isSearch = true;
        isChick = false;
        showType = getIntent().getStringExtra("type");

        mLlSearch.setVisibility(View.VISIBLE);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new TopDecoration(getResources().getDimensionPixelSize(R.dimen.y1)));
        historyAdapter = new SearchHistoryAdapter();
        mRecyclerView.setAdapter(historyAdapter);

        mList.setLayoutManager(new FlowLayoutManager());
        mList.addItemDecoration(new TopDecoration(getResources().getDimensionPixelSize(R.dimen.y24)));
        mList.addItemDecoration(new StartDecoration(getResources().getDimensionPixelSize(R.dimen.x24)));
        mHotAdapter = new HotTagAdapter();
        mList.setAdapter(mHotAdapter);

        mListPlompt.setLayoutManager(new LinearLayoutManager(this));
        mListPlompt.addItemDecoration(new TopDecoration(getResources().getDimensionPixelSize(R.dimen.y1)));
        plomptAdapter = new SearchHistoryAdapter();
        mListPlompt.setAdapter(plomptAdapter);

        mTvHistoryCanle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                alertDialogUtil.createPromptNormalDialog(AllSearchActivity.this, "是否清除记录？");
                alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                    @Override
                    public void CancelOnClick() {
                        alertDialogUtil.dismissDialog();
                    }

                    @Override
                    public void ConfirmOnClick() {
                        strings.clear();
                        mLlhistory.setVisibility(View.GONE);
                        PreferenceUtils.setSearchhistory(AllSearchActivity.this, null);
                        alertDialogUtil.dismissDialog();
                    }
                });
                alertDialogUtil.showDialog();

            }
        });

        mEditSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    SoftKeyboardUtils.dismissSoftKeyboard(AllSearchActivity.this);
                    String curKey = mEditSearch.getText().toString();
                    isSearch = false;
                    mAdapter.setList(null);
                    mKeyWord = "";
                    if (TextUtils.isEmpty(mKeyWord) || (!TextUtils.isEmpty(curKey) && !mKeyWord.equals(mEditSearch.getText().toString()))) {
                        mKeyWord = mEditSearch.getText().toString().trim();
                        strings.add(mKeyWord);
                        if (mAdapter != null) {
                            mLlSearch.setVisibility(View.GONE);
                            mListDosc.setVisibility(View.VISIBLE);
                            mListPlompt.setVisibility(View.GONE);
                            getLoad(mCurPage);
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        mEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isChick) {
                    isChick = false;
                    return;
                }
                String s = editable.toString();
                if (isSearch) {
                    if (!TextUtils.isEmpty(s)) {
                        mLlSearch.setVisibility(View.GONE);
                        mListDosc.setVisibility(View.GONE);
                        mListPlompt.setVisibility(View.VISIBLE);
                        mPresenter.loadKeyWordTag(s);
                    } else {
                        if (isSearch) {
                            mLlSearch.setVisibility(View.VISIBLE);
                            if (mHotAdapter.getList().size() > 0) {
                                mLlHot.setVisibility(View.VISIBLE);
                            }
                            if (historyAdapter.getList().size() > 0) {
                                mFlHistory.setVisibility(View.VISIBLE);
                            }
                            mListDosc.setVisibility(View.GONE);
                        } else {
                            mLlSearch.setVisibility(View.GONE);
                            mListPlompt.setVisibility(View.GONE);
                            mListDosc.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    mLlSearch.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(s)) {
                        mListDosc.setVisibility(View.GONE);
                        mListPlompt.setVisibility(View.VISIBLE);
                        mPresenter.loadKeyWordTag(s);
                    } else {
                        mListDosc.setVisibility(View.VISIBLE);
                        mListPlompt.setVisibility(View.GONE);
                    }
                }
            }
        });
        mListDosc.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mListDosc.setLayoutManager(new LinearLayoutManager(this));
        mListDosc.setLoadMoreEnabled(false);
        mListDosc.getRecyclerView().addItemDecoration(new TopDecoration(getResources().getDimensionPixelSize(R.dimen.y1)));
        mAdapter = new SearchAllAdapter();
        mListDosc.getRecyclerView().setAdapter(mAdapter);
        mListDosc.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                if (showType.equals(SHOW_ALL)) {

                } else {
                    if (mAdapter.getList().size() == 0) {
                        getLoad(mCurPage);
                    } else {
                        mCurPage++;
                        getLoad(mAdapter.getItemCount());
                    }
                }

            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mCurPage = 1;
                getLoad(mCurPage);
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
        isLoading = true;
        mPresenter.loadRecommendTagV2("bag");


        if (showType.equals(SHOW_ALL)) {
            mTvCondition.setText("全部");
            mEditSearch.setHint("搜索你感兴趣的关键词");
        } else if (showType.equals(SHOW_DOC)) {
            mTvCondition.setText("帖子");
            mEditSearch.setHint("搜索相关的帖子、文章");
        } else if (showType.equals(SHOW_BAG)) {
            mTvCondition.setText("书包");
            mEditSearch.setHint("搜索书包、视频、音乐等资源");
        } else if (showType.equals(SHOW_USER)) {
            mTvCondition.setText("同学");
            mEditSearch.setHint("搜索Kira号或名称");
        } else if (showType.equals(SHOW_KIRA)) {
            mTvCondition.setText("动态");
            mEditSearch.setHint("搜索相关的动态内容");
        }
        if (!TextUtils.isEmpty(PreferenceUtils.getSearchhistory(this))) {
            mLlhistory.setVisibility(View.VISIBLE);
            if (historyAdapter != null) {
                String searchhistory = PreferenceUtils.getSearchhistory(this);
                HashSet<String> set = gson.fromJson(searchhistory, HashSet.class);
                if (set.size() > 0 || set != null) {
                    for (String history : set) {
                        strings.add(history);
                    }
                }
                historyAdapter.setList(strings);
            }
        } else {
            mLlhistory.setVisibility(View.GONE);
        }

        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clickEvent("搜索结果列表");
                SearchNormalEntity item = mAdapter.getItem(position);
                if ("top".equals(item.getType())) {
                    //do nothing
                } else if ("user".equals(item.getType())) {
                    //UserTopEntity
                    UserTopEntity userTopEntity = gson.fromJson(item.getData(), UserTopEntity.class);
                    ViewUtils.toPersonal(AllSearchActivity.this, userTopEntity.getUserId());
                } else if ("doc".equals(item.getType()) || "folder".equals(item.getType())) {
                    //ShowFolderEntity
                    ShowFolderEntity entity = gson.fromJson(item.getData(), ShowFolderEntity.class);
                    if (entity.getType().equals(FolderType.ZH.toString())) {
                        NewFileCommonActivity.startActivity(AllSearchActivity.this, FolderType.ZH.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (entity.getType().equals(FolderType.TJ.toString())) {
                        NewFileCommonActivity.startActivity(AllSearchActivity.this, FolderType.TJ.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (entity.getType().equals(FolderType.MH.toString())) {
                        NewFileManHuaActivity.startActivity(AllSearchActivity.this, FolderType.MH.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (entity.getType().equals(FolderType.XS.toString())) {
                        NewFileXiaoshuoActivity.startActivity(AllSearchActivity.this, FolderType.XS.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (entity.getType().equals(FolderType.YY.toString())) {
                        FileMovieActivity.startActivity(AllSearchActivity.this, FolderType.YY.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (entity.getType().equals(FolderType.SP.toString())) {
                        FileMovieActivity.startActivity(AllSearchActivity.this, FolderType.SP.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (entity.getType().equals(FolderType.WZ.toString())) {
                        Intent i = new Intent(AllSearchActivity.this, NewDocDetailActivity.class);
                        i.putExtra("uuid", entity.getFolderId());
                        i.putExtra("title","搜索");
                        startActivity(i);
                    } else if ("MOVIE".equals(entity.getType())) {
                        KiraVideoActivity.startActivity(AllSearchActivity.this, entity.getUuid(), entity.getFolderId(), entity.getFolderName(), entity.getCover());
                    } else if ("MUSIC".equals(entity.getType())) {
                        KiraMusicActivity.startActivity(AllSearchActivity.this, entity.getUuid(), entity.getFolderId(), entity.getFolderName(), entity.getCover());
                    }
                } else {
                    DynamicActivity.startActivity(AllSearchActivity.this, gson.fromJson(item.getData(), NewDynamicEntity.class));
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        String[] strings = {"全部", "帖子", "书包", "同学", "动态"};
        ArrayList<UserFollowTagEntity> entities = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            UserFollowTagEntity entity = new UserFollowTagEntity();
            entity.setText(strings[i]);

            if (i == 0) {
                entity.setId(SHOW_ALL);
            } else if (i == 1) {
                entity.setId(SHOW_DOC);
            } else if (i == 2) {
                entity.setId(SHOW_BAG);
            } else if (i == 3) {
                entity.setId(SHOW_USER);
            } else if (i == 4) {
                entity.setId(SHOW_KIRA);
            }

            if (entity.getId().equals(showType)){
                entity.setSelect(true);
            }else {
                entity.setSelect(false);
            }
            entities.add(entity);
        }
        tagAdapter = new SearchTagAdapter();
        tagAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < tagAdapter.getList().size(); i++) {
                    tagAdapter.getList().get(i).setSelect(false);
                }
                tagAdapter.getList().get(position).setSelect(true);
                showType = tagAdapter.getList().get(position).getId();
                if (showType.equals(SHOW_ALL)) {
                    mTvCondition.setText("全部");
                    mEditSearch.setHint("搜索你感兴趣的关键词");
                } else if (showType.equals(SHOW_DOC)) {
                    mTvCondition.setText("帖子");
                    mEditSearch.setHint("搜索相关的帖子、文章");
                } else if (showType.equals(SHOW_BAG)) {
                    mTvCondition.setText("书包");
                    mEditSearch.setHint("搜索书包、视频、音乐等资源");
                } else if (showType.equals(SHOW_USER)) {
                    mTvCondition.setText("同学");
                    mEditSearch.setHint("搜索Kira号或名称");
                } else if (showType.equals(SHOW_KIRA)) {
                    mTvCondition.setText("动态");
                    mEditSearch.setHint("搜索相关的动态内容");
                }
                tagAdapter.notifyDataSetChanged();
                clickEvent("搜索-标签");
                mCurPage = 1;
                if (isSearch) {
                } else {
                    getLoad(1);
                }
                if (mRvTags.getVisibility() == View.VISIBLE) {
                    mRvTags.setVisibility(View.GONE);
                    mAlpha.setVisibility(View.GONE);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mRvTags.setLayoutManager(new FlowLayoutManager());
        mRvTags.setAdapter(tagAdapter);
        tagAdapter.setList(entities);

    }

    private void getLoad(int page) {
        SearchEntity searchEntity = new SearchEntity(page, mKeyWord);
        mPresenter.loadSearchAllList(searchEntity, showType);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (bottomFragment != null) bottomFragment.show(getSupportFragmentManager(), "Bag");
                if (mRvTags.getVisibility() == View.VISIBLE) {
                    mRvTags.setVisibility(View.GONE);
                    mAlpha.setVisibility(View.GONE);
                } else {
                    mRvTags.setVisibility(View.VISIBLE);
                    mAlpha.setVisibility(View.VISIBLE);
                }


            }
        });
        mIvEditBack.setOnClickListener(new NoDoubleClickListener(2000) {
            @Override
            public void onNoDoubleClick(View v) {
                mEditSearch.setText("");
            }
        });
        mAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRvTags.getVisibility() == View.VISIBLE) {
                    mRvTags.setVisibility(View.GONE);
                    mAlpha.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initPopupMenus() {
        bottomFragment = new BottomMenuFragment();
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item = new MenuItem(0, "全部");
        items.add(item);

        item = new MenuItem(1, getString(R.string.label_doc));
        items.add(item);

        item = new MenuItem(2, getString(R.string.label_bag));
        items.add(item);

        item = new MenuItem(3, "同学");
        items.add(item);

        item = new MenuItem(4, "动态");
        items.add(item);

        bottomFragment.setShowTop(false);
        bottomFragment.setMenuItems(items);
        bottomFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        bottomFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {

                clickEvent("搜索-标签");
                mCurPage = 1;
                if (itemId == 0) {
                    mTvCondition.setText("全部");
                    showType = SHOW_ALL;
                    mEditSearch.setHint("搜索你感兴趣的关键词");
                } else if (itemId == 1) {
                    mTvCondition.setText("帖子");
                    showType = SHOW_DOC;
                    mEditSearch.setHint("搜索相关的帖子、文章");
                } else if (itemId == 2) {
                    mTvCondition.setText("书包");
                    showType = SHOW_BAG;
                    mEditSearch.setHint("搜索书包、视频、音乐等资源");
                } else if (itemId == 3) {
                    mTvCondition.setText("同学");
                    showType = SHOW_USER;
                    mEditSearch.setHint("搜索Kira号或名称");
                } else if (itemId == 4) {
                    mTvCondition.setText("动态");
                    showType = SHOW_KIRA;
                    mEditSearch.setHint("搜索相关的动态内容");
                }
                if (isSearch) {
                } else {
                    getLoad(1);
                }

            }
        });
    }

    @Override
    protected void initListeners() {
        mHotAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clickEvent("搜索-热门搜索");
                if (isSearch) {
                    isChick = true;
                }
                isSearch = false;
                mLlSearch.setVisibility(View.GONE);
                mListDosc.setVisibility(View.VISIBLE);
                mKeyWord = mHotAdapter.getItem(position).getWord();
                mEditSearch.setText(mKeyWord);
                mEditSearch.setSelection(mKeyWord.length());
                strings.add(mKeyWord);
                getLoad(1);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        historyAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                clickEvent("搜索-历史搜索");
                if (isSearch) {
                    isChick = true;
                }
                isSearch = false;
                mLlSearch.setVisibility(View.GONE);
                mListDosc.setVisibility(View.VISIBLE);
                mKeyWord = historyAdapter.getItem(position).toString().trim();
                mEditSearch.setText(mKeyWord);
                mEditSearch.setSelection(mKeyWord.length());
                getLoad(1);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        plomptAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                isChick = true;
                isSearch = false;
                mLlSearch.setVisibility(View.GONE);
                mListPlompt.setVisibility(View.GONE);
                mListDosc.setVisibility(View.VISIBLE);
                mKeyWord = plomptAdapter.getItem(position).toString().trim();
                mEditSearch.setText(mKeyWord);
                mEditSearch.setSelection(mKeyWord.length());
                getLoad(1);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stayEvent("搜索");
        if (strings.size() > 0) {
            Set<String> history = new HashSet<>();
            for (int i = 0; i < strings.size(); i++) {
                history.add(strings.get(i));
            }
            PreferenceUtils.setSearchhistory(AllSearchActivity.this, gson.toJson(history));
        }
        if (mPresenter != null) {
            mPresenter.release();
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {
        isLoading = false;
        mListDosc.setComplete();
//        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void onLoadSearchAllListSuccess(SeachAllEntity entity, String type) {
        isLoading = false;
        mListDosc.setComplete();

        ArrayList<SearchNormalEntity> list = new ArrayList<>();
        if (showType.equals("all")) {
            mListDosc.setLoadMoreEnabled(false);
            mCurPage = 1;
            if (entity.getUser() != null && entity.getUser().size() > 0) {
                SearchNormalEntity entity1 = new SearchNormalEntity();
                entity1.setType("top");
                String userKira = "同学(昵称、Kira号)";
                entity1.setData(userKira);
                list.add(entity1);
                for (int i = 0; i < entity.getUser().size(); i++) {
                    SearchNormalEntity entity2 = new SearchNormalEntity();
                    entity2.setType("user");
                    String asJsonObject = gson.toJson(entity.getUser().get(i));
                    entity2.setData(asJsonObject);
                    list.add(entity2);
                }
            }
            if (entity.getDoc() != null && entity.getDoc().size() > 0) {
                SearchNormalEntity entity1 = new SearchNormalEntity();
                entity1.setType("top");
                String userKira = "帖子文章";
                entity1.setData(userKira);
                list.add(entity1);
                for (int i = 0; i < entity.getDoc().size(); i++) {
                    SearchNormalEntity entity2 = new SearchNormalEntity();
                    entity2.setType("doc");
                    String asJsonObject = gson.toJson(entity.getDoc().get(i));
                    entity2.setData(asJsonObject);
                    list.add(entity2);
                }
            }
            if (entity.getFolder() != null && entity.getFolder().size() > 0) {
                SearchNormalEntity entity1 = new SearchNormalEntity();
                entity1.setType("top");
                String userKira = "书包(视频、音乐、美图等资源)";
                entity1.setData(userKira);
                list.add(entity1);
                for (int i = 0; i < entity.getFolder().size(); i++) {
                    SearchNormalEntity entity2 = new SearchNormalEntity();
                    entity2.setType("folder");
                    String asJsonObject = gson.toJson(entity.getFolder().get(i));
                    entity2.setData(asJsonObject);
                    list.add(entity2);
                }
            }
            if (entity.getDynamic() != null && entity.getDynamic().size() > 0) {
                SearchNormalEntity entity1 = new SearchNormalEntity();
                entity1.setType("top");
                String userKira = "动态";
                entity1.setData(userKira);
                list.add(entity1);
                for (int i = 0; i < entity.getDynamic().size(); i++) {
                    SearchNormalEntity entity2 = new SearchNormalEntity();
                    entity2.setType("dynamic");
                    String asJsonObject = gson.toJson(entity.getDynamic().get(i));
                    entity2.setData(asJsonObject);
                    list.add(entity2);
                }
            }
        } else {
            mListDosc.setLoadMoreEnabled(true);
            if (entity.getUser() != null && entity.getUser().size() > 0) {
                for (int i = 0; i < entity.getUser().size(); i++) {
                    SearchNormalEntity entity2 = new SearchNormalEntity();
                    entity2.setType("user");
                    String asJsonObject = gson.toJson(entity.getUser().get(i));
                    entity2.setData(asJsonObject);
                    list.add(entity2);
                }
            }
            if (entity.getDoc() != null && entity.getDoc().size() > 0) {
                for (int i = 0; i < entity.getDoc().size(); i++) {
                    SearchNormalEntity entity2 = new SearchNormalEntity();
                    entity2.setType("doc");
                    String asJsonObject = gson.toJson(entity.getDoc().get(i));
                    entity2.setData(asJsonObject);
                    list.add(entity2);
                }
            }
            if (entity.getFolder() != null && entity.getFolder().size() > 0) {
                for (int i = 0; i < entity.getFolder().size(); i++) {
                    SearchNormalEntity entity2 = new SearchNormalEntity();
                    entity2.setType("folder");
                    String asJsonObject = gson.toJson(entity.getFolder().get(i));
                    entity2.setData(asJsonObject);
                    list.add(entity2);
                }
            }
            if (entity.getDynamic() != null && entity.getDynamic().size() > 0) {
                for (int i = 0; i < entity.getDynamic().size(); i++) {
                    SearchNormalEntity entity2 = new SearchNormalEntity();
                    entity2.setType("dynamic");
                    String asJsonObject = gson.toJson(entity.getDynamic().get(i));
                    entity2.setData(asJsonObject);
                    list.add(entity2);
                }
            }
        }
        if (mCurPage == 1) {
            if (list.size() > 0) {
                mAdapter.setList(list);
            } else {
                mAdapter.setList(null);
            }
        } else {
            if (list.size() > 0) {
                mAdapter.addList(list);
            }
        }

    }

    public void goItemLoad(String type) {
        mCurPage = 1;
        showType = type;
        getLoad(mCurPage);
        if (type.equals(SHOW_ALL)) {
            mTvCondition.setText("全部");
        } else if (type.equals(SHOW_DOC)) {
            mTvCondition.setText("帖子");
        } else if (type.equals(SHOW_BAG)) {
            mTvCondition.setText("书包");
        } else if (type.equals(SHOW_USER)) {
            mTvCondition.setText("同学");
        } else if (type.equals(SHOW_KIRA)) {
            mTvCondition.setText("动态");
        }
    }

    @Override
    public void onLoadRecommendTagV2Success(ArrayList<RecommendTagEntity> entities) {
        if (entities.size() > 0) {
            mLlHot.setVisibility(View.VISIBLE);
            mHotAdapter.setList(entities);
        } else {
            mLlHot.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadRecommendTagSuccess(ArrayList<RecommendTagEntity> entities) {

    }

    @Override
    public void onLoadKeyWordTagSuccess(ArrayList<RecommendTagEntity> entities) {
        if (entities.size() > 0) {
            list = new ArrayList<>();
            for (RecommendTagEntity entity : entities) {
                list.add(entity.getWord());
            }
            plomptAdapter.setList(list);
        }
    }

    public void likeDynamic(String id, boolean isLie, int position) {
        mPresenter.likeDynamic(id, isLie, position);
    }

    @Override
    public void onLikeDynamicSuccess(boolean isLike, int position) {
        NewDynamicEntity entity = new Gson().fromJson(mAdapter.getList().get(position).getData(), NewDynamicEntity.class);
        entity.setThumb(isLike);
        if (isLike) {
            SimpleUserEntity userEntity = new SimpleUserEntity();
            userEntity.setUserName(PreferenceUtils.getAuthorInfo().getUserName());
            userEntity.setUserId(PreferenceUtils.getUUid());
            userEntity.setUserIcon(PreferenceUtils.getAuthorInfo().getHeadPath());
            entity.getThumbUsers().add(0, userEntity);
            entity.setThumbs(entity.getThumbs() + 1);
        } else {
            for (SimpleUserEntity userEntity : entity.getThumbUsers()) {
                if (userEntity.getUserId().equals(PreferenceUtils.getUUid())) {
                    entity.getThumbUsers().remove(userEntity);
                    break;
                }
            }
            entity.setThumbs(entity.getThumbs() - 1);
        }
        Gson gson = new Gson();
        String newObj = gson.toJson(entity);
        mAdapter.getList().get(position).setData(newObj);
        if (mAdapter.getHeaderLayoutCount() != 0) {
            mAdapter.notifyItemChanged(position + 1);
        } else {
            mAdapter.notifyItemChanged(position);
        }
    }

}
