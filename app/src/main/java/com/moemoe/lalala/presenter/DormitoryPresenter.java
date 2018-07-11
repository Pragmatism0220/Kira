package com.moemoe.lalala.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.moemoe.lalala.R;
import com.moemoe.lalala.greendao.gen.HouseDbEntityDao;
import com.moemoe.lalala.greendao.gen.MapDbEntityDao;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.api.NetResultSubscriber;
import com.moemoe.lalala.model.api.NetSimpleResultSubscriber;
import com.moemoe.lalala.model.entity.HouseDbEntity;
import com.moemoe.lalala.model.entity.HouseLikeEntity;
import com.moemoe.lalala.model.entity.HouseMarkContainer;
import com.moemoe.lalala.model.entity.MapDbEntity;
import com.moemoe.lalala.model.entity.MapEntity;
import com.moemoe.lalala.model.entity.MapMarkContainer;
import com.moemoe.lalala.model.entity.MapMarkEntity;
import com.moemoe.lalala.model.entity.NearUserEntity;
import com.moemoe.lalala.model.entity.PowerEntity;
import com.moemoe.lalala.model.entity.PropUseEntity;
import com.moemoe.lalala.model.entity.RubbishEntity;
import com.moemoe.lalala.model.entity.RubblishBody;
import com.moemoe.lalala.model.entity.SaveVisitorEntity;
import com.moemoe.lalala.model.entity.VisitorsEntity;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.GreenDaoManager;
import com.moemoe.lalala.utils.PreferenceUtils;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.view.widget.map.MapLayout;
import com.moemoe.lalala.view.widget.map.MapWidget;
import com.moemoe.lalala.view.widget.map.interfaces.Layer;
import com.moemoe.lalala.view.widget.map.model.MapObject;
import com.moemoe.lalala.view.widget.view.HouseView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.DuplicateFormatFlagsException;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Hygge on 2018/5/24.
 */

public class DormitoryPresenter implements DormitoryContract.Presenter {
    private DormitoryContract.View view;
    private ApiService apiService;

    @Inject
    public DormitoryPresenter(DormitoryContract.View view, ApiService apiService) {
        this.view = view;
        this.apiService = apiService;
    }


    @Override
    public void release() {
        view = null;
    }

    @Override
    public void loadHouseObjects(boolean isHouse, String visitorId) {
        if (isHouse) {
            apiService.loadHouseObjects()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<MapEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<MapEntity> entities) {
                            if (view != null) view.onLoadHouseObjects(entities);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        } else {
            apiService.loadHouseObjects(visitorId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new NetResultSubscriber<ArrayList<MapEntity>>() {
                        @Override
                        public void onSuccess(ArrayList<MapEntity> entities) {
                            if (view != null) view.onLoadHouseObjects(entities);
                        }

                        @Override
                        public void onFail(int code, String msg) {
                            if (view != null) view.onFailure(code, msg);
                        }
                    });
        }
    }

