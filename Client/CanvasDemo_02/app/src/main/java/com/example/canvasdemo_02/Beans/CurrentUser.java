package com.example.canvasdemo_02.Beans;

public class CurrentUser {
    private String account;
    private String name;
    private String phone;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CurrentUser(String account, String name, String phone, int position) {
        this.account = account;
        this.name = name;
        this.phone = phone;
        this.position = position;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", position=" + position +
                '}';
    }
}
