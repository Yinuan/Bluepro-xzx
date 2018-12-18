package com.klcxkj.bluepro_xzx;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.klcxkj.zqxy.ui.BathChoseActivity;
import com.klcxkj.zqxy.ui.MyBillActivity;
import com.klcxkj.zqxy.ui.WashingChosActivity;
import com.klcxkj.zqxy.zxing.zxing.activity.ScanActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main4Activity extends AppCompatActivity {


    @BindView(R.id.button9)
    Button button9;
    @BindView(R.id.button10)
    Button button10;
    @BindView(R.id.button11)
    Button button11;
    @BindView(R.id.button12)
    Button button12;
    @BindView(R.id.button13)
    Button button13;
    @BindView(R.id.button14)
    Button button14;
    @BindView(R.id.button15)
    Button button15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.button9, R.id.button10, R.id.button11, R.id.button12,
            R.id.button13, R.id.button14, R.id.button15})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.button9:
                intent = new Intent(Main4Activity.this,BathChoseActivity.class);
                startActivity(intent);
                break;
            case R.id.button10:
                startActivity(new Intent(Main4Activity.this, WashingChosActivity.class));
                break;
            case R.id.button11:
                intent = new Intent(Main4Activity.this, ScanActivity.class);
                intent.putExtra("capture_type", ScanActivity.CAPTURE_DRINK);
                startActivity(intent);
                break;
            case R.id.button12:
                intent = new Intent(Main4Activity.this, ScanActivity.class);
                intent.putExtra("capture_type", ScanActivity.CAPTURE_DRYER);
                startActivity(intent);
                break;
            case R.id.button13:
                intent = new Intent(Main4Activity.this, ScanActivity.class);
                intent.putExtra("capture_type", ScanActivity.CAPTURE_ELE);
                startActivity(intent);
                break;
            case R.id.button14:
                intent = new Intent(Main4Activity.this, ScanActivity.class);
                intent.putExtra("capture_type", ScanActivity.CAPTURE_OTHER);
                startActivity(intent);
                break;
            case R.id.button15:
                startActivity(new Intent(Main4Activity.this, MyBillActivity.class));
                break;
        }
    }
}
