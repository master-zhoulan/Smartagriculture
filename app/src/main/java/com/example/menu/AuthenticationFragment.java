package com.example.menu;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;


public class AuthenticationFragment extends Fragment {

    private View rootview;
    private TextView renzheng;
    private Button num1;
    private Button num2;
    private Button num3;
    private Button num4;
    private Button num5;
    private Button num6;
    private Button num7;
    private Button num8;
    private Button num9;
    private Button num0;
    private Button queren;

//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);  //注册
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootview == null) {
            rootview = inflater.inflate(R.layout.authentication_layout, container, false);
        }
        renzheng = rootview.findViewById(R.id.renzheng);
        num0 = rootview.findViewById(R.id.num0);
        num1 = rootview.findViewById(R.id.num1);
        num2 = rootview.findViewById(R.id.num2);
        num3 = rootview.findViewById(R.id.num3);
        num4 = rootview.findViewById(R.id.num4);
        num5 = rootview.findViewById(R.id.num5);
        num6 = rootview.findViewById(R.id.num6);
        num7 = rootview.findViewById(R.id.num7);
        num8 = rootview.findViewById(R.id.num8);
        num9 = rootview.findViewById(R.id.num9);
        queren = rootview.findViewById(R.id.sure);

        num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNumber = renzheng.getText().toString();
                String newNumber = currentNumber + "0";
                renzheng.setText("");
                renzheng.setText(newNumber);
            }
        });

        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNumber = renzheng.getText().toString();
                String newNumber = currentNumber + "1";
                renzheng.setText("");
                renzheng.setText(newNumber);
            }
        });

        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNumber = renzheng.getText().toString();
                String newNumber = currentNumber + "2";
                renzheng.setText("");
                renzheng.setText(newNumber);
            }
        });

        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNumber = renzheng.getText().toString();
                String newNumber = currentNumber + "3";
                renzheng.setText("");
                renzheng.setText(newNumber);
            }
        });

        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNumber = renzheng.getText().toString();
                String newNumber = currentNumber + "4";
                renzheng.setText("");
                renzheng.setText(newNumber);
            }
        });
        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNumber = renzheng.getText().toString();
                String newNumber = currentNumber + "5";
                renzheng.setText("");
                renzheng.setText(newNumber);
            }
        });
        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNumber = renzheng.getText().toString();
                String newNumber = currentNumber + "6";
                renzheng.setText("");
                renzheng.setText(newNumber);
            }
        });
        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNumber = renzheng.getText().toString();
                String newNumber = currentNumber + "7";
                renzheng.setText("");
                renzheng.setText(newNumber);
            }
        });
        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNumber = renzheng.getText().toString();
                String newNumber = currentNumber + "8";
                renzheng.setText("");
                renzheng.setText(newNumber);
            }
        });
        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentNumber = renzheng.getText().toString();
                String newNumber = currentNumber + "9";
                renzheng.setText("");
                renzheng.setText(newNumber);
            }
        });

        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(renzheng.getText().toString().isEmpty()){
                    Toast.makeText(AuthenticationFragment.this.getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                }
                else {
                    EventBus.getDefault().post(new eventbusfasong(renzheng.getText().toString()));
                    renzheng.setText("认证中");
                    // 修改发送信息
                }
            }
        });

        return rootview;
    }

    @Subscribe(threadMode = ThreadMode.POSTING,sticky = true)
    public void onEvent(eventbus event) {
        String msg = event.getMsg();
        try {
            //json解析字符串
            JSONObject jsonObject = new JSONObject(msg);
            //通过 键 获取到 值
            String RC522 = jsonObject.optString("RC522");
            if(RC522.equals("")){
                renzheng.setText("");
                renzheng.setText("认证成功");
            }
            else{
                renzheng.setText("认证失败");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //EventBus.getDefault().cancelEventDelivery(event);  //取消事件继续发布
        //Toast.makeText(MessageFragment.this.getContext(), msg+"接收1", Toast.LENGTH_LONG).show();
    }

//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);  //反注册EventBus
//    }
}