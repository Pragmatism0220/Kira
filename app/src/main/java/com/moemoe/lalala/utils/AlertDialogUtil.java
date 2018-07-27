package com.moemoe.lalala.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.UFormat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.FileXiaoShuoEntity;
import com.moemoe.lalala.model.entity.ShareLive2dEntity;
import com.moemoe.lalala.view.activity.CommunityV1Activity;
import com.moemoe.lalala.view.activity.FileMovieActivity;
import com.moemoe.lalala.view.activity.HouseActivity;
import com.moemoe.lalala.view.activity.Live3dActivity;
import com.moemoe.lalala.view.activity.NewDocDetailActivity;
import com.moemoe.lalala.view.activity.NewFileCommonActivity;
import com.moemoe.lalala.view.activity.NewFileManHuaActivity;
import com.moemoe.lalala.view.activity.NewFileXiaoshuoActivity;
import com.moemoe.lalala.view.activity.ShopDetailActivity;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.moemoe.lalala.view.widget.view.KiraRatingBar;

import java.util.ArrayList;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yi on 2016/11/28.
 */

public class AlertDialogUtil {
    private static AlertDialogUtil alertDialogUtil = null;
    private Dialog dialog = null;
    private View view;
    private OnClickListener onClickListener;
    private OnItemClickListener onItemClickListener;
    private OnDismiss onDismiss;
    private TextView confirm, cancel;
    private Button item1, item2, item3, item4;
    private EditText editText;
    private Context context;
    private int hour;
    private int minute;
    private float score;
    private BottomMenuFragment bottomMenuFragment;

    private AlertDialogUtil() {
    }

    public static AlertDialogUtil getInstance() {
        if (alertDialogUtil == null) {
            alertDialogUtil = new AlertDialogUtil();
        }
        return alertDialogUtil;
    }

