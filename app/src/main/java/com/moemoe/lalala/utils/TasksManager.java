package com.moemoe.lalala.utils;

import android.util.SparseArray;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.moemoe.lalala.model.entity.DownloadEntity;
import com.moemoe.lalala.view.adapter.DownloadListV2Holder;

import java.util.ArrayList;

/**
 * Created by yi on 2017/10/27.
 */

public class TasksManager {

    private static TasksManager instance;

    public static TasksManager getImpl() {
        if (instance == null) {
            synchronized (TasksManager.class) {
                if (instance == null) {
                    instance = new TasksManager();
                }
            }
        }
        return instance;
    }

    public void release() {
        modelList.clear();
        releaseTask();
        instance = null;
    }

    private TasksManagerDBController dbController;
    private ArrayList<DownloadEntity> modelList;

    private TasksManager() {
        dbController = new TasksManagerDBController();
        modelList = dbController.getAllTasks();
    }

    private SparseArray<BaseDownloadTask> taskSparseArray = new SparseArray<>();

    public void addTaskForViewHolder(final BaseDownloadTask task) {
        taskSparseArray.put(task.getId(), task);
    }

    public void removeTaskForViewHolder(final int id) {
        taskSparseArray.remove(id);
    }

    public void updateViewHolder(final int id, final DownloadListV2Holder holder) {
        final BaseDownloadTask task = taskSparseArray.get(id);
        if (task == null) {
            return;
        }
        task.setTag(holder);
    }

    public void updateViewVv2Holder(final int id, final DownloadListV2Holder holder) {
        final BaseDownloadTask task = taskSparseArray.get(id);
        if (task == null) {
            return;
        }
        task.setTag(holder);
    }

    public void releaseTask() {
        taskSparseArray.clear();
    }

    public void removeItem(int position) {
        DownloadEntity entity = modelList.remove(position);
        GreenDaoManager.getInstance().getSession().getDownloadEntityDao().deleteByKey(entity.getId());
    }

    public ArrayList<DownloadEntity> getAll() {
        return modelList;
    }

    public DownloadEntity get(final int position) {
        return modelList.get(position);
    }

    public DownloadEntity getById(final int id) {
        for (DownloadEntity model : modelList) {
            if (model.getId() == id) {
                return model;
            }
        }

        return null;
    }

    public void add(DownloadEntity entity){
        modelList.add(entity);
    }

    /**
     * @param status Download Status
     * @return has already downloaded
     * @see FileDownloadStatus
     */
    public boolean isDownloaded(final int status) {
        return status == FileDownloadStatus.completed;
    }

    public int getStatus(final int id, String path) {
        return FileDownloader.getImpl().getStatus(id, path);
    }

    public long getTotal(final int id) {
        return FileDownloader.getImpl().getTotal(id);
    }

    public long getSoFar(final int id) {
        return FileDownloader.getImpl().getSoFar(id);
    }

    public int getTaskCounts() {
        return modelList.size();
    }

    private static class TasksManagerDBController {

        private TasksManagerDBController() {

        }

        public ArrayList<DownloadEntity> getAllTasks() {
            ArrayList<DownloadEntity> list = (ArrayList<DownloadEntity>) GreenDaoManager.getInstance().getSession().getDownloadEntityDao().loadAll();
            if (list == null) list = new ArrayList<>();
            return list;
        }

    }
}
