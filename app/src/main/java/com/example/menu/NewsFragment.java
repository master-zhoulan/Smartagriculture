package com.example.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.menu.CurveView;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewsFragment extends Fragment {

    private View rootview;

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
        if (rootview == null) {
            rootview = inflater.inflate(R.layout.news_layout, container, false);
        }
        initLine1();
        initLine2();
        initLine3();
        return rootview;
    }

/*    private void updateTime() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //在这里更新数据

            }
        }, 0, 500); //延迟500毫秒后，执行一次task
    }*/
    private BaseLinerView baseLinerView;
    private BaseLinerView2 baseLinerView2;
    private BaseLinerView3 baseLinerView3;

    private void initLine1() {
        baseLinerView = new BaseLinerView() {
            @Override
            public String getLineName() {
                return "温度";
            }
        };
        baseLinerView.mView = rootview.findViewById(R.id.liner);
        baseLinerView.lineColor = "#FF6655";
        baseLinerView.lineColor2 = "#FF6655";
        baseLinerView.zeroLine = 0;
        baseLinerView.minValue = 0;
        baseLinerView.maxValue = 50;
        baseLinerView.initView();
    }
    private void initLine2() {
        baseLinerView2 = new BaseLinerView2() {
            @Override
            public String getLineName() {
                return "湿度";
            }
        };
        baseLinerView2.mView = rootview.findViewById(R.id.liner2);
        baseLinerView2.lineColor = "#FFFFEB3B";
        baseLinerView2.lineColor2 = "#FFFFEB3B";
        baseLinerView2.zeroLine = 0;
        baseLinerView2.minValue = 0;
        baseLinerView2.maxValue = 100;
        baseLinerView2.initView();
    }
    private void initLine3() {
        baseLinerView3 = new BaseLinerView3() {
            @Override
            public String getLineName() {
                return "光度";
            }
        };
        baseLinerView3.mView = rootview.findViewById(R.id.liner3);
        baseLinerView3.lineColor = "#FF3B7CFF";
        baseLinerView3.lineColor2 = "#FF3B7CFF";
        baseLinerView3.zeroLine = 0;
        baseLinerView3.minValue = 50;
        baseLinerView3.maxValue = 300;
        baseLinerView3.initView();
    }


    @Subscribe(threadMode = ThreadMode.MAIN,priority = 0,sticky = true)
    public void onEvent(eventbus event) {
        String msg = event.getMsg();
        try {
            //json解析字符串
            JSONObject jsonObject = new JSONObject(msg);

            //通过 键 获取到 值
            Double TEMP = jsonObject.optDouble("TEMP");
            Double HUM = jsonObject.optDouble("HUM");
            Double LIGHT = jsonObject.optDouble("LIGHT");
            baseLinerView.onUdata(TEMP);
            baseLinerView2.onUdata(HUM);
            baseLinerView3.onUdata(LIGHT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //private Timer timer=new Timer();

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);  //反注册EventBus
        //timer.cancel();//取消任务
    }
}