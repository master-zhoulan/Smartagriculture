package com.example.menu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.menu.utils.MD5Utils;


public class RegisterActivity extends AppCompatActivity {
    //声明所有按钮
    private EditText et_username,et_pwd,et_pwd_sure;
    private Button register;
    private String userName,passWord,passWord_sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        init();
    }
    public void init(){
        //给按钮匹配id
        et_username = (EditText)findViewById(R.id.username);
        et_pwd = (EditText)findViewById(R.id.pwd);
        et_pwd_sure = (EditText)findViewById(R.id.pwd2);
        register = (Button)findViewById(R.id.registerBtn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditString();
                //提示输入信息
                if(TextUtils.isEmpty(userName)){
                    Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(passWord)){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(passWord_sure)){
                    Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!passWord.equals(passWord_sure)){
                    Toast.makeText(RegisterActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                    //判断用户名是否已被注册
                }else if(isExistUserName(userName)){
                    Toast.makeText(RegisterActivity.this, "此账户名已经存在", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //把保存账号密码
                    saveRegisterInfo(userName, passWord);
                    Intent data = new Intent();
                    data.putExtra("userName", userName);
                    setResult(RESULT_OK, data);
                    //跳转到登录界面中
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    RegisterActivity.this.finish();
                }
            }
        });
    }
    //获得已输入信息
    private void getEditString(){
        userName = et_username.getText().toString().trim();
        passWord = et_pwd.getText().toString().trim();
        passWord_sure = et_pwd_sure.getText().toString().trim();
    }
    //判断输入的用户名是否已经存在
    private boolean isExistUserName(String userName){
        boolean has_userName = false;
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        String spPsw = sp.getString(userName, "");
        //判断密码是否为空，不空则注册成功
        if(!TextUtils.isEmpty(spPsw)) {
            has_userName=true;
        }
        return has_userName;
    }
    //将用户名和密码保存到sp中
    private void saveRegisterInfo(String userName,String psw){
        String md5Psw = MD5Utils.md5(psw);//把密码用MD5加密
        SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(userName, md5Psw);
        editor.commit();
    }
    //跳转回登录界面
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        RegisterActivity.this.finish();
    }
}