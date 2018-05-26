package com.moemoe.lalala.view.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.MapMarkContainer;
import com.moemoe.lalala.utils.DialogUtils;
import com.moemoe.lalala.utils.MapToolTipUtils;
import com.moemoe.lalala.utils.NetworkUtils;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.widget.map.MapWidget;
import com.moemoe.lalala.view.widget.map.config.OfflineMapConfig;
import com.moemoe.lalala.view.widget.map.events.MapTouchedEvent;
import com.moemoe.lalala.view.widget.map.events.ObjectTouchEvent;
import com.moemoe.lalala.view.widget.map.interfaces.OnMapTouchListener;
import com.moemoe.lalala.view.widget.view.CircleImageView;
import com.moemoe.lalala.view.widget.view.PileView;

import java.util.List;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Sora on 2018/4/11.
 * 宅屋界面
 */

public class DormitoryActivity extends BaseAppCompatActivity {

    @BindView(R.id.fl_map_root)
    FrameLayout mMap;
    @BindView(R.id.iv_left)
    ImageView mIvLeft;
    @BindView(R.id.iv_right)
    ImageView mIvRight;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.iv_personal)
    ImageView mIvUser;
    @BindView(R.id.ll_tool_bar)
    LinearLayout mLlToolBar;
    @BindView(R.id.dormitory_storage)
    Button mStorage;
    @BindView(R.id.dormitory_role)
    Button mRole;
    @BindView(R.id.dormitory_drama)
    Button mDrama;
    @BindView(R.id.visitor_info)
    RelativeLayout mVisitorInfo;
    @BindView(R.id.visitor_count)
    TextView mVisitorCount;

    private int Test_visitor_count = 66;


    private PileView mPileLayout;

    private MapWidget mapWidget;// 0 map 1 event 2 allUser 3 birthdayUser 4 followUser 5 nearUser
    private MapMarkContainer mContainer;
    private boolean mIsOut = false;

    public static final int VISIBLE_COUNT = 3;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_dormitory;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), null);
        MapToolTipUtils.getInstance().init(this, 5, 8, mapWidget, mMap);
        mPileLayout = findViewById(R.id.pile_layout);
        initMap("home_asa");
        initPraises();
    }

    private void initMap(String map) {
        mapWidget = new MapWidget(this, map, 12);
        mapWidget.centerMap();
        OfflineMapConfig config = mapWidget.getConfig();
        mapWidget.scrollMapTo(0, 0);
        config.setPinchZoomEnabled(true);
        config.setFlingEnabled(true);
        config.setMaxZoomLevelLimit(14);
        config.setMinZoomLevelLimit(12);
        config.setZoomBtnsVisible(false);
        config.setMapCenteringEnabled(true);
        mMap.removeAllViews();
        mMap.addView(mapWidget);
        MapToolTipUtils.getInstance().updateMap(mapWidget);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {

        mapWidget.setOnMapTouchListener(new OnMapTouchListener() {
            @Override
            public void onTouch(MapWidget v, MapTouchedEvent event) {
                List<ObjectTouchEvent> objectTouchEvents = event.getTouchedObjectEvents();
                if (objectTouchEvents.size() == 0) {
                    if (mIsOut) {
                        imgIn();
                        mIsOut = false;
                    } else {
                        imgOut();
                        mIsOut = true;
                    }
                }


//                if (objectTouchEvents.size() == 1) {
//                    ObjectTouchEvent objectTouchEvent = objectTouchEvents.get(0);
//                    Object objectId = objectTouchEvent.getObjectId();
//                    MapMarkEntity entity = mContainer.getMarkById((String) objectId);
//                    // MapMarkEntity entity1 = null;
//                    //  if(mContainerEvent!=null)entity1 = mContainerEvent.getMarkById((String) objectId);
////                    if(entity1!=null) {
////                        entity = entity1;
////                    }else {
////                        entity = entity2;
////                    }
//
//                    if (objectTouchEvent.getLayerId() == 0) {
//                        //埋点统计：用id做判断点击的是哪个位置
//                        String id = entity.getId();
//                        clickEvent(id);
//                    } else if (objectTouchEvent.getLayerId() == 2) {
//                        //埋点统计：用id做判断点击的是哪个位置
//                        clickEvent("全部用户的位置");
//                    } else if (objectTouchEvent.getLayerId() == 3) {
//                        clickEvent("生日派对区域");
//                    } else if (objectTouchEvent.getLayerId() == 4) {
//                        clickEvent("我的好友区域");
//                    } else if (objectTouchEvent.getLayerId() == 5) {
//                        clickEvent("附近的人");
//                    } else if (objectTouchEvent.getLayerId() == 6) {
//                        clickEvent("最佳形象区域");
//                    } else {
//                        //埋点统计：用id做判断点击的是哪个位置
//                        String id = entity.getId();
//                        clickEvent(id);
//                    }
//
//                    if (!TextUtils.isEmpty(entity.getSchema())) {
//                        String temp = entity.getSchema();
//                        if (temp.contains("map_event_1.0") || temp.contains("game_1.0")) {
//                            if (!DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
//                                return;
//                            }
//                        }
//
//                        if (entity.getId().equals("恋爱讲座")) {
//                            if (menuFragment == null) {
//                                ArrayList<MenuItem> items = new ArrayList<>();
//                                MenuItem item = new MenuItem(0, "赤印");
//                                items.add(item);
//                                item = new MenuItem(1, "雪之本境");
//                                items.add(item);
//                                item = new MenuItem(2, "且听琴语");
//                                items.add(item);
//                                menuFragment = new BottomMenuFragment();
//                                menuFragment.setShowTop(true);
//                                menuFragment.setTopContent("选择听哪个故事呢？");
//                                menuFragment.setMenuItems(items);
//                                menuFragment.setMenuType(BottomMenuFragment.TYPE_VERTICAL);
//                                menuFragment.setmClickListener(new BottomMenuFragment.MenuItemClickListener() {
//                                    @Override
//                                    public void OnMenuItemClick(int itemId) {
//                                        String url = "";
//                                        if (itemId == 0) {
//                                            url = "https://www.iqing.in/play/653";
//                                        } else if (itemId == 1) {
//                                            url = "https://www.iqing.in/play/654";
//                                        } else if (itemId == 2) {
//                                            url = "https://www.iqing.in/play/655";
//                                        }
//                                        WebViewActivity.startActivity(MapActivity.this, url, true);
//                                    }
//                                });
//                            }
//                            menuFragment.show(getSupportFragmentManager(), "mapMenu");
//                        } else if (entity.getId().equals("扭蛋机抽奖")) {
//                            if (DialogUtils.checkLoginAndShowDlg(MapActivity.this)) {
//
//                                //埋点统计：用id做判断点击的是哪个位置
//                                String id = entity.getId();
//                                clickEvent(id);
//
//                                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
//                                try {
//                                    temp += "?user_id=" + authorInfo.getUserId()
//                                            + "&nickname=" + (TextUtils.isEmpty(authorInfo.getUserName()) ? "" : URLEncoder.encode(authorInfo.getUserName(), "UTF-8"))
//                                            + "&token=" + PreferenceUtils.getToken();
//                                    Uri uri = Uri.parse(temp);
//                                    IntentUtils.haveShareWeb(MapActivity.this, uri, v);
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        } else {
//                            if (temp.contains("http://s.moemoe.la/game/devil/index.html")) {
//                                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
//                                temp += "?user_id=" + authorInfo.getUserId() + "&full_screen";
//                            }
////                            if (temp.contains("http://192.168.1.186:8020/ceshiN/index.html")) {
////                                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
////                                temp += "?user_id=" + authorInfo.getUserId() + "&full_screen";
////                            }
//                            if (temp.contains("http://kiratetris.leanapp.cn/tab001/index.html")) {
//                                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
//                                temp += "?id=" + authorInfo.getUserId() + "&name=" + authorInfo.getUserName();
//                            }
//                            if (temp.contains("http://prize.moemoe.la:8000/mt")) {
//                                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
//                                temp += "?user_id=" + authorInfo.getUserId() + "&nickname=" + authorInfo.getUserName();
//                            }
//                            if (temp.contains("http://prize.moemoe.la:8000/netaopera/chap")) {
//                                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
//                                temp += "?pass=" + PreferenceUtils.getPassEvent(MapActivity.this) + "&user_id=" + authorInfo.getUserId();
//                            }
//                            if (temp.contains("http://neta.facehub.me/")) {
//                                AuthorInfo authorInfo = PreferenceUtils.getAuthorInfo();
//                                temp += "?open_id=" + authorInfo.getUserId() + "&nickname=" + authorInfo.getUserName() + "&pay_way=alipay,wx,qq" + "&full_screen";
//                            }
//                            if (temp.contains("fanxiao/final.html")) {
//                                temp += "?token=" + PreferenceUtils.getToken()
//                                        + "&full_screen";
//                            }
//                            if (temp.contains("fanxiao/paihang.html")) {
//                                temp += "?token=" + PreferenceUtils.getToken();
//                            }
//                            if (temp.contains("game_1.0")) {
//                                temp += "&token=" + PreferenceUtils.getToken() + "&version=" + AppSetting.VERSION_CODE + "&userId=" + PreferenceUtils.getUUid() + "&channel=" + AppSetting.CHANNEL;
//                            }
//
//
//                            Uri uri = Uri.parse(temp);
//                            IntentUtils.toActivityFromUri(MapActivity.this, uri, v);
//                        }
//                    }
//                }
            }
        });

        mIvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkUtils.checkNetworkAndShowError(DormitoryActivity.this) && DialogUtils.checkLoginAndShowDlg(DormitoryActivity.this)) {
                    //埋点统计：手机个人中心
                    clickEvent("个人中心");
                    Intent i1 = new Intent(DormitoryActivity.this, PersonalV2Activity.class);
                    i1.putExtra(UUID, PreferenceUtils.getUUid());
                    startActivity(i1);
                }
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DormitoryActivity.this, AllSearchActivity.class);
                intent.putExtra("type", "all");
                startActivity(intent);
            }
        });
        mIvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(DormitoryActivity.this, FeedV3Activity.class);
                startActivity(i3);
                finish();
            }
        });
        mIvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DormitoryActivity.this, RoleActivity.class);
                startActivity(intent);
            }
        });
        mStorage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DormitoryActivity.this, ReloadingRoomActivity.class);
                startActivity(intent);
            }
        });
        mDrama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DormitoryActivity.this, DormitoryDramaActivity.class);
                startActivity(intent);
            }
        });
        mVisitorInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    /**
     * 初始化Praise
     */
    public void initPraises() {
        String[] urls = {"http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=2263418180,3668836868&fm=206&gp=0.jpg"};
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < 4; i++) {
            CircleImageView imageView = (CircleImageView) inflater.inflate(R.layout.item_praise, mPileLayout, false);
            Glide.with(this).load(urls[i]).into(imageView);
            mPileLayout.addView(imageView);
        }
    }

    @Override
    protected void initData() {
        mVisitorCount.setText(Test_visitor_count + "");
        if (TextUtils.isEmpty(PreferenceUtils.getUUid())) {
            mIvUser.setImageResource(R.drawable.bg_default_circle);
        } else {
            int size = (int) getResources().getDimension(R.dimen.x64);
            Glide.with(this)
                    .load(StringUtils.getUrl(this, PreferenceUtils.getAuthorInfo().getHeadPath(), size, size, false, true))
                    .error(R.drawable.bg_default_circle)
                    .placeholder(R.drawable.bg_default_circle)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(mIvUser);
        }
    }

    private void imgIn() {
        ObjectAnimator phoneAnimator = ObjectAnimator.ofFloat(mLlToolBar, "translationY", -mLlToolBar.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        phoneAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mRoleAnimator = ObjectAnimator.ofFloat(mRole, "translationY", mRole.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mRoleAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mStorageAnimator = ObjectAnimator.ofFloat(mStorage, "translationY", mStorage.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mStorageAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mDramaAnimator = ObjectAnimator.ofFloat(mDrama, "translationY", mDrama.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mDramaAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mVisitorInfoAnimator = ObjectAnimator.ofFloat(mVisitorInfo, "translationY", mVisitorInfo.getHeight() - getResources().getDimension(R.dimen.y60), 0).setDuration(300);
        mVisitorInfoAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(phoneAnimator).with(mRoleAnimator);
        set.play(mStorageAnimator).with(mDramaAnimator);
        set.play(mVisitorInfoAnimator);
        set.start();
    }

    private void imgOut() {
        ObjectAnimator phoneAnimator = ObjectAnimator.ofFloat(mLlToolBar, "translationY", 0, -getResources().getDimension(R.dimen.y60) - mLlToolBar.getHeight()).setDuration(300);
        phoneAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mStorageAnimator = ObjectAnimator.ofFloat(mStorage, "translationY", 0, -getResources().getDimension(R.dimen.y60) - -mStorage.getHeight() * 2).setDuration(300);
        mStorageAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mRoleAnimator = ObjectAnimator.ofFloat(mRole, "translationY", 0, -getResources().getDimension(R.dimen.y60) - -mRole.getHeight() * 2).setDuration(300);
        mRoleAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mDramaAnimator = ObjectAnimator.ofFloat(mDrama, "translationY", 0, -getResources().getDimension(R.dimen.y60) - -mDrama.getHeight() * 2).setDuration(300);
        mDramaAnimator.setInterpolator(new OvershootInterpolator());
        ObjectAnimator mVisitorInfoAnimator = ObjectAnimator.ofFloat(mVisitorInfo, "translationY", 0, -getResources().getDimension(R.dimen.y10) - -mVisitorInfo.getHeight() * 2).setDuration(300);
        mVisitorInfoAnimator.setInterpolator(new OvershootInterpolator());
        AnimatorSet set = new AnimatorSet();
        set.play(phoneAnimator).with(mRoleAnimator);
        set.play(mStorageAnimator).with(mDramaAnimator);
        set.play(mVisitorInfoAnimator);
        set.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideBtn();
        MapToolTipUtils.getInstance().stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showBtn();
    }

    private void clearMap() {
        if (mapWidget != null) {
            mContainer = new MapMarkContainer();
            mapWidget.clearLayers();
            mapWidget = null;
        }
    }


    private void showBtn() {
        mLlToolBar.setVisibility(View.VISIBLE);
    }

    private void hideBtn() {
        mLlToolBar.setVisibility(View.INVISIBLE);
    }

}
