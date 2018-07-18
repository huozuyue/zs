package com.example.hzy.lovenum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hzy.serviceimpl.SharedPreferencesHelper;
import com.orhanobut.logger.Logger;

public class receiveinfo extends AppCompatActivity implements View.OnClickListener {
    private String strContentString = "";
    private String receive_addressstr = "";
    private String receive_namestr = "";
    private String receive_phonestr = "";
    private EditText receive_address;
    private EditText receive_name;
    private EditText receive_phone;
    private TextView receive_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiveinfo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Logger.d("SendinfoActivity*******************" + strContentString);
        strContentString = SharedPreferencesHelper.getInstance(receiveinfo.this).getStringValue
                ("receive_location");
        receive_addressstr = SharedPreferencesHelper.getInstance(receiveinfo.this).getStringValue
                ("receive_addressstr");
        receive_namestr = SharedPreferencesHelper.getInstance(receiveinfo.this).getStringValue
                ("receive_namestr");
        receive_phonestr = SharedPreferencesHelper.getInstance(receiveinfo.this).getStringValue
                ("receive_phonestr");
        receive_location = findViewById(R.id.receive_location);
        receive_address = findViewById(R.id.receive_address);
        receive_name = findViewById(R.id.receive_name);
        receive_phone = findViewById(R.id.receive_phone);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        strContentString = bundle.getString("location");
        receive_location.setText(strContentString);
        receive_address.setText(receive_addressstr);
        receive_name.setText(receive_namestr);
        receive_phone.setText(receive_phonestr);
        receive_location.setOnClickListener(this);
        Button button =findViewById(R.id.button);
        button.setOnClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * Button点击事件回调方法
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            /**
             * 点击确定按钮
             */
            case R.id.button:
                receive_address = findViewById(R.id.receive_address);
                receive_name = findViewById(R.id.receive_name);
                receive_phone = findViewById(R.id.receive_phone);
                strContentString = receive_location.getText().toString();
                receive_addressstr = receive_address.getText().toString();
                receive_namestr = receive_name.getText().toString();
                receive_phonestr = receive_phone.getText().toString();


                SharedPreferencesHelper.getInstance(receiveinfo.this).putStringValue
                        ("receive_location", strContentString);
                SharedPreferencesHelper.getInstance(receiveinfo.this).putStringValue
                        ("receive_addressstr", receive_addressstr);
                SharedPreferencesHelper.getInstance(receiveinfo.this).putStringValue
                        ("receive_namestr", receive_namestr);
                SharedPreferencesHelper.getInstance(receiveinfo.this).putStringValue
                        ("receive_phonestr", receive_phonestr);
                intent.setClass(this, mainActivity.class);
                intent.putExtra("receivelocation", strContentString);
                Logger.d("locationaddress*******************"+strContentString);
                startActivity(intent);
                //finish();

                break;
            /**
             * 点击下一页按钮
             */
            case R.id.nextButton:
                //nextButton();
                break;
            case R.id.receive_location:
              //  Intent intent = new Intent();
                //(当前Activity，目标Activity)
                // intent.setClass(LoginActivity.this, Activityname1.class);
                //intent.setClass(LoginActivity.this, PoiKeywordSearchActivity.class);
                //intent.setClass(LoginActivity.this, CustomLocationActivity.class);
                //intent.setClass(LoginActivity.this, CustomLocationModeActivity.class);
                intent.setClass(this, twolocationActivity.class);
                intent.putExtra("location", "receivelocation");
                Logger.d("locationaddress*******************"+"location");
                startActivity(intent);
                //finish();
                break;
            default:
                break;
        }
    }

}
