package com.zhuandian.znhl.other;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuandian.znhl.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


/**
 * Created by 谢栋 on 2016/6/26.
 */
public class Music extends Activity implements View.OnClickListener {

    private DatagramSocket ds=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music);

        ((TextView) findViewById(R.id.toolbar_title)).setText("家庭音乐");  //设置toolbar标题

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



        initView();
    }

    private void initView() {
          findViewById(R.id.play).setOnClickListener(this);
          findViewById(R.id.up_song).setOnClickListener(this);
          findViewById(R.id.next_song).setOnClickListener(this);
          findViewById(R.id.add_volume).setOnClickListener(this);
          findViewById(R.id.decrease_volune).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.play :
                Toast.makeText(this, "播放或暂停音乐", Toast.LENGTH_SHORT).show();
                sendInfo("Music:2");
            break;
//           case  R.id.stop :
//               break;
            case R.id.add_volume :
                Toast.makeText(this,"音量增加",Toast.LENGTH_SHORT).show();
                sendInfo("Music:5");
                break;
            case R.id.decrease_volune :
                Toast.makeText(this,"音量减小",Toast.LENGTH_SHORT).show();
                sendInfo("Music:6");
                break;
            case R.id.next_song :
                Toast.makeText(this,"播放下一首",Toast.LENGTH_SHORT).show();
                sendInfo("Music:4");
                break;
            case R.id.up_song :
                Toast.makeText(this,"播放上一首",Toast.LENGTH_SHORT).show();
                sendInfo("Music:3");
                break;
//1开机关机

        }

    }

    private void sendInfo(final String info) {
        new Thread()
        {
            @Override
            public void run() {
                try {
                    ds = new DatagramSocket(8086);
                    //建立字符输入流
                    BufferedReader bufr = new BufferedReader(new InputStreamReader(System.in));
                    String line = null;
                    while (true) {

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


                        ds.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }.start();
    }
}
