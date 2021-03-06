package com.moemoe.lalala.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.moemoe.lalala.R;
import com.moemoe.lalala.app.MoeMoeApplication;
import com.moemoe.lalala.di.components.DaggerSimpleComponent;
import com.moemoe.lalala.di.modules.SimpleModule;
import com.moemoe.lalala.model.entity.AuthorInfo;
import com.moemoe.lalala.model.entity.MotaResult;
import com.moemoe.lalala.presenter.SimpleContract;
import com.moemoe.lalala.presenter.SimplePresenter;
import com.moemoe.lalala.utils.AndroidBug5497Workaround;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.ShareUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.fragment.WebViewFragment;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;
import com.pingplusplus.android.Pingpp;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by yi on 2016/12/2.
 */

public class WebViewActivity extends BaseAppCompatActivity implements SimpleContract.View {
    public static final String EXTRA_KEY_SHARE = "share";
    public static final String EXTRA_KEY_URL = "url";
    public static final String EXTRA_KEY_SHOW_TOOLBAR = "show_toolbar";
    public static final String EXTRA_KEY_SHOW_MORE_MENU = "isshowmoremenu";
    private static final String EXTRA_KEY_LABEL = "tagetkfkalabel";
    private static final String EXTRA_KEY_FIX_LABEL = "islabelfix";
    private final String FULL_SCREEN = "full_screen";
    private final int MENU_OPEN_OUT = 102;
    private final int MENU_OPEN_SHARE = 103;

