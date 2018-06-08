package com.example.hzy.lovenum;

/**
 * Created by hzy on 2018/6/7.
 */

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.Toast;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
public class SearchViewDemo extends Activity {

    private SearchView               searchView;
    private Context                  context;
    private MyHandler                handler;

    // schedule executor
    private ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(10);
    private String                   currentSearchTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_view_demo);
        context = getApplicationContext();
        //handler = new MyHandler(this,this);
        Logger.addLogAdapter(new AndroidLogAdapter());
        int a=1;

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        setTitle(" ");
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customActionBarView = inflater.inflate(R.layout.search_view_demo_title, null);

        searchView = (SearchView)customActionBarView.findViewById(R.id.search_view);
        searchView.setIconified(false);
        searchView.setOnCloseListener(new OnCloseListener() {

            @Override
            public boolean onClose() {
                // to avoid click x button and the edittext hidden
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(context, "begin search", Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                if (newText != null && newText.length() > 0) {
                    currentSearchTip = newText;
                    showSearchTip(newText,SearchViewDemo.this,handler);
                }
                return true;
            }
        });
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL
                | Gravity.RIGHT);
        actionBar.setCustomView(customActionBarView, params);

        // show keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
    Context context1;
    MyHandler handler1;
    public void showSearchTip(String newText,Context context,MyHandler handler) {
        handler1=handler;
        context1=context;
        // excute after 500ms, and when excute, judge current search tip and newText
        currentSearchTip = newText;
        schedule(new SearchTipThread(newText), 3000);
    }


    class SearchTipThread implements Runnable {

        String newText;
        MyHandler handle1;

        public SearchTipThread(String newText){
            this.newText = newText;
        }
        public SearchTipThread(MyHandler handle){
            this.handle1 = handle;
        }

        public void run() {
            // keep only one thread to load current search tip, u can get data from network here
            Logger.d("newText :"+newText+";currentSearchTip :"+currentSearchTip);
            //if (newText != null && newText.equals(currentSearchTip)) {
            //handler=new MyHandler();
              //  handler.sendMessage(handler.obtainMessage(1, newText + " search tip"));
           // handler1.removeCallbacks();
            handle1.sendMessage(handle1.obtainMessage(1, newText + " search tip"));
           // }
        }
    }

    public ScheduledFuture<?> schedule(Runnable command, long delayTimeMills) {
        Logger.d("delayTimeMills"+delayTimeMills);
        //if (scheduledExecutor.schedule())
       // scheduledExecutor.shutdownNow();
        //scheduledExecutor.
        //scheduledExecutor.shutdown();
       // scheduledExecutor.schedule(command, delayTimeMills, TimeUnit.MILLISECONDS).cancel(true);
        return scheduledExecutor.schedule(command, delayTimeMills, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
        }
        return false;
    }

    public class MyHandler extends Handler {
       //int a=1;
        Context context2;
        twolocationActivity two1;
        public MyHandler(Context context1,twolocationActivity two) {
            context2=context1;
            two1=two;

        }

        @Override
        public void handleMessage(Message msg) {
            Logger.d("msg"+msg.what);
            //a=msg.what;
            switch (msg.what) {
                case 1:

                    //Toast.makeText(context2, (String)msg.obj, Toast.LENGTH_SHORT).show();
                    two1.doSearchQuery();
                    break;
            }
        }

    }

}
