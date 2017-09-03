package com.zhuandian.znhl.control;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhuandian.znhl.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by 谢栋 on 2016/6/28.
 */
public class VideoControl extends Activity {

    private WebView webView;
    private SeekBar xSeekBar;
    private SeekBar ySeekBar;
    private TextView xTextView;
    private TextView yTextView;
    private int xStr=90;
    private int yStr=90;
    private DatagramSocket ds=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        ((TextView) findViewById(R.id.toolbar_title)).setText("摄像监控");  //设置toolbar标题

        //设置左上角按钮并绑定监听事件
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置左上角按钮并绑定点击事件
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



//        new Thread(){
//
//            @Override
//            public void run() {
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setInitialScale(55);

        WebSettings setting = webView.getSettings();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().supportMultipleWindows();
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setDrawingCacheEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);//水平不显示滚动条
        webView.setVerticalScrollBarEnabled(false); //垂直不显示滚动条
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);// 设置渲染优先级
        webView.requestFocusFromTouch();
        webView.setLongClickable(true);
        webView.setKeepScreenOn(true);
        //webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);//Ĭ������ΪԶ
        //webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//��ҳ����Ӧ��Ļ���
        webView.getSettings().setUseWideViewPort(true);//任意比例缩放
        webView.getSettings().setLoadWithOverviewMode(true);//自适应屏幕比例
        //webView.setInitialScale(100);//初始缩放比例
        webView.getSettings().setAllowFileAccess(true);
//		webView.getSettings().setAllowFileAccessFromFileURLs(true);
//		webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// 设置缓存模式
        webView.getSettings().setAppCacheEnabled(true);// 启用缓存
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 100);// 设置最大缓存容量---100M
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        setting.setJavaScriptEnabled(true);
//        setting.setBuiltInZoomControls(true);
        webView.loadUrl("http://192.168.1.89");
        webView.setWebChromeClient(new WebChromeClient());




//
//
//            }
//        }.start();



        //初始化摄像头状态
        sendInfo("Position:X:90,Y:90");


        videoXYControl();
    }

    /**
     * 摄像头的X，Y坐标的控制
     */
    private void videoXYControl() {


        xTextView= (TextView) findViewById(R.id.xpercent);
        yTextView= (TextView) findViewById(R.id.ypercent);
        xSeekBar= (SeekBar) findViewById(R.id.x);
        xSeekBar.setMax(180);  //最大值设置为180
        xSeekBar.setProgress(90);   //设置当前值为中间
        xSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                xTextView.setText(progress+"%");
        }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                xStr= seekBar.getProgress();
                Log.v("dddd-->", xStr + "");
                sendInfo("Position:X:"+xStr+",Y:"+yStr);
//                ds.close();

            }
        });


        ySeekBar= (SeekBar) findViewById(R.id.y);
        ySeekBar.setMax(180);   //设置y坐标的最大值为180
        ySeekBar.setProgress(90);    //设置y轴的当前值为90
        ySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                yTextView.setText(progress+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                yStr= seekBar.getProgress();
                Log.v("dddd-->", yStr + "");
//                sendInfo("Position:Y:"+yStr);
                sendInfo("Position:X:"+xStr+",Y:"+yStr);
//                ds.close();

            }
        });

    }



    private void sendInfo(final String info) {

        Log.v("xiedong", "info来啦" + info);
        new Thread() {

            @Override
            public void run() {


                try {
                    ds = new DatagramSocket(8086);


                    //建立字符输入流
                    BufferedReader bufr = new BufferedReader(new InputStreamReader(System.in));
                    String line = null;


                    Thread.sleep(500);
//                            if (flag == 1) {
//
//                                flag = 2;
                    byte buf[] = info.getBytes();

                    //把得到的数据封装进数据包中
                    DatagramPacket dp =
                            new DatagramPacket(buf, buf.length, InetAddress.getByName("192.168.1.2"), 8086);
                    //通过socket发送出去
                    Log.v("aaaa", "进来啦");
                    ds.send(dp);

                    //从服务端接收先关闭掉，socket为阻塞式操作，给服务端发送了数据，但是一直得不到服务端回馈的数据，所以该方法一直
                    //处于阻塞状态，所以利用语音发送的数据一直得不到响应

//                        Log.v("aaaa", "发出去啦");
//
//
//                        byte buf2[] = new byte[1024];
//
//                        //建立接受端的数据包，并且指定接收端的数据包
//                        DatagramPacket dp1 = new DatagramPacket(buf2, buf2.length);
//
//                        //利用socket的receive方法，接受数据
//                        ds.receive(dp1);
//
//                        str = new String(dp1.getData(), 0, dp1.getLength());
//                        Log.v("info2", str);


//                        //利用Handler把消息发送到主线程
//                        Message msg = new Message();
//                        msg.what = GET_INFO_TYPE0;
//                        msg.obj = str;
//                        handler.sendMessage(msg);
                    ds.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }
}
