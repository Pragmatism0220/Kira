package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.DownloadEntity;
import com.moemoe.lalala.model.entity.FolderType;
import com.moemoe.lalala.model.entity.Image;
import com.moemoe.lalala.utils.NoDoubleClickListener;
import com.moemoe.lalala.utils.TasksManager;
import com.moemoe.lalala.utils.ViewUtils;
import com.moemoe.lalala.view.adapter.DownloadListV2Adapter;
import com.moemoe.lalala.view.widget.adapter.BaseRecyclerViewAdapter;
import com.moemoe.lalala.view.widget.recycler.PullAndLoadView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
/**
 * 下载管理列表
 * Created by yi on 2017/10/11.
 */

public class DownLoadListActivity extends BaseAppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTitle;
    @BindView(R.id.rv_list)
    PullAndLoadView mListDocs;

    private DownloadListV2Adapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_bar_list;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        mListDocs.getSwipeRefreshLayout().setEnabled(false);
        mListDocs.setPadding((int)getResources().getDimension(R.dimen.x24),0,(int)getResources().getDimension(R.dimen.x24),0);
        mAdapter = new DownloadListV2Adapter();
        mListDocs.getRecyclerView().setAdapter(mAdapter);
        mListDocs.setLayoutManager(new LinearLayoutManager(this));
        mListDocs.setLoadMoreEnabled(false);
        loadData();
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setImageResource(R.drawable.btn_back_black_normal);
        mIvBack.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                finish();
            }
        });
        mTitle.setText("下载管理");
    }

    @Override
    protected void initListeners() {
        mAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DownloadEntity entity = mAdapter.getItem(position);
                String type = entity.getType();
                if("image".equals(type)){
                    ArrayList<Image> temp = new ArrayList<>();
                    Image image = new Image();
                    String str = entity.getUrl();
                    image.setPath(str);
                    temp.add(image);
                    Intent intent = new Intent(DownLoadListActivity.this, ImageBigSelectActivity.class);
                    intent.putExtra(ImageBigSelectActivity.EXTRA_KEY_FILEBEAN, temp);
                    intent.putExtra(ImageBigSelectActivity.EXTRAS_KEY_FIRST_PHTOT_INDEX,
                            0);
                    startActivity(intent);
                }else if("txt".equals(type)){
                    NewFileXiaoShuo2Activity.startActivity(DownLoadListActivity.this,entity.getPath());
                }else if("movie".equals(type)){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String type1 = "video/* ";
                    File file = new File(entity.getPath());
                    Uri uri;
                    // 判断版本大于等于7.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {// "com.moemoe.lalala.FileProvider"即是在清单文件中配置的authorities
                        uri = FileProvider.getUriForFile(DownLoadListActivity.this, "com.moemoe.lalala.FileProvider", file);// 给目标应用一个临时授权
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    intent.setDataAndType(uri, type1);
                    startActivity(intent);
                }else if(FolderType.MOVIE.toString().equals(type)){

                    String fileId = entity.getFileId();
                    try{
                        JSONObject o = new JSONObject(entity.getAttr());
                        String folderId = o.getString("folderId");
                        String folderName = o.getString("folderName");
                        String cover = o.getString("cover");
                        KiraVideoActivity.startActivity(DownLoadListActivity.this,folderId,fileId,folderName,cover);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(FolderType.MUSIC.toString().equals(type)){

                    String fileId = entity.getFileId();
                    try{
                        JSONObject o = new JSONObject(entity.getAttr());
                        String folderId = o.getString("folderId");
                        String folderName = o.getString("folderName");
                        String cover = o.getString("cover");
                        KiraMusicActivity.startActivity(DownLoadListActivity.this,folderId,fileId,folderName,cover);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else  {
                    showToast("不支持打开此文件");
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TasksManager.getImpl().release();
    }

    private void loadData() {
            mAdapter.setList(TasksManager.getImpl().getAll());
    }
}
