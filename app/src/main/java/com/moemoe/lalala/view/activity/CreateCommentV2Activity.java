package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerCreateCommentComponent;
import com.moemoe.lalala.di.modules.CreateCommentModule;
import com.moemoe.lalala.model.entity.CommentSendV2Entity;
import com.moemoe.lalala.model.entity.ForwardSendEntity;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.model.entity.NewDynamicEntity;
import com.moemoe.lalala.model.entity.ShareArticleEntity;
import com.moemoe.lalala.model.entity.ShareArticleSendEntity;
import com.moemoe.lalala.model.entity.ShareFolderSendEntity;
import com.moemoe.lalala.model.entity.ShareStreamSendEntity;
import com.moemoe.lalala.model.entity.tag.BaseTag;
import com.moemoe.lalala.model.entity.tag.UserUrlSpan;
import com.moemoe.lalala.presenter.CreateCommentContract;
import com.moemoe.lalala.presenter.CreateCommentPresenter;
import com.moemoe.lalala.utils.AlertDialogUtil;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.FileItemDecoration;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.SoftKeyboardUtils;
import com.moemoe.lalala.utils.tag.TagControl;
import com.moemoe.lalala.view.adapter.SelectItemAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.moemoe.lalala.utils.StartActivityConstant.REQ_ALT_USER;

/**
 * 评论
 * Created by Sora on 2018/3/19.
 */

