package com.moemoe.lalala.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.moemoe.lalala.R;
import com.moemoe.lalala.model.entity.ZipInfo;
import com.moemoe.lalala.utils.StorageUtils;
import com.moemoe.lalala.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by yi on 2016/11/30.
 */

public class SelectZipActivity extends BaseAppCompatActivity {

    public static final String EXTRA_SELECT_ZIP = "select_zip";
    private final int RES_OK = 2333;

    @BindView(R.id.list)
    ListView mList;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTitle;
    private BookListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_one_list_samller;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        ViewUtils.setStatusBarLight(getWindow(), $(R.id.top_view));
        mTitle.setText("选择压缩包");
        mAdapter = new BookListAdapter();
        mList.setAdapter(mAdapter);
    }

    @Override
    protected void initToolbar(Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setImageResource(R.drawable.btn_back_black_normal);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.putExtra(EXTRA_SELECT_ZIP,mAdapter.getItem(position));
                setResult(RESULT_OK,i);
                finish();
            }
        });
        queryFiles();
    }

    private void queryFiles(){
        String[] projection = new String[]{MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE
        };

        // cache
        String bookpath = StorageUtils.getNovRootPath();

        // 查询后缀名为txt与pdf，并且不位于项目缓存中的文档
        Cursor cursor = getContentResolver().query(
                Uri.parse("content://media/external/file"),
                projection,
                MediaStore.Files.FileColumns.DATA + " not like ? and ("
                        + MediaStore.Files.FileColumns.DATA + " like ?)",
                new String[]{"%" + bookpath + "%",
                        "%" + ".zip"}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idindex = cursor.getColumnIndex(MediaStore.Files.FileColumns._ID);
            int dataindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            int sizeindex = cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
            List<ZipInfo> list = new ArrayList<>();


            do {
                String path = cursor.getString(dataindex);

                int dot = path.lastIndexOf("/");
                String name = path.substring(dot + 1);
                if (name.lastIndexOf(".") > 0)
                    name = name.substring(0, name.lastIndexOf("."));

                ZipInfo book = new ZipInfo();
                book.setId(name);
                book.setPath(path);
                book.setTitle(name);
                book.setFromSD(true);
                list.add(book);
            } while (cursor.moveToNext());

            cursor.close();

            mAdapter.setData(list);
        } else {
            mAdapter.clear();
        }
    }

    @Override
    protected void initData() {

    }

    private class BookListAdapter extends BaseAdapter {

        private List<ZipInfo> list = new ArrayList<>();
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public ZipInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                convertView = LayoutInflater.from(SelectZipActivity.this).inflate(R.layout.item_select_music,null);
                viewHolder = new ViewHolder();
                viewHolder.cover = (ImageView) convertView.findViewById(R.id.iv_select_music);
                viewHolder.title = (TextView) convertView.findViewById(R.id.tv_select_music);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ZipInfo entity = getItem(position);
            viewHolder.cover.setImageResource(R.drawable.ic_bag_zip);
            viewHolder.title.setText(entity.getTitle());
            return convertView;
        }

        public void setData(List<ZipInfo> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        public void clear() {
            list.clear();
            notifyDataSetChanged();
        }


        class ViewHolder{
            ImageView cover;
            TextView title;
        }
    }
}
