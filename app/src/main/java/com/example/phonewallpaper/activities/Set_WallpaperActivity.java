package com.example.phonewallpaper.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.phonewallpaper.R;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;


public class Set_WallpaperActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView wallpaperImg;
    String title ;

    FloatingActionButton mFab, mDownload, mSetWallpaper, mShareWallpaper;

    LinearLayout mDownloadLayout, mSetWallpaperLayout, mShareWallpaperLayout;

    Animation mShowButton, mHideButton, mShowLayout, mHideLayout;

    private ProgressBar progressBar;

    private View decorView;

    private ProgressDialog loadingBar;

    private String imgUrl;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set__wallpaper);

        decorView = getWindow().getDecorView();

        //Navigation..
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBar());
                }
            }
        });

        Intent intent = getIntent();
          title = intent.getStringExtra("title");
          imgUrl = intent.getStringExtra("url");



        wallpaperImg = findViewById(R.id.wallpaper_img);
        progressBar = findViewById(R.id.progressBar);

        loadingBar = new ProgressDialog(this);

        Glide.with(this)
                .load(imgUrl)
                .into(wallpaperImg);




        mFab = findViewById(R.id.plus_btn);
        mDownload = findViewById(R.id.btn_download);
        mSetWallpaper = findViewById(R.id.btn_set);
        mShareWallpaper = findViewById(R.id.btn_share);

        mDownloadLayout = findViewById(R.id.download_layout);
        mSetWallpaperLayout = findViewById(R.id.set_wallpaper_layout);
        mShareWallpaperLayout = findViewById(R.id.share_wallpaper);

        mShowButton = AnimationUtils.loadAnimation(Set_WallpaperActivity.this,R.anim.show_button);
        mHideButton = AnimationUtils.loadAnimation(Set_WallpaperActivity.this,R.anim.hide_button);

        mShowLayout = AnimationUtils.loadAnimation(Set_WallpaperActivity.this,R.anim.show_layout);
        mHideLayout = AnimationUtils.loadAnimation(Set_WallpaperActivity.this,R.anim.hide_layout);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDownload.setEnabled(true);
                mSetWallpaper.setEnabled(true);
                mShareWallpaper.setEnabled(true);

                if (mDownloadLayout.getVisibility() == View.GONE && mSetWallpaperLayout.getVisibility() == View.GONE
                && mShareWallpaperLayout.getVisibility()== View.GONE){

                    mDownloadLayout.setVisibility(View.VISIBLE);
                    mSetWallpaperLayout.setVisibility(View.VISIBLE);
                    mShareWallpaperLayout.setVisibility(View.VISIBLE);
                    mDownloadLayout.startAnimation(mShowLayout);
                    mSetWallpaperLayout.startAnimation(mShowLayout);
                    mShareWallpaperLayout.startAnimation(mShowLayout);
                    mFab.startAnimation(mShowButton);


                }
                else
                {
                    mDownloadLayout.setVisibility(View.GONE);
                    mDownload.setEnabled(false);
                    mSetWallpaperLayout.setVisibility(View.GONE);
                    mSetWallpaper.setEnabled(false);
                    mShareWallpaperLayout.setVisibility(View.GONE);
                    mShareWallpaper.setEnabled(false);
                    mDownloadLayout.startAnimation(mHideLayout);
                    mSetWallpaperLayout.startAnimation(mHideLayout);
                    mShareWallpaperLayout.startAnimation(mHideLayout);
                    mFab.startAnimation(mHideButton);
                }
            }
        });



        mDownload.setOnClickListener(this);
        mSetWallpaper.setOnClickListener(this);
        mShareWallpaper.setOnClickListener(this);


