package com.klcxkj.zqxy.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.klcxkj.mylibrary.R;
import com.klcxkj.zqxy.MyApp;
import com.klcxkj.zqxy.adapter.ReAdapter;
import com.klcxkj.zqxy.common.Common;
import com.klcxkj.zqxy.databean.DeciveType;
import com.klcxkj.zqxy.databean.KLRechargeBean;
import com.klcxkj.zqxy.databean.UserInfo;
import com.klcxkj.zqxy.response.PublicGetData;
import com.klcxkj.zqxy.response.PublicGivenMoneyData;
import com.klcxkj.zqxy.response.RechargeEntity;
import com.klcxkj.zqxy.utils.GlobalTools;
import com.klcxkj.zqxy.widget.Effectstype;
import com.klcxkj.zqxy.widget.MyGridView;

import net.android.tools.afinal.FinalHttp;
import net.android.tools.afinal.http.AjaxCallBack;
import net.android.tools.afinal.http.AjaxParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.klcxkj.mylibrary.R.id.package_grid;
import static com.klcxkj.mylibrary.R.id.recharge_btn;
import static com.klcxkj.zqxy.fragment.WalletFragment.showInputMethod;

public class KLWalletActivity extends BaseActivity {

    private TextView giveMoney;
    private TextView reMoney; //

    private WebView webView;
    private LinearLayout linearLayout;
    private TextView package_item_tips;
    private MyGridView myGridView;
    private ReAdapter reAdapter;
    private Button button;

    private SharedPreferences sp;
    private UserInfo mUserInfo;
    private List<DeciveType> mList ;

