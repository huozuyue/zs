package com.example.hzy.lovenum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hzy.serviceimpl.SharedPreferencesHelper;
import com.orhanobut.logger.Logger;

public class SendinfoActivity extends AppCompatActivity implements View.OnClickListener {
    private String strContentString = "";
    private String detail_sendstr = "";
    private String name_sendstr = "";
    private String tel_sendstr = "";
    private EditText detail_send;
    private EditText name_send;
    private EditText tel_send;
    private TextView textView1;
    private String remarks_sendstr = "";
    private EditText remarks_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendinfo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        strContentString = SharedPreferencesHelper.getInstance(SendinfoActivity.this).getStringValue
                ("sendlocation");
        detail_sendstr = SharedPreferencesHelper.getInstance(SendinfoActivity.this).getStringValue
                ("detail_sendstr");
        name_sendstr = SharedPreferencesHelper.getInstance(SendinfoActivity.this).getStringValue
                ("name_sendstr");
        tel_sendstr = SharedPreferencesHelper.getInstance(SendinfoActivity.this).getStringValue
                ("tel_sendstr");
        remarks_sendstr = SharedPreferencesHelper.getInstance(SendinfoActivity.this).getStringValue
                ("remarks_sendstr");
        Intent intent = getIntent();
       Bundle bundle = intent.getExtras();
        strContentString = bundle.getString("location","");
        Logger.d("SendinfoActivity*******************"+strContentString);
        textView1 = findViewById(R.id.send_locationID);
        detail_send = findViewById(R.id.detail_send);
        name_send = findViewById(R.id.name_send);
        tel_send = findViewById(R.id.tel_send);
        remarks_send = findViewById(R.id.remarks_send);
        textView1.setText(strContentString);
        detail_send.setText(detail_sendstr);
        name_send.setText(name_sendstr);
        tel_send.setText(tel_sendstr);
        remarks_send.setText(remarks_sendstr);
        Button button = findViewById(R.id.button);
        textView1.setOnClickListener(this);
        button.setOnClickListener(this);
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Button点击事件回调方法
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            /**
             * 点击确认按钮
             */
            case R.id.button:

                strContentString = textView1.getText().toString();
                detail_sendstr = detail_send.getText().toString();
                name_sendstr = name_send.getText().toString();
                tel_sendstr = tel_send.getText().toString();
                remarks_sendstr = remarks_send.getText().toString();


                SharedPreferencesHelper.getInstance(SendinfoActivity.this).putStringValue
                        ("sendlocation", strContentString);
                SharedPreferencesHelper.getInstance(SendinfoActivity.this).putStringValue
                        ("detail_sendstr", detail_sendstr);
                SharedPreferencesHelper.getInstance(SendinfoActivity.this).putStringValue
                        ("name_sendstr", name_sendstr);
                SharedPreferencesHelper.getInstance(SendinfoActivity.this).putStringValue
                        ("tel_sendstr", tel_sendstr);
                SharedPreferencesHelper.getInstance(SendinfoActivity.this).putStringValue
                        ("remarks_sendstr", remarks_sendstr);
                intent.setClass(this, mainActivity.class);
                intent.putExtra("location", strContentString);
                Logger.d("locationaddress*******************"+"location");
                startActivity(intent);
                //finish();
                break;
            /**
             * 点击下一页按钮
             */
            case R.id.nextButton:
                //nextButton();
                break;
            case R.id.send_locationID:
              //  Intent intent = new Intent();
                //(当前Activity，目标Activity)
                // intent.setClass(LoginActivity.this, Activityname1.class);
                //intent.setClass(LoginActivity.this, PoiKeywordSearchActivity.class);
                //intent.setClass(LoginActivity.this, CustomLocationActivity.class);
                //intent.setClass(LoginActivity.this, CustomLocationModeActivity.class);
                intent.setClass(this, twolocationActivity.class);
                intent.putExtra("location", "sendlocation");
                Logger.d("locationaddress*******************"+"location");
                startActivity(intent);
                //finish();
                break;
            default:
                break;
        }
    }


}
