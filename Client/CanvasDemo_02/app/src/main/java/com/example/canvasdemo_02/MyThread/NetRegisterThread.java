package com.example.canvasdemo_02.MyThread;

import com.example.canvasdemo_02.HttpUtils;

import org.json.JSONObject;

public class NetRegisterThread extends Thread{
    private String url;
    private String result;
    private JSONObject params;
    public String getIpv4() {
        return ipv4;
    }

    public void setIpv4(String ipv4) {
        this.ipv4 = ipv4;
    }

    private String ipv4;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResult() {
        return result;
    }

    public NetRegisterThread(String url,JSONObject params) {
        this.url = url;
        this.params=params;
    }

    @Override
    public void run() {
        try {
            result = HttpUtils.sendPostMessage("http://"+ipv4+":8080/register",params,"utf-8");
        }catch (Exception e){
            System.out.println("send post err!!");
        }
        System.out.println("result->"+result);
//        Looper.prepare();
//        //Toast.makeText(getContext(), "result->"+result, Toast.LENGTH_SHORT).show();
//        Looper.loop();
    }
}
