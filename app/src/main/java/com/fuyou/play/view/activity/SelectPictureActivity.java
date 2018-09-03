package com.fuyou.play.view.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fuyou.play.R;
import com.fuyou.play.util.Const;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.view.BaseActivity;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SelectPictureActivity extends BaseActivity {

    /**
     * 最多选择图片的个数
     */
    private static int MAX_NUM = 9;
    private static final int TAKE_PICTURE = 520;
    private Context context;
    private GridView gridview;
    private PictureAdapter adapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashMap<String, Integer> tmpDir = new HashMap<String, Integer>();
    private ArrayList<ImageFloder> mDirPaths = new ArrayList<ImageFloder>();
    private ContentResolver mContentResolver;
    private TextView btn_ok;
    private Button btn_select;
    private ListView listview;
    private FolderAdapter folderAdapter;
    private ImageFloder imageAll, currentImageFolder;

    /**
     * 已选择的图片
     */
    private ArrayList<String> selectedPicture = new ArrayList<String>();
    private String cameraPath = null;
    private int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        initStatusBar(findViewById(R.id.status_bar));
        setBackViews(R.id.iv_back_base);
        ((TextView) findViewById(R.id.tv_title_base)).setText(getString(R.string.title_select_picture));
        try {
            MAX_NUM = getIntent().getIntExtra(Const.INTENT_MAX_NUM, 9);
            context = this;
            mContentResolver = getContentResolver();
            initView();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "SelectPictureActivity");
        }
    }

    public void select(View v) {
        try {
            if (listview.getVisibility() == View.VISIBLE) {
                hideListAnimation();
            } else {
                listview.setVisibility(View.VISIBLE);
                showListAnimation();
                folderAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    public void showListAnimation() {
        try {
            TranslateAnimation ta = new TranslateAnimation(
                    1, 0f, 1, 0f, 1, 1f, 1, 0f);
            ta.setDuration(200);
            listview.startAnimation(ta);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e);
        }
    }

    public void hideListAnimation() {
        try {
            TranslateAnimation ta = new TranslateAnimation(
                    1, 0f, 1, 0f, 1, 0f, 1, 1f);
            ta.setDuration(200);
            listview.startAnimation(ta);
            ta.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    listview.setVisibility(View.GONE);
                }
            });
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "SelectPictureActivity");
        }
    }

    //点击完成按钮
    public void ok() {
        try {
            Intent data = new Intent();
            data.putExtra(Const.INTENT_SELECTED_PICTURE, selectedPicture);
            setResult(RESULT_OK, data);
            this.finish();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "SelectPictureActivity");
        }
    }

    private void initView() {
        try {
            imageAll = new ImageFloder();
            imageAll.setDir("/所有图片");
            currentImageFolder = imageAll;
            mDirPaths.add(imageAll);
            btn_ok =  findViewById(R.id.tv_right_base);
            btn_ok.setVisibility(View.VISIBLE);
            btn_select =  findViewById(R.id.btn_select);
            btn_ok.setText("完成0/" + MAX_NUM);

            btn_ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ok();
                }
            });

            gridview = (GridView) findViewById(R.id.gridview);
            adapter = new PictureAdapter();
            gridview.setAdapter(adapter);

            gridview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        goCamare();
                    }else {
                        startActivity(new Intent(SelectPictureActivity.this, ShowPhotoActivity.class)
                                .putExtra("list_show_photo", (Serializable) (currentImageFolder.images)).putExtra("position", position-1));
                    }
                }
            });

            listview = (ListView) findViewById(R.id.listview);
            folderAdapter = new FolderAdapter();
            listview.setAdapter(folderAdapter);
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    currentImageFolder = mDirPaths.get(position);
                    Log.d("zyh", position + "-------" + currentImageFolder.getName() + "----"
                            + currentImageFolder.images.size());
                    hideListAnimation();
                    adapter.notifyDataSetChanged();
                    btn_select.setText(currentImageFolder.getName());
                }
            });
            getThumbnail();
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "SelectPictureActivity");
        }
    }

    /**
     * 使用相机拍照
     *
     * @version 1.0
     * @author zyh
     */
    protected void goCamare() {
        try {
            if (selectedPicture.size() + 1 > MAX_NUM) {
                Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri imageUri = getOutputMediaFileUri();
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        }catch (Exception e){
            ExceptionUtils.ExceptionSend(e, "SelectPictureActivity");
        }
    }

    /**
     * 用于拍照时获取输出的Uri
     *
     * @version 1.0
     * @author
     * @return
     */
    protected Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Night");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        cameraPath = mediaFile.getAbsolutePath();
        return Uri.fromFile(mediaFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && cameraPath != null) {
            selectedPicture.add(cameraPath);
            Intent data2 = new Intent();
            data2.putExtra(Const.INTENT_SELECTED_PICTURE, selectedPicture);
            setResult(RESULT_OK, data2);
            this.finish();
        }
    }

    public void back(View v) {
        onBackPressed();
    }

    class PictureAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return currentImageFolder.images.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.grid_item_picture, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.checkBox = (Button) convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                holder.iv.setImageResource(R.mipmap.pickphotos_to_camera_normal);
                holder.checkBox.setVisibility(View.INVISIBLE);
            } else {
                position = position - 1;
                holder.checkBox.setVisibility(View.VISIBLE);
                final String item = currentImageFolder.images.get(position);
//                loader.displayImage("file://" + item, holder.iv, options);

                Glide.with(context).load(item).into(holder.iv);
                boolean isSelected = selectedPicture.contains(item);
                holder.checkBox.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!v.isSelected() && selectedPicture.size() + 1 > MAX_NUM) {
                            Toast.makeText(context, "最多选择" + MAX_NUM + "张", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (selectedPicture.contains(item)) {
                            selectedPicture.remove(item);
                        } else {
                            selectedPicture.add(item);
                        }
                        btn_ok.setEnabled(selectedPicture.size()>0);
                        btn_ok.setText("完成" + selectedPicture.size() + "/" + MAX_NUM);
                        v.setSelected(selectedPicture.contains(item));
                    }
                });
                holder.checkBox.setSelected(isSelected);
            }
            return convertView;
        }
    }

    class ViewHolder {
        ImageView iv;
        Button checkBox;
    }

    class FolderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDirPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FolderViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.list_dir_item, null);
                holder = new FolderViewHolder();
                holder.id_dir_item_image = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
                holder.id_dir_item_name = (TextView) convertView.findViewById(R.id.id_dir_item_name);
                holder.id_dir_item_count = (TextView) convertView.findViewById(R.id.id_dir_item_count);
                holder.choose = (ImageView) convertView.findViewById(R.id.choose);
                convertView.setTag(holder);
            } else {
                holder = (FolderViewHolder) convertView.getTag();
            }
            ImageFloder item = mDirPaths.get(position);
