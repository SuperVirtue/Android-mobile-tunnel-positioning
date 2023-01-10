package com.example.canvasdemo_02;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class mySQLite extends SQLiteOpenHelper{
    private final static String TABLE_NAME = "user_table";
    public static final String CREATE_USER = "create table IF NOT EXISTS user_table(user_id INTEGER not null primary key autoincrement,name char(20),phone char(20),account varchar(255),password varchar(255),position int)" ;
    private Context mContext;

    /*
     * 构造方法参数说明
     * 第一个：传入上下文对象
     * 第二个：要创建的数据库名字
     * 第三个：油标工厂,传入油标对象，其实就是一个指针的功能。和ResultSet功能差不多，这个参数一般传入null
     * 第四个: 数据库版本号，用于升级的时候调用。版本号必须大于1
     * 构造方法四个参数传入其实是给父类调用的。
     * */
    public mySQLite(Context context, String name, CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        mContext = context;
    }
    //数据库被创建的时候会调用这个方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_USER);
        Toast.makeText(mContext,"Database Create Successful",Toast.LENGTH_LONG).show();
    }
    //数据库升级的时候调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //插入数据时调用
    public long insert(String name,String phone,String account,String password,int position) {
        //Toast.makeText(mContext.getApplicationContext(), "zzg", Toast.LENGTH_LONG).show();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("phone", phone);
        cv.put("account", account);
        cv.put("password",password);
        cv.put("position",position);
        long row = db.insert(TABLE_NAME, null, cv);
        //Toast.makeText(mContext.getApplicationContext(), String.valueOf(row), Toast.LENGTH_LONG).show();
        return row;
    }

    class PersonModel{
        int id;
        String name;
        String phone;
        String account;
        String password;
        int position;
        PersonModel(int id,String name,String phone,String account,String password,int position){
            this.id=id;
            this.name=name;
            this.phone=phone;
            this.account=account;
            this.password=password;
            this.position=position;
        }
        PersonModel(){}
        public void setId(int id){
            this.id=id;
        }
        public void setName(String name){
            this.name=name;
        }
        public void setPhone(String phone){
            this.phone=phone;
        }
        public void setAccount(String account){
            this.account=account;
        }
        public void setPassword(String password){
            this.password=password;
        }
        public void setPosition(int position){
            this.position=position;
        }
    }

    public boolean isUser(String account,String password){
        //查询全部数据
        Cursor cursor = getWritableDatabase().query(TABLE_NAME,null,"account='"+account+"' and "+"password='"+password+"'",null,null,null,null,null);
        List<PersonModel> list = new ArrayList<>();
        if(cursor.getCount() > 0)
        {
            //移动到首位
            cursor.moveToFirst();
            do {
                @SuppressLint("Range")int id = cursor.getInt(cursor.getColumnIndex("user_id"));
                @SuppressLint("Range")String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range")String phone = cursor.getString(cursor.getColumnIndex("phone"));
                @SuppressLint("Range")String account_ = cursor.getString(cursor.getColumnIndex("account"));
                @SuppressLint("Range")String password_ = cursor.getString(cursor.getColumnIndex("password"));
                @SuppressLint("Range")int position = cursor.getInt(cursor.getColumnIndex("position"));

                PersonModel model = new PersonModel();
                model.setId(id);
                model.setName(name);
                model.setPhone(phone);
                model.setAccount(account_);
                model.setPassword(password_);
                model.setPosition(position);
                list.add(model);
                //移动到下一位
                cursor.moveToNext();
            }while (cursor.moveToNext());
        }
        cursor.close();
        getWritableDatabase().close();
        if(list.size()>0){
            return true;
        }
        return false;
    }

    public boolean isUser(String account) {
        //查询全部的用户名
        Cursor cursor = getWritableDatabase().query(TABLE_NAME,new String[]{"account"},null,null,null,null,null,null);
        List<String> accountList = new ArrayList<>();
        if(cursor.getCount() > 0){
            //移动到首位
            cursor.moveToFirst();
            do {
                @SuppressLint("Range")String account_ = cursor.getString(cursor.getColumnIndex("account"));
                accountList.add(account_);
                cursor.moveToNext();
            }while (cursor.moveToNext());
        }
        cursor.close();
        getWritableDatabase().close();
        if(accountList.contains(account)){
            return true;
        }else
            return false;
    }

}