    public void createPromptDialog(Context context, String title, String content) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        view = LayoutInflater.from(context).inflate(
                R.layout.general_dialog, null);
        this.dialog = new AlertDialog.Builder(context).setView(view).create();
        if (!TextUtils.isEmpty(title))
            ((TextView) view.findViewById(R.id.general_dialog_title)).setText(title);
        else
            ((TextView) view.findViewById(R.id.general_dialog_title)).setText(context.getString(R.string.label_alert));
        if (!TextUtils.isEmpty(content))
            ((TextView) view.findViewById(R.id.general_dialog_content)).setText(content);
        else
            ((TextView) view.findViewById(R.id.general_dialog_content)).setText("");
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        dialog.setCancelable(false);
        confirm = view.findViewById(R.id.general_dialog_btn_confirm);
        cancel = view.findViewById(R.id.general_dialog_btn_cancel);
    }

    public void createBuyFolderDialog(final Context context, int coin, int nowNum, int maxNum, final String folderId, final String folderType, final String cover, final String createUser) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_buy_folder, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = DensityUtil.getScreenWidth(context);
        layoutParams.height = DensityUtil.getScreenHeight(context);
        window.setAttributes(layoutParams);
        TextView tv = contentView.findViewById(R.id.tv_coin);
        tv.setText(coin + " 节操");

        int w = (int) context.getResources().getDimension(R.dimen.x31);
        int marginStart = (int) context.getResources().getDimension(R.dimen.x8);
        int h = (int) context.getResources().getDimension(R.dimen.y36);
        LinearLayout lockRoot = contentView.findViewById(R.id.ll_lock_root);
        LinearLayout lockRoot2 = contentView.findViewById(R.id.ll_lock_root_2);
        if (coin > 30) {
            lockRoot.setVisibility(View.GONE);
        } else {
            lockRoot.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < maxNum; i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, h);
            if (i != 0) {
                lp.leftMargin = marginStart;
            }
            ImageView iv = new ImageView(context);
            if (i < nowNum) {
                iv.setImageResource(R.drawable.ic_share_lock_green);
            } else {
                iv.setImageResource(R.drawable.ic_share_lock_pink);
            }
            lockRoot2.addView(iv);
        }
        lockRoot.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showShareToBuy(context, folderId, folderType, cover, createUser);
            }
        });
        TextView tvLock = contentView.findViewById(R.id.tv_lock);
        tvLock.setText("(已解锁 " + nowNum + "/" + maxNum + ",分享后好友帮你解锁 )");
        dialog.setCancelable(false);
        cancel = contentView.findViewById(R.id.btn_cancel);
        confirm = contentView.findViewById(R.id.btn_buy);
    }

    private void showShareToBuy(final Context context, final String folderId, final String folderType, final String cover, final String createUser) {
        final String url = "http://2333.moemoe.la/share/folder?folderId=" + folderId + "&type=" + folderType + "&userId=" + PreferenceUtils.getUUid() + "&folderCreateUser=" + createUser;
        bottomMenuFragment = new BottomMenuFragment();
        ArrayList<MenuItem> items = new ArrayList<>();

        MenuItem item = new MenuItem(1, "QQ", R.drawable.btn_doc_option_send_qq);
        items.add(item);

        item = new MenuItem(2, "QQ空间", R.drawable.btn_doc_option_send_qzone);
        items.add(item);

        item = new MenuItem(3, "微信", R.drawable.btn_doc_option_send_wechat);
        items.add(item);

        item = new MenuItem(4, "微信朋友圈", R.drawable.btn_doc_option_send_pengyouquan);
        items.add(item);

        item = new MenuItem(5, "微博", R.drawable.btn_doc_option_send_weibo);
        items.add(item);

        bottomMenuFragment.setShowTop(false);
        bottomMenuFragment.setMenuItems(items);
        bottomMenuFragment.setMenuType(BottomMenuFragment.TYPE_HORIZONTAL);
        bottomMenuFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {

                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                // text是分享文本，所有平台都需要这个字段
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                switch (itemId) {
                    case 1:
                        ShareUtils.shareQQ(context, "你被邀请帮助好友解锁书包", url, "更多神秘书包可以下载APP查看哦 " + url, ApiService.URL_QINIU + cover, platformActionListener);
                        break;
                    case 2:
                        ShareUtils.shareQQzone(context, "你被邀请帮助好友解锁书包", url, "更多神秘书包可以下载APP查看哦 " + url, ApiService.URL_QINIU + cover, platformActionListener);
                        ;
                        break;
                    case 3:
                        ShareUtils.shareWechat(context, "你被邀请帮助好友解锁书包", url, "更多神秘书包可以下载APP查看哦 " + url, ApiService.URL_QINIU + cover, platformActionListener);
                        break;
                    case 4:
                        ShareUtils.sharepyq(context, "你被邀请帮助好友解锁书包", url, "更多神秘书包可以下载APP查看哦 " + url, ApiService.URL_QINIU + cover, platformActionListener);
                        break;
                    case 5:
                        ShareUtils.shareWeibo(context, "你被邀请帮助好友解锁书包", url, "更多神秘书包可以下载APP查看哦 " + url, ApiService.URL_QINIU + cover, platformActionListener);
                        break;

                }
            }
        });
//        final OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        oks.setTitle("你被邀请帮助好友解锁书包");
//        //  String url = "http://2333.moemoe.la/share/folder?folderId=" + folderId + "&type=" + folderType + "&userId=" + PreferenceUtils.getUUid();
//        oks.setTitleUrl(url);
//        oks.setText("更多神秘书包可以下载APP查看哦 " + url);
//        oks.setImageUrl(ApiService.URL_QINIU + cover);
//        oks.setUrl(url);
//        oks.setSite(context.getString(R.string.app_name));
//        oks.setSiteUrl(url);
//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//
//            }
//        });
        MoeMoeApplication.getInstance().getNetComponent().getApiService().shareKpi("folder")
                .subscribeOn(Schedulers.io())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFail(int code, String msg) {

                    }
                });
        if (context instanceof FileMovieActivity) {
            bottomMenuFragment.show(((FileMovieActivity) context).getSupportFragmentManager(), "sharesdk");
        } else if (context instanceof NewFileCommonActivity) {
            bottomMenuFragment.show(((NewFileCommonActivity) context).getSupportFragmentManager(), "sharesdk");
        } else if (context instanceof NewFileManHuaActivity) {
            bottomMenuFragment.show(((NewFileManHuaActivity) context).getSupportFragmentManager(), "sharesdk");
        } else if (context instanceof NewFileXiaoshuoActivity) {
            bottomMenuFragment.show(((NewFileXiaoshuoActivity) context).getSupportFragmentManager(), "sharesdk");
        }
