package com.moemoe.lalala.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.moemoe.lalala.R;
import com.moemoe.lalala.utils.ShareModel;
import com.moemoe.lalala.utils.ShareUtils;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.view.widget.netamenu.BottomMenuFragment;
import com.moemoe.lalala.view.widget.netamenu.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Hygge on 2018/4/24.
 */

public class ShareSDKActivity extends BaseAppCompatActivity {


    @BindView(R.id.back)
    Button button;
    private BottomMenuFragment bottomMenuFragment;
    private Platform.ShareParams shareParams;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_share_sdk;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        initPopupMenus();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bottomMenuFragment != null)
                    bottomMenuFragment.show(getSupportFragmentManager(), "share");
            }
        });

    }

    private void initPopupMenus() {
        bottomMenuFragment = new BottomMenuFragment();
        ArrayList<com.moemoe.lalala.view.widget.netamenu.MenuItem> items = new ArrayList<>();

        MenuItem item = new MenuItem(1, "QQ", R.drawable.btn_doc_option_send_qq);
        items.add(item);

        item = new MenuItem(2, "QQ空间", R.drawable.btn_doc_option_send_qzone);
        items.add(item);

        item = new MenuItem(3, "微信", R.drawable.btn_doc_option_send_wechat);
        items.add(item);

        item = new MenuItem(4, "朋友圈", R.drawable.btn_doc_option_send_pengyouquan);
        items.add(item);

        item = new MenuItem(5, "微博", R.drawable.btn_doc_option_send_weibo);
        items.add(item);


        bottomMenuFragment.setShowTop(false);
        bottomMenuFragment.setMenuItems(items);
        bottomMenuFragment.setMenuType(BottomMenuFragment.TYPE_HORIZONTAL);
        bottomMenuFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
            @Override
            public void OnMenuItemClick(int itemId) {
                switch (itemId) {
                    case 1:
                        ShareUtils.shareQQ("来来来，扭蛋机每天免费给你抽宝贝！", "http://prize.moemoe.la:8000/prize/share/", "异次元穿越而来的扭蛋机，每天三枚代币免费抽奖！隔三差五送出正版写真、一言不合秒充流量话费，" +
                                "抽奖累积节操积分更能换取本子、高清、熟肉等”不可描述“珍稀资源。二次元没有骗局，前方真心高能！", "http://source.moemoe.la/static/image3/18.png", platformActionListener);
                        break;
                    case 2:
                        ShareUtils.shareQQzone("来来来，扭蛋机每天免费给你抽宝贝！", "http://prize.moemoe.la:8000/prize/share/", "异次元穿越而来的扭蛋机，每天三枚代币免费抽奖！隔三差五送出正版写真、一言不合秒充流量话费，" +
                                "抽奖累积节操积分更能换取本子、高清、熟肉等”不可描述“珍稀资源。二次元没有骗局，前方真心高能！", "http://source.moemoe.la/static/image3/18.png", platformActionListener);
                        ;
                        break;
                    case 3:
                        ShareUtils.shareWechat("来来来，扭蛋机每天免费给你抽宝贝！", "http://prize.moemoe.la:8000/prize/share/", "异次元穿越而来的扭蛋机，每天三枚代币免费抽奖！隔三差五送出正版写真、一言不合秒充流量话费，" +
                                "抽奖累积节操积分更能换取本子、高清、熟肉等”不可描述“珍稀资源。二次元没有骗局，前方真心高能！", "http://source.moemoe.la/static/image3/18.png", platformActionListener);
                        break;
                    case 4:
                        ShareUtils.sharepyq("来来来，扭蛋机每天免费给你抽宝贝！", "http://prize.moemoe.la:8000/prize/share/", "异次元穿越而来的扭蛋机，每天三枚代币免费抽奖！隔三差五送出正版写真、一言不合秒充流量话费，" +
                                "抽奖累积节操积分更能换取本子、高清、熟肉等”不可描述“珍稀资源。二次元没有骗局，前方真心高能！", "http://source.moemoe.la/static/image3/18.png", platformActionListener);
                        break;
                    case 5:
                        ShareUtils.shareWeibo("来来来，扭蛋机每天免费给你抽宝贝！", "http://prize.moemoe.la:8000/prize/share/", "异次元穿越而来的扭蛋机，每天三枚代币免费抽奖！隔三差五送出正版写真、一言不合秒充流量话费，" +
                                "抽奖累积节操积分更能换取本子、高清、熟肉等”不可描述“珍稀资源。二次元没有骗局，前方真心高能！", "http://source.moemoe.la/static/image3/18.png", platformActionListener);
                        break;

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
            Log.e("kid", "分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.e("kid", "分享失败");
        }

        @Override
        public void onCancel(Platform platform, int i) {
            Log.e("kid", "分享取消");
        }
    };

    /**
     * 分享
     *
     * @param position
     */
    private void share(int position) {

        if (position == 1) {
            qq();
        } else if (position == 4) {
            qzone();
        } else if (position == 5) {
            shortMessage();
        } else {
            Platform plat = null;
            plat = ShareSDK.getPlatform(getPlatform(position));
//            if (platformActionListener != null) {
//                plat.setPlatformActionListener(platformActionListener);
//            }

            plat.share(shareParams);
        }
    }

    /**
     * 获取平台
     *
     * @param position
     * @return
     */
    private String getPlatform(int position) {
        String platform = "";
        switch (position) {
            case 0:
                platform = "Wechat";
                break;
            case 1:
                platform = "QQ";
                break;
            case 2:
                platform = "SinaWeibo";
                break;
            case 3:
                platform = "WechatMoments";
                break;
            case 4:
                platform = "QZone";
                break;
            case 5:
                platform = "ShortMessage";
                break;
        }
        return platform;
    }

    /**
     * 分享到QQ空间
     */
    private void qzone() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接  
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());

        Platform qzone = ShareSDK.getPlatform("QZone");

//        qzone.setPlatformActionListener(platformActionListener); // 设置分享事件回调 //  
        // 执行图文分享  
        qzone.share(sp);
    }

    private void qq() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(shareParams.getTitle());
        sp.setTitleUrl(shareParams.getUrl()); // 标题的超链接  
        sp.setText(shareParams.getText());
        sp.setImageUrl(shareParams.getImageUrl());
        sp.setComment("我对此分享内容的评论");
        sp.setSite(shareParams.getTitle());
        sp.setSiteUrl(shareParams.getUrl());
        Platform qq = ShareSDK.getPlatform("QQ");
//        qq.setPlatformActionListener(platformActionListener);
        qq.share(sp);
    }

    /**
     * 分享到短信
     */
    private void shortMessage() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setAddress("");
        sp.setText(shareParams.getText() + "这是网址《" + shareParams.getUrl() + "》很给力哦！");

        Platform circle = ShareSDK.getPlatform("ShortMessage");
//        circle.setPlatformActionListener(platformActionListener); // 设置分享事件回调  
        // 执行图文分享  
        circle.share(sp);
    }

    /**
     * 初始化分享参数
     *
     * @param shareModel
     */
    public void initShareParams(ShareModel shareModel) {
        if (shareModel != null) {
            Platform.ShareParams sp = new Platform.ShareParams();
            sp.setShareType(Platform.SHARE_TEXT);
            sp.setShareType(Platform.SHARE_WEBPAGE);

            sp.setTitle(shareModel.getText());
            sp.setText(shareModel.getText());
            sp.setUrl(shareModel.getUrl());
            sp.setImageUrl(shareModel.getImageUrl());
            shareParams = sp;
        }
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
}
