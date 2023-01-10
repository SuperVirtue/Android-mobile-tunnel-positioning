package com.example.canvasdemo_02;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.canvasdemo_02.MyThread.NetRegisterThread;
import com.example.canvasdemo_02.databinding.FragmentRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentRegisterBinding binding;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String insertFlag="0";
    int Position = 0;
    String result="";
    private mySQLite myDatabaseHelper;
    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///new一个数据库对象
        //myDatabaseHelper = new mySQLite(getActivity(), "user.db", null, 1);
        ///
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//
//        View accountView = View.inflate(getContext(), R.layout.fragment_register, null);
//        RadioButton radioButton_1 = (RadioButton) accountView.findViewById(R.id.RadioButton_1);
//        radioButton_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("hello:","button_1");
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        binding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertFlag="0";
                Log.i("Tag", "onClick: sorry+"+insertFlag);
                if(binding.editName.getText().toString().isEmpty()){
                    insertFlag="001";//name字段为空
                }else{
                    String regEx_0="^1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$";
                    Pattern pattern_0 = Pattern.compile(regEx_0);
                    Matcher matcher_0 = pattern_0.matcher(binding.editPhone.getText().toString());
                    if(!matcher_0.matches()){
                        insertFlag="002";//电话号码不符合规范
                    }else {
                        String regEx_1 = "^[a-z0-9_-]{3,15}$";
                        Pattern pattern_1 = Pattern.compile(regEx_1);
                        Matcher matcher_1 = pattern_1.matcher(binding.editAccount.getText().toString());
                        if(!matcher_1.matches()){
                            insertFlag="003";//用户名不符合规范
                        }else {
                            String regEx_2 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[\\s\\S]{8,16}$";
                            Pattern pattern_2 = Pattern.compile(regEx_2);
                            Matcher matcher_2 = pattern_2.matcher(binding.editPassword.getText().toString());
                            if(!matcher_2.matches()){
                                insertFlag="004";//密码不符合规范
                            }else {

                                System.out.println("Position_:"+Position);
                                RadioButton rb_0 = (RadioButton)binding.radioGroup.getChildAt(0);
                                RadioButton rb_1 = (RadioButton)binding.radioGroup.getChildAt(1);
//                                    if(rb_0.isChecked()){
//                                        Log.i("Tag","radioButton:0 is checked");
//                                        Position=1;
//
//                                    }else if(rb_1.isChecked()){
//                                        Log.i("Tag","radioButton:1 is checked");
//                                        Position=0;
//                                    }else {
//                                        insertFlag="005";
//                                    }
                                if(rb_0.isChecked()){
                                    Position=1;
                                    Log.i("Tag_q","radioButton:0 is checked");
                                }else {
                                    Position=0;
                                    Log.i("Tag_q","radioButton:1 is checked");
                                }
                            }
                        }
                    }
                }

                if(insertFlag.equals("0")){
//                    long is_ok = myDatabaseHelper.insert(binding.editName.getText().toString(),binding.editPhone.getText().toString(),binding.editAccount.getText().toString(),binding.editPassword.getText().toString(),0);
//                    if(is_ok>0){
//                        Toast.makeText(getContext(),"创建成功",Toast.LENGTH_LONG).show();
//                    }
                    JSONObject params = new JSONObject();
                    try {
                        params.put("name", binding.editName.getText().toString());
                        params.put("iphone", binding.editPhone.getText().toString());
                        params.put("account", binding.editAccount.getText().toString());
                        params.put("password", binding.editPassword.getText().toString());
                        params.put("position", Position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Position:"+Position);
                    NetRegisterThread registerThread = new NetRegisterThread("http://106.15.182.169:8080/register",params);
                    registerThread.setIpv4(MainActivity.getIpv4());
                    registerThread.start();
                    try {
                        registerThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    result=registerThread.getResult();
                    //Toast.makeText(getContext(),result,Toast.LENGTH_LONG).show();
                    if(result.equals("-2")){
                        insertFlag="006";
                    }else if(result.equals("-1")){
                        insertFlag="000";
                    }else {
                        Toast.makeText(getContext(),"创建成功",Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }
                }
                if(insertFlag.equals("000")){
                    Toast.makeText(getContext(),"000：该账户已存在，请重新输入。",Toast.LENGTH_LONG).show();
                }
                if(insertFlag.equals("001")){
                    Toast.makeText(getContext(),"001：姓名不能为空。",Toast.LENGTH_LONG).show();
                }
                if(insertFlag.equals("002")){
                    Toast.makeText(getContext(),"002：电话号码不符合规范，请重新输入。",Toast.LENGTH_LONG).show();
                }
                if(insertFlag.equals("003")){
                    Toast.makeText(getContext(),"003：用户名不符合规范，请重新输入。",Toast.LENGTH_LONG).show();
                }
                if(insertFlag.equals("004")){
                    Toast.makeText(getContext(),"004：密码不符合规范，请重新输入。",Toast.LENGTH_LONG).show();
                }
                if(insertFlag.equals("005")){
                    Toast.makeText(getContext(),"005：请选择一个权限。",Toast.LENGTH_LONG).show();
                }
                if(insertFlag.equals("006")){
                    Toast.makeText(getContext(),"006：网络连接失败。",Toast.LENGTH_LONG).show();
                }
                Log.i("Tag", "onClick: sorry-"+insertFlag);
            }
        });

        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("tag","cancel_button");
                getActivity().onBackPressed();
            }
        });

        return binding.getRoot();
    }
}