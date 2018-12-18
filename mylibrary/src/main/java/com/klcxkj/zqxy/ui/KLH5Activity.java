package com.klcxkj.zqxy.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jaeger.library.StatusBarUtil;
import com.klcxkj.mylibrary.R;
import com.klcxkj.zqxy.common.Common;
import com.klcxkj.zqxy.response.RechargeEntity;
import com.klcxkj.zqxy.utils.GlobalTools;
import com.klcxkj.zqxy.widget.LoadingDialogProgress;

public class KLH5Activity extends AppCompatActivity {

    private WebView webView;
    private RechargeEntity rechargeData;

    private LoadingDialogProgress loadingDialogProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klh5);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.xzx_color),0);
        initView();
        initData();
    }

    private void initView() {
        webView=findViewById(R.id.web_view_root);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingDialogProgress = GlobalTools.getInstance().showDailog(KLH5Activity.this, "加载..");

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (loadingDialogProgress !=null){
                    loadingDialogProgress.dismiss();
                }


            }
        });

    }

    private void initData() {
        Intent intent =getIntent();
        rechargeData = (RechargeEntity) intent.getExtras().getSerializable("rechargeData");
        createOrder(rechargeData);
    }

    private void createOrder(RechargeEntity rechargeData) {

        StringBuffer sb =new StringBuffer();
        sb.append("account="+Common.CARD_NO);
        sb.append("&orderdesc="+rechargeData.getOrderdesc());
        sb.append("&ordertype="+rechargeData.getOrdertype());
        sb.append("&praram1="+rechargeData.getPraram1());
        sb.append("&sno="+rechargeData.getSno());
        sb.append("&sign="+rechargeData.getSign());
        sb.append("&thirdorderid="+rechargeData.getThirdorderid());
        sb.append("&thirdsystem="+rechargeData.getThirdsystem());
        sb.append("&thirdurl="+rechargeData.getThirdurl());
        sb.append("&toaccount="+rechargeData.getToaccount());
        sb.append("&tranamt="+rechargeData.getTranamt());
        sb.append("&b=0");
         String urlString =sb.toString();
        //Order/CreateOrde

        webView.postUrl(Common.PAY_URL, urlString.getBytes());

    }
}
