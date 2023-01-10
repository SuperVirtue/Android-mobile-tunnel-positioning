package com.example.canvasdemo_02;

import static android.content.Context.WIFI_SERVICE;

import android.Manifest;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.canvasdemo_02.Beans.OtherPoint;
import com.example.canvasdemo_02.Beans.WifiStream;
import com.example.canvasdemo_02.MyThread.NetLocationThread;
import com.example.canvasdemo_02.WiFi.MyWifiManager;
import com.example.canvasdemo_02.databinding.FragmentSecondBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    //加载的图片
    ImageSource imageSource = ImageSource.resource(R.drawable.lab_ap);
    private static final int COMPLETED = 0;
    Timer timer ;
    MyWifiManager wifiManager;
    List<WifiStream> wifiList;
    float tunnel_height = 13.5F;
    float tunnel_width = 13.0F;
    float tunnel_ratio = 3.0F;
    String result="";
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View ret = inflater.inflate(R.layout.fragment_second,container,false);
        //Log.i("Tag","Msg-hello@");
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        WifiManager wifimanager= (WifiManager) ret.getContext().getApplicationContext().getSystemService(WIFI_SERVICE);//获取wifi服务
        wifiManager = new MyWifiManager(wifimanager);
        if(MainActivity.getImg_path()!=null&&!MainActivity.getImg_path().equals("")){
            imageSource = ImageSource.uri(MainActivity.getImg_path());
            tunnel_height = MainActivity.getTunnel_height();
            tunnel_width = MainActivity.getTunnel_width();
            tunnel_ratio = MainActivity.getTunnel_ratio();
        }
        binding.paintBoard.setImage(imageSource);
        //图片上的像素点*3为该控件的坐标点
        binding.paintBoard.setCurrentTPosition(new PointF(2000.0f,1500.0f));
        return binding.getRoot();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            float forecast_x=msg.getData().getFloat("forecast_x");
            float forecast_y=msg.getData().getFloat("forecast_y");
            int width=msg.getData().getInt("width");
            int height=msg.getData().getInt("height");
            List<String> other_location = msg.getData().getStringArrayList("other_location");
            System.out.println("@@@@@@@@@@@@@@@@@@@"+other_location+"@@@@@@@@@@@@@@@@@@@");
            System.out.println("sssssssssssssss"+(float)(forecast_x/tunnel_width)*tunnel_ratio*width+","+(float)(forecast_y/tunnel_height)*tunnel_ratio*height+"ssssssssss");
            if (msg.what == COMPLETED) {
                if(forecast_x!=0&&forecast_y!=0){
                    binding.paintBoard.setCurrentTPosition(new PointF((float)(forecast_x/tunnel_width)*tunnel_ratio*width,
                            (float) ((forecast_y/tunnel_height)*tunnel_ratio*height)));//这里的3是比例，13、13.5是地图的长和宽
                    binding.forecastTextview.setText("forecast_x:"+forecast_x+"\n"+"forecast_y:"+forecast_y);
                }
            }
            if(other_location!=null){
                if(forecast_x!=0&&forecast_y!=0){
                    binding.paintBoard.setFingerprintPoints2(other_location);
                }
            }
        }
    };


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {

            public void onClick_backup(View view) {
                binding.buttonSecond.setEnabled(false);
                BitmapFactory.Options options = new BitmapFactory.Options();
                BitmapFactory.decodeResource(getResources(),R.drawable.lab_ap,options);
                //获取图片的宽高
                int height = options.outHeight;
                int width = options.outWidth;

                //获取wifi表
                wifiList = wifiManager.getWifiList();
                //将列表打包成json数据
                JSONObject wifiParams = new JSONObject();
                for(int i=0;i<wifiList.size();i++){
                    if(!wifiList.get(i).isEmpty()){
                        try {
                            wifiParams.put(wifiList.get(i).getWifiStream_mac(),wifiList.get(i).getWifiStream_rssi());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                JSONObject params = new JSONObject();
                try {
                    params.put("currentUser",FirstFragment.CurrentAccount);
                    params.put("wifi_rssi",wifiParams);
                    Log.i("wifi_rssi",wifiParams.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //将json数据包发送出去
                NetLocationThread loginThread = new NetLocationThread(params);
                loginThread.setIpv4(MainActivity.getIpv4());
                loginThread.start();
                try {
                    loginThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result=loginThread.getResult();
                if(!result.equals("-2")){
                    //解析返回的数据
                    float forecast_x = 0,forecast_y=0;
                    JSONObject own_location =new JSONObject();
                    List<OtherPoint> other_point_list = new ArrayList<>();
                    try {
                        //String转JSONObject
                        JSONObject resultJson = new JSONObject(result);
                        //System.out.println("result:------------------"+result);
                        //取数据
                        own_location=(JSONObject)resultJson.getJSONObject("own_location");
                        forecast_x = (float) own_location.getDouble("forecast_x");
                        forecast_y = (float) own_location.getDouble("forecast_y");
                        if(!resultJson.getJSONArray("other_location").toString().equals("{}")){
                            //将其他人的数据写进list中然后统一画在图像当中
                            JSONArray other_json = resultJson.getJSONArray("other_location");
                            for(int d=0;d<other_json.length();d++){
                                JSONObject one_json_location = new JSONObject();
                                one_json_location=other_json.getJSONObject(d);
                                other_point_list.add(new OtherPoint(one_json_location.get("name").toString(),
                                        one_json_location.get("account").toString(),
                                        (float) ((one_json_location.getDouble("location_x")/(float)13)*3*width),
                                        (float) ((one_json_location.getDouble("location_y")/(float)13.5)*3*height)));
                            }
                            binding.paintBoard.setFingerprintPoints(other_point_list);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //将数据当中的位置信息显示在屏幕上
                    binding.paintBoard.setCurrentTPosition(new PointF((float)(forecast_x/tunnel_width)*tunnel_ratio*width, (float) ((forecast_y/tunnel_height)*tunnel_ratio*height)));//这里的3是比例，13、13.5是地图的长和宽
                    binding.forecastTextview.setText("forecast_x:"+forecast_x+"\n"+"forecast_y:"+forecast_y);
                }else {
                    Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_LONG).show();
                }
                binding.buttonSecond.setEnabled(true);
            }

            @Override
            public void onClick(View view) {
                if(binding.buttonSecond.getText().equals("开始")){
                    binding.buttonSecond.setText(R.string.stop);
                    timer = new Timer();
                    TimerStart(timer);
                }else if(binding.buttonSecond.getText().equals("停止")){
                    binding.buttonSecond.setText(R.string.start);
                    TimerStop(timer);
                }
            }
        });
    }

    //定时器
    public void TimerStart(Timer timer){
        TimerTask task = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                float forecast_x = 0,forecast_y=0;
                BitmapFactory.Options options = new BitmapFactory.Options();
                BitmapFactory.decodeResource(getResources(),R.drawable.lab_ap,options);
                //获取图片的宽高
                int height = options.outHeight;
                int width = options.outWidth;
                //获取wifi表
                wifiList = wifiManager.getWifiList();
                //将列表打包成json数据
                JSONObject wifiParams = new JSONObject();
                for(int i=0;i<wifiList.size();i++){
                    if(!wifiList.get(i).isEmpty()){
                        try {
                            wifiParams.put(wifiList.get(i).getWifiStream_mac(),wifiList.get(i).getWifiStream_rssi());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                JSONObject params = new JSONObject();
                try {
                    params.put("currentUser",FirstFragment.CurrentAccount);
                    params.put("wifi_rssi",wifiParams);
                    Log.i("wifi_rssi",wifiParams.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //将json数据包发送出去
                NetLocationThread loginThread = new NetLocationThread(params);
                loginThread.setIpv4(MainActivity.getIpv4());
                loginThread.start();
                try {
                    loginThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                result=loginThread.getResult();
                if(!result.equals("-2")){
                    //解析返回的数据
                    JSONObject own_location =new JSONObject();
                    List<String> other_point_list = new ArrayList<>();
                    try {
                        Bundle bundle = new Bundle();
                        //String转JSONObject
                        JSONObject resultJson = new JSONObject(result);
                        //System.out.println("result:------------------"+result);
                        //取数据
                        own_location=(JSONObject)resultJson.getJSONObject("own_location");
                        forecast_x = (float) own_location.getDouble("forecast_x");
                        forecast_y = (float) own_location.getDouble("forecast_y");
                        if(!((int)resultJson.getInt("other_count")==0)){
                            if(!resultJson.getJSONArray("other_location").toString().equals("{}")){
                                //将其他人的数据写进list中然后统一画在图像当中
                                JSONArray other_json = resultJson.getJSONArray("other_location");
                                for(int d=0;d<other_json.length();d++){
                                    JSONObject one_json_location = new JSONObject();
                                    one_json_location=other_json.getJSONObject(d);
                                    other_point_list.add(new OtherPoint(one_json_location.get("name").toString(),
                                            one_json_location.get("account").toString(),
                                            (float) ((one_json_location.getDouble("location_x")/(float)tunnel_width)*tunnel_ratio*width),
                                            (float) ((one_json_location.getDouble("location_y")/(float)tunnel_height)*tunnel_ratio*height)).toString());
                                }
                                System.out.println("!!!!!!!!!!!!!!"+other_point_list+"!!!!!!!!!!!!");
                                //binding.paintBoard.setFingerprintPoints(other_point_list);
                                bundle.putStringArrayList("other_location", (ArrayList<String>) other_point_list);
                            }
                        }
                        //将数据当中的位置信息显示在屏幕上
                        Message msg = new Message();
                        msg.what = COMPLETED;
                        bundle.putFloat("forecast_x",forecast_x);  //往Bundle中存放数据
                        bundle.putFloat("forecast_y",forecast_y);  //往Bundle中put数据
                        bundle.putInt("width",width);
                        bundle.putInt("height",height);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Looper.prepare();
                    Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        };
        timer.schedule(task,0,3000);
    }

    public void TimerStop(Timer timer){
        timer.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}