//            loader.displayImage("file://" + item.getFirstImagePath(), holder.id_dir_item_image, options);
            Glide.with(context).load(item.getFirstImagePath()).into(holder.id_dir_item_image);
            holder.id_dir_item_count.setText(item.images.size() + "张");
            holder.id_dir_item_name.setText(item.getName());
            holder.choose.setVisibility(currentImageFolder == item ? View.VISIBLE : View.GONE);
            return convertView;
        }
    }

    class FolderViewHolder {
        ImageView id_dir_item_image;
        ImageView choose;
        TextView id_dir_item_name;
        TextView id_dir_item_count;
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.ImageColumns.DATA }, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        // Log.e("TAG", mCursor.getCount() + "");
        if (mCursor.moveToFirst()) {
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                // 获取图片的路径
                String path = mCursor.getString(_date);
                // Log.e("TAG", path);
                imageAll.images.add(path);
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                ImageFloder imageFloder = null;
                String dirPath = parentFile.getAbsolutePath();
                if (!tmpDir.containsKey(dirPath)) {
                    // 初始化imageFloder
                    imageFloder = new ImageFloder();
                    imageFloder.setDir(dirPath);
                    imageFloder.setFirstImagePath(path);
                    mDirPaths.add(imageFloder);
                    // Log.d("zyh", dirPath + "," + path);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFloder));
                } else {
                    imageFloder = mDirPaths.get(tmpDir.get(dirPath));
                }
                imageFloder.images.add(path);
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        // for (int i = 0; i < mDirPaths.size(); i++) {
        //     ImageFloder f = mDirPaths.get(i);
        //     Log.d("zyh", i + "-----" + f.getName() + "---" + f.images.size());
        // }
        tmpDir = null;
    }

    class ImageFloder {
        /**
         * 图片的文件夹路径
         */
        private String dir;

        /**
         * 第一张图片的路径
         */
        private String firstImagePath;
        /**
         * 文件夹的名称
         */
        private String name;

        public List<String> images = new ArrayList<String>();

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
            int lastIndexOf = this.dir.lastIndexOf("/");
            this.name = this.dir.substring(lastIndexOf);
        }

        public String getFirstImagePath() {
            return firstImagePath;
        }

        public void setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
        }

        public String getName() {
            return name;
        }
    }
}