    @Override
    public void loadPower() {
        apiService.power()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<PowerEntity>() {
                    @Override
                    public void onSuccess(PowerEntity entity) {
                        if (view != null) view.onLoadPowerSuccess(entity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void houseToolUse(String toolId) {
        apiService.propUse(toolId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<PropUseEntity>() {
                    @Override
                    public void onSuccess(PropUseEntity entity) {
                        if (view != null) view.onLoadHouseToolUse(entity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }


    @Override
    public void loadRoleLikeCollect(String roleId) {
        apiService.loadRoleLikeCollect(roleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<HouseLikeEntity>() {
                    @Override
                    public void onSuccess(HouseLikeEntity entity) {
                        if (view != null) view.onLoadRoleLikeCollect(entity);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void getVisitorsInfo() {
        apiService.loadVisitor(ApiService.LENGHT, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<VisitorsEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<VisitorsEntity> entities) {
                        if (view != null) view.getVisitorsInfoSuccess(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void getHiVisitorsInfo(String userId) {
        apiService.loadHisVisitor(ApiService.LENGHT, 0, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<ArrayList<VisitorsEntity>>() {
                    @Override
                    public void onSuccess(ArrayList<VisitorsEntity> entities) {
                        if (view != null) view.getHisVisitorsInfo(entities);
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }


    @Override
    public void saveVisitor(SaveVisitorEntity request) {
        apiService.saveVisitor(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                    @Override
                    public void onSuccess() {
                        if (view != null) view.saveVisitorSuccess();
                    }

                    @Override
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });

    }

    @Override
    public void loadHouseRubblish(RubblishBody userRubbishId) {
        apiService.loadHouseRubblish(userRubbishId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetResultSubscriber<RubbishEntity>() {
                    @Override
                    public void onSuccess(RubbishEntity entity) {
                        if (view != null) view.onLoadHouseRubblish(entity);
                    }

                    @Override
                    
                    public void onFail(int code, String msg) {
                        if (view != null) view.onFailure(code, msg);
                    }
                });
    }

    @Override
    public void loadHouseSave(RubblishBody toolId) {
        apiService.loadHouseSave(toolId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetSimpleResultSubscriber() {
                               @Override
                               public void onSuccess() {
                                   if (view != null) view.onLoadHouseSave();
                               }

                               @Override
                               public void onFail(int code, String msg) {
                                   if (view != null) view.onFailure(code, msg);
                               }
                           }
                );
    }


    @Override
    public void addMapMark(Context context, MapMarkContainer container, MapLayout map, String type) {
        addMapMark(context, map, container, type);
    }


    private ArrayList<HouseDbEntity> getRandom(ArrayList<HouseDbEntity> list, ArrayList<NearUserEntity.Point> posList) {
        ArrayList<HouseDbEntity> res = new ArrayList<>();
        int size = list.size();
        if (list.size() > posList.size()) {
            Collections.shuffle(list);
            size = posList.size();
        }
        for (int i = 0; i < size; i++) {
            HouseDbEntity entity = list.get(i);
            NearUserEntity.Point point = posList.get(i);
            entity.setPointX(point.getX());
            entity.setPointY(point.getY());
            res.add(entity);
        }
        return res;
    }

    private void addMapMark(Context context, final MapLayout map, MapMarkContainer container, String type) {
        ArrayList<HouseDbEntity> mapPics = (ArrayList<HouseDbEntity>) GreenDaoManager.getInstance().getSession().getHouseDbEntityDao()
                .queryBuilder()
                .where(HouseDbEntityDao.Properties.House.eq(type))
                .list();
        Layer layer;//0 全天可点击事件
        if (mapPics != null && mapPics.size() > 0) {
            if ("nearUser".equals(type)) {
                String posStr = PreferenceUtils.getNearPosition(context);
                Gson gson = new Gson();
                ArrayList<NearUserEntity.Point> posList = gson.fromJson(posStr, new TypeToken<ArrayList<NearUserEntity.Point>>() {
                }.getType());
                if (posList != null) {
                    mapPics = getRandom(mapPics, posList);
                }
            } else if ("topUser".equals(type)) {
                String posStr = PreferenceUtils.getTopUserPosition(context);
                Gson gson = new Gson();
                ArrayList<NearUserEntity.Point> posList = gson.fromJson(posStr, new TypeToken<ArrayList<NearUserEntity.Point>>() {
                }.getType());
                if (posList != null) {
                    mapPics = getRandom(mapPics, posList);
                }
            }
            Collections.sort(mapPics, new Comparator<HouseDbEntity>() {
                @Override
                public int compare(HouseDbEntity mapDbEntity, HouseDbEntity t1) {
                    int i = mapDbEntity.getLayer() - t1.getLayer();
                    return i;
                }
            });
            for (int i = 0; i < mapPics.size(); i++) {
                HouseDbEntity entity = mapPics.get(i);
                String time = "-1";
                if (StringUtils.isasa()) {
                    time = "1";
                }
                if (StringUtils.issyougo()) {
                    time = "2";
                }
                if (StringUtils.isgogo()) {
                    time = "3";
                }
                if (StringUtils.istasogare()) {
                    time = "4";
                }
                if (StringUtils.isyoru2()) {
                    time = "5";
                }
                if (StringUtils.ismayonaka()) {
                    time = "6";
                }
                if (entity.getShows() != null && entity.getShows().contains(time)) {
                    if (entity.getDownloadState() == 2) {
                        if (FileUtil.isExists(StorageUtils.getHouseRootPath() + entity.getFileName())) {
                            MapMarkEntity entity1 = new MapMarkEntity(entity.getName(), entity.getPointX(), entity.getPointY(), entity.getSchema(), entity.getFileName(), entity.getImage_w(), entity.getImage_h(), entity.getText(), entity.getLayer(), entity.getType());
                            container.addMark(entity1);
                            if (mapPics.size() - 1 == i) {
                                Drawable drawable;
                                if (!TextUtils.isEmpty(entity1.getPath())) {
                                    drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity1.getPath());
                                } else {
                                    if (entity1.getBg() == 0) return;
                                    drawable = ContextCompat.getDrawable(context, entity1.getBg());
                                }
                                if (drawable != null) {
                                    map.setImageDrawable(drawable);
                                } else {
                                    FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity1.getPath());
                                }
//                                map.setMapResource(R.drawable.big_house);
                                return;
                            }
                            HouseDbEntity houseDbEntity = mapPics.get(mapPics.size() - 1);
                            addMarkToMap(context, entity1, map, entity, houseDbEntity.getImage_w(), houseDbEntity.getImage_h());
                        }
                    }
//                    } else if (entity.getType().equals("3")) {
//                        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
//                        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
//                        double v = 3598.0 * heightPixels / 1920.0;
//                        double x = entity.getPointX() * v / 3598.0;
//                        double y = entity.getPointY() * heightPixels / 1920.0;
//                        double wight = entity.getImage_w() * v / 3598.0;
//                        double height = entity.getImage_h() * heightPixels / 1920.0;
//                        double v1 = x - ((v - widthPixels) / 2);
//                        map.addMapMarkView(R.drawable.bg_cardbg_myrole_like, (float) v1, (float) y, wight, height, entity.getSchema(), entity.getText(), entity.getType(), null, entity.getId());
//                    }
                }
                // MapToolTipUtils.getInstance().updateList(container.getContainer());
            }
        }
    }

    private void addMarkToMap(Context context, MapMarkEntity entity, MapLayout layer, HouseDbEntity dbEntity, double oldW, double oldH) {
        Drawable drawable;
        if (!TextUtils.isEmpty(entity.getPath())) {
            drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getPath());
        } else {
            if (entity.getBg() == 0) return;
            drawable = ContextCompat.getDrawable(context, entity.getBg());
        }
        if (drawable != null) {
            int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
            int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            double v = 3600.0 * heightPixels / 1920.0;
            double x = entity.getX() * v / 3600.0;
            double y = entity.getY() * heightPixels / 1920.0;
            double wight = entity.getW() * v / 3600.0;
            double height = entity.getH() * heightPixels / 1920.0;
            double v1 = x - ((v - widthPixels) / 2);
            layer.addMapMarkView(drawable, (float) v1, (float) y, wight, height, entity.getSchema(), entity.getContent(), entity.getType(), null, dbEntity);
        } else {
            FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getPath());
        }
    }

}
