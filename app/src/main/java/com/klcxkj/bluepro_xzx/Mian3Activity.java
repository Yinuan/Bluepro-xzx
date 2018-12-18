package com.klcxkj.bluepro_xzx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.klcxkj.zqxy.MyApp;
import com.klcxkj.zqxy.common.Common;
import com.klcxkj.zqxy.databean.UserInfo;
import com.klcxkj.zqxy.response.PublicGetData;
import com.klcxkj.zqxy.ui.MainAdminActivity;

import net.android.tools.afinal.FinalHttp;
import net.android.tools.afinal.http.AjaxCallBack;
import net.android.tools.afinal.http.AjaxParams;

public class Mian3Activity extends AppCompatActivity {

    private   SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian3);
         sp = getSharedPreferences("adminInfo", Context.MODE_PRIVATE);
        //版本代码号说明
        MyApp.versionCode= MyApp.getLocalVersionName(Mian3Activity.this);
        // 服务器路径设置
        Common.BASE_URL="http://47.96.91.227/appI/api/";//47.96.91.227
        //106.75.164.143:8085
        findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login("18565651402");
            }
        });

        findViewById(R.id.login_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.setClass(Mian3Activity.this, MainAdminActivity.class);
                intent.putExtra("tellPhone","13771820429");
                intent.putExtra("PrjID","0");
                intent.putExtra("app_url","http://47.96.91.227/appI/api/");//106.75.164.143
                startActivity(intent);
            }
        });
    }

    protected void login(final String phonenum) {
        if (!Common.isNetWorkConnected(Mian3Activity.this)) {
            return;
        }

            AjaxParams ajaxParams = new AjaxParams();
            ajaxParams.put("TelPhone", phonenum);
            ajaxParams.put("PrjID", "0");
            ajaxParams.put("Code","0");//
            ajaxParams.put("isOpUser","0");
            ajaxParams.put("phoneSystem", "Android");
            ajaxParams.put("version", MyApp.versionCode);

        Log.d("Mian3Activity", "ajaxParams:" + ajaxParams);
            new FinalHttp().get( Common.BASE_URL+"login2", ajaxParams,
                    new AjaxCallBack<Object>() {

                        @Override
                        public void onSuccess(Object t) {
                            super.onSuccess(t);
                            String result = t.toString();
                            Log.e("Mian3Activity", "login result = " + result);
                            PublicGetData publicGetData = new Gson().fromJson(result, PublicGetData.class);
                            if (publicGetData.error_code.equals("0") || publicGetData.error_code.equals("5")) {

                                UserInfo userInfo = new Gson().fromJson(publicGetData.data, UserInfo.class);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(Common.USER_PHONE_NUM, phonenum);
                                if (userInfo == null) {
                                    editor.putInt(Common.ACCOUNT_IS_USER, 2);//设定为学生用户
                                    editor.putString(Common.USER_INFO, "");
                                } else {

                                    userInfo.TelPhone = Long.valueOf(phonenum);
                                    if (userInfo.GroupID == 1) {
                                        editor.putInt(Common.ACCOUNT_IS_USER, 1);

                                    } else {
                                        userInfo.GroupID = 2;
                                        editor.putInt(Common.ACCOUNT_IS_USER, 2);

                                    }
                                }
                                editor.putString(Common.USER_INFO, new Gson().toJson(userInfo));
                                editor.commit();
                                startActivity(new Intent(Mian3Activity.this,Main4Activity.class));
                            }

                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                            Log.d("Mian3Activity", strMsg);

                        }
                    });



    }
}
