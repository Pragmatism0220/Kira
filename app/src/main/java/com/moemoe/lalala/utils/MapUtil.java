package com.moemoe.lalala.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.moemoe.lalala.greendao.gen.DeskmateEntilsDao;
import com.moemoe.lalala.greendao.gen.HouseDbEntityDao;
import com.moemoe.lalala.greendao.gen.JuQingTriggerEntityDao;
import com.moemoe.lalala.greendao.gen.MapDbEntityDao;
import com.moemoe.lalala.model.api.ApiService;
import com.moemoe.lalala.model.entity.DeskmateEntils;
import com.moemoe.lalala.model.entity.HouseDbEntity;
import com.moemoe.lalala.model.entity.JuQingEntity;
import com.moemoe.lalala.model.entity.JuQingTriggerEntity;
import com.moemoe.lalala.model.entity.MapDbEntity;
import com.moemoe.lalala.view.activity.PrActivity;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yi on 2017/10/10.
 */

public class MapUtil {

    public static void checkAndDownload(Context context, boolean del, ArrayList<MapDbEntity> entities, String type, Observer<MapDbEntity> callback) {//1.未下载 2.下载完成 3.下载失败
        for (MapDbEntity entity : entities) {
            //文件是否存在
            if (FileUtil.isExists(StorageUtils.getMapRootPath() + entity.getFileName())) {
                File file = new File(StorageUtils.getMapRootPath() + entity.getFileName());
                String md5 = entity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (!md5.equals(StringUtils.getFileMD5(file))) {
                    FileUtil.deleteFile(StorageUtils.getMapRootPath() + entity.getFileName());
                    entity.setDownloadState(1);
                } else {
                    entity.setDownloadState(2);
                }
            } else {
                entity.setDownloadState(1);
            }
        }
        //检查文件是否更改
        final MapDbEntityDao dao = GreenDaoManager.getInstance().getSession().getMapDbEntityDao();
        if (del) {
            ArrayList<MapDbEntity> mapList = (ArrayList<MapDbEntity>) dao.queryBuilder()
                    .where(MapDbEntityDao.Properties.Type.eq(type))
                    .list();
            if (mapList != null) {
                dao.deleteInTx(mapList);
            }
        }
        dao.insertOrReplaceInTx(entities);

        //下载
        downLoadFiles(context, entities, callback);
    }