//        oks.show(context);
    }

    /**
     * 分享回调
     */
    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Log.e("kid", "分享成功");
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.e("kid", "分享失败");
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }

        @Override
        public void onCancel(Platform platform, int i) {
            Log.e("kid", "分享取消");
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }

    };

    public void createPromptNormalDialog(Context context, String content) {
        this.context = context;
        Activity activity = (Activity) context;
//        if (!activity.isDestroyed() && !activity.isFinishing()) {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
//        }

        View contentView = View.inflate(context, R.layout.dialog_normal_notice, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        TextView tv = contentView.findViewById(R.id.tv_content);
        tv.setText(content);
        dialog.setCancelable(false);
        cancel = contentView.findViewById(R.id.cancel);
        confirm = contentView.findViewById(R.id.confirm);

        if (!TextUtils.isEmpty(content))
            ((TextView) contentView.findViewById(R.id.tv_content)).setText(content);
        else
            ((TextView) contentView.findViewById(R.id.tv_content)).setText("");
    }

    public void createHongbaoDialog(Context context, float coin) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_hongbao, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        TextView title = contentView.findViewById(R.id.tv_title);
        TextView content = contentView.findViewById(R.id.tv_content);
        LinearLayout redBag = contentView.findViewById(R.id.red_bag);
        dialog.setCancelable(true);
        redBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                onDismiss.onDismiss();
            }
        });
        if (coin == 0) {
            title.setText("很遗憾~");
            content.setText("红包已经被抢完");
            content.setTextColor(Color.WHITE);
        } else {
            title.setText("转发成功");
            content.setText("获得" + String.format(context.getString(R.string.label_get_coin), coin));
            content.setTextColor(ContextCompat.getColor(context, R.color.orange_f2cc2c));
        }
    }

    public void createKiraNoticeDialog(Context context, String content, String btn) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_kira_notice, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        TextView tv = contentView.findViewById(R.id.tv_content);
        dialog.setCancelable(false);
        confirm = contentView.findViewById(R.id.btn_confirm);
        tv.setText(content);
        confirm.setText(btn);
    }

    public void createPingFenDialog(Context context, String btn) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_ping_fen, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        KiraRatingBar bar = contentView.findViewById(R.id.kira_bar);
        bar.setOnRatingBarChangeListener(new KiraRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(KiraRatingBar KiraRatingBar, float rating, boolean fromUser) {
                score = rating;
            }
        });
        dialog.setCancelable(false);
        confirm = contentView.findViewById(R.id.btn_confirm);
        confirm.setText(btn);
    }

    public void createTimepickerDialog(Context context, int hourT, int minuteT) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_timepicker, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        dialog.setCancelable(false);
        cancel = contentView.findViewById(R.id.cancel);
        confirm = contentView.findViewById(R.id.confirm);
        TimePicker timePicker = contentView.findViewById(R.id.timerpicker);
        timePicker.setIs24HourView(true);
        hour = hourT;
        minute = minuteT;
        timePicker.setCurrentHour(hourT);
        timePicker.setCurrentMinute(minuteT);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minutep) {
                hour = hourOfDay;
                minute = minutep;
            }
        });

    }

    public void createNoticeDialog(Context context, String title, String content) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        view = LayoutInflater.from(context).inflate(
                R.layout.general_dialog, null);
        this.dialog = new AlertDialog.Builder(context).setView(view).create();
        if (!TextUtils.isEmpty(title))
            ((TextView) view.findViewById(R.id.general_dialog_title)).setText(title);
        if (!TextUtils.isEmpty(content))
            ((TextView) view.findViewById(R.id.general_dialog_content)).setText(content);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        confirm = view.findViewById(R.id.general_dialog_btn_confirm);
        cancel = view.findViewById(R.id.general_dialog_btn_cancel);
        cancel.setVisibility(View.GONE);
    }

    public void createSelectDialog(Context context) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_select, null);
        this.dialog = new Dialog(context, R.style.NetaDialog2);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = (int) context.getResources().getDimension(R.dimen.x512);
        window.setWindowAnimations(R.style.dialogWindowAnim);
        dialog.setCancelable(false);
        item1 = contentView.findViewById(R.id.btn_item_1);
        item2 = contentView.findViewById(R.id.btn_item_2);
        item3 = contentView.findViewById(R.id.btn_item_3);
        item4 = contentView.findViewById(R.id.btn_item_4);
    }

    public void createGetHideDialog(Context context, int content) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_normal, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        TextView tv = contentView.findViewById(R.id.tv_content);
        TextView num = contentView.findViewById(R.id.tv_num);
        View v = contentView.findViewById(R.id.ll_normal);
        tv.setVisibility(View.GONE);
        v.setVisibility(View.VISIBLE);
        num.setText(content + "");
        dialog.setCancelable(false);
        cancel = contentView.findViewById(R.id.cancel);
        confirm = contentView.findViewById(R.id.confirm);
    }

    public void createNormalDialog(Context context, String content) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_normal, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        TextView tv = contentView.findViewById(R.id.tv_content);
        View v = contentView.findViewById(R.id.ll_normal);
        if (!TextUtils.isEmpty(content)) {
            tv.setText(content);
            tv.setVisibility(View.VISIBLE);
            v.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.GONE);
            v.setVisibility(View.VISIBLE);
        }
        dialog.setCancelable(false);
        cancel = contentView.findViewById(R.id.cancel);
        confirm = contentView.findViewById(R.id.confirm);
        if (context instanceof Live3dActivity) {
            cancel.setText("忽略");
            confirm.setText("加入VIP");
        } else if (context instanceof HouseActivity) {
            cancel.setText("忽略");
            confirm.setText("加入VIP");
        } else {
            cancel.setText("取消");
            confirm.setText("确认");
        }
    }

    public void createNewNormalDialog(Context context, String content, String canlceText) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_normal, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        TextView tv = contentView.findViewById(R.id.tv_content);
        View v = contentView.findViewById(R.id.ll_normal);
        if (!TextUtils.isEmpty(content)) {
            tv.setText(content);
            tv.setVisibility(View.VISIBLE);
            v.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.GONE);
            v.setVisibility(View.VISIBLE);
        }
        dialog.setCancelable(false);
        cancel = contentView.findViewById(R.id.cancel);
        if (!TextUtils.isEmpty(canlceText)) {
            cancel.setText(canlceText);
        }
        confirm = contentView.findViewById(R.id.confirm);
    }

    public void createEditDialog(Context context, int total, int type) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_jump_floor, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        TextView tv = contentView.findViewById(R.id.tv_floor_num);
        TextView tv1 = contentView.findViewById(R.id.tv_text);
        TextView tv2 = contentView.findViewById(R.id.tv_text2);
        editText = contentView.findViewById(R.id.et_floor);
        if (type == 1) {
            tv.setText(context.getString(R.string.label_total_floor, total));
            tv1.setText("跳转到");
            tv2.setText("楼");
        } else if (type == 0) {
            tv.setText(context.getString(R.string.label_total_coin, total));
            tv1.setText("献上");
            tv2.setText("枚节操");
        } else if (type == 2) {
            if (total == 0) {
                tv.setVisibility(View.INVISIBLE);
            } else {
                tv.setText(context.getString(R.string.label_total_count, total));
            }
            tv1.setText("购买");
            tv2.setText("个");
        }

        dialog.setCancelable(false);
        cancel = contentView.findViewById(R.id.cancel);
        confirm = contentView.findViewById(R.id.confirm);
        SoftKeyboardUtils.showSoftKeyboard(context, editText);
    }

    public void createDocEditDialog(Context context, String type) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_tags_doc, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        editText = contentView.findViewById(R.id.et_floor);
        dialog.setCancelable(false);
        cancel = contentView.findViewById(R.id.cancel);
        confirm = contentView.findViewById(R.id.confirm);
        TextView mTvFloor = contentView.findViewById(R.id.tv_floor);
        if (!TextUtils.isEmpty(type)) {
            mTvFloor.setVisibility(View.GONE);
            editText.setHint("名称");
        }
        SoftKeyboardUtils.showSoftKeyboard(context, editText);
    }

    public void createAddLabelDialog(Context context) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_add_label, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        editText = contentView.findViewById(R.id.et_content);
        dialog.setCancelable(false);
        cancel = contentView.findViewById(R.id.cancel);
        confirm = contentView.findViewById(R.id.confirm);
    }

    public void createShareLive2dDialog(final Context context, ShareLive2dEntity entity) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_share_live2d, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        editText = contentView.findViewById(R.id.et_content);

        TextView toVip = contentView.findViewById(R.id.tv_to_vip);
        toVip.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent i = new Intent(context, ShopDetailActivity.class);
                i.putExtra("uuid", "b3b952d1-7f31-4014-8048-2e207bcfe53c");
                context.startActivity(i);
            }
        });

        int w = (int) context.getResources().getDimension(R.dimen.x31);
        int marginStart = (int) context.getResources().getDimension(R.dimen.x12);
        int h = (int) context.getResources().getDimension(R.dimen.y36);
        LinearLayout lockRoot = contentView.findViewById(R.id.ll_lock_root);
        for (int i = 0; i < entity.getMaxNum(); i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, h);
            if (i != 0) {
                lp.leftMargin = marginStart;
            }
            ImageView iv = new ImageView(context);
            if (i < entity.getNowNum()) {
                iv.setImageResource(R.drawable.ic_share_lock_green);
            } else {
                iv.setImageResource(R.drawable.ic_share_lock_pink);
            }
            lockRoot.addView(iv);
        }

        dialog.setCancelable(true);
        confirm = contentView.findViewById(R.id.btn_share);
    }

    public String getEditTextContent() {
        String res = "";
        if (editText != null) {
            res = editText.getText().toString();
        }
        return res;
    }

    public void creatBubbleDialog(Context context) {
        this.context = context;
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
            this.dialog = null;
        }
        View contentView = View.inflate(context, R.layout.dialog_bubble, null);
        this.dialog = new Dialog(context, R.style.NetaDialog);
        this.dialog.setContentView(contentView);
        Window window = this.dialog.getWindow();
        window.setWindowAnimations(R.style.dialogWindowAnim);
        RelativeLayout mRlBubble = (RelativeLayout) contentView.findViewById(R.id.rl_bobble);
        TextView mTvContent = (TextView) contentView.findViewById(R.id.tv_content);
        confirm = (TextView) contentView.findViewById(R.id.iv_chakan);
        cancel = (TextView) contentView.findViewById(R.id.iv_canls);
        mRlBubble.setBackgroundResource(R.drawable.bg_classmate_talkbg_bottom_left);

    }

    public void setButtonText(String comfirmBtn, String cancelBtn, int status) {
        if (cancel != null && confirm != null) {
            if (!TextUtils.isEmpty(comfirmBtn))
                confirm.setText(comfirmBtn);
            else
                confirm.setText(context.getString(R.string.label_confirm));
            if (!TextUtils.isEmpty(cancelBtn))
                cancel.setText(cancelBtn);
            else
                cancel.setText(context.getString(R.string.label_cancel));
            if (status == 2) {
                cancel.setVisibility(View.GONE);
            }
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void setOnClickListener() {
        if (cancel != null) {
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.CancelOnClick();
                }
            });
        }
        if (confirm != null) {
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.ConfirmOnClick();
                }
            });
        }
        if (item1 != null) {
            item1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(0);
                }
            });
        }
        if (item2 != null) {
            item2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(1);
                }
            });
        }
        if (item3 != null) {
            item3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(2);
                }
            });
        }
        if (item4 != null) {
            item4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(3);
                }
            });
        }
    }

    public void showDialog() {
        if (this.dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            setOnClickListener();
            this.dialog.show();
        }
    }

    public void setDismissListener(OnDismiss onDismiss) {
        this.onDismiss = onDismiss;
    }

    public void dismissDialog() {
        if (this.dialog != null && this.dialog.isShowing()) {
            SoftKeyboardUtils.dismissSoftKeyboard(context, editText);
            this.dialog.dismiss();
            this.context = null;
            this.dialog = null;
            alertDialogUtil = null;
            dialog = null;
            view = null;
            onClickListener = null;
            onItemClickListener = null;
            confirm = null;
            cancel = null;
            item1 = null;
            item2 = null;
            item3 = null;
            item4 = null;
            editText = null;
        }
    }

    public boolean isShow() {
        if (this.dialog != null) {
            return this.dialog.isShowing();
        }
        return false;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        if (dialog != null)
            dialog.setOnDismissListener(onDismissListener);
    }

    public interface OnDismiss {
        void onDismiss();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnClickListener {
        void CancelOnClick();

        void ConfirmOnClick();
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}
