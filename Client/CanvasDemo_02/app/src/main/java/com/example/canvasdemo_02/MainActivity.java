package com.example.canvasdemo_02;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.canvasdemo_02.databinding.*;

import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;//设置权限之后回调函数中用于区别不同权限回调的自定义常量值
    private static String ipv4;
    private static String img_path;
    private static float tunnel_width;

    public static float getTunnel_width() {
        return tunnel_width;
    }

    public static void setTunnel_width(float tunnel_width) {
        MainActivity.tunnel_width = tunnel_width;
    }

    public static float getTunnel_height() {
        return tunnel_height;
    }

    public static void setTunnel_height(float tunnel_height) {
        MainActivity.tunnel_height = tunnel_height;
    }

    public static float getTunnel_ratio() {
        return tunnel_ratio;
    }

    public static void setTunnel_ratio(float tunnel_ratio) {
        MainActivity.tunnel_ratio = tunnel_ratio;
    }

    private static float tunnel_height;
    private static float tunnel_ratio;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public static int screen_widths;
    public static int screen_height;
    //定义存储
    SharedPreferences sp = null;
    public static  String getIpv4() {
        return ipv4;
    }
    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }
    public static String getImg_path() {
        return img_path;
    }
    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getApplicationContext().getSharedPreferences("openinfo", Context.MODE_PRIVATE);
        setIpv4(sp.getString("ipv4", null));
        setImg_path(sp.getString("img_path", null));
        setTunnel_height(sp.getFloat("height",(float) 0.0));
        setTunnel_width(sp.getFloat("width",(float) 0.0));
        setTunnel_ratio(sp.getFloat("ratio",(float) 0.0));
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screen_widths = size.x;
        screen_height = size.y;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        requestPermissionBeforeStart();
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings://应该在这里进入设置页面
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivityForResult(intent,1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 2) {
                    ///获取数据
                    String ipv4 = data.getStringExtra("ipv4");
                    String img_path = data.getStringExtra("img_path");
                    Float w=data.getFloatExtra("width",(float) 0.0);
                    Float h=data.getFloatExtra("height",(float) 0.0);
                    Float r=data.getFloatExtra("ratio",(float) 0.0);
                    setIpv4(ipv4);
                    setImg_path(img_path);
                    setTunnel_height(w);
                    setTunnel_height(h);
                    setTunnel_ratio(r);
                }
        }
    }

    public void requestPermissionBeforeStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && (checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED||
                checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED)) {
            if (Build.VERSION.SDK_INT >= 29) {
                requestPermissions(new String[] {
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }else {
                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }
        }
    }
}