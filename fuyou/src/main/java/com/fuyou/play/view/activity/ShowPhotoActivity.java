package com.fuyou.play.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fuyou.play.LApplication;
import com.fuyou.play.R;
import com.fuyou.play.util.Const;
import com.fuyou.play.util.ExceptionUtils;
import com.fuyou.play.util.IntegerVersionSignature;
import com.fuyou.play.util.WeakReferenceHandler;
import com.fuyou.play.view.BaseActivity;
import com.fuyou.play.widget.HackyViewPager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by Administrator on 2017-10-29.
 * 展示图片页面，一张或者多张均可
 *
 * @author wwei
 */
public class ShowPhotoActivity extends BaseActivity implements Handler.Callback{

    private ViewPager viewPager;
    private List<String> urls = new ArrayList<>();
    private ImageView mDownloadIv;
    private int mCurrentPosition;
    private ImageSaveListener imageSaveListener;
    private WeakReferenceHandler mHandler = new WeakReferenceHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setStatusBarTranslucent();
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_show_photo);
            String url = getIntent().getStringExtra("show_photo");
            if (url != null) {
                urls.add(url);
            } else {
                urls = (List<String>) getIntent().getSerializableExtra("list_show_photo");
            }
            mCurrentPosition = getIntent().getIntExtra("position", 0);
            viewPager = (HackyViewPager) findViewById(R.id.vp);
            viewPager.setAdapter(new MyViewPager(this, urls));
            viewPager.setCurrentItem(mCurrentPosition);

            mDownloadIv = (ImageView) findViewById(R.id.download_iv);
            imageSaveListener = new ImageSaveListener() {
                @Override
                public void ImageSave(Uri imageUri) {
                    closeLoading();
                    if (imageUri != null){
                        showToast("Download Success");
                    } else {
                        showToast("Download Error");
                    }
                }
            };

            mDownloadIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoading();
                    new DownloadTask(imageSaveListener).execute(urls.get(mCurrentPosition));
                }
            });

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mCurrentPosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } catch (Exception e) {
            ExceptionUtils.ExceptionSend(e, "ShowPhotoActivity onCreate");
        }
    }

    class MyViewPager extends PagerAdapter {
        List<String> urls;

        MyViewPager(Context context, List<String> urls) {
            this.urls = urls;
        }

        PhotoView photoView;

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            photoView = new PhotoView(ShowPhotoActivity.this);
            Glide.with(ShowPhotoActivity.this).load(urls.get(position)).signature(new IntegerVersionSignature(1)).into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ShowPhotoActivity.this.finish();
                    return false;
                }
            });
            photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    ShowPhotoActivity.this.finish();
                }
            });
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowPhotoActivity.this.finish();
                }
            });
            return photoView;
        }
    }

    private class DownloadTask extends AsyncTask<String, Void, Void> {
        private String mFileName;
        private ImageSaveListener mListener;

        public DownloadTask(ImageSaveListener listener) {
            mListener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mFileName = System.currentTimeMillis() + ".jpg";
        }

        @Override
        protected Void doInBackground(final String... saveUrl) {
            try {
                Bitmap saveBitmap = downloadImage(saveUrl[0]);
                if (saveBitmap == null) {
                    mListener.ImageSave(null);
                } else {
                    saveImage(mFileName, saveBitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private void saveImage(final String fileName, final Bitmap image) {
            File file = new File(Const.FY_DIRECTORY_PICTURE_FOLD_PATH, fileName);
            try {
                file.getParentFile().mkdirs();
                image.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
                MediaScannerConnection.scanFile(LApplication.getInstance(),
                        new String[]{file.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(final String path, final Uri uri) {
                                if (mListener != null) {
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            mListener.ImageSave(uri);
                                        }
                                    });
                                }
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap downloadImage(String url) {
        BufferedInputStream bis = null;
        Bitmap bitmap = null;
        try {
            bis = new BufferedInputStream((new URL(url)).openStream());
            return BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
            // 下载失败

        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {

                }
            }
        }
        return bitmap;
    }

    private void startLoading() {
        findViewById(R.id.pb_base).setVisibility(View.VISIBLE);
    }

    private void closeLoading() {
        findViewById(R.id.pb_base).setVisibility(View.GONE);
    }

    public interface ImageSaveListener{
        void ImageSave(Uri uri);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
