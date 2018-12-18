package com.klcxkj.zqxy.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.klcxkj.mylibrary.R;
import com.klcxkj.zqxy.MyApp;
import com.klcxkj.zqxy.common.Common;
import com.klcxkj.zqxy.databean.ResultMsg;
import com.klcxkj.zqxy.databean.UserInfo;
import com.klcxkj.zqxy.response.PublicGetData;
import com.klcxkj.zqxy.utils.GlobalTools;

import net.android.tools.afinal.FinalHttp;
import net.android.tools.afinal.http.AjaxCallBack;
import net.android.tools.afinal.http.AjaxParams;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KLRefundActivity extends BaseActivity {

    private TextView tv_all;
    private EditText et_refund;
    private Button btn_back;
    private Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klrefund);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.base_color),0);
        showMenu("退款");
        initView();
        initData();
        initClick();
    }

    private void initData() {
        sp = getSharedPreferences("adminInfo", Context.MODE_PRIVATE);
        mUserInfo = Common.getUserInfo(sp);
        tv_all.setText(Common.getShowMonty(mUserInfo.AccMoney, ""));
        et_refund.setText(Common.getShowMonty(mUserInfo.AccMoney, ""));
    }

    private void initView() {
        tv_all =findViewById(R.id.et_refund_money_all);
        et_refund =findViewById(R.id.et_refund_money);
        btn_back =findViewById(R.id.refund_btn_back);
        btn_submit =findViewById(R.id.refund_btn);
        et_refund.requestFocus();
    }
    private void initClick() {

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string =et_refund.getText().toString();
                if (string !=null && string.length()>0){
                    submitRefund(string);
                }


            }
        });

        et_refund.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {


                if (source.equals(".") && dest.toString().length()==0){
                    return "0.";
                }
                if (dest.toString().contains(".")){
                    int index =dest.toString().indexOf(".");
                    int len =dest.toString().substring(index).length();
                    if (len>2){
                        return "";
                    }
                }
                return null;
            }
        }});
    }


    private void submitRefund(String money) {
        loadingDialogProgress = GlobalTools.getInstance().showDailog(this, "提交..");
        OkHttpClient client =new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody=new FormBody.Builder()
                .add("PrjID",""+mUserInfo.PrjID)
                .add("AccID",""+mUserInfo.AccID)
                .add("RechargeWay","100")
                .add("account",Common.CARD_NO) //卡号
                .add("tranamt",money)
                .add("loginCode",mUserInfo.TelPhone+","+mUserInfo.loginCode)
                .add("phoneSystem", "Android")
                .add("version", MyApp.versionCode)
                .build();
        Request request =new Request.Builder()
                .url(Common.BASE_URL + "xzx_Refund")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (loadingDialogProgress !=null){
                    loadingDialogProgress.dismiss();
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (loadingDialogProgress !=null){
                    loadingDialogProgress.dismiss();
                }
                String result =response.body().string();

                if (GlobalTools.isJson(result)){
                    final ResultMsg resultMsg =new Gson().fromJson(result,ResultMsg.class);
                    if (resultMsg.getError_code().equals("0")){
                        //更新用户
                        updateUserInfo(mUserInfo);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast(resultMsg.getMsg());
                        }
                    });
                }
            }
        });
    }

    private void updateUserInfo(final UserInfo mInfo) {

        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("TelPhone", mInfo.TelPhone + "");
        ajaxParams.put("PrjID", mInfo.PrjID + "");
        ajaxParams.put("WXID", "0");
        ajaxParams.put("loginCode", mInfo.TelPhone + "," + mInfo.loginCode);
        ajaxParams.put("phoneSystem", "Android");
        ajaxParams.put("version", MyApp.versionCode);
        ajaxParams.put("isOpUser","0");

        new FinalHttp().get(Common.BASE_URL + "accountInfo", ajaxParams,
                new AjaxCallBack<Object>() {

                    @Override
                    public void onSuccess(Object t) {
                        super.onSuccess(t);
                        String result = t.toString();
                        PublicGetData publicGetData = new Gson().fromJson(result, PublicGetData.class);
                        if (publicGetData.error_code.equals("0")) {

                            UserInfo info = new Gson().fromJson(publicGetData.data, UserInfo.class);
                            info.loginCode = mInfo.loginCode;
                            if (info != null) {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(Common.USER_PHONE_NUM, info.TelPhone + "");
                                editor.putString(Common.USER_INFO, new Gson().toJson(info));
                                editor.putInt(Common.ACCOUNT_IS_USER, info.GroupID);
                                editor.commit();

                                finish();
                            }

                        }

                    }

                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }
                });

    }


}
