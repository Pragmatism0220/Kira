package com.moemoe.lalala.view.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.DownloadEntity;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.StringUtils;
import com.moemoe.lalala.utils.TasksManager;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;

/**
 * Created by yi on 2017/6/26.
 */

public class DownloadListV2Adapter extends BaseRecyclerViewAdapter<DownloadEntity, DownloadListV2Holder> {


    public DownloadListV2Adapter() {
        super(R.layout.download_manager_item_v_2);
    }

    @Override
    protected void convert(DownloadListV2Holder helper, final DownloadEntity item, int position) {
        helper.createItem(item, position);
    }

    @Override
    public int getItemType(int position) {
        return 0;
    }

    @Override
    protected DownloadListV2Holder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return new DownloadListV2Holder(getItemView(R.layout.download_manager_item_v_2, parent), this);
    }

    FileDownloadListener taskDownloadListener = new FileDownloadSampleListener() {

        private DownloadListV2Holder checkCurrentHolder(final BaseDownloadTask task) {
            final DownloadListV2Holder tag = (DownloadListV2Holder) task.getTag();
            if (tag.id != task.getId()) {
                return null;
            }

            return tag;
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.pending(task, soFarBytes, totalBytes);
            final DownloadListV2Holder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloading(FileDownloadStatus.pending, soFarBytes
                    , totalBytes);
            tag.mStatusText.setText("等待中");
        }

        @Override
        protected void started(BaseDownloadTask task) {
            super.started(task);
            final DownloadListV2Holder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.mStatusText.setText("开始下载");
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
            final DownloadListV2Holder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloading(FileDownloadStatus.connected, soFarBytes
                    , totalBytes);
            tag.mStatusText.setText("连接中");
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.progress(task, soFarBytes, totalBytes);
            final DownloadListV2Holder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloading(FileDownloadStatus.progress, soFarBytes
                    , totalBytes);
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            super.error(task, e);
            final DownloadListV2Holder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateNotDownloaded(FileDownloadStatus.error, task.getLargeFileSoFarBytes()
                    , task.getLargeFileTotalBytes());
            TasksManager.getImpl().removeTaskForViewHolder(task.getId());
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.paused(task, soFarBytes, totalBytes);
            final DownloadListV2Holder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateNotDownloaded(FileDownloadStatus.paused, soFarBytes, totalBytes);
            tag.mStatusText.setText("下载暂停");
            TasksManager.getImpl().removeTaskForViewHolder(task.getId());
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            super.completed(task);
            final DownloadListV2Holder tag = checkCurrentHolder(task);
            if (tag == null) {
                return;
            }

            tag.updateDownloaded();
            TasksManager.getImpl().removeTaskForViewHolder(task.getId());
        }
    };

    NoDoubleClickListener taskActionOnClickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            if (v.getTag() == null) {
                return;
            }
            DownloadListV2Holder holder = (DownloadListV2Holder) v.getTag();
            CharSequence action = ((Button) v).getText();
            if (action.equals("暂停")) {
                // to pause
                FileDownloader.getImpl().pause(holder.id);
            } else if (action.equals("开始")) {
                // to start
                // to start
                final DownloadEntity model = TasksManager.getImpl().get(holder.position);
                BaseDownloadTask.IRunningTask taskI = FileDownloadList.getImpl().get(holder.id);
                BaseDownloadTask task;
                if (taskI == null) {
                    task = FileDownloader.getImpl().create(StringUtils.getUrl(model.getUrl()))
                            .setPath(model.getPath())
                            .setCallbackProgressTimes(1000);
                } else {
                    task = taskI.getOrigin();
                }
                task.setListener(taskDownloadListener);
                TasksManager.getImpl()
                        .addTaskForViewHolder(task);
                TasksManager.getImpl()
                        .updateViewVv2Holder(holder.id, holder);
                task.start();
            } else if (action.equals("完成")) {

            }
        }
    };
}
