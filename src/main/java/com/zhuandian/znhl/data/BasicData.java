package com.zhuandian.znhl.data;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class BasicData extends Activity {

    private TextView text;
    private  TextView wendu,shidu,co2,youdu,huojing,weizhi1,weizhi2;

    private int GET_INFO_TYPE0=7;
    private int GET_INFO_TYPE1=8;
    private int ADDINFOOK=9;
    private String strTYPE0;
    private String strTYPE1;
    String  right_info="";
    private int flag=1;

    private String P="0";
    private DatagramSocket ds = null;


    private Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            Log.v("info5","收到handle的消息啦");
            //TYPE:0,Ttemperature:0.000000,CO2:0.000000,H:0.000000,I:0.000000,Sum:0.000000
            //TYPE:1,W:0,D:0,P:0,F:0,Sum:0.000000

//        //测试正则
//        String regex1="TYPE:32";
//        String names=regex1.replaceAll("[A-Za-z]+:(\\d+.\\d+)", "$1");


//            String[] info=data.split(",");
//            Log.v("size",data.length+"");


            if(msg.what==ADDINFOOK)
            {

                //TYPE:0,Ttemperature:0.000000,CO2:0.000000,H:0.000000,I:0.000000,Sum:0.000000,TYPE:1,W:0,D:0,P:0,F:0,Sum:0.000000
                //TYPE:1,W:0,D:0,P:0,F:0,Sum:0.000000

                String data=(String)msg.obj;
                String[] info=data.split(",");

                wendu.setText("温度\n"+info[1].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
                co2.setText("CO2\n"+info[2].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
                shidu.setText("湿度\n" + info[3].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
                youdu.setText("光强\n"+info[4].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
//                co2.setText("CO2\n"+info[1].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
//                co2.setText("CO2\n"+info[1].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
                weizhi1.setText("毒气\n"+""+info[9].replaceAll("[A-Za-z]+:(\\d+)", "$1"));
                weizhi2.setText("火警\n"+""+info[10].replaceAll("[A-Za-z]+:(\\d+)", "$1"));
                Log.v("xiedong", "type0"+data);

            }
//            if(msg.what==GET_INFO_TYPE0)
//            {
//                String data=(String)msg.obj;
//                String[] info=data.split(",");
//                wendu.setText("温度\n"+info[1].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
//                co2.setText("CO2\n"+info[2].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
//                shidu.setText("湿度\n" + info[3].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
//                youdu.setText("光强\n"+info[4].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
////                co2.setText("CO2\n"+info[1].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
////                co2.setText("CO2\n"+info[1].replaceAll("[A-Za-z]+:(\\d+).\\d+", "$1"));
//                Log.v("xiedong", "type0"+data);
//
//            }
////有毒有害气体未处理
//            if(msg.what==GET_INFO_TYPE1)
//            {
//                String data=(String)msg.obj;
//                String[] info=data.split(",");
//                Log.v("xiedong","type1"+data);
//                weizhi1.setText("毒气\n"+""+info[3].replaceAll("[A-Za-z]+:(\\d+)", "$1"));
//                weizhi2.setText("火警\n"+""+info[4].replaceAll("[A-Za-z]+:(\\d+)", "$1"));
//
//            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_data);
        ((TextView) findViewById(R.id.toolbar_title)).setText("基本数据");  //设置toolbar标题

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



        sendInfo();


        findViewById(R.id.shuaxin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sendInfo();
                Toast.makeText(BasicData.this,"正在刷新数据...",Toast.LENGTH_SHORT).show();
            }
        });


 }

    private void initView() {
        co2= (TextView) findViewById(R.id.co2);
        youdu= (TextView) findViewById(R.id.youdu);
        shidu= (TextView) findViewById(R.id.shidu);
        huojing= (TextView) findViewById(R.id.huojing);
        wendu= (TextView) findViewById(R.id.wendu);
        weizhi1= (TextView) findViewById(R.id.weizhi1);
        weizhi2= (TextView) findViewById(R.id.weizhi2);


    }

    private void sendInfo() {

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
                    if (flag == 1) {

                        flag = 2;
                        byte buf[] = "INFO0?".getBytes();

                        //把得到的数据封装进数据包中
                        DatagramPacket dp =
                                new DatagramPacket(buf, buf.length, InetAddress.getByName("192.168.1.2"), 8086);
                        //通过socket发送出去
                        Log.v("aaaa", "进来啦");
                        ds.send(dp);


                        Log.v("aaaa", "发出去啦");


                        byte buf2[] = new byte[1024];

                        //建立接受端的数据包，并且指定接收端的数据包
                        DatagramPacket dp1 = new DatagramPacket(buf2, buf2.length);

                        //利用socket的receive方法，接受数据
                        ds.receive(dp1);

//                        ds.close();
                        strTYPE0 = new String(dp1.getData(), 0, dp1.getLength());
                        Log.v("info2", strTYPE0);
                        if (!(strTYPE0.contains("END_DATA!"))) {

                            right_info = right_info + strTYPE0;
                            Log.v("info1", right_info);

                        }

                        if (right_info.contains("END")) {
                            Log.v("info3", right_info);
//                            parseType0Str(right_info);


                        }


                        if(strTYPE0.contains("um")) {
                            //利用Handler把消息发送到主线程
                            Message msg = new Message();
                            msg.what = GET_INFO_TYPE0;
                            msg.obj = strTYPE0;

                            handler.sendMessage(msg);
                        }
                    }
                    else if (flag == 2) {
                        flag=1;

                        byte buf[] = "INFO1?".getBytes();

                        //把得到的数据封装进数据包中
                        DatagramPacket dp =
                                new DatagramPacket(buf, buf.length, InetAddress.getByName("192.168.1.2"), 8086);
                        //通过socket发送出去
                        Log.v("aaaa", "进来啦");

                        ds.send(dp);
                        Log.v("aaaa", "发出去啦");


                        byte buf2[] = new byte[1024];

                        //建立接受端的数据包，并且指定接收端的数据包
                        DatagramPacket dp1 = new DatagramPacket(buf2, buf2.length);

                        //利用socket的receive方法，接受数据
                        ds.receive(dp1);
//                        ds.close();

                        strTYPE1 = new String(dp1.getData(), 0, dp1.getLength());
                        Log.v("info2", strTYPE1);
                        if (!(strTYPE1.contains("END_DATA!"))) {

                            right_info = right_info + strTYPE1;
                            Log.v("info1", right_info);

                        }

                        if (right_info.contains("END")) {
                            Log.v("info3", right_info);
//                            parseType0Str(right_info);


                        }


                        if(strTYPE1.contains("um")) {
                            //利用Handler把消息发送到主线程
                            Message msg = new Message();
                            msg.what = GET_INFO_TYPE1;
                            msg.obj = strTYPE1;
                            handler.sendMessage(msg);
                        }


                        String addInfo=strTYPE0+","+strTYPE1;
                        if(addInfo.contains("F")) {
                            Message msg = new Message();
                            msg.what = ADDINFOOK;
                            msg.obj = addInfo;
                            handler.sendMessage(msg);
                        }
                    }

//
//
////                        Log.v("aaaa", "发出去啦");
////
////
////                        byte buf2[] = new byte[1024];
////
////                        //建立接受端的数据包，并且指定接收端的数据包
////                        DatagramPacket dp1 = new DatagramPacket(buf2, buf2.length);
////
////                        //利用socket的receive方法，接受数据
////                        ds.receive(dp1);
////
////                        str = new String(dp1.getData(), 0, dp1.getLength());
////                        Log.v("info2",str);
////                        if(!(str.contains("END_DATA!")))
////                        {
////
////                              right_info=right_info+str;
////                              Log.v("info1",right_info);
////
////                        }
////
////                        if(right_info.contains("END")){
////                            Log.v("info3",right_info);
//////                            parseType0Str(right_info);
////
////
////                        }
////
////
////                        //利用Handler把消息发送到主线程
////                        Message msg= new Message();
////                        msg.what=GET_INFO_TYPE0;
////                        msg.obj=str;
////                        handler.sendMessage(msg);
//
                   }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    @Override
    public void finish() {
        ds.close();
       super.finish();
    }
}
