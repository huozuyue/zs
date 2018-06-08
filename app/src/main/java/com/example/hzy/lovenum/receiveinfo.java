package com.example.hzy.lovenum;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

public class receiveinfo extends AppCompatActivity implements View.OnClickListener {
    String strContentString ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiveinfo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
         strContentString = bundle.getString("location");
        Logger.d("SendinfoActivity*******************"+strContentString);
        TextView textView1=findViewById(R.id.textView);
        textView1.setText(strContentString);
        textView1.setOnClickListener(this);
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
            case R.id.textView:
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
