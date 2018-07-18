package com.example.hzy.lovenum;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hzy.serviceimpl.SharedPreferencesHelper;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout ctbl = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ctbl.setTitle("18931121207");
        setSupportActionBar(toolbar);
// 设置返回键和菜单栏可用，可见
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                //SharedPreferencesHelper.getInstance(ScrollingActivity.this).removeStringValue
                // ("token");
                SharedPreferencesHelper.getInstance(ScrollingActivity.this).clear();
                /*Snackbar.make(view, "已退出登录", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent();
                intent.setClass(ScrollingActivity.this, mainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation
                .TOP_BOTTOM, new int[]{Color.RED, Color.YELLOW});
        getWindow().setBackgroundDrawable(gradientDrawable);
    }
}