    private int rechargeMoney; //充值金额


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klwallet);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.base_color),0);
        initView();
        initData();
        initClick();
    }


    private void initData() {
        reAdapter =new ReAdapter(this);
        myGridView.setAdapter(reAdapter);
        //获取项目充值金额
        sp = getSharedPreferences("adminInfo", Context.MODE_PRIVATE);
        mUserInfo =Common.getUserInfo(sp);
        if (mUserInfo.PrjID!=0){
            reMoney.setText(Common.getShowMonty(mUserInfo.AccMoney, ""));
            giveMoney.setText(Common.getShowMonty(mUserInfo.GivenAccMoney, ""));
            mList =new ArrayList<>();
             getMonneyByPro();
        }else {
            //绑定设备上的项目继续使用
            showBindDialog();
        }
    }

    private void initView() {
        showMenu2("充值");
        giveMoney =findViewById(R.id.give_account_txt);
        reMoney =findViewById(R.id.cash_account_txt);
        myGridView =findViewById(package_grid);
        button =findViewById(recharge_btn);
        package_item_tips =findViewById(R.id.package_item_tips);
        linearLayout =findViewById(R.id.layout_root_1);
        webView=findViewById(R.id.web_view_root);
        webView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);

        webView.getSettings().setJavaScriptEnabled(true);

        setTxtValue(0.00f);


    }

    private void setTxtValue(float value){
        String str = "其他 ￥<font color='#18a4ec'>" + value+"</big></font>";
        package_item_tips.setText(Html.fromHtml(str));
        package_item_tips.setVisibility(View.VISIBLE);

        package_item_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRechargeAccount();
            }
        });
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==event.KEYCODE_BACK && webView.getVisibility()==View.VISIBLE){
            webView.setVisibility(View.GONE);
            updateUserInfo(mUserInfo);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
        updateUserInfo(mUserInfo);
    }

    private void initClick() {
        //返回按键
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (webView.getVisibility()==View.VISIBLE){
                    webView.setVisibility(View.GONE);
                    updateUserInfo(mUserInfo);
                }else {
                    finish();
                }*/
                finish();
            }
        });
        //充值
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if (rechargeMoney !=0){
                 submitRecharge(rechargeMoney+"");
                 //submitRecharge(1+"");
                }else {
                 //请选择充值金额
                 toast("请选择充值金额!");
             }
            }
        });
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //1 ,s数据
                if (position==mList.size()-1){
                    //弹出输入框
                    showRechargeAccount();
                }else {
                    //根据充值金额获取赠送金额
                    String value =mList.get(position).getDevname();
                    rechargeMoney =Integer.parseInt(value);
                    //getZengsong(value);
                }
                //2 显示逻辑
                for (int i = 0; i <mList.size() ; i++) {
                    DeciveType type =mList.get(i);
                    if (type.getTypeid()==1){
                        type.setTypeid(0);
                    }
                }
                DeciveType type =mList.get(position);
                type.setTypeid(1);
                reAdapter.setList(mList);
            }
        });
    }

    /**
     * 根据项目获取充值金额
     */
    private void getMonneyByPro() {
        loadingDialogProgress = GlobalTools.getInstance().showDailog(this, "加载..");
        AjaxParams params = new AjaxParams();
        params.put("PrjID", ""+mUserInfo.PrjID );//
        params.put("loginCode",mUserInfo.TelPhone + "," + mUserInfo.loginCode);//
        params.put("phoneSystem", "Android");
        params.put("version", MyApp.versionCode);
        new FinalHttp().get(Common.BASE_URL + "czquery", params, new AjaxCallBack<Object>() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                if (loadingDialogProgress != null) {
                    loadingDialogProgress.dismiss();
                }
                if (!GlobalTools.isJson(result.toString())){
                    return;
                }

                KLRechargeBean rebean = new Gson().fromJson(result.toString(), KLRechargeBean.class);

                if (rebean.getError_code().equals("0")){
                    String mValue ="";
                    if (rebean.getData() !=null && !rebean.getData().isEmpty()) {
                        for (int i = 0; i <rebean.getData().size() ; i++) {
                            DeciveType type =new DeciveType();
                            if (i==0){
                                type.setTypeid(1);
                                mValue =rebean.getData().get(i).getCzvalue();
                                rechargeMoney =Integer.parseInt(mValue);
                            }else {
                                type.setTypeid(0);
                            }
                            type.setDevname(rebean.getData().get(i).getCzvalue());
                            mList.add(type);
                        }
                        mList.add(new DeciveType(0,"其他"));
                        reAdapter.setList(mList);
                        reAdapter.notifyDataSetChanged();
                        //获取第一个默认充值
                     //   getZengsong(mValue);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                if (loadingDialogProgress != null) {
                    loadingDialogProgress.dismiss();
                }
            }
        });
    }
    private void getZengsong(final String account) {
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("PrjID", mUserInfo.PrjID + "");
        ajaxParams.put("SaveMoney", account);
        ajaxParams.put("loginCode", mUserInfo.TelPhone + "," + mUserInfo.loginCode);
        ajaxParams.put("phoneSystem", "Android");
        ajaxParams.put("version", MyApp.versionCode);
        new FinalHttp().get(Common.BASE_URL + "getGivenMoney", ajaxParams,
                new AjaxCallBack<Object>() {
                    @Override
                    public void onSuccess(Object t) {
                        super.onSuccess(t);
                        String result = t.toString();

                        PublicGivenMoneyData publicGivenMoneyData = new Gson().fromJson(result, PublicGivenMoneyData.class);
                        if (publicGivenMoneyData.error_code.equals("0")) {
                            if (publicGivenMoneyData.GivenMoney >=0) {
                                String str = "活动说明：充 <font color='#FF0000'>"
                                        + account + "元</big></font> 送 <font color='#FF0000'>"
                                        + publicGivenMoneyData.GivenMoney + "元</big></font>";
                                package_item_tips.setText(Html.fromHtml(str));
                                package_item_tips.setVisibility(View.VISIBLE);
                            } else {
                                package_item_tips.setVisibility(View.GONE);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Throwable t, int errorNo, String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                    }
                });
    }

    private void submitRecharge(String money){
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
                .add("sno",""+mUserInfo.AccID)
                .add("tranamt",money)
                .add("toaccount",Common.BUSINESSES_ID)
                .add("loginCode",mUserInfo.TelPhone+","+mUserInfo.loginCode)
                .add("phoneSystem", "Android")
                .add("version", MyApp.versionCode)
                .build();
        Request request =new Request.Builder()
                .url(Common.BASE_URL + "xzx_Recharge")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String str =response.body().string();
                if (GlobalTools.isJson(str)){
                    RechargeEntity rechargeData =new Gson().fromJson(str,RechargeEntity.class);
                    if (rechargeData !=null){
                        //创建订单
                        Intent intent=new Intent(KLWalletActivity.this,KLH5Activity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("rechargeData",rechargeData);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
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
                            PublicGetData publicGetData = new Gson().fromJson(
                                    result, PublicGetData.class);
                            if (publicGetData.error_code.equals("0")) {

                                UserInfo info = new Gson().fromJson(publicGetData.data, UserInfo.class);
                                info.loginCode = mInfo.loginCode;
                                if (info != null) {
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString(Common.USER_PHONE_NUM, info.TelPhone + "");
                                    editor.putString(Common.USER_INFO, new Gson().toJson(info));
                                    editor.putInt(Common.ACCOUNT_IS_USER, info.GroupID);
                                    editor.commit();

                                    reMoney.setText(Common.getShowMonty(info.AccMoney, ""));

                                }

                            }

                        }

                        @Override
                        public void onFailure(Throwable t, int errorNo, String strMsg) {
                            super.onFailure(t, errorNo, strMsg);
                        }
                    });

        }



    private void showBindDialog() {
        dialogBuilder.withTitle(getString(R.string.tips))
                .withMessage(getString(R.string.bind_tips2))
                .withEffect(Effectstype.Fadein).isCancelable(false)
                .withButton1Text(getString(R.string.cancel))
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();

                    }
                }).withButton2Text(getString(R.string.sure))
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(KLWalletActivity.this, SearchBratheDeviceActivity.class);
                        intent.putExtra("capture_type", 4);
                        startActivity(intent);

                    }
                }).show();
    }

    public void showRechargeAccount() {

        Dialog dialog = new Dialog(KLWalletActivity.this, R.style.dialog_untran);
        // 通过布局填充器获login_layout
        View view = getLayoutInflater().inflate(R.layout.recharge_account_layout, null);
        // 获取两个文本编辑框（密码这里不做登陆实现，仅演示）
        final EditText account_edit = (EditText) view.findViewById(R.id.account_edit);

        TextView cancel_txt = (TextView) view.findViewById(R.id.cancel_txt);
        TextView sure_txt = (TextView) view.findViewById(R.id.sure_txt);

        dialog.setContentView(view);
        dialog.show();

        account_edit.requestFocus();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                showInputMethod(account_edit, KLWalletActivity.this);
            }
        }, 100);

        cancel_txt.setTag(dialog);
        sure_txt.setTag(dialog);
        cancel_txt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Dialog mAlertDialog = (Dialog) v.getTag();
                if (mAlertDialog != null) {
                    button.setEnabled(true);
                    mAlertDialog.dismiss();
                }

            }
        });

        sure_txt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Dialog mAlertDialog = (Dialog) v.getTag();
                if (mAlertDialog != null) {
                    mAlertDialog.dismiss();

                    String accountString = account_edit.getText().toString();
                    try {
                        int account = Integer.valueOf(accountString);
                        rechargeMoney =account;
                        //getZengsong(account+"");
                        setTxtValue(rechargeMoney);

                    } catch (Exception e) {
                        Common.showToast(KLWalletActivity.this, R.string.insert_recharge_tip2, Gravity.CENTER);
                    }

                }

            }
        });

    }
}
