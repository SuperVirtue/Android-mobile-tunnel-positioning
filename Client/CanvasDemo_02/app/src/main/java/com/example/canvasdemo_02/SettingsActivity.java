package com.example.canvasdemo_02;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.canvasdemo_02.databinding.ActivitySettingsBinding;

import java.lang.ref.WeakReference;

public class SettingsActivity extends AppCompatActivity {
    //定义存储
    SharedPreferences sp = null;

    private ActivitySettingsBinding binding;
    public static final int REQUEST_IMAGE_OPEN = 102;
    public String img_path=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        binding = ActivitySettingsBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        sp = SettingsActivity.this.getSharedPreferences("openinfo", Context.MODE_PRIVATE);
        binding.editTextUrl.setText(sp.getString("ipv4", null));
        img_path=sp.getString("img_path", null);
        if(img_path!=null){
            Bitmap bitmap = BitmapFactory.decodeFile(img_path);
            binding.buttonImage.setImageBitmap(bitmap);
            binding.editTextTextPersonName.setText(Float.toString(sp.getFloat("height", (float) 0.0)));
            binding.editTextTextPersonName2.setText(Float.toString(sp.getFloat("width", (float) 0.0)));
            binding.editTextTextPersonName3.setText(Float.toString(sp.getFloat("ratio", (float) 0.0)));
        }

    }

    public void onBackPressed() {
        String ipv4 = binding.editTextUrl.getText().toString();
        Intent intent = new Intent();
        //通过putExtra()方法返回数据
        intent.putExtra("ipv4",ipv4);
        if(img_path!=null){
            intent.putExtra("img_path",img_path);
        }else {
            intent.putExtra("img_path","");
        }
        setResult(2,intent);
        finish();
    }

    public void OKClick(View v) {
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString("ipv4",binding.editTextUrl.getText().toString());
        editor.putString("img_path",img_path);

        editor.putFloat("height",binding.editTextTextPersonName.getText().toString().isEmpty()?(float)0.0:Float.valueOf(binding.editTextTextPersonName.getText().toString()));
        editor.putFloat("width",binding.editTextTextPersonName.getText().toString().isEmpty()?(float)0.0:Float.valueOf(binding.editTextTextPersonName2.getText().toString()));
        editor.putFloat("ratio",binding.editTextTextPersonName.getText().toString().isEmpty()?(float)0.0:Float.valueOf(binding.editTextTextPersonName3.getText().toString()));
        editor.commit();
        onBackPressed();
    }
    public void CancelClick(View v) {
        Intent intent = new Intent();
        //通过putExtra()方法返回数据
        setResult(2,intent);
        finish();
    }
    public void ImageClick(View v) {
        if (ContextCompat.checkSelfPermission(SettingsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        } else {
            //打开相册
            Intent intent = new Intent(Intent.ACTION_PICK);
            //指定获取的是图片
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_OPEN);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_OPEN:
                if (resultCode == RESULT_OK) { // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(29)
    private void handleImageOnKitKat(Intent data) {
        System.out.println("-----------------------------------");
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        // 根据图片路径显示图片
        displayImage(imagePath);
    }

    /**
     * android 4.4以前的处理方式
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            img_path=imagePath;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap thumbImg = Bitmap.createScaledBitmap(bitmap,binding.buttonImage.getWidth(),binding.buttonImage.getHeight(), true);
            binding.buttonImage.setImageBitmap(thumbImg);
        } else {
            img_path=null;
            Toast.makeText(this, "获取相册图片失败", Toast.LENGTH_SHORT).show();
        }
    }





}