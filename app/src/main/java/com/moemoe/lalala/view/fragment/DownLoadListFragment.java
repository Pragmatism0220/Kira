package com.moemoe.lalala.view.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.DownloadEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.utils.FileUtil;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.TasksManager;
import com.moemoe.lalala.utils.ToastUtils;
import com.moemoe.lalala.view.activity.ImageBigSelectActivity;
import com.moemoe.lalala.view.activity.KiraDownloadVideoPlayerActivity;
import com.moemoe.lalala.view.activity.KiraMusicActivity;
import com.moemoe.lalala.view.activity.KiraVideoActivity;
import com.moemoe.lalala.view.activity.NewFileXiaoShuo2Activity;
import com.moemoe.lalala.view.adapter.DownloadListV2Adapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Sora on 2018/3/5.
 */

public class DownLoadListFragment extends BaseFragment {

    @BindView(R.id.rv_list)
    PullAndLoadView mListDocs;


    private DownloadListV2Adapter mAdapter;

    public static DownLoadListFragment newInstance() {
        return new DownLoadListFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ac_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {

        mListDocs.getSwipeRefreshLayout().setEnabled(false);
        mListDocs.setPadding((int) getResources().getDimension(R.dimen.x24), 0, (int) getResources().getDimension(R.dimen.x24), 0);
        mAdapter = new DownloadListV2Adapter();
        mListDocs.getRecyclerView().setAdapter(mAdapter);
        mListDocs.setLayoutManager(new LinearLayoutManager(getContext()));
        mListDocs.setLoadMoreEnabled(false);
        loadData();


        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DownloadEntity entity = mAdapter.getItem(position);
                String type = entity.getType();
                if ("image".equals(type)) {
                    ArrayList<Image> temp = new ArrayList<>();
                    Image image = new Image();
                    String str = entity.getUrl();
                    image.setPath(str);
                    temp.add(image);
                    Intent intent = new Intent(getContext(), ImageBigSelectActivity.class);
                    intent.putExtra(ImageBigSelectActivity.EXTRA_KEY_FILEBEAN, temp);
                    intent.putExtra(ImageBigSelectActivity.EXTRAS_KEY_FIRST_PHTOT_INDEX,
                            0);
                    startActivity(intent);
                } else if ("txt".equals(type)) {
                    NewFileXiaoShuo2Activity.startActivity(getContext(), entity.getPath());
                } else if ("movie".equals(type)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String type1 = "video/* ";
                    File file = new File(entity.getPath());
                    Uri uri;
                    // 判断版本大于等于7.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// "com.moemoe.lalala.FileProvider"即是在清单文件中配置的authorities
                        uri = FileProvider.getUriForFile(getContext(), "com.moemoe.lalala.FileProvider", file);// 给目标应用一个临时授权
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    intent.setDataAndType(uri, type1);
                    startActivity(intent);
                } else if (FolderType.MOVIE.toString().equals(type)) {

                    String fileId = entity.getFileId();
                    try {
                        JSONObject o = new JSONObject(entity.getAttr());
                        String folderId = o.getString("folderId");
                        String folderName = o.getString("folderName");
                        String cover = o.getString("cover");
                        final File file = new File(StorageUtils.getVideoRootPath(), folderName+ ".mp4");
//                        final File file = new File(Environment.getExternalStorageDirectory().getPath()+"/"+folderName + ".mp4");
                        if (FileUtil.isExists(file.getAbsolutePath())) {
//                            KiraVideoActivity.startActivity(getContext(), folderId, fileId, folderName, cover, entity.getPath());
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_VIEW);
//                            Uri uri = Uri.fromFile(file);
//                            intent.setDataAndType(uri, "video/mp4");
//                            intent.setClassName("com.cooliris.media", "com.cooliris.media.FileProvider");
//                            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                            startActivity(intent);


                            Intent intent = new Intent(getContext(), KiraDownloadVideoPlayerActivity.class);
                            intent.putExtra("path", entity.getPath());
                            intent.putExtra("folderName", folderName);
                            startActivity(intent);
                        }


//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        String type1 = "video/* ";
//                        File file = new File(entity.getPath()+ ".mp4");
//                        Uri uri;
//                        // 判断版本大于等于7.0
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// "com.moemoe.lalala.FileProvider"即是在清单文件中配置的authorities
//                            uri = FileProvider.getUriForFile(getContext(), "com.moemoe.lalala.FileProvider", file);// 给目标应用一个临时授权
//                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        } else {
//                            uri = Uri.fromFile(file);
//                        }
//                        intent.setDataAndType(uri, type1);
//                        startActivity(intent);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (FolderType.MUSIC.toString().equals(type)) {

                    String fileId = entity.getFileId();
                    try {
                        JSONObject o = new JSONObject(entity.getAttr());
                        String folderId = o.getString("folderId");
                        String folderName = o.getString("folderName");
                        String cover = o.getString("cover");
                        KiraMusicActivity.startActivity(getContext(), folderId, fileId, folderName, cover);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ToastUtils.showLongToast(getContext(), "不支持打开此文件");
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    private void loadData() {
        mAdapter.setList(TasksManager.getImpl().getAll());
    }

    @Override
    public void release() {
        super.release();
    }
}

