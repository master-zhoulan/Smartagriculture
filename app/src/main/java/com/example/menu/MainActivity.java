package com.example.menu;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;
import androidx.fragment.app.Fragment;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 项目的主Activity，所有的Fragment都嵌入在这里。
 *
 * @author guolin
 */
//116.62.156.155  test test   /my/devpub  /my/devsub
//192.168.137.1  admin  520184zja  testtopic second
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //访问的地址，这里填写mqtt代理服务器ip
    private String host = "tcp://116.62.156.155";
    //登录账号
    private String userName = "test";
    //登录密码
    private String passWord = "tes";
    //这个是mqtt_id  唯一id，每个客户端应该生成不同的唯一id，这里测试演示数据，我就写固定的id，打包到不同的手机上安装，要手动修改或修改成动态id，获取随机数+时间方式生成一个随机数作为唯一id
    private String mqtt_id = "12";

    private static final String TAG = "MainActivity";
//    private String host = "tcp://192.168.137.1";
//    //登录账号
//    private String userName = "admin";
//    //登录密码
//    private String passWord = "520184zja";
//    //这个是mqtt_id  唯一id，每个客户端应该生成不同的唯一id，这里测试演示数据，我就写固定的id，打包到不同的手机上安装，要手动修改或修改成动态id，获取随机数+时间方式生成一个随机数作为唯一id
//    private String mqtt_id = "12";


    private int i = 1;

    private Handler handler;

    private MqttClient client;

    private String mqtt_sub_topic = "/my/devpub"; //订阅主题(消息接收方)

    private String mqtt_pub_topic = "/my/devsub";//发布主题（消息发送方）