    public static void downLoadFiles(Context context, ArrayList<MapDbEntity> tasks, Observer<MapDbEntity> callback) {//1.未下载 2.下载完成 3.下载失败

        Observable.fromIterable(tasks)
                .filter(new Predicate<MapDbEntity>() {
                    @Override
                    public boolean test(@NonNull MapDbEntity mapDbEntity) throws Exception {
                        if (mapDbEntity.getDownloadState() != 2) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<MapDbEntity, ObservableSource<MapDbEntity>>() {
                    @Override
                    public ObservableSource<MapDbEntity> apply(@NonNull final MapDbEntity mapDbEntity) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<MapDbEntity>() {
                            @Override
                            public void subscribe(@NonNull final ObservableEmitter<MapDbEntity> res) throws Exception {
                                FileDownloader.getImpl().create(ApiService.URL_QINIU + mapDbEntity.getImage_path())
                                        .setPath(StorageUtils.getMapRootPath() + mapDbEntity.getFileName())
                                        .setCallbackProgressTimes(1)
                                        .setListener(new FileDownloadListener() {
                                            @Override
                                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void completed(BaseDownloadTask task) {
                                                mapDbEntity.setDownloadState(2);
                                                res.onNext(mapDbEntity);
                                                res.onComplete();
                                            }

                                            @Override
                                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void error(BaseDownloadTask task, Throwable e) {
                                                mapDbEntity.setDownloadState(3);
                                                res.onNext(mapDbEntity);
                                                res.onComplete();
                                            }

                                            @Override
                                            protected void warn(BaseDownloadTask task) {

                                            }
                                        }).start();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }


    public static void checkAndDownloadHouse(Context context, boolean del, ArrayList<HouseDbEntity> entities, String type, Observer<HouseDbEntity> callback) {//1.未下载 2.下载完成 3.下载失败
        for (HouseDbEntity entity : entities) {
            //文件是否存在
            if (FileUtil.isExists(StorageUtils.getHouseRootPath() + entity.getFileName())) {
                File file = new File(StorageUtils.getHouseRootPath() + entity.getFileName());
                String md5 = entity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (!md5.equals(StringUtils.getFileMD5(file))) {
                    FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
                    entity.setDownloadState(1);
                } else {
                    entity.setDownloadState(2);
                }
            } else {
                if (entity.getType().equals("3")) {
                    entity.setDownloadState(2);
                } else {
                    entity.setDownloadState(1);
                }
            }
        }
        //检查文件是否更改
        final HouseDbEntityDao dao = GreenDaoManager.getInstance().getSession().getHouseDbEntityDao();
        if (del) {
            ArrayList<HouseDbEntity> mapList = (ArrayList<HouseDbEntity>) dao.queryBuilder()
                    .where(HouseDbEntityDao.Properties.House.eq(type))
                    .list();
            if (mapList != null) {
                dao.deleteInTx(mapList);
            }
        }
        dao.insertOrReplaceInTx(entities);

        //下载
        downLoadFilesHouse(context, entities, callback);
    }

    public static void downLoadFilesHouse(Context context, ArrayList<HouseDbEntity> tasks, Observer<HouseDbEntity> callback) {//1.未下载 2.下载完成 3.下载失败

        Observable.fromIterable(tasks)
                .filter(new Predicate<HouseDbEntity>() {
                    @Override
                    public boolean test(@NonNull HouseDbEntity mapDbEntity) throws Exception {
                        if (mapDbEntity.getDownloadState() != 2) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<HouseDbEntity, ObservableSource<HouseDbEntity>>() {
                    @Override
                    public ObservableSource<HouseDbEntity> apply(@NonNull final HouseDbEntity mapDbEntity) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<HouseDbEntity>() {
                            @Override
                            public void subscribe(@NonNull final ObservableEmitter<HouseDbEntity> res) throws Exception {
                                FileDownloader.getImpl().create(ApiService.URL_QINIU + mapDbEntity.getImage_path())
                                        .setPath(StorageUtils.getHouseRootPath() + mapDbEntity.getFileName())
                                        .setCallbackProgressTimes(1)
                                        .setListener(new FileDownloadListener() {
                                            @Override
                                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void completed(BaseDownloadTask task) {
                                                mapDbEntity.setDownloadState(2);
                                                res.onNext(mapDbEntity);
                                                res.onComplete();
                                            }

                                            @Override
                                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void error(BaseDownloadTask task, Throwable e) {
                                                mapDbEntity.setDownloadState(3);
                                                res.onNext(mapDbEntity);
                                                res.onComplete();
                                            }

                                            @Override
                                            protected void warn(BaseDownloadTask task) {

                                            }
                                        }).start();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

    public static void checkAndDownloadDeskmate(Context context, boolean del, ArrayList<DeskmateEntils> entities, String type, Observer<DeskmateEntils> callback) {//1.未下载 2.下载完成 3.下载失败
        for (DeskmateEntils entity : entities) {
            //文件是否存在
            if (FileUtil.isExists(StorageUtils.getHouseRootPath() + entity.getFileName())) {
                File file = new File(StorageUtils.getHouseRootPath() + entity.getFileName());
                String md5 = entity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (!md5.equals(StringUtils.getFileMD5(file))) {
                    FileUtil.deleteFile(StorageUtils.getHouseRootPath() + entity.getFileName());
                    entity.setDownloadState(1);
                } else {
                    entity.setDownloadState(2);
                }
            } else {
                entity.setDownloadState(1);
            }
        }
        //检查文件是否更改
        DeskmateEntilsDao dao = GreenDaoManager.getInstance().getSession().getDeskmateEntilsDao();
        if (del) {
            ArrayList<DeskmateEntils> mapList = (ArrayList<DeskmateEntils>) dao.queryBuilder()
                    .where(DeskmateEntilsDao.Properties.Type.eq(type))
                    .list();
            if (mapList != null) {
                dao.deleteInTx(mapList);
            }
        }
        dao.insertOrReplaceInTx(entities);

        //下载
        downLoadFilesDeskmate(context, entities, callback);
    }

    public static void checkAndDownloadTrigger(Context context, boolean del, ArrayList<JuQingTriggerEntity> entities, String type, Observer<JuQingTriggerEntity> callback) {
        for (JuQingTriggerEntity entity : entities) {
            //文件是否存在
            if (FileUtil.isExists(StorageUtils.getMapRootPath() + entity.getFileName())) {
                File file = new File(StorageUtils.getMapRootPath() + entity.getFileName());
                String md5 = entity.getMd5();
                if (md5.length() < 32) {
                    int n = 32 - md5.length();
                    for (int i = 0; i < n; i++) {
                        md5 = "0" + md5;
                    }
                }
                if (!md5.equals(StringUtils.getFileMD5(file))) {
                    FileUtil.deleteFile(StorageUtils.getMapRootPath() + entity.getFileName());
                    entity.setDownloadState(1);
                } else {
                    entity.setDownloadState(2);
                }

            } else {
                entity.setDownloadState(1);
            }
        }
        //检查文件是否被更改
        JuQingTriggerEntityDao dao = GreenDaoManager.getInstance().getSession().getJuQingTriggerEntityDao();
        if (del) {
            ArrayList<JuQingTriggerEntity> list = (ArrayList<JuQingTriggerEntity>) dao.queryBuilder()
                    .where(JuQingTriggerEntityDao.Properties.Type.eq(type))
                    .list();
            if (list != null) {
                dao.deleteInTx(list);
            }
        }
        dao.insertOrReplaceInTx(entities);
        //下载
        downLoadTrigger(context, entities, callback);
    }


    public static void downLoadTrigger(Context context, ArrayList<JuQingTriggerEntity> tasks, Observer<JuQingTriggerEntity> callBack) {//1.未下载 2.下载完成 3.下载失败
        Observable.fromIterable(tasks)
                .filter(new Predicate<JuQingTriggerEntity>() {
                    @Override
                    public boolean test(JuQingTriggerEntity entity) throws Exception {
                        if (entity.getDownloadState() != 2) {
                            return true;
                        }else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<JuQingTriggerEntity, ObservableSource<JuQingTriggerEntity>>() {
                    @Override
                    public ObservableSource<JuQingTriggerEntity> apply(final JuQingTriggerEntity entity) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<JuQingTriggerEntity>() {
                            @Override
                            public void subscribe(final ObservableEmitter<JuQingTriggerEntity> res) throws Exception {
                                FileDownloader.getImpl().create(ApiService.URL_QINIU + entity.getIconPath())
                                        .setPath(StorageUtils.getMapRootPath()+entity.getFileName())
                                        .setCallbackProgressTimes(1)
                                        .setListener(new FileDownloadListener() {
                                            @Override
                                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void completed(BaseDownloadTask task) {
                                                entity.setDownloadState(2);
                                                res.onNext(entity);
                                                res.onComplete();

                                            }

                                            @Override
                                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void error(BaseDownloadTask task, Throwable e) {
                                                entity.setDownloadState(3);
                                                res.onNext(entity);
                                                res.onComplete();
                                            }

                                            @Override
                                            protected void warn(BaseDownloadTask task) {

                                            }
                                        }).start();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callBack);
    }


    public static void downLoadFilesDeskmate(Context context, ArrayList<DeskmateEntils> tasks, Observer<DeskmateEntils> callback) {//1.未下载 2.下载完成 3.下载失败

        Observable.fromIterable(tasks)
                .filter(new Predicate<DeskmateEntils>() {
                    @Override
                    public boolean test(@NonNull DeskmateEntils deskmateUserEntils) throws Exception {
                        if (deskmateUserEntils.getDownloadState() != 2) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<DeskmateEntils, ObservableSource<DeskmateEntils>>() {
                    @Override
                    public ObservableSource<DeskmateEntils> apply(@NonNull final DeskmateEntils deskmateUserEntils) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<DeskmateEntils>() {
                            @Override
                            public void subscribe(@NonNull final ObservableEmitter<DeskmateEntils> res) throws Exception {
                                FileDownloader.getImpl().create(ApiService.URL_QINIU + deskmateUserEntils.getPath())
                                        .setPath(StorageUtils.getHouseRootPath() + deskmateUserEntils.getFileName())
                                        .setCallbackProgressTimes(1)
                                        .setListener(new FileDownloadListener() {
                                            @Override
                                            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void completed(BaseDownloadTask task) {
                                                deskmateUserEntils.setDownloadState(2);
                                                res.onNext(deskmateUserEntils);
                                                res.onComplete();
                                            }

                                            @Override
                                            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                                            }

                                            @Override
                                            protected void error(BaseDownloadTask task, Throwable e) {
                                                deskmateUserEntils.setDownloadState(3);
                                                res.onNext(deskmateUserEntils);
                                                res.onComplete();
                                            }

                                            @Override
                                            protected void warn(BaseDownloadTask task) {

                                            }
                                        }).start();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

}
