package com.example.hzy.lovenum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.hzy.util.RetrofitUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;


public class myOrde extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orde);
        Logger.addLogAdapter(new AndroidLogAdapter());

        //RelativeLayout layout = new RelativeLayout(this);
        //layout.addView(mapView);
        LinearLayout one = findViewById(R.id.myorder);
        //ScrollView one=findViewById(R.id.myorder);
        /*TextView tv1=new TextView(this);
        TextView tv2=new TextView(this);
        TextView tv3=new TextView(this);
        TextView tv4=new TextView(this);
        TextView tv5=new TextView(this);
        //ConstraintLayout layout=findViewById(R.id.myorder);
        tv1.setText("铁路局");
        tv2.setText("世界名筑");
        tv3.setText("订单编号：00002");
        tv4.setText("待抢单");
        tv5.setText("2018年3月22日 21:30:00");
        //button.setBackground(getResources().getDrawable(R.drawable.ic_launcher));


        LinearLayout three_1=new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams
        .MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //imageView.setLayoutParams(lp);
        @android.support.annotation.IdRes
        int TAG1401 = 1000;
        int TAG1402 = 1001;
        int TAG1403 = 1002;
        int TAG1404 = 1003;
        int TAG1405 = 1004;
        int TAG1406 = 1005;
        int TAG1407 = 1006;
        three_1.addView(tv3);
        three_1.addView(tv4);
        LinearLayout three_2=new LinearLayout(this);
        three_2.addView(tv5);
        LinearLayout three_3=new LinearLayout(this);
        three_3.addView(tv1);
        LinearLayout three_4=new LinearLayout(this);
        three_4.addView(tv2);
        LinearLayout two_1=new LinearLayout(this);
        two_1.addView(three_1);
        two_1.addView(three_2);
        two_1.addView(three_3);
        two_1.addView(three_4);
        two_1.setBackgroundResource(R.drawable.bg_bottom_bar);
        two_1.setOrientation(LinearLayout.VERTICAL);
        lp.setMargins(10, 10, 10, 10);
        two_1.setLayoutParams(lp);

        two_1.setId(TAG1401+1);

int i=1;
        //one.addView(two_1);

        //View bannerView = LayoutInflater.from(myOrde.this).inflate(R.layout.activity_my_orde,
        null);
        //View bannerView1 = LayoutInflater.from(this).inflate(R.layout.activity_my_orde, null);
        View bannerView = ((LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE))
        .inflate(R
                .layout.my_orderitem,null);
        TextView tv_orderNO=bannerView.findViewById(R.id.orderNO);
        TextView tv_orderStatus=bannerView.findViewById(R.id.orderStatus);
        TextView tv_orderDate=bannerView.findViewById(R.id.orderDate);
        TextView tv_orderStart=bannerView.findViewById(R.id.orderStart);
        TextView tv_orderEnd=bannerView.findViewById(R.id.orderEnd);
        //two_2.addView(tv1);
        tv_orderNO.setText("0002");
        tv_orderStatus.setText("进行中");
        tv_orderStatus.setId(TAG1401+i);
        tv_orderDate.setText("2018年3月22日 01:30:00");
        tv_orderStart.setText("天涯海角");
        tv_orderEnd.setText("世界之大");

        //one.addView(bannerView);
       // one.addView(bannerView1);


       // LinearLayout two_2 = (LinearLayout) v.findViewById(R.id.orderNO);
        //two_2.setOrientation(LinearLayout.VERTICAL);
        one.addView(bannerView);
         bannerView = ((LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE))
         .inflate(R
                .layout.my_orderitem,null);
         tv_orderNO=bannerView.findViewById(R.id.orderNO);
         tv_orderStatus=bannerView.findViewById(R.id.orderStatus);
         tv_orderDate=bannerView.findViewById(R.id.orderDate);
         tv_orderStart=bannerView.findViewById(R.id.orderStart);
         tv_orderEnd=bannerView.findViewById(R.id.orderEnd);
        //two_2.addView(tv1);
        //Gson gson=new Gson();
        tv_orderNO.setText("0002");
        tv_orderStatus.setText("进行中");
        tv_orderStatus.setId(TAG1401+i);
        tv_orderDate.setText("2018年3月22日 01:30:00");
        tv_orderStart.setText("天涯海角");
        tv_orderEnd.setText("世界之大");*/

        //one.addView(bannerView);
        // one.addView(bannerView1);
        RetrofitUtil.getOrderInfo(myOrde.this, myOrde.this, one);

        // LinearLayout two_2 = (LinearLayout) v.findViewById(R.id.orderNO);
        //two_2.setOrientation(LinearLayout.VERTICAL);
        //one.addView(bannerView);

//获取控件
        // img_back = (ImageButton) v.findViewById(getResources().getIdentifier
        // (“mediacontroller_top_back”, “id”, context.getPackageName()));


        //layout.addView(tv1);


    }

    @Override
    public void onClick(View v) {
        int tv_id = v.getId();
        Logger.d("v.getId();*******************:" + v.getId());
        // TextView tv_orderDate=(TextView)findViewById(tv_id*999);
        RetrofitUtil.getOrderDetail(myOrde.this, this, 3, v.getId(), "");
    }
}
