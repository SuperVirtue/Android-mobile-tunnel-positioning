package com.example.canvasdemo_02;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.canvasdemo_02.MyThread.NetLoginThread;
import com.example.canvasdemo_02.databinding.FragmentFirstBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstFragment extends Fragment {

    static String CurrentAccount;
    private FragmentFirstBinding binding;
    //private mySQLite myDatabaseHelper;
    String  result="-1";
    //定义记住密码可选框
    private CheckBox remember_password,auto_login;
    //定义存储
    SharedPreferences sp1 = null;
    SharedPreferences sp2 = null;
    //定义输入框
    private EditText account_edit,password_edit;
    //定义ip地址
    private String ipv4_;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        setHasOptionsMenu(true);
        sp1 = getActivity().getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        sp2 = getActivity().getSharedPreferences("openinfo", Context.MODE_PRIVATE);
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ipv4_=sp2.getString("ipv4",null);
        //初始化对象,然后从存储当中取出存储内容
        remember_password =  (CheckBox) binding.checkBox0;
        auto_login = (CheckBox) binding.checkBox1;
        account_edit = (EditText)binding.editAccount;
        password_edit = (EditText) binding.editPassword;
        if(sp1.getBoolean("checkboxBoolean", false)){
            account_edit.setText(sp1.getString("account", null));
            password_edit.setText(sp1.getString("password", null));
            remember_password.setChecked(true);
        }
        if(sp1.getBoolean("autoLoginBoolean",false)){
            //自动登录
            auto_login.setChecked(true);
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
            CurrentAccount=binding.editAccount.getText().toString();
        }

        binding.checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(auto_login.isChecked()){
                    remember_password.setChecked(true);
                }
            }
        });

        binding.checkBox0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!remember_password.isChecked()){
                    auto_login.setChecked(false);
                }
            }
        });

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.editAccount.getText().toString().isEmpty()||binding.editPassword.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"请输入账户和密码",Toast.LENGTH_LONG).show();
                }else {
                    JSONObject params = new JSONObject();
                    try {
                        params.put("account", binding.editAccount.getText().toString());
                        params.put("password", binding.editPassword.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    NetLoginThread loginThread = new NetLoginThread(params);
                    if(ipv4_!=null){
                       loginThread.setIpv4(ipv4_);
                    }else {
                        loginThread.setIpv4(MainActivity.getIpv4());
                    }
                    loginThread.start();
                    try {
                        loginThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    result=loginThread.getResult();
                    if(result.equals("-2")){
                        Toast.makeText(getContext(),"网络连接失败",Toast.LENGTH_LONG).show();
                    }else if(!result.equals("-1")||(binding.editAccount.getText().toString().equals("admin"))){
                        //登录成功，进行页面转换
                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment);
                        CurrentAccount=binding.editAccount.getText().toString();

                        //查看是否勾选记住密码选项，存储相关内容
                        boolean CheckBoxLogin = remember_password.isChecked();
                        boolean CheckBoxAutomaticLogin = auto_login.isChecked();
                        SharedPreferences.Editor editor = sp1.edit();
                        if(CheckBoxLogin){
                            //SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.putString("account", binding.editAccount.getText().toString());
                            editor.putString("password", binding.editPassword.getText().toString());
                            editor.putBoolean("checkboxBoolean", true);
                            if(CheckBoxAutomaticLogin){
                                editor.putBoolean("autoLoginBoolean",true);
                            }else {
                                editor.putBoolean("autoLoginBoolean",false);
                            }
                            editor.commit();
                        }else {
                            editor.clear();
                            editor.putString("account", null);
                            editor.putString("password", null);
                            editor.putBoolean("checkboxBoolean", false);
                            editor.putBoolean("autoLoginBoolean",false);
                            editor.commit();
                        }
                    }else{
                        Toast.makeText(getContext(),"账户或者密码输入错误",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        binding.buttonRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Log.i("Tag","hello");
                //myDatabaseHelper.getWritableDatabase();
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_Register);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}