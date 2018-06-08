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
    public void loadHouseObjects() {
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
    public void addMapMark(Context context, MapMarkContainer container, MapLayout map, String type) {
        addMapMark(context, map, container, type);
    }

//    @Override
//    public void addMapMark(Context context, MapMarkContainer container, MapWidget map, String type) {
//        addMapMark(context, map, container, type);
//        // view.onMapMarkLoaded(container);
//    }
//
//    @Override
//    public void addEventMark(String id, String icon, MapMarkContainer container, Context context, MapWidget map, String storyId) {
//        int iconId = 0;
//        int x = 0;
//        int y = 0;
//        if ("daily_len".equals(icon)) {
//            iconId = R.drawable.btn_event_daily_len;
//        } else if ("daily_mei".equals(icon)) {
//            iconId = R.drawable.btn_event_daily_mei;
//        } else if ("daily_sari".equals(icon)) {
//            iconId = R.drawable.btn_event_daily_sari;
//        } else if ("branch_len".equals(icon)) {
//            iconId = R.drawable.btn_event_branch_len;
//        } else if ("branch_mei".equals(icon)) {
//            iconId = R.drawable.btn_event_branch_mei;
//        } else if ("branch_sari".equals(icon)) {
//            iconId = R.drawable.btn_event_branch_sari;
//        } else if ("main_len".equals(icon)) {
//            iconId = R.drawable.btn_event_main_len;
//        } else if ("main_mei".equals(icon)) {
//            iconId = R.drawable.btn_event_main_mei;
//        } else if ("main_sari".equals(icon)) {
//            iconId = R.drawable.btn_event_main_sari;
//        } else if ("summerfestival".equals(icon)) {
//            iconId = R.drawable.btn_map_event_summerfestival;
//        } else if ("daily_current".equals(icon)) {
//            iconId = R.drawable.btn_event_daily_current;
//        } else if ("branch_current".equals(icon)) {
//            iconId = R.drawable.btn_event_branch_current;
//        } else if ("main_current".equals(icon)) {
//            iconId = R.drawable.btn_event_main_current;
//        }
//
//        if ("activityroom".equals(id)) {
//            x = 1295;
//            y = 866;
//        } else if ("classroom".equals(id)) {
//            x = 1696;
//            y = 412;
//        } else if ("rooftop".equals(id)) {
//            x = 1493;
//            y = 272;
//        } else if ("mainroad".equals(id)) {
//            x = 2988;
//            y = 1311;
//        } else if ("corridor".equals(id)) {
//            x = 1861;
//            y = 715;
//        } else if ("tree".equals(id)) {
//            x = 441;
//            y = 1736;
//        } else if ("canteen".equals(id)) {
//            x = 1108;
//            y = 1455;
//        } else if ("principal".equals(id)) {
//            x = 1877;
//            y = 342;
//        } else if ("coffee".equals(id)) {
//            x = 873;
//            y = 1147;
//        } else if ("playground".equals(id)) {
//            x = 1166;
//            y = 188;
//        } else if ("warehouse".equals(id)) {
//            x = 2321;
//            y = 342;
//        } else if ("summerfestival".equals(id)) {
//            x = 3405;
//            y = 1992;
//        } else if ("library".equals(id)) {
//            x = 3288;
//            y = 1024;
//        }
//        Layer layer = map.getLayerById(1);
//
//        // if(layer == null){
//        // Layer layer = map.createLayer(1);//1 地图剧情
//        //  }
////        MapObject object = layer.getMapObject("地图剧情" + id);
////        if (object == null) {
////            MapMarkEntity entity1 = new MapMarkEntity("地图剧情" + id, x, y, "neta://com.moemoe.lalala/map_event_1.0?id=" + storyId, iconId, 140, 140);
////            container.removeMarkById("地图剧情" + id);
////            container.addMark(entity1);
////            addMarkToMap(context, entity1, layer);
////            // view.onMapEventLoaded(container);
////        }
//        // MapToolTipUtils.getInstance().updateList(container.getContainer());
//    }

//    private void addMapMark(Context context, final MapWidget map, MapMarkContainer container, String type) {
//        ArrayList<MapDbEntity> mapPics = (ArrayList<MapDbEntity>) GreenDaoManager.getInstance().getSession().getMapDbEntityDao()
//                .queryBuilder()
//                .where(MapDbEntityDao.Properties.House.eq(type))
//                .list();
//        Layer layer;//0 全天可点击事件
//        if (mapPics != null && mapPics.size() > 0) {
//            if ("nearUser".equals(type)) {
//                String posStr = PreferenceUtils.getNearPosition(context);
//                Gson gson = new Gson();
//                ArrayList<NearUserEntity.Point> posList = gson.fromJson(posStr, new TypeToken<ArrayList<NearUserEntity.Point>>() {
//                }.getType());
//                if (posList != null) {
//                    mapPics = getRandom(mapPics, posList);
//                }
//            } else if ("topUser".equals(type)) {
//                String posStr = PreferenceUtils.getTopUserPosition(context);
//                Gson gson = new Gson();
//                ArrayList<NearUserEntity.Point> posList = gson.fromJson(posStr, new TypeToken<ArrayList<NearUserEntity.Point>>() {
//                }.getType());
//                if (posList != null) {
//                    mapPics = getRandom(mapPics, posList);
//                }
//            }
//            Collections.sort(mapPics, new Comparator<MapDbEntity>() {
//                @Override
//                public int compare(MapDbEntity mapDbEntity, MapDbEntity t1) {
//                    int i = mapDbEntity.getLayer() - t1.getLayer();
//                    return i;
//                }
//            });
//            for (MapDbEntity entity : mapPics) {
////                Layer tmp = map.getLayerById(entity.getLayer());
////                if (tmp != null) map.removeLayer(entity.getLayer());
////                layer = map.createLayer(entity.getLayer());
//                Layer tmp = map.getLayerById(entity.getLayer());
//                if (tmp != null) map.removeLayer(entity.getLayer());
//                layer = map.createLayer(entity.getLayer());
//                String time = "-1";
//                if (StringUtils.isasa()) {
//                    time = "1";
//                }
//                if (StringUtils.issyougo()) {
//                    time = "2";
//                }
//                if (StringUtils.isgogo()) {
//                    time = "3";
//                }
//                if (StringUtils.istasogare()) {
//                    time = "4";
//                }
//                if (StringUtils.isyoru2()) {
//                    time = "5";
//                }
//                if (StringUtils.ismayonaka()) {
//                    time = "6";
//                }
//                if (entity.getShows() != null && entity.getShows().contains(time)) {
//                    if (entity.getDownloadState() == 2) {
//                        if (FileUtil.isExists(StorageUtils.getHouseRootPath() + entity.getFileName())) {
//                            MapMarkEntity entity1 = new MapMarkEntity(entity.getName(), entity.getPointX(), entity.getPointY(), entity.getSchema(), entity.getFileName(), entity.getImage_w(), entity.getImage_h(), entity.getText(), entity.getLayer(), entity.getType());
//                            container.addMark(entity1);
//                            addMarkToMap(context, entity1, layer);
//                        }
//                    }
//                }
//                // MapToolTipUtils.getInstance().updateList(container.getContainer());
//            }
//        }
//    }

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

//    private void addMarkToMap(Context context, MapMarkEntity entity, Layer layer) {
//        Drawable drawable;
//        if (!TextUtils.isEmpty(entity.getPath())) {
//            drawable = Drawable.createFromPath(StorageUtils.getHouseRootPath() + entity.getPath());
//        } else {
//            if (entity.getBg() == 0) return;
//            drawable = ContextCompat.getDrawable(context, entity.getBg());
//        }
//        if (drawable != null) {
//            MapObject object = new MapObject(entity.getId()
//                    , drawable
//                    , entity.getX()
//                    , entity.getY()
//                    , 0
//                    , 0
//                    , true
//                    , true
//                    , entity.getW()
//                    , entity.getH());
//            layer.addMapObject(object);
//        } else {
//            FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getPath());
//        }
//    }

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
                            addMarkToMap(context, entity1, map, entity);
                        }
                    } else if (entity.getType().equals("3")) {
                        int heightPixels = context.getResources().getDisplayMetrics().heightPixels;
                        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
                        double v = 3598.0 * heightPixels / 1920.0;
                        double x = entity.getPointX() * v / 3598.0;
                        double y = entity.getPointY() * heightPixels / 1920.0;
                        double wight = entity.getImage_w() * v / 3598.0;
                        double height = entity.getImage_h() * heightPixels / 1920.0;
                        double v1 = x - ((v - widthPixels) / 2);
                        map.addMapMarkView(R.drawable.bg_cardbg_myrole_like, (float) v1, (float) y, wight, height, entity.getSchema(), entity.getText(), entity.getType(), null, entity.getId());
                    }
                }
                // MapToolTipUtils.getInstance().updateList(container.getContainer());
            }
        }
    }

    private void addMarkToMap(Context context, MapMarkEntity entity, MapLayout layer, HouseDbEntity dbEntity) {
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
            double v = 3598.0 * heightPixels / 1920.0;
            double x = entity.getX() * v / 3598.0;
            double y = entity.getY() * heightPixels / 1920.0;
            double wight = entity.getW() * v / 3598.0;
            double height = entity.getH() * heightPixels / 1920.0;
            double v1 = x - ((v - widthPixels) / 2);
            layer.addMapMarkView(drawable, (float) v1, (float) y, wight, height, entity.getSchema(), entity.getContent(), entity.getType(), null, dbEntity);
        } else {
            FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getPath());
        }
    }

}
