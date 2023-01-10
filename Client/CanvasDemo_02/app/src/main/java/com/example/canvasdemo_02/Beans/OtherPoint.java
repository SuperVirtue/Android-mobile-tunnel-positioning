package com.example.canvasdemo_02.Beans;

import java.util.ArrayList;
import java.util.List;

public class OtherPoint {
    private String name;
    private String account;
    private float location_x;
    private float location_y;

    public OtherPoint(String str){
        String str_ = str.replaceAll("\'","");
        String[] str_list = str_.split(",");
        List<String> ss = new ArrayList<>();
        for(int i = 0;i<str_list.length;i++){
            ss.add(str_list[i].replaceAll(" ",""));
        }
        if(ss.size()==4){
            String[] str_list_1 = ss.get(0).split("name=");
            this.name = str_list_1[1];
            String[] str_list_2 = ss.get(1).split("account=");
            this.account = str_list_2[1];
            String[] str_list_3 = ss.get(2).split("location_x=");
            this.location_x = Float.parseFloat(str_list_3[1]);
            String[] str_list_4 = ss.get(3).split("location_y=");
            this.location_y = Float.parseFloat(str_list_4[1].replaceFirst(".$",""));
        }else {
            System.out.println("ERORR!!!!!!!!!!");
            return ;
        }

    }

    public OtherPoint(String name, String account, float location_x, float location_y) {
        this.name = name;
        this.account = account;
        this.location_x = location_x;
        this.location_y = location_y;
    }
    public OtherPoint(String name,  float location_x, float location_y) {
        this.name = name;

        this.location_x = location_x;
        this.location_y = location_y;
    }

    public OtherPoint() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public float getLocation_x() {
        return location_x;
    }

    public void setLocation_x(float location_x) {
        this.location_x = location_x;
    }

    public float getLocation_y() {
        return location_y;
    }

    public void setLocation_y(float location_y) {
        this.location_y = location_y;
    }

    @Override
    public String toString() {
        return "otherPoint{" +
                "name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", location_x=" + location_x +
                ", location_y=" + location_y +
                '}';
    }

}
