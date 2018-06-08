package com.example.hzy.lovenum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.WalkRouteResult;
import com.dingwei.util.AMapUtil;
import com.dingwei.util.ToastUtil;
import com.hzy.serviceimpl.SharedPreferencesHelper;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import overlay.DrivingRouteOverlay;


/**
 * AMapV2地图中介绍使用active deactive进行定位<br>
 * 自定义定位模式，当sdk中提供的定位模式不满足要求时，可以自定义<br>
 *
 * 自定义效果<br>
 * 1、定位成功后， 小蓝点和和地图一起移动到定位点<br>
 * 2、手势操作地图后模式修改为 仅定位不移动到中心点<br>
 *
 *
 */
public class mainActivity extends AppCompatActivity implements LocationSource,
		AMapLocationListener, AMap.OnMapTouchListener, View.OnClickListener, TextWatcher,
        Inputtips.InputtipsListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter,
        RouteSearch.OnRouteSearchListener,OnMapClickListener,OnInfoWindowClickListener {
	private AMap aMap;
	private MapView mapView;
	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;
    private TextView searchText;// 输入搜索关键字
    private TextView editCity;// 要输入的城市名字或者城市区号
    Button searButton;
	boolean useMoveToLocationWithMapMode = true;
	private String locationaddress;
	//自定义定位小蓝点的Marker
	Marker locationMarker;

	//坐标和经纬度转换工具
	Projection projection;
    String strContentString="";
    String receivelocation="";
    SharedPreferences settings;
    double sendlat=0.0;
    double sendlon=0.0;
    double receivelat=0.0;
    double receivelon=0.0;
    private RouteSearch mRouteSearch;
    private final int ROUTE_TYPE_DRIVE = 2;
    private DriveRouteResult mDriveRouteResult;
    private RelativeLayout mBottomLayout, mHeadLayout;
    private TextView mRotueTimeDes, mRouteDetailDes;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
       // requestWindowFeature(Window.FEATURE_LEFT_ICON);// 不显示程序的标题栏
        Logger.addLogAdapter(new AndroidLogAdapter());
//取二次定位的数据
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// strContentString = bundle.getString("location","asd")==null?"":bundle.getString("location","asd");
        try {
            strContentString = bundle.getString("location","");
            receivelocation = bundle.getString("receivelocation","");

        }catch (Exception e){

        }
        if("".equals(strContentString)||null==strContentString){strContentString=SharedPreferencesHelper.getInstance(mainActivity.this).getStringValue("sendlocation");}
        if("".equals(receivelocation)||null==receivelocation){receivelocation=SharedPreferencesHelper.getInstance(mainActivity.this).getStringValue("receivelocation");}
		//Logger.d("SendinfoActivity*******************"+strContentString);
		//mapView = new MapView(this);


		//RelativeLayout layout = new RelativeLayout(this);
		//layout.addView(mapView);


		TextView textView = new TextView(this);
		textView.setText("自定义效果\n" +
				" 1、定位成功后， 小蓝点和和地图一起移动到定位点\n" +
				" 2、手势操作地图后模式修改为 仅定位不移动到中心点");
		//layout.addView(textView);

		//setContentView(layout);
		setContentView(R.layout.main_activity);
		mapView = (MapView) findViewById(R.id.map);//找到地图控件
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
        if(null!=editCity.getText()&&!"".equals(editCity.getText())&&null!=searchText.getText()&&!"".equals(searchText.getText())){
            searButton.performClick();
        }
	}


	/**
	 * 初始化
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
            if(!"".equals(strContentString)){
                SharedPreferencesHelper.getInstance(mainActivity.this).putStringValue("sendlocation",strContentString);}
             if(!"".equals(receivelocation)){SharedPreferencesHelper.getInstance(mainActivity.this).putStringValue("receivelocation",receivelocation);}
		}
        registerListener();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);
        mBottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
        mRotueTimeDes = (TextView) findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) findViewById(R.id.secondline);

	}
    /**
     * 注册监听
     */
    private void registerListener() {
        aMap.setOnMapClickListener(mainActivity.this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }
        /**
         * 存储数据
         */
    private void saveinfo(String str,String str1) {

         settings = getSharedPreferences("com.example.hzy.lovenum.SharedPreferencesone", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        //editor.putString("sendlocation",strContentString);
        //editor.putString("receivelocation",receivelocation);
        editor.putString(str,str1);
        //editor.putString("receivelocation",receivelocation);
        editor.commit();
        String sharedPreferencesString = settings.getString("sendlocation","");
        //System.out.println("sharedPreferencesString："+ sharedPreferencesString);
        Logger.d("sendlocation"+sharedPreferencesString);
    }
    /**
     * 查询数据
     */
    private String getinfo(String str) {
        settings = getSharedPreferences("com.example.hzy.lovenum.SharedPreferencesone", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();


        String sharedPreferencesString = settings.getString(str,"");
        System.out.println("sharedPreferencesString："+ sharedPreferencesString);
        return sharedPreferencesString;
    }
	/**
	 * 删除数据
	 */
	private void delinfo(String str) {
		settings = getSharedPreferences("com.example.hzy.lovenum.SharedPreferencesone", MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(str);


	}
	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

		aMap.setOnMapTouchListener(this);

         searButton = (Button) findViewById(R.id.searchButton);
        searButton.setOnClickListener(this);
        searButton.setVisibility(View.GONE);
        //Button nextButton = (Button) findViewById(R.id.nextButton);
        //nextButton.setOnClickListener(this);
        //nextButton.setVisibility(View.GONE);
        searchText = (TextView) findViewById(R.id.keyWord);

        //searchText.addTextChangedListener(this);// 添加文本输入框监听事件
        searchText.setOnClickListener(this);

                searchText.setText(receivelocation);
        editCity = (TextView) findViewById(R.id.city);
        editCity.setOnClickListener(this);
        editCity.setText(strContentString);
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
	}


	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();

		useMoveToLocationWithMapMode = true;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		deactivate();

		useMoveToLocationWithMapMode = false;
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		if(null != mlocationClient){
			mlocationClient.onDestroy();
		}
        SharedPreferencesHelper.getInstance(mainActivity.this).removeStringValue("sendlocation");
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				LatLng latLng = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());


                //地址，如果option中设置isNeedAddress为false，则没有此结果
                locationaddress = amapLocation.getAddress();
                Logger.d("locationaddress：" + locationaddress +
                        "#####strContentString：" + strContentString+"&&");


                if ("".equals(locationaddress) && "".equals(strContentString)) {
                    ToastUtil.show(this, "无法获取定位位置");
                    return;}
                else if("".equals(strContentString) ){
                    editCity.setText(amapLocation.getAddress());
                    //SharedPreferencesHelper.getInstance(mainActivity.this).putStringValue("sendlocation",strContentString);
                    SharedPreferencesHelper.getInstance(mainActivity.this).putDoubbleValue("sendlat", amapLocation.getLatitude());
                    SharedPreferencesHelper.getInstance(mainActivity.this).putDoubbleValue("sendlon",amapLocation.getLongitude());
                }else{
                    editCity.setText(strContentString);
                    locationaddress=strContentString;
                }

                //Logger.d(editCity.getText()+"|"+searchText.getText());
                if(null!=editCity.getText()&&!"".equals(editCity.getText())&&null!=searchText.getText()&&!"".equals(searchText.getText())){
                    searButton.performClick();
                }

				//展示自定义定位小蓝点
				if(locationMarker == null) {
					//首次定位
					locationMarker = aMap.addMarker(new MarkerOptions().position(latLng)
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
							.anchor(0.5f, 0.5f));

					//首次定位,选择移动到地图中心点并修改级别到15级
					aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
				} else {

					if(useMoveToLocationWithMapMode) {
						//二次以后定位，使用sdk中没有的模式，让地图和小蓝点一起移动到中心点（类似导航锁车时的效果）
						startMoveLocationAndMap(latLng);
					} else {
						startChangeLocation(latLng);
					}
					
				}


			} else {
                if(null!=editCity.getText()&&!"".equals(editCity.getText())&&null!=searchText.getText()&&!"".equals(searchText.getText())){
                    searButton.performClick();
                }
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
			}
		}else{
            if(null!=editCity.getText()&&!"".equals(editCity.getText())&&null!=searchText.getText()&&!"".equals(searchText.getText())){
                searButton.performClick();
            }
        }
	}


	/**
	 * 修改自定义定位小蓝点的位置
	 * @param latLng
	 */
	private void startChangeLocation(LatLng latLng) {

		if(locationMarker != null) {
			LatLng curLatlng = locationMarker.getPosition();
			if(curLatlng == null || !curLatlng.equals(latLng)) {
				locationMarker.setPosition(latLng);
			}
		}
	}

	/**
	 * 同时修改自定义定位小蓝点和地图的位置
	 * @param latLng
	 */
	private void startMoveLocationAndMap(LatLng latLng) {

		//将小蓝点提取到屏幕上
		if(projection == null) {
			projection = aMap.getProjection();
		}
		if(locationMarker != null && projection != null) {
			LatLng markerLocation = locationMarker.getPosition();
			Point screenPosition = aMap.getProjection().toScreenLocation(markerLocation);
			locationMarker.setPositionByPixels(screenPosition.x, screenPosition.y);

		}

		//移动地图，移动结束后，将小蓝点放到放到地图上
		myCancelCallback.setTargetLatlng(latLng);
		//动画移动的时间，最好不要比定位间隔长，如果定位间隔2000ms 动画移动时间最好小于2000ms，可以使用1000ms
		//如果超过了，需要在myCancelCallback中进行处理被打断的情况
		aMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng),1000,myCancelCallback);

	}


	MyCancelCallback myCancelCallback = new MyCancelCallback();

	@Override
	public void onTouch(MotionEvent motionEvent) {
		Log.i("amap","onTouch 关闭地图和小蓝点一起移动的模式");
		useMoveToLocationWithMapMode = false;
	}

    private LatLonPoint mStartPoint = new LatLonPoint(39.942295,116.335891);//起点，39.942295,116.335891
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576,116.481288);//终点，39.995576,116.481288

    /**
     * Button点击事件回调方法
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            /**
             * 点击搜索按钮
             */
            case R.id.searchButton:

                sendlat=  SharedPreferencesHelper.getInstance(mainActivity.this).getDoubleValue("sendlat");
                sendlon =SharedPreferencesHelper.getInstance(mainActivity.this).getDoubleValue("sendlon");
                receivelat=SharedPreferencesHelper.getInstance(mainActivity.this).getDoubleValue("receivelat");
                receivelon = SharedPreferencesHelper.getInstance(mainActivity.this).getDoubleValue("receivelon");
                Logger.d("起点纬度（"+sendlat+"）起点经度（"+sendlon+"）终点纬度（"+receivelat+"）终点经度（"+receivelon+")");
                if(sendlat==0||sendlon==0) {
                    Toast.makeText(this, "起点未选择", Toast.LENGTH_LONG).show();
                }else if (receivelat==0||receivelon==0){
                    Toast.makeText(this, "终点未选择", Toast.LENGTH_LONG).show();
                }else{
                    //float distance = AMapUtils.calculateLineDistance(new LatLng(sendlat,sendlon),new LatLng(receivelat,receivelon));
                    //Toast.makeText(this, "距离为："+distance, Toast.LENGTH_LONG).show();
                     mStartPoint = new LatLonPoint(sendlat,sendlon);//起点，39.942295,116.335891
                      mEndPoint = new LatLonPoint(receivelat,receivelon);//终点，39.995576,116.481288

                    setfromandtoMarker();
                    searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
                    /*if(sendlat>receivelat&&sendlon>receivelon){
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(receivelat+(sendlat-receivelat)/2,receivelon+(sendlon-receivelon)/2), 18));
                    }else if (sendlat>receivelat&&sendlon<receivelon){
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(receivelat+(sendlat-receivelat)/2,sendlon+(receivelon-sendlon)/2), 18));
                    }else if (sendlat<receivelat&&sendlon<receivelon){
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sendlat+(receivelat-sendlat)/2,sendlon+(receivelon-sendlon)/2), 18));
                    }else{
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(sendlat+(receivelat-sendlat)/2,receivelon+(sendlon-receivelon)/2), 18));

                    }*/

                }


                break;
            /**
             * 点击下一页按钮
             */
            case R.id.nextButton:
                //nextButton();
                break;
            case R.id.city:
                //Intent intent = new Intent();
                //(当前Activity，目标Activity)
                // intent.setClass(LoginActivity.this, Activityname1.class);
                //intent.setClass(LoginActivity.this, PoiKeywordSearchActivity.class);
                //intent.setClass(LoginActivity.this, CustomLocationActivity.class);
                //intent.setClass(LoginActivity.this, CustomLocationModeActivity.class);
                intent.setClass(this, SendinfoActivity.class);
				intent.putExtra("location", locationaddress);
                Logger.d("locationaddress*******************"+locationaddress);
                startActivity(intent);
                //finish();
                break;
            case R.id.keyWord:
                //Intent intent = new Intent();
                //(当前Activity，目标Activity)
                // intent.setClass(LoginActivity.this, Activityname1.class);
                //intent.setClass(LoginActivity.this, PoiKeywordSearchActivity.class);
                //intent.setClass(LoginActivity.this, CustomLocationActivity.class);
                //intent.setClass(LoginActivity.this, CustomLocationModeActivity.class);
                intent.setClass(this, twolocationActivity.class);
                intent.putExtra("location", "receivelocation");
                Logger.d("locationaddress*******************"+locationaddress);
                startActivity(intent);
                //finish();
                break;
            default:
                break;
        }
    }

    private void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtil.show(this, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(this, "终点未设置");
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mode, null,
                    null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration()*3;
                    String des = AMapUtil.getFriendlyTime(dur)+"内送达("+AMapUtil.getFriendlyLength(dis)+")";

                    mRotueTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.VISIBLE);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                   mRouteDetailDes.setText("金额共计"+taxiCost+"元");
                    //ToastUtil.show(this, des+"约"+taxiCost+"元");
                   /* mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,
                                    DriveRouteDetailActivity.class);
                            intent.putExtra("drive_path", drivePath);
                            intent.putExtra("drive_result",
                                    mDriveRouteResult);
                            startActivity(intent);
                        }
                    });*/

                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(this, R.string.no_result);
                }

            } else {
                ToastUtil.show(this, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }


    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    private ProgressDialog progDialog = null;// 搜索时进度条
    /**
     * 显示进度框
     */
    private void showProgressDialog() {

        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" );
        progDialog.show();
    }
    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
    /**
     * 点击搜索按钮
     */
    public void searchButton() {


    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!AMapUtil.IsEmptyOrNullString(newText)) {
            InputtipsQuery inputquery = new InputtipsQuery(newText, "乌鲁木齐");
            //InputtipsQuery inputquery = new InputtipsQuery(newText, editCity.getText().toString
            // ());
            Inputtips inputTips = new Inputtips(this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
           // searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showerror(this, rCode);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    /**
	 * 监控地图动画移动情况，如果结束或者被打断，都需要执行响应的操作
	 */
	 class MyCancelCallback implements AMap.CancelableCallback {

		LatLng targetLatlng;
		public void setTargetLatlng(LatLng latlng) {
			this.targetLatlng = latlng;
		}

		@Override
		public void onFinish() {
			if(locationMarker != null && targetLatlng != null) {
				locationMarker.setPosition(targetLatlng);
			}
		}

		@Override
		public void onCancel() {
			if(locationMarker != null && targetLatlng != null) {
				locationMarker.setPosition(targetLatlng);
			}
		}
	};



	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mlocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
			//是指定位间隔
			mLocationOption.setInterval(200000);
			//设置定位参数
			mlocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mlocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}


}