package com.example.canvasdemo_02.WiFi;

import static android.content.Context.WIFI_SERVICE;

import android.content.ContextWrapper;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;



import com.example.canvasdemo_02.Beans.WifiStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyWifiManager {

    private WifiManager wifiManager;
    List<WifiStream> allWifi = new ArrayList<WifiStream>();

    public MyWifiManager(WifiManager wifiManager) {
        this.wifiManager=wifiManager;
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            wifiManager.setWifiEnabled(true);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public List<WifiStream> getWifiList() {
        if (!allWifi.isEmpty()){
            allWifi.clear();
        }
        wifiManager.startScan();
        List<ScanResult> scanWifiList01 = wifiManager.getScanResults();
        if (scanWifiList01 != null && scanWifiList01.size() > 0) {
            //对wifilist进行排序
            //Collections.sort(scanWifiList,comparator);

            for (int i = 0; i < scanWifiList01.size(); i++) {
                ScanResult scanResult = scanWifiList01.get(i);
                if (!scanResult.BSSID.isEmpty()) {//wifi名称不是空的话
                    allWifi.add(new WifiStream(scanResult.BSSID,scanResult.level));
                }
            }
        }
        wifiManager.startScan();
        List<ScanResult> scanWifiList02 = wifiManager.getScanResults();
        if(scanWifiList02!=null && scanWifiList02.size()>0){
            for(int i = 0; i < scanWifiList01.size(); i++){
                ScanResult scanResult = scanWifiList01.get(i);
                boolean flag_ = false;
                int j=0;
                for(;j<allWifi.size();j++){
                    if(allWifi.get(j).getWifiStream_mac().equals(scanResult.BSSID)){
                        //System.out.println("-----------------------------------------");
                        flag_=true;
                        break;
                    }
                }
                if(!flag_){
                    allWifi.add(new WifiStream(scanResult.BSSID,scanResult.level));
                }else {
                    //System.out.println("wifi1:"+scanResult.level+",wifi2:"+allWifi.get(j).getWifiStream_rssi());
                    //System.out.println("result"+((float)scanResult.level+(float)allWifi.get(j).getWifiStream_rssi())/(float)2);
                    allWifi.get(j).setWifiStream_rssi(((float)scanResult.level+(float)allWifi.get(j).getWifiStream_rssi())/(float)2);
                }
            }
        }

        return allWifi;
    }

    static Comparator<ScanResult> comparator = new Comparator<ScanResult>() {
        @Override
        public int compare(ScanResult p1 , ScanResult p2 ) {
            if( p1.level > p2.level ){
                return -1 ;  //负数
            }else if ( p1.level < p2.level) {
                return 1 ;  //正数
            }else {
                return 0;  //相等为0
            }
        }
    };

}
