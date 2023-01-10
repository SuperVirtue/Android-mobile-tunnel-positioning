package com.example.canvasdemo_02.MyThread;

import com.example.canvasdemo_02.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class NetLoginThread extends Thread{
    private String result;
    private JSONObject para;

    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    private String ipv4;
    public String getResult() {
        return result;
    }

    public NetLoginThread(JSONObject para) {
        this.para=para;
    }

    @Override
    public void run() {
        try {
            result = HttpUtils.sendPostMessage("http://"+ipv4+":8080/login",para,"utf-8");
        }catch (Exception e){
            System.out.println("send post err!!");
        }
        System.out.println("result->"+result);
    }
}
