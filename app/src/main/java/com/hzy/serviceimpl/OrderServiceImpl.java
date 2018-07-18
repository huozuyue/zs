package com.hzy.serviceimpl;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.dingwei.util.AMapUtil;
import com.example.hzy.lovenum.OrderDetail;
import com.example.hzy.lovenum.R;
import com.hzy.model.OrderInfo;
import com.hzy.model.UserOrder;
import com.hzy.util.JsonListUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hzy on 2018/7/16.
 */

public class OrderServiceImpl {


    public void onAccessSuccess(String jsonstring, Activity activity, String orderDate) {


        //OrderInfo OrderInfo  =  JsonListUtil.jsonToObject(jsonstring,  OrderInfo.class);
        Intent intent = new Intent();
        intent.setClass(activity, OrderDetail.class);
        intent.putExtra("orderDetail", jsonstring);
        intent.putExtra("orderDate", orderDate);
        Logger.d(jsonstring);
        Logger.d(orderDate);
        activity.startActivity(intent);

    }

    public String getStr(int orderNo) {

        JSONObject Order = new JSONObject();

        try {
            Order.put("orderNo", orderNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Order.toString();
    }

    /* public String getOderStatus( int Status){
         if (0 == Status )  {
            return "待支付";
         } else if (retList.get(i).getStatus() == 1) {
             tv_orderStatus.setText("待抢单");
         } else if (retList.get(i).getStatus() == 2) {
             tv_orderStatus.setText("进行中");
         } else if (retList.get(i).getStatus() == 3) {
             tv_orderStatus.setText("已完成");

         } else {
             tv_orderStatus.setText("状态异常");
         }

     }*/
    public String showOrderinfo(String orderDetail, String orderDate, OrderDetail activity, int
            orderNo) {
        activity.setContentView(R.layout.activity_order_detail);


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
            Button btn_zhifu;
            tv_orderNO = activity.findViewById(R.id.orderNO);
            tv_orderStatus = activity.findViewById(R.id.orderStatus);
            tv_orderDate = activity.findViewById(R.id.orderDate);
            tv_orderStart = activity.findViewById(R.id.orderStart);
            tv_orderEnd = activity.findViewById(R.id.orderEnd);
            tv_send_address = activity.findViewById(R.id.send_address);
            tv_send_name = activity.findViewById(R.id.send_name);
            tv_send_phone = activity.findViewById(R.id.send_phone);
            tv_receivieaddress = activity.findViewById(R.id.receivieaddress);
            tv_receive_name = activity.findViewById(R.id.receive_name);
            tv_receive_phone = activity.findViewById(R.id.receive_phone);
            tv_distance = activity.findViewById(R.id.distance);
            tv_shijian = activity.findViewById(R.id.shijian);
            tv_cost = activity.findViewById(R.id.cost);
            btn_zhifu = activity.findViewById(R.id.zhifu);
            tv_orderNO.setText(oi.getOrderNo());
            tv_orderStatus.setText(uo.getStatus());
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
            tv_shijian.setText("预计(" + AMapUtil.getFriendlyTime(oi.getDur()) + "内送达!");
            tv_cost.setText("金额共计" + oi.getTaxiCost() + "元");
            Logger.d("金额共计" + oi.getTaxiCost() + "元");


        }


        return "";
    }
}