//        mSetWallpaper.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(Set_WallpaperActivity.this, "set wallpaper clicked", Toast.LENGTH_SHORT).show();
//            startWall();
//
//            }
//        });
//
//
//        mShareWallpaper.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(Set_WallpaperActivity.this, "Share button clicked", Toast.LENGTH_SHORT).show();
//                        startShare();
//                    }
//                });
//
//        //Image download part...
//
//        mDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                startSave();
////                Picasso.get()
////                        .load(imgUrl)
////                        .into(new Target() {
////                                  @Override
////                                  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
////                                      try {
////                                          String root = Environment.getExternalStorageDirectory().toString();
////                                          File myDir = new File(root + "/WallpaperClub");
////
////                                          if (!myDir.exists()) {
////
////                                              myDir.mkdirs();
////
////                                          }
////
////                                          String name = new Date().toString() + ".jpg";
////                                          myDir = new File(myDir, name);
////                                          FileOutputStream out = new FileOutputStream(myDir);
////                                          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
////
////                                          // show image from file to gallery..
////                                          Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
////                                          intent.setData(Uri.fromFile(myDir));
////                                          sendBroadcast(intent);
////
////                                          out.flush();
////                                          out.close();
////
////                                          Toast.makeText(Set_WallpaperActivity.this, "Download Successful", Toast.LENGTH_SHORT).show();
////                                          progressBar.setVisibility(View.INVISIBLE);
////                                      } catch(Exception e){
////                                          // some action
////
////                                          Toast.makeText(Set_WallpaperActivity.this, "Download failed", Toast.LENGTH_SHORT).show();
////                                          progressBar.setVisibility(View.INVISIBLE);
////                                      }
////                                  }
////
////                            @Override
////                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
////
////                            }
////
////                            @Override
////                            public void onPrepareLoad(Drawable placeHolderDrawable) {
////
////                                  }
////                              }
////                        );
//
//            }
//        });


    }

    public static Bitmap viewToBitmap(View view,int width,int height){
        Bitmap bm=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bm);
        view.draw(canvas);
        return bm;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_set: {
                startWall();

                break;
            }
            case R.id.btn_download: {
                startSave();
                break;
            }
            case R.id.btn_share: {
             //   startShare();
                Toast.makeText(Set_WallpaperActivity.this, "Can't share now, wait until update", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }




//
//    private void refreshGallery(File new_file) {
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        intent.setData(Uri.fromFile(new_file));
//        sendBroadcast(intent);
//    }

// Share Image..

//    private void startShare() {
//        Bitmap bitmap=viewToBitmap(wallpaperImg, wallpaperImg.getWidth(),wallpaperImg.getHeight());
//        Intent intent=new Intent(Intent.ACTION_SEND);
//        intent.setType("image/jpeg");
//        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
//        File file=new File(Environment.getExternalStorageDirectory()+File.separator+"ImageDemo.jpg");
//        try {
//            file.createNewFile();
//            FileOutputStream fileOutputStream=new FileOutputStream(file);
//            fileOutputStream.write(byteArrayOutputStream.toByteArray());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        intent.putExtra(Intent.EXTRA_STREAM,Uri.parse("file:///sdcard/ImageDemo.jpg"));
//        startActivity(Intent.createChooser(intent,"Share Image"));
//
//    }

    private void startSave() {

        FileOutputStream fileOutputStream=null;
        File file=getDisc();
        if (!file.exists()&&!file.mkdirs()){
            Toast.makeText(this,"Can't create directory to save image",Toast.LENGTH_SHORT).show();
            return;
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyymmhhssmm");
        String date=simpleDateFormat.format(new Date());
        String name="Img"+date+".jpg";
        String file_name=file.getAbsolutePath()+"/"+name;
        File new_file=new File(file_name);
        try {
            fileOutputStream=new FileOutputStream(new_file);
            Bitmap bitmap=viewToBitmap(wallpaperImg, wallpaperImg.getWidth(),wallpaperImg.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Toast.makeText(this,"Wallpaper saved",Toast.LENGTH_SHORT).show();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshGallery(new_file);
    }
    public void refreshGallery(File file){
        Intent intent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
    private File getDisc(){
        File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file,"Wallpaper_Club");
    }

    private void startWall() {
        WallpaperManager wallpaperManager=WallpaperManager.getInstance(getApplicationContext());
        try {
            wallpaperManager.setBitmap(viewToBitmap(wallpaperImg, wallpaperImg.getWidth(),wallpaperImg.getHeight()));
            Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorView.setSystemUiVisibility(hideSystemBar());
        }
    }
    private int hideSystemBar(){
       return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                |View.SYSTEM_UI_FLAG_FULLSCREEN
                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }



}
