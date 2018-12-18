package com.klcxkj.bluepro_xzx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.klcxkj.zqxy.MyApp;
import com.klcxkj.zqxy.ui.MainAdminActivity;
import com.klcxkj.zqxy.ui.MainUserActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setOnclick();
    }
    private void setOnclick() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setClass(MainActivity.this, MainUserActivity.class);
                intent.putExtra("tellPhone","321123");//学工号321123
                intent.putExtra("PrjID","9999998");//9999998
                intent.putExtra("accId","17897");//一卡通卡号，充值用到的//17897
                intent.putExtra("businesses_id","1000060");
                intent.putExtra("app_url","http://klcxkj-qzxy.cn:8087/appI/api/");//http://klcxkj-qzxy.cn:8087/appI/api/
                intent.putExtra("pay_url","http://219.143.144.153:9001/Order/CreateOrder");
                startActivity(intent);

            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setClass(MainActivity.this, MainAdminActivity.class);
                intent.putExtra("tellPhone","321123");
                intent.putExtra("PrjID","0");
                intent.putExtra("app_url","http://klcxkj-qzxy.cn:8087/appI/api/");//106.75.164.143
                startActivity(intent);
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除缓存数据
                MyApp.clearData(MainActivity.this);
            }
        });
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Mian3Activity.class));
            }
        });
    }


}
