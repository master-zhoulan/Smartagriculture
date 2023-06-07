package com.example.menu;

//import android.app.Fragment;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageFragment extends Fragment {
    private View rootview;
    private TextView wendu2;
    private TextView shidu2;
    private TextView guangdu2;
    private TextView huozai2;
    private TextView baojing2;
    private TextView menjin2;
    private
    int i =0;

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
        // Inflate the layout for this fragment
        if(rootview == null) {
            rootview = inflater.inflate(R.layout.message_layout, container, false);
        }
        wendu2 = rootview.findViewById(R.id.wendu2);
        shidu2 = rootview.findViewById(R.id.shidu2);
        guangdu2 = rootview.findViewById(R.id.guangdu2);
        huozai2 = rootview.findViewById(R.id.huozai2);
        baojing2 = rootview.findViewById(R.id.baojing2);
        menjin2 = rootview.findViewById(R.id.menjin2);
        return rootview;
    }

    @Subscribe(threadMode = ThreadMode.POSTING,sticky = true)
    public void onEvent(eventbus event) {
        String msg = event.getMsg();
        try {
            //json解析字符串
            JSONObject jsonObject = new JSONObject(msg);
            //通过 键 获取到 值
            double TEMP = jsonObject.optDouble("TEMP");
            double HUM = jsonObject.optDouble("HUM");
            double LIGHT = jsonObject.optDouble("LIGHT");
            String ALARM = jsonObject.optString("ALARM");
            double FLAME = jsonObject.optDouble("FLAME");
            String RC522 = jsonObject.optString("RC522");
            if (!ALARM.equals(""))  i++;
            wendu2.setText(TEMP + "℃");
            shidu2.setText(HUM + "RH");
            guangdu2.setText(LIGHT + "cd/m³");
            huozai2.setText(FLAME+"%");
            if(FLAME>80) {
                    ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 4000);
                    huozai2.setTextColor(Color.RED);
            }
            else if(FLAME<80){
                huozai2.setTextColor(Color.BLACK);
            }
            baojing2.setText(i+"次");
            if(RC522.equals("1"))
                menjin2.setText("认证成功");
            else if(RC522.equals("0"))
                menjin2.setText("无认证");
            else if(RC522.equals("-1"))
                menjin2.setText("认证失败");
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