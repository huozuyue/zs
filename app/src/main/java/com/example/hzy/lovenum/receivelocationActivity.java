package com.example.hzy.lovenum;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.dingwei.util.AMapUtil;
import com.dingwei.util.ToastUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import overlay.PoiOverlay;

public class receivelocationActivity extends AppCompatActivity implements LocationSource,
        AMapLocationListener, AMap.OnMapTouchListener, View.OnClickListener, TextWatcher,
        Inputtips.InputtipsListener, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, PoiSearch.OnPoiSearchListener {
    private String keyWord = "";// 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;// 搜索时进度条

    private PoiResult poiResult; // poi返回的结果 POI（Point Of Interest，兴趣点）搜索结果分页显示
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类 此类定义了搜索的关键字，类别及城市。
    private PoiSearch poiSearch;// POI搜索 POI（Point Of Interest，兴趣点）搜索
    private MarkerOptions markerOption; //标记点的配置
    private LatLng latLng = new LatLng(87.593784,43.851112);//存储经纬度坐标值
    Marker marker;
    //自定义定位小蓝点的Marker

    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private AutoCompleteTextView searchText;// 输入搜索关键字
    private EditText editCity;// 要输入的城市名字或者城市区号
    boolean useMoveToLocationWithMapMode = true;
    private String locationaddress;
    //自定义定位小蓝点的Marker
    Marker locationMarker;

    //坐标和经纬度转换工具
    Projection projection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twolocation);
        Logger.addLogAdapter(new AndroidLogAdapter());

        //mapView = new MapView(this);


        //RelativeLayout layout = new RelativeLayout(this);
        //layout.addView(mapView);


        TextView textView = new TextView(this);
        textView.setText("自定义效果\n" +
                " 1、定位成功后， 小蓝点和和地图一起移动到定位点\n" +
                " 2、手势操作地图后模式修改为 仅定位不移动到中心点");
        //layout.addView(textView);

        //setContentView(layout);

        mapView = (MapView) findViewById(R.id.map);//找到地图控件
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }
    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        aMap.setOnMapTouchListener(this);

        Button searButton = (Button) findViewById(R.id.searchButton);
        searButton.setOnClickListener(this);
      // Button nextButton = (Button) findViewById(R.id.nextButton);
//        nextButton.setOnClickListener(this);
        searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
        searchText.addTextChangedListener(this);// 添加文本输入框监听事件
       // editCity = (EditText) findViewById(R.id.city);
       // editCity.setOnClickListener(this);
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

                Logger.d("hello ***************************" + amapLocation.getAddress() +
                        "#####" + amapLocation.getLatitude());

                locationaddress = amapLocation.getAddress();
                searchText.setText(amapLocation.getAddress());
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
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
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
            case R.id.searchButton:

                //(当前Activity，目标Activity)
                // intent.setClass(LoginActivity.this, Activityname1.class);
                //intent.setClass(LoginActivity.this, PoiKeywordSearchActivity.class);
                //intent.setClass(LoginActivity.this, CustomLocationActivity.class);
                //intent.setClass(LoginActivity.this, CustomLocationModeActivity.class);
                intent.setClass(this, mainActivity.class);
                intent.putExtra("location", keyWord);
                Logger.d("locationaddress*******************"+keyWord);
                startActivity(intent);
                //finish();

                break;
            /**
             * 点击下一页按钮
             */
            case R.id.nextButton:
                //nextButton();
                break;
            case R.id.city:
              //  Intent intent = new Intent();
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
            default:
                break;
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
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showProgressDialog();// 显示进度框
        currentPage = 0;
       // Logger.d("editCity.getText().toString()****" + editCity.getText().toString());
        query = new PoiSearch.Query(keyWord, "", "乌鲁木齐");
        //query = new PoiSearch.Query(keyWord, "", editCity.getText().toString());//
        // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {

        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
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
                Logger.d("tipList.get(i).getName()****"+i +"###"+ tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.route_inputs, listString);
            searchText.setAdapter(aAdapter);
            aAdapter.notifyDataSetChanged();

//开始搜索
            keyWord = AMapUtil.checkEditText(searchText);// 取关键字
            if ("".equals(keyWord)) {
                ToastUtil.show(this, "请输入搜索关键字");
                return;}
            else if(locationaddress.equals(keyWord) ){
                return;

            } else if(listString.contains(keyWord)) {
                Logger.d("keyWord的值:" + keyWord);
                System.out.println("keyWord的值:" + keyWord);
                doSearchQuery();
            }else{
                return;
            }

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

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        dissmissProgressDialog();// 隐藏对话框
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {// 搜索poi的结果
                if (result.getQuery().equals(query)) {// 是否是同一条
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();// 清理之前的图标
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);//poi图层
                        poiOverlay.removeFromMap();//清理图层
                        poiOverlay.addToMap();//在图层上添加maker

                        poiOverlay.zoomToSpan();//移动镜头到当前的视角

                    } else if (suggestionCities != null
                            && suggestionCities.size() > 0) {
                        showSuggestCity(suggestionCities);
                    } else {
                        ToastUtil.show(this,
                                R.string.no_result);
                    }

                }
            } else {
                ToastUtil.show(this,
                        R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
        Logger.d("开始执行 addMarkersToMap();*****" );
        addMarkersToMap();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        Logger.d("latLng*****"+latLng.latitude+"###"+latLng.longitude );
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable
                .ic_index_sender))
                .position(latLng)//位置
                .draggable(true);//设置Marker覆盖物是否可拖拽。
        marker = aMap.addMarker(markerOption);
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
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        ToastUtil.show(this, infomation);

    }
}
