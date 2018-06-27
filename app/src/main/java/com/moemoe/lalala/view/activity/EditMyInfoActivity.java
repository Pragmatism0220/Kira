package com.moemoe.lalala.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.databinding.ActivityEditMyInfoBinding;
import com.moemoe.lalala.di.components.DaggerEditMyInfoComponent;
import com.moemoe.lalala.di.modules.EditMyInfoModule;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.UserInfo;
import com.moemoe.lalala.presenter.EditMyInfoContract;
import com.moemoe.lalala.presenter.EditMyInfoPresenter;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.ErrorCodeUtils;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.SoftKeyboardUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.base.BaseActivity;
import com.moemoe.lalala.view.widget.datetimepicker.date.DatePickerDialog;
import com.moemoe.lalala.view.widget.view.KeyboardListenerLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class EditMyInfoActivity extends BaseActivity implements EditMyInfoContract.View {

    public static final int REQ_EDIT = 6666;
    private final String DATEPICKER_TAG = "datepicker";
    private final int LIMIT_NICK_NAME = 10;
    private final int LIMIT_SIGN = 50;
    private final int REQ_TAKE_PHOTO = 1001;
    private final int REQ_GET_FROM_GALLERY = 1002;
    private final int REQ_CROP_AVATAR = 1003;
    private final int REQ_SECRET = 1004;


    private ActivityEditMyInfoBinding binding;
    private Uri mTmpAvatar;
    private TextView mTvTitle;
    private ImageView mIvBack;
    private String mRawAvatarPath;
    private TextView mTvSave;
    private boolean mHasModified = false;
    private UserInfo mInfo;
    private DatePickerDialog mDatePickerDialog;
    private KeyboardListenerLayout mKlCommentBoard;
    private EditText mEdtCommentInput;
    private View mTvSendComment;
    private boolean mIsNickname;
    private String mUploadPath;
    private String mBgPath;

    @Inject
    EditMyInfoPresenter mPresenter;

    @Override
    protected void initComponent() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_my_info);
        mTvTitle = findViewById(R.id.tv_toolbar_title);
        mIvBack = findViewById(R.id.iv_back);
        mKlCommentBoard = findViewById(R.id.ll_comment_pannel);
        mEdtCommentInput = findViewById(R.id.edt_comment_input);
        mTvSendComment = findViewById(R.id.iv_comment_send);
        mTvSave = findViewById(R.id.tv_menu);
        binding.setPresenter(new Presenter());
        DaggerEditMyInfoComponent.builder()
                .editMyInfoModule(new EditMyInfoModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        mInfo = getIntent().getParcelableExtra("info");

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mDatePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                if (mInfo != null) mInfo.setBirthday(year + "-" + (month + 1) + "-" + day);
                binding.editBrithday.setText(year + "-" + (month + 1) + "-" + day);
                mHasModified = true;
            }
        }, 1990, 6, 1, false);
        mDatePickerDialog.setYearRange(1949, 2016);
        mDatePickerDialog.setCloseOnSingleTapDay(true);

        mEdtCommentInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    mTvSendComment.setEnabled(false);
                } else {
                    mTvSendComment.setEnabled(true);
                }

            }
        });

        mKlCommentBoard.setOnkbdStateListener(new KeyboardListenerLayout.onKeyboardChangeListener() {

            @Override
            public void onKeyBoardStateChange(int state) {
                if (state == KeyboardListenerLayout.KEYBOARD_STATE_HIDE) {
                    mKlCommentBoard.setVisibility(View.GONE);
                }
            }
        });
        mTvSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEdtCommentInput.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    SoftKeyboardUtils.dismissSoftKeyboard(EditMyInfoActivity.this);
                    if (mIsNickname) {
                        if (content.length() > LIMIT_NICK_NAME) {
                            binding.editNikeName.setSelected(true);
                        } else {
                            binding.editNikeName.setSelected(false);
                        }
                        binding.editNikeName.setText(content);
                    } else {
                        if (content.length() > LIMIT_SIGN) {
                            binding.editSignature.setSelected(true);
                        } else {
                            binding.editSignature.setSelected(false);
                        }
                        binding.editSignature.setText(content);
                    }
                    mHasModified = true;
                } else {
                    showToast(R.string.msg_doc_comment_not_empty);
                }
            }
        });
        updataView();
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mTvSave.setVisibility(View.VISIBLE);
        ViewUtils.setRightMargins(mTvSave, (int) getResources().getDimension(R.dimen.x36));
        mTvSave.getPaint().setFakeBoldText(true);
        mTvSave.setText(getString(R.string.label_save_modify));
        mTvTitle.setText(R.string.edit_my_info);
        mTvTitle.setTextColor(getResources().getColor(R.color.black));
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setImageResource(R.drawable.btn_back_black_normal);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        mTvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify();
            }
        });
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    private void showListDialog() {
        final String[] items = {"男", "女"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        listDialog.setTitle("选择性别");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    binding.editSexMore.setText("男");
                    mInfo.setSex("F");
                } else {
                    binding.editSexMore.setText("女");
                    mInfo.setSex("M");
                }
                mHasModified = true;
            }
        });
        listDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (mHasModified) {
            DialogUtils.showAbandonModifyDlg(this);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_GET_FROM_GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                // file
                if (data != null) {
                    ArrayList<String> paths = data
                            .getStringArrayListExtra(MultiImageChooseActivity.EXTRA_KEY_SELETED_PHOTOS);
                    if (paths != null && paths.size() == 1) {
                        mRawAvatarPath = paths.get(0);
                    }
                    go2CropAvatar(0, mRawAvatarPath);
                }
            }
        } else if (requestCode == REQ_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                mRawAvatarPath = mTmpAvatar.getPath();
                go2CropAvatar(0, mRawAvatarPath);
            }
        } else if (requestCode == REQ_CROP_AVATAR) {
            if (resultCode == Activity.RESULT_OK) {
                if (!NetworkUtils.isNetworkAvailable(this)) {
                    showToast(R.string.msg_connection);
                    return;
                }
                createDialog();
                mPresenter.uploadAvatar(data.getStringExtra("path"), data.getIntExtra("type", 0));
                mHasModified = true;
            }
        } else if (requestCode == REQ_SECRET) {
            if (resultCode == RESULT_OK) {
                mInfo.setShowFans(data.getBooleanExtra("show_fans", false));
                mInfo.setShowFavorite(data.getBooleanExtra("show_favorite", false));
                mInfo.setShowFollow(data.getBooleanExtra("show_follow", false));
            }
        } else {
            DialogUtils.handleImgChooseResult(this, requestCode, resultCode, data, new DialogUtils.OnPhotoGetListener() {

                @Override
                public void onPhotoGet(ArrayList<String> photoPaths, boolean override) {
                    mBgPath = photoPaths.get(0);
                    go2CropAvatar(1, mBgPath);
                }
            });
        }
    }


    private void go2CropAvatar(int type, String path) {
        Intent intent = new Intent(this, CropAvatarActivity.class);
        intent.putExtra(CropAvatarActivity.EXTRA_RAW_IMG_PATH, path);
        intent.putExtra("type", type);
        if (type == 1) {
            intent.putExtra(CropAvatarActivity.EXTRA_W_RATIO, 36);
            intent.putExtra(CropAvatarActivity.EXTRA_H_RATIO, 23);
        }
        startActivityForResult(intent, REQ_CROP_AVATAR);
    }

    private void updataView() {
        Glide.with(this)
                .load(StringUtils.getUrl(this, mInfo.getHeadPath(), (int) getResources().getDimension(R.dimen.y100), (int) getResources().getDimension(R.dimen.y100), false, true))
                .override((int) getResources().getDimension(R.dimen.y100), (int) getResources().getDimension(R.dimen.y100))
                .bitmapTransform(new CropCircleTransformation(this))
                .error(R.drawable.bg_default_circle)
                .placeholder(R.drawable.bg_default_circle)
                .into(binding.editPhoto);
        Glide.with(this)
                .load(StringUtils.getUrl(this, ApiService.URL_QINIU + mInfo.getBackground(), (int) getResources().getDimension(R.dimen.y112), (int) getResources().getDimension(R.dimen.y112), false, true))
                .override((int) getResources().getDimension(R.dimen.y112), (int) getResources().getDimension(R.dimen.y112))
                .error(R.drawable.shape_gray_e8e8e8_background)
                .placeholder(R.drawable.shape_gray_e8e8e8_background)
                .into(binding.editBg);
        binding.editNikeName.setText(mInfo.getUserName());
        binding.editSexMore.setText(mInfo.getSex().equals("F") ? "男" : "女");
        binding.editBrithday.setText(mInfo.getBirthday());
        binding.editSignature.setText(mInfo.getSignature());
    }

    @Override
    public void onFailure(int code, String msg) {
        finalizeDialog();
        ErrorCodeUtils.showErrorMsgByCode(this, code, msg);
    }


    @Override
    public void uploadSuccess(String path, int type) {
        finalizeDialog();
        if (type == 0) {
            Glide.with(this)
                    .load(StringUtils.getUrl(this, ApiService.URL_QINIU + path, (int) getResources().getDimension(R.dimen.y100), (int) getResources().getDimension(R.dimen.y100), false, true))
                    .override((int) getResources().getDimension(R.dimen.y100), (int) getResources().getDimension(R.dimen.y100))
                    .placeholder(R.drawable.bg_default_circle)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .error(R.drawable.bg_default_circle)
                    .into(binding.editPhoto);
            mUploadPath = path;
        } else {
            Glide.with(this)
                    .load(StringUtils.getUrl(this, ApiService.URL_QINIU + path, (int) getResources().getDimension(R.dimen.y112), (int) getResources().getDimension(R.dimen.y112), false, true))
                    .override((int) getResources().getDimension(R.dimen.y112), (int) getResources().getDimension(R.dimen.y112))
                    .placeholder(R.drawable.shape_gray_e8e8e8_background)
                    .error(R.drawable.shape_gray_e8e8e8_background)
                    .into(binding.editBg);
            mBgPath = path;
        }

    }

    @Override
    public void uploadFail(int type) {
        finalizeDialog();
        if (type == 0) {
            mRawAvatarPath = "";
        } else {
            mBgPath = "";
        }

    }

    private void modify() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showToast(R.string.msg_connection);
            return;
        }
        mInfo.setUserName(binding.editNikeName.getText().toString());
        if (TextUtils.isEmpty(mInfo.getUserName())) {
            showToast(R.string.msg_nickname_cannot_null);
            return;
        }
        if (mInfo.getUserName().length() > LIMIT_NICK_NAME) {
            showToast(R.string.msg_nickname_too_long);
            return;
        }
        if (binding.editSignature.getText().toString().length() > LIMIT_SIGN) {
            showToast(R.string.msg_sign_too_long);
            return;
        }
        if (StringUtils.isLeagleNickName(mInfo.getUserName())) {
            showToast(R.string.msg_nickname_illegal);
            return;
        }
        if (!TextUtils.isEmpty(mUploadPath)) {
            mInfo.setHeadPath(ApiService.URL_QINIU + mUploadPath);
        }
        if (!TextUtils.isEmpty(mBgPath)) {
            mInfo.setBackground(mBgPath);
        }
        mInfo.setSignature(binding.editSignature.getText().toString());
        createDialog();
        mPresenter.modify(mInfo.getUserName(), TextUtils.isEmpty(mInfo.getSex()) ? "M" : mInfo.getSex(), mInfo.getBirthday(), mInfo.getBackground(), mInfo.getHeadPath(), mInfo.getSignature());
    }

    @Override
    public void modifySuccess() {
        finalizeDialog();
        showToast(R.string.msg_modify_my_data_success);
        PreferenceUtils.getAuthorInfo().setHeadPath(mInfo.getHeadPath());
        PreferenceUtils.getAuthorInfo().setUserName(mInfo.getUserName());
        Intent i = new Intent();
        i.putExtra("info", mInfo);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.release();
        super.onDestroy();
    }

    /**
     * 获取头像
     */
    private void go2getAvatar() {
        com.moemoe.lalala.dialog.AlertDialog.Builder builder = new com.moemoe.lalala.dialog.AlertDialog.Builder(this);
        builder.setTitle(R.string.label_take_avatar);
        CharSequence[] items = new String[]{this.getString(R.string.label_take_photo),
                this.getString(R.string.label_get_picture)};

        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mTmpAvatar == null) {
                    mTmpAvatar = Uri.fromFile(StorageUtils.getTempFile("ava" + System.currentTimeMillis() + ".jpg"));
                }

                if (which == 0) {
                    // 拍照
                    try {
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
                        i.putExtra(MediaStore.EXTRA_OUTPUT, mTmpAvatar);
                        startActivityForResult(i, REQ_TAKE_PHOTO);// CAMERA_WITH_DATA
                    } catch (Exception e) {
                        Toast.makeText(EditMyInfoActivity.this, getString(R.string.msg_no_system_camera),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(EditMyInfoActivity.this, MultiImageChooseActivity.class);
                    intent.putExtra(MultiImageChooseActivity.EXTRA_KEY_MAX_PHOTO, 1);
                    startActivityForResult(intent, REQ_GET_FROM_GALLERY);
                }
            }
        });
        try {
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class Presenter  {
        public void onClick(View view) {
            switch (view.getId()) {
                //地址
                case R.id.edit_address_item:
                    Intent intent = new Intent(getApplicationContext(), AddAddressActivity.class);
                    startActivity(intent);
                    break;
                //头像
                case R.id.edit_photo_item:
                    go2getAvatar();
                    break;
                //背景
                case R.id.edit_bg_item:
                    try {
                        ArrayList<String> arrayList = new ArrayList<>();
                        DialogUtils.createImgChooseDlg(EditMyInfoActivity.this, null, EditMyInfoActivity.this, arrayList, 1).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                //昵称
                case R.id.edit_nike_name_item:
                    mKlCommentBoard.setVisibility(View.VISIBLE);
                    mEdtCommentInput.setText("");
                    mEdtCommentInput.setHint("昵称");
                    mEdtCommentInput.requestFocus();
                    mIsNickname = true;
                    SoftKeyboardUtils.showSoftKeyboard(EditMyInfoActivity.this, mEdtCommentInput);
                    break;
                //性别
                case R.id.edit_sex_item:
                    showListDialog();
                    break;
                case R.id.edit_brithday_item:
                    mDatePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                    break;
                default:
                    break;
            }
        }

    }
}
