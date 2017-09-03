package com.zhuandian.znhl.other;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.zhuandian.znhl.R;
import com.zhuandian.znhl.myUtils.GlobalVariable;

/**
 * Created by 谢栋 on 2016/7/1.
 */
public class OurWeb extends Activity{

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ourweb);

        ((TextView) findViewById(R.id.toolbar_title)).setText("官方网站");  //设置toolbar标题

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

        webView= (WebView) findViewById(R.id.ourweb);
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

//        setting.setBuiltInZoomControls(true);
        webView.loadUrl(GlobalVariable.OURWEB);
        webView.setWebChromeClient(new WebChromeClient());


    }
}
