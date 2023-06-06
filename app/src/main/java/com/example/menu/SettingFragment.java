package com.example.menu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class SettingFragment extends Fragment {
    private View rootview;
    private Switch recognize;
    private ImageView imageView;
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);  //注册
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
        imageView = rootview.findViewById(R.id.imageView);

        recognize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"LED1\":1}"));
                    Toast.makeText(SettingFragment.this.getContext(), "人脸识别开始", Toast.LENGTH_SHORT).show();
                }else{
                    //未选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"LED1\":0}"));
                    Toast.makeText(SettingFragment.this.getContext(), "人脸识别结束", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootview;
    }

    @Subscribe(threadMode = ThreadMode.POSTING,sticky = true)
    public void onEvent(eventbus event) {
        byte[] imageData = event.getMsg().getBytes(); // 二进制图片数据
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        ImageView imageView = rootview.findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);  //反注册EventBus
    }
}