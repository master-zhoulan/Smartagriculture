package com.example.menu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;


public class SettingFragment extends Fragment {
    private View rootview;
    private Button recognize;
    private Button face_add;
    private Button face_sub;
//    private ImageView imageView;
    private EditText facename;

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);  //注册
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootview == null) {
            rootview = inflater.inflate(R.layout.setting_layout, container, false);
        }
        recognize = rootview.findViewById(R.id.recognize);
//        imageView = rootview.findViewById(R.id.imageView);
        facename = rootview.findViewById(R.id.facename);
        face_add = rootview.findViewById(R.id.face_add);
        face_sub = rootview.findViewById(R.id.face_sub);

        recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new eventbusfasong("{\"PERSONCMD\":3 , \"PERSONNAME\":\"\" }"));
            }
        });

        face_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = facename.getText().toString();
                EventBus.getDefault().post(new eventbusfasong("{\"PERSONCMD\":1, \"PERSONNAME\":\"" + text + "\"}"));
            }
        });

        face_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = facename.getText().toString();
                EventBus.getDefault().post(new eventbusfasong("{\"PERSONCMD\":2, \"PERSONNAME\":\"" + text + "\"}" ));
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
            int RESULT = jsonObject.optInt("RESULT");
            if(RESULT == 1){
                Toast.makeText(SettingFragment.this.getContext(), "人脸操作成功", Toast.LENGTH_LONG).show();
            }
            else if(RESULT == 0){
//                Toast.makeText(SettingFragment.this.getContext(), "人脸操作失败", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);  //反注册EventBus
    }
}