package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerNewFolderComponent;
import com.moemoe.lalala.di.modules.NewFolderModule;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.LibraryContribute;
import com.moemoe.lalala.model.entity.ShowFolderEntity;
import com.moemoe.lalala.presenter.NewFolderContract;
import com.moemoe.lalala.presenter.NewFolderPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FolderDecoration;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.BagCollectionTopAdapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;
import com.moemoe.lalala.view.widget.recycler.PullCallback;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import io.rong.imlib.IGetNotificationQuietHoursCallback;

import static com.moemoe.lalala.utils.StartActivityConstant.REQ_LIBRARY_TOUGAO;

/**
 * 新文件夹界面
 * Created by yi on 2017/8/18.
 */

public class NewFolderActivity extends BaseAppCompatActivity implements NewFolderContract.View {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_left_menu)
    TextView mTvMenuLeft;
    @BindView(R.id.tv_toolbar_title)
    TextView mTitle;
    @BindView(R.id.iv_menu_list)
    ImageView mIvMenu;
    @BindView(R.id.tv_menu)
    TextView mTvMenuRight;
    @BindView(R.id.tv_right_menu)
    TextView mTvTop;
    @BindView(R.id.list)
    PullAndLoadView mListDocs;
    @BindView(R.id.iv_add_folder)
    ImageView mIvAdd;
    @BindView(R.id.tv_show_sp_examine)
    TextView mExamineSp;
    @Inject
    NewFolderPresenter mPresenter;

    private BagCollectionTopAdapter mAdapter;
    private String mFolderType;
    private String mType;
    private String mUserId;
    private boolean isLoading = false;
    private BottomMenuFragment bottomMenuFragment;
    private boolean mIsSelect;
    private HashMap<Integer, ShowFolderEntity> mSelectMap;
    private boolean isSubmission;
    private String departmentId;
    private String collection;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_folder;
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
        super.onDestroy();
    }

    public static void startActivity(Context context, String userId, String folderType, String type) {
        Intent i = new Intent(context, NewFolderActivity.class);
        i.putExtra(UUID, userId);
        i.putExtra("folderType", folderType);
        i.putExtra("type", type);
        context.startActivity(i);
    }

    public static void startActivity(Context context, String userId, String folderType, String type, boolean isSubmission, String departmentId) {
        Intent i = new Intent(context, NewFolderActivity.class);
        i.putExtra(UUID, userId);
        i.putExtra("folderType", folderType);
        i.putExtra("type", type);
        i.putExtra("isSubmission", isSubmission);
        i.putExtra("departmentId", departmentId);
        ((BaseAppCompatActivity) context).startActivityForResult(i, REQ_LIBRARY_TOUGAO);
    }

    public static void startActivity(Context context, String userId, String folderType, String type, boolean isSubmission, String departmentId, String collection) {
        Intent i = new Intent(context, NewFolderActivity.class);
        i.putExtra(UUID, userId);
        i.putExtra("folderType", folderType);
        i.putExtra("type", type);
        i.putExtra("isSubmission", isSubmission);
        i.putExtra("departmentId", departmentId);
        i.putExtra("collection", collection);
        ((BaseAppCompatActivity) context).startActivityForResult(i, REQ_LIBRARY_TOUGAO);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerNewFolderComponent.builder()
                .newFolderModule(new NewFolderModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        mUserId = getIntent().getStringExtra(UUID);
        mFolderType = getIntent().getStringExtra("folderType");
        mType = getIntent().getStringExtra("type");
        isSubmission = getIntent().getBooleanExtra("isSubmission", false);
        departmentId = getIntent().getStringExtra("departmentId");
        collection = getIntent().getStringExtra("collection");
        mIsSelect = false;
        String title = "";
        mSelectMap = new HashMap<>();
        mTvTop.setText("置顶");
        mTvTop.setTextColor(ContextCompat.getColor(this, R.color.txt_gray_main));
        if (mFolderType.equals(FolderType.ZH.toString())) {
            title = "综合";
        } else if (mFolderType.equals(FolderType.TJ.toString())) {
            title = "图集";
        } else if (mFolderType.equals(FolderType.MH.toString())) {
            title = "漫画";
        } else if (mFolderType.equals(FolderType.XS.toString())) {
            title = "小说";
        } else if (mFolderType.equals(FolderType.WZ.toString())) {
            title = "文章";
        } else if (mFolderType.equals(FolderType.YY.toString())) {
            title = "音乐集";
        } else if (mFolderType.equals(FolderType.SP.toString())) {
            title = "视频";
            mExamineSp.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    VideoExamineActivity.startActivity(NewFolderActivity.this);
                }
            });
        }

        mTitle.setText(title);
        if (mUserId.equals(PreferenceUtils.getUUid())) {
            if (mFolderType.equals(FolderType.SP.toString())) {
                mExamineSp.setVisibility(View.VISIBLE);
            }
            if (isSubmission) {
                mIvAdd.setVisibility(View.GONE);
            } else {
                mIvAdd.setVisibility(View.VISIBLE);
            }
            if (mFolderType.equals(FolderType.WZ.toString())) {
                mIvAdd.setImageResource(R.drawable.btn_add_folder_item);
            } else {
                mIvAdd.setImageResource(R.drawable.btn_create_folder);
            }
            mIvAdd.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (mFolderType.equals(FolderType.WZ.toString())) {

                    } else {
                        NewFolderEditActivity.startActivity(NewFolderActivity.this, "create", mFolderType, null);
                    }
                }
            });
        } else {
            mExamineSp.setVisibility(View.GONE);
            mIvAdd.setVisibility(View.GONE);
        }
        mListDocs.getSwipeRefreshLayout().setColorSchemeResources(R.color.main_light_cyan, R.color.main_cyan);
        mAdapter = new BagCollectionTopAdapter();
        mListDocs.getRecyclerView().setAdapter(mAdapter);
        RecyclerView.LayoutManager manager;
        if (mFolderType.equals(FolderType.WZ.toString())) {
            manager = new LinearLayoutManager(this);
        } else {
            manager = new GridLayoutManager(this, 3);
            mListDocs.getRecyclerView().addItemDecoration(new FolderDecoration());
        }
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final ShowFolderEntity entity = mAdapter.getItem(position);

                if (!mIsSelect) {
                    if (mFolderType.equals(FolderType.ZH.toString())) {
                        NewFileCommonActivity.startActivity(NewFolderActivity.this, FolderType.ZH.toString(), entity.getFolderId(), entity.getCreateUser());
                    } else if (mFolderType.equals(FolderType.TJ.toString())) {
                        if (isSubmission) {
//                            NewFileCommonActivity.startActivity(NewFolderActivity.this, FolderType.TJ.toString(), entity.getFolderId(), entity.getCreateUser(), true);
                            final AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
                            alertDialogUtil.createNormalDialog(NewFolderActivity.this, "确认投稿？");
                            alertDialogUtil.setOnClickListener(new AlertDialogUtil.OnClickListener() {
                                @Override
                                public void CancelOnClick() {
                                    alertDialogUtil.dismissDialog();
                                }

                                @Override
                                public void ConfirmOnClick() {
                                    LibraryContribute contribute = new LibraryContribute();
                                    contribute.setTargetId(entity.getFolderId());
                                    contribute.setDepartmentId(departmentId);
                                    contribute.setType(mFolderType);
                                    mPresenter.loadLibrarySubmitContribute(contribute);
                                    alertDialogUtil.dismissDialog();
                                }
                            });
                            alertDialogUtil.showDialog();

                        } else {
                            NewFileCommonActivity.startActivity(NewFolderActivity.this, FolderType.TJ.toString(), entity.getFolderId(), entity.getCreateUser());
                        }
                    } else if (mFolderType.equals(FolderType.MH.toString())) {
                        if (isSubmission) {
                            if (!TextUtils.isEmpty(collection) && collection.equals("collection")) {
                                Intent intent = new Intent();
                                intent.putExtra("folderId", entity.getFolderId());
                                intent.putExtra("folderName", entity.getFolderName());
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                NewFileManHuaActivity.startActivity(NewFolderActivity.this, FolderType.MH.toString(), entity.getFolderId(), entity.getCreateUser(), true, departmentId);
                            }
                        } else {
                            NewFileManHuaActivity.startActivity(NewFolderActivity.this, FolderType.MH.toString(), entity.getFolderId(), entity.getCreateUser());
                        }
                    } else if (mFolderType.equals(FolderType.XS.toString())) {
                        if (isSubmission) {
                            NewFileXiaoshuoActivity.startActivity(NewFolderActivity.this, FolderType.XS.toString(), entity.getFolderId(), entity.getCreateUser(), true, departmentId);
                        } else {
                            NewFileXiaoshuoActivity.startActivity(NewFolderActivity.this, FolderType.XS.toString(), entity.getFolderId(), entity.getCreateUser());
                        }
                    } else if (mFolderType.equals(FolderType.YY.toString())) {
                        FileMovieActivity.startActivity(NewFolderActivity.this, FolderType.YY.toString(), entity.getFolderId(), entity.getCreateUser(), "11111111-1111-1111-1111-111111111111".equals(entity.getFolderId()));
                    } else if (mFolderType.equals(FolderType.SP.toString())) {
                        FileMovieActivity.startActivity(NewFolderActivity.this, FolderType.SP.toString(), entity.getFolderId(), entity.getCreateUser(), "11111111-1111-1111-1111-111111111111".equals(entity.getFolderId()));
                    }
                } else {
                    if (entity.isSelect()) {
                        mSelectMap.remove(position);
                        entity.setSelect(false);
                    } else {
                        mSelectMap.put(position, entity);
                        entity.setSelect(true);
                    }
                    mAdapter.notifyItemChanged(position);
                    if (mSelectMap.size() > 1) {
                        mTvTop.setEnabled(false);
                        mTvTop.setTextColor(ContextCompat.getColor(NewFolderActivity.this, R.color.gray_929292));
                    } else {
                        mTvTop.setEnabled(true);
                        mTvTop.setTextColor(ContextCompat.getColor(NewFolderActivity.this, R.color.main_cyan));
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        mListDocs.setLayoutManager(manager);
        mListDocs.setLoadMoreEnabled(false);
        mListDocs.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                isLoading = true;
                mPresenter.loadFolderList(mFolderType, mAdapter.getItemCount(), mUserId, mType);
            }

            @Override
            public void onRefresh() {
                isLoading = true;
                mPresenter.loadFolderList(mFolderType, 0, mUserId, mType);
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
        if (mUserId.equals(PreferenceUtils.getUUid())) {
            initPopupMenus();
            if (isSubmission) {
                mIvMenu.setVisibility(View.GONE);
            } else {
                mIvMenu.setVisibility(View.VISIBLE);
            }
        } else {
            mIvMenu.setVisibility(View.GONE);
        }
        mPresenter.loadFolderList(mFolderType, 0, mUserId, mType);
    }

    private void initPopupMenus() {
        bottomMenuFragment = new BottomMenuFragment();
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item = new MenuItem(1, "选择");
        items.add(item);
        bottomMenuFragment.setMenuItems(items);
        bottomMenuFragment.setShowTop(false);
        bottomMenuFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        bottomMenuFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                if (itemId == 1) {
                    mIvBack.setVisibility(View.GONE);
                    mIvMenu.setVisibility(View.GONE);
                    mTvMenuLeft.setVisibility(View.VISIBLE);
                    ViewUtils.setLeftMargins(mTvMenuLeft, getResources().getDimensionPixelSize(R.dimen.x36));
                    mTvMenuLeft.setText(getString(R.string.label_give_up));
                    mTvMenuLeft.setTextColor(ContextCompat.getColor(NewFolderActivity.this, R.color.black_1e1e1e));
                    mTvMenuRight.setVisibility(View.VISIBLE);
                    ViewUtils.setRightMargins(mTvMenuRight, getResources().getDimensionPixelSize(R.dimen.x36));
                    mTvMenuRight.setText(getString(R.string.label_delete));
                    mTvMenuRight.setTextColor(ContextCompat.getColor(NewFolderActivity.this, R.color.main_cyan));
                    mTvTop.setVisibility(View.VISIBLE);
                    mIsSelect = !mIsSelect;
                    mAdapter.setSelect(mIsSelect);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setImageResource(R.drawable.btn_back_black_normal);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        mIvMenu.setImageResource(R.drawable.btn_menu_black_normal);
        mIvMenu.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (bottomMenuFragment != null)
                    bottomMenuFragment.show(getSupportFragmentManager(), "Folder");
            }
        });
        mTvMenuLeft.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                mIvBack.setVisibility(View.VISIBLE);
                mIvMenu.setVisibility(View.VISIBLE);
                mTvMenuLeft.setVisibility(View.GONE);
                mTvMenuRight.setVisibility(View.GONE);
                mTvTop.setVisibility(View.GONE);
                mIsSelect = !mIsSelect;
                for (ShowFolderEntity entity : mAdapter.getList()) {
                    entity.setSelect(false);
                }
                mSelectMap.clear();
                mAdapter.setSelect(mIsSelect);
                mAdapter.notifyDataSetChanged();
            }
        });
        mTvMenuRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mSelectMap.size() > 0) {
                    createDialog();
                    ArrayList<String> ids = new ArrayList<>();
                    for (ShowFolderEntity id : mSelectMap.values()) {
                        ids.add(id.getFolderId());
                    }
                    mPresenter.deleteFolders(ids, mFolderType);
                }
            }
        });
        mTvTop.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mSelectMap.size() == 1) {
                    createDialog();
                    for (ShowFolderEntity entity : mSelectMap.values()) {
                        mPresenter.topFolder(entity.getFolderId());
                    }
                }
            }
        });
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onFailure(int code, String msg) {
        finalizeDialog();
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
        isLoading = false;
        mListDocs.setComplete();
    }

    @Override
    public void onLoadFolderListSuccess(Object o, boolean isPull) {
        isLoading = false;
        mListDocs.setComplete();
        ArrayList<ShowFolderEntity> entities = (ArrayList<ShowFolderEntity>) o;
        if (entities.size() >= ApiService.LENGHT) {
            mListDocs.setLoadMoreEnabled(true);
        } else {
            mListDocs.setLoadMoreEnabled(false);
        }
        if (isPull) {
            mAdapter.setList(entities);
        } else {
            mAdapter.addList(entities);
        }
    }

    @Override
    public void onDeleteFoldersSuccess() {
        finalizeDialog();
        for (ShowFolderEntity entity : mSelectMap.values()) {
            mAdapter.getList().remove(entity);
        }
        mAdapter.notifyDataSetChanged();
        mSelectMap.clear();
    }

    @Override
    public void onTopFolderSuccess() {
        finalizeDialog();
        for (Integer i : mSelectMap.keySet()) {
            mAdapter.getList().get(i).setSelect(false);
            ShowFolderEntity entity = mAdapter.getList().remove((int) i);
            mAdapter.getList().add(0, entity);
            mAdapter.notifyItemRangeChanged(0, i + 1);
        }
        mSelectMap.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LIBRARY_TOUGAO && resultCode == RESULT_OK) {
//            if (data != null) {
            setResult(RESULT_OK);
            finish();
//            }
        }
    }

    /**
     * 投稿成功
     */
    @Override
    public void onLoadLibrarySubmitContribute() {
        showToast("投稿成功");
        setResult(RESULT_OK);
        finish();
    }
}