//    private String mqtt_sub_topic = "testtopic"; //订阅主题(消息接收方)
//
//    private String mqtt_pub_topic = "second";//发布主题（消息发送方）

    private MqttConnectOptions options;

    private ImageView circle;
    private ImageView circle2;
    private TextView shebei;

    private ScheduledExecutorService scheduler;
    /**
     * 用于展示消息的Fragment
     */
    private MessageFragment messageFragment;

    /**
     * 用于展示联系人的Fragment
     */
    private ContactsFragment contactsFragment;

    /**
     * 用于展示动态的Fragment
     */
    private NewsFragment newsFragment;

    /**
     * 用于展示设置的Fragment
     */
    private SettingFragment settingFragment;

    /**
     * 消息界面布局
     */
    private View messageLayout;

    /**
     * 联系人界面布局
     */
    private View contactsLayout;

    /**
     * 动态界面布局
     */
    private View newsLayout;

    /**
     * 设置界面布局
     */
    private View settingLayout;

    /**
     * 在Tab布局上显示消息图标的控件
     */
    private ImageView messageImage;

    /**
     * 在Tab布局上显示联系人图标的控件
     */
    private ImageView contactsImage;

    /**
     * 在Tab布局上显示动态图标的控件
     */
    private ImageView newsImage;

    /**
     * 在Tab布局上显示设置图标的控件
     */
    private ImageView settingImage;

    /**
     * 在Tab布局上显示消息标题的控件
     */
    private TextView messageText;

    /**
     * 在Tab布局上显示联系人标题的控件
     */
    private TextView contactsText;

    /**
     * 在Tab布局上显示动态标题的控件
     */
    private TextView newsText;

    /**
     * 在Tab布局上显示设置标题的控件
     */
    private TextView settingText;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    ViewPager2 viewPager2;
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);  //注册
        Log.d(TAG, "This is onStart");
    }
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "This is onCreate");
        // 初始化布局元素
        messageLayout = findViewById(R.id.message_layout);
        contactsLayout = findViewById(R.id.contacts_layout);
        newsLayout = findViewById(R.id.news_layout);
        settingLayout = findViewById(R.id.setting_layout);
        messageImage = (ImageView) findViewById(R.id.message_image);
        contactsImage = (ImageView) findViewById(R.id.contacts_image);
        newsImage = (ImageView) findViewById(R.id.news_image);
        settingImage = (ImageView) findViewById(R.id.setting_image);
        messageText = (TextView) findViewById(R.id.message_text);
        contactsText = (TextView) findViewById(R.id.contacts_text);
        newsText = (TextView) findViewById(R.id.news_text);
        settingText = (TextView) findViewById(R.id.setting_text);
        circle = (ImageView) findViewById(R.id.centerImage);
        circle2 = (ImageView) findViewById(R.id.centerImage2);
        shebei = (TextView) findViewById(R.id.shebei);

        messageLayout.setOnClickListener(this);
        contactsLayout.setOnClickListener(this);
        newsLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        circle2.setVisibility(View.GONE);
        initViews();
        // 第一次启动时选中第0个tab
        setTabSelection(2);
        setTabSelection(1);
        setTabSelection(0);

    }
    //设备5秒未连接,会显示未连接
    CountDownTimer timer = new CountDownTimer(5000, 1000) {
        public void onTick(long millisUntilFinished) {
            // 每隔 1000 毫秒执行一次
            // 例如，更新文本视图的文本
        }

        public void onFinish() {
            // 倒计时完成时执行的代码
            circle.setVisibility(View.VISIBLE);
            circle2.setVisibility(View.GONE);
            shebei.setText("设备未连接");
            //rippleBackground.stopRippleAnimation();
        }
    };

    protected void onPause() {
        super.onPause();
        Log.d(TAG, "This is onPause");
    }
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Log.d(TAG, "This is onUserLeaveHint");
    }

    /**
     * 在这里获取到每个需要用到的控件的实例，并给它们设置好必要的点击事件。
     */
    private void initViews() {
        Log.d(TAG, "This is initViews");
        init();
        startReconnect();

        HandlerThread handlerThread = new HandlerThread("MyHandlerThread");
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper()) {

            @SuppressLint("SetTextIl8n")
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1: //开机校验更新回传
                        break;
                    case 2: //反馈回转
                        break;
                    case 3: //MQTT收到消息回传
                        runOnUiThread(new Runnable()//不允许其他线程直接操作组件，用提供的此方法可以
                        {
                            public void run() {
                                // TODO Auto-generated method stub
                                String s = msg.obj.toString();
                                try {
                                    //json解析字符串
                                    JSONObject jsonObject = new JSONObject(s);
                                    //通过eventbus传给fragment
                                    EventBus.getDefault().postSticky(new eventbus(jsonObject.toString()));
                                    String flag = jsonObject.optString("START");
                                    //final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content1);
                                    //动画
                                    if(flag.equals("OK") || jsonObject != null) {
                                        //circle.setImageResource(R.drawable.circle_green);
                                        circle.setVisibility(View.GONE);
                                        circle2.setVisibility(View.VISIBLE);
                                        //rippleBackground.startRippleAnimation();
                                        shebei.setText("设备已连接");
                                        timer.start();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;
                    case 30: //连接失败
                        Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                        break;
                    case 31: //连接成功
                        Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                        try {
                            client.subscribe(mqtt_sub_topic, 2);
                            Toast.makeText(MainActivity.this, "订阅成功", Toast.LENGTH_SHORT).show();
                        } catch (MqttException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "订阅失败", Toast.LENGTH_SHORT).show();
                        }
//                        publishmessageplus(mqtt_pub_topic,"第一个客户端发送的信息");
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "This is onClick");
        switch (v.getId()) {
            case R.id.message_layout:
                // 当点击了消息tab时，选中第1个tab
                setTabSelection(0);
                break;
            case R.id.contacts_layout:
                // 当点击了联系人tab时，选中第2个tab
                setTabSelection(1);
                break;
            case R.id.news_layout:
                // 当点击了动态tab时，选中第3个tab
                setTabSelection(2);
                break;
            case R.id.setting_layout:
                // 当点击了设置tab时，选中第4个tab
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示消息，1表示联系人，2表示动态，3表示设置。
     */
    private void setTabSelection(int index) {
        Log.d(TAG, "This is setTabSelection");
        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                messageImage.setImageResource(R.drawable.buttom_selected_one);
                messageText.setTextColor(Color.GREEN);
                if (messageFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    messageFragment = new MessageFragment();
                    transaction.add(R.id.content, messageFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(messageFragment);
                }
                break;
            case 1:
                // 当点击了联系人tab时，改变控件的图片和文字颜色
                contactsImage.setImageResource(R.drawable.buttom_selected_two);
                contactsText.setTextColor(Color.GREEN);
                if (contactsFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    contactsFragment = new ContactsFragment();
                    transaction.add(R.id.content, contactsFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(contactsFragment);
                }
                break;
            case 2:
                // 当点击了动态tab时，改变控件的图片和文字颜色
                newsImage.setImageResource(R.drawable.buttom_selected_three);
                newsText.setTextColor(Color.GREEN);
                if (newsFragment == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    newsFragment = new NewsFragment();
                    transaction.add(R.id.content, newsFragment);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(newsFragment);
                }
                break;
            case 3:
            default:
                // 当点击了设置tab时，改变控件的图片和文字颜色
                settingImage.setImageResource(R.drawable.buttom_selected_four);
                settingText.setTextColor(Color.GREEN);
                if (settingFragment == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.content, settingFragment);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        Log.d(TAG, "This is clearSelection");
        messageImage.setImageResource(R.drawable.buttom_unselected_one);
        messageText.setTextColor(Color.parseColor("#82858b"));
        contactsImage.setImageResource(R.drawable.buttom_unselected_two);
        contactsText.setTextColor(Color.parseColor("#82858b"));
        newsImage.setImageResource(R.drawable.buttom_unselected_three);
        newsText.setTextColor(Color.parseColor("#82858b"));
        settingImage.setImageResource(R.drawable.buttom_unselected_four);
        settingText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        Log.d(TAG, "This is hideFragments");
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (contactsFragment != null) {
            transaction.hide(contactsFragment);
        }
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
    }
    private void init() {
        Log.d(TAG, "This is init()");
        try {

            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存

            client = new MqttClient(host, mqtt_id,

                    new MemoryPersistence());

            //MQTT的连接设置

            options = new MqttConnectOptions();

            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接

            options.setCleanSession(true);

            //设置连接的用户名

            options.setUserName(userName);

            //设置连接的密码

            options.setPassword(passWord.toCharArray());

            // 设置超时时间 单位为秒

            options.setConnectionTimeout(10);

            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制

            options.setKeepAliveInterval(20);

            //设置回调

            client.setCallback(new MqttCallback() {

                @Override

                public void connectionLost(Throwable cause) {

                    //连接丢失后，一般在这里面进行重连

                    System.out.println("connectionLost----------");

                }

                @Override

                public void deliveryComplete(IMqttDeliveryToken token) {

                    //publish后会执行到这里

                    System.out.println("deliveryComplete---------"

                            + token.isComplete());

                }

                @Override

                public void messageArrived(String topicName, MqttMessage message)

                        throws Exception {

                    //subscribe后得到的消息会执行到这里面

                    System.out.println("messageArrived----------");
                    timer.cancel();
                    Message msg = new Message();
                    msg.what = 3;
                    //重新编码，解决乱码问题
                    String strMsg = new String(message.getPayload(), "UTF-8");
                    System.out.println(strMsg);
                    msg.obj = strMsg;
                    handler.sendMessage(msg);
                }
            });

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    /**
     * 连接
     */
    private void Mqtt_connect() {
        Log.d(TAG, "This is Mqtt_connect");
        new Thread(new Runnable() {

            @Override

            public void run() {

                try {

                    if (!(client.isConnected())) {

                        client.connect(options);

                        Message msg = new Message();

                        msg.what = 31;

                        handler.sendMessage(msg);

                    }


                } catch (Exception e) {

                    e.printStackTrace();

                    Message msg = new Message();

                    msg.what = 30;

                    handler.sendMessage(msg);

                }

            }

        }).start();

    }

    /**
     * 线程池执行任务，心跳包，当断开连接后，执行自动连接代理服务器
     */
    private void startReconnect() {
        Log.d(TAG, "This is startReconnect");
        scheduler = Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override

            public void run() {
                if (!client.isConnected()) {
                    Mqtt_connect();

                }

            }

        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);

    }

    /**
     * 发布订阅
     *
     * @param topic
     * @param message2
     */
    private void publishmessageplus(String topic, String message2) {
        Log.d(TAG, "This is publishmessageplus");
        if (client == null || !client.isConnected()) {
            return;
        }

        MqttMessage message = new MqttMessage();
        message.setPayload(message2.getBytes());

        try {
            client.publish(topic, message);
        } catch (MqttException e) {

            e.printStackTrace();
        }
    }


    //订阅从fragment传来的消息
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(eventbusfasong event) throws UnsupportedEncodingException {
        Log.d(TAG, "This is onEvent");
        String emsg = event.getMsg();
        String strMsg = new String(emsg.getBytes(),"UTF-8");
        //Toast.makeText(MainActivity.this, strMsg+"发送", Toast.LENGTH_LONG).show();
        publishmessageplus(mqtt_pub_topic,strMsg);
    }

    //反注册EventBus
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "This is onDestroy");
        EventBus.getDefault().unregister(this);
    }
}