    @BindView(R.id.include_toolbar)
    View mToolbar;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.iv_menu_list)
    ImageView mIvMenu;
    private WebViewFragment mWebViewFragment;

    @Inject
    SimplePresenter mPresenter;
    private String mUrl;
    private boolean mShouldTint = false;
    private boolean mHaveShare = false;
    private BottomMenuFragment bottomMenuFragment;
    private boolean ismHaveShare = true;
    private int type;
    private String num;
    private String per;
    private String lines;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_webview;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        DaggerSimpleComponent.builder()
                .simpleModule(new SimpleModule(this))
                .netComponent(MoeMoeApplication.getInstance().getNetComponent())
                .build()
                .inject(this);
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        AndroidBug5497Workaround.assistActivity(this);
        mProgressBar = (ProgressBar) findViewById(R.id.pgbar_progress);
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        String mTitle = getIntent().getStringExtra(EXTRA_KEY_LABEL);
        mUrl = getIntent().getStringExtra(EXTRA_KEY_URL);
        boolean mShowToolBar = getIntent().getBooleanExtra(EXTRA_KEY_SHOW_TOOLBAR, true);
        mHaveShare = getIntent().getBooleanExtra(EXTRA_KEY_SHARE, false);
        if (TextUtils.isEmpty(mUrl)) {
            mUrl = getIntent().getStringExtra(UUID);
            if (TextUtils.isEmpty(mUrl)) {
                finish();
            }
        }
        if (mShowToolBar && mUrl.contains(FULL_SCREEN)) {
            mShowToolBar = false;
        }
        if (mShowToolBar) {
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mTitle)) {
            mTvTitle.setTextColor(ContextCompat.getColor(this, R.color.main_cyan));
            mTvTitle.setText(mTitle);
        }
        mIvMenu.setVisibility(View.VISIBLE);
        mIvMenu.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (bottomMenuFragment != null) {
                    ismHaveShare = false;
                    bottomMenuFragment.show(getSupportFragmentManager(), "WebMenu");
                }

            }
        });
        initPopupMenus();
        mWebViewFragment = WebViewFragment.newInstance(mUrl);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mWebViewFragment).commit();
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }

    public void setmIvBack() {
        finish();
    }

    public void showShare(final int type) {//0 抽奖 1魔塔 2游戏
        this.type = type;
        if (bottomMenuFragment != null) {
            ismHaveShare = true;
            bottomMenuFragment.show(getSupportFragmentManager(), "WebShare");
        }
//        final OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        String url;
//        if (type == 0) {
//            oks.setTitle("来来来，扭蛋机每天免费给你抽宝贝！");
//            url = "http://prize.moemoe.la:8000/prize/share/";
//            oks.setText("异次元穿越而来的扭蛋机，每天三枚代币免费抽奖！隔三差五送出正版写真、一言不合秒充流量话费，" +
//                    "抽奖累积节操积分更能换取本子、高清、熟肉等”不可描述“珍稀资源。二次元没有骗局，前方真心高能！");
//            oks.setImageUrl("http://source.moemoe.la/static/image3/18.png");
//        } else if (type == 1) {
//            oks.setTitle("↑←↓→ Neta学园的高智商战斗剧情");
//            url = "http://ad.moemoe.la:8001/ad/ad_mota";
//            oks.setText("关于拯救校长这件事，能用武力解决就不用再讲道理了，即便只用↑←↓→，能通关的同学寥寥无几。");
//            oks.setImageUrl("http://s.moemoe.la/mota/h.jpg");
//        } else {
//            clickEvent("首页-分享");
//            oks.setTitle("打魔王竟然要靠手速？来挑战吧！");
//            url = "http://s.moemoe.la/game/devil/start.html";
//            oks.setText("你的好友用鬼畜般的手速对魔王造成了成吨的伤害，难道你不想挑战下？！");
//            oks.setImageUrl("http://s.moemoe.la/mowang2.png");
//        }
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//
//        oks.setTitleUrl(url);
//        // text是分享文本，所有平台都需要这个字段
//
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl(url);
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl(url);
//        // 启动分享GUIh
//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                if (mWebViewFragment != null) {
//                    if (type == 0) {
//                        mWebViewFragment.shareUrl();
//                    } else {
//                        MotaResult pay = new MotaResult();
//                        pay.setResult("success");
//                        pay.setType("share");
//                        Gson gson = new Gson();
//                        String temp = gson.toJson(pay);
//                        mWebViewFragment.resultMota(temp);
//                    }
//                    if (type == 2) {
//                        AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
//                        mPresenter.loadGameShare(authorInfo.getUserId());
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//                MotaResult pay = new MotaResult();
//                pay.setResult("fail");
//                pay.setType("share");
//                Gson gson = new Gson();
//                String temp = gson.toJson(pay);
//                mWebViewFragment.resultMota(temp);
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//                MotaResult pay = new MotaResult();
//                pay.setResult("cancel");
//                pay.setType("share");
//                Gson gson = new Gson();
//                String temp = gson.toJson(pay);
//                mWebViewFragment.resultMota(temp);
//                if (type == 2) {
//                    AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
//                    mPresenter.loadGameShare(authorInfo.getUserId());
//                }
//            }
//        });
//        oks.show(this);
    }

    public void showShare(String num, String per, String lines) {
        type = 10;
        clickEvent("结果-分享");
        this.num = num;
        this.per = per;
        this.lines = lines;
        if (bottomMenuFragment != null) {
            ismHaveShare = true;
            bottomMenuFragment.show(getSupportFragmentManager(), "WebShare");
        }
//        final OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        String url;
//        oks.setTitle("打魔王竟然要靠手速？来挑战吧！");
//        url = "http://s.moemoe.la/game/devil/challenge.html?num=" + num + "&per=" + per + "&lines=" + lines;
//        oks.setText("你的好友用鬼畜般的手速对魔王造成了成吨的伤害，难道你不想挑战下？！");
//        oks.setImageUrl("http://s.moemoe.la/mowang2.png");
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//
//        oks.setTitleUrl(url);
//        // text是分享文本，所有平台都需要这个字段
//
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl(url);
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite(getString(R.string.app_name));
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl(url);
//        // 启动分享GUI
//        oks.setCallback(new PlatformActionListener() {
//            @Override
//            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
//                if (mWebViewFragment != null) {
//                    MotaResult pay = new MotaResult();
//                    pay.setResult("success");
//                    pay.setType("share");
//                    Gson gson = new Gson();
//                    String temp = gson.toJson(pay);
//                    mWebViewFragment.resultMota(temp);
//                    AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
//                    mPresenter.loadGameShare(authorInfo.getUserId());
//                }
//            }
//
//            @Override
//            public void onError(Platform platform, int i, Throwable throwable) {
//                MotaResult pay = new MotaResult();
//                pay.setResult("fail");
//                pay.setType("share");
//                Gson gson = new Gson();
//                String temp = gson.toJson(pay);
//                mWebViewFragment.resultMota(temp);
//            }
//
//            @Override
//            public void onCancel(Platform platform, int i) {
//                MotaResult pay = new MotaResult();
//                pay.setResult("cancel");
//                pay.setType("share");
//                Gson gson = new Gson();
//                String temp = gson.toJson(pay);
//                mWebViewFragment.resultMota(temp);
//                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
//                mPresenter.loadGameShare(authorInfo.getUserId());
//            }
//        });
//        oks.show(this);
    }

    private void initPopupMenus() {
        bottomMenuFragment = new BottomMenuFragment();
        ArrayList<MenuItem> items = new ArrayList<>();
        MenuItem item;
        if (ismHaveShare) {
            item = new MenuItem(1, "QQ", R.drawable.btn_doc_option_send_qq);
            items.add(item);

            item = new MenuItem(2, "QQ空间", R.drawable.btn_doc_option_send_qzone);
            items.add(item);

            item = new MenuItem(3, "微信", R.drawable.btn_doc_option_send_wechat);
            items.add(item);

            item = new MenuItem(4, "微信朋友圈", R.drawable.btn_doc_option_send_pengyouquan);
            items.add(item);

            item = new MenuItem(5, "微博", R.drawable.btn_doc_option_send_weibo);
            items.add(item);
            bottomMenuFragment.setMenuType(BottomMenuFragment.TYPE_HORIZONTAL);
        } else {
            item = new MenuItem(MENU_OPEN_OUT, getString(R.string.label_open_out));
            items.add(item);
            if (mHaveShare) {
                MenuItem share = new MenuItem(MENU_OPEN_SHARE, getString(R.string.label_share));
                items.add(share);
            }
            bottomMenuFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
        }
        bottomMenuFragment.setMenuItems(items);
        bottomMenuFragment.setShowTop(false);
        bottomMenuFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                if (ismHaveShare) {
                    // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                    // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                    // text是分享文本，所有平台都需要这个字段
                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                    String title;
                    String titleUrl;
                    String text;
                    String imageUrl;
                    if (type == 0) {
                        title = "来来来，扭蛋机每天免费给你抽宝贝！";

                        titleUrl = "http://prize.moemoe.la:8000/prize/share/";
                        text = "异次元穿越而来的扭蛋机，每天三枚代币免费抽奖！隔三差五送出正版写真、一言不合秒充流量话费，" +
                                "抽奖累积节操积分更能换取本子、高清、熟肉等”不可描述“珍稀资源。二次元没有骗局，前方真心高能！";
                        imageUrl = "http://source.moemoe.la/static/image3/18.png";
                    } else if (type == 1) {
                        title = "↑←↓→ Neta学园的高智商战斗剧情";
                        titleUrl = "http://ad.moemoe.la:8001/ad/ad_mota";
                        text = "关于拯救校长这件事，能用武力解决就不用再讲道理了，即便只用↑←↓→，能通关的同学寥寥无几。";
                        imageUrl = "http://s.moemoe.la/mota/h.jpg";
                    } else if (type == 10) {
                        title = "打魔王竟然要靠手速？来挑战吧！";
                        titleUrl = "http://s.moemoe.la/game/devil/challenge.html?num=" + num + "&per=" + per + "&lines=" + lines;
                        text = "你的好友用鬼畜般的手速对魔王造成了成吨的伤害，难道你不想挑战下？！";
                        imageUrl = "http://s.moemoe.la/mowang2.png";
                    } else {
                        clickEvent("首页-分享");
                        title = "打魔王竟然要靠手速？来挑战吧！";
                        titleUrl = "http://s.moemoe.la/game/devil/start.html";
                        text = "你的好友用鬼畜般的手速对魔王造成了成吨的伤害，难道你不想挑战下？！";
                        imageUrl = "http://s.moemoe.la/mowang2.png";
                    }
                    switch (itemId) {
                        case 1:
                            ShareUtils.shareQQ(WebViewActivity.this, title, titleUrl, text, imageUrl, platformActionListener);
                            break;
                        case 2:
                            ShareUtils.shareQQzone(WebViewActivity.this, title, titleUrl, text, imageUrl, platformActionListener);
                            ;
                            break;
                        case 3:
                            ShareUtils.shareWechat(WebViewActivity.this, title, titleUrl, text, imageUrl, platformActionListener);
                            break;
                        case 4:
                            ShareUtils.sharepyq(WebViewActivity.this, title, titleUrl, text, imageUrl, platformActionListener);
                            break;
                        case 5:
                            ShareUtils.shareWeibo(WebViewActivity.this, title, titleUrl, text, imageUrl, platformActionListener);
                            break;

                    }
                } else {
                    if (itemId == MENU_OPEN_OUT) {
                        Uri uri = Uri.parse(mUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    } else if (itemId == MENU_OPEN_SHARE) {
                        showShare(0);
                    }
                }
            }
        });
    }

    /**
     * 分享回调
     */
    PlatformActionListener platformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            if (mWebViewFragment != null) {
                if (type == 0) {
                    mWebViewFragment.shareUrl();
                } else if (type == 10) {
                    MotaResult pay = new MotaResult();
                    pay.setResult("success");
                    pay.setType("share");
                    Gson gson = new Gson();
                    String temp = gson.toJson(pay);
                    mWebViewFragment.resultMota(temp);
                    AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                    mPresenter.loadGameShare(authorInfo.getUserId());
                } else {
                    MotaResult pay = new MotaResult();
                    pay.setResult("success");
                    pay.setType("share");
                    Gson gson = new Gson();
                    String temp = gson.toJson(pay);
                    mWebViewFragment.resultMota(temp);
                }
                if (type == 2) {
                    AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                    mPresenter.loadGameShare(authorInfo.getUserId());
                }
            }
            Log.e("kid", "分享成功");
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            if (type == 10) {
                MotaResult pay = new MotaResult();
                pay.setResult("fail");
                pay.setType("share");
                Gson gson = new Gson();
                String temp = gson.toJson(pay);
                mWebViewFragment.resultMota(temp);
            } else {
                MotaResult pay = new MotaResult();
                pay.setResult("fail");
                pay.setType("share");
                Gson gson = new Gson();
                String temp = gson.toJson(pay);
                mWebViewFragment.resultMota(temp);
            }
            Log.e("kid", "分享失败");
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }

        @Override
        public void onCancel(Platform platform, int i) {
            if (type == 10) {
                MotaResult pay = new MotaResult();
                pay.setResult("cancel");
                pay.setType("share");
                Gson gson = new Gson();
                String temp = gson.toJson(pay);
                mWebViewFragment.resultMota(temp);
                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
                mPresenter.loadGameShare(authorInfo.getUserId());
            } else {
                MotaResult pay = new MotaResult();
                pay.setResult("fail");
                pay.setType("share");
                Gson gson = new Gson();
                String temp = gson.toJson(pay);
                mWebViewFragment.resultMota(temp);
            }
            Log.e("kid", "分享取消");
            if (bottomMenuFragment != null) {
                bottomMenuFragment.dismiss();
            }
        }

    };

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_KEY_URL, url);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, boolean needToolBar, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_KEY_URL, url);
        intent.putExtra(EXTRA_KEY_SHOW_TOOLBAR, needToolBar);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String url, boolean needMoreMenu) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_KEY_URL, url);
        intent.putExtra(EXTRA_KEY_SHOW_MORE_MENU, needMoreMenu);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String url, String label, boolean needMoreMenu, boolean titleEditable) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_KEY_URL, url);
        intent.putExtra(EXTRA_KEY_LABEL, label);
        intent.putExtra(EXTRA_KEY_SHOW_MORE_MENU, needMoreMenu);
        intent.putExtra(EXTRA_KEY_FIX_LABEL, titleEditable);
        context.startActivity(intent);
    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void cancelProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPageStarted(String url) {
        showProgressBar();
    }

    @Override
    public void showCustomView(View view, WebChromeClient.CustomViewCallback callback, boolean isUseNew) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mShouldTint) {
            //tintManager.setStatusBarTintEnabled(false);
            // 设置状态栏的颜色
            //tintManager.setStatusBarTintResource(R.color.black);
            getWindow().getDecorView().setFitsSystemWindows(false);
        }
        mToolbar.setVisibility(View.GONE);
    }

    @Override
    public void hideCustomView(boolean isUseNew) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mShouldTint) {
            //tintManager.setStatusBarTintEnabled(true);
            // 设置状态栏的颜色
            //tintManager.setStatusBarTintResource(R.color.main_title_cyan);
            getWindow().getDecorView().setFitsSystemWindows(true);
        }
        mToolbar.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            showToast(R.string.msg_connection);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebViewFragment == null || mWebViewFragment.mChromeClient == null)
            super.onBackPressed();
        if (!mWebViewFragment.mChromeClient.onBackPressed()) {
            //if(mWebViewFragment.mWebView.canGoBack()){
            //     mWebViewFragment.mWebView.goBack();
            //  }else {
            super.onBackPressed();
            // }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
//            if (resultCode == Activity.RESULT_OK) {
//                String result = data.getExtras().getString("pay_result");
//                /* 处理返回值
//                 * "success" - payment succeed
//                 * "fail"    - payment failed
//                 * "cancel"  - user canceld
//                 * "invalid" - payment plugin not installed
//                 */
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                // showMsg(result, errorMsg, extraMsg);
//                MotaResult pay = new MotaResult();
//                pay.setResult(result);
//                pay.setType("pay");
//                Gson gson = new Gson();
//                String temp = gson.toJson(pay);
//                mWebViewFragment.resultMota(temp);
//            }
        }
    }

    public void resultMota(String temp) {
        mWebViewFragment.resultMota(temp);
    }


    public void saveLive2d() {
        mPresenter.doRequest("space", 9);
    }

    @Override
    public void onFailure(int code, String msg) {
    }

    @Override
    public void onSuccess(Object o) {
    }

    @Override
    public void onLoadGameShareSuccess() {
        if (mWebViewFragment != null) {
            mWebViewFragment.reload();
        }
    }
}
