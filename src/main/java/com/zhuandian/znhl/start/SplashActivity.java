package com.zhuandian.znhl.start;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.zhuandian.znhl.MainActivity;
import com.zhuandian.znhl.MyApplication.MyApplication;
import com.zhuandian.znhl.R;
import com.zhuandian.znhl.myUtils.GlobalVariable;


/**
 * Created by 谢栋 on 2016/5/21.
 */
public class SplashActivity  extends Activity {


//    private  String url = "http://www.baidu.com";



//    public static String TAG = "LocTestDemo";

    private BroadcastReceiver broadcastReceiver;
    public static String LOCATION_BCR = "location_bcr";

    private SharedPreferences sharedPreferences;

    private Intent intent = null;
    private final int SPLASH_DISPLAY_LENGHT = 5000; //延迟5秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置为全屏
        initialize();
//
        initializeViews();
//        locInfo = (TextView) findViewById(R.id.info);
//        locInfo.setText("wod ");
//
        MyApplication.getInstance().requestLocationInfo();
        //加入loding界面的线程
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);
////



}




    private void initialize()
    {
        registerBroadCastReceiver();
    }

    private void initializeViews()
    {
//		locBtn = (Button) findViewById(R.id.location);
//        locInfo = (TextView) findViewById(R.id.info);
    }

    private void initializeListeners()//手势动作监听
    {
//        locBtn.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
////                locInfo.setText("定位中...");
//
//                MyApplication.getInstance().requestLocationInfo();
//            }
//        });
    }

    /**
     * 注册一个广播，监听定位结果
     */
    private void registerBroadCastReceiver()
    {
        broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String address = intent.getStringExtra("address");
                //toast中的第一个参数是设置要显示在哪个界面的上下文中
//                Toast toast=Toast.makeText(SplashActivity.this, address, Toast.LENGTH_LONG);
//                toast.show();
                GlobalVariable.LocationInfo=address;
                Log.d("xiedong", GlobalVariable.LocationInfo + "  ");
                //	locInfo.setText(address+"啊哈哈，这里就是地址的字符串");
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(LOCATION_BCR);
        registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }





}
