package com.example.menu;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;


public class ContactsFragment extends Fragment {
    private static final String TAG = "ContactsFragment";
    private View rootview;
    private Switch LED1;
    private Switch LED2;
    private Switch DJ1;
    private Switch DJ2;
    private Switch MJ;
    private Switch DJ;
    private Switch FMQ;
    private Switch FACE;
    private EditText DJ1CS;
    private EditText DJ2CS;
    private EditText CS;
    String flag;
    int jieshouLED1 = 2;

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
        // Inflate the layout for this fragment
        if(rootview == null) {
            rootview = inflater.inflate(R.layout.contacts_layout, container, false);
        }
        LED1 = rootview.findViewById(R.id.LED1);
        LED2 = rootview.findViewById(R.id.LED2);
        DJ1 = rootview.findViewById(R.id.DJ1);
        DJ2 = rootview.findViewById(R.id.DJ2);
        DJ = rootview.findViewById(R.id.DJ);
        MJ = rootview.findViewById(R.id.MJ);
        FMQ = rootview.findViewById(R.id.FMQ);
        DJ1CS = rootview.findViewById(R.id.DJ1CS);
        DJ2CS = rootview.findViewById(R.id.DJ2CS);
        CS = rootview.findViewById(R.id.CS);
        FACE = rootview.findViewById(R.id.face);

        LED1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"LED1\":1}"));
                    jieshouLED1=0;
                }else{
                    //未选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"LED1\":0}"));
                    jieshouLED1=0;
                }
            }
        }); //电源
        LED2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"LED2\":1}"));
                }else {
                    //未选中状态 可以做一些操作

                    EventBus.getDefault().post(new eventbusfasong("{\"LED2\":0}"));
                }
            }
        }); //工作灯
        DJ1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String text = DJ1CS.getText().toString();
                if (isChecked){
                    //选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"MOT1\":" + text + "}"));
                }else {
                    //未选中状态 可以做一些操作

                    EventBus.getDefault().post(new eventbusfasong("{\"MOT1\":0}"));
                }
            }
        });  //浇水
        DJ2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String text = DJ2CS.getText().toString();
                if (isChecked){
                    //选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"MOT2\":" + text + "}"));
                }else {
                    //未选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"MOT2\":0}"));
                }
            }
        });  //大棚
        MJ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"LED3\":1}"));
                }else {
                    //未选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"LED3\":0}"));
                }
            }
        });   //门禁
        DJ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"LED4\":1}"));
                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 150);
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 4000);
                }else {
                    //未选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"LED4\":0}"));
                }
            }
        });   //火警
        FMQ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String text = CS.getText().toString();
                if (isChecked){
                    //选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"ALARM\":" + text + "}"));
                }else {
                    //未选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"ALARM\":0}"));
                }
            }
        });  //报警

        FACE.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"LED1\":1}"));
                }else{
                    //未选中状态 可以做一些操作
                    EventBus.getDefault().post(new eventbusfasong("{\"LED1\":0}"));
                }
            }
        }); //人脸识别
        return rootview;
    }
    @Subscribe(threadMode = ThreadMode.POSTING,sticky = true)
    public void onEvent(eventbus event) {
        String msg = event.getMsg();
        if(jieshouLED1==2) {
            try {
                JSONObject jsonObject = new JSONObject(msg);
                flag = jsonObject.optString("LED1");
                if (flag.equals("1"))  //接收信息,对电源开关自动置位
                {
                    LED1.setChecked(true);
                } else if (flag.equals("0")) {
                    LED1.setChecked(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(jieshouLED1<2){
            jieshouLED1++;
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);  //反注册EventBus
    }
}