public class CreateCommentV2Activity extends BaseAppCompatActivity implements CreateCommentContract.View {
    private static final int LIMIT_CONTENT = 150;
    public static final int TYPE_ARTICLE = 0;
    public static final int TYPE_FOLDER = 1;
    public static final int TYPE_DYNAMIC = 2;
    public static final int TYPE_RETWEET = 3;
    public static final int TYPE_MUSIC = 4;
    public static final int TYPE_MOVIE = 5;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.tv_content_count)
    TextView mTvContentCount;
    @BindView(R.id.list)
    RecyclerView mRvImg;
    @BindView(R.id.ll_extra_root)
    LinearLayout mExtraRoot;
    @BindView(R.id.cb_comment)
    CheckBox mCbComment;
    @BindView(R.id.rl_ope_root)
    View mRlOpRoot;

    @Inject
    CreateCommentPresenter mPresenter;

    private SelectItemAdapter mSelectAdapter;
    private ArrayList<String> mPaths;
    private String mId;
    private String mCommentTo;
    private boolean isSec;
    private int commentType;
    private int type;
    private String hint;
    private String docOrDynamicId;

    public static void startActivity(Context context, String id, boolean isSec, String commentTo, int commentType, String docOrDynamicId) {
        Intent i = new Intent(context, CreateCommentV2Activity.class);
        i.putExtra(UUID, id);
        i.putExtra("commentTo", commentTo);
        i.putExtra("isSec", isSec);
        i.putExtra("commentType", commentType);
        i.putExtra("docOrDynamicId", docOrDynamicId);
        context.startActivity(i);
    }

    public static void startActivity(Context context, String id, boolean isSec, String commentTo, int commentType, String hint, String docOrDynamicId) {
        Intent i = new Intent(context, CreateCommentV2Activity.class);
        i.putExtra(UUID, id);
        i.putExtra("commentTo", commentTo);
        i.putExtra("isSec", isSec);
        i.putExtra("commentType", commentType);
        i.putExtra("hint", hint);
        i.putExtra("docOrDynamicId", docOrDynamicId);
        context.startActivity(i);
    }

    public static void startActivity(Context context, String id, boolean isSec, String commentTo, int commentType, NewDynamicEntity entity, int type, String docOrDynamicId) {
        Intent i = new Intent(context, CreateCommentV2Activity.class);
        i.putExtra(UUID, id);
        i.putExtra("commentTo", commentTo);
        i.putExtra("isSec", isSec);
        i.putExtra("type", type);
        i.putExtra("commentType", commentType);
        i.putExtra("dynamic", entity);
        i.putExtra("docOrDynamicId", docOrDynamicId);
        context.startActivity(i);
    }

    public static void startActivity(Context context, String id, boolean isSec, String commentTo, int commentType, NewDynamicEntity entity, int type, String hint, String docOrDynamicId) {
        Intent i = new Intent(context, CreateCommentV2Activity.class);
        i.putExtra(UUID, id);
        i.putExtra("commentTo", commentTo);
        i.putExtra("isSec", isSec);
        i.putExtra("type", type);
        i.putExtra("commentType", commentType);
        i.putExtra("hint", hint);
        i.putExtra("dynamic", entity);
        i.putExtra("docOrDynamicId", docOrDynamicId);
        context.startActivity(i);
    }

    public static void startActivity(NewDocDetailActivity context, String id, boolean isSec, String commentTo, int commentType, int TYPE_DOC) {
        Intent i = new Intent(context, CreateCommentV2Activity.class);
        i.putExtra(UUID, id);
        i.putExtra("commentTo", commentTo);
        i.putExtra("isSec", isSec);
        i.putExtra("commentType", commentType);
        context.startActivityForResult(i, TYPE_DOC);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.ac_create_comment_v2;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerCreateCommentComponent.builder()
                .createCommentModule(new CreateCommentModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        AndroidBug5497Workaround.assistActivity(this);
        mId = getIntent().getStringExtra(UUID);
        mCommentTo = getIntent().getStringExtra("commentTo");
        isSec = getIntent().getBooleanExtra("isSec", false);
        commentType = getIntent().getIntExtra("commentType", 0);
        type = getIntent().getIntExtra("type", 0);
        hint = getIntent().getStringExtra("hint");
        docOrDynamicId = getIntent().getStringExtra("docOrDynamicId");
        if (TextUtils.isEmpty(mId)) {
            finish();
            return;
        }
        if (!TextUtils.isEmpty(hint)) {
            mEtContent.setHint("回复 " + hint + ":");
        }
        SoftKeyboardUtils.showSoftKeyboard(this, mEtContent);
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Editable editable = mEtContent.getText();
                int len = editable.length();
                if (len > LIMIT_CONTENT) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    mEtContent.setText(editable.subSequence(0, LIMIT_CONTENT));
                    editable = mEtContent.getText();
                    int newLen = editable.length();
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    Selection.setSelection(editable, selEndIndex);
                }
                mTvContentCount.setText(mEtContent.getText().length() + "/" + LIMIT_CONTENT);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSelectAdapter = new SelectItemAdapter(this);
        mSelectAdapter.setSelectSize(0);
        mRvImg.setLayoutManager(new GridLayoutManager(this, 3));
        mRvImg.addItemDecoration(new FileItemDecoration());
        mRvImg.setAdapter(mSelectAdapter);
        mPaths = new ArrayList<>();
        mRlOpRoot.setOnClickListener(null);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @OnClick({R.id.iv_add_img, R.id.iv_alt_user, R.id.tv_send, R.id.go_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_img:
                if (mPaths.size() < 1) {
                    try {
                        DialogUtils.createImgChooseDlg(CreateCommentV2Activity.this, null, CreateCommentV2Activity.this, mPaths, 1).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showToast("只能选择一张图片");
                }
                break;
            case R.id.iv_alt_user:
                Intent i3 = new Intent(CreateCommentV2Activity.this, SearchActivity.class);
                i3.putExtra("show_type", SearchActivity.SHOW_USER);
                startActivityForResult(i3, REQ_ALT_USER);
                break;
            case R.id.tv_send:
                done();
                break;
            case R.id.go_back:
                onBackPressed();
                break;
        }
    }

    private void done() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast(R.string.msg_connection);
            return;
        }
        if (TextUtils.isEmpty(mEtContent.getText())) {
            showToast(R.string.msg_doc_content_cannot_null);
            return;
        }
        if (TextUtils.isEmpty(mEtContent.getText().toString().replace(" ", ""))) {
            showToast("评论不能全为空格");
            return;
        }
        if (mEtContent.getText().length() > LIMIT_CONTENT) {
            showToast("超过字数限制");
            return;
        }
        createDialog();
        CommentSendV2Entity commentSendV2Entity = new CommentSendV2Entity();
        commentSendV2Entity.rt = mCbComment.isChecked();
        commentSendV2Entity.content = TagControl.getInstance().paresToString(mEtContent.getText());
        commentSendV2Entity.commentTo = mCommentTo;
        Set<HashMap<String, String>> attr = TagControl.getInstance().getAttr("at_user", mEtContent.getText());
        ArrayList<String> atUser = new ArrayList<>();
        for (HashMap<String, String> map : attr) {
            atUser.addAll(map.values());
        }
        commentSendV2Entity.atUsers = atUser;
        mPresenter.createComment(isSec, mId, commentSendV2Entity, mPaths, commentType);

        if (mCbComment.isChecked()) {
            if (type == TYPE_DYNAMIC) {
                NewDynamicEntity mDynamic = getIntent().getParcelableExtra("dynamic");
                ForwardSendEntity entity = new ForwardSendEntity();
                entity.rt = mCbComment.isChecked();
                entity.dynamicId = docOrDynamicId;
                entity.etText = TagControl.getInstance().paresToString(mEtContent.getText());
                entity.type = mDynamic.getType();
                if (mPaths.size() > 0) {
                    Image image = new Image();
                    image.setPath((String) mPaths.get(0));
                    entity.img = image;
                } else {
                    entity.img = new Image();
                }
                mPresenter.createForward(TYPE_DYNAMIC, entity);
            } else if (commentType == TYPE_ARTICLE) {
                ShareArticleSendEntity entity = new ShareArticleSendEntity();
                entity.docId = docOrDynamicId;
                entity.shareText = TagControl.getInstance().paresToString(mEtContent.getText());
                mPresenter.createForward(commentType, entity);
            }
        }
// else if (commentType == TYPE_FOLDER) {
//            ShareFolderSendEntity entity = new ShareFolderSendEntity();
//            entity.folderId = mId;
//            entity.shareText = TagControl.getInstance().paresToString(mEtContent.getText());
//            entity.folderCreateUser = mId;
//            mPresenter.createForward(commentType, entity);
//        } else if (commentType == TYPE_MOVIE || commentType == TYPE_MUSIC) {
//            mPresenter.createForward(commentType, new ShareStreamSendEntity(mId, mId, TagControl.getInstance().paresToString(mEtContent.getText())));
//        }
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
        SoftKeyboardUtils.dismissSoftKeyboard(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_ALT_USER && resultCode == RESULT_OK) {
            if (data != null) {
                String userId = data.getStringExtra("user_id");
                String userName = data.getStringExtra("user_name");
                insertTextInCurSelection("@" + userName, userId);
            }
        } else {
            mRvImg.setVisibility(View.VISIBLE);
            DialogUtils.handleImgChooseResult(this, requestCode, resultCode, data, new DialogUtils.OnPhotoGetListener() {

                @Override
                public void onPhotoGet(ArrayList<String> photoPaths, boolean override) {
                    mPaths.addAll(photoPaths);
                    ArrayList<Object> res = new ArrayList<>();
                    res.addAll(mPaths);
                    mSelectAdapter.setData(res);
                }
            });
        }
    }

    private void insertTextInCurSelection(String str, String id) {
        SpannableStringBuilder lastEditStr = new SpannableStringBuilder(mEtContent.getText());
        int cursorIndex = mEtContent.getSelectionStart();
        BaseTag tag = new BaseTag();
        tag.setTag("at_user");
        tag.setSpan(new UserUrlSpan(this, tag));
        HashMap<String, String> attrs = new HashMap<>();
        attrs.put("user_id", id);
        tag.setAttrs(attrs);
        if (cursorIndex < 0) {
            lastEditStr.insert(lastEditStr.length(), str + " ");
            lastEditStr.setSpan(tag.getSpan(), mEtContent.getText().length(), mEtContent.getText().length() + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            lastEditStr.insert(cursorIndex, str + " ");
            lastEditStr.setSpan(tag.getSpan(), cursorIndex, cursorIndex + str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mEtContent.setText(lastEditStr);
    }

    @Override
    public void onFailure(int code, String msg) {
        finalizeDialog();
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }

    @Override
    public void onBackPressed() {
        SoftKeyboardUtils.dismissSoftKeyboard(this);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onCreateCommentSuccess() {
        finalizeDialog();
        showToast("发表评论成功");
        setResult(RESULT_OK);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mEtContent, InputMethodManager.HIDE_NOT_ALWAYS);
        onBackPressed();
    }

    @Override
    public void onCreateForwardSuccess(float i) {
        finalizeDialog();
        if (i == -1) {
            showToast("转发成功");
            onBackPressed();
        } else {
//            Intent intent = new Intent();
//            intent.putExtra("coin",i);
//            setResult(RESULT_OK,intent);
            AlertDialogUtil alertDialogUtil = AlertDialogUtil.getInstance();
            alertDialogUtil.createHongbaoDialog(this, i);
            alertDialogUtil.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    onBackPressed();
                }
            });
            alertDialogUtil.showDialog();
        }
    }

    @Override
    public void onCreateForwardSuccess() {
        finalizeDialog();
        showToast("转发成功");
        onBackPressed();
    }
}
