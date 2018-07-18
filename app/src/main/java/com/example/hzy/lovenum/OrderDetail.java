package com.example.hzy.lovenum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dingwei.util.AMapUtil;
import com.hzy.enumclass.OrderStatus;
import com.hzy.model.OrderInfo;
import com.hzy.model.UserOrder;
import com.hzy.util.JsonListUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class OrderDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.addLogAdapter(new AndroidLogAdapter());
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        String orderDetail = "";
        String orderDate = "";

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        // strContentString = bundle.getString("location","asd")==null?"":bundle.getString
        // ("location","asd");
        try {
            orderDetail = bundle.getString("orderDetail", "");
            orderDate = bundle.getString("orderDate", "");

        } catch (Exception e) {

        }
        setContentView(R.layout.activity_order_detail);


        if (null != orderDetail && null != orderDate && !"".equals(orderDetail) && !"".equals
                (orderDate)) {

            OrderInfo oi = JsonListUtil.jsonToObject(orderDetail, OrderInfo.class);
            UserOrder uo = JsonListUtil.jsonToObject(orderDate, UserOrder.class);
            TextView tv_orderNO;
            TextView tv_orderStatus;
            TextView tv_orderDate;
            TextView tv_orderStart;
            TextView tv_orderEnd;
            TextView tv_send_address;
            TextView tv_send_name;
            TextView tv_send_phone;
            TextView tv_receivieaddress;
            TextView tv_receive_name;
            TextView tv_receive_phone;
            TextView tv_distance;
            TextView tv_shijian;
            TextView tv_cost;
            TextView tv_remark;
            Button btn_zhifu;
            tv_orderNO = findViewById(R.id.orderNO);
            tv_orderStatus = findViewById(R.id.orderStatus);
            tv_orderDate = findViewById(R.id.orderDate);
            tv_orderStart = findViewById(R.id.orderStart);
            tv_orderEnd = findViewById(R.id.orderEnd);
            tv_send_address = findViewById(R.id.send_address);
            tv_send_name = findViewById(R.id.send_name);
            tv_send_phone = findViewById(R.id.send_phone);
            tv_receivieaddress = findViewById(R.id.receivieaddress);
            tv_receive_name = findViewById(R.id.receive_name);
            tv_receive_phone = findViewById(R.id.receive_phone);
            tv_distance = findViewById(R.id.distance);
            tv_shijian = findViewById(R.id.shijian);
            tv_cost = findViewById(R.id.cost);
            btn_zhifu = findViewById(R.id.zhifu);
            tv_remark = findViewById(R.id.remarks);
            String remark = oi.getRemarks_sendstr();
            if (null != remark && !"".equals(remark)) {
                tv_remark.setVisibility(View.VISIBLE);
                tv_remark.setText(remark);
            }
            tv_orderNO.setText(String.valueOf(oi.getOrderNo()));
            tv_orderStatus.setText(String.valueOf(OrderStatus.getOrderState(uo.getStatus())));
            tv_orderDate.setText(uo.getCommittime());
            tv_orderStart.setText(oi.getSendlocation());
            tv_orderEnd.setText(oi.getReceive_location());
            tv_send_address.setText(oi.getDetail_sendstr());
            tv_send_name.setText(oi.getName_sendstr());
            tv_send_phone.setText(oi.getTel_sendstr());
            tv_receivieaddress.setText(oi.getReceive_addressstr());
            tv_receive_name.setText(oi.getReceive_namestr());
            tv_receive_phone.setText(oi.getReceive_phonestr());
            tv_distance.setText(AMapUtil.getFriendlyLength(oi.getDis()));
            tv_shijian.setText("预计" + AMapUtil.getFriendlyTime(oi.getDur()) + "内送达!");
            tv_cost.setText("金额共计" + oi.getTaxiCost() + "元");
            Logger.d("金额共计" + oi.getTaxiCost() + "元");


        }


        //OrderServiceImpl osi=new OrderServiceImpl();
        //osi.showOrderinfo(orderDetail,orderDate, this,2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
