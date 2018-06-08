package com.example.hzy.lovenum;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.dingwei.util.AMapUtil;
import com.dingwei.util.ToastUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import overlay.PoiOverlay;

/**
 * AMapV1地图中简单介绍poisearch搜索
 */
public class OrderActivity extends Activity implements
        OnMarkerClickListener, InfoWindowAdapter, TextWatcher,
		OnPoiSearchListener, OnClickListener, InputtipsListener,LocationSource,AMapLocationListener, AMap.OnMapTouchListener {
	private AMap aMap;
	private MapView mapView;
	private AutoCompleteTextView searchText;// 输入搜索关键字
	private String keyWord = "";// 要输入的poi搜索关键字
	private ProgressDialog progDialog = null;// 搜索时进度条
	private EditText editCity;// 要输入的城市名字或者城市区号
	private PoiResult poiResult; // poi返回的结果
	private int currentPage = 0;// 当前页面，从0开始计数
	private PoiSearch.Query query;// Poi查询条件类
	private PoiSearch poiSearch;// POI搜索
    //
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    boolean useMoveToLocationWithMapMode = true;

    //自定义定位小蓝点的Marker
    Marker locationMarker;

    //坐标和经纬度转换工具
    Projection projection;
    //

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Logger.d("hello ***************************");
        //
        mapView = new MapView(this);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        //
		setContentView(R.layout.order_activity);
		init();
	}


	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}


	/**
	 * 设置页面监听
	 */
	private void setUpMap() {
		Button searButton = (Button) findViewById(R.id.searchButton);
		searButton.setOnClickListener(this);
		Button nextButton = (Button) findViewById(R.id.nextButton);
		nextButton.setOnClickListener(this);
		searchText = (AutoCompleteTextView) findViewById(R.id.keyWord);
		searchText.addTextChangedListener(this);// 添加文本输入框监听事件
		editCity = (EditText) findViewById(R.id.city);
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		//
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

		aMap.setOnMapTouchListener(this);
		//
	}
//
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


    OrderActivity.MyCancelCallback myCancelCallback = new OrderActivity.MyCancelCallback();

    @Override
    public void onTouch(MotionEvent motionEvent) {
        Log.i("amap","onTouch 关闭地图和小蓝点一起移动的模式");
        useMoveToLocationWithMapMode = false;
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
            mLocationOption.setInterval(2000);
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
    //
	/**
	 * 点击搜索按钮
	 */
	public void searchButton() {
		keyWord = AMapUtil.checkEditText(searchText);// 取关键字
		if ("".equals(keyWord)) {
			ToastUtil.show(OrderActivity.this, "请输入搜索关键字");
			return;
		} else {
			Logger.d("keyWord的值:"+keyWord);
			System.out.println("keyWord的值:"+keyWord);
			doSearchQuery();
		}
	}

	/**
	 * 点击下一页按钮
	 */
	public void nextButton() {
		if (query != null && poiSearch != null && poiResult != null) {
			if (poiResult.getPageCount() - 1 > currentPage) {
				currentPage++;
				query.setPageNum(currentPage);// 设置查后一页
				poiSearch.searchPOIAsyn();
			} else {
				ToastUtil.show(OrderActivity.this,
						R.string.no_result);
			}
		}
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

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery() {
		showProgressDialog();// 显示进度框
		currentPage = 0;
		query = new PoiSearch.Query(keyWord, "", editCity.getText().toString());// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		return false;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(final Marker marker) {
		View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
				null);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(marker.getTitle());

		TextView snippet = (TextView) view.findViewById(R.id.snippet);
		snippet.setText(marker.getSnippet());
		ImageButton button = (ImageButton) view
				.findViewById(R.id.start_amap_app);
		// 调起高德地图app
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startAMapNavi(marker);
			}
		});
		return view;
	}

	/**
	 * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
	 */
	public void startAMapNavi(Marker marker) {
		// 构造导航参数
		NaviPara naviPara = new NaviPara();
		// 设置终点位置
		naviPara.setTargetPoint(marker.getPosition());
		// 设置导航策略，这里是避免拥堵
		naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

		// 调起高德地图导航
		try {
			AMapUtils.openAMapNavi(naviPara, getApplicationContext());
		} catch (com.amap.api.maps.AMapException e) {

			// 如果没安装会进入异常，调起下载页面
			AMapUtils.getLatestAMapApp(getApplicationContext());

		}

	}

	/**
	 * 判断高德地图app是否已经安装
	 */
	public boolean getAppIn() {
		PackageInfo packageInfo = null;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(
					"com.autonavi.minimap", 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		// 本手机没有安装高德地图app
		if (packageInfo != null) {
			return true;
		}
		// 本手机成功安装有高德地图app
		else {
			return false;
		}
	}

	/**
	 * 获取当前app的应用名字
	 */
	public String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					getPackageName(), 0);
		} catch (NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
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
		ToastUtil.show(OrderActivity.this, infomation);

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
		if (!AMapUtil.IsEmptyOrNullString(newText)) {
		    InputtipsQuery inputquery = new InputtipsQuery(newText, editCity.getText().toString());
		    Inputtips inputTips = new Inputtips(OrderActivity.this, inputquery);
		    inputTips.setInputtipsListener(this);
		    inputTips.requestInputtipsAsyn();
		}
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
						PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						ToastUtil.show(OrderActivity.this,
								R.string.no_result);
					}
				}
			} else {
				ToastUtil.show(OrderActivity.this,
						R.string.no_result);
			}
		} else {
			ToastUtil.showerror(this, rCode);
		}

	}
	
	@Override
	public void onPoiItemSearched(PoiItem item, int rCode) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Button点击事件回调方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 点击搜索按钮
		 */
		case R.id.searchButton:
			searchButton();
			break;
		/**
		 * 点击下一页按钮
		 */
		case R.id.nextButton:
			nextButton();
			break;
		default:
			break;
		}
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
			searchText.setAdapter(aAdapter);
			aAdapter.notifyDataSetChanged();
		} else {
			ToastUtil.showerror(this, rCode);
		}
		
	}


}
