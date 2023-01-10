package com.example.canvasdemo_02.Beans;

public class WifiStream
{
    public String getWifiStream_mac() {
        return WifiStream_mac;
    }

    public void setWifiStream_mac(String wifiStream_mac) {
        WifiStream_mac = wifiStream_mac;
    }

    public float getWifiStream_rssi() {
        return WifiStream_rssi;
    }

    public void setWifiStream_rssi(float wifiStream_rssi) {
        WifiStream_rssi = wifiStream_rssi;
    }

    String WifiStream_mac;
    float WifiStream_rssi;

    public WifiStream(String bssid, float level) {
        this.WifiStream_mac = bssid;
        this.WifiStream_rssi = level;
    }

    public boolean isEmpty(){
        if(WifiStream_mac.isEmpty()){
            return true;
        }else if(String.valueOf(WifiStream_rssi).equals("")){
            return true;
        }else
            return false;
    }
}