package com.moemoe.lalala.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.moemoe.lalala.R;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by KID on 2017/12/29.
 * 一切AppID和Key请在ShareSDK.xml中配置
 * ShareSDK分享工具类
 */

public class ShareUtils {
    //QQ好友分享
    public static void shareQQ(Context context, String title, String shareurl, String text, String imageUrl, PlatformActionListener listener) {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.SSOSetting(true);
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(shareurl); // 标题的超链接
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        qq.setPlatformActionListener(listener);
        qq.share(sp);
    }

    //QQ好友分享
    public static void shareQQBimtp(Context context, String title, String shareurl, String text, String imageUrl, PlatformActionListener listener) {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.SSOSetting(true);
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle(title);
        sp.setTitleUrl(shareurl); // 标题的超链接
        sp.setText(text);
        sp.setImagePath(imageUrl);
        qq.setPlatformActionListener(listener);
        qq.share(sp);
    }

    //QQ空间
    public static void shareQQzone(Context context, String title, String shareurl, String text, String imageUrl, PlatformActionListener listener) {
        Platform qqZone = ShareSDK.getPlatform(QZone.NAME);
        qqZone.SSOSetting(true);
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle(title);
        sp.setTitleUrl(shareurl); // 标题的超链接
        sp.setSiteUrl(shareurl);
        sp.setSite(context.getResources().getString(R.string.app_name));
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        qqZone.setPlatformActionListener(listener);
        qqZone.share(sp);
    }

    //QQ空间
    public static void shareQQzoneBitmap(Context context, String title, String shareurl, String text, String imageUrl, PlatformActionListener listener) {
        Platform qqZone = ShareSDK.getPlatform(QZone.NAME);
        qqZone.SSOSetting(true);
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle(title);
        sp.setTitleUrl(shareurl); // 标题的超链接
        sp.setSiteUrl(shareurl);
        sp.setSite(context.getResources().getString(R.string.app_name));
        sp.setText(text);
        sp.setImagePath(imageUrl);
        qqZone.setPlatformActionListener(listener);
        qqZone.share(sp);
    }

    //新浪微博
    public static void shareWeibo(Context context, String title, String shareurl, String text, String imageUrl, PlatformActionListener listener) {
        Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);


        sina.SSOSetting(true);
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(shareurl); // 标题的超链接
        sp.setSiteUrl(shareurl);
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        sina.setPlatformActionListener(listener);
        sina.share(sp);
    }

    //新浪微博
    public static void shareWeiboBitMap(Context context, String title, String shareurl, String text, Bitmap imageUrl, PlatformActionListener listener) {
        Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
        sina.SSOSetting(true);
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setTitle(title);
        sp.setTitleUrl(shareurl); // 标题的超链接
        sp.setSiteUrl(shareurl);
        sp.setText(text);
        sp.setImageData(imageUrl);
        sina.setPlatformActionListener(listener);
        sina.share(sp);
    }

    //微信
    public static void shareWechat(Context context, String title, String shareurl, String text, String imageUrl, PlatformActionListener listener) {
        Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
        weixin.SSOSetting(true);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setImageUrl(imageUrl);
        sp.setUrl(shareurl);
        sp.setText(text);
        sp.setSite(context.getResources().getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        weixin.setPlatformActionListener(listener);
        weixin.share(sp);
    }

    //微信
    public static void shareWechatBitmap(Context context, String title, String shareurl, String text, Bitmap imageUrl, PlatformActionListener listener) {
        Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
        weixin.SSOSetting(true);
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle(title);
        sp.setImageData(imageUrl);
        sp.setUrl(shareurl);
        sp.setText(text);
        sp.setSite(context.getResources().getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        weixin.setPlatformActionListener(listener);
        weixin.share(sp);
    }

    //微信朋友圈
    public static void sharepyq(Context context, String title, String shareurl, String text, String imageUrl, PlatformActionListener listener) {
        Platform weixin = ShareSDK.getPlatform(WechatMoments.NAME);
        weixin.SSOSetting(true);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_WEBPAGE);
        sp.setTitle(title);
        sp.setImageUrl(imageUrl);
        sp.setUrl(shareurl);
        sp.setText(text);
        sp.setSite(context.getResources().getString(R.string.app_name));
        weixin.setPlatformActionListener(listener);
        weixin.share(sp);
    }

    //微信朋友圈
    public static void sharepyqBitmap(Context context, String title, String shareurl, String text, Bitmap imageUrl, PlatformActionListener listener) {
        Platform weixin = ShareSDK.getPlatform(WechatMoments.NAME);
        weixin.SSOSetting(true);
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle(title);
        sp.setImageData(imageUrl);
        sp.setUrl(shareurl);
        sp.setText(text);
        sp.setSite(context.getResources().getString(R.string.app_name));
        weixin.setPlatformActionListener(listener);
        weixin.share(sp);
    }
}