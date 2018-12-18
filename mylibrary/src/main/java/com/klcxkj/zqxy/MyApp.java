package com.klcxkj.zqxy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.klcxkj.zqxy.common.Common;

/**
 * autor:OFFICE-ADMIN
 * time:2018/2/5
 * email:yinjuan@klcxkj.com
 * description:
 */

public class MyApp {

    public static String versionCode ="V4.1.4";
    public static int checkCode =-11; //租赁设备判断
    public static String washingDecivename ="洗衣机";

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext().getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String verCode =localVersion.substring(1,localVersion.length());
        return verCode;
    }

    public static void clearData(Context context){
        SharedPreferences sp =sp = context.getSharedPreferences("adminInfo", Context.MODE_PRIVATE);;
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(Common.USER_PHONE_NUM);
        editor.remove(Common.USER_INFO);
        editor.remove(Common.ACCOUNT_IS_USER);
        editor.remove(Common.USER_BRATHE+Common.getUserPhone(sp));
        editor.remove(Common.USER_WASHING+Common.getUserPhone(sp));
        editor.commit();
    }
}
